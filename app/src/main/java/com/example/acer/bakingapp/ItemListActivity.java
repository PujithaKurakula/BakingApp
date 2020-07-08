package com.example.acer.bakingapp;
import android.app.LoaderManager;
import android.appwidget.AppWidgetManager;
import android.content.AsyncTaskLoader;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.bakingapp.Ingredients;
import com.example.acer.bakingapp.ListAdapter;

import com.example.acer.bakingapp.IngredientsModel;
import com.example.acer.bakingapp.StepsModel;

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

public class ItemListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {


    String bUrl ="https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    SharedPreferences sharedPreferences;
    static String id;
    private boolean mTwoPane;
    ProgressBar progressBar;

    static Context context;

    ArrayList<IngredientsModel>  ingredients ;
    ArrayList<StepsModel> steps;
    RecyclerView ingreRecycler,stepsRecycler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        context=this;

        progressBar = findViewById(R.id.progress);
        ingreRecycler = findViewById(R.id.ingredients);
        stepsRecycler = findViewById(R.id.steps);
        id=ListAdapter.id;
        ingredients =new ArrayList<>();
        steps = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        getLoaderManager().initLoader(104,null,this);





        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.addwidget)
        {
            SharedPreferences sharedPreferences=context.getSharedPreferences(context.getPackageName(),MODE_PRIVATE);
            SharedPreferences.Editor se=sharedPreferences.edit();
            StringBuffer buffer=new StringBuffer();
            for(int j=0;j<ingredients.size();j++){

                buffer.append(ingredients.get(j).quantity+"\t"+
                        ingredients.get(j).measure+"\t"+ingredients.get(j).ingredient+"\n");
            }
            se.putString("widget",buffer.toString());
            se.apply();
            Intent intent=new Intent(ItemListActivity.this,WidgetBaking.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[]  mypos=AppWidgetManager.getInstance(ItemListActivity.this).getAppWidgetIds(new ComponentName
                    (getApplicationContext(),WidgetBaking.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,mypos);
            sendBroadcast(intent);
            Toast.makeText(this, "pooji", Toast.LENGTH_SHORT).show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<String>(this) {
            @Override
            public String loadInBackground() {
                //   progressBar.setVisibility(View.VISIBLE);
                try {
                    URL url = new URL(bUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream inputStream =urlConnection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    if(scanner.hasNext()){
                        return scanner.next();
                    }

                    else {
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
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {


        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject root = jsonArray.getJSONObject(Integer.parseInt(id));
            JSONArray  ingredient= root.getJSONArray("ingredients");
            ingredients.clear();
            steps.clear();
            for (int i =0;i<ingredient.length();i++){

                JSONObject ingr = ingredient.getJSONObject(i);
                String quantity = ingr.optString("quantity");
                String measure = ingr.optString("measure");
                String ing= ingr.optString("ingredient");
                ingredients.add(new IngredientsModel(ing,measure,quantity));
            }

            ingreRecycler.setLayoutManager(new LinearLayoutManager(this));

            ingreRecycler.setAdapter(new Ingredients(this,ingredients));


            JSONArray step = root.getJSONArray("steps");
            for(int j=0;j<step.length();j++){
                JSONObject stepview=step.getJSONObject(j);
                String shortdescription=stepview.getString("shortDescription");
                String description=stepview.getString("description");
                String video=stepview.getString("videoURL");
                String thumbNail=stepview.getString("thumbnailURL");
                steps.add(new StepsModel(shortdescription,description,video,thumbNail));
            }

            stepsRecycler.setLayoutManager(new LinearLayoutManager(this));

            stepsRecycler.setAdapter(new SimpleItemRecyclerViewAdapter(this,steps,mTwoPane));


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        Context context;
        ArrayList<StepsModel> stepsArrayList;
        boolean mTwoPane;

        public SimpleItemRecyclerViewAdapter(Context context, ArrayList<StepsModel> stepsArrayList,boolean mTwoPane) {
            this.context = context;
            this.stepsArrayList = stepsArrayList;
            this.mTwoPane=mTwoPane;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            StepsModel steps =stepsArrayList.get(position);
            holder.mIdView.setText(steps.getShortDescription());

        }

        @Override
        public int getItemCount() {
            return stepsArrayList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            ItemListActivity finalItemListActivity = (ItemListActivity) ItemListActivity.context;
            ItemDetailFragment detailFragment =new ItemDetailFragment();
            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        int pos = getAdapterPosition();
                        if (mTwoPane){

                            Bundle arg = new Bundle();

                            arg.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(pos));
                            arg.putString("description",stepsArrayList.get(pos).getDescription());
                            arg.putString("videourl",stepsArrayList.get(pos).getVideo());
                            arg.putString("shortdesc",stepsArrayList.get(pos).getShortDescription());
                            arg.putString("thumbnail",stepsArrayList.get(pos).getThumbnail());
                            detailFragment.setArguments(arg);
                            finalItemListActivity.getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container,detailFragment).commit();
                        }
                        else
                        {
                            Intent intent =new Intent(context.getApplicationContext(),ItemDetailActivity.class);
                            intent.putExtra(ItemDetailFragment.ARG_ITEM_ID,pos);
                            intent.putExtra("description",stepsArrayList.get(pos).getDescription());
                            intent.putExtra("videourl",stepsArrayList.get(pos).getVideo());
                            intent.putExtra("shortdesc",stepsArrayList.get(pos).getShortDescription());
                            intent.putExtra("thumbnail",stepsArrayList.get(pos).getThumbnail());
                            intent.putExtra("size",stepsArrayList.size());
                            context.startActivity(intent);

                        }

                    }
                });

            }
        }
    }
}