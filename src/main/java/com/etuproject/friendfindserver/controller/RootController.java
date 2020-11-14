package com.etuproject.friendfindserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RootController extends AbstractAPIController{

    @RequestMapping( method = RequestMethod.GET, headers="Accept=application/json")
    public String index(){
        return produceJSONResponse("FriendFindv0.1");
    }
}
