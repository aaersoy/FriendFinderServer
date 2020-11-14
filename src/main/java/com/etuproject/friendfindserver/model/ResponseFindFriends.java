package com.etuproject.friendfindserver.model;

import com.etuproject.friendfindserver.entity.UserQueryMasked;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFindFriends {

    private List<UserQueryMasked> friends;
}
