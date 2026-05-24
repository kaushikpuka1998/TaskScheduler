# Every Second Job (High-Frequency Scheduling)

Quick reference for scheduling jobs to run every second or at other frequent intervals.

## Every Second - Basic Example

```java
import Services.TaskScedulerService;
import Stratergy.RecurringSchedulingStrategy;
import Tasks.PrintMessageTask;
import java.time.Duration;

TaskScedulerService scheduler = TaskScedulerService.getInstance();
scheduler.initialize(3);

// Run a task every second
scheduler.schedule(
    new PrintMessageTask("System Heartbeat"),
    new RecurringSchedulingStrategy(Duration.ofSeconds(1))
);

scheduler.schedule(
    new PrintMessageTask("Checking..."),
    new RecurringSchedulingStrategy(Duration.ofSeconds(1))
);
```

## All High-Frequency Duration Options

| Duration | Period | Code |
|----------|--------|------|
| 100 milliseconds | 0.1 seconds | `Duration.ofMillis(100)` |
| 200 milliseconds | 0.2 seconds | `Duration.ofMillis(200)` |
| 500 milliseconds | 0.5 seconds | `Duration.ofMillis(500)` |
| 1 second | 1 second | `Duration.ofSeconds(1)` |
| 2 seconds | 2 seconds | `Duration.ofSeconds(2)` |
| 5 seconds | 5 seconds | `Duration.ofSeconds(5)` |
| 10 seconds | 10 seconds | `Duration.ofSeconds(10)` |
| 30 seconds | 30 seconds | `Duration.ofSeconds(30)` |
| 1 minute | 60 seconds | `Duration.ofMinutes(1)` |
| 5 minutes | 300 seconds | `Duration.ofMinutes(5)` |
| 1 hour | 3600 seconds | `Duration.ofHours(1)` |

## Why Not Use CronSchedulingStrategy?

**CronSchedulingStrategy** is designed for **weekly** schedules with minute-level granularity:
- Format: `"minute hour day-of-week"`
- Example: `"0 2 MONDAY"` → Every Monday at 2:00 AM
- Minimum frequency: Once per day (on a specific day)

**RecurringSchedulingStrategy** is perfect for **frequent intervals**:
- Supports any Duration (seconds, milliseconds, etc.)
- Example: `Duration.ofSeconds(1)` → Every second
- Can run every millisecond if needed

## Complete Example: Every Second Job

```java
import Observer.LoggingObserver;
import Services.TaskScedulerService;
import Stratergy.RecurringSchedulingStrategy;
import Tasks.PrintMessageTask;
import java.time.Duration;

public class EverySecondExample {
    public static void main(String[] args) throws InterruptedException {
        TaskScedulerService scheduler = TaskScedulerService.getInstance();
        scheduler.initialize(5);
        scheduler.addObserver(new LoggingObserver());

        // Every second task
        scheduler.schedule(
            new PrintMessageTask("1 second tick"),
            new RecurringSchedulingStrategy(Duration.ofSeconds(1))
        );

        // Run for 30 seconds and then stop
        Thread.sleep(30000);
        scheduler.shutDown();
    }
}
```

## Combining Weekly (Cron) and Frequent (Recurring) Tasks

```java
// Weekly task (CronSchedulingStrategy)
scheduler.schedule(
    new PrintMessageTask("Monday backup"),
    new CronSchedulingStrategy("0 2 MONDAY")
);

// Frequent task (RecurringSchedulingStrategy)
scheduler.schedule(
    new PrintMessageTask("Heartbeat every second"),
    new RecurringSchedulingStrategy(Duration.ofSeconds(1))
);
```

## Performance Notes

- **Every second** (1 task/second): Minimal overhead
- **Every 100ms** (10 tasks/second): Reasonable overhead
- **Every 10ms** (100 tasks/second): Consider thread pool size
- **Every 1ms** (1000 tasks/second): May be overkill for most use cases

Use `scheduler.initialize(N)` to set appropriate thread pool size for your frequency needs.

## Output Example

Running an every-second task might produce output like:
```
[Observer] Task scheduled: PrintMessageTask - Heartbeat every second
[Observer] Task starting execution: PrintMessageTask
[Observer] Task completed successfully: PrintMessageTask
[Observer] Task scheduled: PrintMessageTask - Heartbeat every second
[Observer] Task starting execution: PrintMessageTask
[Observer] Task completed successfully: PrintMessageTask
...
```

The same task repeats every second.

