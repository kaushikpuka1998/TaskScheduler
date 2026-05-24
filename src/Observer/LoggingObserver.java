package Observer;

import Tasks.ScheduledTask;

public class LoggingObserver implements TaskExecutionObserver{
    @Override
    public void onTaskStarted(ScheduledTask scheduledTask) {
        System.out.println("Task started: " + scheduledTask.getTask().getName() + " at " + scheduledTask.getNextExecutionTime());
    }

    @Override
    public void onTaskCompleted(ScheduledTask task) {
        System.out.println("Task completed: " + task.getTask().getName() + " at " + System.currentTimeMillis());
    }

    @Override
    public void onTaskFailed(ScheduledTask task, Exception exception) {
        System.out.println("Task failed: " + task.getTask().getName() + " at " + System.currentTimeMillis());
        System.out.println("Error: " + exception.getMessage());
    }
}
