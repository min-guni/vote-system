package meeting.decision.config;

import com.maxmind.geoip2.DatabaseReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;

@Configuration
@Profile("prod")
public class GeoIp2Config {

    @Value("${geoip2.database.path}")
    private String path;

    @Bean
    public DatabaseReader databaseReader() throws IOException {
        File resource = new File(path);
        return new DatabaseReader.Builder(resource).build();
    }
}
