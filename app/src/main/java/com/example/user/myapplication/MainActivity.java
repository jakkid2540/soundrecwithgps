package com.example.user.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;


public class MainActivity extends AppCompatActivity  {


    GraphView graph_TV;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
    TextView current_TV;
    TextView min_TV;
    TextView max_TV;
    TextView avg_TV;
    Button button;


    float min = 999;
    float max = 0;
    float avg = 0;
    int i = -1;

    private Mymediarecorder mRecorder;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            float a = mRecorder.getMaxAmplitude();
            a = 20 * (float) (Math.log10(a));
            print(String.valueOf(a));
            current_TV.setText(String.valueOf(a));
            print(String.valueOf(i));
            series.appendData(new DataPoint(i, a), true, 10, true);
            graph_TV.addSeries(series);


            if (i <= 8) {
                graph_TV.getViewport().setMinX(0);
                graph_TV.getViewport().setMaxX(8);
            } else {
                graph_TV.getViewport().setMinX(i - 8);
                graph_TV.getViewport().setMaxX(i);

            }
            if (a < min && a > 0) {
                min = a;

            }
            current_TV.setText(String.valueOf(a));
            print("min : " + String.valueOf(min));
            min_TV.setText(String.valueOf(min));
            if (a > max) {
                max = a;
                avg = (a + (avg * i)) / (i + 1);
            }
            print("max :" + String.valueOf(max));
            max_TV.setText(String.valueOf(max));
            print("avg :" + String.valueOf(avg));
            avg_TV.setText(String.valueOf(avg));
            i++;
            // lat lng


            start();


        }

    };

    private void start() {
        handler.postDelayed(runnable, 1000);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        File file = FileUtil.createFile();
        if (file != null) {
            mRecorder.setMyRecAudioFile(file);
            if (mRecorder.startRecorderstartRecorder()) {
                start();
                print("started");
            } else {
                print("error1");
            }
        } else {
            print("error2");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123
            );

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermissionToRecordAudio();

        graph_TV = findViewById(R.id.graph);
        current_TV = findViewById(R.id.current);
        min_TV = findViewById(R.id.min);
        max_TV = findViewById(R.id.max);
        avg_TV = findViewById(R.id.avg);
        button = findViewById(R.id.button);


        graph_TV.getViewport().setXAxisBoundsManual(true);
        graph_TV.getViewport().setYAxisBoundsManual(true);
        graph_TV.getViewport().setMaxY(100);
        graph_TV.getViewport().setMinY(0);
        graph_TV.getViewport().setMaxXAxisSize(10);

        mRecorder = new Mymediarecorder();




    }


    //public void click(View view) {
//        Random random = new Random();
//        int a = random.nextInt(100);
//        print(String.valueOf(a));
//
//        series.appendData(new DataPoint(i,a),true, 10, true);
//        graph_TV.addSeries(series);
//
//        if (i <= 8) {
//            graph_TV.getViewport().setMinX(0);
//            graph_TV.getViewport().setMaxX(8);
//        }
//        else {
//            graph_TV.getViewport().setMinX(i-8);
//            graph_TV.getViewport().setMaxX(i);
//        }
//        setValue(a);
//    }
//    public void setValue(int val){
//        if (val < min ){
//            min = val;
//        }
//        print("min :" + String.valueOf(min));
//        current_TV.setText(String.valueOf(val));
//        min_TV.setText(String.valueOf(min));
//        if (val > max) {
////            max = val ;
////        }
////        avg = (val+(avg*i))/(i+1);
////        i++;
////
////        print("max :" + String.valueOf(max));
////        max_TV.setText(String.valueOf(max));
////        print("avg :" + String.valueOf(avg));
////        avg_TV.setText(String.valueOf(avg));
//
//    }

    public void print(String message) {
        Log.d("DB", message);
    }

    public void click(View view) {
//

        if (mRecorder.isRecording == Boolean.parseBoolean(String.valueOf(true))) {


            mRecorder.stopRecording();
            stop();

        } else {

            mRecorder.startRecorderstartRecorder();
            start();


        }


        Log.d("popeye", String.valueOf(mRecorder.isRecording));


    }


    private void stop() {
        handler.removeCallbacksAndMessages(null);
    }



}

