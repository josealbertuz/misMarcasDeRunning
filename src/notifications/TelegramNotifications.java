package notifications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TelegramNotifications {
    private String api;
    private String id;

    public TelegramNotifications(String api, String id) {
        this.api = api;
        this.id = id;
    }

    public void sendTelegram(String message) throws IOException {
        String direction = "https://api.telegram.org/bot" + api + "/sendMessage?chat_id=" + id + "&text=" + message;
        URL url = new URL(direction);
        URLConnection con = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    }
}
