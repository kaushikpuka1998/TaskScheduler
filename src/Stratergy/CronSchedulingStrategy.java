package Stratergy;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Optional;

public class CronSchedulingStrategy implements SchedulingStrategy {
    private final int minute;
    private final int hour;
    private final DayOfWeek dayOfWeek;

    public CronSchedulingStrategy(int minute, int hour, DayOfWeek dayOfWeek) {
        this.minute = minute;
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
    }


    @Override
    public Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime) {
        // Implement logic to parse the cron expression and calculate the next execution time
        // This is a placeholder implementation and should be replaced with actual cron parsing logic
        LocalDateTime next = LocalDateTime.now()
                .withSecond(0)
                .withNano(0)
                .withMinute(minute)
                .withHour(hour);


        while(next.getDayOfWeek() != dayOfWeek || next.isBefore(LocalDateTime.now())) {
            next = next.plusDays(1);
        }
        return Optional.of(next);

    }
}
