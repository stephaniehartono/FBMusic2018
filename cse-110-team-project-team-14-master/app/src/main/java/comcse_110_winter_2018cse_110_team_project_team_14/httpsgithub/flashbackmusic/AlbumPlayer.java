package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AlbumPlayer extends AppCompatActivity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_player);

        final String[] idList = getIntent().getStringArrayExtra("ID_LIST");
        final String[] songs = new String[idList.length];

        for(int i = 0; i < idList.length; i++) {
            Song song = new Song(this, idList[i]);
            songs[i] = song.getTitle();
        }

        setMetaData();

        AlbumSongListAdapter adapter = new AlbumSongListAdapter(this, songs, idList);
        ListView listview = (ListView) findViewById(R.id.albumSongListView);
        listview.setAdapter(adapter);

        ImageButton playButton = (ImageButton) findViewById(R.id.playAlbumButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                boolean fmb = getIntent().getBooleanExtra("FLASH_BACK_MODE", false);
                if(!fmb) {
                    intent.putExtra("SONGS_TO_QUEUE", idList);
                    startActivity(intent);
                }
            }
        });
    }

    public void setMetaData() {
        String albumTitle = getIntent().getStringExtra("albumTitle");
        String albumArtist = getIntent().getStringExtra("albumArtist");
        byte[] coverArt = getIntent().getByteArrayExtra("albumArt");

        TextView title = (TextView) findViewById(R.id.titleAlbum);
        TextView artist = (TextView) findViewById(R.id.artistAlbum);
        ImageView art = (ImageView) findViewById(R.id.albumImage);

        title.setText(albumTitle);
        artist.setText(albumArtist);
        if(coverArt != null) {
            art.setImageBitmap(BitmapFactory.decodeByteArray(coverArt, 0, coverArt.length));
        }
    }
}