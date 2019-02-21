package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;

import static comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic.Song.testSongComparator;
import static org.junit.Assert.*;

public class SongComparatorTest {
    @Before
    public void setUp() {
    }

    @Test
    public void testSongs() {
        Song song1 = new Song("Love Story", 3);
        Song song2 = new Song("You Belong With Me", 5);
        Song song3 = new Song("Blank Space", 1);



        PriorityQueue<Song> songsPriorityQueue = new PriorityQueue<Song>(3, testSongComparator);
        songsPriorityQueue.add(song1);
        songsPriorityQueue.add(song2);
        songsPriorityQueue.add(song3);

        assertEquals("You Belong With Me", songsPriorityQueue.peek().testGetTitle());
        songsPriorityQueue.poll();
        assertEquals("Love Story", songsPriorityQueue.peek().testGetTitle());
        songsPriorityQueue.poll();
        assertEquals("Blank Space", songsPriorityQueue.peek().testGetTitle());

    }

}