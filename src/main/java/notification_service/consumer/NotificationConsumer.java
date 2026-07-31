package notification_service.consumer;

import lombok.extern.slf4j.Slf4j;
import notification_service.events.ApplicationStatusEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {
 @KafkaListener(
         topics = "application-status-events",
         groupId = "notification-group"
 )
    public void handleStatusChangeEvent(ApplicationStatusEvent event){
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
