package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;


public class Parser {
    HashMap<Double, Integer> monthlyTransactions = new HashMap<>();
    int validTxnsCount = 0;
    double sum = 0;

    //Entries are not sorted by timestamp. We need to parse all entries.
    //Duplicate entries also present
    public void updateMonthDataByPage(JsonObject jsonObject, String monthYear) throws ParseException {
        String[] date = monthYear.split("-");
        String month = date[0];
        String year = date[1];

        String startDate = year+"/"+month+"/"+"01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date stDate = sdf.parse(startDate);
        long start = stDate.getTime();

        Calendar cal = Calendar.getInstance();
        cal.setTime(stDate);
        int lastDateOfMonth =  cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        String endDate = year+"/"+month+"/"+ lastDateOfMonth + " 23:59:59";
        Date edDate = sdf.parse(endDate);
        long end = edDate.getTime();


        JsonArray data = jsonObject.get("data").getAsJsonArray();
        int n = data.size();

        for(int i=0; i<n; i++) {
            long txnTS = Long.parseLong(data.get(i).getAsJsonObject().get("timestamp").toString());
            double amount = trimAmount(data.get(i).getAsJsonObject().get("amount").toString());
            int id = Integer.parseInt(data.get(i).getAsJsonObject().get("id").toString());

            if(txnTS>=start && txnTS<=end){
                monthlyTransactions.put(amount, id);
            }
        }
    }

    public double getAvg() {
        double sum = 0;
        int count = monthlyTransactions.size();
        Iterator itr = monthlyTransactions.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Double, Integer> entry = (Map.Entry) itr.next();
            sum = sum + entry.getKey();
        }
        return sum/count;
    }

    public List<Integer> getExceededIds(double avg) {
        List<Integer> list = new ArrayList<>();
        Iterator itr = monthlyTransactions.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<Double, Integer> entry = (Map.Entry) itr.next();
            if(entry.getKey() > avg)
                list.add(entry.getValue());
        }
        return list;
    }

    public double trimAmount(String amount) {
        return Double.parseDouble(amount
                .replaceAll(",","")
                .replace("$","")
                .replaceAll("\"", ""));
    }

}
