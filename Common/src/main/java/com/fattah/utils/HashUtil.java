package com.fattah.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class HashUtil {

        public static String sha1Hash(String input) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
                return bytesToHex(hashBytes);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("SHA-1 algorithm not found", e);
            }
        }

        private static String bytesToHex(byte[] bytes) {
            StringBuilder hexString = new StringBuilder(2 * bytes.length);
            for (byte b : bytes) {
                // Convert each byte to a 2-digit hex string
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        }

        // مثال استفاده
        public static void main(String[] args) {
            String input = "1433575575";
            Date now= new Date();
            System.out.println(now.getTime());
            String hashed = sha1Hash(input);
            System.out.println("SHA-1 Hash: " + hashed);
        }
    }


