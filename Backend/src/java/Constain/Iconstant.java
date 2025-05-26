/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Constain;

import java.security.SecureRandom;

/**
 *
 * @author Sonvu
 */
public class Iconstant {

    public static final String GOOGLE_CLIENT_ID = "xxxxx.apps.googleusercontent.com";

    public static final String GOOGLE_CLIENT_SECRET = "xxxxxx";

    public static final String GOOGLE_REDIRECT_URI = "http://localhost:9999/Iter1_Test/login";

    public static final String GOOGLE_GRANT_TYPE = "authorization_code";

    public static final String GOOGLE_LINK_GET_TOKEN = "https://oauth2.googleapis.com/token";

    public static final String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+<>?";

    private static final String ALL_CHARACTERS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL_CHARS;

    // Hàm tạo mật khẩu ngẫu nhiên
    public static String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom(); // Tạo đối tượng SecureRandom để đảm bảo mật khẩu ngẫu nhiên
        StringBuilder password = new StringBuilder(length);

        // Đảm bảo mật khẩu có ít nhất 1 ký tự từ mỗi loại
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Tạo các ký tự còn lại
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // Trộn các ký tự ngẫu nhiên
        return shuffleString(password.toString());
    }

    // Hàm trộn ký tự trong chuỗi
    private static String shuffleString(String str) {
        char[] array = str.toCharArray();
        SecureRandom random = new SecureRandom();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Hoán đổi
            char temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return new String(array);
    }
    
    public static void main(String[] args) {
        System.out.println(Iconstant.generateRandomPassword(8));
    }

}
