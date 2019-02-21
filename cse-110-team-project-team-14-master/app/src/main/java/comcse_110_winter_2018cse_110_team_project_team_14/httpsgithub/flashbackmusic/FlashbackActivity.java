package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;

public class FlashbackActivity extends AppCompatActivity {
    static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashback_layout);

        final ImageButton play_btn = (ImageButton) findViewById(R.id.play);

        //making the priority queue
        //FlashBack flashBack = new FlashBack(Calendar.getInstance(), );

        // Retrieve the song title, album title, and id of the song that was clicked on
        //String songTitle = getIntent().getStringExtra("SONG_NAME");
        //String albumTitle = getIntent().getStringExtra("ALBUM_NAME");
        //final int songId = getIntent().getIntExtra("SONG_ID", -1);
        //byte[] albumImage = getIntent().getByteArrayExtra("ALBUM_ART");
/*
        // Set the song title and album title
        TextView songName = (TextView) findViewById(R.id.song_name);
        TextView albumName = (TextView) findViewById(R.id.album_name);
        ImageView album = (ImageView) findViewById(R.id.album);
        songName.setText(songTitle);
        albumName.setText(albumTitle);

        if(albumImage != null) {
            Bitmap albumArt = BitmapFactory.decodeByteArray(albumImage, 0, albumImage.length);
            album.setImageBitmap(albumArt);
        }

        // Load the song and play the song once the mediaPlayer is ready
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        loadMedia(songId);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                play_btn.setBackgroundResource(R.drawable.pause);
            }
        });

        // Onclick listener for the play/pause button
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPause(mediaPlayer, play_btn);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //flashback button toggle
        ToggleButton flashbackButton = (ToggleButton) findViewById(R.id.returnFromPastButton);
        flashbackButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isFlash) {
                if (!isFlash) {
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    Intent intent = new Intent(getApplicationContext(), MusicPlayer.class);
                    startActivity(intent);
                } else {
                }
            }
        });*/
    }

    public void playOrPause(MediaPlayer mediaPlayer, ImageButton play_btn) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play_btn.setBackgroundResource(R.drawable.play);
        } else {
            mediaPlayer.start();
            play_btn.setBackgroundResource(R.drawable.pause);
        }
    }

    // Loads the media player with a song
    public void loadMedia(int resourceId) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        if(mediaPlayer.isPlaying()) {
            mediaPlayer.release();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
            }
        });

        AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(resourceId);
        try {
            mediaPlayer.setDataSource(assetFileDescriptor);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}