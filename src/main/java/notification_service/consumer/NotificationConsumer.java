package notification_service.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification_service.events.ApplicationStatusEvent;
import notification_service.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final EmailService emailService;
 @KafkaListener(
         topics = "application-status-events",
         groupId = "notification-group"
 )
    public void handleStatusChangeEvent(ApplicationStatusEvent event){
     log.info("Received status change event for applicationId: {}", event.getApplicationId());

     if(event.getUserEmail() == null || event.getUserEmail().isBlank()){
         log.warn("No user email in event for applicationId: {} - skipping email", event.getApplicationId());

         return;
     }
     emailService.sendStatusChangeEmail(
             event.getUserEmail(),
             event.getUserName(),
             event.getCompanyName(),
             event.getJobTitle(),
             event.getNewStatus().toString(),
             event.getApplicationId().toString()
             );
     log.info("=== NOTIFICATION SERVICE RECEIVED EVENT ===");
     log.info("Application ID: {}", event.getApplicationId());
     log.info("User ID: {}", event.getUserId());
     log.info("Company: {}", event.getCompanyName());
     log.info("Job Title: {}", event.getCompanyName());
     log.info("New Status: {}", event.getNewStatus());
     log.info("Action: Would send email to user about status change to {}", event.getNewStatus());
     log.info("========================================");
 }

}
