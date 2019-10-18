package tj.zdaroviyRebonyk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;

import tj.zdaroviyRebonyk.Adapter.SubCategoryViewPagerAdapter;

public class SubCatActivity extends AppCompatActivity {
    SharedPreferences pref;
    String id = null, tit;
    private TabLayout tab;
    private SubCategoryViewPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_cat_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pref = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            id = getIntent().getExtras().getString("id", null);
            tit = getIntent().getExtras().getString("tit", "");
            pref.edit().putString("id", id).apply();
            pref.edit().putString("tit", tit).apply();
            pref.edit().putInt("posInPager", 0).apply();
        } catch (Exception e) {
            id = pref.getString("id", "0");
            tit = pref.getString("tit", "");
        }
        toolbar.setTitle(tit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fillData();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        pref.edit().putInt("posInPager",0).apply();
        super.onDestroy();
    }

    public void fillData() {
        mPagerAdapter = new SubCategoryViewPagerAdapter(getSupportFragmentManager(), id);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        tab = findViewById(R.id.sliding_tabs);
        tab.setupWithViewPager(mViewPager);
    }


}
