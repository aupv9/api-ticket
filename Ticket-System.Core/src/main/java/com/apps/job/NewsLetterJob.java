package com.apps.job;

import com.apps.request.EmailNewsletter;
import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.var;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Order(value = 1)
public class NewsLetterJob extends QuartzJobBean {
    @Autowired
    public EmailService emailService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        var emails = (List<EmailNewsletter>)jobDataMap.get("data");
        emails.forEach(emailNewsletter -> {
            Email email = null;
            try {
                email = DefaultEmail.builder()
                        .from(new InternetAddress("sendmailticket@gmail.com", "System Admin"))
                        .to(Lists.newArrayList(new InternetAddress(emailNewsletter.getEmail(), emailNewsletter.getName())))
                        .subject("Promotion")
                        .body("")//Empty body
                        .encoding("UTF-8").build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //Defining the model object for the given Freemarker template
                    final Map<String, Object> modelObject = new HashMap<>();
                    modelObject.put("code", emailNewsletter.getCode());
                    modelObject.put("image",emailNewsletter.getImage());

            try {
                emailService.send(email, "idus_martii.ftl", modelObject);

            } catch (CannotSendEmailException e) {
                e.printStackTrace();
            }
        });
    }
}
