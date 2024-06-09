package org.example;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class weatherhistoryAPI {
    private static final String API_KEY = "be9a30b14079f875a7ae7e931502ac4a";
    private static final String BASE_URL = "https://history.openweathermap.org/data/2.5/aggregated/year";
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

        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("lat", lat)
                .addQueryParameter("lon", lon)
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
            System.err.println("Failed to fetch yearly weather data: " + e.getMessage());
            throw e; // Rethrow the exception to indicate failure
        }

        return weatherData;
    }

    public void saveWeatherDataToARFF(List<JsonObject> weatherData, String filename) throws IOException {
        try (FileWriter file = new FileWriter(filename)) {

            file.write("@RELATION weather_data\n\n");
            file.write("@ATTRIBUTE date DATE \"yyyy-MM-dd\"\n");
            file.write("@ATTRIBUTE tempature NUMERIC\n");
            file.write("@ATTRIBUTE humidity NUMERIC\n");
            file.write("@ATTRIBUTE pressure NUMERIC\n");
            file.write("@ATTRIBUTE wind_speed NUMERIC\n");
            file.write("@ATTRIBUTE weather STRING\n\n");
            file.write("@DATA\n");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (JsonObject jsonObject : weatherData) {
                if (jsonObject.has("current")) {
                    JsonObject current = jsonObject.getAsJsonObject("current");
                    long timestamp = current.get("dt").getAsLong();
                    String date = Instant.ofEpochSecond(timestamp).atOffset(ZoneOffset.UTC).toLocalDate().format(formatter);
                    String temperature = current.get("temp").getAsString();
                    String humidity = current.get("humidity").getAsString();
                    String pressure = current.get("pressure").getAsString();
                    String windSpeed = current.get("wind_speed").getAsString();
                    String weatherDescription = current.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();

                    // Write a line in ARFF format
                    file.write(String.join(",", date, temperature, humidity, pressure, windSpeed, weatherDescription));
                    file.write("\n");
                }
            }
        }
    }

}
