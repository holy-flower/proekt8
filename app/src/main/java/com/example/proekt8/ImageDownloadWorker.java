package com.example.proekt8;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.Data;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ImageDownloadWorker extends Worker {

    public ImageDownloadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        String imageUri = null;
        try {
            URL url = new URL("https://random.dog/woof.json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String response = scanner.next();
                JSONObject jsonObject = new JSONObject(response);
                imageUri = jsonObject.getString("url");
            } else {
                return Result.failure();
            }
        } catch (Exception e) {
            return Result.failure();
        }

        Data outputData = new Data.Builder()
                .putString("imageUri", imageUri)
                .build();

        return Result.success(outputData);
    }
}
