package Stratergy;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

public class CronSchedulingStrategy implements SchedulingStrategy {
    private final int minute;
    private final int hour;
    private final DayOfWeek dayOfWeek;

    /**
     * Constructor with individual cron fields
     * @param minute minute (0-59)
     * @param hour hour (0-23)
     * @param dayOfWeek day of week (MONDAY to SUNDAY)
     */
    public CronSchedulingStrategy(int minute, int hour, DayOfWeek dayOfWeek) {
        this.minute = minute;
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Constructor that accepts a cron expression string
     * Format: "minute hour day-of-week"
     * Example: "0 2 MONDAY" or "30 14 FRIDAY"
     * @param cronExpression cron expression string
     */
    public CronSchedulingStrategy(String cronExpression) {
        CronFields fields = parseCronExpression(cronExpression);
        this.minute = fields.minute;
        this.hour = fields.hour;
        this.dayOfWeek = fields.dayOfWeek;
    }

    /**
     * Parses a cron expression string and extracts the components
     * Format: "minute hour day-of-week"
     * @param cronExpression the cron expression string
     * @return CronFields object containing parsed values
     */
    private static CronFields parseCronExpression(String cronExpression) {
        String[] parts = cronExpression.trim().split("\\s+");

        if (parts.length < 3) {
            throw new IllegalArgumentException(
                    "Invalid cron expression format. Expected: 'minute hour day-of-week' (e.g., '0 2 MONDAY')"
            );
        }

        try {
            int minute = parseMinute(parts[0]);
            int hour = parseHour(parts[1]);
            DayOfWeek dayOfWeek = parseDayOfWeek(parts[2]);

            return new CronFields(minute, hour, dayOfWeek);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cron expression: " + cronExpression, e);
        }
    }

    private static int parseMinute(String minuteStr) {
        if ("*".equals(minuteStr)) {
            return 0; // Default to 0 if wildcard
        }
        int minute = Integer.parseInt(minuteStr);
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59, got: " + minute);
        }
        return minute;
    }

    private static int parseHour(String hourStr) {
        if ("*".equals(hourStr)) {
            return 0; // Default to 0 if wildcard
        }
        int hour = Integer.parseInt(hourStr);
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23, got: " + hour);
        }
        return hour;
    }

    private static DayOfWeek parseDayOfWeek(String dayStr) {
        try {
            // Try parsing as day name (MONDAY, TUESDAY, etc.)
            return DayOfWeek.valueOf(dayStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Try parsing as day number (0=SUNDAY, 1=MONDAY, ..., 6=SATURDAY)
            try {
                int dayNumber = Integer.parseInt(dayStr);
                if (dayNumber < 0 || dayNumber > 6) {
                    throw new IllegalArgumentException("Day must be between 0-6 or a valid day name");
                }
                // Convert cron day numbering (0=SUNDAY) to Java DayOfWeek
                return convertCronDayToJavaDayOfWeek(dayNumber);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(
                        "Invalid day of week: " + dayStr +
                        ". Expected day name (MONDAY-SUNDAY) or number (0-6 where 0=SUNDAY)"
                );
            }
        }
    }

    private static DayOfWeek convertCronDayToJavaDayOfWeek(int cronDay) {
        // Cron: 0=SUNDAY, 1=MONDAY, ..., 6=SATURDAY
        // Java DayOfWeek: MONDAY=1, TUESDAY=2, ..., SUNDAY=7
        if (cronDay == 0) {
            return DayOfWeek.SUNDAY;
        } else {
            return DayOfWeek.of(cronDay);
        }
    }

    @Override
    public Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime) {
        LocalDateTime next = LocalDateTime.now()
                .withSecond(0)
                .withNano(0)
                .withMinute(minute)
                .withHour(hour);

        while (next.getDayOfWeek() != dayOfWeek || next.isBefore(LocalDateTime.now())) {
            next = next.plusDays(1);
        }
        return Optional.of(next);
    }

    /**
     * Inner class to hold parsed cron fields
     */
    private static class CronFields {
        final int minute;
        final int hour;
        final DayOfWeek dayOfWeek;

        CronFields(int minute, int hour, DayOfWeek dayOfWeek) {
            this.minute = minute;
            this.hour = hour;
            this.dayOfWeek = dayOfWeek;
        }
    }
}
