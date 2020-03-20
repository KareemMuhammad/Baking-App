package com.oikm.a100.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTest = new ActivityTestRule<MainActivity>(MainActivity.class);
    @Test
    public void testTest() {
        int RECYCLER_VIEW_FIRST_ITEM = 0;
        String NUTELLA_PIE = "Nutella Pie";
        onView(withId(R.id.cardsRecyclerView)).check(matches(hasDescendant(withText(NUTELLA_PIE))));
        onView(withId(R.id.cardsRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_VIEW_FIRST_ITEM, click()));
    }
    @Test
    public void onScrollPosition(){
        Espresso.onView(ViewMatchers.withId(R.id.cardsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, ViewActions.longClick()));
    }
}
