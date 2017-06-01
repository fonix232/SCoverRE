package com.sec.android.cover.ledcover.fsm.grace.missedevent;

import android.util.Log;

public class LedCoverMissedEventManager {
    private static final int MISSED_EVENT_DISPLAY_LIMIT = 3;
    private static final String TAG = LedCoverMissedEventManager.class.getSimpleName();
    private MissedEventPriorityQeue mAllEventQueue = new MissedEventPriorityQeue();
    private MissedEventPriorityQeue mCurrentEventQueue = new MissedEventPriorityQeue();
    private MissedEventPriorityQeue mNewEventQueue = new MissedEventPriorityQeue();

    public void addMutedMissedEvent(MissedEvent addedEvent) {
        if (this.mAllEventQueue.contains(addedEvent)) {
            Log.d(TAG, "addMutedMissedEvent: " + addedEvent.getPackageName() + " exists - update");
            this.mAllEventQueue.remove(addedEvent);
        }
        if (this.mCurrentEventQueue.contains(addedEvent)) {
            this.mCurrentEventQueue.remove(addedEvent);
            this.mCurrentEventQueue.offer(addedEvent);
        }
        if (this.mNewEventQueue.contains(addedEvent)) {
            this.mNewEventQueue.remove(addedEvent);
            this.mNewEventQueue.offer(addedEvent);
        }
        this.mAllEventQueue.offer(addedEvent);
    }

    public void addInactiveUserMissedEvent(MissedEvent addedEvent) {
        if (this.mNewEventQueue.contains(addedEvent)) {
            this.mNewEventQueue.remove(addedEvent);
        }
        this.mNewEventQueue.offer(addedEvent);
    }

    public void addMissedEvent(MissedEvent addedEvent) {
        if (this.mAllEventQueue.contains(addedEvent)) {
            Log.d(TAG, "addMissedEvent: " + addedEvent.getPackageName() + " exists - update");
            this.mAllEventQueue.remove(addedEvent);
        }
        this.mAllEventQueue.offer(addedEvent);
        if (this.mCurrentEventQueue.contains(addedEvent)) {
            this.mCurrentEventQueue.remove(addedEvent);
        }
        if (this.mNewEventQueue.contains(addedEvent)) {
            this.mNewEventQueue.remove(addedEvent);
        }
        this.mNewEventQueue.offer(addedEvent);
    }

    public void addMissedEvents(MissedEvent[] addedEvents) {
        for (MissedEvent event : addedEvents) {
            addMissedEvent(event);
        }
    }

    public void removeMissedEvent(MissedEvent removedEvent) {
        if (this.mAllEventQueue.contains(removedEvent)) {
            this.mAllEventQueue.remove(removedEvent);
        } else {
            Log.d(TAG, "removeMissedEvent: " + removedEvent.getPackageName() + " - nothing to remove");
        }
        if (this.mCurrentEventQueue.contains(removedEvent)) {
            this.mCurrentEventQueue.remove(removedEvent);
        }
        if (this.mNewEventQueue.contains(removedEvent)) {
            this.mNewEventQueue.remove(removedEvent);
        }
    }

    public void removeMissedEvents(MissedEvent[] removedEvents) {
        for (MissedEvent event : removedEvents) {
            removeMissedEvent(event);
        }
    }

    public MissedEvent[] getAllMissedEvents() {
        return (MissedEvent[]) this.mAllEventQueue.toArray(new MissedEvent[0]);
    }

    public void clearCurrentQueue() {
        this.mCurrentEventQueue.clear();
    }

    public void addLatestEventsToCurrentQueue() {
        for (int count = 0; count < 3 && !this.mAllEventQueue.isEmpty(); count++) {
            this.mCurrentEventQueue.add((MissedEvent) this.mAllEventQueue.poll());
        }
        this.mAllEventQueue.addAll(this.mCurrentEventQueue);
    }

    public boolean isEmpty() {
        return this.mAllEventQueue.isEmpty();
    }

    public boolean isCurrentQueueEmpty() {
        return this.mCurrentEventQueue.isEmpty();
    }

    public MissedEvent pollCurrentMissedEventFromQueue() {
        return (MissedEvent) this.mCurrentEventQueue.poll();
    }

    public boolean hasMissedEvent(MissedEvent event) {
        return this.mAllEventQueue.contains(event);
    }

    public MissedEvent pollNewMissedEventFromQueue() {
        return (MissedEvent) this.mNewEventQueue.poll();
    }

    public void clearNewEventQueue() {
        this.mNewEventQueue.clear();
    }
}
