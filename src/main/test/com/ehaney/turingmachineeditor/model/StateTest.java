package com.ehaney.turingmachineeditor.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {
    @Test
    @DisplayName("new states have no transitions yet")
    void new_states_have_no_transitions_yet() {
        assertEquals(0,
                new State("0", State.Command.H).getTransitions().size());
    }

    @Test
    @DisplayName("to string format is correct")
    void to_string_format_is_correct() {
        assertEquals("0 H",
                new State("0", State.Command.H).toString());
    }

    @Test
    @DisplayName("can add a transition to self")
    void can_add_a_transition_to_self() {
        State s = new State("0", State.Command.R);
        Transition t = new Transition(s, "a", "b", s);
        s.addTransition(t);
        assertEquals(1, s.getTransitions().size());
        assertEquals(t, s.getTransitions().get(0));
    }

    @Test
    @DisplayName("can add transitions between different states")
    void can_add_transitions_between_different_states() {
        State s0 = new State("0", State.Command.L);
        State s1 = new State("1", State.Command.R);
        Transition t = new Transition(s0, "#", "#", s1);
        Transition v = new Transition(s0, "a", "b", s1);

        s0.addTransition(t).addTransition(v);

        assertEquals(2, s0.getTransitions().size());
        assertEquals(0, s1.getTransitions().size());

    }
}