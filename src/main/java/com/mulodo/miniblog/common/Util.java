/**
 * 
 */
package com.mulodo.miniblog.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author TriLe
 *
 */
public class Util {

    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * Hash input string
     * 
     * @param value
     *            string which need to hash
     * @return hash value of input
     */
    public static String hashSHA256(String value) {

	// Check null and empty
	if (StringUtils.isEmpty(value)) {
	    return null;
	}

	MessageDigest md;
	try {
	    md = MessageDigest.getInstance(Contants.HASH_ALGORITHM);
	} catch (NoSuchAlgorithmException e) {
	    // Log exception
	    logger.error("HASH_ALGORITHM=" + Contants.HASH_ALGORITHM, e);
	    return null;
	}

	md.update(value.getBytes());

	byte byteData[] = md.digest();

	// convert the byte to hex format method 1
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < byteData.length; i++) {
	    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	}

	System.out.println("Hex format : " + sb.toString());

	// convert the byte to hex format method 2
	StringBuffer hexString = new StringBuffer();
	for (int i = 0; i < byteData.length; i++) {
	    String hex = Integer.toHexString(0xff & byteData[i]);
	    if (hex.length() == 1)
		hexString.append('0');
	    hexString.append(hex);
	}

	// Log result
	String result = hexString.toString();
	logger.debug("Value: [{}]; Hash: [{}]", value, result);

	return result;
    }

    /**
     * Create new token by hash user ID and current date time (to unsure token
     * is unique)
     * 
     * @param userID
     *            ID of user
     * @return new token if userID > 0 and NULL otherwise
     */
    public static String createToken(int userID) {
	// Validate userID
	if (0 > userID) {
	    return null;
	}
	// Get current ms
	long currentMs = System.currentTimeMillis();
	// Append username and current date time and then hash to create new
	// token
	return hashSHA256(userID + "@" + currentMs);
    }
}
