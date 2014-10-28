package com.whitneychampion.crashcourse.model;

import com.whitneychampion.crashcourse.api.ApiBase;

/**
 * Created by Whitney Champion on 4/21/14.
 */
public class RestaurantList extends ApiBase {

    private Restaurant[] restaurants;

    public Restaurant[] getRestaurants() {
        return restaurants;
    }

    public void set(Restaurant[] restaurants) {
        this.restaurants = restaurants;
    }

}