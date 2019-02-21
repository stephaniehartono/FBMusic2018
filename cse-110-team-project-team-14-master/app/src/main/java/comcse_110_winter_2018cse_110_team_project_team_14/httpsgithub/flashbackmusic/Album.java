package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import java.util.HashMap;
import java.util.Map;

public class Album  {
    Map<String, Song> songs;
    private String title, artist;

    public Album(String title, String artist) {
        this.title = title;
        this.artist = artist;
        songs = new HashMap<String, Song>();
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public Map<String, Song> getSongs() {
        return this.songs;
    }

    public void addSong(Song song) {
        songs.put(song.getTitle(), song);
    }
}
