import joseutils.Utils;
import modelo.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Scanner;


public class main {
    static Scanner s = new Scanner(System.in);

    public static void main(String[] args) {
        AppManagement appManagement = new AppManagement();
        boolean exit = false;
        int optionHallMenu = 0;
        User user = new User();
        boolean checkTryCatch;
        int optionLoggedMenu = 0;


        if (!appManagement.getProperties().getProperty("guest_access").equals("true")) {
            System.out.println("Lo sentimos, pero el programa se encuentra en mantenimiento");
        } else {
            while (!exit) {
                printHallMenu();
                checkTryCatch = false;
                do {
                    System.out.print("Introduce una opción: ");
                    try {
                        optionHallMenu = Integer.parseInt(s.nextLine());
                        checkTryCatch = true;
                    } catch (Exception e) {
                        System.out.println("Tienes que introducir un número");
                    }
                } while (!checkTryCatch);

                switch (optionHallMenu) {
                    case 1:
                        raceFinder(appManagement);
                        break;
                    case 2:
                        markFinderByRace(appManagement);
                        break;
                    case 3:
                        user = logIn(appManagement);
                        break;
                    case 4:
                        signUp(appManagement);
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("Tienes que introducir una opción");
                        break;
                }

                while (user.isLogged()) {
                    System.out.println("Bienvenido a MisMarcasdeRunning.");
                    System.out.println("Logueado como: " + user.getUserName());
                    System.out.println("Su última conexión fue " + user.getLastConnection());
                    printLoggedMenu();
                    checkTryCatch = false;
                    do {
                        System.out.print("Introduce una opción: ");
                        try {
                            optionLoggedMenu = Integer.parseInt(s.nextLine());
                            checkTryCatch = true;
                        } catch (Exception e) {
                            System.out.println("Tienes que introducir un número");
                        }
                    } while (!checkTryCatch);

                    switch (optionLoggedMenu) {
                        case 1:
                            raceFinder(appManagement);
                            break;
                        case 2:
                            markFinderByRace(appManagement);
                            break;
                        case 3:
                            System.out.println(user);
                            break;
                        case 4:
                            editProfile(user, appManagement);
                            break;
                        case 5:
                            displayPersonalMarks(appManagement, user);
                            break;
                        case 6:
                            markRegister(appManagement, user);
                            break;
                        case 7:
                            markFinderByUser(appManagement);
                            break;
                        case 8:
                            generateJSON(appManagement, user);
                            break;
                        case 9:
                            removeUser(appManagement, user);
                            break;
                        case 10:
                            user.changeToLogOut();
                            appManagement.saveLogRecord(user.getId(), "Cierre de sesión");
                            appManagement.saveUserAndUserlist(user);
                            user = new User();
                            break;
                        default:
                            System.out.println("Tienes que introducir alguna opción");
                            break;
                    }
                }
            }
        }
    }

    public static void printLoggedMenu() {
        System.out.println("=======================================");
        System.out.println("1. Buscar carreras");
        System.out.println("2. Ver marcas de carreras");
        System.out.println("3. Mostrar mi perfil de usuario");
        System.out.println("4. Cambiar datos");
        System.out.println("5. Ver mis marcas personales");
        System.out.println("6. Introducir una marca en una carrera");
        System.out.println("7. Ver marcas usuarios");
        System.out.println("8. Generar un JSON de mi usuario");
        System.out.println("9. Borrar perfil de usuario");
        System.out.println("10. Cerrar sesión");
        System.out.println("=======================================");
    }

    public static void printHallMenu() {
        System.out.println("Bienvenido a MisMarcasdeRunning");
        System.out.println("=======================================");
        System.out.println("1. Buscar carreras");
        System.out.println("2. Ver marcas de carreras");
        System.out.println("3. Iniciar sesión");
        System.out.println("4. Registrarse");
        System.out.println("5. Salir");
        System.out.println("=======================================");
    }

