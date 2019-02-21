package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicListAdapter extends ArrayAdapter {

    private Activity context;
    private ArrayList<Song> songs;
    private MusicLibrary musicLibrary;

    public MusicListAdapter(Activity context, ArrayList<Song> songs) {
        super(context, R.layout.music_view, songs);
        this.context = context;
        this.songs = songs;
        this.musicLibrary = new MusicLibrary(context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.music_view, null,true);
        final ImageView statusButton = (ImageView) rowView.findViewById(R.id.statusButton);

        final Song song = songs.get(position);

        TextView titleTextField = (TextView) rowView.findViewById(R.id.songTitle);
        TextView artistTextField = (TextView) rowView.findViewById(R.id.artist);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.albumCover);

        titleTextField.setText(song.getTitle());
        artistTextField.setText(song.getArtist());
        byte[] albumArtArray = song.getAlbumArt();

        // If the song has an album image, set the image view as the image
        if(albumArtArray != null) {
            Bitmap albumImage = BitmapFactory.decodeByteArray(albumArtArray, 0, albumArtArray.length);
            imageView.setImageBitmap(albumImage);
        }

        setDefaultStatusButton(statusButton, song);

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusButton(statusButton, song);
            }
        });

        return rowView;
    }

    public void setDefaultStatusButton(ImageView statusButton, Song song) {
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

    public void changeStatusButton(ImageView statusButton, Song song) {
        if(song.getStatus() == 0) {
            statusButton.setBackgroundResource(R.drawable.favor);
            song.setStatus(1);
        }
        else if(song.getStatus() == 1) {
            statusButton.setBackgroundResource(R.drawable.dislike);
            song.setStatus(-1);
        }
        else if(song.getStatus() == -1){
            statusButton.setBackgroundResource(R.drawable.neutral);
            song.setStatus(0);
        }
    }
}