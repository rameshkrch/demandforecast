package com.capgemini.demandforecast.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

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

  public Demand(String email, String subject, String content, Date receiveDate) {
    this.email = email;
    this.subject = subject;
    this.content = content;
    this.receiveDate = receiveDate;

    this.deadline = getDeadline(content, receiveDate);
    this.skills = getSkills(content);
    this.customer = getCustomer(content);
    this.jobCategory = getJobCategory(content);
  }

  private String getJobCategory(String content) {
    for (DemandCategory demandCategory : DemandCategory.values()) {
      if (Pattern.compile(Pattern.quote(String.valueOf(demandCategory)), Pattern.CASE_INSENSITIVE)
          .matcher(content)
          .find()) {
        return demandCategory.name();
      }
    }
    return "OTHERS";
  }

  public String getCustomer(String content) {
    for (KnownCustomer knownCustomer : KnownCustomer.values()) {
      if (Pattern.compile(Pattern.quote(knownCustomer.name()), Pattern.CASE_INSENSITIVE)
          .matcher(content)
          .find()) {
        return knownCustomer.name();
      }
      if (knownCustomer.isPSU) {
        PSU = true;
      }
    }
    return "OTHERS";
  }

  public ArrayList<String> getSkills(String content) {
    ArrayList<String> skillsList = new ArrayList<>();
    for (Skills skills : Skills.values()) {
      if (Pattern.compile(Pattern.quote(String.valueOf(skills)), Pattern.CASE_INSENSITIVE)
              .matcher(content)
              .find()) {
        skillsList.add(skills.name());
      }
    }
    if (skillsList.isEmpty()) {
      skillsList.add("OTHERS");
    }
    return skillsList;
  }

  public Date getDeadline(String content, Date receiveDate) {
    // TO-DO
    return receiveDate;
  }

}
