package com.etuproject.friendfindserver.repository;

import com.etuproject.friendfindserver.entity.Edge;
import com.etuproject.friendfindserver.entity.Vertex;
import com.etuproject.friendfindserver.entity.User;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;

class FileOperation {

    private static FileOperation instance;
    private static int nextID;

    //For random extra users in simulation.
    private static int extraPopulationSize = 40;

    private static String userFile = System.getProperty("user.dir") + "\\data\\" + "Users";
    private static String nodeFile = System.getProperty("user.dir") + "\\data\\" + "LA.cnode";
    private static String edgeFile = System.getProperty("user.dir") + "\\data\\" + "LA.cedge";

    private FileOperation() {    }

    static FileOperation getInstance() {
        if (instance == null) {
            instance = new FileOperation();
            nextID = 0;
        }
        return instance;
    }

    void initiate() {
        fillVertices();
        fillEdges();
        calculateDistances();

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(userFile));
            String str;
            while ((str = in.readLine()) != null) {
                String[] lineParsed = str.split(", ");
                String userName = lineParsed[0];
                String password = lineParsed[1];
                int interestRadius = Integer.parseInt(lineParsed[2]);
                int anonymityLevel = Integer.parseInt(lineParsed[3]);
                boolean isAdmin = lineParsed[4].equals("admin");
                User u = new User(nextID++, userName, password, interestRadius, anonymityLevel);
                u.setAdmin(isAdmin);
                u.setLocationID(Integer.parseInt(lineParsed[5]));
                InMemoryRepository.getInstance().addUser(u);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Random Population Initialization
        String password = "12345";
        for (int i = 100; i < extraPopulationSize + 100; ++i ) {
            User u = new User(nextID, "U"+nextID, password, 100, 100);
            nextID++;

            int randomLocationID = (int)(Math.random() * InMemoryRepository.getInstance().getVertexCount());
            u.setLocationID(randomLocationID);
            InMemoryRepository.getInstance().addUser(u);
        }
    }

    private void fillVertices() {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(nodeFile));
            String str;
            while ((str = in.readLine()) != null) {
                int id = Integer.parseInt(str.split(" ")[0]);
                double longitude = Double.parseDouble(str.split(" ")[1]);
                double latitude = Double.parseDouble(str.split(" ")[2]);
                Vertex v = new Vertex(id, latitude, longitude);
                InMemoryRepository.getInstance().addVertex(v);
                InMemoryRepository.graph.addVertex(id);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillEdges() {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(edgeFile));
            String str;
            while ((str = in.readLine()) != null) {
                String[] lineParsed = str.split(" ");
                int id = Integer.parseInt(lineParsed[0]);
                int from = Integer.parseInt(lineParsed[1]);
                int to = Integer.parseInt(lineParsed[2]);
                double distance = Double.parseDouble(lineParsed[3]);
                Edge e = new Edge(id, from, to, distance);
                InMemoryRepository.getInstance().addEdge(e);

                DefaultWeightedEdge dwe;
                if (InMemoryRepository.graph.getEdge(from, to) == null){
                    dwe = InMemoryRepository.graph.addEdge(from, to);
                    InMemoryRepository.graph.setEdgeWeight(dwe, e.getDistance());
                }

                if (InMemoryRepository.graph.getEdge(to, from) == null){
                    dwe = InMemoryRepository.graph.addEdge(to, from);
                    InMemoryRepository.graph.setEdgeWeight(dwe, e.getDistance());
                }

            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateDistances() {
        for (Integer i: InMemoryRepository.graph.vertexSet()) {
            for (Integer j: InMemoryRepository.graph.vertexSet()) {
                double weight = DijkstraShortestPath.findPathBetween(InMemoryRepository.graph, i, j).getWeight();
                InMemoryRepository.getInstance().addDistance(i, j, weight);
            }
        }
    }

    User createNewUser(String userName, String password, int interestRadius, int anonymityLevel) {
        User u = new User(nextID++, userName, password, interestRadius, anonymityLevel);
        u.setAdmin(false);
        u.setLocationID((int)(Math.random() * InMemoryRepository.getInstance().getVertexCount()));

        try {
            FileWriter writer = new FileWriter(userFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            String line = userName + ", " + password + ", " + 100 + ", " + 100 + ", user";
            line += ", " + u.getLocationID();
            line += "\n";
            System.out.print("Line: " + line);
            bufferedWriter.write(line);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return u;
    }
}
