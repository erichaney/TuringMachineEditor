package com.ehaney.turingmachineeditor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MachineTest {

    @Nested
    @DisplayName("On creation...")
    class OnCreate {

        @Test
        @DisplayName("should clone initial tape")
        void should_clone_initial_tape() {
            Tape t = new Tape("[a] b c d");
            Machine m = new Machine(t, new State("0", State.Action.L));
            assertFalse(t.equals(m.getTape()));
            assertEquals(t.toString(), m.getTape().toString());
        }

        @Test
        @DisplayName("should have one state")
        void should_have_one_state() {
            State s = new State("1", State.Action.L);
            Machine m = new Machine(new Tape("[x] y z"), s);
            assertEquals(1, m.getStates().size());
            assertEquals(s, m.getState("1"));
        }
    }

    @Nested
    @DisplayName("On adding states...")
    class OnAddingStates {
        Machine m;

        @BeforeEach
        void setUp() {
            m = new Machine("[a] b c d", "1", State.Action.L);
        }

        @Test
        @DisplayName("can add states fluently")
        void can_add_states_fluently() {
            m.addState("2", State.Action.R)
                    .addState("3", State.Action.H)
                    .addState("4", State.Action.R);
        }

        @Test
        @DisplayName("prevents adding states with duplicate ids")
        void prevents_adding_states_with_duplicate_ids() {
            assertThrows(IllegalArgumentException.class,
                    () -> m.addState("1", State.Action.N));
        }
    }

    @Nested
    @DisplayName("On removing states...")
    class OnRemovingStates {
        @Test
        @DisplayName("state is removed from the map of states")
        void state_is_removed_from_the_map_of_states() {
            Machine m = new Machine("[a] b c d", "0", State.Action.R);
            assertEquals(1, m.getStates().size());
            m.addState("1", State.Action.N).addState("2", State.Action.L);
            assertEquals(3, m.getStates().size());
            m.removeState("2");
            m.removeState("1");
            assertEquals(1, m.getStates().size());
        }

        @Test
        @DisplayName("cannot remove initial state")
        void cannot_remove_initial_state() {
            Machine m = new Machine("[a] b c", "0", State.Action.L);
            assertThrows(IllegalArgumentException.class,
                    () -> m.removeState("0"));
        }
    }

    @Nested
    @DisplayName("On changing state id...")
    class OnChangingStateID {

        @Test
        @DisplayName("the new id is the key to the old state")
        void the_new_id_is_the_key_to_the_old_state() {
            Machine m = new Machine("a [b] c d", "0", State.Action.L);
            State s = m.getState("0");
            m.setStateID(s, "1");
            assertEquals("1", s.getID());
            assertEquals("1", m.getState("1").getID());
            assertEquals(s, m.getState("1"));
        }
    }

    @Nested
    @DisplayName("Simulation test")
    class SimulationTest {

        // This machine overwrites the first occurrence of "ab" with "cc" and halts.
        Machine m;

        @BeforeEach
        void setUp() {
            m = new Machine("[b] a a a b a", "0", State.Action.R);
            State s0 = m.getState("0");
            State s1 = new State("1", State.Action.R);
            State s2 = new State("2", State.Action.L);
            State s3 = new State("3", State.Action.H);

            s0
                    .addTransition("a", "a", s1)
                    .addTransition("#", "#", s3);
            s1
                    .addTransition("b", "c", s2)
                    .addTransition("#", "#", s3);
            s2
                    .addTransition("a", "c", s3);

            m.addAllStates(s1, s2, s3);
        }

        @Test
        @DisplayName("Step forward advances to correct state")
        void step_forward_advances_to_correct_state() {
            m.stepForward();
            assertEquals("b [a] a a b a", m.getTape().toString());
            assertEquals(1, m.getTape().getHeadIndex());
            assertEquals("0", m.getCurrentStateID());
            m.stepForward();
            assertEquals("b a [a] a b a", m.getTape().toString());
            assertEquals(2, m.getTape().getHeadIndex());
            assertEquals("1", m.getCurrentStateID());
        }

        @Test
        @DisplayName("simulation halts on halt state")
        void simulation_halts_on_halt_state() {
            m.stepForward(7);
            assertEquals("b a a [c] c a", m.getTape().toString());
            assertEquals("3", m.getCurrentStateID());
            assertTrue(m.isHalted());

            m.stepForward();
            assertEquals("b a a [c] c a", m.getTape().toString());
            assertEquals("3", m.getCurrentStateID());
            assertTrue(m.isHalted());
        }

        @Test
        @DisplayName("undo reverts to previous configuration")
        void undo_reverts_to_previous_configuration() {
            m.stepForward();
            assertEquals("b [a] a a b a", m.getTape().toString());
            assertEquals(1, m.getStepNumber());
            assertEquals("0", m.getCurrentStateID());

            m.stepForward();
            assertEquals("b a [a] a b a", m.getTape().toString());
            assertEquals(2, m.getStepNumber());
            assertEquals("1", m.getCurrentStateID());

            m.undoStep();
            assertEquals("b [a] a a b a", m.getTape().toString());
            assertEquals(1, m.getStepNumber());
            assertEquals("0", m.getCurrentStateID());

            m.undoStep();
            assertEquals("[b] a a a b a", m.getTape().toString());
            assertEquals(0, m.getStepNumber());
            assertEquals("0", m.getCurrentStateID());
        }

        @Test
        @DisplayName("redo can undo undo")
        void redo_can_undo_undo() {
            m.stepForward(5);
            assertEquals("b a a [a] c a", m.getTape().toString());
            assertEquals("2", m.getCurrentStateID());
            assertEquals(5, m.getStepNumber());

            m.undoStep();
            assertEquals("b a a a [b] a", m.getTape().toString());
            assertEquals("1", m.getCurrentStateID());
            assertEquals(4, m.getStepNumber());

            m.redoStep();
            assertEquals("b a a [a] c a", m.getTape().toString());
            assertEquals("2", m.getCurrentStateID());
            assertEquals(5, m.getStepNumber());
        }

        @Test
        @DisplayName("reset reloads the initial conditions")
        void reset_reloads_the_initial_conditions() {
            assertEquals("[b] a a a b a", m.getTape().toString());
            assertEquals(0, m.getStepNumber());
            assertEquals("0", m.getCurrentStateID());
            m.stepForward(5);
            assertEquals("b a a [a] c a", m.getTape().toString());
            assertEquals("2", m.getCurrentStateID());
            assertEquals(5, m.getStepNumber());
            m.reset();
            assertEquals("[b] a a a b a", m.getTape().toString());
            assertEquals(0, m.getStepNumber());
            assertEquals("0", m.getCurrentStateID());

        }
    }


}