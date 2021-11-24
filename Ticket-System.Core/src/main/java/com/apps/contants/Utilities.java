package com.apps.contants;


import com.apps.domain.entity.Offer;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;



public class Utilities {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;


    public static String getCurrentTime(){
        return LocalDate.now().format(dateFormatter)+" "+ LocalTime.now().format(timeFormatter);
    }

    public static String convertIsoToDate(String date){
        Instant instant = Instant.parse(date);
//        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
//        ZonedDateTime zdt = instant.atZone(zoneId);
        Timestamp  timestamp = Timestamp.from(instant);
        return timestamp.toLocalDateTime().format(dateFormatter);
    }

    public static String getTimeExpirePayment5m(){
        return LocalDate.now().format(dateFormatter)+" "+ LocalTime.now().plusMinutes(5).format(timeFormatter);
    }

    public static double getDiscountByCode(String code, double amount, Offer offer) {
        return offer.getType().equals(OfferTypeEnum.Flat.name()) ?
                offer.getDiscountAmount() :
                (amount / 100) * offer.getPercentage();
    }
}
