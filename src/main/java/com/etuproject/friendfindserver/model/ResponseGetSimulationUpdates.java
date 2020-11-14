package com.etuproject.friendfindserver.model;

import com.etuproject.friendfindserver.entity.UserAdminMasked;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetSimulationUpdates {

    private List<UserAdminMasked> friends;
}
