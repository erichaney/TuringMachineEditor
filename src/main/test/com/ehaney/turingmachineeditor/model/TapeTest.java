package com.ehaney.turingmachineeditor.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TapeTest {

    @Nested
    @DisplayName("On tape construction...")
    class OnConstruction {

        @Test
        @DisplayName("tapeOrigin equals the last bracketed symbol in input string")
        void tape_origin_equals_the_last_bracketed_symbol_in_input_string() {
            assertEquals("a",
                    new Tape("[a] b c d e").readSymbol());
            assertEquals("d",
                    new Tape("a b [c] [d] e f").readSymbol());
            assertEquals("a",
                    new Tape("a b c").readSymbol());
            assertEquals("", new Tape("").readSymbol());
        }

        @Test
        @DisplayName("toString is equivalent to input string")
        void to_string_is_equivalent_to_input_string() {
            String input1 = "[a] b c d";
            String input2 = "x [y] z";
            String input3 = "a [z]";
            assertEquals(input1, new Tape(input1).toString());
            assertEquals(input2, new Tape(input2).toString());
            assertEquals(input3, new Tape(input3).toString());
        }

        @Test
        @DisplayName("head position equals zero")
        void head_position_equals_zero() {
            assertEquals(0,
                    new Tape("[a] b c d").getHeadIndex());
            assertEquals(0,
                    new Tape("a b c d").getHeadIndex());
            assertEquals(0,
                    new Tape("w [x] y z").getHeadIndex());
            assertEquals(0,
                    new Tape("").getHeadIndex());
        }

        @Test
        @DisplayName("size equals the number of tokens in the input string")
        void size_equals_the_number_of_tokens_in_the_input_string() {
            assertEquals(4,
                    new Tape("[a] b c d").size());
            assertEquals(1,
                    new Tape("").size());
            assertEquals(8,
                    new Tape("w [x] y z $ a b c").size());
        }

        @Test
        @DisplayName("tape head is at the bracketed symbol of the input string")
        void tape_head_is_at_the_bracketed_symbol_of_the_input_string() {
            assertEquals("x",
                    new Tape("[x] y z").readSymbol());
            assertEquals("y",
                    new Tape("x [y] z").readSymbol());
            assertEquals("x",
                    new Tape("x y z").readSymbol());
        }

        @Test
        @DisplayName("left bound matches position of first symbol of the input string")
        void left_bound_matches_position_of_first_symbol_of_the_input_string() {
            assertEquals(0,
                    new Tape("[x] y z").getLeftBound());
            assertEquals(-1,
                    new Tape("x [y] z").getLeftBound());
        }

        @Test
        @DisplayName("right bound matches position of last symbol of the input string")
        void right_bound_matches_position_of_last_symbol_of_the_input_string() {
            assertEquals(2,
                    new Tape("[x] y z").getRightBound());
            assertEquals(0,
                    new Tape("x y [z]").getRightBound());
        }

        @Test
        @DisplayName("blank symbols are on tape")
        void blank_symbols_are_on_tape() {
            Tape t = new Tape("[a] b c");
            assertEquals(Tape.BLANK_SYMBOL, t.getSymbolAt(-1));
            assertEquals(Tape.BLANK_SYMBOL, t.getSymbolAt(3));
            assertEquals(Tape.BLANK_SYMBOL, t.getSymbolAt(99999));
            assertEquals(Tape.BLANK_SYMBOL, t.getSymbolAt(-99999));
        }
    }

    @Nested
    @DisplayName("On shifting tape head left or right...")
    class OnShift {

        @Test
        @DisplayName("blank symbols are added to left when shifting left")
        void blank_symbols_are_added_to_left() {
            Tape t = new Tape("[a] b c");
            t.shiftLeft();
            assertEquals(Tape.BLANK_SYMBOL, t.readSymbol());
            t.shiftLeft(5);
            assertEquals(Tape.BLANK_SYMBOL, t.readSymbol());
        }

        @Test
        @DisplayName("blank symbols are added to right when shifting right")
        void blank_symbols_are_added_to_right() {
            Tape t = new Tape("a [b] c");
            t.shiftRight(2);
            assertEquals(Tape.BLANK_SYMBOL, t.readSymbol());
            t.shiftRight(5);
            assertEquals(Tape.BLANK_SYMBOL, t.readSymbol());
        }

        @Test
        @DisplayName("left bound changes when shifting left")
        void left_bound_changes_when_shifting_left() {
            Tape t = new Tape("[a] b c");
            assertEquals(0, t.getLeftBound());
            t.shiftLeft();
            assertEquals(-1, t.getLeftBound());
        }

        @Test
        @DisplayName("right bound changes when shifting right")
        void right_bound_changes_when_shifting_right() {
            Tape t = new Tape("x y [z]");
            assertEquals(0, t.getRightBound());
            t.shiftRight(2);
            assertEquals(2, t.getRightBound());
        }

        @Test
        @DisplayName("head position changes when shifting")
        void head_position_changes_when_shifting() {
            Tape t = new Tape("x y [z]");
            assertEquals(0, t.getHeadIndex());
            t.shiftRight(3);
            assertEquals(3, t.getHeadIndex());
            t.shiftLeft(10);
            assertEquals(-7, t.getHeadIndex());
        }
    }

   @Nested
   @DisplayName("On writing symbol to tape...")
    class OnWrite {

       @Test
       @DisplayName("written symbol appears at tape head's position")
       void written_symbol_appears_at_tape_head_s_position() {
           Tape t = new Tape("a [b] c d");
           t.writeSymbol("z");
           assertEquals("z", t.readSymbol());
           t.shiftLeft();
           t.writeSymbol("y");
           assertEquals("y", t.readSymbol());
           t.shiftRight(2);
           t.writeSymbol("x");
           assertEquals("x", t.readSymbol());
       }
   }

}