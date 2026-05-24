package Observer;

import Tasks.ScheduledTask;

public interface TaskExecutionObserver {
    void onTaskStarted(ScheduledTask scheduledTask);
    void onTaskCompleted(ScheduledTask task);
    void onTaskFailed(ScheduledTask task, Exception exception);
}
