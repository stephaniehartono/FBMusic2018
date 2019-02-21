package comcse_110_winter_2018cse_110_team_project_team_14.httpsgithub.flashbackmusic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.PeopleService.Builder;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Guangyan_Cai on 3/12/2018.
 */

public class UserInfo {
    static private Context context;
    static private String currentUsername = "";
    static private ArrayList<String> friendsUsername = new ArrayList<>();


    static public void setupUserInfo(Context c, String code) {
        context = c;
        new CreatePeopleService().execute(code);
    }

    static public String getCurrentUsername() {
        return currentUsername;
    }

    static public boolean isFriend(String friendName) {
        return friendsUsername.contains(friendName);
    }

    static public ArrayList<String> getFriendsUsername() {
        return friendsUsername;
    }

    static private class CreatePeopleService extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... code) {
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();

            String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";

            GoogleTokenResponse tokenResponse = null;
            try {
                tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        httpTransport,
                        jsonFactory,
                        context.getString(R.string.clientID),
                        context.getString(R.string.clientSecret),
                        code[0],
                        redirectUrl).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setClientSecrets(context.getString(R.string.clientID),
                            context.getString(R.string.clientSecret))
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .build();

            credential.setFromTokenResponse(tokenResponse);

            PeopleService peopleService = new Builder(httpTransport, jsonFactory, credential)
                    .build();

            Person currentUser = null;
            try {
                currentUser = peopleService.people().get("people/me")
                        .setPersonFields("names")
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (currentUser != null) {
                List<Name> names = currentUser.getNames();
                for (Name name : names) {
                    if (name != null) {
                        currentUsername = name.getDisplayName();
                        break;
                    }
                }
            }


            ListConnectionsResponse response = null;
            try {
                response = peopleService.people().connections().list("people/me")
                        .setPersonFields("names")
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                List<Person> friends = response.getConnections();
                if (friends != null) {
                    for (Person friend : friends) {
                        List<Name> names = friend.getNames();
                        for (Name name : names) {
                            if (name != null) {
                                friendsUsername.add(name.getDisplayName());
                                break;
                            }
                        }
                    }
                }
            }
            return null;
        }
    }
}
