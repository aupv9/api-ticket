package com.apps.contants;


import com.apps.domain.entity.Offer;
import com.apps.filter.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;


public class Utilities {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    public static String getCurrentTime(){
        return LocalDate.now().format(dateFormatter)+" "+ LocalTime.now().format(timeFormatter);
    }

    public static String convertIsoToDate(String date){
//        Instant instant = Instant.parse(date);
//        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
//        ZonedDateTime zdt = instant.atZone(zoneId);
        return LocalDate.parse(date).format(dateFormatter);
//        return timestamp.toLocalDateTime().format(dateFormatter);
    }

    public static LocalDate convertStringToLocalDate(String date){
        return LocalDate.parse(date,dateTimeFormatter);
    }



    public static String getTimeExpirePayment5m(){
        return LocalDate.now().format(dateFormatter)+" "+ LocalTime.now().plusMinutes(5).format(timeFormatter);
    }

    public static double getDiscountByCode(String code, double amount, Offer offer) {
        return offer.getType().equals(OfferTypeEnum.Flat.name()) ?
                offer.getDiscountAmount() :
                (amount / 100) * offer.getPercentage();
    }
    public static UserDetails getUserDetails (){
        var authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return  (UserDetails)authentication.getPrincipal();
    }

    public static String subDate(int countDate){
        return LocalDate.now().minusDays(countDate).format(dateFormatter);
    }

}
