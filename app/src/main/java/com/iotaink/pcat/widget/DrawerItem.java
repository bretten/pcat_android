package com.iotaink.pcat.widget;

/**
 * Interface to model a ListView item for a navigation drawer
 *
 * Currently represents a simple item with only text
 */
public interface DrawerItem {

    /**
     * Returns an integer representing the type of this DrawerItem
     *
     * Implementations of ArrayAdapter should delegate getItemViewType()
     * to this to determine the type
     *
     * @return An integer representing the View type
     */
    public int getViewType();

    /**
     * Should return the text for this DrawerItem
     *
     * @return String
     */
    public String getText();

    /**
     * Should set the text for this DrawerItem
     *
     * @param text
     */
    public void setText(String text);

}
