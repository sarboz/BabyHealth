package tj.zdaroviyRebonyk.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import tj.zdaroviyRebonyk.Fragments.BookmarkFragment;
import tj.zdaroviyRebonyk.Fragments.CategoryFragment;
import tj.zdaroviyRebonyk.Fragments.SavedFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CategoryFragment.newInstance(position);
            case 1:
                return BookmarkFragment.newInstance(position);
            case 2:
                return SavedFragment.newInstance(position);
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Главы";
            case 1:
                return "Закладки";
            case 2:
                return "Цитаты";
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}