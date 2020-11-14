package com.etuproject.friendfindserver.entity;

import com.etuproject.friendfindserver.repository.InMemoryRepository;
import com.etuproject.friendfindserver.simulation.TickSimulator;

import java.util.*;

public class Belief {
    private Map<Long, Map<User, Set<Integer>>> allBeliefs;
    private Map<User, Long> mostRecentQueryTimes;

    public Belief(User self) {

        List<User> friends = new ArrayList<>();
        InMemoryRepository.getInstance().getFriends(self, friends);
        long time = TickSimulator.getInstance(self).getTime();
        for (User friend: friends) {
            set(time, friend, InMemoryRepository.graph.vertexSet());

        }
    }

    public Set<Integer> get(long time, User friend) {
        return allBeliefs.get(time).get(friend);
    }

    public void set(long time, User friend, Set<Integer> newBelief) {

        if (!allBeliefs.containsKey(time)) {
            allBeliefs.put(time, new HashMap<>());
        }
        if (!allBeliefs.get(time).containsKey(friend)) {
            allBeliefs.get(time).put(friend, new HashSet<>());
        }

        allBeliefs.get(time).get(friend).clear();
        allBeliefs.get(time).get(friend).addAll(newBelief);
        mostRecentQueryTimes.put(friend, time);
    }

    public long getMostRecentQueryTime(User friend) {
        return mostRecentQueryTimes.get(friend);
    }
}
