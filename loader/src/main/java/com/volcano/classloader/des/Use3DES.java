package com.volcano.classloader.des;

/**
 * @Author bjvolcano
 * @Date 2021/5/7 1:53 下午
 * @Version 1.0
 */

import lombok.SneakyThrows;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 使用3DES 算法对目标数据执行加解密
 *
 * @author ljheee
 */

public class Use3DES {

    public static byte[] key = "01234567899876543210rwop".getBytes();
    
    private static final String ALGORITHM = "DESede";// 定义加密算法

    /**
     * @param key 192位的加密Key
     * @param src 明文数据
     * @return
     */

    public static byte[] encrypt(byte[] key, byte[] src) {
        byte[] value = null;
        SecretKey deskey = new SecretKeySpec(key, ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            value = cipher.doFinal(src);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * 解密
     *
     * @param key 192位的加密Key
     * @param src 待解密数据
     * @return
     */
    @SneakyThrows
    public static byte[] decrypt(byte[] key, byte[] src) {
        byte[] value = null;
        SecretKey deskey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        value = cipher.doFinal(src);
        return value;
    }
}
