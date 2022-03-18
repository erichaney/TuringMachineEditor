package com.ehaney.turingmachineeditor.model;

import java.util.Collection;
import java.util.LinkedList;

/**
 * The data model of the Turing Machine's tape and read/write head.
 *
 * The logical coordinates of cells on the Tape are called "positions" and are
 * like those of an integer number line, including their extent to positive and
 * negative infinity. Thus, no position is considered out of bounds on a tape
 * -- though indexing it using far-out positions is likely to return just the
 * blank symbol.
 *
 * Although a Tape acts as if it has an infinite size, the size method is supported
 * and returns a finite value. The size of a tape is interpreted to mean the
 * "visible" size: the number of cells that are given as initial input plus any
 * additional cells that have been "seen" by the tape head as it moves left and
 * right along the tape. The extreme left and right tape position of this visible
 * region are called the left bound and right bound.
 */
public class Tape {

    /** The symbol representing a blank space on the tape. */
    public static final String BLANK_SYMBOL = "#";

    /** The Turing Machine's tape of symbols. */
    private LinkedList<String> tape;

    /**
     * The current (possibly negative) position of the tape cell that is subject
     * to read/write operations.
     *
     * Unlike the usual list interface, Tape supports negative indices. Negative
     * indices represent positions to the left of the initial head position,
     * which is always regarded as index 0 (the origin).
     * */
    private int headPosition;

    /**
     * The real index of the initial head position within the underlying linked
     * list.
     *
     * From a Turing Machine's point of view, the tape position of the origin is
     * always 0. But from the point of view of the underlying linked list, the
     * origin index will increment whenever the head moves into the far left
     * region of the tape and requires more blank symbols to be added to the
     * beginning of the linked list.
     *
     * So this means that the list index of the head will always be equal to
     * originIndex + headPosition.
     * */
    private int originIndex;

    /**
     * Constructs a Tape from the given input string.
     *
     * The tape input string must be formatted according to the following
     * specifications:
     * - The symbols must be separated by at least one space.
     * - A pair of square brackets should be placed around the initial head position.
     * -- If no symbol has square brackets on each side, the initial head position
     *    will default to the first symbol of the input string.
     * -- If more than one symbol is bracketed, the initial head position will
     * default to the last symbol that is bracketed.
     *
     *  Example of formatted input: "[1] 0 1 1 + 1 0 1"
     *
     *  This specification maintains compatibility with the original .tur files.
     *
     * @param tapeInput The initial data written to the tape.
     */
    public Tape(String tapeInput) {
        tape = new LinkedList<>();
        headPosition = 0;
        originIndex = 0;

        String[] tokens = tapeInput.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            String symbol = tokens[i];
            if (symbol.startsWith("[") && symbol.endsWith("]")) {
                originIndex = i;
            }
        }
    }

    /**
     * Constructs a Tape by cloning the given Tape object.
     *
     * @param other The Tape to clone.
     */
    public Tape(Tape other) {
        this.tape = new LinkedList<>();
        this.headPosition = other.headPosition;
        this.originIndex = other.originIndex;
        this.tape.addAll(other.tape);
    }

    /**
     * Get the current position of the head on the tape.
     *
     * This returns a negative position if the head is to the left of the origin.
     *
     * @return The current position of the head using the Turing Machine's tape
     * coordinates.
     */
    public int getHeadPosition() {
        return headPosition;
    }

    /**
     * Get the size of the tape's "viewed" region.
     *
     * Properly speaking, a tape's size is infinite, but practically speaking it is
     * finite in the sense that it was given a finite input string and has viewed
     * a finite number of cells during its operations.
     *
     * @return The size of the tape's viewed region.
     */
    public int size() {
        return tape.size();
    }

    /**
     * Get the tape position of the leftmost cell in the tape's viewed region.
     *
     * This position corresponds to index 0 of the underlying linked list.
     *
     * @return The tape position of the leftmost cell in the tape's viewed region.
     */
    public int getLeftBound() {
        return originIndex * -1;
    }

    /**
     * Get the tape position of the rightmost cell in the tape's viewed region.
     *
     * This position corresponds to the last index of the underlying linked list.
     *
     * @return The tape position of the rightmost cell in the tape's viewed region.
     */
    private  int getRightBound() {
        return tape.size() - 1 - originIndex;
    }

    /**
     * Overwrite the cell at the current head position with the given symbol.
     *
     * @param symbol The symbol to write on the tape.
     */
    public void writeSymbol(String symbol) {
        int headListIndex = originIndex + headPosition;
        tape.set(headListIndex, symbol);
    }

    /**
     * Get the symbol on the tape at the current head position.
     *
     * @return The symbol scanned by the tape head.
     */
    public String readSymbol() {
        return tape.get(originIndex + headPosition);
    }

    /**
     * Get the symbol at the given position on the tape.
     *
     * @param position The position in the tape's coordinate system,
     * @return The symbol on the tape at the given position.
     */
    public String getSymbolAt(int position) {
        if (position < getLeftBound() || position > getRightBound()) {
            return Tape.BLANK_SYMBOL;
        } else {
            return tape.get(originIndex + position);
        }
    }

    /**
     * Shift the tape head left along the tape.
     */
    public void shiftLeft() {
        headPosition--;
        if (headPosition < getLeftBound()) {
            tape.addFirst(Tape.BLANK_SYMBOL);
            originIndex++;
        }
    }

    /**
     * Shift the tape head left along the tape repeatedly.
     *
     * @param times The number of times to shift left.
     */
    public void shiftLeft(int times) {
        if (times < 0) {
            throw new IllegalArgumentException("Expected a non-negative number" +
                    " of times to shift left. Got " + times);
        }

        for (int i = 0; i < times; i++) {
            shiftLeft();
        }
    }

    /**
     * Shift the tape head right along the tape.
     */
    public void shiftRight() {
        headPosition++;
        if (headPosition > getRightBound()) {
            tape.addLast(Tape.BLANK_SYMBOL);
        }
    }

    /**
     * Shift the tape head right along the tape repeatedly.
     *
     * @param times The number of times to shift right.
     */
    public void shiftRight(int times) {
        if (times < 0) {
            throw new IllegalArgumentException("Expected a non-negative number" +
                    " of times to shift right. Got " + times);
        }

        for (int i = 0; i < times; i++) {
            shiftRight();
        }
    }
}
