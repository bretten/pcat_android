package com.iotaink.pcat.dataaccess;

import android.content.Context;
import android.content.SharedPreferences;

import com.iotaink.pcat.R;
import com.iotaink.pcat.util.Calculator;

/**
 * Temporary wrapper class for SharedPreferences
 *
 * Currently the application only stores one set of values for a PCAT analysis.  Since the
 * value set is relatively small, SharedPreferences was used to store the values.
 *
 * If development continues, the storage method should be changed and this class should be
 * removed.
 */
public class ReadWriter {

    /**
     * Context
     */
    private Context mContext;

    /**
     * SharedPreferences
     */
    private SharedPreferences mSharedPreferences;

    /**
     * The default float value when retrieving a float from SharedPreferences
     */
    private static final float DEFAULT_FLOAT_VALUE = 0;

    /**
     * Constructor
     *
     * Uses the Context to get SharedPreferences
     *
     * @param context
     */
    public ReadWriter(Context context) {
        this.mContext = context;
        this.mSharedPreferences = context.getSharedPreferences(
                context.getString(R.string.shared_preferences_data_filename), Context.MODE_PRIVATE);
    }

    /**
     * Convenience wrapper for SharedPreferences getFloat()
     *
     * @param key
     * @return Float at the given key
     */
    public float getFloat(String key) {
        return this.mSharedPreferences.getFloat(key, DEFAULT_FLOAT_VALUE);
    }

    /**
     * Convenience wrapper for getting a float in String format
     *
     * @param key
     * @return String form of a stored float
     */
    public String getString(String key) {
        return Float.toString(this.getFloat(key));
    }

    /**
     * Convenience wrapper for putting a float in SharedPreferences and then
     * calculating all values
     *
     * TODO: For future development, only calculations based on the value being saved should be done
     *
     * @param key
     * @param value
     */
    public void putValue(String key, float value) {
        SharedPreferences.Editor editor = this.mSharedPreferences.edit();
        editor.putFloat(key, value).commit();

        // Calculate values
        this.calculateAllValues();
    }

    /**
     * Convenience wrapper for putting a float in SharedPreferences in a String format
     *
     * @param key
     * @param value
     */
    public void putValue(String key, String value) {
        float newValue;
        // Make sure the string value can be parsed to a float
        try {
            newValue = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            // The value could not be parsed correctly, so just use the default value
            newValue = DEFAULT_FLOAT_VALUE;
        }
        this.putValue(key, newValue);
    }

    /**
     * Temporary function to calculate all PCAT related values.
     *
     * TODO: For future development, only calculations based on the value being saved should be done
     */
    private void calculateAllValues() {
        SharedPreferences.Editor editor = this.mSharedPreferences.edit();

        // Calculate all the values
        // Target Population
        editor.putFloat(this.mContext.getString(R.string.anc_tp),
                Calculator.targetPopulation(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.anc_hfca), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.anc_pp), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.anc_nm), DEFAULT_FLOAT_VALUE)
                ));

        // Step 1
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s1_a_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.anc_tp), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s1_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Drop off
        editor.putFloat(this.mContext.getString(R.string.s1_a_drop_off),
                Calculator.patientDropOff(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.anc_tp), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s1_a_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 2
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s2_a_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s1_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Drop off
        editor.putFloat(this.mContext.getString(R.string.s2_a_drop_off),
                Calculator.patientDropOff(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s1_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_a_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 2 B
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s2_b_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_b_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 2 E
        editor.putFloat(this.mContext.getString(R.string.s2_e_total),
                this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_b_total), DEFAULT_FLOAT_VALUE)
                + this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_c_total), DEFAULT_FLOAT_VALUE)
                + this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_d_total), DEFAULT_FLOAT_VALUE)
                );

        // Step 3
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s3_a_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_b_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s3_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Drop off
        editor.putFloat(this.mContext.getString(R.string.s3_a_drop_off),
                Calculator.patientDropOff(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_b_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s3_a_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 4
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s4_a_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s3_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Drop off
        editor.putFloat(this.mContext.getString(R.string.s4_a_drop_off),
                Calculator.patientDropOff(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s3_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 5
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s5_a_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_e_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s5_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Drop off
        editor.putFloat(this.mContext.getString(R.string.s5_a_drop_off),
                Calculator.patientDropOff(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_e_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s5_a_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 6
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s6_a_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s5_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Drop off
        editor.putFloat(this.mContext.getString(R.string.s6_a_drop_off),
                Calculator.patientDropOff(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s5_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_a_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 6 B
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s6_b_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_b_total), DEFAULT_FLOAT_VALUE)
                ));

        // Step 7
        // Percentage
        editor.putFloat(this.mContext.getString(R.string.s7_a_percent),
                Calculator.patientPercentage(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_b_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s7_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Drop off
        editor.putFloat(this.mContext.getString(R.string.s7_a_drop_off),
                Calculator.patientDropOff(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_b_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s7_a_total), DEFAULT_FLOAT_VALUE)
                ));

        // Extra completing through step 4 if drop off eliminated
        // Step 1
        editor.putFloat(this.mContext.getString(R.string.s1_a_extra),
                Calculator.step1Extra(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.anc_tp), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_a_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_b_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_c_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_d_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Step 2
        editor.putFloat(this.mContext.getString(R.string.s2_a_extra),
                Calculator.step2Extra(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s1_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_b_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_c_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_d_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Step 3
        editor.putFloat(this.mContext.getString(R.string.s3_a_extra),
                Calculator.step3Extra(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s3_a_drop_off), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_percent), DEFAULT_FLOAT_VALUE)
                ));
        // Step 4
        editor.putFloat(this.mContext.getString(R.string.s4_a_extra),
                Calculator.step4Extra(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s3_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s4_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Step 5
        editor.putFloat(this.mContext.getString(R.string.s5_a_extra),
                Calculator.step5Extra(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s2_e_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_a_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_b_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_b_total), DEFAULT_FLOAT_VALUE)
                ));
        // Step 6
        editor.putFloat(this.mContext.getString(R.string.s6_a_extra),
                Calculator.step6Extra(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s5_a_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_b_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s7_a_percent), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s7_a_total), DEFAULT_FLOAT_VALUE)
                ));
        // Step 7
        editor.putFloat(this.mContext.getString(R.string.s7_a_extra),
                Calculator.step7Extra(
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s6_b_total), DEFAULT_FLOAT_VALUE),
                        this.mSharedPreferences.getFloat(this.mContext.getString(R.string.s7_a_total), DEFAULT_FLOAT_VALUE)
                ));
        editor.commit();
    }

}
