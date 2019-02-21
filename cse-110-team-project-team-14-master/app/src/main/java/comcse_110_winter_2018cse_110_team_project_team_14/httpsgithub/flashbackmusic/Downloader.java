package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.Manifest;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Downloader {

    private Activity activity;
    private String path = "/storage/emulated/0/Download/";
    private boolean zip;
    private String fileName;
    private String url;
    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private final String TAG = "Downloader";
    private MusicLibrary musicLibrary;

    public Downloader(Activity activity) {
        this.activity = activity;
        this.musicLibrary = new MusicLibrary(activity);
        checkPermissions();
    }

    // Returns the file name of the file downloaded
    public void download(String url) {
        checkPermissions();
        // Once a download is complete, check if the file is a zip file. Extract if is.

        final DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        final String filename = URLUtil.guessFileName(url, null, "audio/MP3");
        fileName = filename;

        request.allowScanningByMediaScanner();

        // Check the type of file we're about to download
        if(filename.contains(".zip")) {
            request.setMimeType("application/zip");
            zip = true;
        }
        else if(filename.contains(".mp3")) {
            zip = false;
            request.setMimeType("audio/MP3");
        }
        // If the file to be downloaded is not an mp3/zip file, do not download
        else {
            Log.d(TAG, "Not valid file type");
            return;
        }

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        request.setTitle(filename);
        final String source = url;
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ArrayList<Song> songsDownloaded = new ArrayList<>();
                if (zip) {
                    songsDownloaded.addAll(unpackZip(path, filename));
                }
                else {
                    songsDownloaded.add(new Song(activity, filename));
                }
                for (Song song : songsDownloaded) {
                    song.setSource(source);
                }
                Log.d(TAG, "Downloaded: " + filename);
                Toast toast = Toast.makeText(activity,
                        "Downloaded: " + filename,
                        Toast.LENGTH_LONG);
                toast.show();
                MainActivity.downloadCallback();
            }
        };
        activity.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Log.d(TAG, "Downloading: " + filename);
        Toast toast = Toast.makeText(activity,
                "Downloading: " + filename,
                Toast.LENGTH_LONG);
        toast.show();
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(activity, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    100);
            return false;
        }
        return true;
    }

    // Slightly modified code from:
    // https://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
    private ArrayList<Song> unpackZip(String path, String zipname) {
        ArrayList<Song> songs = new ArrayList<>();
        File file;
        InputStream is;
        ZipInputStream zis;
        Log.d(TAG, "Extracting zip file: " + zipname);
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            file = new File(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();
                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                songs.add(new Song(activity, filename));
                Log.d(TAG, "Extracted file: " + filename);
                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }

        catch(Exception e) {
            e.printStackTrace();
            return songs;
        }

        // Delete the zip file as we no longer need it
        file.delete();
        Log.d(TAG, "Finished extracting zip file: " + zipname);
        return songs;
    }
}