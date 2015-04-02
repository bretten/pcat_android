package com.iotaink.pcat.widget;

/**
 * Implementation of DrawerItem representing a section in ListView
 */
public class SectionItem implements DrawerItem {

    /**
     * The item text
     */
    private String mText;

    /**
     * Constructor
     *
     * @param text
     */
    public SectionItem(String text) {
        this.mText = text;
    }

    /**
     * Returns an integer representing the type of this DrawerItem
     *
     * Implementations of ArrayAdapter should delegate getItemViewType()
     * to this to determine the type
     *
     * @return An integer representing the View type
     */
    @Override
    public int getViewType() {
        return DrawerArrayAdapter.DrawerItemType.SECTION.ordinal();
    }

    /**
     * Gets the item text
     *
     * @return String
     */
    @Override
    public String getText() {
        return this.mText;
    }

    /**
     * Sets the item text
     *
     * @param text
     */
    @Override
    public void setText(String text) {
        this.mText = text;
    }

}
