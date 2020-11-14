package com.etuproject.friendfindserver.lbs;

import com.etuproject.friendfindserver.entity.UserQueryMasked;

import java.util.List;

public interface LBS {

    List<UserQueryMasked> query(String userName);
}
