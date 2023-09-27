package com.capgemini.demandforecast.config;

import com.capgemini.demandforecast.entity.Demand;
import com.capgemini.demandforecast.messaging.EmailTransformer;
import com.capgemini.demandforecast.repository.DemandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;

@Slf4j
@Configuration
public class MailIntegrationConfig {

  @Bean
  public IntegrationFlow mainIntegration(EmailProperties props, EmailTransformer emailTransformer, DemandRepository demandRepository) {

    return IntegrationFlow.from(
            Mail.imapInboundAdapter(props.getImapUrl())
                .shouldDeleteMessages(false)
                .simpleContent(true)
                .autoCloseFolder(false),
            e -> e.poller(Pollers.fixedDelay(props.getPollRate())))
//        .<Message>filter(
//            (Message) -> {
//              boolean containsKeyword = false;
//              try {
//                Address[] sender = Message.getFrom();
//                validSender = sender[0].toString().equalsIgnoreCase("rameshkrch@gmail.com");
//                  containsKeyword = Message.getSubject().toUpperCase().contains("IKEA");
//              } catch (MessagingException e) {
//                throw new RuntimeException(e);
//              }
//              return containsKeyword;
//            })
            .transform(emailTransformer)
        .handle(
            message -> {
              log.info("New message received: ", message);
                Demand demand = (Demand) message.getPayload();
                demandRepository.save(demand);
            })
        .get();
  }
}
