package com.capgemini.demandforecast.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@ToString
@Document(collection = "emails")
public class Email implements Serializable {

    @Id
    private String id;
    private LocalDateTime receiveDate;
    private LocalDateTime deadline;
    private String email;
    private String subject;
    private boolean hasAttachment;
    private String content;
    private ArrayList<String> skills;
    private String customer;
    private boolean PSU;

    public Email (String email, String subject, String content) {
        this.email = email;
        this.subject = subject;
        this.content = content;

        this.deadline = getDeadline(content);
        this.skills = getSkills(content);
        this.customer = getCustomer(content);
        this.PSU = isPSU(content);

    }

    public String getCustomer(String content) {
        for (KnownCustomer knownCustomer : KnownCustomer.values()) {
            if (knownCustomer.name().equalsIgnoreCase(content)) {
                return knownCustomer.name();
            }
        }
        return "OTHERS";
    }

    public ArrayList<String> getSkills(String content) {
        //TO-DO
        return null;
    }

    public LocalDateTime getDeadline(String content) {
        //TO-DO
        return null;
    }

    private boolean isPSU(String content) {
        return false;
    }

}
