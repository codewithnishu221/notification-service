package notification_service.service;

import notification_service.dto.StaleApplicationDto;
import notification_service.dto.UpcomingInterviewDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledNotificationServiceTest {

    @Mock
    private EmailService emailService;

    // Answers.RETURNS_DEEP_STUBS allows chaining restClient.get().uri().retrieve().body(...)
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private RestClient restClient;

    @InjectMocks
    private ScheduledNotificationService scheduledNotificationService;

    @Test
    @DisplayName("sendFollowUpReminders queries Tracker Service and dispatches reminder email")
    void sendFollowUpReminders_Success() {
        StaleApplicationDto stale = new StaleApplicationDto();
        stale.setApplicationId(1L);
        stale.setUserEmail("user@test.com");
        stale.setUserName("Alice");
        stale.setCompanyName("Netflix");
        stale.setJobTitle("Backend Engineer");
        stale.setDaysSinceApplied(10);

        when(restClient.get()
                .uri("/api/applications/internal/stale")
                .retrieve()
                .body(StaleApplicationDto[].class))
                .thenReturn(new StaleApplicationDto[]{stale});

        scheduledNotificationService.sendFollowUpReminders();

        verify(emailService, times(1)).sendFollowUpReminderEmail(
                eq("user@test.com"), eq("Alice"), eq("Netflix"), eq("Backend Engineer"), eq(10)
        );
    }

    @Test
    @DisplayName("sendInterviewReminders queries Tracker Service and dispatches interview reminder")
    void sendInterviewReminders_Success() {
        UpcomingInterviewDto interview = new UpcomingInterviewDto();
        interview.setUserEmail("user@test.com");
        interview.setUserName("Bob");
        interview.setCompanyName("Uber");
        interview.setJobTitle("Platform Engineer");

        when(restClient.get()
                .uri("/api/applications/internal/upcoming-interviews")
                .retrieve()
                .body(UpcomingInterviewDto[].class))
                .thenReturn(new UpcomingInterviewDto[]{interview});

        scheduledNotificationService.sendInterviewReminders();

        verify(emailService, times(1)).sendInterviewReminderEmail(
                eq("user@test.com"), eq("Bob"), eq("Uber"), eq("Platform Engineer")
        );
    }
}