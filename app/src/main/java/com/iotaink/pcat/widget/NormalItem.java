package com.iotaink.pcat.widget;

/**
 * Implementation of DrawerItem representing a simple, clickable ListView item
 */
public class NormalItem implements DrawerItem {

    /**
     * The item text
     */
    private String mText;

    /**
     * Constructor
     *
     * @param text
     */
    public NormalItem(String text) {
        this.mText = text;
    }

    /**
     * Returns an integer representing the type of this DrawerItem.
     *
     * Implementations of ArrayAdapter should delegate getItemViewType()
     * to this to determine the type
     *
     * @return An integer representing the View type
     */
    @Override
    public int getViewType() {
        return DrawerArrayAdapter.DrawerItemType.NORMAL.ordinal();
    }

    /**
     * Returns the text
     *
     * @return String
     */
    @Override
    public String getText() {
        return this.mText;
    }

    /**
     * Sets the text
     *
     * @param text
     */
    @Override
    public void setText(String text) {
        this.mText = text;
    }

}
