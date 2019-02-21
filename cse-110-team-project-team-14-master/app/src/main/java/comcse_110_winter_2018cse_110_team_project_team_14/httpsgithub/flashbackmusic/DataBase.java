package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Guangyan_Cai on 3/6/2018.
 */

class DataBaseListener {
    public void onSuccess(ArrayList<PlayingHistory> allPlayingHistory) {}
}

public class DataBase {
    static private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("playing_history");

    static public void uploadPlayingHistory(PlayingHistory playingHistory) {
        myRef.push().setValue(playingHistory);
    }

    static public void retrieveAllPlayingHistory(final DataBaseListener dataBaseListener) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<PlayingHistory> allPlayingHistory = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    PlayingHistory playingHistory = child.getValue(PlayingHistory.class);
                    allPlayingHistory.add(playingHistory);
                }
                dataBaseListener.onSuccess(allPlayingHistory);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DataBase", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
