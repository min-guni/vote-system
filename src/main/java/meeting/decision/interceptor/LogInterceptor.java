package meeting.decision.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {
    private static final long WARNING_THRESHOLD = 1000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());

        String uuid = UUID.randomUUID().toString().substring(0,8);
        request.setAttribute("uuid", uuid);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBeanType().getSimpleName();

        String uuid = (String) request.getAttribute("uuid");

        if (executionTime > WARNING_THRESHOLD) {
            log.warn("[{}] [{}.{}] executed in {} ms", uuid , className , methodName , executionTime);
        } else {
            log.info("[{}] [{}.{}] executed in {} ms", uuid , className , methodName , executionTime);
        }
    }
}
