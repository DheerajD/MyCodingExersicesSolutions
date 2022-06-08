package service;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UserService {
    CallerService callerService;
    Parser parser;

    public UserService() {
        callerService = new CallerService();
        parser = new Parser();
    }
    public int[] getUserTransactions(String uid, String txnType, String monthYear) {
        JsonObject initialJsonObject = callerService.getCallJSONData(uid, txnType, 1);  //Call to 1st page data to fetch total_pages
        int totalPages = Integer.parseInt(initialJsonObject.get("total_pages").getAsString());

        double monthlyAvg = getMonthlyAvg(uid, txnType, monthYear, totalPages);
        return getExceededIds(monthlyAvg);
    }

    public double getMonthlyAvg(String uid, String txnType, String monthYear, int totalPages) {
        for(int i=0; i<=totalPages; i++) {
            JsonObject jsonObject = callerService.getCallJSONData(uid, txnType, i);
            try {
                parser.updateMonthDataByPage(jsonObject, monthYear);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return parser.getAvg();
    }

    public int[] getExceededIds(double monthlyAvg){
        List<Integer> list = parser.getExceededIds(monthlyAvg);
        Collections.sort(list);

        int n = list.size();
        int[] result = new int[n];
        for(int i=0; i<n; i++)
            result[i] = list.get(i);

        return result;
    }
}
