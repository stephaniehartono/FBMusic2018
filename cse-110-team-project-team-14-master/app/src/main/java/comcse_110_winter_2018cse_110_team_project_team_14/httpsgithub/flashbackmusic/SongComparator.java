package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import java.util.Comparator;

public class SongComparator implements Comparator<Song> {
    @Override
    public int compare(Song x, Song y) {
        return y.getPriority() - x.getPriority();
    }
}

