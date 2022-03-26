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

    /** The possible tape actions performed on entry to a state. */
    public enum Action {L, R, H, Y, N;}

    /** The unique identifier of the state. */
    private String id;

    /** The tape action that this state will trigger on entry. */
    private Action action;

    /**
     * The transitions leading out of this state, keyed by the symbol that must
     * be read on the tape to activate the transition.
     */
    private Map<String, Transition> transitions;

    /**
     * Constructs a new state of the turing machine.
     * @param id The unique identifier.
     * @param action The tape action that this state will trigger on entry.
     */
    public State(String id, Action action) {
        this.id = id;
        this.action = action;
        transitions = new HashMap<>();
    }

    /** Get the unique identifier of this state. */
    public String getID() {
        return id;
    }

    /** Get the tape action that this state will trigger on entry. */
    public Action getAction() {
        return action;
    }

    /**
     * Change the tape action that this state will trigger on entry.
     *
     * @param action The tape action.
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * Add a new transition originating from this state.
     *
     * @param readSymbol The tape symbol read to activate this transition.
     * @param writeSymbol The symbol written to the tape.
     * @param toState The state that this transition leads to.
     * @return This state with the added transition.
     */
    public State addTransition(String readSymbol, String writeSymbol, State toState) {
        if (transitions.containsKey(readSymbol)) {
            throw new IllegalStateException("A state cannot have two or more " +
                    "transitions with the same scanned symbol.");
        } else {
            transitions.put(readSymbol,
                    new Transition(this, readSymbol, writeSymbol, toState));
        }

        return this;
    }

    /**
     * Get the transitions originating from this state.
     *
     * @return The list of transitions originating from this state.
     */
    public List<Transition> getTransitions() {
        return transitions.values().stream().toList();
    }

    /**
     * Return true if this state has a transition activated by the given tape symbol.
     *
     * @param readSymbol The tape symbol that activates a transition.
     * @return True if the state has a transition keyed to the given symbol.
     */
    public boolean hasTransition(String readSymbol) {
        return transitions.containsKey(readSymbol);
    }

    /**
     * Get the transition activated by the given tape symbol, or null if there isn't one.
     *
     * @param readSymbol The tape symbol that activates a transition.
     * @return The transition activated by the given symbol, or null.
     */
    public Transition getTransition(String readSymbol) {
        return transitions.get(readSymbol);
    }

    /**
     * Remove the given transition from this state.
     *
     * If the transition is null or does not originate from this state, the
     * method returns false to signal an unsuccessful removal. Otherwise, the
     * method removes the transition from the state's private map of transitions
     * and removes references to this state from the transition object itself.
     *
     * @param t The transition to remove.
     * @return True if the transition was removed, false otherwise.
     */
    public boolean removeTransition(Transition t) {
        if (t == null || !transitions.containsValue(t))
            return false;
        t.deleteLink();
        return transitions.remove(t.getReadSymbol(), t);
    }

    /**
     * Remove the transition triggered by the given symbol, if it exists.
     * @param readSymbol The tape symbol that triggers the transition to be removed.
     * @return True if the transition was removed, false otherwise.
     */
    public boolean removeTransition(String readSymbol) {
        return removeTransition(transitions.get(readSymbol));
    }

    /**
     * Remap the given transition to the new key.
     * @param newKey The new read symbol on the tape associated with the transition.
     * @param t The transition to remap.
     * @return True if the transition could be remapped, false otherwise.
     */
    public boolean updateTransitionKey(String newKey, Transition t) {
        if (t == null || !transitions.containsValue(t)) {
            return false;
        } else {
            transitions.remove(t.getReadSymbol());
            transitions.put(newKey, t);
            return true;
        }
    }

    @Override
    public String toString() {
        return id + " " + action.toString();
    }
}
