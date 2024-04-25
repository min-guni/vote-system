package meeting.decision.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE) //transactional이 제일 먼저 시작하도록 설정
public class TransactionConfig {
}
