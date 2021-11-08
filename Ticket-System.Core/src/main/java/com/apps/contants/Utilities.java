package com.apps.contants;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



public class Utilities {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;


    public static String getCurrentTime(){
        return LocalDate.now().format(dateFormatter)+" "+ LocalTime.now().format(timeFormatter);
    }
}
