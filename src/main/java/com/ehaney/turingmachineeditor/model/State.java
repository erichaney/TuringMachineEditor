package com.ehaney.turingmachineeditor.model;

import java.util.ArrayList;
import java.util.List;

public class State {

    public enum Action {L, R, H, Y, N;}

    private String id;

    private Action action;
    private List<Transition> transitions;
    public State(String id, Action action) {
        this.id = id;
        this.action = action;
        transitions = new ArrayList<>();
    }

    public String getID() {
        return id;
    }

    public State addTransition(Transition t) {
        transitions.add(t);
        return this;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }
    public boolean removeTransition(Transition t) {
        if (t == null)
            return false;
        t.deleteLink();
        return transitions.remove(t);
    }

    @Override
    public String toString() {
        return id + " " + action.toString();
    }
}
