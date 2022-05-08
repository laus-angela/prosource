package email.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class HttpClientConfiguration {
    private static final int CONNECTION_TIMEOUT_SEC = 5;
    private static final int READ_TIMEOUT_SEC = 5;

    @Bean
    @Primary
    public RestTemplate restTemplate(final ObjectMapper objectMapper) {
        return new RestTemplateBuilder()
                .additionalMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .setConnectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SEC))
                .setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SEC))
                .build();
    }
}
