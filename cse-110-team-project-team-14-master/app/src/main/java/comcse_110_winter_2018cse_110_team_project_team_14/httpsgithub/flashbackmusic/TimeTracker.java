package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.util.Log;

import java.util.Date;

/**
 * Created by Guangyan_Cai on 3/16/2018.
 */
enum TimeMode {
    NORMAL, MOCK
}

public class TimeTracker {
    static private TimeMode mode = TimeMode.NORMAL;
    static private long mockedTime = 0;

    static public long getTime() {
        if (mode == TimeMode.NORMAL) {
            return new Date().getTime();
        }
        if (mode == TimeMode.MOCK) {
            return mockedTime;
        }
        return 0;
    }

    static public void turnOnNormal() {
        mode = TimeMode.NORMAL;
    }

    static public void turnOnMock() {
        mode = TimeMode.MOCK;
    }

    static public void setMockedTime(long time) {
        mockedTime = time;
        Date date = new Date();
        date.setTime(mockedTime);
        Log.d("TimeTracker", "Time is set to " + date.toString());
    }
}
