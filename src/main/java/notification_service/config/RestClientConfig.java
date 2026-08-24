package notification_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced // Use this if calling Tracker Service via Eureka service name (e.g. http://APPLICATION-TRACKER-SERVICE)
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        // Base URL can be configured here or passed dynamically in ScheduledNotificationService
        return builder
                .baseUrl("http://APPLICATION-TRACKER-SERVICE") // Or your target service/gateway URL
                .build();
    }
}