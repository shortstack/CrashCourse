package com.whitneychampion.crashcourse.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.whitneychampion.crashcourse.R;
import com.whitneychampion.crashcourse.model.Restaurant;

public class RestaurantActivity extends Activity {

    private Restaurant restaurant;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_activity);

        context = this;

        // get restaurant
        if ((getIntent().getParcelableExtra("restaurant")!=null)) {
            restaurant = getIntent().getParcelableExtra("restaurant");
        }

        // get layout parts
        TextView restaurant_name = (TextView) findViewById(R.id.restaurant_name);
        TextView restaurant_address = (TextView) findViewById(R.id.restaurant_address);
        TextView restaurant_citystate = (TextView) findViewById(R.id.restaurant_citystate);

        // populate restaurant data
        restaurant_name.setText(restaurant.getName());
        restaurant_address.setText(restaurant.getAddress());
        restaurant_citystate.setText(restaurant.getCity() + ", " + restaurant.getState());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(context,"Settings",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
