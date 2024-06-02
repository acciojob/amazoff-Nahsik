package com.driver;

import io.swagger.models.auth.In;

public class Utils {

    public static Integer convertTimeToMinutes(String time){
        String[] timeArray = time.split(":");
        return Integer.parseInt(timeArray[0])*60 + Integer.parseInt(timeArray[1]);
    }
}
