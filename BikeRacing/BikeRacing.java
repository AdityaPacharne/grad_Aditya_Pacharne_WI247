import java.util.*;
import java.util.concurrent.*;
import java.time.LocalTime;

class BikerResult {
    String name;
    long duration;
    LocalTime startTime;
    LocalTime endTime;
    
    BikerResult(String n, long d, LocalTime s, LocalTime e) {
        name = n;
        duration = d;
        startTime = s;
        endTime = e;
    }
}

class Biker implements Callable<BikerResult> {
    String name;
    int distance;
    CountDownLatch startLatch;
    
    Biker(String n, int d, CountDownLatch latch) {
        name = n;
        distance = d * 1000;
        startLatch = latch;
    }
    
    public BikerResult call() throws Exception {
        startLatch.await();
        
        int covered = 0;
        Random rand = new Random();
        long startTime = System.currentTimeMillis();
        LocalTime start = LocalTime.now();
        
        while(covered < distance) {
            int speed = 100 + rand.nextInt(400);
            Thread.sleep(speed);
            covered += 100;
            if(covered > distance) covered = distance;
            System.out.println(name + " covered " + covered + "m");
        }
        
        long endTime = System.currentTimeMillis();
        LocalTime end = LocalTime.now();
        long timeTaken = (endTime - startTime) / 1000;
        
        return new BikerResult(name, timeTaken, start, end);
    }
}

public class BikeRacing {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the bike racing game");
        
        ExecutorService es = Executors.newFixedThreadPool(8);
        
        System.out.print("Enter race distance in km: ");
        int raceDistance = sc.nextInt();
        
        String[] bikerNames = {"A", "B", "C", "D", "E", "F", "G", "H"};
        CountDownLatch startLatch = new CountDownLatch(8);
        List<Future<BikerResult>> futures = new ArrayList<>();
        
        for(int i=0; i<8; i++) {
            Biker b = new Biker(bikerNames[i], raceDistance, startLatch);
            futures.add(es.submit(b));
            startLatch.countDown();
        }
        
        List<BikerResult> results = new ArrayList<>();
        for(Future<BikerResult> f : futures) results.add(f.get());
        
        es.shutdown();
        
        results.sort(Comparator.comparingLong(r -> r.duration));
        
        System.out.println("\nRace Results:");
        int rank = 1;
        for(BikerResult r : results) {
            System.out.println("Rank " + rank++ + ": " + r.name + 
                " | Start: " + r.startTime + 
                " | End: " + r.endTime + 
                " | Time: " + r.duration + "s");
        }
        
        sc.close();
    }
}

