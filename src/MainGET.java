import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainGET {

    public static <Date> void main(String[] args) {
        try {
            String key = "39206396-a3d0261b98314ee7c13677bfd";
//            String startDate, endDate;

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));

            String request;
            System.out.println("Enter request");
            request = reader.readLine();
            reader.close();

            URL url = new URL("https://pixabay.com/api/?key="+key+"&q="+request+"&image_type=photo");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                scanner.close();

                System.out.println("Processing...");
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);
                JSONArray arr = (JSONArray) data_obj.get("hits");
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject t = (JSONObject) arr.get(i);
                    try{
                        InputStream in = new URL((String) t.get("largeImageURL")).openStream();
                        Files.copy(in, Paths.get(("img\\"+String.valueOf(i)+".jpg")), StandardCopyOption.REPLACE_EXISTING);
                    } catch (Exception e){}
                }
                System.out.println("Downloaded " + arr.size() + " images");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}