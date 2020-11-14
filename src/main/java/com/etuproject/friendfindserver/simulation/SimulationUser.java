package com.etuproject.friendfindserver.simulation;

import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.entity.Vertex;
import com.etuproject.friendfindserver.repository.InMemoryRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SimulationUser implements Tickable {

    private User self;
    private Movement movement;
    private static final double NEW_MOVEMENT_CHANCE = 0.05;

    SimulationUser(User self) {
        this.self = self;
    }

    @Override
    public void tick(int seconds) {
        if (movement == null) {
            decideMovement();
        }
        if (movement != null) {
            int newLocationID = movement.move(seconds);
            if (newLocationID != self.getLocationID()) {
                System.out.println("User " + self.getUserName() + " has moved from " + self.getLocationID() + " to "+ newLocationID);
                self.setLocationID(newLocationID);
            }

            if (movement.isFinished()) {
                System.out.println("User " + self.getUserName() + " has finished moving.");
                movement = null;
            }
        }
    }

    private void decideMovement() {
        double chance = Math.random();
        if (chance < NEW_MOVEMENT_CHANCE) {
            int destination = getRandomDestination(self.getLocationID());
            GraphPath<Integer, DefaultWeightedEdge> path =
                    DijkstraShortestPath.findPathBetween(InMemoryRepository.graph, self.getLocationID(), destination);
            this.movement = new Movement(path.getEdgeList());
            System.out.println(self.getUserName() + " has decided to move from " + self.getLocationID() + " to "+ destination);
            System.out.print(self.getUserName() + "'s path will be: ");
            String pathString = "" + InMemoryRepository.graph.getEdgeSource(path.getEdgeList().get(0));
            for (int i = 0; i < path.getEdgeList().size(); ++i) {
                pathString += (" -> " + InMemoryRepository.graph.getEdgeTarget(path.getEdgeList().get(i)));
            }
            System.out.println(pathString);
        }
    }

    private int getRandomDestination(int currentLocationID) {
        int randomID;
        do {
            randomID = (int)(Math.random() * InMemoryRepository.getInstance().getVertexCount());
        } while (randomID == currentLocationID);

        return randomID;
    }
}
