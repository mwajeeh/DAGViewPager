package com.example.mwajeeh.dagviewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commonsware.cwac.pager.SimplePageDescriptor;
import com.example.mwajeeh.dagviewpager.flowFragments.AgeTriage;
import com.example.mwajeeh.dagviewpager.flowFragments.DateOfBirthFragment;
import com.example.mwajeeh.dagviewpager.flowFragments.EyeColor;
import com.example.mwajeeh.dagviewpager.flowFragments.FemaleQuestion;
import com.example.mwajeeh.dagviewpager.flowFragments.GenderFragment;
import com.example.mwajeeh.dagviewpager.flowFragments.HairColor;
import com.example.mwajeeh.dagviewpager.flowFragments.MaleQuestion;
import com.example.mwajeeh.dagviewpager.flowFragments.Summary;
import com.example.mwajeeh.dagviewpager.flowFragments.WelcomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAGViewPagerFragment extends Fragment implements FlowActivity.BackPressListener {

    private static final String FLOW_PARAMS = "flow_params";

    private DynamicArgsPagerAdapter adapter;
    private DirectedAcyclicGraph<Node> graph;
    private Map<String, Node> map;
    private Node root;
    private Bundle flowParams = new Bundle();
    private Bundle flattenedFlowParams = new Bundle();
    private ViewPager pager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //build flow params before invoking super so that child fragments can access it during onCreate
        if (savedInstanceState != null) {
            final Bundle bundle = savedInstanceState.getBundle(FLOW_PARAMS);
            if (bundle != null) {
                flowParams.putAll(bundle);
            }
        }
        rebuildFlattenedFlowParams();
        super.onCreate(savedInstanceState);
        buildFlow();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new DynamicArgsPagerAdapter(getContext(), getChildFragmentManager());
        pager.setAdapter(adapter);
        show(root);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(FLOW_PARAMS, flowParams);
    }

    public void next(String currentNode) {
        next(map.get(currentNode));
    }

    private void next(Node current) {
        List<Node> outgoingEdges = graph.getOutgoingEdges(current);
        if (ListUtils.isEmpty(outgoingEdges)) {
            //we are at the end
            return;
        }
        int currentItem = pager.getCurrentItem();

        //lazily trim adapter as we move forward
        boolean removed = false;
        for (int i = adapter.getCount() - 1; i > currentItem; i--) {
            String nodeTag = adapter.getPageDescriptor(i).getFragmentTag();
            adapter.remove(i);
            //remove associated flow data
            flowParams.remove(nodeTag);
            removed = true;
        }
        if (removed) {
            rebuildFlattenedFlowParams();
        }

        //move forward
        for (Node edge : outgoingEdges) {
            if (edge.select(flattenedFlowParams)) {
                show(edge);
                break;
            }
        }
    }

    private void rebuildFlattenedFlowParams() {
        flattenedFlowParams.clear();
        for (String key : flowParams.keySet()) {
            flattenedFlowParams.putAll(flowParams.getBundle(key));
        }
    }

    private void show(Node node) {
        adapter.add(node.descriptor);
        pager.setCurrentItem(adapter.getCount() - 1);
    }

    private void buildFlow() {
        //welcome screen
        root = new Node(getPageDescriptor(WelcomeFragment.class.getName(), "Welcome"), Selectors.ALWAYS_SELECT);

        //Age screen
        Node age = new Node(getPageDescriptor(DateOfBirthFragment.class.getName(), "Age"), Selectors.ALWAYS_SELECT);

        //AgeTriage screen
        Node ageTriage = new Node(getPageDescriptor(AgeTriage.class.getName(), "Age"), selectIfKid());

        //Gender screen
        Node gender = new Node(getPageDescriptor(GenderFragment.class.getName(), "Gender"), selectIfAdult());

        //Condition screen
        Node maleQuestion = new Node(getPageDescriptor(MaleQuestion.class.getName(), "Married"), selectIfMale());
        Node femaleQuestion = new Node(getPageDescriptor(FemaleQuestion.class.getName(), "Pregnant"), selectIfFemale());

        Node hairColor = new Node(getPageDescriptor(HairColor.class.getName(), "Hair Color"), Selectors.ALWAYS_SELECT);
        Node eyeColor = new Node(getPageDescriptor(EyeColor.class.getName(), "Eye Color"), selectIfBlondFemale());
        Node summary = new Node(getPageDescriptor(Summary.class.getName(), "Summary"), summarySelector());

        graph = new DirectedAcyclicGraph<>();
        graph.addNode(root);
        graph.addNode(age);
        graph.addNode(ageTriage);
        graph.addNode(gender);
        graph.addNode(maleQuestion);
        graph.addNode(femaleQuestion);
        graph.addNode(hairColor);
        graph.addNode(eyeColor);
        graph.addNode(summary);

        addEdge(root, age);

        addEdge(age, ageTriage);
        addEdge(age, gender);

        addEdge(gender, maleQuestion);
        addEdge(gender, femaleQuestion);

        addEdge(maleQuestion, hairColor);
        addEdge(femaleQuestion, hairColor);

        addEdge(hairColor, eyeColor);
        addEdge(hairColor, summary);

        addEdge(eyeColor, summary);

        //fill map for indexing purposes
        map = new HashMap<>();
        ArrayList<Node> sortedList = graph.getSortedList();
        for (Node node : sortedList) {
            map.put(node.getTag(), node);
        }
    }

    private void addEdge(Node from, Node to) {
        graph.addEdge(to, from);
    }

    private NodeSelector selectIfKid() {
        return new NodeSelector() {
            @Override
            public boolean select(Bundle args) {
                return args.getInt("Your Age") < 16;
            }
        };
    }

    private NodeSelector selectIfAdult() {
        return new NodeSelector() {
            @Override
            public boolean select(Bundle args) {
                return args.getInt("Your Age") >= 16;
            }
        };
    }

    private NodeSelector selectIfMale() {
        return new NodeSelector() {
            @Override
            public boolean select(Bundle args) {
                return "male".equalsIgnoreCase(args.getString("Gender"));
            }
        };
    }

    private NodeSelector selectIfFemale() {
        return new NodeSelector() {
            @Override
            public boolean select(Bundle args) {
                return "female".equalsIgnoreCase(args.getString("Gender"));
            }
        };
    }

    private NodeSelector selectIfBlond() {
        return new NodeSelector() {
            @Override
            public boolean select(Bundle args) {
                return "blond".equalsIgnoreCase(args.getString("Hair Color"));
            }
        };
    }

    private NodeSelector selectIfBlondFemale() {
        return new Selectors.ConditionalNodeSelector(
                selectIfFemale())
                .and(selectIfBlond());
    }

    private NodeSelector summarySelector() {
        return new NodeSelector() {
            @Override
            public boolean select(Bundle args) {
                boolean blondFemale = selectIfBlondFemale().select(args);
                return !blondFemale || args.containsKey("Eye Color");
            }
        };
    }

    @NonNull
    public static ArgsPageDescriptor getPageDescriptor(String tag, String title) {
        SimplePageDescriptor simplePageDescriptor = new SimplePageDescriptor(tag, title);
        return new ArgsPageDescriptor(simplePageDescriptor, null);
    }

    @NonNull
    public static ArgsPageDescriptor getPageDescriptor(String tag, String title, Bundle args) {
        SimplePageDescriptor simplePageDescriptor = new SimplePageDescriptor(tag, title);
        return new ArgsPageDescriptor(simplePageDescriptor, args);
    }

    @Override
    public boolean onBackPressed() {
        int currentItem = pager.getCurrentItem();
        if (currentItem - 1 >= 0) {
            pager.setCurrentItem(currentItem - 1);
            return true;
        }
        return false;
    }

    public void addFlowParams(String nodeTag, Bundle args) {
        flowParams.putBundle(nodeTag, args);
        flattenedFlowParams.putAll(args);
    }

    public Bundle getAllFlowParams() {
        return flowParams;
    }
}
