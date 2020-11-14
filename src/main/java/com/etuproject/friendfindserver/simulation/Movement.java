package com.etuproject.friendfindserver.simulation;

import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.repository.InMemoryRepository;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

@Getter
@Setter
class Movement {

    private double secondsPassedInCurrentLocation;
    private List<DefaultWeightedEdge> path;
    private int currentLocation;
    private boolean isFinished;

    Movement(List<DefaultWeightedEdge> path) {
        this.path = path;
        this.currentLocation = 0;
        this.secondsPassedInCurrentLocation = 0;
        this.isFinished = false;
    }

    int move(double seconds) {
        secondsPassedInCurrentLocation += seconds;
        double secondsWillTakeForNextHop = InMemoryRepository.graph.getEdgeWeight(path.get(currentLocation));
        while (secondsPassedInCurrentLocation > secondsWillTakeForNextHop) {
            currentLocation++;
            if (currentLocation == path.size()) {
                isFinished = true;
                return InMemoryRepository.graph.getEdgeTarget(path.get(currentLocation -1));
            }
            secondsPassedInCurrentLocation -= secondsWillTakeForNextHop;
            secondsWillTakeForNextHop = InMemoryRepository.graph.getEdgeWeight(path.get(currentLocation));
        }
        return InMemoryRepository.graph.getEdgeSource(path.get(currentLocation));
    }
}
