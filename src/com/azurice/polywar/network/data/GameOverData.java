package com.azurice.polywar.network.data;

import java.io.Serializable;

public class GameOverData implements Serializable {
    int rank;
    int shoot;
    double distance;

    public GameOverData(int rank, int shoot, double distance) {
        this.rank = rank;
        this.shoot = shoot;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameOverData<rank: " + rank + ", shoot: " + shoot + ", distance: " + distance + ">";
    }
}
