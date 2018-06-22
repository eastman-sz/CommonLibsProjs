package com.ble.lib.util;

import java.util.Locale;

/**
 * Created by E on 2017/12/8.
 */
public class CommonBleUtils {

    private final static String mHexStr = "0123456789ABCDEF";

    public static String byteArray2Hex(byte[] data){
        StringBuilder builder = new StringBuilder();
        for (byte byteChar : data) {
            builder.append(String.format("%02X", byteChar));
        }
        return builder.toString();
    }

    /**
     * 十六进制字符串转换成 ASCII字符串
     * @param hexStr String Byte字符串
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr){
        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int iTmp = 0x00;;

        for (int i = 0; i < bytes.length; i++){
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (iTmp & 0xFF);
        }
        return new String(bytes);
    }

    /**
     * 十六进制字符串转换成 ASCII字符串
     * @param hexStr String Byte字符串
     * @return String 对应的字符串
     */
    public static String hexStr2StrUTF8(String hexStr){
        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int iTmp = 0x00;;

        for (int i = 0; i < bytes.length; i++){
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (iTmp & 0xFF);
        }
        String result = null;
        try{
            result = new String(bytes , "UTF-8");
        }catch (Exception e){
            e.printStackTrace();

            result = new String(bytes);
        }
        return result;
    }

    public static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            int value = (high << 4) | low;
            if (value > 127)
                value -= 256;
            raw[i] = (byte) value;
        }
        return raw;
    }

    /**
     * 16进制字符串转化为byte数组
     * @param hexText
     * @return
     */
    public static byte[] hexTextToBytes(String hexText){
        return hexToBytes(hexText.toCharArray());
    }

    /**
     * 十六进制转换为十进制。
     * @param hexStr 十六进制
     * @return 十进制
     */
    public static int hex2Integral(String hexStr){
       return Integer.parseInt(hexStr , 16);
    }

    /**
     * 十六进制转换为十进制。
     * @param hexStr 十六进制
     * @return 十进制
     */
    public static float hex2Float(String hexStr){
        return Float.intBitsToFloat(Integer.valueOf(hexStr, 16));
    }

    /**
     * 十进制转化为十六进制
     * @param integral 十进制
     * @return 十六进制
     */
    public static String integral2Hex(int integral){
        String result = Integer.toHexString(integral);
        int mod = result.length()%2;
        result = 0 == mod ? result : ("0" + result);
        return result;
    }

    /**
     * 十进制转化为二进制
     * @param i 十进制
     * @return 二进制
     */
    public static String toBinaryString(int i){
        return Integer.toBinaryString(i);
    }

    /**
     * 十六进制转换为二进制
     * @param hex 十六进制
     * @return 二进制
     */
    public static String hex2BinaryString(String hex){
        int i = hex2Integral(hex);
        return toBinaryString(i);
    }

    /**
     * 十六进制转换为二进制
     * @param hex 十六进制
     * @return 二进制
     */
    public static String hex2BinaryString(String hex , int bitsNum){
        String binaryString = hex2BinaryString(hex);
        int length = binaryString.length();
        if (length >= bitsNum){
            return binaryString;
        }
        //不足位，前面补0
        int diff = bitsNum - length;
        for (int i = 0; i < diff; i++){
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }


}
