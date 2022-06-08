package service;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//Reference for making API calls: https://mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
public class CallerService {
    URL url;
    public JsonObject getCallJSONData(String uid, String txnType, int pageNo) {
        try {
            url = new URL("https://jsonmock.hackerrank.com/api/transactions/search?userId="+uid+"&txnType="+txnType+"&page="+pageNo);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            if (con.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

            String output, res="";
            while ((output = br.readLine()) != null)
                res = res + output;

            //con.disconnect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonTree = jsonParser.parse(res);
            return jsonTree.getAsJsonObject();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


}
