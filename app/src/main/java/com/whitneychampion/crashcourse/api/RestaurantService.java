package com.whitneychampion.crashcourse.api;

import android.content.Context;

import com.whitneychampion.crashcourse.listener.AsyncTaskCompleteListener;
import com.whitneychampion.crashcourse.model.Restaurant;

/**
 * Created by Whitney Champion on 8/23/14.
 */
public interface RestaurantService {

    void getRestaurantsAsynchronous(Context context, AsyncTaskCompleteListener listener)
            throws ApiException;

}
