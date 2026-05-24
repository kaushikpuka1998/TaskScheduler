package Stratergy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class RecurringSchedulingStrategy implements SchedulingStrategy {
    private Duration intervalInSeconds;

    public RecurringSchedulingStrategy(Duration intervalInSeconds) {
        this.intervalInSeconds = intervalInSeconds;
    }

    @Override
    public Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime) {
        if (lastExecutionTime == null) {
            return Optional.of(LocalDateTime.now().plus(intervalInSeconds));
        }
        return Optional.of(lastExecutionTime.plus(intervalInSeconds));
    }
}
