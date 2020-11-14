package com.etuproject.friendfindserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestSimulationTick {

    private String userName;
    private int tick;   // -1: Periodic tick; Other positive numbers: Single tick with that number
}
