package edu.hackathon.habit.util;
import okhttp3.*;
import java.util.concurrent.TimeUnit;

public class finderUtils {

    public String findCity(String lat, String lng, String radius) {
        String jsonResponse = "";
        try {
            HttpUrl.Builder builder = HttpUrl.parse("https://www.freemaptools.com/ajax/get-all-cities-inside.php")
                    .newBuilder();
            HttpUrl.Builder urlBuilder = builder;
            urlBuilder.addQueryParameter("lat", lat);
            urlBuilder.addQueryParameter("lng", lng);
            urlBuilder.addQueryParameter("radius", radius);
            String requestUrl = urlBuilder.build().toString();
            Request requestBuilder = new Request.Builder().url(requestUrl).addHeader("Authorization", "").build();
            OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();
            Response response = client.newCall(requestBuilder).execute();
            jsonResponse = response.body().string();
        } catch (Exception e) {

        }
        return jsonResponse;
    }
}