package com.azurice.polywar.server.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DatabaseHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DatabaseHelper instance = new DatabaseHelper();
    private final Connection connection;

    ;


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
//            statement.executeUpdate(
//                    "CREATE TABLE GAME_RECORD " +
//                            "(ID                     INT PRIMARY KEY NOT NULL," +
//                            " GAME_PLAYERS_RECORD_ID INT             NOT NULL," +
//                            " START_TIME             TEXT            NOT NULL," +
//                            " END_TIME               TEXT            NOT NULL," +
//                            " WINNER_PLAYER_ID       INT             NOT NULL);"
//            );
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

    /**
     * Update player data(Create the player if not exist)
     *
     * @param name name
     * @return the player id
     */
    public int updatePlayer(String name) {
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

    public void createPlayer(String name, boolean online) {

    }
}
