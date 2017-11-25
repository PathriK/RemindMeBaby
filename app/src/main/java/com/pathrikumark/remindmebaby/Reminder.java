package com.pathrikumark.remindmebaby;

/**
 * Created by keert on 14-11-2017.
 */

public class Reminder {
    enum STATE {
        IDLE,
        WAITING,
        IN_PROGRESS,
        SNOOZE
    }
    final String NA_STATUS_TEXT = "-";
    final String name;
    final long duration;
    final long waitTime;
    STATE currentState;
    STATE prevState;
    long startTime;
    int reminderDuration;
    int elapsedTime;
    String formattedDuration;
    String formattedElapsed;
    String statusText = NA_STATUS_TEXT;

    public Reminder(String name, long duration, long waitTime) {
        this.name = name;
        this.duration = duration;
        this.waitTime = waitTime;
        currentState = STATE.IDLE;
    }

    public void toggleStart() {
        currentState = STATE.IN_PROGRESS;
        startTime = System.currentTimeMillis();
        reminderDuration = (int) (duration/1000);
        elapsedTime = 0;
        formattedDuration = formatDuration(reminderDuration);
        formattedElapsed = formatDuration(elapsedTime);
        statusText = STATE.IN_PROGRESS.name();
    }

    public void toggleWait() {
        currentState = STATE.WAITING;
        startTime = System.currentTimeMillis();
        reminderDuration = (int) (waitTime/1000);
        elapsedTime = 0;
        formattedDuration = formatDuration(reminderDuration);
        formattedElapsed = formatDuration(elapsedTime);
        statusText = STATE.WAITING.name();
    }

    public void toggleStop() {
        prevState = currentState;
        currentState = STATE.IDLE;
        startTime = 0;
        if (prevState != null)
            statusText = prevState.name() + " Over";
        else
            statusText = NA_STATUS_TEXT;
    }

    public void toggleSnooze() {
        prevState = currentState;
        currentState = STATE.SNOOZE;
        if (prevState != null)
            statusText = prevState.name() + " Snoozed";
        else
            statusText = NA_STATUS_TEXT;
    }

    public int getReminderDuration() {
        return reminderDuration;
    }

    public void updateElapsedDuration(){
        elapsedTime = (int) ((System.currentTimeMillis() - startTime)/1000);
        formattedElapsed = formatDuration(elapsedTime);
    }

    public int getElapsedDuration(){
        return elapsedTime;
    }

    public boolean needsUpdate(){
        if(startTime != 0 && currentState != STATE.IDLE)
            return true;
        return false;
    }

    public boolean isSnoozed() {
        return currentState == STATE.SNOOZE;
    }

    public String getStatusText(){
        return statusText;
    }

    private String formatDuration(int duration){
        String formattedDuration;
        int sec = duration % 60;
        int min = duration / 60;
        int hr = min / 60;
        min = min % 60;
        formattedDuration = String.format("%2dH %2dM %2dS", hr, min, sec);
        return formattedDuration;
    }
}
