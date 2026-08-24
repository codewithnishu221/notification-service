package notification_service.controller;

import lombok.RequiredArgsConstructor;
import notification_service.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final EmailService emailService;

    @GetMapping("/test-email")
    public String testEmail(){
        emailService.sendStatusChangeEmail(
                "nishutest@gmail.com", "Test User", "Google",
                "Backend Developer",
                "INTERVIEW_SCHEDULED",
                "29"
        );
        return "EMail sent- check your inbox";
    }

}
