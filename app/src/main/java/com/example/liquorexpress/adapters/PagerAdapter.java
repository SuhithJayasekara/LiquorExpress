package com.example.liquorexpress.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.liquorexpress.CategoriesFragment;
import com.example.liquorexpress.HomeFragment;
import com.example.liquorexpress.OrderFragment;
import com.example.liquorexpress.UserFragment;

public class PagerAdapter extends FragmentStateAdapter {
    private int tabCount;
    private boolean isAdmin;

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity, int count, boolean isAdmin) {
        super(fragmentActivity);
        this.tabCount = count;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (isAdmin) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance("0", "H");
                case 1:
                    return CategoriesFragment.newInstance("0", "H");
                case 2:
                    return UserFragment.newInstance("0", "H");
                case 3:
                    return OrderFragment.newInstance("0", "H");
                default:
                    return null;
            }
        } else {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance("0", "H");
                case 1:
                    return CategoriesFragment.newInstance("0", "H");
                case 2:
                    return OrderFragment.newInstance("0", "H");
                default:
                    return null;
            }
        }
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}