    public static void raceFinder(AppManagement appManagement) {

        boolean checkTryCatch, getBacktoLoggedMenu = false;
        int optionRaceFinder = 0;
        String queryData;
        while (!getBacktoLoggedMenu) {
            System.out.println("\tBuscador de carreras");
            System.out.println("=======================================");
            System.out.println("1. Mostrar las carreras del programa");
            System.out.println("2. Buscar carrera por id");
            System.out.println("3. Buscar carrera por provincia");
            System.out.println("4. Buscar carrera por nombre");
            System.out.println("5. Volver");
            System.out.println("=======================================");

            checkTryCatch = false;
            do {
                System.out.print("Introduce una opción: ");
                try {
                    optionRaceFinder = Integer.parseInt(s.nextLine());
                    checkTryCatch = true;
                } catch (Exception e) {
                    System.out.println("Tienes que introducir un número");
                }
            } while (!checkTryCatch);

            switch (optionRaceFinder) {
                case 1:
                    for (Race r : appManagement.getRaceList()) {
                        System.out.println(r);
                    }
                    break;
                case 2:
                    queryData = queryIdRace();
                    Utils.mockLoading();
                    if (appManagement.raceFinderById(queryData) == -1)
                        System.out.println("La carrera que buscas no existe");
                    else {
                        System.out.println(appManagement.getRaceList().get(appManagement.raceFinderById(queryData)));
                    }
                    break;
                case 3:
                    System.out.print("Introduce el nombre de la provincia donde se celebra la carrera: ");
                    queryData = s.nextLine();
                    if (appManagement.raceFinderByCountry(queryData).isEmpty()) {
                        System.out.println("No existen carreras que se hayan celebrado en " + queryData);
                    } else {
                        for (Race r : appManagement.raceFinderByCountry(queryData)) {
                            System.out.println(r);
                        }
                    }
                    break;
                case 4:
                    System.out.print("Introduce el nombre de la carrera: ");
                    queryData = s.nextLine();
                    if (appManagement.raceFinderByName(queryData).isEmpty()) {
                        System.out.println("No existen carreras con el nombre de " + queryData);
                    } else {
                        for (Race r : appManagement.raceFinderByName(queryData)) {
                            System.out.println(r);
                        }
                    }
                    break;
                case 5:
                    getBacktoLoggedMenu = true;
                    break;
                default:
                    System.out.println("Tiene que introducir alguna opción");
                    break;
            }
        }
    }

