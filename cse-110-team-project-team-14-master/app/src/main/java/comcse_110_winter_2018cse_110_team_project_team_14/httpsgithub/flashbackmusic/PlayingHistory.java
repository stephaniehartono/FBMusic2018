package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import java.io.Serializable;

/**
 * Created by Guangyan_Cai on 3/6/2018.
 */

public class PlayingHistory implements Serializable {
    public String username;
    public double lastPlayedLocLat, lastPlayedLocLong;
    public long lastPlayedTime;
    public String source, title, artist, album;

    public PlayingHistory() {}

    public PlayingHistory(String username, Song song) {
        this.username = username;
        lastPlayedLocLat = song.getLastPlayedLocLat();
        lastPlayedLocLong = song.getLastPlayedLocLong();
        lastPlayedTime = song.getLastPlayedTime();
        source = song.getSource();
        title = song.getTitle();
        artist = song.getArtist();
        album = song.getAlbum();
    }
}
