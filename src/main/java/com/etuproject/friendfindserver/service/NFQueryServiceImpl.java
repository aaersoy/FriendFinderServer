package com.etuproject.friendfindserver.service;

import com.etuproject.friendfindserver.entity.UserQueryMasked;
import com.etuproject.friendfindserver.exception.InvalidQueryMethodException;
import com.etuproject.friendfindserver.lbs.LBS;
import com.etuproject.friendfindserver.lbs.StrongAnonymityLBS;
import com.etuproject.friendfindserver.lbs.UnconstrainedLBS;
import com.etuproject.friendfindserver.lbs.WeakAnonymityLBS;
import com.etuproject.friendfindserver.model.RequestChangeQueryMethod;
import com.etuproject.friendfindserver.model.RequestFindFriends;
import com.etuproject.friendfindserver.model.ResponseChangeQueryMethod;
import com.etuproject.friendfindserver.model.ResponseFindFriends;
import com.etuproject.friendfindserver.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("NFQueryServiceImpl")
public class NFQueryServiceImpl implements NFQueryService {

    private static LBS lbs;

    public ResponseFindFriends query(RequestFindFriends requestFindFriends) {
        if (lbs == null) {
            lbs = new UnconstrainedLBS();
        }
        List<UserQueryMasked> result =  lbs.query(requestFindFriends.getUserName());
        ResponseFindFriends response = new ResponseFindFriends();


        response.setFriends(result);
        List<UserQueryMasked> user=response.getFriends();
        for (int i = 0; i < user.size(); i++) {
            System.out.println(user.get(i).getUserName());
        }

        System.out.println(result);
        return response;
    }

    public ResponseChangeQueryMethod changeQueryMethod(RequestChangeQueryMethod requestChangeQueryMethod) throws InvalidQueryMethodException {
        ResponseChangeQueryMethod response = new ResponseChangeQueryMethod();
        if (InMemoryRepository.getInstance().getUser(requestChangeQueryMethod.getUserName()).isAdmin()) {
            response.setQueryMethod(requestChangeQueryMethod.getQueryMethod());
            if (requestChangeQueryMethod.getQueryMethod() == 0) {
                lbs = new UnconstrainedLBS();
            } else if (requestChangeQueryMethod.getQueryMethod() == 1) {
                lbs = new WeakAnonymityLBS();
            } else if (requestChangeQueryMethod.getQueryMethod() == 2) {
                lbs = new StrongAnonymityLBS();
            } else {
                throw new InvalidQueryMethodException();
            }
        } else {
            throw new InvalidQueryMethodException();
        }
        return response;
    }
}
