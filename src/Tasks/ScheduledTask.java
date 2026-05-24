package Tasks;

import ENUMS.TaskStatus;
import Stratergy.SchedulingStrategy;

import java.time.LocalDateTime;
import java.util.Optional;

public class ScheduledTask implements Comparable<ScheduledTask>{
    private final String id;
    private final Task task;
    private final SchedulingStrategy strategy;
    private final long sequenceNumber;

    private LocalDateTime nextExecutionTime;
    private LocalDateTime lastExecutionTime;

    private TaskStatus status;

    public ScheduledTask( Task task, SchedulingStrategy strategy, long sequenceNumber) {
        this.id = java.util.UUID.randomUUID().toString();
        this.task = task;
        this.strategy = strategy;
        this.sequenceNumber = sequenceNumber;
        this.nextExecutionTime = strategy.getNextExecutionTime(null)
                .orElseThrow(() -> new IllegalArgumentException("Invalid scheduling strategy: No next execution time available"));
        this.status = TaskStatus.SCHEDULED;
    }

    public Task getTask() {
        return task;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getNextExecutionTime() {
        return nextExecutionTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public boolean hasMoreExecutions(){
        return strategy.getNextExecutionTime(this.lastExecutionTime).isPresent();
    }

    public void updateForNextExecution(){
        this.lastExecutionTime = LocalDateTime.now();
        Optional<LocalDateTime> nextTime = strategy.getNextExecutionTime(this.lastExecutionTime);
        nextTime.ifPresent(time -> {
            this.nextExecutionTime = time;
            this.status = TaskStatus.SCHEDULED;
        });
    }

    @Override
    public int compareTo(ScheduledTask o) {
        int compare = this.nextExecutionTime.compareTo(o.nextExecutionTime);
        if (compare == 0) { // If Task1 and Task2 are same time then sequenceNumber will be followed like FIFO if sequence is 1 then first , if 2 then second
            return Long.compare(this.sequenceNumber, o.sequenceNumber);
        }
        return compare;
    }
}
