package com.etuproject.friendfindserver.controller;

import com.etuproject.friendfindserver.exception.AuthenticationFailedException;
import com.etuproject.friendfindserver.exception.InvalidQueryMethodException;
import com.etuproject.friendfindserver.exception.PreferenceUpdateException;
import com.etuproject.friendfindserver.exception.UserAlreadyExistException;
import com.etuproject.friendfindserver.model.*;
import com.etuproject.friendfindserver.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class MobileAppController extends AbstractAPIController {
    private Logger logger = LoggerFactory.getLogger("Mobile App API");

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Qualifier("UserServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("NFQueryServiceImpl")
    private NFQueryService NFQueryService;

    @Autowired
    @Qualifier("SimulationServiceImpl")
    private SimulationService simulationService;


    /**
     * Login API
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> login(@Valid @RequestBody RequestLogin requestLogin, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponseLogin responseLogin = userService.
                    login(requestLogin);

            return new ResponseEntity<>(responseLogin, HttpStatus.ACCEPTED);
        } catch (AuthenticationFailedException e) {
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * SignUp API
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/sign_up", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> signUp(@Valid @RequestBody RequestSignUp requestSignUp, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponseSignUp responseSignUp = userService.signUp(requestSignUp);
            return new ResponseEntity<>(responseSignUp, HttpStatus.ACCEPTED);
        } catch (UserAlreadyExistException e) {
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Update Preferences API
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/update_preferences", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> updatePreference(@Valid @RequestBody RequestPreferenceUpdate requestPreferenceUpdate, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponsePreferenceUpdate res = userService.updatePreferences(requestPreferenceUpdate);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (PreferenceUpdateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update Location API
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/update_location", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> updateLocation(@Valid @RequestBody RequestLocationUpdate requestLocationUpdate, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponseLocationUpdate responseLocationUpdate = userService.updateLocation(requestLocationUpdate);
            if (responseLocationUpdate != null) {
                return new ResponseEntity<>(responseLocationUpdate, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Find Friends API
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/find_friends", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> findFriends(@Valid @RequestBody RequestFindFriends requestFindFriends, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponseFindFriends responseFindFriends = NFQueryService.query(requestFindFriends);
            return new ResponseEntity<>(responseFindFriends, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Simulation Tick API
     */
    @RequestMapping(value = "/simulation_tick", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> simulationTick(@Valid @RequestBody RequestSimulationTick requestSimulationTick, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponseSimulationTick responseSimulationTick = simulationService.tick(requestSimulationTick);
            return new ResponseEntity<>(responseSimulationTick, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get Simulation Updates API
     */
    @RequestMapping(value = "/get_simulation_updates", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> getSimulationUpdates(@Valid @RequestBody RequestGetSimulationUpdates request, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponseGetSimulationUpdates response = simulationService.getUpdates(request);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Change Query Method API
     */
    @RequestMapping(value = "/change_query_method", method = RequestMethod.POST, headers = "Accept=application/json", produces = "application/zip")
    public ResponseEntity<?> changeQueryMethod(@Valid @RequestBody RequestChangeQueryMethod request, Errors errors) {
        try {
            if (errors.hasErrors()) {
                String errorMessage = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));
                return new ResponseEntity<>(produceJSONResponse(errorMessage), HttpStatus.BAD_REQUEST);
            }

            ResponseChangeQueryMethod response = NFQueryService.changeQueryMethod(request);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (InvalidQueryMethodException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(produceJSONResponse(INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