    public static void markFinderByRace(AppManagement appManagement) {

        int optionMenuMarkFinderByRace = 0;
        boolean checkTryCatch, getBackToLoggedMenu = false;
        String queryData;

        while (!getBackToLoggedMenu) {
            System.out.println("\tBuscador marcas por carrera");
            System.out.println("===========================================================");
            System.out.println("1. Mostrar todas las marcas de una carrera");
            System.out.println("2. Mostrar las N marcas primeras de una carrera");
            System.out.println("3. Mostrar las marcas de una carrera en un año determinado");
            System.out.println("4. Volver");
            System.out.println("============================================================");

            checkTryCatch = false;
            do {
                System.out.print("Introduce una opción: ");
                try {
                    optionMenuMarkFinderByRace = Integer.parseInt(s.nextLine());
                    checkTryCatch = true;
                } catch (Exception e) {
                    System.out.println("Tienes que introducir un número");
                }
            } while (!checkTryCatch);

            switch (optionMenuMarkFinderByRace) {
                case 1:
                    queryData = queryIdRace();
                    if (appManagement.markFinderByRaceId(queryData).isEmpty())
                        System.out.println("No existen marcas para la carrera con id " + queryData);
                    else {
                        for (MarkDataclass m : appManagement.markFinderByRaceId(queryData)) {
                            System.out.println(m);
                        }
                    }
                    break;
                case 2:
                    int numberOfMarks = 0;
                    checkTryCatch = false;
                    queryData = queryIdRace();
                    System.out.print("Introduce el número de marcas que quieres ver: ");
                    try {
                        numberOfMarks = Integer.parseInt(s.nextLine());
                        checkTryCatch = true;
                    } catch (Exception e) {
                        System.out.println("Tienes que introducir un número");
                    }
                    if (checkTryCatch) {
                        int sizeOfArray = appManagement.markFinderByRaceIdGivingANumber(queryData, numberOfMarks).size();

                        if (appManagement.markFinderByRaceIdGivingANumber(queryData, numberOfMarks).isEmpty())
                            System.out.println("No existen marcas para la carrera con id " + queryData);
                        else {
                            for (MarkDataclass m : appManagement.markFinderByRaceIdGivingANumber(queryData, numberOfMarks)) {
                                System.out.println(m);
                            }
                            if (sizeOfArray < numberOfMarks)
                                System.out.println("Lo sentimos pero solo existen " + sizeOfArray + " marcas en esta carrera");
                        }
                    }
                    break;
                case 3:
                    int year = 0;
                    checkTryCatch = false;
                    queryData = queryIdRace();
                    System.out.print("Introduce el año en el cual se celebró la carrera: ");
                    try {
                        year = Integer.parseInt(s.nextLine());
                        checkTryCatch = true;
                    } catch (Exception e) {
                        System.out.println("Tienes que introducir un año");
                    }
                    if (checkTryCatch) {
                        if (appManagement.markFinderByRaceIdYear(queryData, year).isEmpty())
                            System.out.println("No existen marcas para la carrera con id " + queryData + " en el año " + year);
                        else {
                            for (MarkDataclass m : appManagement.markFinderByRaceIdYear(queryData, year)) {
                                System.out.println(m);
                            }
                        }
                    }
                    break;
                case 4:
                    getBackToLoggedMenu = true;
                    break;
                default:
                    System.out.println("Tienes que introducir alguna opción");
                    break;
            }
        }
    }

