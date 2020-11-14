package com.etuproject.friendfindserver.repository;

import com.etuproject.friendfindserver.entity.*;
import com.etuproject.friendfindserver.simulation.TickSimulator;
import javafx.util.Pair;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRepository {

    private static InMemoryRepository instance;
    private static Map<String, User> userMap;
    private static Map<Integer, Vertex> vertexMap;
    private static Map<String, Double> distanceMap;
    private static Map<Integer, Edge> edgeMap;
    public static DefaultDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph;
    private static Map<User, Belief> beliefMap;

    private InMemoryRepository(){
        userMap = new HashMap<>();
        vertexMap = new HashMap<>();
        distanceMap = new HashMap<>();
        edgeMap = new HashMap<>();
        graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        beliefMap = new HashMap<>();
    }

    public static InMemoryRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryRepository();
            FileOperation.getInstance().initiate();
        }
        return instance;
    }

    public boolean signUp(String userName, String password, int interestRadius, int anonymityLevel) {
        if (userMap.containsKey(userName)) {
            return false;
        } else {
            User u = FileOperation.getInstance().createNewUser(userName, password, interestRadius, anonymityLevel);
            userMap.put(u.getUserName(), u);
            return true;
        }
    }

    public boolean setPreference(String userName, int interestRadius, int anonymityLevel) {
        if (userMap.containsKey(userName)) {
            User u = userMap.get(userName);
            u.getPreference().setInterestRadius(interestRadius);
            u.getPreference().setAnonymityLevel(anonymityLevel);
            return true;
        }
        return false;
    }

    public int updateLocation(String userName, double latitude, double longitude) {
        if (userMap.containsKey(userName)){
            int locationID = userMap.get(userName).getLocationID();

            // En yakın node'un enlem ve boylamına set edilecek. Set edilen vertexin id bilgisi dönülecek.
            int newLocationID = 0;
            double minDistance = Double.POSITIVE_INFINITY;
            for (Vertex v: vertexMap.values()) {
                double latitudeDiff = Math.abs(latitude - v.getLatitude());
                double longitudeDiff = Math.abs(longitude - v.getLongitude());
                double distance = Math.sqrt(Math.pow(latitudeDiff, 2) + Math.pow(longitudeDiff, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    newLocationID = v.getId();
                }
            }

            return newLocationID;
        }
        return -1;
    }

    public boolean authenticate(String userName, String password) {
        if (userMap.containsKey(userName) && userMap.get(userName).getPassword().equals(password)) {

            beliefMap.put(getUser(userName), new Belief(getUser(userName)));

            return true;
        } else {
            return false;
        }
    }

    public User getUser(String userName) {
        return userMap.get(userName);
    }

    public void getEdges(List<Edge> edgeList) {
        if (edgeList == null) {
            edgeList = new ArrayList<>();
        }
        edgeList.addAll(edgeMap.values());
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edgeMap.values());
    }

    public Edge getEdge(int id) {
        return edgeMap.get(id);
    }

    public void addEdge(Edge edge) {
        edgeMap.put(edge.getId(), edge);
    }

    public void addDistance(int from, int to, double distance) {
        distanceMap.put(from + "-" + to, distance);
    }

    public double getDistance(int from, int to) {
        return distanceMap.get(from + "-" + to);
    }

    public void getVertices(List<Vertex> vertexList) {
        if (vertexList == null) {
            vertexList = new ArrayList<>();
        }
        vertexList.addAll(vertexMap.values());
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(vertexMap.values());
    }

    public Vertex getVertex(int id) {
        return vertexMap.get(id);
    }

    public void addVertex(Vertex vertex) {
        vertexMap.put(vertex.getId(), vertex);
    }

    public int getVertexCount() {
        return vertexMap.values().size();
    }

    public void getUsers(List<User> userList) {
        if (userList == null) {
            userList = new ArrayList<>();
        }
        userList.addAll(userMap.values());
    }

    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    public void addUser(User user) {
        userMap.put(user.getUserName(), user);
    }

    public void getFriends(User self, List<User> friendsList) {
        if (friendsList == null) {
            friendsList = new ArrayList<>();
        }
        friendsList.addAll(userMap.values());
        friendsList.remove(self);
    }

    public void getFriendsWithAdminMasked(User self, List<UserAdminMasked> friendsList) {
        if (friendsList == null) {
            friendsList = new ArrayList<>();
        }
        for (User u: userMap.values()) {
            if (u != self) {
                friendsList.add(new UserAdminMasked(u.getUserName(), u.getLocationID()));
            }
        }
    }

    public Belief getBelief(User u) {
        return beliefMap.get(u);
    }
}
