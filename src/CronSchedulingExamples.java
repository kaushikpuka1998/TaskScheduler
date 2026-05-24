import Observer.LoggingObserver;
import Services.TaskScedulerService;
import Stratergy.CronSchedulingStrategy;
import Stratergy.RecurringSchedulingStrategy;
import Tasks.DataBackupTask;
import Tasks.PrintMessageTask;

import java.time.Duration;

public class CronSchedulingExamples {
    /**
     * This file demonstrates various ways to use cron expressions
     * with the TaskScheduler
     */
    public static void main(String[] args) throws InterruptedException {
        TaskScedulerService scheduler = TaskScedulerService.getInstance();
        scheduler.initialize(5);
        scheduler.addObserver(new LoggingObserver());

        // ========== DAILY TASKS ==========

        // Every Monday at 2:30 AM - System maintenance
        scheduler.schedule(
                new PrintMessageTask("Starting Monday maintenance tasks"),
                new CronSchedulingStrategy("30 2 MONDAY")
        );

        // Every Friday at 6:00 PM - End of week report
        scheduler.schedule(
                new PrintMessageTask("Generating end-of-week report"),
                new CronSchedulingStrategy("0 18 FRIDAY")
        );

        // Every Sunday at midnight - Weekly backup
        scheduler.schedule(
                new DataBackupTask("/Users/kgstrivers/Desktop/source",
                                 "/Users/kgstrivers/Desktop/backup"),
                new CronSchedulingStrategy("0 0 SUNDAY")
        );

        // ========== USING NUMERIC DAY NOTATION ==========

        // Wednesday (3) at 2:15 PM - Mid-week check
        scheduler.schedule(
                new PrintMessageTask("Mid-week status check"),
                new CronSchedulingStrategy("15 14 3")
        );

        // Tuesday (2) at 9:30 AM - Team standup
        scheduler.schedule(
                new PrintMessageTask("Team standup meeting"),
                new CronSchedulingStrategy("30 9 2")
        );

        // ========== USING NAMED DAYS (PREFERRED) ==========

        // Every Monday at 8:00 AM
        scheduler.schedule(
                new PrintMessageTask("Monday morning standup"),
                new CronSchedulingStrategy("0 8 MONDAY")
        );

        // Every Thursday at 3:00 PM - Deployment window
        scheduler.schedule(
                new PrintMessageTask("Opening deployment window"),
                new CronSchedulingStrategy("0 15 THURSDAY")
        );

        // Every Saturday at 5:00 PM - Weekend report
        scheduler.schedule(
                new PrintMessageTask("Weekend summary report"),
                new CronSchedulingStrategy("0 17 SATURDAY")
        );

        // ========== EDGE CASES ==========

        // Sunday at 11:59 PM - Before Monday reset
        scheduler.schedule(
                new PrintMessageTask("End of week cleanup"),
                new CronSchedulingStrategy("59 23 SUNDAY")
        );

        // Monday at 12:00 AM (midnight) - Start of week
        scheduler.schedule(
                new PrintMessageTask("Start of new week tasks"),
                new CronSchedulingStrategy("0 0 MONDAY")
        );

        // Friday at 11:00 AM - Mid-day check
        scheduler.schedule(
                new PrintMessageTask("Friday mid-day status"),
                new CronSchedulingStrategy("0 11 FRIDAY")
        );

        // ========== HIGH-FREQUENCY TASKS (USE RecurringSchedulingStrategy) ==========

        // Every second - System heartbeat
        scheduler.schedule(
                new PrintMessageTask("Heartbeat - System is running"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(1))
        );

        // Every 5 seconds - Health check
        scheduler.schedule(
                new PrintMessageTask("Health check - All systems operational"),
                new RecurringSchedulingStrategy(Duration.ofSeconds(5))
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

        // Keep the scheduler running
        Thread.sleep(60000);
        scheduler.shutDown();
    }

    // REFERENCE: Cron vs Recurring Strategies
    //
    // Use CronSchedulingStrategy for weekly tasks (specific day, hour, minute)
    // Use RecurringSchedulingStrategy for frequent tasks (every second, minute, hour)
    //
    // HIGH-FREQUENCY TASK EXAMPLES:
    // Every second: new RecurringSchedulingStrategy(Duration.ofSeconds(1))
    // Every 5 seconds: new RecurringSchedulingStrategy(Duration.ofSeconds(5))
    // Every 30 seconds: new RecurringSchedulingStrategy(Duration.ofSeconds(30))
    // Every minute: new RecurringSchedulingStrategy(Duration.ofMinutes(1))
    // Every 5 minutes: new RecurringSchedulingStrategy(Duration.ofMinutes(5))
    // Every hour: new RecurringSchedulingStrategy(Duration.ofHours(1))
    //
    // WEEKLY CRON TASK EXAMPLES (Format: "minute hour day-of-week"):
    // "0 0 MONDAY" = Every Monday at midnight
    // "30 2 TUESDAY" = Every Tuesday at 2:30 AM
    // "0 9 WEDNESDAY" = Every Wednesday at 9:00 AM
    // "45 14 THURSDAY" = Every Thursday at 2:45 PM
    // "0 18 FRIDAY" = Every Friday at 6:00 PM
    // "0 0 SUNDAY" = Every Sunday at midnight
    // "59 23 SATURDAY" = Every Saturday at 11:59 PM
}

