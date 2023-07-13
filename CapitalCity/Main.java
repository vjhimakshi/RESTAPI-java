import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
public class Main {
    private static final String RESTURL ="https://jsonmock.hackerrank.com/api/countries";
    private static final Logger logger = Logger.getLogger("Main");

    public static String getResponse(String endpoint,String country) throws IOException {
        final String urlEncodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8.toString());
        String newurl = endpoint+"?name="+urlEncodedCountry;
        System.out.println("New Url "+newurl);
        URL url = new URL(newurl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.addRequestProperty("Content-Type", "application/json");
        int status = con.getResponseCode();
        if(status<200 || status>=300) {
            throw new IOException("Error in reading data with status:"+status);
        }

        BufferedReader br = new BufferedReader(new
                InputStreamReader(con.getInputStream()));
        String response;
        StringBuilder sb = new StringBuilder();
        while((response = br.readLine())!=null) {
            sb.append(response);
        }

        br.close();
        con.disconnect();
        return sb.toString();
    }

    public static String getCapitalCity(String country) {
        {
            String capitalCity = "-1";
            try {
                StringBuilder sb = new StringBuilder();
                String response = getResponse(RESTURL, country);
                JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
                JsonArray apiData = jsonResponse.getAsJsonArray("data");
                for (JsonElement e : apiData) {
                    capitalCity = e.getAsJsonObject()
                            .get("capital")
                            .getAsString();
                    if (capitalCity == null || capitalCity.isEmpty()) {
                        return "-1";
                    } else {
                        return capitalCity;
                    }
                }


            } catch (IOException e) {
                final String errMsg = String.format("Error in getting result for country %s", country);
                logger.log(Level.SEVERE, errMsg, e);
            }
            return capitalCity;
        }
    }

    public static void main(String[] args) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new
//                InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
//        String country = bufferedReader.readLine();
//        String phoneNumber = bufferedReader.readLine();
        final String country = "Houston";
        final String result = Main.getCapitalCity(country);
        System.out.println(result);
//        bufferedWriter.write(result);
//        bufferedWriter.newLine();
//        bufferedReader.close();
//        bufferedWriter.close();
    }
}