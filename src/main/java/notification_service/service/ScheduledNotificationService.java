package notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification_service.dto.StaleApplicationDto;
import notification_service.dto.UpcomingInterviewDto;
import notification_service.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledNotificationService {

    private final EmailService emailService;
    private final RestClient restClient; // calls Tracker Service

    // runs every day at 9 AM
    @Scheduled(cron = "0 */2 * * * ?")
    public void sendFollowUpReminders() {
        log.info("Running follow-up reminder scheduled job");

        try {
            // call Tracker Service to get all applications
            // that have been in APPLIED status for 7+ days
            // Tracker Service needs a new internal endpoint for this
            StaleApplicationDto[] staleApps = restClient.get()
                    .uri("/api/applications/internal/stale")
                    .retrieve()
                    .body(StaleApplicationDto[].class);

            if (staleApps == null) return;

            for (StaleApplicationDto app : staleApps) {
                emailService.sendFollowUpReminderEmail(
                        app.getUserEmail(),
                        app.getUserName(),
                        app.getCompanyName(),
                        app.getJobTitle(),
                        app.getDaysSinceApplied()
                );
                log.info("Sent follow-up reminder for applicationId: {}", app.getApplicationId());
            }
        } catch (Exception e) {
            log.error("Failed to run follow-up reminder job", e);
        }
    }

    // runs every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendInterviewReminders() {
        log.info("Running interview reminder scheduled job");

        try {
            // call Tracker Service to get all applications
            // that have INTERVIEW_SCHEDULED status and interview is tomorrow
            UpcomingInterviewDto[] upcomingInterviews = restClient.get()
                    .uri("/api/applications/internal/upcoming-interviews")
                    .retrieve()
                    .body(UpcomingInterviewDto[].class);

            if (upcomingInterviews == null) return;

            for (UpcomingInterviewDto interview : upcomingInterviews) {
                emailService.sendInterviewReminderEmail(
                        interview.getUserEmail(),
                        interview.getUserName(),
                        interview.getCompanyName(),
                        interview.getJobTitle()
                );
            }
        } catch (Exception e) {
            log.error("Failed to run interview reminder job", e);
        }
    }
}