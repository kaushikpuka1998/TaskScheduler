package Tasks;

public class PrintMessageTask implements Task {
    private String message;

    public PrintMessageTask(String name) {
        this.message = name;
    }

    @Override
    public String getName() {
        return "PrintMessageTask";
    }

    @Override
    public void execute() throws Exception {
        // Simulate printing a message
        System.out.println("Executing task: " + getName());
        System.out.println("Message: " + this.message);
        Thread.sleep(1000); // Simulate time taken to print the message
        System.out.println("Message printed successfully.");
    }
}
