package com.ntt.tl.evaluation.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;
/**
 * @author avenegas
 *
 */
public class CommonUtil {

    /**
     * @return
     */
    public static final String generateUUID() {
    	
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();
        return uuidString;
    }
    
    
    /**
     * @param fieldValue
     * @param regexPattern
     * @return
     */
    public static boolean validateRegexPattern(String fieldValue, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        return pattern.matcher(fieldValue).matches();
    }
    

}
