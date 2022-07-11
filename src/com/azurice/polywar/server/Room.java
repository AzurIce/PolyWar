package com.azurice.polywar.server;

import com.azurice.polywar.entity.predict.Missile;
import com.azurice.polywar.network.data.GamePlayerControlData;
import com.azurice.polywar.network.data.GamePlayerData;
import com.azurice.polywar.network.data.MissileData;
import com.azurice.polywar.network.packet.GamePlayerDataListPacket;
import com.azurice.polywar.network.packet.GamePlayerDataPacket;
import com.azurice.polywar.network.packet.MissileDataListPacket;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    private static final PolyWarServer server = PolyWarServer.getInstance();
    private static final Logger LOGGER = LogManager.getLogger();
    public static final int MAP_SIZE = 4096;
    private static final Random r = new Random();

    //
    public int id;
    public Player owner;
    public List<Missile> missileList = Collections.synchronizedList(new ArrayList<>());
    public List<Player> playerList = Collections.synchronizedList(new ArrayList<>());
    public WorldMap map;

    public boolean playing = false;

    // Game
    public Map<Integer, GamePlayer> gamePlayers = Collections.synchronizedMap(new HashMap<>());

    public Room(int id, Player owner, WorldMap map) {
        this.id = id;
        this.owner = owner;
        this.playerList.add(owner);
        this.map = map;
    }

    public void regenerateMap() {
        map = WorldMap.generateWorldMap();
    }


    public void tick() {
        if (playerList.size() == 0) {
            server.removeRoom(this);
        }
        if (playing) {
            for (int i = 0; i < playerList.size(); i++) {
                gamePlayers.get(playerList.get(i).id).tick(this);
            }
            sendPlayerDataListPacket();
            for (int i = 0; i < missileList.size(); i++) {
                missileList.get(i).tick(this);
            }
            sendMissileListPacket();
        }

    }


    public void removeMissile(Missile missile) {
        missileList.remove(missile);
    }

    public void sendPlayerDataListPacket() {
        List<GamePlayerData> gamePlayerDataList = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            gamePlayerDataList.add(gamePlayers.get(playerList.get(i).id).getGamePlayerData());
        }
        for (int i = 0; i < playerList.size(); i++) {
            server.sendPacket(playerList.get(i).socketChannel, GamePlayerDataListPacket.of(gamePlayerDataList));
        }
    }

    public void sendMissileListPacket() {
        List<MissileData> missileDataList = new ArrayList<>();
        for (int i = 0; i < missileList.size(); i++) {
            missileDataList.add(missileList.get(i).getMissileData());
        }
        for (int i = 0; i < playerList.size(); i++) {
            server.sendPacket(playerList.get(i).socketChannel, MissileDataListPacket.of(missileDataList));
        }
    }


    public void startGame() {
        gamePlayers = Collections.synchronizedMap(new HashMap<>());
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            GamePlayer gameplayer = new GamePlayer(Vec2d.rand(MAP_SIZE), map, player.id);
            while (!map.isAvailableToSpawnAt(gameplayer.getCoord())) {
                gameplayer.setCoord(Vec2d.rand(MAP_SIZE));
            }
            LOGGER.info("Sending PlayerData: {}", gameplayer.getGamePlayerData());
            server.sendPacket(player.socketChannel, GamePlayerDataPacket.of(gameplayer.getGamePlayerData()));
            gamePlayers.put(player.id, gameplayer);
        }
        playing = true;
    }

    public Room(int id, Player owner) {
        this(id, owner, WorldMap.generateWorldMap(MAP_SIZE));
    }

    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    public void removePlayer(Player player) {
        this.playerList.remove(player);
    }

    @Override
    public String toString() {
        return "Room<Id: " + id + ", owner: " + owner + ", players: " + playerList + ">";
    }

    public void updatePlayerControlData(Player player, GamePlayerControlData gamePlayerControlData) {
        gamePlayers.get(player.id).setGamePlayerControlData(gamePlayerControlData);
    }

    public void addMissile(Missile missile) {
        this.missileList.add(missile);
    }
}
