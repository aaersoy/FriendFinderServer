package com.etuproject.friendfindserver.model;

import com.etuproject.friendfindserver.entity.Edge;
import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.entity.UserAdminMasked;
import com.etuproject.friendfindserver.entity.Vertex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLogin {

    private User self;
    private List<UserAdminMasked> friends;
    private List<Vertex> vertices;
    private List<Edge> edges;
}
