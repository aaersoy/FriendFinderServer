package com.etuproject.friendfindserver.service;

import com.etuproject.friendfindserver.entity.Edge;
import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.entity.UserAdminMasked;
import com.etuproject.friendfindserver.entity.Vertex;
import com.etuproject.friendfindserver.exception.AuthenticationFailedException;
import com.etuproject.friendfindserver.exception.PreferenceUpdateException;
import com.etuproject.friendfindserver.exception.UserAlreadyExistException;
import com.etuproject.friendfindserver.model.*;
import com.etuproject.friendfindserver.repository.InMemoryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("UserServiceImpl")
public class UserServiceImpl implements UserService {

    public ResponseLocationUpdate updateLocation(RequestLocationUpdate requestLocationUpdate) {
        int newLocationID = InMemoryRepository.getInstance()
                .updateLocation(requestLocationUpdate.getUserName(),
                        requestLocationUpdate.getLatitude(),
                        requestLocationUpdate.getLongitude());

        return new ResponseLocationUpdate(newLocationID);
    }

    public ResponseLogin login(RequestLogin requestLogin) throws AuthenticationFailedException {

        if (InMemoryRepository.getInstance().authenticate(requestLogin.getUserName(), requestLogin.getPassword())) {
            ResponseLogin res = new ResponseLogin();
            User u = InMemoryRepository.getInstance().getUser(requestLogin.getUserName());
            res.setSelf(u);

            List<UserAdminMasked> population = new ArrayList<>();
            List<Edge> edges = new ArrayList<>();
            List<Vertex> vertices = new ArrayList<>();
            InMemoryRepository.getInstance().getVertices(vertices);
            if (u.isAdmin()) {
                InMemoryRepository.getInstance().getFriendsWithAdminMasked(u, population);
                InMemoryRepository.getInstance().getEdges(edges);
            }
            res.setFriends(population);
            res.setEdges(edges);
            res.setVertices(vertices);
            return res;
        } else {
            throw new AuthenticationFailedException();
        }
    }

    public ResponsePreferenceUpdate updatePreferences(RequestPreferenceUpdate requestPreferenceUpdate) throws PreferenceUpdateException {
        boolean result =  InMemoryRepository.getInstance()
                .setPreference(requestPreferenceUpdate.getUserName(),
                        requestPreferenceUpdate.getInterestRadius(),
                        requestPreferenceUpdate.getAnonymityLevel());
        if (result) {
            return new ResponsePreferenceUpdate("Update successful");
        } else {
            throw new PreferenceUpdateException();
        }
    }

    public ResponseSignUp signUp(RequestSignUp requestSignUp) throws UserAlreadyExistException {
        boolean result = InMemoryRepository.getInstance().signUp(requestSignUp.getUserName(),
                requestSignUp.getPassword(), requestSignUp.getInterestRadius(), requestSignUp.getAnonymityLevel());

        if (!result) {
            throw new UserAlreadyExistException();
        }
        return new ResponseSignUp("User is Signed Up");
    }
}
