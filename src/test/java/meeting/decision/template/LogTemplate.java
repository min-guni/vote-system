package meeting.decision.template;

import jakarta.persistence.EntityManager;

public class LogTemplate {
    public void execute(EntityManager em, CheckedRunnable runnable) throws Exception{
        em.flush();
        em.clear();
        System.out.println("수행 시작");
        long start = System.currentTimeMillis();
        runnable.run();
        em.flush();
        em.clear();
        long end = System.currentTimeMillis();
        System.out.println("수행 종료 [수행시간 : " + (end - start) + "ms]");
    }
}
