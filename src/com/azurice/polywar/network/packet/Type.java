package com.azurice.polywar.network.packet;

public enum Type {
    PING_PACKET(false),

    GET_ROOM_LIST_PACKET(false), ROOM_LIST_PACKET(true),
    CREATE_ROOM_PACKET(false), ROOM_PACKET(true), EXIT_ROOM_PACKET(false),

    PLAYER_LIST_PACKET(true),
    REGENERATE_MAP_PACKET(false), MAP_PACKET(true), START_GAME_PACKET(false),
    GAME_PLAYER_DATA_PACKET(true), GAME_PLAYER_CONTROL_DATA(true), GAME_PLAYER_DATA_LIST_PACKET(true), MISSILE_DATA_LIST_PACKET(true), END_GAME_PACKET(true),

    NAME_PACKET(true), NAME_VALID_PACKET(false), NAME_INVALID_PACKET(false);

    public final boolean hasContent;

    Type(boolean hasContent) {
        this.hasContent = hasContent;
    }
}
