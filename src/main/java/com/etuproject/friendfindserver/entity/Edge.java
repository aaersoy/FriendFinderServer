package com.etuproject.friendfindserver.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Edge {

    private int id;
    private int from;
    private int to;
    private double distance;

    public Edge(int id, int from, int to, double distance) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
}
