package com.etuproject.friendfindserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestChangeQueryMethod {

    private String userName;
    private int queryMethod;
}
