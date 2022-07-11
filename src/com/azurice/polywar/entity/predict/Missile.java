package com.azurice.polywar.entity.predict;

import com.azurice.polywar.entity.Wall;
import com.azurice.polywar.model.Model;
import com.azurice.polywar.network.data.MissileData;
import com.azurice.polywar.server.GamePlayer;
import com.azurice.polywar.server.Room;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class Missile extends SpeedDirectionEntity {
    public static final int SPEED = 10;
    public int ownerId;

    ////// Constructors //////
//    public Missile(Vec2d coord, double angle) {
//        this(coord, Vec2d.U.multiply(SPEED).rotate(angle));
//    }

    public Missile(Vec2d coord, Vec2d speed, int ownerId, Color color) {
        super(coord, Model.MISSILE, color);
        this.speed = speed;
        this.ownerId = ownerId;
        lastTickCoord = new Vec2d(coord);
        lastTickSpeed = new Vec2d(speed);
        updateLastTickTime();
    }

    public Missile(Vec2d coord, Vec2d speed, int ownerId) {
        super(coord, Model.MISSILE);
        this.speed = speed;
        this.ownerId = ownerId;
        lastTickCoord = new Vec2d(coord);
        lastTickSpeed = new Vec2d(speed);
        updateLastTickTime();
    }


    public void tick(Room room) {
        for (int i = 0; i < room.gamePlayers.size(); i++) {
            GamePlayer gamePlayer = room.gamePlayers.get(room.playerList.get(i).id);
            if (gamePlayer.getCoord().minus(coord).length() <= 16 && gamePlayer.id != ownerId) {
                gamePlayer.decHealth();
                room.removeMissile(this);
                return;
            }
        }

        for (Wall wall : room.map.walls) {
            Vec2d collisionPoint = getPolygon().intersect(wall.getPolygon());
            if (collisionPoint != Vec2d.ZERO) {
                room.removeMissile(this);
            }
        }

        if (coord.x > Room.MAP_SIZE || coord.x < 0 || coord.y > Room.MAP_SIZE || coord.y < 0)
            room.removeMissile(this);

        super.tick();
    }

    public MissileData getMissileData() {
        return new MissileData(coord, speed, ownerId);
    }
}
