package com.iotaink.pcat.text;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Abstract implementation of TextWatcher that has callbacks both immediately after text is changed
 * and after a delay.
 *
 * onBeforeTextChanged and onTextChanged are not implemented.
 */
public abstract class DelayedTextWatcher implements TextWatcher {

    /**
     * The delay (ms)
     */
    private long mDelay;

    /**
     * The EditText that is being watched
     */
    private EditText mEditText;

    /**
     * Handler for the delay
     */
    private Handler mHandler;

    /**
     * Runnable for the delay
     *
     * Runs the delayed function afterDelay()
     */
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (DelayedTextWatcher.this.mEditText != null) {
                DelayedTextWatcher.this.runDelayed(DelayedTextWatcher.this.mEditText);
            }
        }
    };

    /**
     * Default amount to delay (ms)
     */
    private static final long DEFAULT_DELAY = 1500;

    /**
     * Constructor
     */
    public DelayedTextWatcher() {
        this.mDelay = DEFAULT_DELAY;
    }

    /**
     * Constructor
     *
     * @param delay
     */
    public DelayedTextWatcher(long delay) {
        this.mDelay = delay;
    }

    /**
     * Constructor
     *
     * @param delay
     * @param editText
     */
    public DelayedTextWatcher(long delay, EditText editText) {
        this.mDelay = delay;
        this.mEditText = editText;
    }

    /**
     * Constructor
     * @param editText
     * @param handler Handler should be brought in from the Activity
     */
    public DelayedTextWatcher(EditText editText, Handler handler) {
        this.mDelay = DEFAULT_DELAY;
        this.mEditText = editText;
        this.mHandler = handler;
    }

    /**
     * Callback for running right after text has changed
     *
     * @param view The EditText being watched
     */
    public abstract void runInstantly(EditText view);

    /**
     * Callback for a delayed run after text has changed
     *
     * @param view The EditText being watched
     */
    public abstract void runDelayed(EditText view);

    /**
     * Not implemented
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    /**
     * Not implemented
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    /**
     * Runs a callback instantly and runs another with a delay using a Handler and a
     * Runnable
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        if (DelayedTextWatcher.this.mEditText != null) {
            DelayedTextWatcher.this.runInstantly(DelayedTextWatcher.this.mEditText);
        }
        this.mHandler.removeCallbacks(this.mRunnable);
        this.mHandler.postDelayed(this.mRunnable, this.mDelay);
    }

}
