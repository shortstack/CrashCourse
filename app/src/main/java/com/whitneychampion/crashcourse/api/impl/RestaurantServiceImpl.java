package com.whitneychampion.crashcourse.api.impl;

import android.content.Context;

import com.whitneychampion.crashcourse.Constants;
import com.whitneychampion.crashcourse.api.ApiException;
import com.whitneychampion.crashcourse.api.ApiHelper;
import com.whitneychampion.crashcourse.api.RestaurantService;
import com.whitneychampion.crashcourse.listener.AsyncTaskCompleteListener;
import com.whitneychampion.crashcourse.model.Restaurant;
import com.whitneychampion.crashcourse.model.RestaurantList;

/**
 * Created by Whitney Champion on 8/23/14.
 */
public class RestaurantServiceImpl implements RestaurantService {

    private static final String EXCEPTION_MESSAGE = "An error occurred.";

    @Override
    public void getRestaurantsAsynchronous(Context context, AsyncTaskCompleteListener listener)
            throws ApiException {

        //try to make call
        String url = Constants.API_URL + ApiHelper.GET_RESTAURANTS;
        try {
            ApiHelper.get(url, context, RestaurantList.class, listener, null);
        } catch (Exception e) {
            throw new ApiException(EXCEPTION_MESSAGE, null);
        }
    }

}
