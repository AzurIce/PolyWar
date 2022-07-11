package com.azurice.polywar.server;

import com.azurice.polywar.network.data.GamePlayerControlData;
import com.azurice.polywar.network.data.GamePlayerData;
import com.azurice.polywar.network.packet.GamePlayerDataListPacket;
import com.azurice.polywar.network.packet.GamePlayerDataPacket;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    private static final PolyWarServer server = PolyWarServer.getInstance();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MAP_SIZE = 4096;
    private static final Random r = new Random();

    //
    public int id;
    public Player owner;
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
//        LOGGER.info("tick Playing: {}", playing);
        if (playing) {
//            LOGGER.info("PlayerList {} ticking...", playerList);
            for (int i = 0; i < playerList.size(); i++) {
//                LOGGER.info("Ticking GamePlayer{}...", gamePlayers.get(playerList.get(i).id));
                gamePlayers.get(playerList.get(i).id).tick();
            }
            sendPlayerDataListPacket();
        }

    }


    public void sendPlayerDataListPacket() {
        List<GamePlayerData> gamePlayerDataList = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            gamePlayerDataList.add(gamePlayers.get(playerList.get(i).id).getGamePlayerData());
        }
        for (int i = 0; i < playerList.size(); i++) {
            server.sendPacket(playerList.get(i).socketChannel, GamePlayerDataListPacket.of(gamePlayerDataList));
        }
//        LOGGER.info("Sending PlayerDataList: {}", gamePlayerDataList);

    }


    public void startGame() {
        gamePlayers = Collections.synchronizedMap(new HashMap<>());
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            GamePlayer gameplayer = new GamePlayer(
                    new Vec2d(r.nextInt(0, MAP_SIZE), r.nextInt(0, MAP_SIZE)),
                    map,
                    player.id
            );
            while (!map.isAvailableToSpawnAt(gameplayer.getCoord())) {
                gameplayer.setCoord(new Vec2d(r.nextInt(0, MAP_SIZE), r.nextInt(0, MAP_SIZE)));
            }
            server.sendPacket(player.socketChannel, GamePlayerDataPacket.of(gameplayer.getGamePlayerData()));
            gamePlayers.put(player.id, gameplayer);
        }
        playing = true;
//        LOGGER.info("Playing: {}", playing);
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
}
