package com.example.forager.Activities;
import android.os.SystemClock;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatDelegate;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.forager.R;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ForagerTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    // Test invalid input
    @Test
    public void testInput(){
        onView(withId(R.id.search_button)).perform(click());

        Assert.assertEquals(mActivityTestRule.getActivity().getClass().getName(), MainActivity.class.getName());
    }

    // Test to add an item to book list then remove (use with empty book list)
    @Test
    public void testSaveAndDelete() {
        onView(withId(R.id.input_editText)).perform(typeText("macbeth"));

        onView(withId(R.id.search_button)).perform(click());

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.fab)).perform(click());

        onView(isRoot()).perform(pressBack());

        onView(isRoot()).perform(pressBack());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        onView(withId(R.id.nav)).perform(NavigationViewActions.navigateTo(R.id.saved_books));

        // Allow recyclerView to load
        SystemClock.sleep(1000);

        onView(withId(R.id.bookListRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeRight()));
    }

    // Test if night mode is working
    @Test
    public void testNightMode(){
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav)).perform(NavigationViewActions.navigateTo(R.id.settings));

        onView(withId(R.id.nightMode)).perform(click());

        int nightMode = AppCompatDelegate.getDefaultNightMode();

        assertThat(nightMode, is(AppCompatDelegate.MODE_NIGHT_YES));

        onView(withId(R.id.nightMode)).perform(click());

        nightMode = AppCompatDelegate.getDefaultNightMode();

        assertThat(nightMode, is(AppCompatDelegate.MODE_NIGHT_NO));
    }
}
