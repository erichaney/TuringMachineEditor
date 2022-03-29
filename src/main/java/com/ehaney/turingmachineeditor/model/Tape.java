package com.ehaney.turingmachineeditor.model;

import java.util.ArrayList;

/**
 * The data model of the Turing Machine's tape and read/write head.
 *
 * The indices of cells on the Tape are like those of an integer number line,
 * extending to positive and negative infinity with an origin at zero. Thus, no
 * index is considered out of bounds on a tape -- though indexing it using
 * far-out positions is likely to return just the blank symbol.
 *
 * Although a Tape acts as if it has an infinite size, the size method is supported
 * and returns a finite value. The size of a tape is interpreted to mean the
 * "visible" size: the number of cells that are given as initial input plus any
 * additional cells that have been "seen" by the tape head as it moves left and
 * right along the tape. The extreme left and right tape index of this visible
 * region are called the left bound and right bound.
 */
public class Tape {

    /** The symbol representing a blank space on the tape. */
    public static final String BLANK_SYMBOL = "#";

    /** The tape symbols with negative indices. */
    public ArrayList<String> tapeNegative;

    /** The tape symbol at index zero. */
    public String tapeOrigin;

    /** The tape symbols with positive indices. */
    public ArrayList<String> tapePositive;

    /**
     * The current (possibly negative) index of the tape cell that is subject
     * to read/write operations.
     *
     * Unlike the usual list interface, Tape supports negative indices. Negative
     * indices represent positions to the left of the initial head position,
     * which is always regarded as index 0 (the origin).
     * */
    private int headIndex;


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
        tapePositive = new ArrayList<>();
        tapeNegative = new ArrayList<>();
        headIndex = 0;

        // Parse input string and initialize tapeOrigin
        int originOffset = 0;
        String[] tokens = tapeInput.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            String symbol = tokens[i];
            if (symbol.startsWith("[") && symbol.endsWith("]")
                    && !symbol.substring(1, 2).equals("")) {
                originOffset = i;
                tokens[i] = symbol.substring(1, 2);
                tapeOrigin = tokens[i];
            }
        }
        if (tapeOrigin == null) {
            tapeOrigin = tokens[0];
        }

        // Initialize tapeNegative
        tapeNegative.add("--blank-space-for-origin--");
        for (int i = originOffset - 1; i >= 0; i--) {
            if (!tokens[i].equals("")) {
                tapeNegative.add(tokens[i]);
            }
        }

        // Initialize tapePositive
        tapePositive.add("--blank-space-for-origin--");
        for (int i = originOffset + 1; i < tokens.length; i++) {
            if (!tokens[i].equals("")) {
                tapePositive.add(tokens[i]);
            }
        }
    }

    /**
     * Constructs a Tape by cloning the given Tape object.
     *
     * @param other The Tape to clone.
     */
    public Tape(Tape other) {
        this.tapeNegative = new ArrayList<>();
        this.tapeNegative.addAll(other.tapeNegative);

        this.tapeOrigin = other.tapeOrigin;

        this.tapePositive = new ArrayList<>();
        this.tapePositive.addAll(other.tapePositive);

        this.headIndex = other.headIndex;
    }

    /**
     * Get the current index of the head on the tape.
     *
     * This returns a negative index if the head is to the left of the origin.
     *
     * @return The current index of the head.
     */
    public int getHeadIndex() {
        return headIndex;
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
        return tapePositive.size() + tapeNegative.size() - 1;
    }

    /**
     * Get the tape index of the leftmost cell in the tape's viewed region.
     *
     * @return The index of the leftmost cell in the tape's viewed region.
     */
    public int getLeftBound() {
        return -1 * (tapeNegative.size() - 1);
    }

    /**
     * Get the tape index of the rightmost cell in the tape's viewed region.
     *
     * @return The index of the rightmost cell in the tape's viewed region.
     */
    public int getRightBound() {
        return tapePositive.size() - 1;
    }

    /**
     * Overwrite the cell at the current head position with the given symbol.
     *
     * @param symbol The symbol to write on the tape.
     */
    public void writeSymbol(String symbol) {
        if (headIndex == 0) {
            tapeOrigin = symbol;
        } else if (headIndex > 0) {
            tapePositive.set(headIndex, symbol);
        } else {
            tapeNegative.set(headIndex * -1, symbol);
        }
    }

    /**
     * Get the symbol on the tape at the current head position.
     *
     * @return The symbol scanned by the tape head.
     */
    public String readSymbol() {
        if (headIndex == 0) {
            return tapeOrigin;
        } else if (headIndex > 0) {
            return tapePositive.get(headIndex);
        } else {
            return tapeNegative.get(headIndex * -1);
        }
    }

    /**
     * Get the symbol at the given index on the tape.
     *
     * @param index The index of the tape cell.
     * @return The symbol on the tape at the given index.
     */
    public String getSymbolAt(int index) {
        if (index < getLeftBound() || index > getRightBound()) {
            return Tape.BLANK_SYMBOL;
        } else if (index == 0) {
            return tapeOrigin;
        } else if (index > 0){
            return tapePositive.get(index);
        } else {
            return tapeNegative.get(-1 * index);
        }
    }

    /**
     * Shift the tape head left along the tape.
     */
    public void shiftLeft() {
        headIndex--;
        if (headIndex < getLeftBound()) {
           tapeNegative.add(Tape.BLANK_SYMBOL);
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
        headIndex++;
        if (headIndex > getRightBound()) {
            tapePositive.add(Tape.BLANK_SYMBOL);
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = getLeftBound(); i <= getRightBound(); i++) {
            if (i == 0) {
                str.append("[" + tapeOrigin + "] ");
            } else {
                str.append(getSymbolAt(i) + " ");
            }
        }
        str.deleteCharAt(str.length() -1); // delete last space
        return str.toString();
    }
}
