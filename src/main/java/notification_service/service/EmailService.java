package notification_service.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    @Value("${app.mail.from}")
    private String fromEmail;
    @Value("${app.mail.from-name}")
    private String fromName;

    public void sendStatusChangeEmail(String toEmail, String userName, String companyName, String jobTitle, String newStatus, String applicationId){
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("to", toEmail);
        variables.put("jobTitle", jobTitle);
        variables.put("companyName", companyName);
        variables.put("newStatus", newStatus);
        context.setVariables(variables);

         String renderHtml = templateEngine.process("emails/status-change", context);
         String subject = "Application Update: {jobTitle} at {companyName}";
          sendHtmlEmail(toEmail, subject, renderHtml);
    }
    public void sendFollowUpReminderEmail(String toEmail, String userName, String companyName, String jobTitle, int daysSinceApplied){
        String subject = "Time to follow up? {jobTitle} at {companyName} - {daysSinceApplied} days ago";
        Context context = new Context();
        Map<String, Object> contextVariables = new HashMap<>();
        contextVariables.put("userName", userName);
        contextVariables.put("jobTitle", jobTitle);
        contextVariables.put("daysSinceApplied", daysSinceApplied);
        contextVariables.put("companyName", companyName);
        String htmlTemplate = templateEngine.process("emails/follow-up-reminder", context);
        sendHtmlEmail(toEmail, subject, htmlTemplate);
    }
    public void sendInterviewReminderEmail(String toEmail, String userName, String companyName, String jobTitle){
        String subject = "Interview Tomorrow? {jobTitle} at {companyName} - You've got this!";
        Context context = new Context();
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("companyName", companyName);
        variables.put("jobTitle", jobTitle);
        context.setVariables(variables);
        String htmlTemplate = templateEngine.process("emails/interview-reminder", context);
        sendHtmlEmail(toEmail, subject, htmlTemplate );
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
           log.error("Failed to send email to: {}", to, e);
        }
    }
}
