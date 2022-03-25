package com.ehaney.turingmachineeditor.model;

public class Transition {
    private State fromState;
    private String readSymbol;
    private String writeSymbol;
    private State toState;

     public Transition(State fromState, String readSymbol, String writeSymbol, State toState) {
         this.fromState = fromState;
         this.readSymbol = readSymbol;
         this.writeSymbol = writeSymbol;
         this.toState = toState;
     }

    public void deleteLink() {
         fromState = null;
         toState = null;
    }

    public String getReadSymbol() {
        return readSymbol;
    }

    public void setReadSymbol(String readSymbol) {
         if (fromState.hasTransition(readSymbol)) {
             throw new IllegalStateException("A state cannot have two or more " +
                     "transitions with the same scanned symbol.");
         }

         fromState.addTransition(readSymbol, this.writeSymbol, this.toState);
         fromState.removeTransition(this);
    }

    public String getWriteSymbol() {
        return writeSymbol;
    }

    public void setWriteSymbol(String writeSymbol) {
        this.writeSymbol = writeSymbol;
    }

    @Override
    public String toString() {
        return fromState.getID() + " " +
                toState.getID() + " " +
                readSymbol + " " +
                writeSymbol;
    }

    public State getToState() {
         return toState;
    }

    public State getFromState() {
         return fromState;
    }
}
