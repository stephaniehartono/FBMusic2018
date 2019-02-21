package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.proto.action.ViewActions;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import static comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic.Song.AlbumTestSorter;
import static comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic.Song.ArtistTestSorter;
import static comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic.Song.LikeTestSorter;
import static comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic.Song.TitleSorter;
import static comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic.Song.TitleTestSorter;
import static junit.framework.Assert.assertEquals;

public class SorterTester {

    @Before
    public void setUp() {
    }


    //@Test(expected = Exception.class)

    // Testing an empty list of strings to see if there will be an exception
    @Test
    public void testEmptySongList() {
        Song[] songs = new Song[0];
        try {
            Arrays.sort(songs, TitleTestSorter);
        } catch (Exception e) {
            assertEquals("","1");
        }

    }

    @Test
    public void testOneSong() {
        Song song1 = new Song("Blank Space", "Taylor Swift", "1989", 1);
        Song[] songs = {song1};

        Arrays.sort(songs, TitleTestSorter);
        assertEquals("Blank Space", songs[0].testGetTitle());

        Arrays.sort(songs, ArtistTestSorter);
        assertEquals("Taylor Swift", songs[0].testGetArtist());

        Arrays.sort(songs, AlbumTestSorter);
        assertEquals("1989", songs[0].testGetAlbum());

        Arrays.sort(songs, LikeTestSorter);
        assertEquals(1, songs[0].testGetLike());
    }

    // Testing three songs from the same album and artist
    @Test
    public void testThreeSongs() {
        Song song1 = new Song("Love Story", "Taylor Swift", "Fearless", 1);
        Song song2 = new Song("Change", "Taylor Swift", "Fearless", 1);
        Song song3 = new Song("Tell Me Why", "Taylor Swift", "Fearless", 1);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, TitleTestSorter);

        assertEquals("Change", songs[0].testGetTitle());
        assertEquals("Love Story", songs[1].testGetTitle());
        assertEquals("Tell Me Why", songs[2].testGetTitle());
    }

    //Testing three songs without same artist and album
    @Test
    public void testdiffArtist() {
        Song song1 = new Song("Love Story", "Taylor Swift", "Fearless", 1);
        Song song2 = new Song("Shape of You", "Ed Sheeran", "Divide", 0);
        Song song3 = new Song("Stay", "Zedd", "Everything", 0);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, TitleTestSorter);

        assertEquals("Love Story", songs[0].testGetTitle());
        assertEquals("Shape of You", songs[1].testGetTitle());
        assertEquals("Stay", songs[2].testGetTitle());
    }

    // Testing Same Song/songs with same title
    @Test
    public void testSameName() {
        Song song1 = new Song("Love Story", "Taylor Swift", "Fearless", 1);
        Song song2 = new Song("Love Story", "Taylor Swift", "Fearless", 1);
        Song song3 = new Song("Love Story", "Taylor Swift", "Fearless", 1);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, TitleTestSorter);

        assertEquals("Love Story", songs[0].testGetTitle());
        assertEquals("Love Story", songs[1].testGetTitle());
        assertEquals("Love Story", songs[2].testGetTitle());
    }

    // Testing 3 Songs with artist
    @Test
    public void testDiffArtist() {
        Song song1 = new Song("Love Story", "Taylor Swift", "Fearless", 1);
        Song song2 = new Song("Shape of You", "Ed Sheeran", "Divide", 0);
        Song song3 = new Song("Stay", "Zedd", "Everything", 0);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, ArtistTestSorter);

        assertEquals("Ed Sheeran", songs[0].testGetArtist());
        assertEquals("Taylor Swift", songs[1].testGetArtist());
        assertEquals("Zedd", songs[2].testGetArtist());
    }

    //Testing three songs with album
    @Test
    public void testDiffAlbum() {
        Song song1 = new Song("Love Story", "Taylor Swift", "Fearless", 1);
        Song song2 = new Song("Shape of You", "Ed Sheeran", "Divide", 0);
        Song song3 = new Song("Stay", "Zedd", "Everything", 0);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, AlbumTestSorter);

        assertEquals("Divide", songs[0].testGetAlbum());
        assertEquals("Everything", songs[1].testGetAlbum());
        assertEquals("Fearless", songs[2].testGetAlbum());
    }

    //Testing album that has a number
    @Test
    public void testDiffAlbumWithNum() {
        Song song1 = new Song("Shake it Off", "Taylor Swift", "1989", 1);
        Song song2 = new Song("Shape of You", "Ed Sheeran", "Divide", 0);
        Song song3 = new Song("Stay", "Zedd", "Everything", 0);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, AlbumTestSorter);

        assertEquals("1989", songs[0].testGetAlbum());
        assertEquals("Divide", songs[1].testGetAlbum());
        assertEquals("Everything", songs[2].testGetAlbum());

    }

    //Testing three songs with the same status
    @Test
    public void testSameStatus() {
        Song song1 = new Song("You Belong With Me", "Taylor Swift", "Fearless", 1);
        Song song2 = new Song("Something Just Like This", "CColdplay, The Chainsmokers", "Memories...Do Not Open", 1);
        Song song3 = new Song("It Ain't Me", "Selena Gomez, Kygo", "Stargazing", 1);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, LikeTestSorter);

        assertEquals("It Ain't Me", songs[0].testGetTitle());
        assertEquals("Something Just Like This", songs[1].testGetTitle());
        assertEquals("You Belong With Me", songs[2].testGetTitle());

    }

    //Testing with 3 different status
    public void testDiffStatus() {
        Song song1 = new Song("You Belong With Me", "Taylor Swift", "Fearless", 1);
        Song song2 = new Song("Something Just Like This", "CColdplay, The Chainsmokers", "Memories...Do Not Open", 0);
        Song song3 = new Song("It Ain't Me", "Selena Gomez, Kygo", "Stargazing", -1);

        Song[] songs = {song1, song2, song3};
        Arrays.sort(songs, LikeTestSorter);

        assertEquals("You Belong With Me", songs[0].testGetTitle());
        assertEquals("Something Just Like This", songs[1].testGetTitle());
        assertEquals("It Ain't Me", songs[2].testGetTitle());

        assertEquals(1, songs[0].testGetLike());
        assertEquals(0, songs[0].testGetLike());
        assertEquals(-1, songs[0].testGetLike());


    }





}
