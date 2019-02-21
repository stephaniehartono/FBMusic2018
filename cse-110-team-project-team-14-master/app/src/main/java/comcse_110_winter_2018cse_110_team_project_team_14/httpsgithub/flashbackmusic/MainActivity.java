package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Song> songs;
    final private int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42;
    ListView listview;
    LocationTracker locationTracker;
    static MusicLibrary musicLibrary;
    boolean flashBack;
    public static MusicListAdapter adapter;
    static public Downloader downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ToggleButton vibeModeSwitch = (ToggleButton) findViewById(R.id.vibeToggle);
        downloader = new Downloader(this);
        if (checkPermission()) {
            locationTracker = new LocationTracker(this);
        }
        else {
            requestPermission();
        }

        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);

        musicLibrary = new MusicLibrary(this);
        songs = musicLibrary.getSongs();

        ImageButton time = (ImageButton) findViewById(R.id.clock);
        time.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, TimeActivity.class);
                startActivity(intent);
            }

        });

        //dropdown list to show option for sorting tracks
        Spinner myDropDown = (Spinner) findViewById(R.id.dropDown);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sortList));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myDropDown.setAdapter(myAdapter);

//        myDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemSelected(AdapterView<?>) adapterView, View view, int i, long l){
//                if(i == 0){
//                    startActivity(new Intent(MainActivity.this, TitleSortActivity.class ));
//                }
//                else if(i == 1){
//                    startActivity(new Intent(MainActivity.this, AlbumSortActivity.class ));
//                }
//                else if(i == 2){
//                    startActivity(new Intent(MainActivity.this, ArtistSortActivity.class ));
//                }
//            }
//        });

        //If vibemode is switched on, start playing the playlist
        vibeModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flashBack = b;
                if (flashBack) {
                    Location location = locationTracker.getLocation();
                    VibeMode vibeMode = new VibeMode(Calendar.getInstance(), location, songs);
                    String[] sortedPlayListIds = vibeMode.getSortedPlayList();
                    Intent intent = new Intent(MainActivity.this, MusicPlayer.class);
                    intent.putExtra("flashBackPlayList", sortedPlayListIds);
                    intent.putExtra("desireColor", "bold2");
                    startActivity(intent);
                }
            }
        });

        adapter = new MusicListAdapter(this, songs);
        listview = (ListView) findViewById(R.id.musicList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = songs.get(position);
                if (song.getStatus() != -1 && !vibeModeSwitch.isChecked()) {
                    Location location = locationTracker.getLocation();
                    Intent intent = new Intent(view.getContext(), MusicPlayer.class);
                    intent.putExtra("fileName", song.getFileName());
                    intent.putExtra("TIME", location.getTime());
                    intent.putExtra("LATITUDE", location.getLatitude());
                    intent.putExtra("LONGITUDE", location.getLongitude());
                    intent.putExtra("desireColor", "bold");
                    startActivity(intent);
                }
            }
        });

        Button goToAlbums = (Button) findViewById(R.id.goToAlbums);
        goToAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AlbumActivity.class);
                intent.putExtra("FLASH_BACK_MODE", vibeModeSwitch.isChecked());
                startActivity(intent);
            }
        });


        Button goToDownloads = (Button) findViewById(R.id.goToDownload);
        goToDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DownloadActivity.class);
                startActivity(intent);
            }
        });

        ImageButton logout = (ImageButton) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.putExtra("logout", true);
                startActivity(intent);
            }
        });

        DataBase.retrieveAllPlayingHistory(new DataBaseListener() {
            @Override
            public void onSuccess(ArrayList<PlayingHistory> allPlayingHistory) {
                Gson gson = new Gson();
                Log.d("PlayingHistory", "All playing history: ");
                for (PlayingHistory ph: allPlayingHistory) {
                    Log.d("PlayingHistory", gson.toJson(ph));
                }
            }
        });
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
        }
        else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            locationTracker = new LocationTracker(this);
        }
    }

    static public void downloadCallback() {
        musicLibrary.update();
        songs = musicLibrary.getSongs();
        adapter.notifyDataSetChanged();
    }
}