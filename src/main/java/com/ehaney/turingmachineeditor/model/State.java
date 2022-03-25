package com.ehaney.turingmachineeditor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The data model of a state of a Turing Machine.
 * <p>
 * States have a unique id and a tape action, which is one of: L, R, H, Y, or N.
 * <p>
 * States are linked through transition objects. Each state keeps a list of the
 * transitions that originate from it.
 */
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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public State addTransition(String readSymbol, String writeSymbol, State toState) {
        if (!hasTransition(readSymbol, writeSymbol, toState)) {
            Transition t = new Transition(this, readSymbol, writeSymbol, toState);
            transitions.add(t);
        }
        return this;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public boolean hasTransition(String readSymbol, String writeSymbol, State toState) {
        return transitions.stream()
                .anyMatch(t -> {
                    return t.getReadSymbol().equals(readSymbol) &&
                            t.getWriteSymbol().equals(writeSymbol) &&
                            t.getToState().equals(toState);
                });
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
