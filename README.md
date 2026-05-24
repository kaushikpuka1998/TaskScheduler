# 🚀 Task Scheduler - Low Level Design (LLD)

A scalable, thread-safe, and extensible **Task Scheduler System** built using **Core Java**, demonstrating:

- Multithreading
- Producer Consumer Pattern
- Scheduling Algorithms
- Retry Mechanism
- CRON Scheduling
- Thread Coordination
- Design Patterns
- SOLID Principles

---

## 🔥 Features

### ✅ Core Features

- One-time task scheduling
- Recurring task scheduling
- CRON-based scheduling
- Concurrent task execution
- Worker thread pool
- Retry mechanism
- Task cancellation
- Task lifecycle tracking
- Observer notifications
- Thread-safe scheduling

---

# 🧠 Design Patterns Used

| Pattern | Usage |
|---|---|
| Command Pattern | Encapsulating executable tasks |
| Strategy Pattern | Scheduling algorithms |
| Observer Pattern | Lifecycle notifications |
| Singleton Pattern | Single scheduler instance |
| Producer Consumer Pattern | Queue + Worker coordination |

---

# 🏗️ High Level Architecture

```text
                +----------------------+
                |  Client Application  |
                +----------+-----------+
                           |
                           v
                +----------------------+
                | TaskSchedulerService |
                +----------+-----------+
                           |
       ---------------------------------------------
       |                    |                      |
       v                    v                      v
+-------------+    +----------------+    +----------------+
| Priority    |    | Worker Threads |    | Observers      |
| Queue       |    | (Consumers)    |    | (Listeners)    |
+-------------+    +----------------+    +----------------+
       |
       v
+----------------------+
| ScheduledTask        |
+----------------------+
       |
       +----------------------------+
       |                            |
       v                            v
+--------------+        +-----------------------+
| Task         |        | SchedulingStrategy    |
+--------------+        +-----------------------+
```

---

# 📂 Project Structure

```text
task-scheduler/
│
├── enums/
│   └── TaskStatus.java
│
├── exceptions/
│   └── TaskSchedulerException.java
│
├── task/
│   ├── Task.java
│   ├── PrintMessageTask.java
│   └── DataBackupTask.java
│
├── strategy/
│   ├── SchedulingStrategy.java
│   ├── OneTimeSchedulingStrategy.java
│   ├── RecurringSchedulingStrategy.java
│   └── CronSchedulingStrategy.java
│
├── retry/
│   └── RetryPolicy.java
│
├── observer/
│   ├── TaskExecutionObserver.java
│   └── LoggingObserver.java
│
├── model/
│   ├── ScheduledTask.java
│   └── RetryableScheduledTask.java
│
├── scheduler/
│   └── TaskSchedulerService.java
│
└── demo/
    └── TaskSchedulerDemo.java
```

---

# ⚙️ Functional Requirements

- Schedule one-time tasks
- Schedule recurring tasks
- Schedule CRON tasks
- Execute tasks concurrently
- Retry failed tasks
- Cancel scheduled tasks
- Notify observers
- Track task lifecycle

---

# 📌 Non Functional Requirements

- Thread-safe
- Extensible
- Scalable
- SOLID-compliant
- Reliable concurrent execution
- High cohesion and loose coupling

---

# 🧩 Core Components

---

## 1️⃣ Task

Represents executable work.

```java
public interface Task {

    String getName();

    void execute() throws Exception;
}
```

---

## 2️⃣ SchedulingStrategy

Defines scheduling behavior.

```java
public interface SchedulingStrategy {

    Optional<LocalDateTime> getNextExecutionTime(
            LocalDateTime lastExecutionTime
    );
}
```

---

# ⏰ Scheduling Strategies

---

## ✅ One-Time Scheduling

Executes once at a future time.

```java
new OneTimeSchedulingStrategy(
        LocalDateTime.now().plusSeconds(5)
)
```

---

## 🔁 Recurring Scheduling

Executes repeatedly at fixed intervals.

```java
new RecurringSchedulingStrategy(
        Duration.ofSeconds(10)
)
```

---

## 🕒 CRON Scheduling

Supports CRON expressions.

```java
new CronSchedulingStrategy(
        "* * * * * SUN"
)
```

---

# 🧾 CRON Format

Spring-style 6-field CRON format:

```text
┌──────── second
│ ┌────── minute
│ │ ┌──── hour
│ │ │ ┌── day of month
│ │ │ │ ┌ month
│ │ │ │ │ ┌ day of week
│ │ │ │ │ │
* * * * * *
```

---

# 🧪 CRON Examples

| Requirement | Expression |
|---|---|
| Every second | `* * * * * *` |
| Every 5 seconds | `*/5 * * * * *` |
| Every Sunday | `0 0 0 * * SUN` |
| Every second on Sunday | `* * * * * SUN` |
| Every Monday 9 AM | `0 0 9 * * MON` |

