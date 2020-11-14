package com.etuproject.friendfindserver.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class Vertex implements Serializable {

    private int id;
    private double latitude;
    private double longitude;

    public Vertex(int id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Vertex() {
        this.id = -1;
        this.latitude = 0;
        this.longitude = 0;
    }
}
