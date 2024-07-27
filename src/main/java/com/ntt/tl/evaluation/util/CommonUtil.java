package com.ntt.tl.evaluation.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
/**
 * @author avenegas
 *
 */
public class CommonUtil {
	
	
	public static final Map<String, Object> parceListToMap(List<String> items ) {
		Map<String, Object> propertyUser = new HashMap<>();
		items.forEach(item -> propertyUser.put("typeUser", item));
		
		return propertyUser;
	}

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
