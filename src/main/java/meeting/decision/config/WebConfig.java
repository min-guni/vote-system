package meeting.decision.config;

import lombok.RequiredArgsConstructor;
import meeting.decision.argumentresolver.LoginUserArgumentResolver;
import meeting.decision.fomatter.VoteResultTypeEnumFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;
    private final VoteResultTypeEnumFormatter voteResultTypeEnumFormatter;


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(voteResultTypeEnumFormatter);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
