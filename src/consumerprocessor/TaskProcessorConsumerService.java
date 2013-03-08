
package consumerprocessor;

import java.util.concurrent.*;

/**
 * This simply "wraps" the task processor into a CompletionService so that the
 * results are obtained as they become available.
 * @author umermansoor
 */
public class TaskProcessorConsumerService
{
    public final CompletionService<Callable<Boolean>> cs;
    private final ExecutorService tasksExecutor;
    
    public TaskProcessorConsumerService() {
        tasksExecutor = Executors.newSingleThreadExecutor();
        cs = new ExecutorCompletionService<Callable<Boolean>>(tasksExecutor);
    }
}
