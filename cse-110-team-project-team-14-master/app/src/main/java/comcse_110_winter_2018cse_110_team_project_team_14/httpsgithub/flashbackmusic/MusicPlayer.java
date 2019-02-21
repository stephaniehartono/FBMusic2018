package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class MusicPlayer extends AppCompatActivity {
    static MediaPlayer mediaPlayer;
    ImageButton statusButton;
    Song song;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_view);

        Intent intent = getIntent();
        String yourColor = intent.getStringExtra("desireColor");
        TextView normal = (TextView) findViewById(R.id.normal);
        TextView vibe = (TextView) findViewById(R.id.vibe);
        if(yourColor != null){
            normal.setTypeface(null, Typeface.BOLD);
        }
        else{
            vibe.setTypeface(null, Typeface.BOLD);
        }

        statusButton = (ImageButton) findViewById(R.id.neutral);
        final ImageButton play_btn = (ImageButton) findViewById(R.id.play);

        String fileName = getIntent().getStringExtra("fileName");
        String[] albumQueue = getIntent().getStringArrayExtra("SONGS_TO_QUEUE");
        String[] flashBackQueue = getIntent().getStringArrayExtra("flashBackPlayList");

        // If only a single track is being played
        if(fileName != null) {
            song = new Song(this, fileName);
            setDefaultStatusButton(statusButton, song);

            // Favorite/Dislike/Neutral settings for the song currently playing
            statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeStatusButton(statusButton, song);
                }
            });
        }

        if(albumQueue != null) {
            Song[] trackList = new Song[albumQueue.length];
            for(int i = 0; i < albumQueue.length; i++) {
                trackList[i] = new Song(this, albumQueue[i]);
            }
            playAlbumTracks(trackList);
        }
        else if(fileName != null){
            playSingleTrack(new Song(this, fileName));
        }
        else if(flashBackQueue != null) {
            Song[] trackList = new Song[flashBackQueue.length];
            for(int i = 0; i < flashBackQueue.length; i++) {
                trackList[i] = new Song(this, flashBackQueue[i]);
            }
            playAlbumTracks(trackList);
        }

        // Onclick listener for the play/pause button
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPause(mediaPlayer, play_btn);
            }
        });

    }

    public void setDefaultStatusButton(ImageButton statusButton, Song song) {
        if(song.getStatus() == 0) {
            statusButton.setBackgroundResource(R.drawable.neutral);
        }
        else if(song.getStatus() == 1) {
            statusButton.setBackgroundResource(R.drawable.favor);
        }
        else {
            statusButton.setBackgroundResource(R.drawable.dislike);
        }
    }

    public void changeStatusButton(ImageButton statusButton, Song song) {
        if(song.getStatus() == 0) {
            statusButton.setBackgroundResource(R.drawable.favor);
            song.setStatus(1);
        }
        // If we dislike the song, set the status to -1 and stop playback
        else if(song.getStatus() == 1) {
            statusButton.setBackgroundResource(R.drawable.dislike);
            song.setStatus(-1);
            if(mediaPlayer != null) {
                mediaPlayer.reset();
                finish();
            }
        }
        else if(song.getStatus() == -1){
            statusButton.setBackgroundResource(R.drawable.neutral);
            song.setStatus(0);
        }
    }

    // If the user clicks on an album's play button, the entire album is played
    public void playAlbumTracks(Song[] songs) {
        song = songs[index];
        while(song.getStatus() == -1 && index != songs.length) {
            index++;
            song = songs[index];
        }
        setMetaData(song);
        setDefaultStatusButton(statusButton, song);
        loadMedia(song.getFileName());
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusButton(statusButton, song);
            }
        });
        final Song[] songList = songs;

        // When a song finishes playing, load the next song if it exists
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                index++;
                if(index < songList.length && songList[index].getStatus() != -1) {
                    song = songList[index];
                    setMetaData(song);
                    loadMedia(song.getFileName());
                    statusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            changeStatusButton(statusButton, song);
                        }
                    });
                }
            }
        });
    }

    // If the user clicks on a track from MainActivity, that one song is played
    public void playSingleTrack(final Song song) {
        setMetaData(song);
        loadMedia(song.getFileName());
        
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                ImageButton play_btn =(ImageButton) findViewById(R.id.play);
                play_btn.setBackgroundResource(R.drawable.play);
            }
        });
    }

    // Sets TextViews / Album Image given a song
    public void setMetaData(final Song song) {
        TextView songName = (TextView) findViewById(R.id.song_name);
        TextView albumName = (TextView) findViewById(R.id.album_name);
        ImageView album = (ImageView) findViewById(R.id.album);
        final TextView timelastPlayed = (TextView) findViewById(R.id.timeLastPlayed);
        final TextView locationLang = (TextView) findViewById(R.id.locLang);
        final TextView locationLong = (TextView) findViewById(R.id.locLong);

        long time = getIntent().getLongExtra("TIME", 0);
        double latitude = getIntent().getDoubleExtra("LATITUDE", 0.0);
        double longitude = getIntent().getDoubleExtra("LONGITUDE", 0.0);

        song.setLastPlayedLocLat(latitude);
        song.setLastPlayedLocLong(longitude);
        song.setLastPlayedTime(time);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(time);
        timelastPlayed.setText(String.valueOf(calendar.getTime()));
        locationLang.setText("Lang: " + latitude);
        locationLong.setText("Long: " + longitude);
        DataBase.uploadPlayingHistory(new PlayingHistory(UserInfo.getCurrentUsername(), song));

        songName.setText(song.getTitle());
        albumName.setText(song.getAlbum());

        if(song.getAlbumArt() != null) {
            Bitmap albumArt = BitmapFactory.decodeByteArray(song.getAlbumArt(), 0,
                    song.getAlbumArt().length);
            album.setImageBitmap(albumArt);
        }

    }

    public void playOrPause(MediaPlayer mediaPlayer, ImageButton play_btn) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play_btn.setImageResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            play_btn.setImageResource(R.drawable.pause);
        }
    }

    // Loads the media player with a song
    public void loadMedia(String fileName) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        if(mediaPlayer != null) {
            mediaPlayer.reset();
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                ImageButton play_btn = (ImageButton) findViewById(R.id.play);
                play_btn.setImageResource(R.drawable.pause);
            }
        });

        try {
            mediaPlayer.setDataSource("/storage/emulated/0/Download/" + fileName);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}