    public static void editProfile(User user, AppManagement appManagement) {

        int optionMenuEditProfile = 0;
        boolean checkTryCatch, getBackToLoggedMenu = false;
        String queryData;

        while (!getBackToLoggedMenu) {
            System.out.println("\tMenú de cambio de datos");
            System.out.println("===============================");
            System.out.println("1. Cambiar email");
            System.out.println("2. Cambiar contraseña");
            System.out.println("3. Cambiar nombre de usuario");
            System.out.println("4. Cambiar fecha de nacimiento");
            System.out.println("5. Cambiar número de teléfono");
            System.out.println("6. Volver");
            System.out.println("===============================");

            checkTryCatch = false;
            do {
                System.out.print("Introduce una opción: ");
                try {
                    optionMenuEditProfile = Integer.parseInt(s.nextLine());
                    checkTryCatch = true;
                } catch (Exception e) {
                    System.out.println("Tienes que introducir un número");
                }
            } while (!checkTryCatch);

            switch (optionMenuEditProfile) {
                case 1:
                    System.out.print("Introduce el nuevo email: ");
                    queryData = s.nextLine();
                    checkTryCatch = false;
                    if (!appManagement.getEmailPassword().containsKey(queryData) || !User.validateEmail(queryData)) {
                        user.generateNewToken();
                        try {
                            user.sendEmailForChangingEmail(queryData);
                            checkTryCatch = true;
                        } catch (Exception e) {

                        }
                        try {
                            user.sendTelegramForChangingEmail();
                            checkTryCatch = true;
                        } catch (Exception e) {

                        }
                        if (checkTryCatch) {
                            System.out.println("Para poder realizar el cambio tendrá que introducir el código que le hemos enviado" +
                                    " a al correo introducido");
                            String token;
                            int attemps = 3;
                            do {
                                System.out.println("Tiene " + attemps + " intentos");
                                System.out.print("Introduzca el código de verificación: ");
                                token = s.nextLine();
                                attemps--;
                            } while (attemps >= 1 && !user.checkToken(token));
                            if (attemps == 1) System.out.println("Su email no pudo ser modificado");
                            else {
                                appManagement.updateHashMapKey(user, queryData, 0);
                                appManagement.saveLogRecord(user.getId(), "Modificación de email :" +
                                        "Era " + user.getEmail() + " y ahora es " + queryData);
                                user.setEmail(queryData);
                                appManagement.saveUserAndUserlist(user);
                                appManagement.saveHashMap(appManagement.getEmailPassword(), "emailPassword.dat");
                                System.out.println("Su email ha sido cambiado con éxito a " + user.getEmail());
                            }
                        } else
                            System.out.println("Compruebe su conexión a Internet, en caso contrario no podremos cambiarle el email");
                    } else
                        System.out.println("Esta email ya está cogido por otro usuario, tiene un formato erróneo o no existe");
                    break;
                case 2:
                    System.out.println("Recuerde que la nueva contraseña debe tener una longitud mínima de 8 carácteres, un letra mayúscula, " +
                            "una letra minúscula y un número.");
                    System.out.print("Introduzca la antigua contraseña: ");
                    String oldPassword = s.nextLine();
                    System.out.print("Introduzca la nueva contraseña: ");
                    String newPassword1 = s.nextLine();
                    System.out.print("Introduza la nueva contraseña de nuevo: ");
                    String newPassword2 = s.nextLine();
                    if (user.checkActualPassword(oldPassword) && user.changePassword(newPassword1, newPassword2) && User.checkPassword(newPassword1)) {
                        appManagement.saveLogRecord(user.getId(), "Modificación de contraseña");
                        appManagement.getEmailPassword().put(user.getEmail(), newPassword1);
                        appManagement.getUserNamePassword().put(user.getUserName(), newPassword1);
                        user.setPassword(newPassword1);
                        appManagement.saveUserAndUserlist(user);
                        appManagement.saveHashMap(appManagement.getEmailPassword(), "emailPassword.dat");
                        appManagement.saveHashMap(appManagement.getUserNamePassword(), "userNamePassword.dat");
                        System.out.println("Contraseña cambiada con éxito");
                    } else
                        System.out.println("No pudimos cambiar su contraseña, ha introducido datos erróneos o tiene un formato no válido.");
                    break;
                case 3:
                    System.out.print("Introduzca al nombre de usuario que desea cambiar: ");
                    queryData = s.nextLine();
                    if (!appManagement.getUserNamePassword().containsKey(queryData)) {
                        System.out.println("Para realizar el cambio deberá introducir su contraseña");
                        System.out.print("Introduzca su contraseña: ");
                        if (user.checkActualPassword(s.nextLine())) {
                            appManagement.saveLogRecord(user.getId(), "Modificación del nombre de usuario: " +
                                    "Era " + user.getUserName() + " y ahora es " + queryData);
                            appManagement.updateHashMapKey(user, queryData, 1);
                            user.setUserName(queryData);
                            appManagement.saveUserAndUserlist(user);
                            appManagement.saveHashMap(appManagement.getUserNamePassword(), "userNamePassword.dat");
                            System.out.println("Su nombre de usuario fue cambiado a " + user.getUserName());
                        } else System.out.println("Lo sentimos pero la contraseña es incorrecta");
                    } else System.out.println("Ese nombre de usuario ya está cogido");
                    break;
                case 4:
                    System.out.print("Introduce la nueva fecha de nacimiento: ");
                    queryData = s.nextLine();
                    if (User.checkBirthDate(queryData)) {
                        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
                        appManagement.saveLogRecord(user.getId(), "Modificación de fecha de nacimiento: " +
                                "Era " + dtf.print(user.getBirthDate()) + " y ahora es " + queryData);
                        user.setBirthDate(dtf.parseLocalDate(queryData));
                        appManagement.saveUserAndUserlist(user);
                        System.out.println("Su fecha de nacimiento ha sido cambiada a " + dtf.print(user.getBirthDate()));
                    } else System.out.println("La fecha tiene un formato no válido");
                    break;
                case 5:
                    System.out.print("Introduce el nuevo número de teléfono: ");
                    queryData = s.nextLine();
                    if (User.checkPhoneNumber(queryData)) {
                        appManagement.saveLogRecord(user.getId(), "Modificación de teléfono: " +
                                "Era " + user.getPhoneNumber() + " y ahora es " + queryData);
                        user.setPhoneNumber(queryData);
                        appManagement.saveUserAndUserlist(user);
                        System.out.println("Su número ha sido cambiado a " + user.getPhoneNumber());
                    } else System.out.println("El número de teléfono tiene un formato no válido");
                    break;
                case 6:
                    getBackToLoggedMenu = true;
                    break;
                default:
                    System.out.println("Tienes que introducir una opción");
                    break;
            }
        }
    }

