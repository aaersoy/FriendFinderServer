package com.etuproject.friendfindserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestLocationUpdate {

    @NotNull()
    private String userName;
    @NotNull()
    private double latitude;
    @NotNull()
    private double longitude;
}
