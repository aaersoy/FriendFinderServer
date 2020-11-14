package com.etuproject.friendfindserver.entity;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class User extends UserAdminMasked {

    private Integer id;
    private String password;
    private boolean isAdmin;
    private Preference preference;

    public User(Integer id, String userName, String password, int interestRadius, int anonymityLevel) {
        super.setUserName(userName);
        super.setLocationID(0);
        this.id = id;
        this.password = password;
        this.isAdmin = false;
        this.preference = new Preference(interestRadius, anonymityLevel);
    }

    public User(Integer id, String userName, String password) {
        this(id, userName, password, 500, 500);
    }
}
