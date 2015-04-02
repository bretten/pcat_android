package com.iotaink.pcat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import com.iotaink.pcat.R;

/**
 * Extension of EditText that has a key associated with it for
 * ease of reference
 */
public class KeyedEditText extends EditText {

    /**
     * The associated key
     */
    private String mKey;

    /**
     * Constructor
     *
     * Gets the key specified in the XML layout in the custom namespace
     *
     * @param context
     * @param attrs
     */
    public KeyedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyedView);
        try {
            this.mKey = typedArray.getString(R.styleable.KeyedView_key);
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

}
