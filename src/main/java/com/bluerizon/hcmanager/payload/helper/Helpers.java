// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.payload.helper;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Helpers
{
    public static String path_file = "/home/upload/hc-manager/facture/";
    public static String currentDate() {
        final Date date = new Date();
        final SimpleDateFormat datej = new SimpleDateFormat("yyyy-MM-dd");
        final String datejour = datej.format(date);
        final SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
        final String heurejour = heure.format(date);
        return datejour+" "+heurejour;
    }
    public static String currentDateSimple() {
        final Date date = new Date();
        final SimpleDateFormat datej = new SimpleDateFormat("yyyy-MM-dd");
        final String datejour = datej.format(date);
        final SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
        final String heurejour = heure.format(date);
        return datejour;
    }

    public static Date getDateFromString(String s) {
        final Date datej;
        try {
            datej = new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return datej;
    }
    
    public static String generatRef(final String classe, final Long lastId) {
        final Date date = new Date();
        final SimpleDateFormat datej = new SimpleDateFormat("yyyyMMdd");
        final String datejour = datej.format(date);
        final SimpleDateFormat heure = new SimpleDateFormat("HHmmss");
        final String heurejour = heure.format(date);
        return classe.substring(0, 1).toUpperCase()+""+lastId+""+datejour+""+heurejour;
    }

    public static String generatRef(final String classe) {
        return classe.substring(0, 2).toUpperCase()+""+UUID.randomUUID().toString();
    }
    
    public static String generat() {
        final Date date = new Date();
        final SimpleDateFormat datej = new SimpleDateFormat("yyyyMMdd");
        final String datejour = datej.format(date);
        final SimpleDateFormat heure = new SimpleDateFormat("HHmmss");
        final String heurejour = heure.format(date);
        final Calendar calendar = Calendar.getInstance();
        return datejour+""+heurejour+""+calendar.getTimeInMillis();
    }

    public static String convertDate(Date dateInit){
        DateFormat date = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("FR","fr"));
        SimpleDateFormat datej = new SimpleDateFormat("dd/MM/yyyy");
        String datejour = datej.format(dateInit);

        return datejour;
    }

    public static String convertHeure(Date dateInit){
        DateFormat date = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("FR","fr"));
        SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
        String heurejour = heure.format(dateInit);
        return heurejour;
    }

    public static String year(){
//        DateFormat date = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("FR","fr"));
        Date date = new Date();
        SimpleDateFormat datej = new SimpleDateFormat("yyyy");
        String datejour = datej.format(date);
        return datejour;
    }

    public static String convertFr(Date date1){
//        DateFormat date = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, new Locale("FR","fr"));/
        SimpleDateFormat datej = new SimpleDateFormat("MMMM yyyy");
//        datej.format(date1);
        return datej.format(date1);
    }

    public static Long dayBetween(Date firstDate, Date secondDate){
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }

    public static Date tomorowDate(){
        Date td = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(td);
        c.add(Calendar.DATE, 1);

        return c.getTime();
    }
}
