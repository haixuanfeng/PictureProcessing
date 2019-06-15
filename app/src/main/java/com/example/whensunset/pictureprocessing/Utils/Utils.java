package com.example.whensunset.pictureprocessing.Utils;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    private static String outputMediaFile = "/PictureProcessing";

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = c.getTime();
        String dateStringParse = sdf.format(date);

        return dateStringParse;
    }

    public static void toastMessage(Context context, String MessageType) {
        Toast.makeText(context, MessageType, Toast.LENGTH_SHORT).show();
    }


    public static String getOutputMediaFile() {
        return outputMediaFile;
    }
}
