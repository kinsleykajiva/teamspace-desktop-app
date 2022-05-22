package team.space.utils;

import javafx.application.Platform;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class XUtils {

    private static Matcher match;
    private final static Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-.]+([A-Za-z0-9-_.]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9-]+)*(.[A-Za-z]{2,})$");
    private final static  Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9_.]{3,20}");
    private final static  Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{3,10}");

    private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String currentTimeStamp(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return dtf.format(LocalDateTime.now());
    }

    public static Long convertDateStringToLong(String date){
        if(date.isEmpty()){
            return null;
        }
        try {
            // obtain date and time from initial string
            Date dateobj = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT).parse(date);
            // convert date to long
            return dateobj.getTime();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static Date convertStringToDate(String date){
        if(date.isEmpty()){
            return null;
        }
        try {
            // obtain date and time from initial string
            Date dateobj = new SimpleDateFormat(DATE_FORMAT, Locale.ROOT).parse(date);
            // convert date to long
            return dateobj;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Run this Runnable in the JavaFX Application Thread. This method can be
     * called whether or not the current thread is the JavaFX Application
     * Thread.
     *
     * @param runnable The code to be executed in the JavaFX Application Thread.
     */
    public static void invoke(Runnable runnable) {
        if (isNull(runnable)) {
            return;
        }

        try {
            if (Platform.isFxApplicationThread()) {
                runnable.run();
            }
            else {
                Platform.runLater(runnable);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * check that email address not empty or correct
     * @param email
     * @return - true if email valid <br>
     *          - false if not valid
     */
    public static boolean checkEmail(String email){
        match = EMAIL_PATTERN.matcher(email);
        return !email.trim().equals("") && match.matches();
    }

    /**
     * check that username not empty or correct
     * @param username
     * @return - true if username valid <br>
     *          - false if not valid
     */
    public static boolean checkUserName(String username){
        match = USERNAME_PATTERN.matcher(username);
        return   checkStringEmpty(username) && match.matches();
    }


    /**
     * check that name not empty or correct
     * @param name
     * @return - true if name valid <br>
     *          - false if not valid
     */
    public static boolean checkName(String name){
        match = NAME_PATTERN.matcher(name);
        return   checkStringEmpty(name) && match.matches();
    }


    /**
     * check that string not empty or null
     * @param str
     * @return  - true if string is correct <br>
     *           - false if string incorrect
     */
    public static boolean checkStringEmpty(String str ){
        return !(str == null || str.trim().length() == 0 || "".equals(str.trim())) ;
    }


    /**
     * check string length with in min & max value length allowed
     * @param str string to be checked
     * @param min length accepted for string
     * @param max length accepted for string
     * @return - true if string with in range <br>
     *          - false if string not with in range
     */
    public static boolean checkStringLength(String str , int min , int max){
        return !(str.length() > max || str.length() < min );
    }
}
