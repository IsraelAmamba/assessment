package com.example.challenge;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.challenge.adapters.FortuneMessageAdapter;
import com.example.challenge.model.FortuneMessage;
import com.example.challenge.network.NetworkClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String url = "*************";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static FortuneMessageAdapter fortuneMessageAdapter;
    private static List<FortuneMessage> fortuneMessageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeUIElements();
        attachRecyclerViewAdapter();

        //now do the pull repeatedly
        autoPullMessageRepeatedly();

    }

    private void attachRecyclerViewAdapter(){
        fortuneMessageAdapter = new FortuneMessageAdapter(fortuneMessageList);
        recyclerView.setAdapter(fortuneMessageAdapter);
        fortuneMessageAdapter.notifyDataSetChanged();
    }

    private void initializeUIElements(){
        recyclerView = findViewById(R.id.recyclerFortuneList);

        //add the layout manager for RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL, 16));

        recyclerView.setLayoutManager(mLayoutManager);
    }

    public class PullAdviceTask extends AsyncTask<String, Void, Void>{
        public String serverResponse;
        private Context context;

        public PullAdviceTask(Context context_){
            context = context_;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {
            Request request = new Request.Builder()
                    .url(urls[0])
                    .build();

            try {
                NetworkClient networkClient = NetworkClient.getInstance();
                OkHttpClient okHttpClient = networkClient.getOkHttpClient();

                Response response =  okHttpClient.newCall(request).execute();
                serverResponse = response.body().string();

            } catch (EOFException ex){

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, context.getString(R.string.wrong_inputs), Toast.LENGTH_SHORT).show();

                    }
                });


            }catch (Exception e){
                Log.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, context.getString(R.string.timeout_message), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            try {
                Gson gson = new Gson();

                if(serverResponse != null){
                    Log.e(TAG, serverResponse);
                    FortuneMessage fortuneMessage = gson.fromJson(serverResponse, FortuneMessage.class);
                    if (fortuneMessage.getFortune() != null)
                    {
                        fortuneMessageList.add(fortuneMessage);
                        fortuneMessageAdapter.notifyItemInserted(fortuneMessageList.size()-1);
                    }
                }
            }catch (Exception ex){
                Log.e(TAG, ex.toString());
            }
        }
    }

    public void autoPullMessageRepeatedly(){

        try {
            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        //create a worker thread with asynctask to pull new message
                        PullAdviceTask pullAdviceTask = new PullAdviceTask(MainActivity.this);
                        pullAdviceTask.execute(url);
                    }
                    finally
                    {
                        handler.postDelayed(this, 3000);//repeat pull after every 3 millisecons
                    }
                }
            };

            handler.post(runnable);
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
