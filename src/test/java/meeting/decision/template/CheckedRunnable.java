package meeting.decision.template;

@FunctionalInterface
public interface CheckedRunnable {
    public abstract void run() throws Exception;
}
