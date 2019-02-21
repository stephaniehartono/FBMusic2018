package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public class AlbumActivity extends AppCompatActivity{
    private MusicLibrary musicLibrary;
    private HashMap<String, Album> albums;

    @Override
    public void onCreate(Bundle savedInstanceStace) {
        super.onCreate(savedInstanceStace);
        setContentView(R.layout.album_view);
        musicLibrary = new MusicLibrary(this);

        albums = musicLibrary.getAlbums();

        final String[] albumTitles = new String[albums.size()];
        final String[] albumArtists = new String[albums.size()];
        final byte[][] coverArt = new byte[albums.size()][];

        int i = 0;
        for(Album album : albums.values()) {
            albumTitles[i] = album.getTitle();
            albumArtists[i] = album.getArtist();
            for(Song songValue : album.getSongs().values()) {
                coverArt[i] = songValue.getAlbumArt();
                break;
            }
            i++;
        }

        AlbumListAdapter adapter = new AlbumListAdapter(this, albumTitles, albumArtists, coverArt);
        ListView listview = (ListView) findViewById(R.id.albumList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Album album = albums.get(albumTitles[i]);
                Map<String, Song> songs = album.getSongs();
                // List of ids of the songs that belong to this album
                String[] fileNamesOfSongsInAlbum = new String[album.getSongs().size()];
                int index = 0;
                for(Song song : songs.values()) {
                    fileNamesOfSongsInAlbum[index] = song.getFileName();
                    index++;
                }
                Intent intent = new Intent(view.getContext(), AlbumPlayer.class);
                intent.putExtra("ID_LIST", fileNamesOfSongsInAlbum);
                intent.putExtra("albumArt", coverArt[i]);
                intent.putExtra("albumTitle", album.getTitle());
                intent.putExtra("albumArtist", album.getArtist());
                intent.putExtra("FLASH_BACK_MODE", getIntent().getBooleanExtra("FLASH_BACK_MODE", false));

                startActivity(intent);
            }
        });

        Button goToSongs = (Button) findViewById(R.id.goToSongs);
        goToSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}