package com.etuproject.friendfindserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Preference {

    private int interestRadius;
    private int anonymityLevel;


}
