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
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class Utilities {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;

    public static String getCurrentTime(){
        return LocalDate.now().format(dateFormatter)+" "+ LocalTime.now().format(timeFormatter);
    }

    public static String convertIsoToDateTime(String date){
        return Timestamp.valueOf(date).toLocalDateTime().format(dateTimeFormatter);
    }
    public static String convertIsoToDate(String date){
        return LocalDate.parse(date).format(dateFormatter);
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

    public static String subDate(long countDate){
        return LocalDate.now().minusDays(countDate).format(dateFormatter);
    }
    public static String addDate(long countDate){
        return LocalDate.now().plusDays(countDate).format(dateFormatter);
    }

    public static String addDateParam(long countDate,String date ){
        return LocalDate.parse(date).plusDays(countDate).format(dateFormatter);
    }

    public static Date startOfWeek() {
        return startOfWeek(new Date());
    }

    public static Date startOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return cal.getTime();
    }

    public static String startOfWeek(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(startOfWeek(date));
    }

    public static String startOfWeek(String format) {
        return startOfWeek(new Date(), format);
    }
    public static String currentWeekEndDate() {
        Calendar mth = Calendar.getInstance();
        mth.set(mth.get(Calendar.YEAR), mth.get(Calendar.MONTH),
                mth.get(Calendar.DAY_OF_MONTH) - (mth.get(Calendar.DAY_OF_WEEK) + 6) % 7 + 7);
        return (new SimpleDateFormat("yyyy-MM-dd").format(mth.getTime()));
    }

    public static String currentWeekEndDate(Date date) {
        Calendar mth = Calendar.getInstance();
        mth.setTime(date);
        mth.set(mth.get(Calendar.YEAR), mth.get(Calendar.MONTH),
                mth.get(Calendar.DAY_OF_MONTH) - (mth.get(Calendar.DAY_OF_WEEK) + 6) % 7 + 7);
        return (new SimpleDateFormat("yyyy-MM-dd").format(mth.getTime()));
    }
}