---

# 🔄 Retry Mechanism

## Why Retry?

Distributed systems can fail because of:

- Network failures
- Temporary DB downtime
- API timeout
- Service unavailability

Retry handling improves:

- Reliability
- Availability
- Fault tolerance

---

# 🛡️ Retry Policy

```java
RetryPolicy retryPolicy =
        new RetryPolicy(3, 2000);
```

Meaning:
- Maximum 3 retries
- 2-second retry delay

---

# 🔥 Retryable Task

```java
RetryableScheduledTask retryTask =
        new RetryableScheduledTask(
                task,
                strategy,
                sequenceNumber,
                retryPolicy
        );
```

---

# 🧵 Thread Safety

---

## 🔐 Synchronization

Uses:

- `synchronized`
- `wait()`
- `notifyAll()`

for thread-safe coordination.

---

## 📌 Priority Queue

Tasks are ordered using:

- Execution time
- FIFO sequence number

---

## ⚡ AtomicLong

Used for:
- Thread-safe sequence generation

---

## 📋 CopyOnWriteArrayList

Ideal for observers:
- Frequent reads
- Rare modifications

---

# 🔄 Task Lifecycle

```text
SCHEDULED
    ↓
RUNNING
    ↓
COMPLETED
    ↓
RESCHEDULED (Recurring tasks)
```

---

## ❌ Failure Flow

```text
RUNNING
    ↓
FAILED
    ↓
RETRY
    ↓
FAILED AGAIN
    ↓
DEAD LETTER QUEUE (Future Enhancement)
```

---

# 🚀 Example Usage

---

## ✅ One-Time Task

```java
scheduler.schedule(
        new PrintMessageTask("Hello World"),
        new OneTimeSchedulingStrategy(
                LocalDateTime.now().plusSeconds(5)
        )
);
```

---

## 🔁 Recurring Task

```java
scheduler.schedule(
        new DataBackupTask(
                "/source",
                "/backup"
        ),
        new RecurringSchedulingStrategy(
                Duration.ofSeconds(10)
        )
);
```

---

## 🕒 CRON Task

```java
scheduler.schedule(
        new PrintMessageTask(
                "Running every second on Sunday"
        ),
        new CronSchedulingStrategy(
                "* * * * * SUN"
        )
);
```

---

# ▶️ Running the Application

---

## 🔨 Compile

```bash
javac */*.java
```

---

## ▶️ Run

```bash
java demo.TaskSchedulerDemo
```

---

# 📊 Sample Output

```text
Task Started : PrintMessageTask
[2026-05-24T12:30:11] Hello World
Task Completed : PrintMessageTask

Task Started : DataBackupTask
Backing up data from /source to /backup
Backup completed successfully
Task Completed : DataBackupTask
```

---

# ⏱️ Time Complexity

| Operation | Complexity |
|---|---|
| Schedule Task | O(log N) |
| Poll Task | O(log N) |
| Cancel Task | O(N) |
| Notify Observers | O(K) |

Where:
- N = Number of tasks
- K = Number of observers

---

# 🧱 SOLID Principles

---

## ✅ Single Responsibility Principle

Each class has one responsibility.

---

## ✅ Open Closed Principle

New scheduling strategies can be added easily.

---

## ✅ Liskov Substitution Principle

All strategies are interchangeable.

---

## ✅ Interface Segregation Principle

Small focused interfaces.

---

## ✅ Dependency Inversion Principle

Depends on abstractions.

---

# 🔮 Future Enhancements

- Exponential Backoff Retry
- Dead Letter Queue (DLQ)
- Distributed Scheduler
- Persistent Storage
- Leader Election
- Priority Scheduling
- Metrics & Monitoring
- REST APIs
- Dashboard UI
- Kafka Integration

---

# ❓ Why Not ScheduledExecutorService?

This project intentionally uses:

- Raw threads
- wait/notify
- PriorityQueue

to demonstrate:
- Thread coordination internals
- Scheduling mechanics
- Concurrency understanding

which are frequently asked in backend interviews.

---

# 🎯 Interview Topics Covered

- Multithreading
- Producer Consumer Pattern
- Scheduling Algorithms
- Retry Handling
- CRON Scheduling
- Thread Safety
- Design Patterns
- Low Level Design
- Concurrent Programming

---

# 🛠️ Technologies Used

- Java 17+
- Core Java Concurrency
- Spring CronExpression (optional)
- OOP Principles
- Collections Framework

---

# 👨‍💻 Author

## Kaushik Ghosh

Software Engineer | Backend Developer | Java Enthusiast

### GitHub Repository

https://github.com/kaushikpuka1998/TaskScheduler

---

# ⭐ If you found this useful, please give the repository a star!