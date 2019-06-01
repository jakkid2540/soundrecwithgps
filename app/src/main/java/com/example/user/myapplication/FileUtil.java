package com.example.user.myapplication;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    public  static File createFile() {
        File root= android.os.Environment.getExternalStorageDirectory();
        File myCaptureFile = new File(root.getAbsolutePath() + "/myApp/Audios/buffer.mp3");

        if (myCaptureFile.exists()){
            myCaptureFile.delete();
        }
        try {
            myCaptureFile.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }
        return myCaptureFile;
    }
}
