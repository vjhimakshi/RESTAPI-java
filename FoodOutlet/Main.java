package hackerrank;
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

private static final String FOODURL ="https://jsonmock.hackerrank.com/api/food_outlets";


   public static List<String> getRelevantFoodOutlets(String city, int maxCost){
        List<String> foodOutlets= new ArrayList<>();
        try {
            String response = getResponse(FOODURL,city,1);
            JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
            int currentPage = jsonResponse.getAsJsonObject().get("page").getAsInt();
            int totalPages = jsonResponse.getAsJsonObject().get("total_pages").getAsInt();
            System.out.println(totalPages);
            JsonArray apiData = jsonResponse.getAsJsonArray("data");
            for(JsonElement e: apiData){
                if(e.getAsJsonObject().get("estimated_cost").getAsInt()<=maxCost){
                    foodOutlets.add(e.getAsJsonObject().get("name").getAsString());
                }
            }
            if(currentPage<totalPages) {
                for (int page = currentPage+1; page <= totalPages; page++) {
                    response = getResponse(FOODURL, city, page);
                    jsonResponse = new Gson().fromJson(response, JsonObject.class);
                    apiData = jsonResponse.getAsJsonArray("data");
                    for (JsonElement e : apiData) {
                        if (e.getAsJsonObject().get("estimated_cost").getAsInt() <= maxCost) {
                            foodOutlets.add(e.getAsJsonObject()
                                    .get("name")
                                    .getAsString());
                        }
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Exception "+e);
        }
        return foodOutlets;
    }

 public static String getResponse(String endpoint,String city, int page) throws IOException {
        final String urlEncodedCountry = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
        String newurl = endpoint+"?city="+urlEncodedCountry+"&"+"page="+page;
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

  public static void main(String[] args) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new
//                InputStreamReader(System.in));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
//        String country = bufferedReader.readLine();
//        String phoneNumber = bufferedReader.readLine();
//        String result = Result.getPhoneNumbers(country, phoneNumber);
        final String country = "Houston";
        // final String phoneNumber = "123456";

        final List<String> result = Main.getRelevantFoodOutlets(country, 30);
        System.out.println(result);
//        bufferedWriter.write(result);
//        bufferedWriter.newLine();
//        bufferedReader.close();
//        bufferedWriter.close();
    }
}