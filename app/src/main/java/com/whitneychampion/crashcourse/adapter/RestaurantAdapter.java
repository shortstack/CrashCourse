package com.whitneychampion.crashcourse.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whitneychampion.crashcourse.R;
import com.whitneychampion.crashcourse.activity.RestaurantActivity;
import com.whitneychampion.crashcourse.api.RestaurantService;
import com.whitneychampion.crashcourse.api.impl.RestaurantServiceImpl;
import com.whitneychampion.crashcourse.model.Restaurant;

/**
 * Created by Whitney Champion on 8/23/14.
 */
public class RestaurantAdapter extends ArrayAdapter<Restaurant> {

    private RestaurantService service = new RestaurantServiceImpl();
    int resource;
    Context context;
    Restaurant[] data;

    public RestaurantAdapter(Context context, int resource, Restaurant[] items) {
        super(context, resource, items);
        this.resource = resource;
        this.context = context;
        this.data = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RestaurantHolder holder = null;
        View row = convertView;

        // get restaurant
        final Restaurant restaurant = data[position];

        if ( row == null )
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(resource, parent, false);

            // get layout items
            holder = new RestaurantHolder();
            holder.name = (TextView) row.findViewById(R.id.restaurant_name);
            holder.container = (LinearLayout) row.findViewById(R.id.restaurant_holder);

            row.setTag(holder);

        } else {
            holder = (RestaurantHolder)row.getTag();
        }

        // set name of restaurant
        holder.name.setText(restaurant.getName());

        // set onclick listener for restaurant
        holder.container.setOnClickListener(new RestaurantOnClickListener(restaurant));

        return row;
    }

    // this identifies each section of the row layout holding each restaurant object
    static class RestaurantHolder {
        TextView name;
        LinearLayout container;
    }

    // restaurant onclick listener
    class RestaurantOnClickListener implements View.OnClickListener {

        Restaurant restaurant;

        private RestaurantOnClickListener(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void onClick(View view) {

            // open restaurant activity
            Intent intent = new Intent(context, RestaurantActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("restaurant", restaurant);
            context.startActivity(intent);

        }

    }

}