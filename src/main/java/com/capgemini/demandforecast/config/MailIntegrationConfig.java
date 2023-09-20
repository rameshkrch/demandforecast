package com.capgemini.demandforecast.config;

import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

import java.util.Arrays;

@Slf4j
@Configuration
public class MailIntegrationConfig {

  @Bean
  public IntegrationFlow mainIntegration(EmailProperties props) {
    return IntegrationFlow.from(
            Mail.imapInboundAdapter(props.getImapUrl())
                .shouldDeleteMessages(false)
                .simpleContent(true)
                .autoCloseFolder(false),
            e -> e.poller(Pollers.fixedDelay(props.getPollRate())))
        .<Message>filter(
            (Message) -> {
              boolean validSender = false;
              try {
                validSender = Message.getSubject().contains("test");
              } catch (MessagingException e) {
                throw new RuntimeException(e);
              }
              return validSender;
            })
        .handle(
            message -> {
              log.info("New message received: ", message);
            })
        .get();
  }
}
