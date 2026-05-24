package Tasks;

public class DataBackupTask implements Task {
    private String source;
    private String destination;

    public DataBackupTask( String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String getName() {
        return "DataBackupTask";
    }

    @Override
    public void execute() throws Exception {
        // Simulate data backup logic
        System.out.println("Backing up data from " + source + " to " + destination);
        // Here you would add the actual backup logic, such as copying files or databases
        // For demonstration, we will just print a message
        Thread.sleep(2000); // Simulate time taken for backup
        System.out.println("Data backup completed successfully.");
    }
}
