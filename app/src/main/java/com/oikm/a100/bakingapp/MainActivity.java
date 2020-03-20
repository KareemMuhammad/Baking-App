package com.oikm.a100.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.oikm.a100.bakingapp.Model.Bakery;
import com.oikm.a100.bakingapp.Model.JsonPlaceHolderApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BakeryAdapter.OnClickListener {
    private static final String KEY_BAKERY = "bakery";
    private static final String KEY_INDEX = "index";
    public RecyclerView recyclerView;
    private List<Bakery> bakeryList;
    private BakeryAdapter bakeryAdapter;
    public TextView errorText;
    public ProgressBar progressBar;
    private static final String KEY_LiST= "bakeryList";
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null ) {
            bakeryList = savedInstanceState.getParcelableArrayList(KEY_LiST);
        }
            initializeView();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            JsonPlaceHolderApi jasonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<List<Bakery>> call = jasonPlaceHolderApi.getBakery();
            call.enqueue(new Callback<List<Bakery>>() {
                @Override
                public void onResponse(Call<List<Bakery>> call, Response<List<Bakery>> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        errorText.setVisibility(View.INVISIBLE);
                        bakeryList = response.body();
                        Log.d(TAG, "response delivered");
                        bakeryAdapter.setData(bakeryList);
                        bakeryAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, response.message());
                        errorText.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<List<Bakery>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }
            });
    }
private void initializeView(){
        errorText = findViewById(R.id.error);
        progressBar = findViewById(R.id.progressBar);
    bakeryList = new ArrayList<Bakery>();
    bakeryAdapter = new BakeryAdapter(bakeryList, this);
    recyclerView = findViewById(R.id.cardsRecyclerView);
    recyclerView.setAdapter(bakeryAdapter);
    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this
            ,LinearLayoutManager.VERTICAL,false);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
}
    @Override
    public void onButtonClick(int i) {
        Intent intent = new Intent(MainActivity.this, ExpandCardActivity.class);
        Bakery bakery = bakeryList.get(i);
        intent.putExtra(KEY_BAKERY,bakery);
        intent.putExtra(KEY_INDEX,i);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_LiST, (ArrayList<? extends Parcelable>) bakeryList);
    }
}
