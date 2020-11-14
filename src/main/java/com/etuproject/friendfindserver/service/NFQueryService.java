package com.etuproject.friendfindserver.service;

import com.etuproject.friendfindserver.exception.InvalidQueryMethodException;
import com.etuproject.friendfindserver.model.RequestChangeQueryMethod;
import com.etuproject.friendfindserver.model.RequestFindFriends;
import com.etuproject.friendfindserver.model.ResponseChangeQueryMethod;
import com.etuproject.friendfindserver.model.ResponseFindFriends;

public interface NFQueryService {

    ResponseFindFriends query(RequestFindFriends requestFindFriends);
    ResponseChangeQueryMethod changeQueryMethod(RequestChangeQueryMethod requestChangeQueryMethod) throws InvalidQueryMethodException;
}
