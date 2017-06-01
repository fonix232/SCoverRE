package com.sec.android.cover.ledcover.fsm.grace.missedevent;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MissedEventPriorityQeue extends PriorityQueue<MissedEvent> {
    private static final long serialVersionUID = -5697062364376858773L;

    private static class MissedEventPriorityComparator implements Comparator<MissedEvent> {
        private MissedEventPriorityComparator() {
        }

        public int compare(MissedEvent a, MissedEvent b) {
            return Long.compare(a.getPriority(), b.getPriority());
        }
    }

    public MissedEventPriorityQeue() {
        super(1, new MissedEventPriorityComparator());
    }

    public boolean add(MissedEvent o) {
        if (o == null || contains(o)) {
            return false;
        }
        return super.add(o);
    }

    public boolean offer(MissedEvent o) {
        if (o == null || contains(o)) {
            return false;
        }
        return super.offer(o);
    }
}
