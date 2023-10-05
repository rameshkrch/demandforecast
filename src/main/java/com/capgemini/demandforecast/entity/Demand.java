package com.capgemini.demandforecast.entity;

import com.capgemini.demandforecast.messaging.EmailToDemandMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Document(collection = "demands")
public class Demand implements Serializable {

  @Id private String id;
  private Date receiveDate;
  private Date deadline;
  private String email;
  private String subject;
  private boolean hasAttachment;
  private String content;
  private ArrayList<String> skills;
  private String customer;
  private boolean PSU;
  private String jobCategory;
  private String status;
  private List<String> attachments;

  private EmailToDemandMapper emailtodemandmapper;

  public Demand(String email, String subject, String content, Date receiveDate, List<String> attachments)
      throws ParseException {
    this.email = email;
    this.subject = subject;
    this.content = content;
    this.receiveDate = receiveDate;
    this.attachments = attachments;

    this.deadline = emailtodemandmapper.getDeadline(receiveDate);
    this.skills = emailtodemandmapper.getSkills(content);
    this.customer = emailtodemandmapper.getCustomer(content,this);
    this.jobCategory = emailtodemandmapper.getJobCategory(content);
    this.status = emailtodemandmapper.getStatus(receiveDate);
    this.hasAttachment = emailtodemandmapper.getHasAttachment(attachments);
  }

  public void setPSU(boolean PSU) {
    this.PSU = PSU;
  }
}
