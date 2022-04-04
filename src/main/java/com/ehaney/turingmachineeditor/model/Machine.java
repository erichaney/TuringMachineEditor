package com.ehaney.turingmachineeditor.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * The data model of the Turing Machine.
 * <p>
 * A Turing Machine canonically consists of a tape and tape reader, a set of states,
 * an initial state, and a set of state transition functions. The machine also
 * keeps account of its history with stacks for is previous steps that can be
 * rolled back with undo and rolled forward with redo. It also keeps a reference
 * to a clone of its input tape and initial state in its InitialConfiguration
 * to facilitate rewinding the machine back to step 0.
 * <p>
 * A "Step" object is a simple record of how the machine has changed during a
 * single transition. Steps are used to roll back the state of the machine during
 * undo operations.
 */
public class Machine {

    /**
     * The number of times the machine has transitioned.
     */
    private int stepNumber;

    /**
     * The tape of symbols and tape head.
     */
    private Tape tape;

    /**
     * The active state.
     */
    private State currentState;

    /**
     * The map of all states keyed by their IDs.
     */
    private Map<String, State> states;

    /**
     * A record of the initial tape and the initial state.
     */
    private InitialConfiguration init;

    /**
     * A record of the current state transition.
     */
    private Step currentStep;

    /**
     * The undo and redo stack of steps.
     */
    private Deque<Step> undoStack;
    private Deque<Step> redoStack;

    /**
     * Constructs a Turing Machine with a tape, one state, and no transitions.
     *
     * @param initialTape  The tape of the Turing Machine.
     * @param initialState The initial state.
     */
    public Machine(Tape initialTape, State initialState) {
        stepNumber = 0;
        tape = new Tape(initialTape);
        currentState = initialState;
        states = new HashMap<>();
        states.put(initialState.getID(), initialState);
        init = new InitialConfiguration(initialTape, initialState);
        currentStep = new Step(initialTape, initialState);
        undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();
    }

    /**
     * Constructs a Turing Machine with the given tape string, one state, and no
     * transitions.
     *
     * @param tapeString  The space-separated symbols of the tape.
     * @param stateID     The ID of the initial state.
     * @param stateAction The action of the initial state.
     */
    public Machine(String tapeString, String stateID, State.Action stateAction) {
        this(new Tape(tapeString), new State(stateID, stateAction));
    }

    public Tape getTape() {
        return tape;
    }

    public Map<String, State> getStates() {
        return states;
    }

    public State getState(String id) {
        return states.get(id);
    }

    public Machine addState(State s) {
        if (!states.containsKey(s.getID())) {
            states.put(s.getID(), s);
        } else {
            throw new IllegalArgumentException("Found another state with the same " +
                    "id. States must have unique ids.");
        }
        return this;
    }

    public Machine addState(String id, State.Action action) {
        return addState(new State(id, action));
    }

    public void setStateID(State state, String newID) {
        if (states.containsKey(newID)) {
            throw new IllegalArgumentException("Found another state with the " +
                    "same id. States must have unique ids.");
        } else {
            states.remove(state.getID());
            state.setID(newID);
            addState(state);
        }
    }

    public boolean isHalted() {
        State.Action a = currentState.getAction();
        return a == State.Action.H || a == State.Action.Y || a == State.Action.N;
    }

    private void transition() {
        stepNumber++;

        String readSymbol = tape.readSymbol();
        Transition availableTransition = currentState.getTransition(readSymbol);

        if (availableTransition != null) {
            tape.writeSymbol(availableTransition.getWriteSymbol());
            currentState = availableTransition.getToState();
        }

        if (currentState.getAction() == State.Action.L) {
            tape.shiftLeft();
        } else if (currentState.getAction() == State.Action.R) {
            tape.shiftRight();
        }
    }

    public void stepForward() {
        if (isHalted()) {
            return;
        } else if (redoStack.size() > 0) {
            redoStep();
        } else {
            transition();
            undoStack.push(currentStep);
            currentStep = new Step(tape, currentState);
        }
    }

    public void undoStep() {
        if (undoStack.size() == 0) {
            return;
        }
        stepNumber--;
        redoStack.push(currentStep);
        currentStep = undoStack.pop();

        // rewind using the information from the previous step
        tape.setPosition(currentStep.tapePosition);
        tape.writeSymbol(currentStep.readSymbol);
        currentState = getState(currentStep.stateID);
    }

    public void redoStep() {
        if (redoStack.size() == 0) {
            return;
        } else {
           undoStack.push(currentStep);
           currentStep = redoStack.pop();
           transition();
        }
    }

    public boolean removeState(String id) {
        if (id.equals(init.initialState.getID())) {
            throw new IllegalArgumentException("Removing the initial state is prohibited.");
        } else if (states.remove(id) == null) {
            return false;
        } else {
            return true;
        }
    }

    public void addAllStates(State... states) {
        for (State s : states) {
            addState(s);
        }
    }

    public String getCurrentStateID() {
        return currentState.getID();
    }

    public void stepForward(int times) {
        for (int i = 0; i < times; i++) {
            stepForward();
        }
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void reset() {
        stepNumber = 0;
        undoStack.clear();
        redoStack.clear();
        tape = init.initialTape;
        currentState = init.initialState;
        currentStep = new Step(tape, currentState);
    }

    /**
     * This class holds a clone of the initial Tape and the initial state for
     * the purposes of efficiently resetting the machine.
     */
    private class InitialConfiguration {
        Tape initialTape;
        State initialState;

        InitialConfiguration(Tape initialTape, State initialState) {
            this.initialTape = new Tape(initialTape);
            this.initialState = initialState;
        }
    }

    /**
     * This class keeps a record of a single state transition for the purposes of
     * undoing (and redoing) a step of the Turing Machine's operation.
     */
    private class Step {
        int tapePosition;
        String stateID;
        String readSymbol;

        Step(int tapePosition, String stateID, String readSymbol) {
            this.tapePosition = tapePosition;
            this.stateID = stateID;
            this.readSymbol = readSymbol;
        }

        Step(Tape tape, State state) {
            this.tapePosition = tape.getHeadIndex();
            this.stateID = state.getID();
            this.readSymbol = tape.readSymbol();
        }
    }

}
