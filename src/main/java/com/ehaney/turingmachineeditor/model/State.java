package com.ehaney.turingmachineeditor.model;

public class State {
    enum Command {L, R, H, Y, N}
    String id;
    Command command;

    State(String id, Command command) {
        this.id = id;
        this.command = command;
    }


}
