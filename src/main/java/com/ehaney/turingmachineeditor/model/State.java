package com.ehaney.turingmachineeditor.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The data model of a state of a Turing Machine.
 * <p>
 * States have a unique id and a tape action, which is one of: L, R, H, Y, or N.
 * <p>
 * States are linked through transition objects. Each state keeps a map of the
 * transitions that originate from it, keyed by the symbol that must be read on
 * the tape to activate that transition.
 */
public class State {

    public enum Action {L, R, H, Y, N;}

    private String id;

    private Action action;

    private Map<String, Transition> transitions;

    public State(String id, Action action) {
        this.id = id;
        this.action = action;
        transitions = new HashMap<>();
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
        if (!transitions.containsKey(readSymbol)) {
            transitions.put(readSymbol,
                    new Transition(this, readSymbol, writeSymbol, toState));
        }

        return this;
    }

    public List<Transition> getTransitions() {
        return transitions.values().stream().toList();
    }

    public boolean hasTransition(String readSymbol) {
        return transitions.containsKey(readSymbol);
    }

    public Transition getTransition(String readSymbol) {
        return transitions.get(readSymbol);
    }

    public boolean removeTransition(Transition t) {
        if (t == null)
            return false;
        t.deleteLink();
        return transitions.remove(t.getReadSymbol(), t);
    }

    public boolean removeTransition(String readSymbol) {
        return removeTransition(transitions.get(readSymbol));
    }

    @Override
    public String toString() {
        return id + " " + action.toString();
    }
}
