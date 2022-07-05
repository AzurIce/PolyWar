package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.GamePage;
import com.azurice.polywar.util.math.Vec2d;

public abstract class AbstractEntity {
    protected Vec2d speed = Vec2d.ZERO;
    protected Vec2d coord;
    protected GamePage gamePage;

    public AbstractEntity(Vec2d coord, GamePage gamePage) {
        this.coord = coord;
        this.gamePage = gamePage;
    }

    public void tick() {
        coord = coord.add(speed);
    }
}
