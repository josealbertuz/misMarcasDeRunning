package modelo;

import com.google.gson.Gson;
import joseutils.Utils;
import notifications.EmailNotifications;
import notifications.TelegramNotifications;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import java.io.*;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class AppManagement {
    private ArrayList<User> userList;
    private ArrayList<Race> raceList;
    private HashMap<String, String> userNamePassword;
    private HashMap<String, String> emailPassword;
    private static Properties properties;

    public AppManagement() {
        loadProperties();
        createDataDirectories();
        retrieveUserList();
        retrieveRaceList();
        userNamePassword = retrieveHashMap("userNamePassword.dat");
        emailPassword = retrieveHashMap("emailPassword.dat");
    }


    public ArrayList<User> getUserList() {
        return userList;
    }

    public ArrayList<Race> getRaceList() {
        return raceList;
    }

    public HashMap<String, String> getUserNamePassword() {
        return userNamePassword;
    }

    public HashMap<String, String> getEmailPassword() {
        return emailPassword;
    }

    public User getUser(int position) {
        return userList.get(position);
    }

    public Race getRace(int position) {
        return raceList.get(position);
    }

    public Mark getMark(String userName, int position) {
        for (User user : userList) {
            if (user.getEmail().equals(userName)) {
                return user.getMarkList().get(position);
            }
        }
        return null;
    }

    public int getNumberUsersRegistred() {
        return userList.size();
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        AppManagement.properties = properties;
    }

    public int userFinder(String userName) {
        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                return userList.indexOf(u);
            }
        }
        return -1;
    }

    public int emailFinder(String email) {
        for (User u : userList) {
            if (u.getEmail().equals(email)) {
                return userList.indexOf(u);
            }
        }
        return -1;
    }

    public void addUser(User user) {
        user.generateIdNumber();
        userList.add(user);
        userNamePassword.put(user.getUserName(), user.getPassword());
        emailPassword.put(user.getEmail(), user.getPassword());
        properties.setProperty(user.getId(), "Primer inicio de sesión");
        storeProperties();
        saveUser(user);
        saveUserList(userList);
        saveHashMap(userNamePassword, "userNamePassword.dat");
        saveHashMap(emailPassword, "emailPassword.dat");
    }

    public boolean checkRepeatedUser(String userName, String email) {
        return (userNamePassword.containsKey(userName) || emailPassword.containsKey(email));
    }

    /*
    0 updates emailPassword whereas any other number updates userNamePassword
     */

    public void updateHashMapKey(User user, String newKey, int option) {
        if (option == 0) {
            emailPassword.remove(user.getEmail());
            emailPassword.put(newKey, user.getPassword());
        } else {
            userNamePassword.remove(user.getUserName());
            userNamePassword.put(newKey, user.getPassword());
        }
    }

    public void removeUser(User user) {
        user.changeToLogOut();
        userNamePassword.remove(user.getUserName());
        emailPassword.remove(user.getEmail());
        userList.remove(user);
        saveUserList(userList);
        saveHashMap(userNamePassword, "userNamePassword.dat");
        saveHashMap(emailPassword, "emailPassword.dat");

        File currentUser = new File(properties.getProperty("files_location") + "\\users\\u" + user.getId() + ".dat");
        File deletedUser = new File(properties.getProperty("files_location") + "\\deleted_users\\u" + user.getId() + ".dat");

        try {
            Files.copy(currentUser.toPath(), deletedUser.toPath());
        } catch (IOException e) {

        }
        currentUser.delete();
    }

    public boolean addMark(String userName, Mark mark) {

        int userPosition = userFinder(userName);

        return userList.get(userPosition).addMark(mark);
    }

    public void addRace(Race race) {
        raceList.add(race);
    }

    public ArrayList<User> userFinderByUserName(String userName) {

        ArrayList<User> listOfUsersWithSimilarNames = new ArrayList<>();
        for (User u : userList) {
            if (u.getUserName().toUpperCase().contains(userName.toUpperCase())) {
                listOfUsersWithSimilarNames.add(u);
            }
        }
        return listOfUsersWithSimilarNames;
    }

    public ArrayList<Race> raceFinderByName(String raceName) {

        ArrayList<Race> listOfRacesWithSimilarName = new ArrayList<>();

        String raceName2 = Utils.replaceAccents(raceName).toUpperCase();

        for (Race r : raceList) {
            String raceName1 = Utils.replaceAccents(r.getName()).toUpperCase();
            if (raceName1.contains(raceName2)) {
                listOfRacesWithSimilarName.add(r);
            }
        }
        return listOfRacesWithSimilarName;
    }

    public ArrayList<Race> raceFinderByCountry(String raceCountry) {

        ArrayList<Race> listOfRacesWithSimilarCountry = new ArrayList<>();
        String country2 = Utils.replaceAccents(raceCountry).toUpperCase();

        for (Race r : raceList) {
            String country1 = Utils.replaceAccents(r.getCountry()).toUpperCase();
            if (country1.contains(country2)) {
                listOfRacesWithSimilarCountry.add(r);
            }
        }
        return listOfRacesWithSimilarCountry;
    }

    public int raceFinderById(String id) {

        for (Race r : raceList) {
            if (r.getId().equals(id)) {
                return raceList.indexOf(r);
            }
        }
        return -1;


    }

    public ArrayList<Mark> markFinderByUserName(String userName) {

        ArrayList<Mark> listOfMarksByUserName = new ArrayList<>();
        int postionUser = userFinder(userName);

        if (postionUser != -1) listOfMarksByUserName = userList.get(postionUser).getMarkList();

        listOfMarksByUserName.sort(Mark::compareTo);
        return listOfMarksByUserName;
    }

    public ArrayList<Mark> markFinderByUserNamePublic(String userName) {

        ArrayList<Mark> listOfMarksByUserNamePublic = new ArrayList<>();

        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                for (Mark m : u.getMarkList()) {
                    if (m.isPublished()) {
                        listOfMarksByUserNamePublic.add(m);
                    }
                }
            }
        }
        listOfMarksByUserNamePublic.sort(Mark::compareTo);
        return listOfMarksByUserNamePublic;
    }

    public ArrayList<Mark> markFinderbyYearUser(String userName, int year) {

        ArrayList<Mark> listOfMarksByYearUser = new ArrayList<>();

        for (User u : userList) {
            if (u.getUserName().equals(userName)) {
                for (Mark m : u.getMarkList()) {
                    if (m.getYear() == year) {
                        listOfMarksByYearUser.add(m);
                    }
                }
            }
        }
        listOfMarksByYearUser.sort(Mark::compareTo);
        return listOfMarksByYearUser;
    }

    public ArrayList<Mark> markFinderByUserRaceId(String userName, String id) {

        ArrayList<Mark> listOfMarksByUserRaceId = new ArrayList<>();
        int postionUser = userFinder(userName);

        for (Mark m : userList.get(postionUser).getMarkList()) {
            if (m.getRace().getId().equals(id)) {
                listOfMarksByUserRaceId.add(m);
            }
        }

        listOfMarksByUserRaceId.sort(Mark::compareTo);
        return listOfMarksByUserRaceId;
    }

    public ArrayList<MarkDataclass> markFinderByRaceIdGivingANumber(String id, int numberOfMarks) {

        ArrayList<MarkDataclass> listOfMarksbyRaceIdGivingANumber = new ArrayList<>();
        int cont = 0;

        for (User u : userList) {
            for (Mark m : u.getMarkList()) {
                if (m.getRace().getId().equals(id)) {
                    listOfMarksbyRaceIdGivingANumber.add(new MarkDataclass(u.getUserName(), m.getMark(), m.getYear(), m.isPublished()));
                    cont++;
                    if (cont == numberOfMarks) break;
                }
            }
            if (cont == numberOfMarks) break;
        }
        listOfMarksbyRaceIdGivingANumber.sort(MarkDataclass::compareTo);
        return listOfMarksbyRaceIdGivingANumber;
    }

    public ArrayList<MarkDataclass> markFinderByRaceId(String id) {

        ArrayList<MarkDataclass> listOfMarksbyRaceId = new ArrayList<>();

        for (User u : userList) {
            for (Mark m : u.getMarkList()) {
                if (m.getRace().getId().equals(id)) {
                    listOfMarksbyRaceId.add(new MarkDataclass(u.getUserName(), m.getMark(), m.getYear(), m.isPublished()));
                }
            }
        }
        listOfMarksbyRaceId.sort(MarkDataclass::compareTo);
        return listOfMarksbyRaceId;
    }

    public ArrayList<MarkDataclass> markFinderByRaceIdYear(String id, int year) {

        ArrayList<MarkDataclass> listOfMarksbyRaceIdYear = new ArrayList<>();

        for (User u : userList) {
            for (Mark m : u.getMarkList()) {
                if (m.getRace().getId().equals(id) && m.getYear() == year) {
                    listOfMarksbyRaceIdYear.add(new MarkDataclass(u.getUserName(), m.getMark(), m.getYear(), m.isPublished()));
                }
            }
        }
        listOfMarksbyRaceIdYear.sort(MarkDataclass::compareTo);
        return listOfMarksbyRaceIdYear;

    }

    public int checkLogin(String userNameOrEmail, String password) {

        int userPosition;

        if (userNamePassword.containsKey(userNameOrEmail)) {
            userPosition = userNamePassword.get(userNameOrEmail).equals(password) ? userFinder(userNameOrEmail) : -1;
        } else if (emailPassword.containsKey(userNameOrEmail)) {
            userPosition = emailPassword.get(userNameOrEmail).equals(password) ? emailFinder(userNameOrEmail) : -1;
        } else userPosition = -1;

        return userPosition;

    }

    public static String welcomeEmail(User user) throws IOException {
        String welcomeEmail = Utils.fileReader(properties.getProperty("html_resources") + "\\welcome_plus_token_MisMarcasdeRunning.html");
        welcomeEmail = welcomeEmail.replaceAll("name", user.getUserName());
        welcomeEmail = welcomeEmail.replaceAll("code", user.getToken());
        return welcomeEmail;
    }

    public static String markRegistered(Mark mark, String userName) throws IOException {
        String markRegistered = Utils.fileReader(properties.getProperty("html_resources") + "\\markRegistered.html");
        markRegistered = markRegistered.replaceAll("race", mark.getRace().getName());
        markRegistered = markRegistered.replaceAll("year", Integer.toString(mark.getYear()));
        markRegistered = markRegistered.replaceAll("name", userName);
        markRegistered = markRegistered.replaceAll("_id_", mark.getId());

        return markRegistered;
    }

    public static String editEmail(String token) throws IOException {
        String editEmail = Utils.fileReader(properties.getProperty("html_resources") + "\\editEmail.html");
        editEmail = editEmail.replaceAll("code", token);
        return editEmail;
    }

    public void sendEmailAddUser(User user) throws IOException, MessagingException {
        EmailNotifications emailNotifications = new EmailNotifications(properties.getProperty("email_sender"),
                properties.getProperty("email_user"),properties.getProperty("email_password"), properties.getProperty("email_host"));
        emailNotifications.sendEmail(user.getEmail(), welcomeEmail(user), "\uD83D\uDC4B Bienvenido a MisMarcasdeRunning");

    }

    public void sendTelegramAddUser(User user) throws IOException {
        TelegramNotifications telegram = new TelegramNotifications(properties.getProperty("telegram_api"), properties.getProperty("telegram_id"));
        telegram.sendTelegram("Bienvenido " + user.getUserName() + ", este es su código de verificación " + user.getToken());
    }

    public void generateJSONEmail(User user) throws MessagingException {
        EmailNotifications emailNotifications = new EmailNotifications(properties.getProperty("email_sender"),
                properties.getProperty("email_user"),properties.getProperty("email_password"), properties.getProperty("email_host"));
        Gson gson = new Gson();

        emailNotifications.sendEmail(user.getEmail(), gson.toJson(user), "Le enviamos su perfil de usuario en JSON");

    }

    public void generateJSONdirectory(User user) throws IOException {
        Gson gson = new Gson();

        PrintWriter pw = new PrintWriter(new FileWriter(user.getUserName() + ".json"));
        pw.println(gson.toJson(user));
        pw.close();
    }

    /*
    Here I'm gonna make all the serialization process
     */

    /*
    Create data directories if doesn't exist
     */

    public void createDataDirectories() {
        String directories = "data/data_structures/deleted_users/races/users";
        String[] listOfFiles = directories.split("/");
        if (!new File(listOfFiles[0]).exists()) {
            try {
                Files.createDirectory(Paths.get(listOfFiles[0]));
                for (int i = 1; i < listOfFiles.length; i++) {
                    Files.createDirectory(Paths.get("data/" + listOfFiles[i]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            properties.setProperty("files_location","data");
            storeProperties();
        }
    }

    /*
    Properties methods
     */
    public void loadProperties() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream("config/appProperties.config"));
        } catch (IOException e) {

        }
    }

    public static void storeProperties() {
        try {
            properties.store(new FileOutputStream("config/appProperties.config"), null);
        } catch (FileNotFoundException e) {

        } catch (UnknownHostException e) {

        } catch (IOException e) {

        }
    }

    public void setUserProperty(String userId) {

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
        properties.setProperty(userId, dtf.print(new LocalDateTime()));
        storeProperties();

    }

    /*
    Log methods
     */

    public void saveLogRecord(String idUser, String commentary) {

        LocalDateTime dateTime = new LocalDateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-mm-yyyy HH:mm:ss");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(properties.getProperty("files_location") + "\\log.dat", true));
            bw.write(commentary + ";" + idUser + ";" + dtf.print(dateTime) + "\n");
            bw.close();
        } catch (IOException e) {

        }
    }

    /*
    Serialization methods
    */


    public void saveUserList(ArrayList<User> userList) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(properties.getProperty("files_location") +
                    "\\data_structures\\userlist.dat"));
            out.writeObject(userList);
            out.close();
        } catch (IOException e) {

        }
    }

    public void saveUser(User user) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(properties.getProperty("files_location") +
                    "\\users\\" + "u" + user.getId() + ".dat"));
            out.writeObject(user);
            out.close();
        } catch (IOException e) {

        }
    }

    //This method just put together saveUser and saveUserlist just to save typing more lines

    public void saveUserAndUserlist(User user) {
        saveUser(user);
        saveUserList(userList);
    }

    public void retrieveUserList() {
        userList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(properties.getProperty("files_location") +
                    "\\data_structures\\userlist.dat"));
            userList = (ArrayList) input.readObject();
        } catch (Exception e) {
            File file = new File(properties.getProperty("files_location") + "\\users");
            String[] users = file.list();
            if (users != null){
                for (String s : users) {
                    try {
                        ObjectInputStream input = new ObjectInputStream(new FileInputStream(file.toString() + "\\" + s));
                        userList.add((User) input.readObject());
                    } catch (Exception e1) {

                    }
                }
            }
        }

    }

    public void saveRaceList(ArrayList<Race> raceList) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(properties.getProperty("files_location") +
                    "\\data_structures\\racelist.dat"));
            out.writeObject(raceList);
            out.close();
        } catch (IOException e) {

        }
    }

    public void saveRace(Race race) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(properties.getProperty("files_location") +
                    "\\races\\" + "r" + race.getId() + ".dat"));
            out.writeObject(race);
            out.close();
        } catch (IOException e) {

        }
    }

    public void retrieveRaceList() {
        raceList = new ArrayList<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(properties.getProperty("files_location") +
                    "\\data_structures\\racelist.dat"));
            raceList = (ArrayList) input.readObject();
        } catch (Exception e) {
            File file = new File(properties.getProperty("files_location") + "\\races");
            String[] races = file.list();
            if (races != null && races.length > 0){
                for (String s : races) {
                    try {
                        ObjectInputStream input = new ObjectInputStream(new FileInputStream(file.toString() + "\\" + s));
                        raceList.add((Race) input.readObject());
                    } catch (Exception e1) {

                    }
                }
            }else{
                insertInto();
            }

        }
    }
    

    public void saveHashMap(HashMap<String, String> hashMap, String file) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(properties.getProperty("files_location") +
                    "\\data_structures\\" + file));
            out.writeObject(hashMap);
            out.close();
        } catch (IOException e) {

        }
    }

    public HashMap<String, String> retrieveHashMap(String file) {
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(properties.getProperty("files_location") +
                    "\\data_structures\\" + file));
            hashMap = (HashMap) input.readObject();
        } catch (Exception e) {

        }
        return hashMap;
    }

    private void insertInto (){
        raceList.add(new Race("1", "San Antón", "Jaén", "www.com", "álava"));
        raceList.add(new Race("2", "Media Maratón Jaén", "Granada", "http://www.aytojaen.es", "É"));
        raceList.add(new Race("3", "Pantano del Víboras", "Las Casillas", "https://www.martos.es", "Jaén"));
        raceList.add(new Race("4", "Reto Araque", "Jamilena", "http://www.deportime.com", "Jaén"));
        saveRaceList(raceList);
    }



}
