package com.oriseq.controller.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoderUtils {

    public static String encodePassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        try {
            boolean checkpw = BCrypt.checkpw(rawPassword, encodedPassword);
            return checkpw;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}