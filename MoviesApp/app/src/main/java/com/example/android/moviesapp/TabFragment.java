package com.example.android.moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.moviesapp.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yomna on 8/13/2016.
 */
public  class TabFragment extends Fragment {
    private static final String TAG = "RecyclerViewExample";
    private List<Movies> movies;
    private SharedPreferences prefs ;
    public static String pref ;
    private String prefOnPause ;
    private String prefOnResume ;
  //  public  static int position= 0;

    public static RecyclerView mRecyclerView;
    public static MyRecyclerAdapter adapter; ///// class is going to be implemented
    private ProgressBar progressBar;
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";

public static List<Movies> moviesList;

    public TabFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);


    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      //  inflater.inflate(R.menu.main, menu);
        //  inflater.inflate(R.menu.main, menu);

    }
    @Override
    public void onPause() {
        super.onPause();
        prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        prefOnPause = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_favorite));
        saveInstanceOnpause();


    }

    @Override
    public void onResume() {
        super.onResume();
        prefs= PreferenceManager.getDefaultSharedPreferences(getContext());

        prefOnResume = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_favorite));
        boolean orientation = getContext().getResources().getBoolean(R.bool.is_landscape);
        if(orientation && prefOnResume.equals("favorite")) {
            Log.v("orientation" , String.valueOf(orientation));
            updateList();
        }

      restoreInstanceOnResume(prefOnResume);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        onSaveOnDestroy();
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){

            Log.v("yomna","SuccessSetting");
            startActivity(new Intent(getContext(), SettingActivity.class));

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void saveInstanceOnpause(){
        //String prefPause = pref;
                mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
      //  return prefPause;

    }

    public void restoreInstanceOnResume(String pref){

        if(pref.equals(prefOnPause)) {
            if (mBundleRecyclerViewState != null) {
                Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);

            }
        }
        else
            updateList();
    }


    public void updateList (){
        AsyncHttpTask movieTask = new AsyncHttpTask();
             prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
             pref = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular));

        if(pref.equals(getString(R.string.pref_sort_popular))) {
            final String url = "https://api.themoviedb.org/3/movie/popular?api_key=";
            Log.v("yomna","Success-popular");
            movieTask.execute(url);

        }
        else if(pref.equals(getString(R.string.pref_sort_top_rated))) {
            final String url = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
            Log.v("yomna","Success-top-rated");
            movieTask.execute(url);

        }
        else if(pref.equals("favorite")){

            updateAdapter();

        }
    }

    public void updateAdapter(){
        Cursor cursor;
        // kda el list dy 3azen nms7 mnha
        moviesList = new ArrayList<Movies>();


        cursor = getActivity().getContentResolver()
                .query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Log.v("cursor favorite", "not null");
            for (int i = 0 ; i < cursor.getCount(); i++){

                Movies movieItem = new Movies();
                movieItem.setId(cursor.getString(0));
                movieItem.setRelease_date(cursor.getString(1));
                movieItem.setTitle(cursor.getString(2));
                movieItem.setOverview(cursor.getString(3));
                movieItem.setPoster_path(cursor.getString(4));
                movieItem.setVote_average(Double.parseDouble(cursor.getString(5)));
                movieItem.setBackdrop_path(cursor.getString(6));
                moviesList.add(movieItem);
                if(i != cursor.getCount() )
                    cursor.moveToNext();
            }


        }
        adapter = new MyRecyclerAdapter((AppCompatActivity) getContext(),moviesList);
        mRecyclerView.setAdapter(adapter);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        // Initialize recycler view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));



        return rootView;
    }
    public class AsyncHttpTask extends AsyncTask<String, Void, List<Movies>> {


        @Override
        protected void onPreExecute() {
         //   setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected List doInBackground(String... params) {
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;




            try{

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                   return null ;
                }
                moviesJsonStr = buffer.toString();
                Log.v("yomna",  moviesJsonStr);

            }catch (Exception e){
                Log.e(TAG, "Error ", e);
                return null ;
            }

            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return parseResultToMoviesObject(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }
        protected void onPostExecute(List result) {
            // Download complete. Let us update UI
        //    progressBar.setVisibility(View.GONE);

            if (result != null) {
                adapter = new MyRecyclerAdapter((AppCompatActivity) getContext(), result);
                mRecyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getContext(), "Check the internet connection", Toast.LENGTH_SHORT).show();
            }
        }
        private List parseResultToMoviesObject(String moviesJsonStr)throws JSONException {
            try {
                JSONObject movieJson = new JSONObject(moviesJsonStr);
                JSONArray resultArray = movieJson.optJSONArray("results");
                movies = new ArrayList<>();

                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject resultArr = resultArray.optJSONObject(i);
                    Movies movieItem = new Movies();
                   // item.setTitle(post.optString("title"));
                    movieItem.setTitle(resultArr.optString("title"));
                    movieItem.setPoster_path(resultArr.optString("poster_path"));
                    movieItem.setRelease_date(resultArr.optString("release_date"));
                    movieItem.setOverview(resultArr.optString("overview"));
                    movieItem.setVote_average(resultArr.optDouble("vote_average"));
                    movieItem.setId(resultArr.optString("id"));
                    movieItem.setBackdrop_path(resultArr.optString("backdrop_path"));
                    ///// in order to get all elements in the object >>>>>>>

                    movies.add(movieItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }
    }

}
