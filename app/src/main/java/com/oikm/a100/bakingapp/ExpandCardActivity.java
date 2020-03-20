package com.oikm.a100.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oikm.a100.bakingapp.Fragments.StepsFragment;
import com.oikm.a100.bakingapp.Model.Bakery;
import com.oikm.a100.bakingapp.Model.Ingredients;
import com.oikm.a100.bakingapp.Widget.RecipesService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExpandCardActivity extends AppCompatActivity implements ExpandCardAdapter.OnClickListener {
    private static final String TAG = ExpandCardActivity.class.getSimpleName();
    private static final String DESC = "desc";
    private static final String VIDEO = "video";
    private static final String KEY_INGREDIENT = "ingredient";
    private static final String KEY_PREFERENCE = "shared preference";
 private static final String KEY_WIDGET = "bakery";
 private static final String KEY_SAVE = "recipe";
 private static final String KEY_RECIPE = "bakery";
 private static final String KEY_STEPS = "steps";
 private static final String KEY_INDEX = "index";
 public RecyclerView recyclerView;
 public ExpandCardAdapter myAdapter;
 public Bakery recipe;
 boolean mTwoPane;
 int index;
 public List<Ingredients> ingredientsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_card);
        loadIngredientList();
        if (savedInstanceState != null){
            recipe = savedInstanceState.getParcelable(KEY_SAVE);
            if (recipe != null) {
                recyclerView = findViewById(R.id.expandRecyclerView);
                myAdapter = new ExpandCardAdapter(recipe.getIngredients(), this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ExpandCardActivity.this
                        , LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(myAdapter);
            }
        }else {
            if (findViewById(R.id.linear_layout_tablet) != null) {
                mTwoPane = true;
                recipe = new Bakery();
                Intent intent = getIntent();
                if (intent.hasExtra(KEY_RECIPE) || intent.hasExtra(KEY_INDEX)) {
                    Log.d(TAG, "extra arrived");
                    recipe = intent.getParcelableExtra(KEY_RECIPE);
                    index = intent.getIntExtra(KEY_INDEX, 0);
                    if (recipe != null) {
                        String desc = recipe.getSteps().get(index).getDescription();
                        String video = recipe.getSteps().get(index).getVideoURL();
                        Bundle bundle = new Bundle();
                        bundle.putString(DESC, desc);
                        bundle.putString(VIDEO, video);
                        StepsFragment stepsFragment = new StepsFragment();
                        stepsFragment.setArguments(bundle);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.playerContainer, stepsFragment)
                                .commit();
                    }
                }
            } else {
                mTwoPane = false;
            recipe = new Bakery();
            Intent intent = getIntent();
            if (intent.hasExtra(KEY_RECIPE)) {
                Log.d(TAG, "extra arrived");
                recipe = intent.getParcelableExtra(KEY_RECIPE);
                if (recipe != null) {
                    Log.d(TAG, "extra not null");
                    ingredientsList = new ArrayList<>();
                    ingredientsList = recipe.getIngredients();
                    recyclerView = findViewById(R.id.expandRecyclerView);
                    myAdapter = new ExpandCardAdapter(recipe.getIngredients(), this);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ExpandCardActivity.this
                            , LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(myAdapter);
                    sendIntentWidget();
                }
            } else
                Log.d(TAG, "extra null");
        }
        }
    }
    public void sendIntentWidget(){
        Intent intent = new Intent(this, RecipesService.class);
        intent.putExtra(KEY_WIDGET,recipe);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(intent);
    }
    public void saveIngredientList(){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_PREFERENCE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json  = gson.toJson(ingredientsList);
        editor.putString(KEY_INGREDIENT,json);
        editor.apply();
    }
    public void loadIngredientList(){
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_PREFERENCE,MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_INGREDIENT,null);
        Type type = new TypeToken<ArrayList<Ingredients>>() {}.getType();
        ingredientsList = gson.fromJson(json,type);
        if (ingredientsList == null){
            ingredientsList = new ArrayList<>();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_SAVE,recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepsClick(int i) {
        Log.d(TAG,"no "+i);
       Intent intent = new Intent(ExpandCardActivity.this,StepsActivity.class);
       if (recipe != null){
           intent.putExtra(KEY_STEPS,recipe);
           intent.putExtra("index",i);
           startActivity(intent);
       }
    }

    @Override
    protected void onDestroy() {
        saveIngredientList();
        super.onDestroy();
    }
}
