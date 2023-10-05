package com.capgemini.demandforecast.messaging;

import com.capgemini.demandforecast.entity.Demand;
import com.capgemini.demandforecast.entity.DemandCategory;
import com.capgemini.demandforecast.entity.KnownCustomer;
import com.capgemini.demandforecast.entity.Skills;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class EmailToDemandMapper {
    public boolean getHasAttachment(List<String> attachments) {
        return !attachments.isEmpty();
    }

    public String getStatus(Date receiveDate) throws ParseException {
        return receiveDate.compareTo(getDeadline(receiveDate)) <= 0 ? "Open" : "Closed";
    }

    public String getJobCategory(String content) {
        for (DemandCategory demandCategory : DemandCategory.values()) {
            if (Pattern.compile(Pattern.quote(String.valueOf(demandCategory)), Pattern.CASE_INSENSITIVE)
                    .matcher(content)
                    .find()) {
                return demandCategory.name();
            }
        }
        return "OTHERS";
    }

    public String getCustomer(String content, Demand demand) {
        for (KnownCustomer knownCustomer : KnownCustomer.values()) {
            if (Pattern.compile(Pattern.quote(knownCustomer.name()), Pattern.CASE_INSENSITIVE)
                    .matcher(content)
                    .find()) {
                return knownCustomer.name();
            }
            if (knownCustomer.isPSU) {
                demand.setPSU(true);
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

    public Date getDeadline(Date receiveDate) throws ParseException {
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
