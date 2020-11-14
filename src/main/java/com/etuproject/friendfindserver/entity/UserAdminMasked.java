package com.etuproject.friendfindserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminMasked extends UserQueryMasked {

    private int locationID;

    public UserAdminMasked(String userName, int locationID) {
        super(userName);
        this.locationID = locationID;
    }
}
