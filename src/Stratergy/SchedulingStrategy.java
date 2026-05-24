package Stratergy;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SchedulingStrategy {
    Optional<LocalDateTime> getNextExecutionTime(LocalDateTime lastExecutionTime);
}
