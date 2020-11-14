package com.etuproject.friendfindserver.service;

import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.model.RequestGetSimulationUpdates;
import com.etuproject.friendfindserver.model.RequestSimulationTick;
import com.etuproject.friendfindserver.model.ResponseGetSimulationUpdates;
import com.etuproject.friendfindserver.model.ResponseSimulationTick;
import com.etuproject.friendfindserver.repository.InMemoryRepository;
import com.etuproject.friendfindserver.simulation.TickSimulator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Qualifier("SimulationServiceImpl")
public class SimulationServiceImpl implements SimulationService {

    public ResponseSimulationTick tick(RequestSimulationTick requestSimulationTick) {
        User self = InMemoryRepository.getInstance().getUser(requestSimulationTick.getUserName());
        ResponseSimulationTick response = new ResponseSimulationTick();
        if (self.isAdmin()) {
            TickSimulator.getInstance(self)
                    .execute(requestSimulationTick.getTick());
            response.setMessage("Tick successful");
        } else {
            response.setMessage("Tick failed");
        }

        return response;
    }

    public ResponseGetSimulationUpdates getUpdates(RequestGetSimulationUpdates requestGetSimulationUpdates) {
        User self = InMemoryRepository.getInstance().getUser(requestGetSimulationUpdates.getUserName());
        ResponseGetSimulationUpdates response = new ResponseGetSimulationUpdates();
        response.setFriends(new ArrayList<>());
        if (self.isAdmin()) {
            InMemoryRepository.getInstance().getFriendsWithAdminMasked(self, response.getFriends());
        }

        return response;
    }

}
