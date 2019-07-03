package com.szyz.rock.util;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Encodes a string using MD5 hashing
 *
 */
public class MD5
{
    private static final String HMAC_SHA1 = "HmacSHA1";
    /**
     * Encodes a string
     *
     * @param str String to encode
     * @return Encoded String
     * @throws NoSuchAlgorithmException
     */
    public static String crypt(String str)
    {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        StringBuffer hexString = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try {
                md.update(str.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte[] hash = md.digest();

            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                }
                else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hexString.toString().toUpperCase();
    }

    public static byte[] getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        return mac.doFinal(data);
    }

}
