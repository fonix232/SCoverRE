package com.sec.android.cover.ledcover.fsm.dream;

import java.util.Comparator;
import java.util.PriorityQueue;

public class LedStatePriorityQueue extends PriorityQueue<LedState> {
    private static final long serialVersionUID = -2187107619545804959L;

    private static class LedStatePriorityComparator implements Comparator<LedState> {
        private final QueueType mType;

        public LedStatePriorityComparator(QueueType type) {
            this.mType = type;
        }

        public int compare(LedState a, LedState b) {
            return Integer.compare(a.getPriority(this.mType), b.getPriority(this.mType));
        }
    }

    public enum QueueType {
        MAIN,
        POWER_BUTTON,
        COVER_CLOSE,
        DELAYED
    }

    public LedStatePriorityQueue(QueueType type) {
        super(1, new LedStatePriorityComparator(type));
    }

    public boolean add(LedState o) {
        if (o == null || contains(o)) {
            return false;
        }
        return super.add(o);
    }

    public boolean offer(LedState o) {
        if (o == null || contains(o)) {
            return false;
        }
        return super.offer(o);
    }
}
