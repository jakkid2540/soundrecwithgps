package com.example.user.myapplication;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Mymediarecorder
{
        public File myRecAudioFile;
        private MediaRecorder mMediaRecorder;
        public boolean isRecording = false;

        public float getMaxAmplitude() {
            if (mMediaRecorder != null) {
                try {
                    return mMediaRecorder.getMaxAmplitude();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    return 0;
                }
            } else {
                return 5;
            }
        }

        public File getMyRecAudioFile() {
            return myRecAudioFile;
        }

        public void setMyRecAudioFile(File myRecAudioFile) {
            this.myRecAudioFile = myRecAudioFile;
        }

        /**
         * Recording
         *
         * @return Whether to start recording successfully
         */
        public boolean startRecorderstartRecorder() {
            if (myRecAudioFile == null) {
                Log.d("DB","e2");
                return false;
            }
            try {
                mMediaRecorder = new MediaRecorder();

                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mMediaRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


                mMediaRecorder.prepare();
                mMediaRecorder.start();
                isRecording = true;
                return true;
            } catch (IOException exception) {
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                isRecording = false;
                exception.printStackTrace();
            } catch (IllegalStateException e) {
                stopRecording();
                e.printStackTrace();
                isRecording = false;
            }
            return false;
        }

        public boolean stopRecording() {
            if (mMediaRecorder != null) {
                if (isRecording) {
                    try {
                        mMediaRecorder.stop();
                        mMediaRecorder.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                mMediaRecorder = null;
                isRecording = false;
            }
            return false;
        }


        public void delete() {
            stopRecording();
            if (myRecAudioFile != null) {
                myRecAudioFile.delete();
                myRecAudioFile = null;
            }
        }

}
