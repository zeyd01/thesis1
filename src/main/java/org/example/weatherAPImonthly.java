package org.example;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class weatherAPImonthly {
    weatherhistoryAPI weatherhistoryAPI = new weatherhistoryAPI();
    private static final String API_KEY = "c303342a6ee73763d924a176f7d7ed12";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/onecall/timemachine";
    private OkHttpClient client;

    public weatherAPImonthly() {
        client = new OkHttpClient();
    }

    public void fetchAndSaveWeatherData(String lat, String lon) throws IOException {
        List<JsonObject> weatherData = getMonthlyWeatherData(lat, lon);
        LocalDate currentDate = LocalDate.now();
        weatherhistoryAPI.saveWeatherDataToARFF(weatherData, "weather_data_" + currentDate.getYear() + "-" + currentDate.getMonthValue() + ".arff");
    }

    private List<JsonObject> getMonthlyWeatherData(String lat, String lon) throws IOException {
        List<JsonObject> weatherData = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), 1);
        LocalDate endDate = currentDate;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            long timestamp = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
            HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                    .addQueryParameter("lat", lat)
                    .addQueryParameter("lon", lon)
                    .addQueryParameter("dt", String.valueOf(timestamp))
                    .addQueryParameter("appid", API_KEY)
                    .build();

            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                    weatherData.add(json);
                }
            } catch (IOException e) {
                System.err.println("Failed to fetch weather data for date: " + date);
                e.printStackTrace();
            }
        }

        return weatherData;
    }

}
