package com.example.myapplication.network.models;

public class Section {

    String name;
    int incoming, passed, today, unread, active;

    public Section(String name) {
        this.name = name;
        incoming = -1;
        passed = -1;
        today = -1;
        unread = -1;
        active = -1;
    }

    public Section(String name, int passed, int incoming, int today, int active, int unread) {
        this.name = name;
        this.incoming = incoming;
        this.passed = passed;
        this.today = today;
        this.unread = unread;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIncoming() {
        return incoming;
    }

    public void setIncoming(int incoming) {
        this.incoming = incoming;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
