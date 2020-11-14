package com.etuproject.friendfindserver.simulation;

import com.etuproject.friendfindserver.entity.User;
import com.etuproject.friendfindserver.repository.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class TickSimulator {

    private List<Tickable> tickables = new ArrayList<>();
    private final int SECONDS_PER_TICK = 300;
    private long currentTime = 0;
    private static TickSimulator instance;
    private TickRunnable tickRunnable;
    private ScheduledExecutorService scheduler;

    private TickSimulator(User self) {
        List<User> friendsList = new ArrayList<>();
        InMemoryRepository.getInstance().getFriends(self, friendsList);
        for (User u: friendsList) {
            add(new SimulationUser(u));
        }
    }

    public static TickSimulator getInstance(User self) {
        if (instance == null) {
            instance = new TickSimulator(self);
        }

        return instance;
    }

    private void add(Tickable o) {
        tickables.add(o);
    }

    public void execute(int tickCount) {
        if (tickCount == -1) {
            if (tickRunnable == null) {
                scheduler = Executors.newScheduledThreadPool(1);
                tickRunnable = new TickRunnable(this, tickCount * SECONDS_PER_TICK * -1);
                scheduler.scheduleAtFixedRate(tickRunnable, 0, 1, TimeUnit.SECONDS);
            } else {
                scheduler.shutdown();
                tickRunnable = null;
            }
        } else {
            tick(tickCount * SECONDS_PER_TICK);
        }

    }

    private void tick(int tickSeconds) {
        System.out.println("Another " + tickSeconds + " seconds have passed.");
        for (Tickable tickable : tickables) {
            tickable.tick(tickSeconds);
        }
        currentTime += tickSeconds;
    }

    public long getTime() {
        return currentTime;
    }

    private static class TickRunnable implements Runnable {

        private int tickSeconds;
        private TickSimulator parent;

        TickRunnable(TickSimulator parent, int tickSeconds) {
            this.parent = parent;
            this.tickSeconds = tickSeconds;
        }

        @Override
        public void run() {
            parent.tick(tickSeconds);
        }
    }

}
