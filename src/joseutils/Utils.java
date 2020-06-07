package joseutils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class Utils {

    public static String replaceAccents(String word) {

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == 'Á') {
                word = word.substring(0, i) + 'A' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'á') {
                word = word.substring(0, i) + 'a' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'É') {
                word = word.substring(0, i) + 'E' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'é') {
                word = word.substring(0, i) + 'e' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'Í') {
                word = word.substring(0, i) + 'I' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'í') {
                word = word.substring(0, i) + 'i' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'Ó') {
                word = word.substring(0, i) + 'O' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'ó') {
                word = word.substring(0, i) + 'o' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'Ú') {
                word = word.substring(0, i) + 'U' + word.substring(i + 1);
            }
            if (word.charAt(i) == 'ú') {
                word = word.substring(0, i) + 'u' + word.substring(i + 1);
            }
        }
        return word;
    }

    public static void mockLoading() {
        try {
            System.out.println();
            System.out.print("CARGANDO ");
            for (int i = 0; i < 8; i++) {
                System.out.print(" # ");
                Thread.sleep(150);
            }
            System.out.println();
        } catch (Exception e) {

        }
    }

    public static String fileReader(String location) throws IOException {

        String reader;
        String file = "";


        BufferedReader br = new BufferedReader(new FileReader(location));
        while ((reader = br.readLine()) != null) {
            file += reader + "\n";
        }

        return file;
    }

    public static String httpRequestJSON(String url) throws IOException {
        String json = "";
        String reader;

        URL direction = new URL(url);
        URLConnection con = direction.openConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((reader = br.readLine()) != null) {
            json = json.concat(reader);
        }
        br.close();
        return json;

    }

    public static void copyFile (File source, File target) throws IOException {
        InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(target);
        byte [] buffer = new byte[1024];
        while (is.read(buffer) > 0){
            os.write(buffer);
        }
        is.close();
        os.close();
    }
}
