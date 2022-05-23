// 
// Decompiled by Procyon v0.5.36
// 

package com.bluerizon.hcmanager.payload.helper;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Helpers
{
    public static String currentDate() {
        final Date date = new Date();
        final SimpleDateFormat datej = new SimpleDateFormat("yyyy-MM-dd");
        final String datejour = datej.format(date);
        final SimpleDateFormat heure = new SimpleDateFormat("HH:mm:ss");
        final String heurejour = heure.format(date);
        return datejour+" "+heurejour;
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
    
    public static String typeFile() throws IOException {
        final File file = new File("3.flv");
        final FileNameMap fileNameMap = URLConnection.getFileNameMap();
        final String mimeType = fileNameMap.getContentTypeFor(file.getName());
        return mimeType;
    }
    
    public static String sizeFile() {
        final double size_bytes = 1.999901221E9;
        final double size_kb = size_bytes / 1024.0;
        final double size_mb = size_kb / 1024.0;
        final double size_gb = size_mb / 1024.0;
        String cnt_size;
        if (size_gb > 0.0) {
            cnt_size = size_gb+"";
        }
        else if (size_mb > 0.0) {
            cnt_size = size_mb+"";
        }
        else {
            cnt_size =  size_kb+"";
        }
        return cnt_size;
    }
}
