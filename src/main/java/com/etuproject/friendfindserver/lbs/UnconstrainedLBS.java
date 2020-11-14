package com.etuproject.friendfindserver.lbs;

import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.entity.UserQueryMasked;
import com.etuproject.friendfindserver.repository.InMemoryRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;

public class UnconstrainedLBS implements LBS {

    @Override
    public List<UserQueryMasked> query(String userName) {
        User self = InMemoryRepository.getInstance().getUser(userName);
        List<User> friends = new ArrayList<>();
        InMemoryRepository.getInstance().getFriends(self, friends);
        List<UserQueryMasked> resultList = new ArrayList<>();

        for (User friend: friends) {
            GraphPath<Integer, DefaultWeightedEdge> path =
                    DijkstraShortestPath.findPathBetween(InMemoryRepository.graph, self.getLocationID(), friend.getLocationID());
            if (self.getPreference().getInterestRadius() > path.getWeight()) {
                resultList.add(new UserQueryMasked(friend.getUserName()));
            }
        }
        return resultList;
    }
}
