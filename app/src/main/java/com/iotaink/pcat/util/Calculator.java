package com.iotaink.pcat.util;

/**
 * Utility class to handle calculations for the PCAT
 *
 * These calculations were copied from the Excel file so it is possible
 * they can be simplified.  Get in touch with the PCAT creator to potentially
 * work out a better way to handle calculations
 */
public class Calculator {

    /**
     * Target population
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static float targetPopulation(float a, float b, float c) {
        return Calculator.validate(a * (b / 12) * c);
    }

    /**
     * Patient percentage
     *
     * @param initialCount
     * @param finalCount
     * @return
     */
    public static float patientPercentage(float initialCount, float finalCount) {
        return Calculator.validate(finalCount / initialCount);
    }

    /**
     * Patient drop off
     *
     * @param initialCount
     * @param finalCount
     * @return
     */
    public static float patientDropOff(float initialCount, float finalCount) {
        return Calculator.validate(initialCount - finalCount);
    }

    /**
     * Extra completing through step 4 if drop off eliminated (Step 1)
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     * @param g
     * @return
     */
    public static float step1Extra(float a, float b, float c, float d, float e, float f, float g) {
        return Calculator.validate((((a * b * c) + (d + e)) * f) - g);
    }

    /**
     * Extra completing through step 4 if drop off eliminated (Step 2)
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     * @return
     */
    public static float step2Extra(float a, float b, float c, float d, float e, float f) {
        return Calculator.validate((((a * b) + (c + d)) * e) - f);
    }

    /**
     * Extra completing through step 4 if drop off eliminated (Step 3)
     * @param a
     * @param b
     * @return
     */
    public static float step3Extra(float a, float b) {
        return Calculator.validate(a * b);
    }

    /**
     * Extra completing through step 4 if drop off eliminated (Step 4)
     *
     * @param a
     * @param b
     * @return
     */
    public static float step4Extra(float a, float b) {
        return Calculator.validate(a - b);
    }

    /**
     * Extra completing through step 7 if drop off eliminated (Step 5)
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static float step5Extra(float a, float b, float c, float d) {
        return Calculator.validate((a * b * c) - d);
    }

    /**
     * Extra completing through step 7 if drop off eliminated (Step 6)
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    public static float step6Extra(float a, float b, float c, float d) {
        return Calculator.validate((a * b * c) - d);
    }

    /**
     * Extra completing through step 7 if drop off eliminated (Step 7)
     *
     * @param a
     * @param b
     * @return
     */
    public static float step7Extra(float a, float b) {
        return Calculator.validate(a - b);
    }

    /**
     * Since no validation currently exists in this app, if a calculation results in NaN,
     * just replace it with 0
     *
     * @param val
     * @return
     */
    private static float validate(float val) {
        if (Float.isNaN(val)) {
            return 0;
        }
        return val;
    }

}
