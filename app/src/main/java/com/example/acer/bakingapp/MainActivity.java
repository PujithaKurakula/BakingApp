package com.example.acer.bakingapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<String> {
    RecyclerView recyclerView;
    ProgressBar pb;
    String bkurl="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    ArrayList<ModelClass> models;
    int[] images={R.drawable.nutella,R.drawable.brown,R.drawable.lemoncake,R.drawable.chesse};
    public  static final int Loader_ID=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerview);
        pb=findViewById(R.id.progress);
        models=new ArrayList<>();

        getLoaderManager().initLoader(Loader_ID,null,this);

    }


    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            public String loadInBackground() {

                try {
                    URL url = new URL(bkurl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    if (scanner.hasNext()) {
                        return scanner.next();
                    } else {
                        return null;
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                pb.setVisibility(View.VISIBLE);
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        pb.setVisibility(View.INVISIBLE);

        try {
            JSONArray jsonArray=new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject result = jsonArray.getJSONObject(i);
                String name=result.optString("name");
                String id=result.optString("id");
                models.add(new ModelClass(name,id));
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new ListAdapter(this,models,images));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

        @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
