package com.example.saif.nustana.Order;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saif.nustana.Menu.Fragments.BurgerFragment;
import com.example.saif.nustana.Menu.Fragments.DessertsFragment;
import com.example.saif.nustana.Menu.Fragments.DrinksFragment;
import com.example.saif.nustana.Menu.Fragments.OthersFragment;
import com.example.saif.nustana.Menu.Fragments.PizzaFragment;
import com.example.saif.nustana.Order.Fragments.DeliveredFragment;
import com.example.saif.nustana.Order.Fragments.DeliveringFragment;
import com.example.saif.nustana.Order.Fragments.PendingFragment;

public class OrdersCategoryAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;


    public OrdersCategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0)
            return new PendingFragment();
        else if (position == 1)
            return new DeliveringFragment();
        else
            return new DeliveredFragment();
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {

        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "PENDING";
        else if (position == 1)
            return "DELIVERING";
        else
            return "DELIVERED";
    }
}

