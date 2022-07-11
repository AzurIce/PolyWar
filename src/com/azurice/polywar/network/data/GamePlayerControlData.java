package com.azurice.polywar.network.data;

import java.io.Serializable;

public class GamePlayerControlData implements Serializable {
    public boolean keyUpPressed;
    public boolean keyDownPressed;
    public boolean keyLeftPressed;
    public boolean keyRightPressed;
    public boolean keyShootPressed;

    public GamePlayerControlData() {
        this(false, false, false, false, false);
    }

    public GamePlayerControlData(boolean keyUpPressed, boolean keyDownPressed,
                                 boolean keyLeftPressed, boolean keyRightPressed, boolean keyShootPressed) {
        this.keyUpPressed = keyUpPressed;
        this.keyDownPressed = keyDownPressed;
        this.keyLeftPressed = keyLeftPressed;
        this.keyRightPressed = keyRightPressed;
        this.keyShootPressed = keyShootPressed;
    }

    @Override
    public String toString() {
        return "GamePlayerControlData<u: " + keyUpPressed + ", d: " + keyDownPressed +
                ", l: " + keyLeftPressed + ", r: " + keyRightPressed + ", shoot: " + keyShootPressed + ">";
    }
}
