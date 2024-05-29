package meeting.decision.filter;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
public class IpFilter implements Filter {
    private final DatabaseReader dbReader;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String ip = httpServletRequest.getRemoteAddr();
        try{
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse countryResponse = dbReader.country(ipAddress);
            String countryCode = countryResponse.getCountry().getIsoCode();
            if("KR".equals(countryCode)){
                chain.doFilter(request, response);
            }
            else{
                log.error("Access Denied | IP ADDRESS : {} | COUNTRY CODE : {}", ipAddress , countryCode);
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpServletResponse.getWriter().write("Access Denied");
            }
        } catch (GeoIp2Exception e) {
            log.error("Error processing IP check", e);
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().write("Error processing IP check");
        }
    }

    @Override
    public void destroy() {
        try {
            dbReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
