package com.utils.lib.ss.common;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
/**
 * DES加密算法加密请求参数。
 * @author E
 */
public class DES {
	
    private static final String KEY = "7fdc4b8e";
    
    /**
     * 字符串通过DES加密。
     * @param encryptString 要加密的字符串
     * @return 经过DES加密后的字符串
     * @throws Exception 加密过程中抛出的异常
     */
    public static String encryptDES(String encryptString)
            throws Exception {
        int mode = Cipher.ENCRYPT_MODE;
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        byte[] keyData = KEY.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyData);
        Key key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(mode, key);
        byte[] result = cipher.doFinal(encryptString.getBytes());
        return Base64.encode(result);

    }

    /**
     * 解密经过DES加密的字符串。
     * @param decryptString 要解密的字符串。
     * @return 解密后的字符串
     * @throws Exception 解密过程中抛出的异常
     */
    public static String decryptDES(String decryptString)
            throws Exception {
        byte[] encodeByte = Base64.decode(decryptString);

        int mode = Cipher.DECRYPT_MODE;
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        byte[] keyData = KEY.getBytes();
        DESKeySpec keySpec = new DESKeySpec(keyData);
        Key key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(mode, key);
        byte[] result = cipher.doFinal(encodeByte);
        return new String(result, "UTF-8");
    }
}
