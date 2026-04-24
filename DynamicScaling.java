import java.time.Duration;
import java.time.Instant;

public class DynamicScaling {
    
    static class MathTask implements Runnable {
        private final int taskId;
        
        public MathTask(int taskId) {
            this.taskId = taskId;
        }
        
        @Override
        public void run() {
            long sum = 0;
            for (int i = 1; i <= 10_000_000; i++) {
                long i3 = (long) i * i * i;
                long i4 = (long) i * i * i * i;
                sum += i3 + i4;
            }
            System.out.printf("Thread %d completed with partial sum: %d%n", 
                            taskId, sum);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
       
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available logical processors: " + availableProcessors);

        
        
        System.out.println("\nTesting with 1 thread:");
        runTest(1);
        
        
        Thread.sleep(1000);
        
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("\nTesting with " + availableProcessors + " threads:");
        runTest(availableProcessors);
    }
    
    private static void runTest(int threadCount) throws InterruptedException {
    
        Thread[] threads = new Thread[threadCount];
        

        Instant start = Instant.now();
        
    
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new MathTask(i));
            threads[i].start();
        }
        
     
        for (Thread thread : threads) {
            thread.join();
        }
        
        Instant end = Instant.now();
        long timeElapsed = Duration.between(start, end).toMillis();
        
        System.out.printf("Time taken with %d thread(s): %d ms%n", 
                        threadCount, timeElapsed);
    
        if (threadCount > 1) {
            
            System.out.printf("Using %d threads completed in %d ms%n", 
                            threadCount, timeElapsed);
        }
    }
}