package org.springframework.samples.petclinic.util;

public class Converter {
    public static long stringToLong(String number){
        long convertedNumber = 0L;
        try {
            convertedNumber = Long.parseLong(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedNumber;
    }
}
