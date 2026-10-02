package notification_service.service;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", "noreply@jobtracker.com");
        ReflectionTestUtils.setField(emailService, "fromName", "Job Tracker Team");
    }

    @Test
    @DisplayName("sendStatusChangeEmail renders template and dispatches email")
    void sendStatusChangeEmail_Success() {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("emails/status-change"), any(Context.class)))
                .thenReturn("Status Updated");

        emailService.sendStatusChangeEmail(
                "candidate@test.com", "John", "Google", "Backend Engineer", "INTERVIEW_SCHEDULED", "101"
        );

        verify(templateEngine, times(1)).process(eq("emails/status-change"), any(Context.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("sendFollowUpReminderEmail renders follow up template and dispatches")
    void sendFollowUpReminderEmail_Success() {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("emails/follow-up-reminder"), any(Context.class)))
                .thenReturn("Follow Up");

        emailService.sendFollowUpReminderEmail(
                "candidate@test.com", "John", "Microsoft", "Fullstack", 8
        );

        verify(templateEngine, times(1)).process(eq("emails/follow-up-reminder"), any(Context.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("sendInterviewReminderEmail renders interview template and dispatches")
    void sendInterviewReminderEmail_Success() {
        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(new Properties()));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("emails/interview-reminder"), any(Context.class)))
                .thenReturn("Interview Tomorrow");

        emailService.sendInterviewReminderEmail(
                "candidate@test.com", "John", "Apple", "iOS Dev"
        );

        verify(templateEngine, times(1)).process(eq("emails/interview-reminder"), any(Context.class));
        verify(mailSender, times(1)).send(mimeMessage);
    }
}