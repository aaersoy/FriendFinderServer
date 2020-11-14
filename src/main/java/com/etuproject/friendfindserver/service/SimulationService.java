package com.etuproject.friendfindserver.service;

import com.etuproject.friendfindserver.model.RequestGetSimulationUpdates;
import com.etuproject.friendfindserver.model.RequestSimulationTick;
import com.etuproject.friendfindserver.model.ResponseGetSimulationUpdates;
import com.etuproject.friendfindserver.model.ResponseSimulationTick;

public interface SimulationService {

    ResponseSimulationTick tick(RequestSimulationTick requestSimulationTick);
    ResponseGetSimulationUpdates getUpdates(RequestGetSimulationUpdates requestGetSimulationUpdates);
}
