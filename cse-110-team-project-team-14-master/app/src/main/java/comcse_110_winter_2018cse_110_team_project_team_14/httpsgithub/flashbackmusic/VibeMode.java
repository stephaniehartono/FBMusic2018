package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.location.Location;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class VibeMode {
    private static int morning = 8;
    private static int noon = 12;
    private static int night = 20;

    private PriorityQueue<Song> songsPriorityQueue;
    private Calendar curTime;
    private Location curLoc;
    private ArrayList<Song> songList;

    public VibeMode(Calendar curTime, Location curLoc, ArrayList<Song> songList) {
        this.curLoc = curLoc;
        this.curTime = curTime;
        this.songList = songList;
        removeDislike();
        Comparator<Song> songComparator = new SongComparator();
        songsPriorityQueue = new PriorityQueue<Song>(1, songComparator);
        priortize();
    }

    private void removeDislike() {
        for (Song song : songList) {
            if(song.getStatus() == -1){
                songList.remove(song);
            }
        }
    }

    public String[] getSortedPlayList() {
        String[] sortedPlaylist = new String[songsPriorityQueue.size()];
        PriorityQueue<Song> copy = new PriorityQueue<Song>(songsPriorityQueue);
        int i = 0;
        while(!copy.isEmpty()) {
            sortedPlaylist[i] = copy.poll().getFileName();
            i++;
        }
        return sortedPlaylist;
    }

    // gets playlist
    public PriorityQueue<Song> getPlayList() {
        return songsPriorityQueue;
    }

    // gets next song in the play list
    public Song nextSong() {
        Song next = songsPriorityQueue.peek();
        if (next != null) {
            songsPriorityQueue.poll();
        }
        // future updates:
        // maybe do something else if song playlist is empty
        return next;
    }

    // set the weight of each song
    private void priortize (){
        for (Song song : songList) {
            song.setPriority(locWeight(song) + dayWeight(song));
            songsPriorityQueue.add(song);
        }
    }

    // 0 if not in range, 1 if in range
    // if range is within 10 kiliometers
    private int locWeight(Song song) {
        double range = (curLoc.distanceTo(song.getLocation())) / 1000;
        if (range <= 10)
        {
            return 3;
        }
        return 0;
    }

    private int dayWeight (Song song) {
        Calendar musicWeek = Calendar.getInstance();
        musicWeek.setTimeInMillis(song.getLastPlayedTime());
        Calendar currentWeek = Calendar.getInstance();
        Calendar lastWeek = Calendar.getInstance();
        lastWeek.add(Calendar.DATE, -14);

        if (musicWeek.after(lastWeek) && musicWeek.before(currentWeek))
        {
            return 2;
        }
        else {
            return 0;
        }
    }
}