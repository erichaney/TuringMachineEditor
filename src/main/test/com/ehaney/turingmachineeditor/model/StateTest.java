package com.ehaney.turingmachineeditor.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateTest {
    @Test
    @DisplayName("new states have no transitions yet")
    void new_states_have_no_transitions_yet() {
        assertEquals(0,
                new State("0", State.Action.H).getTransitions().size());
    }

    @Test
    @DisplayName("to string format is correct")
    void to_string_format_is_correct() {
        assertEquals("0 H",
                new State("0", State.Action.H).toString());
    }

    @Test
    @DisplayName("can add a transition to self")
    void can_add_a_transition_to_self() {
        State s = new State("0", State.Action.R);
        s.addTransition("a", "b", s);
        assertEquals(1, s.getTransitions().size());
        assertTrue(s.hasTransition("a", "b", s));
    }

    @Test
    @DisplayName("can add transitions between different states")
    void can_add_transitions_between_different_states() {
        State s0 = new State("0", State.Action.L);
        State s1 = new State("1", State.Action.R);

        s0.addTransition("#", "#", s1)
                .addTransition("a", "b", s1);

        assertEquals(2, s0.getTransitions().size());
        assertEquals(0, s1.getTransitions().size());

    }

    @Test
    @DisplayName("can remove a transition")
    void can_remove_a_transition() {
        State s = new State("0", State.Action.R);
        s.addTransition("0", "1", s);
        assertTrue(s.removeTransition(s.getTransitions().get(0)));
        assertEquals(0, s.getTransitions().size());
    }

    @Test
    @DisplayName("can_get_id")
    void can_get_id() {
        assertEquals("1",
                new State("1", State.Action.H).getID());
    }

    @Test
    @DisplayName("can get and set state actions")
    void can_get_and_set_state_actions() {
        State s = new State("0", State.Action.R);
        assertEquals(State.Action.R, s.getAction());

        s.setAction(State.Action.L);
        assertEquals(State.Action.L, s.getAction());
    }
}