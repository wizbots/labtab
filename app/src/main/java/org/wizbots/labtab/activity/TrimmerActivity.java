package org.wizbots.labtab.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.craterzone.videomergelib.VideoMergeCallbackListener;
import com.craterzone.videomergelib.VideoMerger;

import org.wizbots.labtab.R;
import org.wizbots.labtab.util.ResourceClass;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

public class TrimmerActivity extends ParentActivity implements OnTrimVideoListener, OnK4LVideoListener, VideoMergeCallbackListener {

    private K4LVideoTrimmer mVideoTrimmer;
    private ProgressDialog mProgressDialog;
    private VideoMerger videoMerger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trimmer);

        // ResourceClass.deleteDirectory();

        Intent extraIntent = getIntent();
        String path = "";
        OnK4LVideoListener lVideoListener;

        if (extraIntent != null) {
            path = extraIntent.getStringExtra(MainActivity.EXTRA_VIDEO_PATH);
        }

        //setting progressbar
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Trimminng....");

        mVideoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));
        if (mVideoTrimmer != null) {
            mVideoTrimmer.setMaxDuration(300);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnK4LVideoListener(this);
          /*  File dir =new File(Environment.getExternalStorageDirectory(),"LabTabVideo/");
            if(!dir.exists()){
                dir.mkdir();
            }*/
            mVideoTrimmer.setDestinationPath("/storage/emulated/0/LabTabVideo/");
            mVideoTrimmer.setVideoURI(Uri.parse(path));
            mVideoTrimmer.setVideoInformationVisibility(true);
        }
    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri uri) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mergeVideo();
                Log.d("TrimmerActivity", uri.getPath());
                Toast.makeText(TrimmerActivity.this, "Video Captured", Toast.LENGTH_SHORT).show();
//                Toast.makeText(TrimmerActivity.this, "video saved at :" + uri.getPath(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent();
        intent.putExtra("URI", uri);
        setResult(RESULT_OK, intent);
       /* Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndType(uri, "video/mp4");
        startActivity(intent);*/
        finish();
    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        mVideoTrimmer.destroy();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimmerActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mergeVideo() {
        videoMerger = VideoMerger.newInstance(TrimmerActivity.this, this);
        videoMerger.merge(ResourceClass.getVideoDirectory());
    }

    @Override
    public void onVideoMerge(String filePath) {
        Toast.makeText(this, "file path:" + filePath, Toast.LENGTH_SHORT).show();
    }
}
