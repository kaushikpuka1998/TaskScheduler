import Observer.LoggingObserver;
import Services.TaskScedulerService;
import Stratergy.CronSchedulingStrategy;
import Stratergy.OneTimeSchedulingStrategy;
import Stratergy.RecurringSchedulingStrategy;
import Tasks.DataBackupTask;
import Tasks.PrintMessageTask;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TaskScedulerService scheduler = TaskScedulerService.getInstance();
        scheduler.initialize(3); // Initialize with 3 worker threads
        scheduler.addObserver(new LoggingObserver());

        scheduler.schedule(
                new PrintMessageTask("Hello, World!"),
                new OneTimeSchedulingStrategy(LocalDateTime.now().plusSeconds(5))
        );

        scheduler.schedule(
                new DataBackupTask("/Users/kgstrivers/Desktop/source", "/Users/kgstrivers/Desktop/source"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(10))
        );

        // Example 1: Using cron expression string (NEW WAY)
        // Format: "minute hour day-of-week"
        scheduler.schedule(
                new PrintMessageTask("Weekly Report"),
                new CronSchedulingStrategy("0 2 SUNDAY")  // 2:00 AM on Sunday
        );

        // Example 2: Using traditional parameters (STILL SUPPORTED)
        scheduler.schedule(
                new PrintMessageTask("Daily Task"),
                new CronSchedulingStrategy(1, 0, DayOfWeek.SUNDAY)  // 12:01 AM on Sunday
        );
        Thread.sleep(30000);
        scheduler.shutDown();
    }
}