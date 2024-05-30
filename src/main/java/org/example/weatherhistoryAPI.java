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

public class weatherhistoryAPI {

    private static final String API_KEY = "c303342a6ee73763d924a176f7d7ed12";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/onecall/timemachine";
    private OkHttpClient client;

    public weatherhistoryAPI() {
        client = new OkHttpClient();
    }

    public void fetchAndSaveWeatherData(String lat, String lon, int year) throws IOException {
        List<JsonObject> weatherData = getYearlyWeatherData(lat, lon, year);
        saveWeatherDataToARFF(weatherData, "weather_data_" + year + ".arff");
    }

    private List<JsonObject> getYearlyWeatherData(String lat, String lon, int year) throws IOException {
        List<JsonObject> weatherData = new ArrayList<>();

        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

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

    public void saveWeatherDataToARFF(List<JsonObject> weatherData, String filename) throws IOException {
        try (FileWriter file = new FileWriter(filename)) {
            // Write ARFF header
            file.write("@RELATION weather_data\n\n");
            file.write("@ATTRIBUTE tarih DATE \"yyyy-MM-dd\"\n");
            file.write("@ATTRIBUTE sicaklik NUMERIC\n");
            file.write("@ATTRIBUTE nem NUMERIC\n");
            file.write("@ATTRIBUTE basinc NUMERIC\n");
            file.write("@ATTRIBUTE rüzgar_siddeti NUMERIC\n");
            file.write("@ATTRIBUTE hava_tipi STRING\n\n");
            file.write("@DATA\n");

            for (JsonObject jsonObject : weatherData) {
                // Extract necessary fields from the JSON object
                long timestamp = jsonObject.get("current").getAsJsonObject().get("dt").getAsLong();
                String date = LocalDate.ofEpochDay(timestamp / 86400).toString(); // Convert timestamp to ISO date
                String temperature = jsonObject.get("current").getAsJsonObject().get("temp").getAsString();
                String humidity = jsonObject.get("current").getAsJsonObject().get("humidity").getAsString();
                String pressure = jsonObject.get("current").getAsJsonObject().get("pressure").getAsString();
                String windSpeed = jsonObject.get("current").getAsJsonObject().get("wind_speed").getAsString();
                String weatherDescription = jsonObject.get("current").getAsJsonObject()
                        .get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();

                // Write a line in ARFF format
                file.write(String.join(",", date, temperature, humidity, pressure, windSpeed, weatherDescription));
                file.write("\n");
            }
        }
    }
}
