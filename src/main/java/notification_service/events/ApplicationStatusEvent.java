package notification_service.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import notification_service.enums.ApplicationStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusEvent {
    private Long applicationId;
    private Long userId;
    private String companyName;
    private String jobTitle;
    private ApplicationStatus newStatus;
    private String userEmail;
}
