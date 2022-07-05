package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.GamePage;
import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

public class Missile extends BaseDrawableEntity {
    public Missile(Vec2d coord, GamePage gamePage) {
        super(coord, gamePage, new Polygon(
                new Vec2d(2, -5), new Vec2d(-2, -5), new Vec2d(-2, 5), new Vec2d(2, 5))
        );
    }

}
