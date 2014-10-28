package com.whitneychampion.crashcourse.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.whitneychampion.crashcourse.R;
import com.whitneychampion.crashcourse.adapter.RestaurantAdapter;
import com.whitneychampion.crashcourse.api.ApiException;
import com.whitneychampion.crashcourse.api.RestaurantService;
import com.whitneychampion.crashcourse.api.impl.RestaurantServiceImpl;
import com.whitneychampion.crashcourse.listener.AsyncTaskCompleteListener;
import com.whitneychampion.crashcourse.api.ApiBase;
import com.whitneychampion.crashcourse.model.RestaurantList;
import com.whitneychampion.crashcourse.util.ApiResponseUtil;

public class ListRestaurantsActivity extends Activity {

    private ListView listView;
    private Context context;
    private RestaurantAdapter restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_restaurants_activity);

        context = this;

        listView = (ListView) findViewById(R.id.listView);

        // get restaurants
        RestaurantService service = new RestaurantServiceImpl();
        try {
            service.getRestaurantsAsynchronous(context, new GetRestaurantsListener());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private class GetRestaurantsListener implements AsyncTaskCompleteListener<ApiBase> {

        @Override
        public void onTaskComplete(ApiBase result) {

            // get restaurants
            RestaurantList restaurants;
            try {
                restaurants = (RestaurantList) ApiResponseUtil.parseResponse(result, RestaurantList.class);

                // populate adapter and attached it to the list view
                restaurantAdapter = new RestaurantAdapter(context, R.layout.restaurant_row, restaurants.getRestaurants());

                if (restaurants.getRestaurants().length!=0) {
                    listView.setAdapter(restaurantAdapter);
                    listView.setVisibility(View.VISIBLE);
                } else {
                    listView.setVisibility(View.GONE);
                }

            } catch (ApiException e) {
                Log.e("ApiError", e.getMessage());
                return;
            }
        }
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
            Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
