package org.wizbots.labtab.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.craterzone.videomergelib.VideoMergeCallbackListener;
import com.craterzone.videomergelib.VideoMerger;

import org.wizbots.labtab.R;

import java.io.File;
import java.util.ArrayList;

import life.knowledge4.videotrimmer.utils.FileUtils;

public class VideoMergeActivity extends ParentActivity implements VideoMergeCallbackListener {

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    private String filemanagerstring;
    private String selectedImagePath;
    private VideoMerger videoMerger;
    private Button btnPlay;
    private ArrayList<String> videoFilesList = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_merge);

        listView = (ListView) findViewById(R.id.video_file_list);
        findViewById(R.id.btn_add_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVideoFromGallery();
            }
        });

        findViewById(R.id.btn_merge_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mergeVideos();

            }
        });

        btnPlay = (Button) findViewById(R.id.btn_play_video);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VideoMergeActivity.this, VideoPlayerActivity.class);
                intent.putExtra("videoPath", videoFilesList.get(0));
                startActivity(intent);
            }
        });

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, videoFilesList);
        listView.setAdapter(listAdapter);
    }

    private void mergeVideos() {
        if (videoFilesList.size() < 2) {
            Toast.makeText(this, "must be two files to merge", Toast.LENGTH_SHORT).show();
            return;
        }
        File dir = new File(Environment.getExternalStorageDirectory(), "LabTabMergeVideos");
        if (!dir.exists()) {
            dir.mkdir();
        }
        videoMerger = VideoMerger.newInstance(VideoMergeActivity.this, this);
        //videoMerger.merge(Environment.getExternalStorageDirectory()+"/MyCameraVideo");
        videoMerger.merge(dir.getAbsolutePath(), videoFilesList);
    }

    private void getVideoFromGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

            /*    // OI FILE Manager
                filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                selectedImagePath = getPath(selectedImageUri);*/

                selectedImagePath = FileUtils.getPath(VideoMergeActivity.this, selectedImageUri);
                if (selectedImagePath != null) {
                    videoFilesList.add(selectedImagePath);
                    listAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    @Override
    public void onVideoMerge(String filePath) {
        if (videoMerger != null) {
            videoMerger.cancel();
        }
        btnPlay.setVisibility(View.VISIBLE);
        videoFilesList.clear();
        videoFilesList.add(filePath);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoMerger != null) {
            videoMerger.cancel();
        }
    }
}
