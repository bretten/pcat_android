package com.iotaink.pcat.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iotaink.pcat.R;

import java.util.List;

/**
 * Extension of ArrayAdapter to handle differentiating between normal and section items
 * in a ListView.
 *
 * Works with DrawerItem types
 */
public class DrawerArrayAdapter extends ArrayAdapter<DrawerItem> {

    /**
     * Context
     */
    private Context mContext;

    /**
     * Enum representing the different types of DrawerItems
     */
    public enum DrawerItemType {
        SECTION, NORMAL
    }

    /**
     * Constructor
     *
     * @param context
     * @param resource
     * @param objects
     */
    public DrawerArrayAdapter(Context context, int resource, DrawerItem[] objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    /**
     * Constructor
     *
     * @param context
     * @param resource
     * @param textViewResourceId
     * @param objects
     */
    public DrawerArrayAdapter(Context context, int resource, int textViewResourceId, List<DrawerItem> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
    }

    /**
     * Checks the type of the item (differentiated as just integers)
     *
     * @param position
     * @return An integer representing the type (corresponds to the DrawerItemType enum)
     */
    @Override
    public int getItemViewType(int position) {
        return this.getItem(position).getViewType();
    }

    /**
     * Gets the number of types
     *
     * @return The number of DrawerItem types
     */
    @Override
    public int getViewTypeCount() {
        return DrawerItemType.values().length;
    }

    /**
     * Checks whether a DrawerItem is clickable or not
     *
     * @param position
     * @return True means it is clickable, false means it is not
     */
    @Override
    public boolean isEnabled(int position) {
        if (this.getItemViewType(position) == DrawerItemType.SECTION.ordinal()) {
            return false;
        }
        return true;
    }

    /**
     * Gets the view for the item at the specified position.  If a view has already been used
     * it will be available in the ViewHolder to reuse
     *
     * Handles both types of DrawerItems separately
     *
     * TODO: Potentially simplify this by requiring DrawerItems to to implement this in the interface
     * but need to be sure to continue using the ViewHolder pattern
     *
     * @param position
     * @param convertView
     * @param parent
     * @return The view to show for the item at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // If the View is being shown for the first time, inflate it
        if (convertView == null) {
            // Inflate the view
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            // Determine the layout based on the type
            if (this.getItemViewType(position) == DrawerItemType.SECTION.ordinal()) {
                convertView = inflater.inflate(R.layout.drawer_section_item, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.drawer_normal_item, parent, false);
            }

            // Instantiate a ViewHolder to hold the contained Views
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.drawer_item_text);
            viewHolder.position = position;
            // Set the tag so it can be referenced later
            convertView.setTag(viewHolder);
        } else {
            // The View has already been used, so retrieve it
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the text of the ViewHolder's TextView
        viewHolder.textView.setText(this.getItem(position).getText());

        return convertView;
    }

    /**
     * ViewHolder for a basic item View containing ONLY a TextView
     */
    private static class ViewHolder {
        TextView textView;
        int position;
    }

}
