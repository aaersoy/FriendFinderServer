package com.etuproject.friendfindserver.service;

import com.etuproject.friendfindserver.exception.AuthenticationFailedException;
import com.etuproject.friendfindserver.exception.PreferenceUpdateException;
import com.etuproject.friendfindserver.exception.UserAlreadyExistException;
import com.etuproject.friendfindserver.model.*;

public interface UserService {

    ResponseLogin login(RequestLogin requestLogin) throws AuthenticationFailedException;
    ResponseLocationUpdate updateLocation(RequestLocationUpdate requestLocationUpdate);
    ResponsePreferenceUpdate updatePreferences(RequestPreferenceUpdate requestPreferenceUpdate) throws PreferenceUpdateException;
    ResponseSignUp signUp(RequestSignUp requestSignUp) throws UserAlreadyExistException;
}
