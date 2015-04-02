package com.iotaink.pcat;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.iotaink.pcat.widget.DrawerArrayAdapter;
import com.iotaink.pcat.widget.DrawerItem;
import com.iotaink.pcat.widget.NormalItem;
import com.iotaink.pcat.widget.SectionItem;

import java.util.ArrayList;

/**
 * Based off of the sample Android NavigationDrawerFragment
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Holds callbacks
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Handles switching the state ActionBar when the navigation drawer
     * is opened or closed
     */
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * DrawerLayout
     */
    private DrawerLayout mDrawerLayout;

    /**
     * ListView containing the drawer items
     */
    private ListView mDrawerListView;

    /**
     * The View containing the drawer Fragment
     */
    private View mFragmentContainerView;

    /**
     * The collection of DrawerItems
     */
    private ArrayList<DrawerItem> mItems;

    /**
     * The current/last selected position in the list of
     * DrawerItems
     */
    private int mCurrentSelectedPosition = 1;

    /**
     * Constructor
     */
    public NavigationDrawerFragment() {
    }

    /**
     * onActivityCreated
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    /**
     * onCreateView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the ListView of the drawer
        this.mDrawerListView = (ListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        // Set the click listener for the drawer items
        this.mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavigationDrawerFragment.this.selectItem(position);
            }
        });
        // Create a collection to hold the drawer items
        this.mItems = new ArrayList<>();
        // Get the resources to retrieve data for the drawer items
        Resources resources = this.getResources();
        // Add the header for the ANC section
        this.mItems.add(new SectionItem(resources.getString(R.string.title_anc)));
        // Add the steps for the ANC section
        String[] ancStrings = resources.getStringArray(R.array.drawer_items_anc);
        for (String ancString : ancStrings) {
            this.mItems.add(new NormalItem(ancString));
        }
        // Add the header for the ART section
        this.mItems.add(new SectionItem(resources.getString(R.string.title_art)));
        // Add the steps for the ART section
        String[] artStrings = resources.getStringArray(R.array.drawer_items_art);
        for (String artString : artStrings) {
            this.mItems.add(new NormalItem(artString));
        }

        // Set the custom Adapter to handle displaying normal and section items
        this.mDrawerListView.setAdapter(new DrawerArrayAdapter(
                ((ActionBarActivity) this.getActivity()).getSupportActionBar().getThemedContext(),
                R.layout.drawer_normal_item,
                R.id.drawer_item_text,
                this.mItems));

        // Set the current item as checked
        this.mDrawerListView.setItemChecked(this.mCurrentSelectedPosition, true);

        return this.mDrawerListView;
    }

    /**
     * Gets the drawer item from the collection given a position
     *
     * @param position
     * @return
     */
    public DrawerItem getItem(int position) {
        if (this.mItems == null) {
            return null;
        }
        return this.mItems.get(position);
    }

    /**
     * Checks if the drawer is open
     *
     * @return
     */
    public boolean isDrawerOpen() {
        return this.mDrawerLayout != null && this.mDrawerLayout.isDrawerOpen(this.mFragmentContainerView);
    }

    /**
     * Initial setup of the drawer
     *
     * @param fragmentId
     * @param drawerLayout
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        // Get and set the View containing the Fragment
        this.mFragmentContainerView = getActivity().findViewById(fragmentId);
        // Set the DrawerLayout
        this.mDrawerLayout = drawerLayout;

        // Set the shadow
        this.mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Set up the ActionBar
        ActionBar actionBar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // Instantiate the ActionBarDrawerToggle
        this.mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!NavigationDrawerFragment.this.isAdded()) {
                    return;
                }
                // Calls Activity's onPrepareOptionsMenu()
                NavigationDrawerFragment.this.getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!NavigationDrawerFragment.this.isAdded()) {
                    return;
                }
                // Calls Activity's onPrepareOptionsMenu()
                NavigationDrawerFragment.this.getActivity().supportInvalidateOptionsMenu();
            }
        };

        this.mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                NavigationDrawerFragment.this.mDrawerToggle.syncState();
            }
        });
        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
    }

    /**
     * Handles clicks on the drawer's items
     *
     * @param position
     */
    private void selectItem(int position) {
        this.mCurrentSelectedPosition = position;
        if (this.mDrawerListView != null) {
            this.mDrawerListView.setItemChecked(position, true);
        }
        if (this.mDrawerLayout != null) {
            this.mDrawerLayout.closeDrawer(this.mFragmentContainerView);
        }
        if (this.mCallbacks != null) {
            this.mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    /**
     * onAttach
     *
     * Requires that the Activity that this drawre is attached to implement the
     * required callbacks
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    /**
     * onDetach
     */
    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallbacks = null;
    }

    /**
     * onCreateOptionsMenu
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, update the ActionBar to is general state
        if (this.mDrawerLayout != null && this.isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            ActionBar actionBar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setTitle(R.string.app_name);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * onOptionsItemSelected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Get the id of the clicked item
        int id = item.getItemId();

        // Recalculate the PCAT values
        if (id == R.id.action_calculate) {
            ((MainActivity) this.getActivity()).calculate();
            return true;
        } else if (id == R.id.action_previous || id == R.id.action_next) {
            // Move to the previous or next step
            int moveToPosition = this.determineMoveToPosition(item, this.mCurrentSelectedPosition);
            // Get the item at the position that will be moved to
            DrawerItem moveToDrawerItem = this.mItems.get(moveToPosition);

            // If it is a section item, ignore it
            while (moveToDrawerItem.getViewType() == DrawerArrayAdapter.DrawerItemType.SECTION.ordinal()) {
                moveToPosition = this.determineMoveToPosition(item, moveToPosition);
                moveToDrawerItem = this.mItems.get(moveToPosition);
            }

            // Update the current/last selected position
            this.mCurrentSelectedPosition = moveToPosition;
            // Show the new step
            ((MainActivity) this.getActivity()).onNavigationDrawerItemSelected(this.mCurrentSelectedPosition);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Determines which step to move to next based on if next or previous was clicked
     * and the position of the current step
     *
     * @param item The menu item was clicked (either previous or next)
     * @param currentPosition Position of the current step
     * @return
     */
    private int determineMoveToPosition(MenuItem item, int currentPosition) {
        // The last position
        int lastPos = this.mItems.size() - 1;
        // The position to move to
        int newPosition;

        // Previous means moving back in position while next means moving forward
        if (item.getItemId() == R.id.action_previous) {
            if (currentPosition <= 0 || currentPosition >= this.mItems.size()) {
                newPosition = lastPos;
            } else {
                newPosition = currentPosition - 1;
            }
        } else {
            if (currentPosition >= lastPos || currentPosition < 0) {
                newPosition = 0;
            } else {
                newPosition = currentPosition + 1;
            }
        }
        return newPosition;
    }

    /**
     * Interface to force common functionality for any Activity this Fragment is attached to
     */
    public static interface NavigationDrawerCallbacks {

        /**
         * Callback to handle whenever a drawer item is clicked
         *
         * @param position
         */
        void onNavigationDrawerItemSelected(int position);
    }

}
