package com.azurice.polywar.server;

import com.azurice.polywar.entity.Wall;
import com.azurice.polywar.entity.predict.Missile;
import com.azurice.polywar.entity.predict.SpeedDirectionEntity;
import com.azurice.polywar.model.Model;
import com.azurice.polywar.network.data.GamePlayerControlData;
import com.azurice.polywar.network.data.GamePlayerData;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;

import static com.azurice.polywar.world.WorldMap.MAP_SIZE;
import static java.lang.Math.abs;

public class GamePlayer extends SpeedDirectionEntity {
//    private static final Logger LOGGER = LogManager.getLogger();
//vvvvvv CONSTANTS vvvvvv//
    private static final double FRICTION = 0.01;
    private static final double ACC = 4;
    private static final int SHOOT_COOL_DOWN = 10;
    private static final int CIRCLE_HEALTH_DESC_COOL_DOWN = 20;
    //^^^^^^ CONSTANTS ^^^^^^//
    private final WorldMap worldMap;
    protected Vec2d acceleration = Vec2d.ZERO;
    GamePlayerControlData gamePlayerControlData = new GamePlayerControlData();
    //vvvvvv PROPERTIES vvvvvv//
    public int id;
    private int health = 100;
    private int shootCoolDown = 0;
    private int circleHealthDescCoolDown = 0;
    public int shootCount = 0;
    public double distance = 0;

    //^^^^^^ PROPERTIES ^^^^^^//


    //vvvvvv CONSTRUCTOR vvvvvv//
    public GamePlayer(Vec2d coord, WorldMap worldMap, int id) {
        super(coord, Model.PLAYER);
        this.worldMap = worldMap;
        this.id = id;
    }
    //^^^^^^ CONSTRUCTOR ^^^^^^//

    //vvvvvv NETWORK vvvvvv//
    public GamePlayerData getGamePlayerData() {
        return new GamePlayerData(id, health, getCoord(), getSpeed());
    }
    //^^^^^^ NETWORK ^^^^^^//

    public void setGamePlayerControlData(GamePlayerControlData gamePlayerControlData) {
        this.gamePlayerControlData = gamePlayerControlData;
    }

    public void tick(Room room) {
        if (coord.minus(new Vec2d(MAP_SIZE / 2d, MAP_SIZE / 2d)).length() > room.map.radius) {
            if (circleHealthDescCoolDown <= 0) {
                descHealth();
                circleHealthDescCoolDown = CIRCLE_HEALTH_DESC_COOL_DOWN;
            }
            circleHealthDescCoolDown--;
        }
        if (room.gamePlayers.size() - room.diedGamePlayerIds.size() == 1) {
            room.killGamePlayer(this);
            return;
        }
        if (health <= 0) {
            room.killGamePlayer(this);
            return;
        }

        shootCoolDown--;
        if (gamePlayerControlData.keyShootPressed && shootCoolDown <= 0) {
            room.addMissile(new Missile(coord, speed.add(Vec2d.D.rotate(getAngle()).multiply(Missile.SPEED)), id));
            room.sendMissileSoundPacket(id);
            shootCoolDown = SHOOT_COOL_DOWN;
            shootCount++;
        }

//        LOGGER.info("Ticking GamePlayer: {}", this);
        acceleration = Vec2d.ZERO;
        if (gamePlayerControlData.keyUpPressed) {
            acceleration = acceleration.add(new Vec2d(0, -ACC));
        }
        if (gamePlayerControlData.keyDownPressed) {
            acceleration = acceleration.add(new Vec2d(0, ACC));
        }
        if (gamePlayerControlData.keyLeftPressed) {
            acceleration = acceleration.add(new Vec2d(-ACC, 0));
        }
        if (gamePlayerControlData.keyRightPressed) {
            acceleration = acceleration.add(new Vec2d(+ACC, 0));
        }

        for (Wall wall : worldMap.walls) {
            Vec2d collisionPoint = getPolygon().intersect(wall.getPolygon());
            if (collisionPoint != Vec2d.ZERO) {
                while (collisionPoint != Vec2d.ZERO) {
                    coord = coord.minus(collisionPoint.minus(coord).normalize().add(speed.normalize()));
                    collisionPoint = getPolygon().intersect(wall.getPolygon());
                }
                speed = Vec2d.ZERO;
            }
        }

        speed = speed.add(acceleration);
        if (abs(speed.length()) < FRICTION * (speed.length() * speed.length() + 2 * speed.length())) {
            speed = Vec2d.ZERO;
        } else {
            speed = speed.minus(speed.normalize().multiply(FRICTION * (1.2 * speed.length() * speed.length() + 2 * speed.length())));
        }
        if (abs(speed.x) < 0.5) {
            speed = new Vec2d(0, speed.y);
        }
        if (abs(speed.y) < 0.5) {
            speed = new Vec2d(speed.x, 0);
        }

        distance += speed.length();
        super.tick();
    }


    public void descHealth() {
        health -= 10;
    }


    @Override
    public String toString() {
        return "GamePlayer<coord: " + coord + ", speed: " + speed + ", health: " + health + ">";
    }
}
