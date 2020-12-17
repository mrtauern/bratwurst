package com.example.bratwurst.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


@Service
public class EncryptionServiceImpl implements EncryptionService
{
    private byte[] key = "C*qwNm\\&w$:vvYbTeRv&Xf+xBN2/)7%f".getBytes();


    @Override
    public String encrypt(String data)
    {
        try
        {
            byte[] iv = new byte[16];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            byte[] bytes = data.getBytes();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivspec);
            byte[] cipherText = cipher.doFinal(bytes);
            //append iv
            byte[] ciphertextAndIv = new byte[cipherText.length + 16];

            System.arraycopy(iv, 0, ciphertextAndIv, 0, 16);
            System.arraycopy(cipherText, 0, ciphertextAndIv, 16, cipherText.length);

            return  new String(Base64.getEncoder().encode(ciphertextAndIv));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return data;
        }
    }

    @Override
    public String decrypt(String data)
    {
        try
        {
            byte[] bytes = Base64.getDecoder().decode(data.getBytes());

            byte[] iv = new byte[16];
            System.arraycopy(bytes, 0, iv, 0, 16);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            byte[] message = new byte[bytes.length - 16];
            System.arraycopy(bytes, 16, message, 0, bytes.length - 16);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivspec);

            return  new String(cipher.doFinal(message));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return data;
        }
    }
}
