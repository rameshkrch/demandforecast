package com.capgemini.demandforecast.messaging;

import com.capgemini.demandforecast.entity.Email;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.ContentType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;

import java.io.IOException;

@Slf4j
public class EmailTransformer extends AbstractMailMessageTransformer<Email> {

  @Override
  protected AbstractIntegrationMessageBuilder<Email> doTransform(Message mailMessage) {
    Email email = processPayload(mailMessage);
    return MessageBuilder.withPayload(email);
  }

  private Email processPayload(Message mailMessage) {
    try {
      String subject = mailMessage.getSubject();
      String email = ((InternetAddress) mailMessage.getFrom()[0]).getAddress();
      String content = getTextFromMessage(mailMessage);

      return parseEmail(email, subject, content);
    } catch (MessagingException | IOException e) {
      log.error("MessagingException: {}", e);
    }

    return null;
  }

  private Email parseEmail(String email, String subject, String content) {
    return null;
  }

  private String getTextFromMessage(Message message) throws IOException, MessagingException {
    String result = "";
    if (message.isMimeType("text/plain")) {
      result = message.getContent().toString();
    } else if(message.isMimeType("multipart/*")){
      MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
      result = getTextFromMimeMultipart(mimeMultipart);
    }
    return result;
  }

  private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException {
    int count = mimeMultipart.getCount();
    if (count == 0) throw new MessagingException("Email have no body part.");

    boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
    if (multipartAlt) {
      return getTextFromBodypart(mimeMultipart.getBodyPart(count - 1));
    }
    return null;
  }

  private String getTextFromBodypart(BodyPart bodyPart) {
    return null;
  }

}
