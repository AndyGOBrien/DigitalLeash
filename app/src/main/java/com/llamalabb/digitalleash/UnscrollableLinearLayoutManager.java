package com.llamalabb.digitalleash;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;


/**
 * Created by andy on 9/26/17.
 */

public class UnscrollableLinearLayoutManager extends LinearLayoutManager {

    public UnscrollableLinearLayoutManager(Context context){
        super(context);
    }
    public boolean canScrollVertically(){
        return false;
    }
}
