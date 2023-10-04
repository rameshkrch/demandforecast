package com.capgemini.demandforecast.config;

import com.capgemini.demandforecast.entity.Demand;
import com.capgemini.demandforecast.messaging.EmailTransformer;
import com.capgemini.demandforecast.repository.DemandRepository;
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

  private static final String SENDER = "RAMESHKRCH@GMAIL.COM";

  @Bean
  public IntegrationFlow mainIntegration(
      EmailProperties props, EmailTransformer emailTransformer, DemandRepository demandRepository) {

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
                validSender =
                    Arrays.stream(Message.getFrom())
                        .anyMatch(i -> i.toString().toUpperCase().contains(SENDER));
              } catch (MessagingException e) {
                log.error("Exception caught while filtering messages.", e);
              }
              return validSender;
            })
        .transform(emailTransformer)
        .handle(
            message -> {
              log.info("New message received: {}", message);
              Demand demand = (Demand) message.getPayload();
              demandRepository.save(demand);
            })
        .get();
  }
}
