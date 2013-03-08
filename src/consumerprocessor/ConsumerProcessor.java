package consumerprocessor;

import java.util.concurrent.*;

/**
 * Quick demonstration of a Callable returning Callable to implement various
 * stages of rules engine.
 * @author umermansoor
 */
public class ConsumerProcessor {

    /** queue to simply add values as instructed by Callables **/
    static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    
    /** Executor to run Callables returned by CompletionService. Returning 
     * Callables may just log or add another callable to the queue for further
     * processing. */
    static ExecutorService exec;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        TaskProcessorConsumerService tsp = new TaskProcessorConsumerService();

        tsp.cs.submit(new Callable<Callable<Boolean>>  () {
            @Override
            public Callable<Boolean> call() throws Exception {
                Thread.sleep(5000);

                return new Callable<Boolean>() {
                    @Override public Boolean call() {
                        log("The Callable says: Log this");
                        addToSomeQueue("tothequeue");
                        return true;
                    }
                };
            }
        });

        tsp.cs.submit(new Runnable() {
            @Override
            public void run() {
                log("Runnable is executing...");

            }
        },
                new Callable() {
                    @Override
                    public Boolean call() throws Exception {
                        Thread.sleep(5000);

                        log("The Callable-As-Runnable-Result says: Log this");
                        addToSomeQueue("toinfinityandbeyond");

                        return true;
                    }
                });
        
        exec = Executors.newSingleThreadExecutor();
        
        Callable<Boolean> result = tsp.cs.take().get();
        exec.submit(result);
    }
    
    

    public static void log(String str) {
        System.out.println(str);
    }

    public static void addToSomeQueue(String str) {
        queue.add(str);
    }
}
