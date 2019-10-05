package edu.hackathon.habit.util;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import java.util.concurrent.TimeUnit;

public class FinderUtils {

    public String[] findCities(String lat, String lng, String radius) {
        String jsonResponse = "";
        Response response=null;
        JSONObject jsonObject;
        JSONArray jsonArray=null;
        try {
            HttpUrl.Builder builder = HttpUrl.parse("https://www.freemaptools.com/ajax/get-all-cities-inside.php")
                    .newBuilder();
            HttpUrl.Builder urlBuilder = builder;
            urlBuilder.addQueryParameter("lat", lat);
            urlBuilder.addQueryParameter("lng", lng);
            urlBuilder.addQueryParameter("radius", radius);
            String requestUrl = urlBuilder.build().toString();
            Request requestBuilder = new Request.Builder().url(requestUrl).addHeader("Authorization", "").build();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60000, TimeUnit.SECONDS)
                    .readTimeout(60000, TimeUnit.SECONDS).writeTimeout(60000, TimeUnit.SECONDS).build();
            response = client.newCall(requestBuilder).execute();
            jsonResponse = response.body().string();
            jsonObject = XML.toJSONObject(jsonResponse);
            JSONObject jsonElement =  jsonObject.getJSONObject("cities");
            jsonArray = jsonElement.getJSONArray("city");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getCityNameList(jsonArray);
    }
    private String[] getCityNameList(JSONArray jsonArray){
        if(jsonArray==null || jsonArray.length()==0) return null;
        String[] cities= new String[jsonArray.length()];
        for (int i=0; i<jsonArray.length();i++){
            cities[i]= ""+((JSONObject)jsonArray.get(i)).get("name");
        }

        return cities;
    }

    public String findCity(String lat, String lng) {
        String list[]= findCities("26.310778","80.253227","16");
        String cityName =  list==null?"":list[0];
        return cityName;
    }
    public static void main(String args[]){
        FinderUtils uti= new FinderUtils();
        System.out.println("This is the data : "+uti.findCity("26.310778","80.253227"));
    }
}