package org.example;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class weatherAPImonthly {
    private static final String API_KEY = "be9a30b14079f875a7ae7e931502ac4a";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/daily";
    private OkHttpClient client;

    public weatherAPImonthly() {
        client = new OkHttpClient();
    }

    public void fetchAndSaveWeatherData(String lat, String lon) throws IOException {
        List<JsonObject> weatherData = getTenDayForecast(lat, lon);
        LocalDate currentDate = LocalDate.now();
        saveWeatherDataToARFF(weatherData, "weather_data_" + currentDate.getYear() + "-" + currentDate.getMonthValue() + ".arff");
    }
    private List<JsonObject> getTenDayForecast(String lat, String lon) throws IOException {
        List<JsonObject> weatherData = new ArrayList<>();

        // Construct the API URL
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("lat", lat)
                .addQueryParameter("lon", lon)
                .addQueryParameter("cnt", "10") // Request forecast for the next 10 days
                .addQueryParameter("appid", API_KEY)
                .build();

        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray forecastList = jsonResponse.getAsJsonArray("list");
                for (JsonElement element : forecastList) {
                    JsonObject forecast = element.getAsJsonObject();
                    weatherData.add(forecast);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherData;
    }

    public void saveWeatherDataToARFF(List<JsonObject> weatherData, String filename) throws IOException {
        try (FileWriter file = new FileWriter(filename)) {
            // Write ARFF header
            file.write("@RELATION forecast_data\n\n");
            file.write("@ATTRIBUTE tarih DATE \"yyyy-MM-dd\"\n");
            file.write("@ATTRIBUTE max_temperature NUMERIC\n");
            file.write("@ATTRIBUTE min_temperature NUMERIC\n");
            file.write("@ATTRIBUTE average_temperature NUMERIC\n");
            file.write("@ATTRIBUTE precipitation NUMERIC\n");
            file.write("@ATTRIBUTE wind_speed NUMERIC\n");
            file.write("@ATTRIBUTE wind_direction STRING\n\n");
            file.write("@DATA\n");

            for (JsonObject forecast : weatherData) {
                long timestamp = forecast.get("dt").getAsLong();
                LocalDate date = LocalDate.ofEpochDay(timestamp / 86400).plusDays(1); // Convert timestamp to date
                double maxTemp = forecast.getAsJsonObject("temp").get("max").getAsDouble();
                double minTemp = forecast.getAsJsonObject("temp").get("min").getAsDouble();
                double avgTemp = (maxTemp + minTemp) / 2.0;
                double precipitation = 0.0; // Default value for precipitation
                if (forecast.has("rain") && !forecast.get("rain").isJsonNull()) {
                    precipitation = forecast.get("rain").getAsDouble(); // Assuming rainfall in mm
                }
                double windSpeed = forecast.get("speed").getAsDouble();
                String windDirection = forecast.get("deg").getAsString();

                // Write a line in ARFF format
                file.write(String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%s\n",
                        date, maxTemp, minTemp, avgTemp, precipitation, windSpeed, windDirection));
            }
        }
    }

    public static void main(String[] args) {
        weatherAPImonthly weatherAPI = new weatherAPImonthly();

        // Test the API for the current month
        try {
            weatherAPI.fetchAndSaveWeatherData("35.6895", "139.6917");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
