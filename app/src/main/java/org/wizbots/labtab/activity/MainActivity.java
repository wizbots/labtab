package org.wizbots.labtab.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.craterzone.logginglib.manager.LoggerManager;

import org.wizbots.labtab.R;

import java.io.File;

import life.knowledge4.videotrimmer.utils.FileUtils;

public class MainActivity extends ParentActivity {

    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String EXTRA_VIDEO_PATH = "EXTRA_VIDEO_PATH";
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static Context ActivityContext = null;
    private Uri fileUri;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {

        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {

        if (Environment.getExternalStorageState() == null) {
            return null;
        }
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MyCameraVideo");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                //output.setText("Failed to create directory MyCameraVideo.");

                Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
    /*    java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());*/

        File mediaFile;

        if (type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + System.currentTimeMillis() + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityContext = getBaseContext();

        findViewById(R.id.btn_record_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                // create a file to save the video
                fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

                // set the image file name
                if (fileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                }
                // set the video image quality to high
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra("android.intent.extra.durationLimit", 300);

                // start the Video Capture Intent
                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            }
        });

        findViewById(R.id.btn_merge_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityContext, VideoMergeActivity.class));
            }
        });

        findViewById(R.id.btn_send_diagnostic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoggerManager.getInstance(MainActivity.this).sendDiagnostic(MainActivity.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // After camera screen this code will excuted

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                // output.setText("Video File : " + data.getData());

                // Video captured and saved to fileUri specified in the Intent
                if (fileUri != null) {
                    final Uri selectedUri = fileUri;
                    if (selectedUri != null) {
                        startTrimActivity(selectedUri);
                    } else {
                        Toast.makeText(ActivityContext, "Video saved to: " + data.getData(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                //  output.setText("User cancelled the video capture.");

                // User cancelled the video capture
                Toast.makeText(ActivityContext, "User cancelled the video capture.",
                        Toast.LENGTH_LONG).show();

            } else {

                // output.setText("Video capture failed.");

                // Video capture failed, advise user
                Toast.makeText(ActivityContext, "Video capture failed.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(ActivityContext, TrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(ActivityContext, uri));
        startActivity(intent);
    }

}
