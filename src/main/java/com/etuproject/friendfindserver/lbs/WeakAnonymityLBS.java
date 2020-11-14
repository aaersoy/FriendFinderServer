package com.etuproject.friendfindserver.lbs;

import com.etuproject.friendfindserver.entity.Belief;
import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.entity.UserQueryMasked;
import com.etuproject.friendfindserver.repository.InMemoryRepository;
import com.etuproject.friendfindserver.simulation.TickSimulator;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WeakAnonymityLBS implements LBS {

    @Override
    public List<UserQueryMasked> query(String userName) {
        User self = InMemoryRepository.getInstance().getUser(userName);
        List<User> friends = new ArrayList<>();
        InMemoryRepository.getInstance().getFriends(self, friends);
        List<UserQueryMasked> resultList = new ArrayList<>();

        long time = TickSimulator.getInstance(self).getTime();
        Belief belief = InMemoryRepository.getInstance().getBelief(self);
        Set<Integer> reach = new HashSet<>();
        Set<Integer> Pin = new HashSet<>();
        Set<Integer> Pout = new HashSet<>();
        Set<Integer> whereaboutsT;
        Set<Integer> whereaboutsTAU = new HashSet<>();

        for (User friend: friends) {
            double distance = InMemoryRepository.getInstance().getDistance(self.getLocationID(), friend.getLocationID());
            if (self.getPreference().getInterestRadius() > distance) {  // Eliminate false positives
                long mostRecentQueryTime = belief.getMostRecentQueryTime(friend);
                whereaboutsT = belief.get(mostRecentQueryTime, friend);
                whereaboutsTAU.clear();
                reach.clear();
                Pin.clear();
                Pout.clear();
                calculateExtrapolatedBelief((time - mostRecentQueryTime), whereaboutsT, whereaboutsTAU);
                calculateReach((time - mostRecentQueryTime), self.getLocationID(), reach);
                partitionBelief(whereaboutsTAU, reach, Pin, Pout);

                if (Pin.contains(friend.getLocationID()) && Pin.size() >= friend.getPreference().getAnonymityLevel()) {
                    resultList.add(new UserQueryMasked(friend.getUserName()));
                    belief.set(time, friend, Pin);
                } else if (Pout.contains(friend.getLocationID()) && Pout.size() >= friend.getPreference().getAnonymityLevel()) {
                    belief.set(time, friend, Pout);
                } else if (Pin.contains(friend.getLocationID()) && Pin.size() < friend.getPreference().getAnonymityLevel()) {
                    belief.set(time, friend, whereaboutsTAU);
                } else if (Pout.contains(friend.getLocationID()) && Pout.size() < friend.getPreference().getAnonymityLevel()) {
                    belief.set(time, friend, whereaboutsTAU);
                }
            }
        }

        return resultList;
    }

    private void calculateExtrapolatedBelief(long deltaTime, Set<Integer> whereaboutsT1, Set<Integer> whereaboutsT2) {
        for (Integer priorBeliefVertexId: whereaboutsT1) {
            for (Integer vertexId: InMemoryRepository.graph.vertexSet()) {
                double weight = InMemoryRepository.getInstance().getDistance(priorBeliefVertexId, vertexId);
                if (weight < deltaTime) {
                    whereaboutsT2.add(vertexId);
                }
            }
        }
    }

    private void calculateReach(long deltaTime, int currentLocationId, Set<Integer> reach) {
        for (Integer vertexId: InMemoryRepository.graph.vertexSet()) {
            double weight = InMemoryRepository.getInstance().getDistance(currentLocationId, vertexId);
            if (weight < deltaTime) {
                reach.add(vertexId);
            }
        }
    }

    private void partitionBelief(Set<Integer> whereaboutsTAU, Set<Integer> reach, Set<Integer> Pin, Set<Integer> Pout) {
        for (Integer i: whereaboutsTAU) {
            if (reach.contains(i)) {
                Pin.add(i);
            } else {
                Pout.add(i);
            }
        }

        for (Integer i: reach) {
            if (whereaboutsTAU.contains(i)) {
                Pin.add(i);
            } else {
                Pout.add(i);
            }
        }
    }
}
