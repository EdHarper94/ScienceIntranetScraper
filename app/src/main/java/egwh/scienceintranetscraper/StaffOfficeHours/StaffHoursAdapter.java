package egwh.scienceintranetscraper.StaffOfficeHours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import egwh.scienceintranetscraper.R;

/**
 * @file StaffHoursAdapter.java
 * @author Ed Harper
 * @date 20/04/2017
 *
 * List view adapter for staff hours,
 * Inflates staff_hours_view and adds data.
 */

public class StaffHoursAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;

    private ArrayList<StaffHours> staffHoursArrayList;

    /**
     * Initialises staff hours adapter
     * @param context adapter context
     * @param staffHoursArrayList staff hours to add to inflated view
     */
    public StaffHoursAdapter(Context context, ArrayList<StaffHours> staffHoursArrayList){
        this.context = context;
        this.inflater = (LayoutInflater.from(context));
        this.staffHoursArrayList = staffHoursArrayList;
    }

    /**
     * Gets the total modules in modules Arrau
     * @return
     */
    @Override
    public int getCount() {
        return staffHoursArrayList.size();
    }

    /**
     * Gets the item at the passes position
     * @param position the position in the expanded view
     * @return the module item at the passed position
     */
    @Override
    public Object getItem(int position) {
        return staffHoursArrayList.get(position);
    }

    /**
     * Gets id of module at a position
     * @param position the position in the expanded view
     * @return the id of the module at the passes position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Holds items for recycling
     */
    public class ViewHolder{
        public TextView nameView;
        public TextView hourView;
        public TextView updatedView;
    }

    /**
     * Inflates staff_hours_view, adds hours data and then returns the view
     * @param position the items position
     * @param convertView the view to be recycled
     * @param parent parent view
     * @return recycled convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        // If new view
        if(convertView == null){
            // Init view holder
            viewHolder = new ViewHolder();

            // Inflate new view
            convertView = inflater.inflate(R.layout.staff_hours_view, parent, false);

            // Init views
            viewHolder.nameView = (TextView)convertView.findViewById(R.id.name_view);
            viewHolder.hourView = (TextView)convertView.findViewById(R.id.hours_view);
            viewHolder.updatedView = (TextView)convertView.findViewById(R.id.updated_view);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set data
        viewHolder.nameView.setText(staffHoursArrayList.get(position).getFullName());
        viewHolder.hourView.setText(staffHoursArrayList.get(position).getHours());
        viewHolder.updatedView.setText(staffHoursArrayList.get(position).getLastUpdated());

        return convertView;
    }
}
