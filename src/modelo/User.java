package modelo;


import notifications.EmailNotifications;
import notifications.TelegramNotifications;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

public class User implements Serializable, Cloneable {
    private String id;
    private String email;
    private String password;
    private String userName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String token;
    private boolean firstLogin;
    private boolean isLogged;
    private ArrayList<Mark> markList;
    private String lastConnection;
    private static int idNumber = Integer.parseInt(AppManagement.getProperties().getProperty("id_number"));

    public User(String email, String password, String userName, LocalDate birthDate, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        token = tokenGenerator();
        firstLogin = true;
        isLogged = false;
        markList = new ArrayList<>();
    }

    public User(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userName = user.getUserName();
        this.birthDate = user.getBirthDate();
        this.phoneNumber = user.getPhoneNumber();
        this.token = tokenGenerator();
        this.firstLogin = true;
        this.isLogged = false;
        this.markList = user.getMarkList();
    }

    public User() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getToken() {
        return token;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public String getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(String lastConnection) {
        this.lastConnection = lastConnection;
    }

    public void changeToLogged() {
        isLogged = true;
    }

    public void changeToLogOut() {
        isLogged = false;
    }

    public ArrayList<Mark> getMarkList() {
        markList.sort(Mark::compareTo);
        return markList;
    }

    private String tokenGenerator() {
        String token = "";
        Random rdn = new Random();
        while (token.length() < 8) {
            switch (rdn.nextInt(2) + 1) {
                case 1:
                    token += ((char) (rdn.nextInt(26) + 'A'));
                    break;
                case 2:
                    token += (rdn.nextInt(10));
                    break;
            }

        }
        return token;
    }

    public boolean checkToken(String token) {
        if (this.token.equals(token)) {
            firstLogin = false;
            return true;
        } else return false;
    }

    public void generateNewToken() {
        token = tokenGenerator();
    }


    public boolean checkActualPassword(String password) {
        return (this.password.equals(password));
    }

    public boolean changePassword(String password1, String password2) {
        return password1.equals(password2);
    }

    public boolean addMark(Mark mark) {
        for (Mark m : markList) {
            if (m.getYear() == mark.getYear() && m.getRace().getId().equals(mark.getRace().getId())) {
                return false;
            }
        }
        markList.add(mark);
        return true;
    }

    public static boolean checkEmail(String email) {
        char AT = '@';
        char DOT = '.';
        boolean at = false, dot = false;
        int position = 0, cont = 0;

        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == AT) {
                at = true;
                position = i;
                cont++;
            }
        }
        if (at && cont == 1) {
            for (int i = position; i < email.length(); i++) {
                if (email.charAt(i) == DOT) {
                    dot = true;
                    break;
                }
            }
        }
        return (at & dot);
    }

    public static boolean validateEmail (String email) {
        InternetAddress ia;
        try {
            ia = new InternetAddress(email);
            ia.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }

    }

    public static boolean checkPassword(String password) {
        boolean upperCase = false, lowerCase = false, number = false;
        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isLowerCase(password.charAt(i))) lowerCase = true;
                if (Character.isUpperCase(password.charAt(i))) upperCase = true;
                if (Character.isDigit(password.charAt(i))) number = true;
            }
        }
        return (upperCase && lowerCase && number);

    }

    public static boolean checkBirthDate(String birthDate) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        try{
            dtf.parseLocalDate(birthDate);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
        return Pattern.matches("[1-9][0-9]{8}",phoneNumber);

    }

    public void sendEmailForChangingEmail(String email) throws IOException, MessagingException {
        EmailNotifications emailNotifications = new EmailNotifications(AppManagement.getProperties().getProperty("email_sender"),
                AppManagement.getProperties().getProperty("email_user"),AppManagement.getProperties().getProperty("email_password"),
                AppManagement.getProperties().getProperty("email_host"));
        emailNotifications.sendEmail(email, AppManagement.editEmail(token), "\uD83D\uDC4B Verificación de cambio de email");
    }

    public void sendTelegramForChangingEmail() throws IOException {
        TelegramNotifications telegram = new TelegramNotifications(AppManagement.getProperties().getProperty("telegram_api"),
                AppManagement.getProperties().getProperty("telegram_id"));
        telegram.sendTelegram("Este es su código de verificación para el cambio de correo " + token);
    }

    public void markRegisteredEmail (Mark mark) throws IOException, MessagingException {
        EmailNotifications emailNotifications = new EmailNotifications(AppManagement.getProperties().getProperty("email_sender"),
                AppManagement.getProperties().getProperty("email_user"),AppManagement.getProperties().getProperty("email_password"),
                AppManagement.getProperties().getProperty("email_host"));
        emailNotifications.sendEmail(email, AppManagement.markRegistered(mark, userName), "\uD83C\uDFC3\u200D♂️ Nueva marca registrada");
    }

    public void markRegisteredTelegram (Mark mark) throws IOException {
        TelegramNotifications telegram = new TelegramNotifications(AppManagement.getProperties().getProperty("telegram_api"),
                AppManagement.getProperties().getProperty("telegram_id"));
        telegram.sendTelegram("Su marca en la carrera de " + mark.getRace().getName() + " en el año " + mark.getYear() +
                " ha sido registrada con éxito");

    }

    
    public void generateIdNumber (){
        idNumber++;
        AppManagement.getProperties().setProperty("id_number",Integer.toString(idNumber));
        AppManagement.storeProperties();
        id = Integer.toString(idNumber);
    }

    public int compareTo(User user) {
        return userName.compareToIgnoreCase(user.getUserName());
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        return "\n=========================================" +
                "\nUsuario: " + userName +
                "\nEmail: " + email +
                "\nFecha de nacimiento: " + dtf.print(birthDate) +
                "\nNúmero de teléfono: " + phoneNumber +
                "\nNúmero de marcas registradas: " + markList.size() +
                "\n=======================================";
    }
}
