package com.example.mwajeeh.dagviewpager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Selectors {

    public static final NodeSelector ALWAYS_SELECT = new NodeSelector() {
        @Override
        public boolean select(Bundle args) {
            return true;
        }
    };

    public static class ConditionalNodeSelector implements NodeSelector {
        private final NodeSelector root;
        private List<NodeSelectorWithOp> selectors = new ArrayList<>();

        public ConditionalNodeSelector(NodeSelector selector) {
            this.root = selector;
        }

        public ConditionalNodeSelector and(NodeSelector selector) {
            selectors.add(new NodeSelectorWithOp(selector, AndOp.AND));
            return this;
        }

        public ConditionalNodeSelector or(NodeSelector selector) {
            selectors.add(new NodeSelectorWithOp(selector, OrOp.OR));
            return this;
        }

        private static class NodeSelectorWithOp {
            private final NodeSelector selector;
            private final Op op;

            private NodeSelectorWithOp(NodeSelector selector, Op op) {
                this.selector = selector;
                this.op = op;
            }
        }

        private interface Op {
            boolean perform(NodeSelector selector, Bundle args, boolean currentResult);
        }

        private static class AndOp implements Op {
            private static AndOp AND = new AndOp();

            private AndOp() {
                //no instance
            }

            @Override
            public boolean perform(NodeSelector selector, Bundle args, boolean currentResult) {
                return selector.select(args) && currentResult;
            }
        }

        private static class OrOp implements Op {
            private static OrOp OR = new OrOp();

            private OrOp() {
                //no instance
            }

            @Override
            public boolean perform(NodeSelector selector, Bundle args, boolean currentResult) {
                return selector.select(args) || currentResult;
            }
        }

        @Override
        public boolean select(Bundle args) {
            //first result
            boolean currentResult = root.select(args);
            if (ListUtils.isEmpty(selectors)) {
                return currentResult;
            }
            for (NodeSelectorWithOp selector : selectors) {
                currentResult = selector.op.perform(selector.selector, args, currentResult);
            }
            return currentResult;
        }
    }
}
