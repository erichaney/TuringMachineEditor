package com.ehaney.turingmachineeditor.model;

import java.util.ArrayList;
import java.util.List;

public class State {
    public enum Command {L, R, H, Y, N}

    private String id;
    private Command command;
    private List<Transition> transitions;

    public State(String id, Command command) {
        this.id = id;
        this.command = command;
        transitions = new ArrayList<>();
    }

    public String getID() {
        return id;
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
        return id + " " + command.toString();
    }
}
