import java.util.*;
import java.util.concurrent.*;

class Biker implements Runnable {
    String name;
    int startTime;
    int endTime;
    int distance;

    Biker(String n, int d) {
        name = n;
        new Thread(this).start();
    }

    public void run() {
        for(int i=0; i<distance * 1000; i++) {
        }
    }
}

class Shared {
    static AtomicInteger count = new AtomicInteger(1);

    public distanceIncrement(Biker b) {
        synchronized(Biker.class) {
            b.distance++;
        }
        if(distance == 10) {
            System.out.println(
            b.stop();
        }
    }
}

public class BikeRacing {
    public static void main (String[] args) {

        System.out.println("Welcome to the bike racing game");

        int bikers = Runtime.getRuntime().availableProcessors();
        ExecutorService es = Executors.newFixedThreadPool(bikers);


    }
}