    public static void displayPersonalMarks(AppManagement appManagement, User user) {
        int optionMenuDisplayPersonalMarks = 0;
        boolean checkTryCatch, getBackToLoggedMenu = false;
        String queryData;

        while (!getBackToLoggedMenu) {

            System.out.println("\tBuscador de marcas personales");
            System.out.println("===============================");
            System.out.println("1. Mostrar todas mis marcas");
            System.out.println("2. Mostrar mis marcas en una carrera");
            System.out.println("3. Mostrar mis marcas en una año determinado");
            System.out.println("4. Volver");
            System.out.println("===============================");

            checkTryCatch = false;
            do {
                System.out.print("Introduce una opción: ");
                try {
                    optionMenuDisplayPersonalMarks = Integer.parseInt(s.nextLine());
                    checkTryCatch = true;
                } catch (Exception e) {
                    System.out.println("Tienes que introducir un número");
                }
            } while (!checkTryCatch);

            switch (optionMenuDisplayPersonalMarks) {
                case 1:
                    if (user.getMarkList().isEmpty()) System.out.println("No hay marcas registradas");
                    else {
                        for (Mark m : user.getMarkList()) {
                            System.out.println(m);
                        }
                    }
                    break;
                case 2:
                    queryData = queryIdRace();
                    if (appManagement.markFinderByUserRaceId(user.getUserName(), queryData).isEmpty())
                        System.out.println("No hay ninguna marca registrada en la carrera con id " + queryData);
                    else {
                        for (Mark m : appManagement.markFinderByUserRaceId(user.getUserName(), queryData)) {
                            System.out.println(m);
                        }
                    }
                    break;
                case 3:
                    int year = 0;
                    System.out.print("Introduce el año en el cual quieres buscar marcas: ");
                    checkTryCatch = false;
                    try {
                        year = Integer.parseInt(s.nextLine());
                        checkTryCatch = true;
                    } catch (Exception e) {
                        System.out.println("Tienes que introducir un año con formato válido");
                    }
                    if (checkTryCatch) {
                        if (appManagement.markFinderbyYearUser(user.getUserName(), year).isEmpty())
                            System.out.println("No hay ninguna marca registrada a lo largo del año " + year);
                        else {
                            for (Mark m : appManagement.markFinderbyYearUser(user.getUserName(), year)) {
                                System.out.println(m);
                            }
                        }
                    }
                    break;
                case 4:
                    getBackToLoggedMenu = true;
                    break;
                default:
                    System.out.println("Tienes que introducir alguna opción");
                    break;

            }
        }
    }

