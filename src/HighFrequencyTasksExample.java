import Observer.LoggingObserver;
import Services.TaskScedulerService;
import Stratergy.RecurringSchedulingStrategy;
import Tasks.PrintMessageTask;

import java.time.Duration;

/**
 * Example: Running tasks at high frequency (every second, every millisecond, etc.)
 * For tasks that need to run very frequently (sub-minute intervals),
 * use RecurringSchedulingStrategy with Duration instead of CronSchedulingStrategy.
 */
public class HighFrequencyTasksExample {
    public static void main(String[] args) throws InterruptedException {
        TaskScedulerService scheduler = TaskScedulerService.getInstance();
        scheduler.initialize(5);
        scheduler.addObserver(new LoggingObserver());

        // ========== EVERY SECOND EXAMPLES ==========

        // Simple example: Every second
        scheduler.schedule(
                new PrintMessageTask("Heartbeat - System is running"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(1))
        );

        // Every 2 seconds
        scheduler.schedule(
                new PrintMessageTask("Checking every 2 seconds"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(2))
        );

        // ========== SUB-SECOND EXAMPLES ==========

        // Every 500 milliseconds
        scheduler.schedule(
                new PrintMessageTask("High-frequency check (500ms)"),
                new RecurringSchedulingStrategy(Duration.ofMillis(500))
        );

        // Every 200 milliseconds
        scheduler.schedule(
                new PrintMessageTask("Very fast pulse (200ms)"),
                new RecurringSchedulingStrategy(Duration.ofMillis(200))
        );

        // ========== VARIOUS INTERVALS ==========

        // Every 5 seconds - Health check
        scheduler.schedule(
                new PrintMessageTask("Health check - All systems operational"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(5))
        );

        // Every 10 seconds - Status report
        scheduler.schedule(
                new PrintMessageTask("10-second status report"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(10))
        );

        // Every 30 seconds - Cache refresh
        scheduler.schedule(
                new PrintMessageTask("Refreshing system cache"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(30))
        );

        // Every minute - Activity log
        scheduler.schedule(
                new PrintMessageTask("Logging current activity"),
                new RecurringSchedulingStrategy(Duration.ofMinutes(1))
        );

        // Every 5 minutes - Performance metrics
        scheduler.schedule(
                new PrintMessageTask("Collecting performance metrics"),
                new RecurringSchedulingStrategy(Duration.ofMinutes(5))
        );

        // Keep the scheduler running for demonstration
        Thread.sleep(120000);  // Run for 2 minutes
        scheduler.shutDown();
    }
}

/*
 * COMMON DURATION PATTERNS FOR HIGH-FREQUENCY TASKS:
 *
 * Duration.ofMillis(100)       → Every 100 milliseconds
 * Duration.ofMillis(500)       → Every 500 milliseconds (half second)
 * Duration.ofMillis(1000)      → Every 1000 milliseconds (1 second)
 * Duration.ofSeconds(1)        → Every 1 second
 * Duration.ofSeconds(2)        → Every 2 seconds
 * Duration.ofSeconds(5)        → Every 5 seconds
 * Duration.ofSeconds(10)       → Every 10 seconds
 * Duration.ofSeconds(30)       → Every 30 seconds
 * Duration.ofMinutes(1)        → Every 1 minute
 * Duration.ofMinutes(5)        → Every 5 minutes
 * Duration.ofHours(1)          → Every 1 hour
 *
 * WHY USE RecurringSchedulingStrategy FOR HIGH-FREQUENCY TASKS?
 *
 * - CronSchedulingStrategy is designed for weekly schedules
 * - It works with minute-level granularity (specific day, hour, minute)
 * - RecurringSchedulingStrategy is perfect for repeating intervals
 * - It supports Duration which allows millisecond precision
 *
 * COMPARISON:
 *
 * CronSchedulingStrategy("0 2 MONDAY")
 *   → Runs every Monday at 2:00 AM
 *   → Use for: Weekly tasks, reports, maintenance windows
 *
 * RecurringSchedulingStrategy(Duration.ofSeconds(1))
 *   → Runs every second
 *   → Use for: Heartbeats, health checks, real-time monitoring
 */

