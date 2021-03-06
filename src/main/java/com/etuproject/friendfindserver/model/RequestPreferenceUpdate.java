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
public class RequestPreferenceUpdate {

    @NotNull()
    private String userName;
    private int interestRadius;
    private int anonymityLevel;
}