    public static void markRegister(AppManagement appManagement, User user) {

        System.out.println("Para poder registrar una marca debe introducir el id de la carrera en la cual quieres registrar la marca");
        String raceId = queryIdRace();
        int racePosition = appManagement.raceFinderById(raceId);
        if (racePosition == -1)
            System.out.println("La carrera con id " + raceId + " no existe");
        else {
            System.out.println("Vamos a proceder a registrar su marca en la carrera de " +
                    appManagement.getRace(racePosition).getName());
            System.out.print("Introduzca la marca en el siguiente formato \"hh:mm:ss\": ");
            String mark = s.nextLine();
            System.out.print("Introduzca el año en el cual se obtuvo dicha marca: ");
            String year = s.nextLine();
            System.out.print("Introduzca la posición general, si no la sabe introduzca un 0: ");
            String overallRaking = s.nextLine();
            System.out.print("Introduzca la posición por categoria, si no la sabe introduzca un 0: ");
            String categoryRanking = s.nextLine();
            System.out.print("¿Quiere hacer pública su marca? s/n: ");
            String published = s.nextLine();
            while (!published.equalsIgnoreCase("s") && !published.equalsIgnoreCase("n")) {
                System.out.print("¿Quiere hacer pública su marca? s/n: ");
                published = s.nextLine();
            }
            try {
                DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");
                boolean isPublished = published.equalsIgnoreCase("s");
                Mark markAux = new Mark(appManagement.getRaceList().get(appManagement.raceFinderById(raceId)), Integer.parseInt(year),
                        Integer.parseInt(overallRaking), Integer.parseInt(categoryRanking), dtf.parseLocalTime(mark), isPublished);
                if (appManagement.addMark(user.getUserName(), markAux)) {
                    appManagement.saveLogRecord(user.getId(), "Grabación de marca");
                    appManagement.saveUserAndUserlist(user);
                    System.out.println("Su marca fue registrada con éxito");
                    try {
                        user.markRegisteredEmail(markAux);
                    } catch (Exception e) {

                    }
                    try {
                        user.markRegisteredTelegram(markAux);
                    } catch (Exception e) {

                    }
                } else System.out.println("La marca que está intentando registrar ya ha sido registrada anteriormente");
            } catch (Exception e) {
                System.out.println("La marca no se pudo registrar, asegurese de que el formato de los datos es correcto");
            }
        }
    }

    public static void markFinderByUser(AppManagement appManagement) {
        String queryData = queryUserName();
        if (appManagement.markFinderByUserNamePublic(queryData).isEmpty())
            System.out.println(queryData + " no tiene marcas registradas");
        else {
            for (Mark m : appManagement.markFinderByUserNamePublic(queryData)) {
                System.out.println(m);
            }
        }
    }

    public static void removeUser(AppManagement appManagement, User user) {

        System.out.println("Vamos a proceder a borrar su cuenta");
        System.out.print("Introduzca su contraseña para poder eliminar su cuenta: ");
        if (user.checkActualPassword(s.nextLine())) {
            appManagement.removeUser(user);
            System.out.println("Su cuenta fue eliminado con éxito");
        } else System.out.println("La contraseña no es correcta, por lo que no podemos eliminarle la cuenta");
    }

    public static void signUp(AppManagement appManagement) {

        boolean checkTryCatch;

        System.out.println("Vamos a proceder a crearle un perfil de usuario");
        System.out.print("Introduzca un nombre de usuario: ");
        String userName = s.nextLine();
        System.out.print("Introduzca su correo electrónico: ");
        String email = s.nextLine();
        System.out.print("Introduzca un contraseña: ");
        String password = s.nextLine();
        System.out.print("Introduzca su fecha de nacimiento en el siguiente formato \"dd/MM/aaaa\": ");
        String date = s.nextLine();
        System.out.print("Introduzca su número de teléfono: ");
        String phoneNumber = s.nextLine();

        if (!appManagement.checkRepeatedUser(userName, email)) {
            if (User.validateEmail(email) && User.checkPassword(password) && User.checkPhoneNumber(phoneNumber) &&
                    User.checkBirthDate(date)) {
                DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
                User userAux = new User(email, password, userName, dtf.parseLocalDate(date), phoneNumber);
                checkTryCatch = false;
                try {
                    appManagement.sendEmailAddUser(userAux);
                    checkTryCatch = true;
                } catch (MessagingException | IOException e) {
                    System.out.println(e);
                }
                try {
                    appManagement.sendTelegramAddUser(userAux);
                    checkTryCatch = true;
                } catch (Exception e) {

                }
                if (checkTryCatch) {
                    appManagement.addUser(userAux);
                    System.out.println("Bienvenido " + userName + ", su registro se ha realizado con éxito");
                } else
                    System.out.println("Compruebe su conexión a internet, en caso contrario no podremos registrarle");
            } else {
                if (!User.validateEmail(email))
                    System.out.println("El email introducido no éxiste o tiene un formato erróneo");
                if (!User.checkPassword(password))
                    System.out.println("La contraseña debe tener un mínimo de 8 carácteres, una mayúculas, una miníscula " +
                            "y un número");
                if (!User.checkBirthDate(date)) System.out.println("La fecha de nacimiento tiene un formato erróneo");
                if (!User.checkPhoneNumber(phoneNumber))
                    System.out.println("El número de telefono tiene un formáto erróneo");
            }
        } else {
            System.out.println("Este usuario ya está registrado");
        }

    }


