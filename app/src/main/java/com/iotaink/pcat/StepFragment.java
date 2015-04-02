package com.iotaink.pcat;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotaink.pcat.dataaccess.ReadWriter;
import com.iotaink.pcat.text.DelayedTextWatcher;
import com.iotaink.pcat.widget.KeyedEditText;
import com.iotaink.pcat.widget.KeyedTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment to represent a step in the PCAT analysis
 */
public class StepFragment extends Fragment {

    /**
     * Data access layer
     */
    private ReadWriter mDataAccess;

    /**
     * The root View of the Fragment
     */
    private LinearLayout mRootView;

    /**
     * A mapping of EditText keys to their values
     */
    private HashMap<String, String> mInputMap;

    /**
     * A mapping of TextView keys to their calculated values
     */
    private HashMap<String, String> mCalculatedValueMap;

    /**
     * A mapping of EditText keys to their TextWatchers
     */
    private HashMap<String, DelayedTextWatcher> mWatcherMap;

    /**
     * Handler for delays in the DelayedTextWatcher
     */
    private Handler mHandler;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up the data access layer
        this.mDataAccess = new ReadWriter(this.getActivity().getApplicationContext());
        // Instantiate the maps
        this.mInputMap = new HashMap<>();
        this.mCalculatedValueMap = new HashMap<>();
        this.mWatcherMap = new HashMap<>();
        // Instantiate the handler
        this.mHandler = new Handler();
    }

    /**
     * onCreateView
     *
     * Sets up all of the KeyedEditText inputs and the KeyedTextViews
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Determine which step to show
        String stepBundleKey = this.getString(R.string.bundle_step);
        String step = this.getArguments().getString(stepBundleKey);
        // Inflate the layout
        View fragmentView = inflater.inflate(this.determineLayout(step), container, false);
        // Keep reference to the root View so nested Views can be reached
        this.mRootView = (LinearLayout) fragmentView.getRootView();

        // Set up any KeyedEditTexts and KeyedTextViews
        for (int i = 0; i <= this.mRootView.getChildCount(); i++) {
            View v = this.mRootView.getChildAt(i);
            // Map KeyedTextViews to their value
            if (v instanceof KeyedTextView) {
                // Get the key
                String key = ((KeyedTextView) v).getKey();
                // Add the key to the map for reference
                // NOTE: Data load is deferred to onResume() on background thread
                this.mCalculatedValueMap.put(key, "");
            }

            // Map KeyedEditTexts to their values and add text listeners and watchers
            if (v instanceof KeyedEditText) {
                // The key of this KeyedEditText
                String key = ((KeyedEditText) v).getKey();

                // Add the key to the input map with an empty value
                // NOTE: Data load is deferred to onResume() on background thread
                this.mInputMap.put(key, "");

                // Instantiate a TextWatcher (delayed) to listen for changes
                // NOTE: DelayedTextWatcher should be passed an instance of the KeyedEditText and a Handler
                DelayedTextWatcher textWatcher = new DelayedTextWatcher((KeyedEditText) v, this.mHandler) {
                    @Override
                    public void runInstantly(EditText view) {
                        // Get the key and the value
                        String key = ((KeyedEditText) view).getKey();
                        String value = view.getText().toString();
                        // Store the value in the input map
                        StepFragment.this.mInputMap.put(key, value);
                        // Disable the menu items to prevent moving ahead while calculating
                        ((MainActivity) StepFragment.this.getActivity()).setMenuItemsEnabled(false /* enabled */);
                    }

                    @Override
                    public void runDelayed(EditText view) {
                        StepFragment.this.afterTextChangedDelay((KeyedEditText) view);
                    }
                };
                // Add the TextWatcher to the map and the EditText
                this.mWatcherMap.put(key, textWatcher);
                ((KeyedEditText) v).addTextChangedListener(textWatcher);

                // Instantiate a TextView.OnEditorActionListener
                TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        return StepFragment.this.onEditTextAction((KeyedEditText) v, actionId, event);
                    }
                };
                // Add the listener to the KeyedEditText
                ((KeyedEditText) v).setOnEditorActionListener(actionListener);
            }
        }

        return fragmentView;
    }

    /**
     * onResume
     *
     * Load the PCAT data on the background thread
     */
    @Override
    public void onResume() {
        super.onResume();
        // Load existing data
        new LoadDataTask().execute();
    }

    /**
     * onPause
     *
     * Save any data on the background thread before leaving this Fragment
     *
     * TODO: Since onPause starts an AsyncTask, the app will crash on a screen orientation change.
     * Possible solutions to handle this include saving the state of the task or potentially putting
     * the AsyncTask in StepFragment and retain the instance.
     */
    @Override
    public void onPause() {
        super.onPause();
        // Save the data of all inputs
        new SaveDataTask().execute();
    }

    /**
     * Calculates PCAT data on the background thread
     */
    public void calculate() {
        new UpdateAndCalculateTask().execute();
    }

    /**
     * Method to handle actions on the text editor
     *
     * When next or previous is clicked, the listener will detect it and call this method
     * to save the data on the background thread
     *
     * @param view
     * @param actionId
     * @param event
     * @return
     */
    private boolean onEditTextAction(KeyedEditText view, int actionId, KeyEvent event) {
        // Catch presses on the previous or next buttons
        if (actionId == EditorInfo.IME_ACTION_PREVIOUS || actionId == EditorInfo.IME_ACTION_NEXT) {
            // Get the key and the value
            String key = view.getKey();
            String value = view.getText().toString();
            // Store the new value in the input map
            this.mInputMap.put(key, value);
            // Perform save on the background thread, then update the UI
            new UpdateAndCalculateTask().execute();
            return true;
        }
        return false;
    }

    /**
     * Method to handle delayed actions after editing text
     *
     * Calculates the PCAT data on the background thread
     *
     * @param view
     */
    private void afterTextChangedDelay(KeyedEditText view) {
        // Perform save on the background thread, then update the UI
        this.calculate();
    }

    /**
     * Determines the which step's layout to show
     *
     * @param step
     * @return Resource integer id of the layout to show
     */
    private int determineLayout(String step) {
        if (step.equals(this.getString(R.string.step_1))) {
            return R.layout.fragment_step_1;
        } else if (step.equals(this.getString(R.string.step_2))) {
            return R.layout.fragment_step_2;
        } else if (step.equals(this.getString(R.string.step_3))) {
            return R.layout.fragment_step_3;
        } else if (step.equals(this.getString(R.string.step_4))) {
            return R.layout.fragment_step_4;
        } else if (step.equals(this.getString(R.string.step_5))) {
            return R.layout.fragment_step_5;
        } else if (step.equals(this.getString(R.string.step_6))) {
            return R.layout.fragment_step_6;
        } else if (step.equals(this.getString(R.string.step_7))) {
            return R.layout.fragment_step_7;
        } else if (step.equals(this.getString(R.string.step_art))) {
            return R.layout.fragment_step_5;
        } else {
            return R.layout.fragment_step_tp;
        }
    }

    /**
     * AsyncTask to handle calculating PCAT data
     *
     * AsyncTask just uses StepFragment instance class variables.  Consider rewriting so it
     * does not rely on class variables
     */
    private class UpdateAndCalculateTask extends AsyncTask<Void, Void, Void> {

        /**
         * ProgressDialog
         */
        private ProgressDialog mProgress;

        @Override
        protected Void doInBackground(Void... params) {
            if (StepFragment.this.mInputMap != null) {
                // Save all values in the input map
                for (Map.Entry<String, String> entry : StepFragment.this.mInputMap.entrySet()) {
                    StepFragment.this.mDataAccess.putValue(entry.getKey(), entry.getValue());
                }
            }
            if (StepFragment.this.mCalculatedValueMap != null) {
                // After saving new data, recalculate all PCAT values
                for (String key : StepFragment.this.mCalculatedValueMap.keySet()) {
                    // Get the value
                    String value = StepFragment.this.mDataAccess.getString(key);
                    // Add the calculated value to the map
                    StepFragment.this.mCalculatedValueMap.put(key, value);
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show the progress indicator
            this.mProgress = new ProgressDialog(StepFragment.this.getActivity());
            this.mProgress.setTitle(R.string.progress_saving);
            this.mProgress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Now that all the calculations are done, update the Views with the new values
            for (int i = 0; i <= StepFragment.this.mRootView.getChildCount(); i++) {
                View v = StepFragment.this.mRootView.getChildAt(i);
                // Need to update only the Views which contain calculated values
                if (v instanceof KeyedTextView) {
                    // Get the key
                    String key = ((KeyedTextView) v).getKey();
                    // Get the stored value from the input map
                    String value = StepFragment.this.mCalculatedValueMap.get(key);
                    // Round or convert the value to a percent
                    float floatValue = Float.parseFloat(value);
                    if (((KeyedTextView) v).isRounded()) {
                        floatValue = Math.round(floatValue);
                    }
                    if (((KeyedTextView) v).isPercent()) {
                        floatValue *= 100;
                    }
                    // Populate the value
                    ((KeyedTextView) v).setText(Float.toString(floatValue));
                }
            }
            // Close the progress indicator
            this.mProgress.dismiss();
            // Re-enabled the menu items
            ((MainActivity) StepFragment.this.getActivity()).setMenuItemsEnabled(true /* enabled */);
        }
    }

    /**
     * AsyncTask to handle just saving PCAT data
     *
     * AsyncTask just uses StepFragment instance class variables.  Consider rewriting so it
     * does not rely on class variables
     */
    private class SaveDataTask extends AsyncTask<Void, Void, Void> {

        /**
         * ProgressDialog
         */
        private ProgressDialog mProgress;

        @Override
        protected Void doInBackground(Void... params) {
            if (StepFragment.this.mInputMap != null) {
                // Save all values in the input map
                for (Map.Entry<String, String> entry : StepFragment.this.mInputMap.entrySet()) {
                    StepFragment.this.mDataAccess.putValue(entry.getKey(), entry.getValue());
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show the progress indicator
            this.mProgress = new ProgressDialog(StepFragment.this.getActivity());
            this.mProgress.setTitle(R.string.progress_saving);
            this.mProgress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Close the progress indicator
            this.mProgress.dismiss();
        }
    }

    /**
     * AsyncTask to handle just loading PCAT data
     *
     * AsyncTask just uses StepFragment instance class variables.  Consider rewriting so it
     * does not rely on class variables
     */
    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        /**
         * ProgressDialog
         */
        private ProgressDialog mProgress;

        @Override
        protected Void doInBackground(Void... params) {
            // Perform initial setup of a PCAT value
            // NOTE: Value may become editable later.  App will not check for "first run"
            // using something like SharedPreferences just in case the app data is cleared
            // So it will ALWAYS set this value
            StepFragment.this.mDataAccess.putValue(
                    StepFragment.this.getString(R.string.anc_pp),
                    StepFragment.this.getString(R.string.anc_pp_constant)
            );

            if (StepFragment.this.mInputMap != null) {
                // Load all the input values
                for (String key : StepFragment.this.mInputMap.keySet()) {
                    // Get the value
                    String value = StepFragment.this.mDataAccess.getString(key);
                    // Add the value to the input map
                    StepFragment.this.mInputMap.put(key, value);
                }
            }
            if (StepFragment.this.mCalculatedValueMap != null) {
                // Load all calculated values
                for (String key : StepFragment.this.mCalculatedValueMap.keySet()) {
                    // Get the value
                    String value = StepFragment.this.mDataAccess.getString(key);
                    // Add the value to the calculated value map
                    StepFragment.this.mCalculatedValueMap.put(key, value);
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show the progress indicator
            this.mProgress = new ProgressDialog(StepFragment.this.getActivity());
            this.mProgress.setTitle(R.string.progress_loading);
            this.mProgress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Now that all the data is loaded, update the Views
            for (int i = 0; i <= StepFragment.this.mRootView.getChildCount(); i++) {
                View v = StepFragment.this.mRootView.getChildAt(i);
                // For calculated values, just set the text of the KeyedTextView
                if (v instanceof KeyedTextView) {
                    // Get the key
                    String key = ((KeyedTextView) v).getKey();
                    // Get the stored value from the input map
                    String value = StepFragment.this.mCalculatedValueMap.get(key);
                    // Round or convert the value to a percent
                    float floatValue = Float.parseFloat(value);
                    if (((KeyedTextView) v).isRounded()) {
                        floatValue = Math.round(floatValue);
                    }
                    if (((KeyedTextView) v).isPercent()) {
                        floatValue *= 100;
                    }
                    // Populate the value
                    ((KeyedTextView) v).setText(Float.toString(floatValue));
                }

                // For editable values, remove the text watcher before setting the next
                // so it is not triggered multiple times
                if (v instanceof KeyedEditText) {
                    // Get the key
                    String key = ((KeyedEditText) v).getKey();
                    // Get the stored value from the input map
                    String value = StepFragment.this.mInputMap.get(key);
                    // Remove the listener
                    ((KeyedEditText) v).removeTextChangedListener(StepFragment.this.mWatcherMap.get(key));
                    // Populate the KeyedEditText with the stored value
                    ((KeyedEditText) v).setText(value);
                    // Re-add the listener
                    ((KeyedEditText) v).addTextChangedListener(StepFragment.this.mWatcherMap.get(key));
                }
            }
            // Close the progress indicator
            this.mProgress.dismiss();
        }
    }

}
