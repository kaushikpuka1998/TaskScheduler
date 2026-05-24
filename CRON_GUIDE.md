# Cron Expression Parsing Guide

## Overview
The enhanced `CronSchedulingStrategy` now supports both:
1. **String-based cron expressions** (NEW - Recommended)
2. **Individual parameters** (Legacy - Still supported for backward compatibility)

## Cron Expression Format

```
minute hour day-of-week
```

### Components:
- **minute**: 0-59 (the minute of the hour)
- **hour**: 0-23 (the hour in 24-hour format)
- **day-of-week**: Day name (MONDAY-SUNDAY) or number (0-6, where 0=SUNDAY)

## Usage Examples

### 1. Using Cron Expression Strings (RECOMMENDED)

#### Example 1: Every Monday at 2:30 AM
```java
scheduler.schedule(
    new PrintMessageTask("Monday task"),
    new CronSchedulingStrategy("30 2 MONDAY")
);
```

#### Example 2: Every Friday at 6:00 PM
```java
scheduler.schedule(
    new DataBackupTask("/source", "/backup"),
    new CronSchedulingStrategy("0 18 FRIDAY")
);
```

#### Example 3: Every Sunday at midnight
```java
scheduler.schedule(
    new PrintMessageTask("Weekly Report"),
    new CronSchedulingStrategy("0 0 SUNDAY")
);
```

#### Example 4: Using numeric day notation (Wednesday = 3)
```java
scheduler.schedule(
    new PrintMessageTask("Wednesday task"),
    new CronSchedulingStrategy("15 14 3")  // 2:15 PM on Wednesday
);
```

### 2. Using Traditional Parameters (Legacy)

```java
scheduler.schedule(
    new PrintMessageTask("Task"),
    new CronSchedulingStrategy(30, 2, DayOfWeek.MONDAY)  // 2:30 AM on Monday
);
```

## Day of Week Reference

### Day Names (Case-insensitive):
- MONDAY
- TUESDAY
- WEDNESDAY
- THURSDAY
- FRIDAY
- SATURDAY
- SUNDAY

### Numeric Days (Cron standard):
- 0 = SUNDAY
- 1 = MONDAY
- 2 = TUESDAY
- 3 = WEDNESDAY
- 4 = THURSDAY
- 5 = FRIDAY
- 6 = SATURDAY

## Common Scheduling Patterns

| Task | Cron Expression | Meaning |
|------|-----------------|---------|
| Daily backup at midnight | `0 0 *` | Every day at 12:00 AM |
| Friday report | `0 9 FRIDAY` | Every Friday at 9:00 AM |
| Monday morning standup | `30 8 MONDAY` | Every Monday at 8:30 AM |
| Weekly review Sunday | `0 20 SUNDAY` | Every Sunday at 8:00 PM |
| Mid-week check Wednesday | `45 15 3` | Every Wednesday at 3:45 PM |

## Error Handling

The parser validates input and throws `IllegalArgumentException` for invalid formats:

```java
try {
    // Invalid format - missing component
    new CronSchedulingStrategy("30 2");  // Throws exception
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}

try {
    // Invalid hour value
    new CronSchedulingStrategy("0 25 MONDAY");  // Throws exception
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}

try {
    // Invalid day of week
    new CronSchedulingStrategy("0 2 INVALID");  // Throws exception
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```

## Implementation Details

The `CronSchedulingStrategy` class now includes:

1. **String Constructor**: Parses cron expressions
   ```java
   new CronSchedulingStrategy(String cronExpression)
   ```

2. **Original Constructor**: Maintains backward compatibility
   ```java
   new CronSchedulingStrategy(int minute, int hour, DayOfWeek dayOfWeek)
   ```

3. **Parser Methods**:
   - `parseCronExpression()` - Main parser
   - `parseMinute()` - Validates and parses minute (0-59)
   - `parseHour()` - Validates and parses hour (0-23)
   - `parseDayOfWeek()` - Handles both day names and numbers

## Tips & Best Practices

1. **Use day names for clarity**: `"0 2 MONDAY"` is more readable than `"0 2 1"`

2. **24-hour format**: Remember to use 24-hour notation
   - 2 PM = 14
   - 9 PM = 21
   - 12 AM (midnight) = 0

3. **Validation**: The parser validates all inputs to catch errors early

4. **Timezone**: Cron times are based on system local time via `LocalDateTime.now()`

## High-Frequency Tasks (Every Second, Every Millisecond)

For tasks that need to run very frequently (sub-minute intervals), use `RecurringSchedulingStrategy` with `Duration` instead of `CronSchedulingStrategy`.

### Every Second Example
```java
// Run every second
scheduler.schedule(
    new PrintMessageTask("Heartbeat - System is running"),
    new RecurringSchedulingStrategy(Duration.ofSeconds(1))
);
```

### Other High-Frequency Examples
```java
// Every 500 milliseconds
scheduler.schedule(
    new PrintMessageTask("Fast pulse"),
    new RecurringSchedulingStrategy(Duration.ofMillis(500))
);

// Every 5 seconds
scheduler.schedule(
    new PrintMessageTask("Health check"),
    new RecurringSchedulingStrategy(Duration.ofSeconds(5))
);

// Every 30 seconds
scheduler.schedule(
    new PrintMessageTask("Cache refresh"),
    new RecurringSchedulingStrategy(Duration.ofSeconds(30))
);

// Every minute
scheduler.schedule(
    new PrintMessageTask("Activity log"),
    new RecurringSchedulingStrategy(Duration.ofMinutes(1))
);
```

### When to Use Each Strategy

| Strategy | Frequency | Example |
|----------|-----------|---------|
| CronSchedulingStrategy | Weekly | Every Monday at 2:00 AM |
| RecurringSchedulingStrategy | Any Interval | Every second, every 5 minutes, every hour |

## Future Enhancements (Optional)

If you need more advanced cron features, you can:
1. Add support for additional cron fields (day-of-month, month)
2. Support comma-separated values (e.g., "0 9,14 MONDAY,FRIDAY")
3. Support ranges (e.g., "0 9-17 MONDAY-FRIDAY")
4. Integrate Quartz Scheduler library for full cron expression support

## Migration from Legacy Code

If you have existing code using the old format:

**Before:**
```java
new CronSchedulingStrategy(30, 2, DayOfWeek.MONDAY)
```

**After (optional upgrade):**
```java
new CronSchedulingStrategy("30 2 MONDAY")
```

Both formats continue to work!

