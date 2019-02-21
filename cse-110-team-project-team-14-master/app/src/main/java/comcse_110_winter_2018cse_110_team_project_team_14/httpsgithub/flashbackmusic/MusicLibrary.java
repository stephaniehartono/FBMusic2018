package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MusicLibrary {
    private Context context;
    private HashMap<String, Album> albums = new HashMap<>();
    private ArrayList<Song> songs = new ArrayList<>();


    public MusicLibrary(Context context) {
        this.context = context;
        update();
    }

    private void createSongs() {
        File songDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if(songDirectory != null && songDirectory.listFiles() != null) {
            for (File file : songDirectory.listFiles()) {
                String fileName = file.getName();
                if (!fileName.contains("zip")) {
                    songs.add(new Song(context, fileName));
                }
            }
        }
    }

    private void createAlbums() {
        for (Song song : songs) {
            if(albums.containsKey(song.getAlbum())) {
                albums.get(song.getAlbum()).addSong(song);
            }
            else {
                Album album = new Album(song.getAlbum(), song.getArtist());
                albums.put(song.getAlbum(), album);
            }
        }
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public HashMap<String, Album> getAlbums() {
        return albums;
    }

    public void addSongs(ArrayList<Song> newSongs) {
        songs.addAll(newSongs);
    }

    public void update() {
        songs.clear();
        albums.clear();
        createSongs();
        createAlbums();
    }
}
