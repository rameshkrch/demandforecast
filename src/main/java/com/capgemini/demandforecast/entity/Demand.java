package com.capgemini.demandforecast.entity;

import com.capgemini.demandforecast.messaging.EmailTransformer;
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

  private EmailTransformer emailtransformer;

  public Demand(String email, String subject, String content, Date receiveDate, List<String> attachments)
      throws ParseException {
    this.email = email;
    this.subject = subject;
    this.content = content;
    this.receiveDate = receiveDate;
    this.attachments = attachments;

    this.deadline = emailtransformer.getDeadline(receiveDate);
    this.skills = emailtransformer.getSkills(content);
    this.customer = emailtransformer.getCustomer(content,this);
    this.jobCategory = emailtransformer.getJobCategory(content);
    this.status = emailtransformer.getStatus(receiveDate);
    this.hasAttachment = emailtransformer.getHasAttachment(attachments);
  }

  public void setPSU(boolean PSU) {
    this.PSU = PSU;
  }
}
