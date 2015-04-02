package com.iotaink.pcat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.iotaink.pcat.R;

/**
 * Extension of EditText that has a key associated with it for
 * ease of reference
 */
public class KeyedTextView extends TextView {

    /**
     * The associated key
     */
    private String mKey;

    /**
     * Indicates if the value is rounded
     */
    private boolean mIsRounded;

    /**
     * Indicates if the value is a percent
     */
    private boolean mIsPercent;

    /**
     * Constructor
     *
     * Gets the key specified in the XML layout in the custom namespace
     *
     * @param context
     * @param attrs
     */
    public KeyedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyedView);
        try {
            this.mKey = typedArray.getString(R.styleable.KeyedView_key);
            this.mIsRounded = typedArray.getBoolean(R.styleable.KeyedView_isRounded, false);
            this.mIsPercent = typedArray.getBoolean(R.styleable.KeyedView_isPercent, false);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * Gets the key
     *
     * @return The key string
     */
    public String getKey() {
        return this.mKey;
    }

    /**
     * Checks if the value is rounded
     *
     * @return
     */
    public boolean isRounded() {
        return this.mIsRounded;
    }

    /**
     * Checks if the value is a percent
     * @return
     */
    public boolean isPercent() {
        return this.mIsPercent;
    }

}
