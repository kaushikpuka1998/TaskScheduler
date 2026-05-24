package Services;

import ENUMS.TaskStatus;
import Observer.TaskExecutionObserver;
import Stratergy.SchedulingStrategy;
import Tasks.ScheduledTask;
import Tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class TaskScedulerService {
    private static volatile TaskScedulerService instance;
    private final PriorityQueue<ScheduledTask> taskQueue;
    private final Object queueLock;
    private final List<Thread> workers;
    private final AtomicLong sequenceCounter;
    private final CopyOnWriteArrayList<TaskExecutionObserver> observers;
    private volatile boolean running;

    private TaskScedulerService(){
        this.taskQueue = new PriorityQueue<>();
        this.queueLock = new Object();
        this.workers = new ArrayList<>();
        this.sequenceCounter = new AtomicLong(0);
        this.observers = new CopyOnWriteArrayList<>();
        this.running = true;
    }

    public static TaskScedulerService getInstance(){
        if(instance == null){
            synchronized (TaskScedulerService.class){
                if(instance == null){
                    instance = new TaskScedulerService();
                }
            }
        }
        return instance;
    }

    public void initialize(int workerCount){
        for(int i=0;i<workerCount;i++){
            Thread worker = new Thread(this::runWorker, "Scheduler-worker-"+i);
            worker.start();
            workers.add(worker);
        }
    }

    public String schedule(Task task, SchedulingStrategy strategy){

        ScheduledTask scheduledTask = new ScheduledTask(task,strategy, sequenceCounter.incrementAndGet());

        synchronized (queueLock){
            taskQueue.offer(scheduledTask);
            queueLock.notifyAll();
        }
        return scheduledTask.getId();
    }

    public boolean cancel(String taskId){
        synchronized (queueLock){
            for(ScheduledTask scheduledTask : taskQueue){
                if(scheduledTask.getId().equals(taskId)){
                    scheduledTask.setStatus(TaskStatus.CANCELLED);
                    return true;
                }
            }
        }
        return false;
    }

    public void addObserver(TaskExecutionObserver observer){
        observers.add(observer);
    }

    public void shutDown(){
        running = false;
        synchronized(queueLock){
            queueLock.notifyAll();
        }

        for(Thread worker : workers){
            worker.interrupt();
        }
    }

    private void runWorker(){
        while(running){
            ScheduledTask taskToExecute = null;
            synchronized(queueLock){
                while(running){
                    try{
                        if(taskQueue.isEmpty()){
                            queueLock.wait();
                            continue;
                        } else {
                            ScheduledTask nextTask = taskQueue.peek();
                            long waitTime = Duration.between(LocalDateTime.now(), nextTask.getNextExecutionTime()).toMillis();
                            if(waitTime > 0){
                                queueLock.wait(waitTime);
                            } else {
                                taskToExecute = taskQueue.poll();
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if(taskToExecute != null){
                executeTask(taskToExecute);
            }
        }
    }

    private void executeTask(ScheduledTask scheduledTask){
        try{
            scheduledTask.setStatus(TaskStatus.RUNNING);
            notifyTaskStarted(scheduledTask);
            scheduledTask.getTask().execute();
            scheduledTask.setStatus(TaskStatus.COMPLETED);
            notifyTaskCompleted(scheduledTask);
        } catch (Exception e) {
            scheduledTask.setStatus(TaskStatus.FAILED);
            notifyTaskFailed(scheduledTask, e);
        }
    }

    private void notifyTaskStarted(ScheduledTask task){
        for(TaskExecutionObserver observer: observers){
            observer.onTaskStarted(task);
        }
    }

    private void notifyTaskCompleted(ScheduledTask task){
        for(TaskExecutionObserver observer: observers){
            observer.onTaskCompleted(task);
        }
    }

    private void notifyTaskFailed(ScheduledTask task, Exception exception){
        for(TaskExecutionObserver observer: observers){
            observer.onTaskFailed(task, exception);
        }
    }

}
