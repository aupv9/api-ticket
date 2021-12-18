package com.apps.job;

import com.apps.config.kafka.Message;
import com.apps.config.properties.SendGridProperties;


import com.apps.contants.Utilities;
import com.apps.service.OrdersService;
import com.apps.service.SeatService;
import com.apps.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import lombok.var;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;


@Component
@Slf4j
@RequiredArgsConstructor
public class EmailJob extends QuartzJobBean {


    private final OrdersService ordersService;

    @Async
    @Scheduled(cron = "*/5 * * * *")
    public void reportCurrentTime() throws ExecutionException, InterruptedException {
        String currentTime = Utilities.getCurrentTime();
        var listOrderExpire = this.ordersService.findAllOrderExpiredReserved(currentTime);
        for (Integer order : listOrderExpire){
            var listOrdersDetail = this.ordersService.findOrderDetailById(order);
            if(listOrdersDetail.size() > 0){
                for (Integer orderDetail: listOrdersDetail){
                    int deleted = this.ordersService.deleteOrderDetail(orderDetail);
                }
            }
            var listOrdersSeat = this.ordersService.findOrderSeatById(order);
            if(listOrdersSeat.size() > 0){
                for (Integer orderSeat: listOrdersSeat){
                    int deleted = this.ordersService.deleteOrderSeat(orderSeat);
                }
            }
            int deleted = this.ordersService.delete(order);
        }
            this.ordersService.sendDataToClient();
//            this.seatService.sendDataToClient();

    }


    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStatusShowTimes() {


    }

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private SendGridProperties sendGridProperties;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");

        sendMail(mailProperties.getUsername(), recipientEmail, subject, body);
    }

    private void sendMail(String fromEmail, String toEmail, String subject, String body) {
        try {
            log.info("Sending Email to {}", toEmail);
            log.info("Sending from Email {}", fromEmail);
            log.info("Email to {}", toEmail);
            log.info("Password Email to {}", mailProperties.getPassword());

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            messageHelper.setFrom(fromEmail);
            messageHelper.setTo(toEmail);

            mailSender.send(message);
        } catch (MessagingException ex) {
            log.error("Failed to send email to {}", toEmail);
        }
    }


}
