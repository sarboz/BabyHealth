package tj.zdaroviyRebonyk.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import tj.zdaroviyRebonyk.Fragments.SubCategoryFragment;

public class SubCategoryViewPagerAdapter extends FragmentPagerAdapter {
    private String id;
    private String[] authors = {"Все","Комаровский","Сирс","Сергей Бутрий"};

    public SubCategoryViewPagerAdapter(FragmentManager fm, String id) {
        super(fm);
        this.id = id;
    }

    @Override
    public Fragment getItem(int position) {
        return SubCategoryFragment.newInstance(id, authors[position],position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return authors[position];
    }

    @Override
    public int getCount() {
        return 4;
    }
}

