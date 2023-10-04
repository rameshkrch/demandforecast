package com.capgemini.demandforecast.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
  private String status;
  private List<String> attachments;

  public Demand(String email, String subject, String content, Date receiveDate, List<String> attachments)
      throws ParseException {
    this.email = email;
    this.subject = subject;
    this.content = content;
    this.receiveDate = receiveDate;
    this.attachments = attachments;

    this.deadline = getDeadline(content, receiveDate);
    this.skills = getSkills(content);
    this.customer = getCustomer(content);
    this.jobCategory = getJobCategory(content);
    this.status = getStatus(receiveDate);
    this.hasAttachment = getHasAttachment(attachments);
  }

  private boolean getHasAttachment(List<String> attachments) {
    return !attachments.isEmpty();
  }

  private String getStatus(Date receiveDate) {
    return receiveDate.compareTo(getDeadline()) <= 0 ? "Open" : "Closed";
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

  public Date getDeadline(String content, Date receiveDate) throws ParseException {
    /*    String regex = "Deadline: (\\d{2}-\\d{2}-\\d{4})";
    return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(content).find()
           ? new SimpleDateFormat("dd-MM-yyyy")
               .parse(Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(content).group(1))
           : Date.from(
               (receiveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(3))
                   .atStartOfDay()
                   .atZone(ZoneId.systemDefault())
                   .toInstant());
       */
    return Date.from(
        (receiveDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(3))
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant());
  }
}
