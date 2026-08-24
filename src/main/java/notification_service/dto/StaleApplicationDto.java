package notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaleApplicationDto {
    private Long applicationId;
    private Long userId;
    private String userName;
    private String userEmail;
    private String companyName;
    private String jobTitle;
    private int daysSinceApplied;
}