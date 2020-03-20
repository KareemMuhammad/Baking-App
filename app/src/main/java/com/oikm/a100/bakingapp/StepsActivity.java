package com.oikm.a100.bakingapp;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Rational;
import android.view.Display;

import com.oikm.a100.bakingapp.Fragments.StepsFragment;
import com.oikm.a100.bakingapp.Model.Bakery;

public class StepsActivity extends AppCompatActivity {
    ActionBar actionBar;
    private static final String KEY_STEPS = "steps";
    private static final String KEY_SAVE = "step";
    private static final String DESC = "desc";
    private static final String VIDEO = "video";
    public Bakery steps;
    public int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelable(KEY_SAVE);
        }
            actionBar = this.getSupportActionBar();
            Intent intent = getIntent();
            if (intent.hasExtra(KEY_STEPS) || intent.hasExtra("index")) {
                index = intent.getIntExtra("index", 0);
                steps = intent.getParcelableExtra(KEY_STEPS);
                String desc = steps.getSteps().get(index).getDescription();
                String video = steps.getSteps().get(index).getVideoURL();
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

    @Override
    protected void onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int length = size.y;
            Rational aspects = new Rational(width, length);
            PictureInPictureParams.Builder pictureBuilder = new PictureInPictureParams.Builder();
            pictureBuilder.setAspectRatio(aspects).build();
            enterPictureInPictureMode(pictureBuilder.build());
        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
      if (isInPictureInPictureMode){
          actionBar.hide();
      }else {
          actionBar.show();
      }
    }

}

