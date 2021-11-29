package com.example.traveloka;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPgAdapter extends FragmentStatePagerAdapter {

    public ViewPgAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new fragment_trangchu();
            case 1:
                return new fragment_xe();

            case 2:
                return new fragment_lich();

            default:
                return new fragment_trangchu();

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
