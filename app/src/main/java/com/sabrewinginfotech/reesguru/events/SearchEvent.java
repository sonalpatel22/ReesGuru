package com.sabrewinginfotech.reesguru.events;

/**
 * Created by Alpesh on 12/5/2017.
 */

public class SearchEvent {

    private int action=0;
    private String event;
    public static int ACTION_SEARCH=0;


    public SearchEvent(int action, String event) {
        this.action = action;
        this.event = event;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
