package com.azurice.polywar.server.database;

import com.azurice.polywar.network.data.GameOverData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseHelper.class);

    private static final DatabaseHelper instance = new DatabaseHelper();
    private final Connection connection;


    private DatabaseHelper() {

        LOGGER.info("Initializing database...");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:polywar.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS PLAYER " +
                            "(ID    INTEGER  PRIMARY KEY AUTOINCREMENT," +
                            " NAME  TEXT     UNIQUE      NOT NULL);"
            );
            statement.close();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS GAME_OVER_DATA " +
                            "(ID              INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "  GAME_PLAYER_ID INT                 NOT NULL," +
                            " RANK            INT                 NOT NULL," +
                            " SHOOT           INT                 NOT NULL," +
                            " DISTANCE        INT                 NOT NULL);"
            );
//            statement.executeUpdate(
//                    "CREATE TABLE GAME_PLAYERS_RECORD " +
//                            "(ID                INT PRIMARY KEY NOT NULL," +
//                            " PLAYERS_RECORD_ID INT             NOT NULL," +
//                            " START_TIME        TEXT            NOT NULL," +
//                            " END_TIME          TEXT            NOT NULL," +
//                            " WINNER_PLAYER_ID  INT             NOT NULL);"
//            );

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static DatabaseHelper getInstance() {
        return instance;
    }

    public void insertGameOverData(int gamePlayerId, GameOverData gameOverData) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT INTO GAME_OVER_DATA(GAME_PLAYER_ID, RANK, SHOOT, DISTANCE)" +
                            " values(" + gamePlayerId + ", " + gameOverData.rank +
                            ", " + gameOverData.shoot + "," + gameOverData.distance + ");"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GameOverData> getGameOverDataListByPlayerId(int playerId) {
        List<GameOverData> gameOverDataList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM GAME_OVER_DATA WHERE GAME_PLAYER_ID = " + playerId + ";"
            );
            while (rs.next()) {
                gameOverDataList.add(
                        new GameOverData(rs.getInt("RANK"),
                                rs.getInt("SHOOT"),
                                rs.getInt("DISTANCE"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameOverDataList;
    }

    /**
     * Insert a player if not exist
     *
     * @param name name
     * @return the player id
     */
    public int insertPlayerIfNotExist(String name) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "INSERT OR IGNORE INTO PLAYER(NAME)" +
                            " values('" + name + "');");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return getPlayerIdByName(name);
    }

    public int getPlayerIdByName(String name) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM PLAYER WHERE NAME LIKE '" + name + "';"
            );
            return rs.next() ? rs.getInt("ID") : -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPlayerNameById(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM PLAYER WHERE id = " + id + ";"
            );
            return rs.next() ? rs.getString("NAME") : "NULL";
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
