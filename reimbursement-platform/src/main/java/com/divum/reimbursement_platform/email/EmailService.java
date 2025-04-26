package com.divum.reimbursement_platform.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


@RequiredArgsConstructor
@Log4j2
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private String prepareEmailContent(String templatePath, Map<String, String> replacements) {
        try {
            // Load the template file
            Resource resource = new ClassPathResource(templatePath);
            String content = new String(Files.readAllBytes(Paths.get(resource.getURI())));

            // Replace all placeholders
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                content = content.replace("{" + entry.getKey() + "}", entry.getValue());
            }

            return content;
        } catch (Exception e) {
            log.error("Failed to prepare email content", e);
            throw new RuntimeException("Failed to prepare email content", e);
        }
    }

    public void sendReimbursementEmail(final String receiver,
                                       final String subject,
                                       final String templatePath,
                                       final Map<String, String> replacements) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(subject);

            String htmlContent = prepareEmailContent(templatePath, replacements);
            helper.setText(htmlContent, true);

//            log.info("Sending email to {} with template {}", receiver, templatePath);
//            mailSender.send(message);
            log.info("Sent email to {} with template {}", receiver, templatePath);
        } catch (MessagingException e) {
            log.error("Error while sending email to {} with template {}", receiver, templatePath);
        }
    }
}
