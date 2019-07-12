package com.craterzone.videomergelib;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class VideoMerger {
    private String workingPath;
    private ArrayList<String> videoList;
    private AsyncTask<String, Integer, String> mergeVideos;
    private Activity activity;
    private VideoMergeCallbackListener callBack;

    private VideoMerger(Activity activity, VideoMergeCallbackListener callBack) {
        this.activity = activity;
        this.callBack = callBack;

    }

    public static VideoMerger newInstance(Activity activity, VideoMergeCallbackListener callBack) {

        return new VideoMerger(activity, callBack);
    }

    @SuppressWarnings("")
    public void merge(String dirPath) {
        this.videoList = new ArrayList();
        File dir = new File(dirPath);
        File file[] = dir.listFiles();
        for (File mfile : file) {
            videoList.add(mfile.getAbsolutePath());
        }
        //Set the working path
        this.workingPath = dirPath;
        mergeVideos = new MergeVideos(workingPath, videoList).execute();

    }

    @SuppressWarnings("")
    public void merge(String dirPath, ArrayList<String> videoFileList) {
        this.videoList = new ArrayList();
        for (String mfile : videoFileList) {
            videoList.add(mfile);
        }
        //Set the working path
        this.workingPath = dirPath;
        mergeVideos = new MergeVideos(workingPath, videoList).execute();
    }

    public void cancel() {
        if (mergeVideos != null) {
            mergeVideos.cancel(true);
        }
    }

    private void deleteFiles() {
        for (String filePath : videoList) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }

    }

    class MergeVideos extends AsyncTask<String, Integer, String> {

        //The working path where the video files are located
        private String workingPath;
        //The file names to merge
        private ArrayList<String> videosToMerge;
        //Dialog to show to the user
        private ProgressDialog progressDialog;


        private MergeVideos(String workingPath, ArrayList<String> videosToMerge) {
            this.workingPath = workingPath;
            this.videosToMerge = videosToMerge;
        }

        @Override
        protected void onPreExecute() {


            progressDialog = ProgressDialog.show(activity,
                    "", "Loading...", true);


        }

        @Override
        protected String doInBackground(String... params) {
            int count = videosToMerge.size();
            try {
                Movie[] inMovies = new Movie[count];
                for (int i = 0; i < count; i++) {
                    File file = new File(videosToMerge.get(i));
                    if (file.exists()) {
                        // FileInputStream fis = new FileInputStream(file);
                        // FileChannel fc = fis.getChannel();
                        inMovies[i] = MovieCreator.build(file.getPath());
                        //fis.close();
                        // fc.close();
                    }
                }
                List<Track> videoTracks = new LinkedList<Track>();
                List<Track> audioTracks = new LinkedList<Track>();

                for (Movie m : inMovies) {
                    for (Track t : m.getTracks()) {
                        if (t.getHandler().equals("soun")) {
                            audioTracks.add(t);
                        }
                        if (t.getHandler().equals("vide")) {
                            videoTracks.add(t);
                        }
                        if (t.getHandler().equals("")) {

                        }
                    }
                }

                Movie result = new Movie();

                if (audioTracks.size() > 0) {
                    result.addTrack(new AppendTrack(audioTracks
                            .toArray(new Track[audioTracks.size()])));
                }
                if (videoTracks.size() > 0) {
                    result.addTrack(new AppendTrack(videoTracks
                            .toArray(new Track[videoTracks.size()])));
                }
                Container out = new DefaultMp4Builder()
                        .build(result);

                //rotate video

                // out.getMovieBox().getMovieHeaderBox().setMatrix(ROTATE_270);

                long timestamp = new Date().getTime();
                String timestampS = "" + timestamp;

                File storagePath = new File(workingPath);
                if (!storagePath.exists())
                    storagePath.mkdirs();

                File myMovie = new File(storagePath, "tempVideo_" + (new Date().getTime()) + ".mp4");
                if (myMovie.exists()) {
                    myMovie.delete();
                    myMovie.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(myMovie);
                FileChannel fco = fos.getChannel();

                fco.position(0);
                out.writeContainer(fco);
                // out.getBoxes();
                fco.close();
                fos.close();

                return myMovie.getAbsolutePath();

            } catch (FileNotFoundException e) {
                Log.d("VideoMerger", "exception:", e);
            } catch (IOException e) {
                Log.d("VideoMerger", "exception:", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String filePath) {
            if (progressDialog != null)
                progressDialog.dismiss();
            callBack.onVideoMerge(filePath);
            // deleteFiles();
        }

    }
}
