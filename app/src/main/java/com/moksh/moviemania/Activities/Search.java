package com.moksh.moviemania.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.moksh.moviemania.APIs.RetrofitInstance;
import com.moksh.moviemania.R;
import com.moksh.moviemania.SearchResult.SearchedResult;
import com.moksh.moviemania.SearchResult.SearchedResultAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Search extends AppCompatActivity {
    RecyclerView searchedRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        Bottom navigation code STARTS here
//        Initialissing And Assign Variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        Set the Text Selected
        bottomNavigationView.setSelectedItemId(R.id.search);

//        performing item click
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        return true;
                    case R.id.upcoming:
                        startActivity(new Intent(getApplicationContext(),Upcoming.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

//        search box activity
        searchedRecyclerView = findViewById(R.id.searchedRecyclerView);
        searchedRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        SearchView searchView = (SearchView) findViewById(R.id.search_box);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    Call<SearchedResult> searchedResultCall = RetrofitInstance.getMoviesService().getSearchedResult("0a9a3315040e03cc51efab97258c9fbb","en-US",query,1,true);
                    searchedResultCall.enqueue(new Callback<SearchedResult>() {
                        @Override
                        public void onResponse(Call<SearchedResult> call, Response<SearchedResult> response) {
                            SearchedResult searchedResult = response.body();
                            SearchedResultAdapter adapter = new SearchedResultAdapter(getBaseContext(),searchedResult.getResults());
                            searchedRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<SearchedResult> call, Throwable t) {
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
                }


               return  true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }

        });

    }
//    class searchTask extends AsyncTask<String, Void, String>
//    {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String result = "";
//            try {
//                URL url = new URL(strings[0].substring(4));
//                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
//                httpsURLConnection.connect();
//                InputStream is = httpsURLConnection.getInputStream();
//                InputStreamReader ir = new InputStreamReader(is);
//                int data = is.read();
//                while(data > -1)
//                {
//                    char coming = (char) data;
//                    result += coming;
//                    data = is.read();
//                }
//            }catch (Exception e)
//            {
//                Log.i("error", e.toString());
//            }
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try{
//                JSONObject jsonObject = new JSONObject(s);
//                String result = jsonObject.getString("results");
//                JSONArray jsonArray = new JSONArray(result);
//                SearchedResultAdapter adapter = new SearchedResultAdapter(getBaseContext(),jsonArray);
//                searchedRecyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }catch (Exception e)
//            {
//                Log.i("error in json",e.toString());
//            }
//
//        }
//    }
}