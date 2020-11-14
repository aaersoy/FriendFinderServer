package com.etuproject.friendfindserver.controller;

import com.etuproject.friendfindserver.model.JSONResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

public abstract class AbstractAPIController {

    private Logger logger = LoggerFactory.getLogger(AbstractAPIController.class);

    protected final String INTERNAL_SERVER_ERROR = "Internal Server Error occurred";
    protected final String NOT_AUTHORIZED_ERROR = "User is not authorized! Please check your credentials!";
    protected final String JSON_VALIDATION_ERROR = "JSON is not valid";
    protected final String JSON_PROCESSING_ERROR = "Internal server error occurred while generating response!";
    protected final String OK = "OK";

    @Autowired
    ObjectMapper objectMapper;

    protected String produceJSONResponse(String result) {
        try {
            return objectMapper.writeValueAsString(new JSONResponse(result));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JSON_PROCESSING_ERROR;
        }
    }
}