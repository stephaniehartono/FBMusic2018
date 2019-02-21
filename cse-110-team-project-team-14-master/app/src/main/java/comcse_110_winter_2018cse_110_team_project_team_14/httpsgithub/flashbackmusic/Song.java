package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Base64;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;


/**
 * Song class used to store information about a song.
 * Written by: Randy Chea
 */

public class Song {
    // status == 1 -> favorited
    // status == 0 -> neutral
    // status == -1 -> disliked
    private int status = 0;
    private String fileName, title, artist, album, genre =  "Unknown";
    private byte[] albumArt;
    private boolean played;
    private double lastPlayedLocLat, lastPlayedLocLong;
    private long lastPlayedTime;
    private int priority;
    private Uri mediaPath;
    private String source = "somewhere";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Constructor using a directory path
    public Song(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        mediaPath = Uri.parse("/storage/emulated/0/Download/" + fileName);
        editor = sharedPreferences.edit();

        if (!sharedPreferences.getBoolean("created", false)) {
            extractRaw();
            editor.putBoolean("created", true);
            editor.apply();
            updateSharePreference();
        }
        else {
            extractSharePreference();
        }
    }

    // This constructor is for testing only
    public Song(String title, String artist, String album, int status) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.status = status;
    }

    public Song(String title, int priority){
        this.title = title;
        this.priority = priority;
    }

    private void extractRaw() {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(context, mediaPath);
        title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        album = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        artist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        genre = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
        albumArt = metaRetriever.getEmbeddedPicture();
        played = false;
        lastPlayedLocLat = 0;
        lastPlayedLocLong = 0;
        lastPlayedTime = 0;
        priority = 0;

        if(artist == null) {
            artist = "Unknown";
        }
    }

    private void extractSharePreference() {
        fileName = sharedPreferences.getString("fileName", "");
        status = sharedPreferences.getInt("status", 0);
        title = sharedPreferences.getString("title", "");
        album = sharedPreferences.getString("album", "");
        artist = sharedPreferences.getString("artist", "");
        genre = sharedPreferences.getString("genre", "");
        String albumArtString = sharedPreferences.getString("albumArt", "");
        if (!albumArtString.equals("")) {
            albumArt = Base64.decode(albumArtString, Base64.DEFAULT);
        }
        played = sharedPreferences.getBoolean("played", played);
        lastPlayedLocLat = sharedPreferences.getFloat("lastPlayedLocLat", 0);
        lastPlayedLocLong = sharedPreferences.getFloat("lastPlayedLocLong", 0);
        lastPlayedTime = sharedPreferences.getLong("lastPlayedTime", 0);
        priority = sharedPreferences.getInt("priority", 0);
        source = sharedPreferences.getString("source", source);
    }

    private void updateSharePreference() {
        editor.putString("fileName", fileName);
        editor.putInt("status", status);
        editor.putString("title", title);
        editor.putString("album", album);
        editor.putString("artist", artist);
        editor.putString("genre", genre);
        if (albumArt != null) {
            editor.putString("albumArt", Base64.encodeToString(albumArt, Base64.DEFAULT));
        }
        editor.putBoolean("played", played);
        editor.putFloat("lastPlayedLocLat", (float) lastPlayedLocLat);
        editor.putFloat("lastPlayedLocLong", (float) lastPlayedLocLong);
        editor.putLong("lastPlayedTime", lastPlayedTime);
        editor.putInt("priority", priority);
        editor.putString("source", source);
        editor.apply();
    }

    public void setLastPlayedLocLat(double latitude) {
        lastPlayedLocLat = latitude;
        updateSharePreference();
    }

    public void setLastPlayedLocLong(double longitude) {
        lastPlayedLocLong = longitude;
        updateSharePreference();
    }

    public void setLastPlayedTime(long time) {
        lastPlayedTime = time;
        updateSharePreference();
    }

    public void setStatus(int status) {
        this.status = status;
        updateSharePreference();
    }

    public void setPriority(int priority) {
        this.priority = priority;
        updateSharePreference();
    }

    public int getStatus() {
        extractSharePreference();
        return status;
    }

    public byte[] getAlbumArt() {
        extractSharePreference();
        return albumArt;
    }

    public String getFileName() {
        extractSharePreference();
        return fileName;
    }

    public String getTitle() {
        extractSharePreference();
        return title;
    }

    public String getArtist() {
        extractSharePreference();
        return artist;
    }

    public String getAlbum() {
        extractSharePreference();
        return album;
    }

    public String getGenre() {
        extractSharePreference();
        return genre;
    }

    public double getLastPlayedLocLat() {
        extractSharePreference();
        return lastPlayedLocLat;
    };


    public double getLastPlayedLocLong() {
        extractSharePreference();
        return lastPlayedLocLong;
    };

    public long getLastPlayedTime() {
        return lastPlayedTime;
    };

    public Calendar getCalendar() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(lastPlayedTime);
        return calendar;
    }

    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(lastPlayedLocLat);
        location.setLongitude(lastPlayedLocLong);
        return location;
    }

    public int getPriority() {
        extractSharePreference();
        return priority;
    }

    public Uri getUri() {
        extractSharePreference();
        return mediaPath;
    }

    public void setSource(String source) {
        this.source = source;
        updateSharePreference();
    }

    public String getSource() {
        extractSharePreference();
        return source;
    }

    public int compareTo(Song compareSong) {
        return 0;
    }

    public String testGetTitle() {
        return title;
    }

    public String testGetArtist() {
        return artist;
    }

    public String testGetAlbum() {
        return album;
    }

    public int testGetLike() {
        return status;
    }

    public int testGetPriority() {
        return priority;
    }

    // sorts title
    public static Comparator<Song> TitleSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            String song1 = o1.getTitle();
            String song2 = o2.getTitle();
            return song1.compareTo(song2);
        }
    };

    // sorts album
    public static Comparator<Song> AlbumSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            String song1 = o1.getAlbum();
            String song2 = o2.getAlbum();
            return song1.compareTo(song2);
        }
    };

    // artist
    public static Comparator<Song> ArtistSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            String song1 = o1.getArtist();
            String song2 = o2.getArtist();
            return song1.compareTo(song2);
        }
    };

    // sorts favorite
    public static Comparator<Song> LikeSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            int song1 = o1.getStatus();
            int song2 = o2.getStatus();
            return song2 - song1;
        }
    };



    // sorts title
    public static Comparator<Song> TitleTestSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            String song1 = o1.testGetTitle();
            String song2 = o2.testGetTitle();
            return song1.compareTo(song2);
        }
    };

    // sorts album
    public static Comparator<Song> AlbumTestSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            String song1 = o1.testGetAlbum();
            String song2 = o2.testGetAlbum();
            return song1.compareTo(song2);
        }
    };

    // artist
    public static Comparator<Song> ArtistTestSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            String song1 = o1.testGetArtist();
            String song2 = o2.testGetArtist();
            return song1.compareTo(song2);
        }
    };

    // sorts favorite
    public static Comparator<Song> LikeTestSorter = new Comparator<Song>() {
        @Override
        public int compare(Song o1, Song o2) {
            int song1 = o1.testGetLike();
            int song2 = o2.testGetLike();

            if (song1 == song2) {
                return o1.testGetTitle().compareTo(o2.testGetTitle());
            }
            return song2 - song1;
        }
    };

    public static Comparator<Song> testSongComparator = new Comparator<Song>() {
        @Override
        public int compare(Song x, Song y) {
            return y.testGetPriority() - x.testGetPriority();
        }
    };
}
