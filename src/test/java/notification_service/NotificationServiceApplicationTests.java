package notification_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

@SpringBootTest
@ActiveProfiles("test")
class NotificationServiceApplicationTests {

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private RestClient restClient;

    @Test
    void contextLoads() {
    }
}