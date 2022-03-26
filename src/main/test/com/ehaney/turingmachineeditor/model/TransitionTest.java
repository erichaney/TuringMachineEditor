package com.ehaney.turingmachineeditor.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransitionTest {
    @Test
    @DisplayName("can change the read symbol")
    void can_change_the_read_symbol() {
        State s = new State("1", State.Action.R);
        s.addTransition("a", "b", s);
        Transition t = s.getTransition("a");
        t.setReadSymbol("z");
        assertTrue(s.hasTransition("z"));
        assertEquals(t, s.getTransition("z"));
    }

    @Test
    @DisplayName("does not allow more than one transition with the same read symbol")
    void does_not_allow_more_than_one_transition_with_the_same_read_symbol() {
        State s = new State("1", State.Action.N);
        s.addTransition("a", "b", s);

        assertThrows(IllegalStateException.class,
                () -> s.addTransition("a", "z", s),
                "Shouldn't be possible to add a transition with the same scanned symbol");

        s.addTransition("b", "b", s);
        assertThrows(IllegalStateException.class,
                () -> s.getTransition("b").setReadSymbol("a"),
                "Shouldn't be possible to set a transition's read symbol to one that" +
                        "already exists on another transition from that state.");
    }

    @Test
    @DisplayName("can set write symbol")
    void can_set_write_symbol() {
        State s = new State("1", State.Action.R);
        s.addTransition("a", "b", new State("2", State.Action.L));
        assertEquals("b", s.getTransition("a").getWriteSymbol());
        s.getTransition("a").setWriteSymbol("z");
        assertEquals("z", s.getTransition("a").getWriteSymbol());
    }

    @Test
    @DisplayName("toString format is correct")
    void to_string_format_is_correct() {
        State s = new State("1", State.Action.L);
        Transition t = s.addTransition("a", "b", s)
                .getTransition("a");

        assertEquals("1 1 a b", t.toString());
    }

    @Test
    @DisplayName("can redirect transition to a different state")
    void can_redirect_transition_to_a_different_state() {
        State s = new State("1", State.Action.L);
        State s2 = new State("2", State.Action.L);
        s.addTransition("a", "b", s);
        Transition t = s.getTransition("a");
        assertEquals(s, t.getToState());
        t.linkTo(s2);
        assertEquals(s2, t.getToState());
    }
}