    public static User logIn(AppManagement appManagement) {
        User userAux = new User();
        boolean checkLogin = true;

        System.out.print("Introduzca su nombre de usuario o email: ");
        String userNameOrEmail = s.nextLine();
        System.out.print("Introduzca su contraseña: ");
        String password = s.nextLine();

        int userPosition = appManagement.checkLogin(userNameOrEmail, password);

        if (userPosition == -1) System.out.println("El usuario no existe o la contraseña es errónea");
        else {
            userAux = appManagement.getUserList().get(userPosition);
            if (userAux.isFirstLogin()) {
                checkLogin = false;
                System.out.println("No has introducido aún el código de verificación que hemos enviado a tu email y Telegram");
                System.out.print("Introduce el código de verificación: ");
                if (userAux.checkToken(s.nextLine())) {
                    checkLogin = true;
                    System.out.println("Su código fue verificado con éxito");
                } else System.out.println("Lo sentimos pero el código no es válido, reviselo y vuelvalo a introducir");
            }
            if (checkLogin) {
                userAux.changeToLogged();
                userAux.setLastConnection(appManagement.getProperties().getProperty(userAux.getId()));
                appManagement.setUserProperty(userAux.getId());
                appManagement.saveLogRecord(userAux.getId(), "Inicio de sesión");
                appManagement.saveUserAndUserlist(userAux);
                return userAux;
            }
        }
        return userAux;
    }

    public static void generateJSON(AppManagement appManagement, User user) {

        int optionMenuJSON = 0;
        boolean checkTryCatch, getBackToLoggedMenu = false;

        while (!getBackToLoggedMenu) {
            System.out.println("\t\tGenerador JSON");
            System.out.println("=================================================");
            System.out.println("1. Enviar mi usuario a mi correo en formato JSON");
            System.out.println("2. Generar un archivo JSON");
            System.out.println("3. Volver");
            System.out.println("=================================================");

            checkTryCatch = false;

            do {
                System.out.print("Introduce una opción: ");
                try {
                    optionMenuJSON = Integer.parseInt(s.nextLine());
                    checkTryCatch = true;
                } catch (Exception e) {
                    System.out.println("Tienes que introducir un número");
                }
            } while (!checkTryCatch);

            switch (optionMenuJSON) {
                case 1:
                    try {
                        appManagement.generateJSONEmail(user);
                        System.out.println("Su fichero se ha generado con éxito y fue enviado a su correo");
                    } catch (Exception e) {
                        System.out.println("Ha ocurrido un error, vuelva a intentarlo");
                    }
                    break;
                case 2:
                    try {
                        appManagement.generateJSONdirectory(user);
                        System.out.println("Su fichero fue generado con éxito");
                    } catch (Exception e) {
                        System.out.println("Ha ocurrido un error, vuelva a intentarlo");
                    }
                    break;
                case 3:
                    getBackToLoggedMenu = true;
                    break;
                default:
                    System.out.println("Tienes que introducir una opción");
                    break;
            }
        }
    }

    public static String queryIdRace() {

        System.out.print("Introduce el id de la carrera: ");
        return s.nextLine();
    }

    public static String queryUserName() {

        System.out.print("Introduce el nombre de usuario: ");
        return s.nextLine();
    }

}
