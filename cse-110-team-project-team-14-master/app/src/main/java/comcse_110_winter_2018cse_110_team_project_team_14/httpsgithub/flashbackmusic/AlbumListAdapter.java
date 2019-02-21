package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Randy on 2/17/2018.
 */

public class AlbumListAdapter extends ArrayAdapter {
    private final Activity context;
    private final String[] albumArtist;
    private final String[] albumTitles;
    private final byte[][] coverArt;

    public AlbumListAdapter(Activity context, String[] albumTitles, String[] albumArtist, byte[][] coverArt) {
        super(context, R.layout.album_view, albumTitles);

        this.context = context;
        this.albumArtist = albumArtist;
        this.albumTitles = albumTitles;
        this.coverArt = coverArt;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.album_list, null,true);

        TextView titleTextField = (TextView) rowView.findViewById(R.id.albumTitle);
        TextView artistTextField = (TextView) rowView.findViewById(R.id.albumArtist);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.albumCoverArt);

        if(coverArt[position] != null) {
            Bitmap albumArt = BitmapFactory.decodeByteArray(coverArt[position], 0, coverArt[position].length);
            imageView.setImageBitmap(albumArt);
        }

        titleTextField.setText(albumTitles[position]);
        artistTextField.setText(albumArtist[position]);

        return rowView;
    }
}