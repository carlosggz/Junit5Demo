package com.example.junit5demo.infrastructure.mail;

import com.example.junit5demo.domain.contracts.notifiers.MailNotifier;
import com.example.junit5demo.domain.exceptions.MailNotifierException;
import com.example.junit5demo.infrastructure.config.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Component
public class MailNotifierImpl implements MailNotifier {
    final static String ENCODING = "UTF-8";
    final MailProperties mailProperties;
    final JavaMailSender mailSender;

    @Override
    public void sendNotification(String body) throws MailNotifierException {

        try {
            MimeMessage message = getMessage(body);
            mailSender.send(message);
        }
        catch (Exception ex) {
            throw new MailNotifierException("Error sending message", ex);
        }
    }

    private MimeMessage getMessage(String body) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, ENCODING);

        helper.setSubject(mailProperties.getSubject());
        helper.setFrom(mailProperties.getFromAddress(), mailProperties.getFromName());
        helper.setTo(mailProperties.getToAddress().toArray(String[]::new));

        if (!CollectionUtils.isEmpty(mailProperties.getBccAddress())) {
            helper.setBcc(mailProperties.getBccAddress().toArray(String[]::new));
        }

        helper.setText(body, true);

        return message;
    }
}
