package com.iotaink.pcat;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.iotaink.pcat.widget.DrawerItem;

/**
 * The MainActivity which handles showing a navigation drawer fragment
 * and a fragment representing the different steps of the PCAT analysis
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * The menu
     */
    private Menu mMenu;

    /**
     * The navigation drawer fragment
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * The current step fragment
     */
    private StepFragment mCurrentStepFragment;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the navigation drawer fragment
        this.mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer
        this.mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Switch to the initial step on start up
        this.switchStepFragment(this.getString(R.string.step_tp));
    }

    /**
     * Handles clicks on navigation drawer items
     *
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Get the drawer Fragment
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) this.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Get the item that was clicked
        DrawerItem item = drawerFragment.getItem(position);
        if (item != null) {
            // Switch the step based on which item was clicked
            this.switchStepFragment(item.getText());
        }
    }

    /**
     * onCreateOptionsMenu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        // If the drawer is not open, show buttons on the action bar that related
        // to the step Fragments
        if (!this.mNavigationDrawerFragment.isDrawerOpen()) {
            this.getMenuInflater().inflate(R.menu.main, menu);
            ActionBar actionBar = this.getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * onOptionsItemSelected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The id of the item that was clicked
        int id = item.getItemId();

        if (id == R.id.action_about) {
            DialogFragment fragment = new AboutDialogFragment();
            fragment.show(this.getSupportFragmentManager(), "about");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * onBackPressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // If the back button was pressed, recalculate all the PCAT values
        if (this.mCurrentStepFragment != null) {
            this.mCurrentStepFragment.calculate();
        }
    }

    /**
     * Calculates values for the current step Fragment
     */
    public void calculate() {
        if (this.mCurrentStepFragment != null) {
            this.mCurrentStepFragment.calculate();
        }
    }

    /**
     * Enables or disables the items in MenuItems
     * @param enabled
     */
    public void setMenuItemsEnabled(boolean enabled) {
        if (this.mMenu != null) {
            this.mMenu.setGroupEnabled(R.id.menu_items, enabled);
        }
    }

    /**
     * Switches the step Fragment based on the key passed in
     *
     * @param key
     */
    private void switchStepFragment(String key) {
        this.mCurrentStepFragment = new StepFragment();
        Bundle args = new Bundle();
        args.putString(this.getString(R.string.bundle_step), key);
        this.mCurrentStepFragment.setArguments(args);
        // Change the Fragment
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, this.mCurrentStepFragment);
        //ft.addToBackStack(key);
        ft.commit();
    }

}
