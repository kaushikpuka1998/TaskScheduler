package Stratergy;

import java.time.LocalDateTime;
import java.util.Optional;

public class OneTimeSchedulingStrategy implements SchedulingStrategy {
    private LocalDateTime scheduledTime;

    public OneTimeSchedulingStrategy(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    public Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime) {
        if (lastExecutionTime == null || lastExecutionTime.isBefore(scheduledTime)) {
            return Optional.of(scheduledTime);
        }
        return Optional.empty();
    }
}
