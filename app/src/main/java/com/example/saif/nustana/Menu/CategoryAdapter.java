package com.example.saif.nustana.Menu;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saif.nustana.Menu.Fragments.BurgerFragment;
import com.example.saif.nustana.Menu.Fragments.DessertsFragment;
import com.example.saif.nustana.Menu.Fragments.DrinksFragment;
import com.example.saif.nustana.Menu.Fragments.OthersFragment;
import com.example.saif.nustana.Menu.Fragments.PizzaFragment;

public class CategoryAdapter extends FragmentPagerAdapter {

    /**
     * Context of the app
     */
    private Context mContext;


    public CategoryAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new BurgerFragment();
        else if (position == 1)
            return new PizzaFragment();
        else if (position == 2)
            return new DessertsFragment();
        else if (position == 3)
            return new DrinksFragment();
        else
            return new OthersFragment();

    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {

        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "BURGERS";
        else if (position == 1)
            return "PIZZA";
        else if (position == 2)
            return "DESSERTS";
        else if (position == 3)
            return "DRINKS";
        else
            return "Others";
    }
}
