package com.example.proekt8;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    public final String TAG = "RRR";
    Button bStart, btJustDoIt;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.btStart);
        btJustDoIt = findViewById(R.id.btJustDoIt);
        imageView = findViewById(R.id.imageView);

        btJustDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ImageDownloadWorker.class).build();
                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);

                WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(workRequest.getId())
                        .observe(MainActivity.this, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                if(workInfo != null && workInfo.getState().isFinished()){
                                    try {
                                        String imageUri = WorkManager.getInstance(getApplicationContext())
                                                .getWorkInfoById(workRequest.getId()).get().getOutputData().getString("imageUri");
                                        Glide.with(MainActivity.this).load(imageUri).into(imageView);
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Work is in progress");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Work finished");
            }
        });
    }
}
