package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumSongListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] songs;
    private final String[] fileNames;

    public AlbumSongListAdapter(Activity context, String[] songs, String[] fileNames) {
        super(context, R.layout.album_player, songs);
        this.context = context;
        this.songs = songs;
        this.fileNames = fileNames;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.album_song_view, null, true);

        final Song song = new Song(context, fileNames[position]);
        final ImageView statusButton = (ImageView) rowView.findViewById(R.id.statusButton);

        TextView songTitle = (TextView) rowView.findViewById(R.id.albumSongTitle);
        songTitle.setText(song.getTitle());
        setDefaultStatusButton(statusButton, song);

        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatusButton(statusButton, song);
            }
        });

        return rowView;
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
}