package com.example.bratwurst.hashfunctions;

import java.security.MessageDigest;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class HashFunctions {

    //Available algorithms: MD2, MD5, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512
    public final String PEBER = "matodefe";

    //HASH CONVERTER
    public String getHash(byte[] inputBytes, String algorithm, byte[] salt) {

        String hashValue = "";

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.reset();
            messageDigest.update(salt);
            messageDigest.update(inputBytes);
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();

        } catch (Exception e) {

        }

        return hashValue;
    }

    //SALT GENERATOR
    public byte[] createSalt() {
//		byte[] bytes = new byte[3];
//
//		SecureRandom random = new SecureRandom();
//		random.nextBytes(bytes);

        String generatedString = randomAlphanumericString();

        return generatedString.getBytes();
    }

    //ALPHANUMERIC GENERATOR
    public static String randomAlphanumericString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

}



