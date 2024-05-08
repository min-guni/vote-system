package meeting.decision.config;

import lombok.RequiredArgsConstructor;
import meeting.decision.filter.IpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.parameters.P;

@Configuration
@RequiredArgsConstructor
@Profile("prod")
public class FilterConfig {

    private final IpFilter ipFilter;

    @Bean
    public FilterRegistrationBean<IpFilter> ipFilterFilterRegistrationBean(){
        FilterRegistrationBean<IpFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(ipFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
