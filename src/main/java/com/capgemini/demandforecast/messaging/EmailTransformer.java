package com.capgemini.demandforecast.messaging;

import com.capgemini.demandforecast.entity.Demand;

import jakarta.mail.*;
import jakarta.mail.internet.ContentType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Slf4j
public class EmailTransformer extends AbstractMailMessageTransformer<Demand> {

  private static final String DIR = "./";

  @Override
  protected AbstractIntegrationMessageBuilder<Demand> doTransform(Message mailMessage) {
    Demand demand = processPayload(mailMessage);
    assert demand != null;
    return MessageBuilder.withPayload(demand);
  }

  private Demand processPayload(Message mailMessage) {
    try {
      String subject = mailMessage.getSubject();
      String email = ((InternetAddress) mailMessage.getFrom()[0]).getAddress();
      String content = getTextFromMessage(mailMessage);
      Date receiveDate = mailMessage.getReceivedDate();
      List<String> attachments = downloadAttachments(mailMessage);

      return parseEmail(email, subject, content, receiveDate, attachments);
    } catch (MessagingException | IOException | ParseException e) {
      log.error("MessagingException: {0}", e);
    }
    return null;
  }

  private List<String> downloadAttachments(Message mailMessage)
      throws MessagingException, IOException {
    if (mailMessage.getContentType().contains("multipart")) {
      List<String> downloadedAttachments = new ArrayList<>();
      Multipart multipart = (Multipart) mailMessage.getContent();
      for (int i = 0; i < multipart.getCount(); i++) {
        MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(i);
        if ( Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
          String file = part.getFileName();
          part.saveFile(DIR + File.separator + part.getFileName());
          downloadedAttachments.add(file);
        }
      }
      return downloadedAttachments;
    }
    return null;
  }

  private String getTextFromMessage(Message message) throws IOException, MessagingException {
    String result = "";
    Object content = message.getContent();
    if (message.isMimeType("text/plain")) {
      result = message.getContent().toString();
    } else if (message.isMimeType("multipart/*")) {
      MimeMultipart mimeMultipart = (MimeMultipart) content;
      result = getTextFromMimeMultipart(mimeMultipart);
    }
    return result;
  }

  private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)
      throws MessagingException, IOException {
    int count = mimeMultipart.getCount();
    if (count == 0) throw new MessagingException("Email have no body part.");

    boolean multipartAlt =
        new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
    if (multipartAlt) {
      return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < count; i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      result.append(getTextFromBodyPart(bodyPart));
    }

    return result.toString();
  }

  private String getTextFromBodyPart(BodyPart bodyPart) throws MessagingException, IOException {
    String result = "";
    if (bodyPart.isMimeType("text/plain")) {
      result = (String) bodyPart.getContent();
    } else if (bodyPart.isMimeType("text/html")) {
      String html = (String) bodyPart.getContent();
      result = Jsoup.parse(html).text();
    } else if (bodyPart.getContent() instanceof MimeMultipart) {
      result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
    }
    return result;
  }

  private Demand parseEmail(String senderEmailAddress, String subject, String content, Date receiveDate, List<String> attachments) throws ParseException {
    return new Demand(senderEmailAddress, subject, content, receiveDate, attachments);
  }


}
