package tj.zdaroviyRebonyk;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.StoreType;
import tj.zdaroviyRebonyk.Adapter.ViewPagerAdapter;
import tj.zdaroviyRebonyk.Fragments.SubCategoryFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapter mPagerAdapter;
    private TabLayout tab;
    private ViewPager mViewPager;
    FragmentManager ft = getSupportFragmentManager();
    Fragment newFragment;
    private static FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fillData();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public void fillData() {
        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        tab = findViewById(R.id.sliding_tabs);
        tab.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mPagerAdapter);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Нажмите еще раз для выхода.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public static void log(String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(1)
                .setShowLaterButton(true)
                .setDebug(false)
                .setStoreType(StoreType.GOOGLEPLAY)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        if (id == R.id.nav_like) {
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Couldn't launch the market", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_share) {
            try {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        ImageView searchClose = (ImageView) searchView.findViewById(R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_close_white_24dp);
        EditText text = (EditText) searchView.findViewById(R.id.search_src_text);
        text.setHintTextColor(Color.WHITE);
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    FrameLayout f = findViewById(R.id.frame);
                    f.setVisibility(View.VISIBLE);
                    newFragment = SubCategoryFragment.newInstance(newText);
                    ft.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .setCustomAnimations(R.animator.side_in, R.animator.side_out)
                            .replace(R.id.frame, newFragment).commit();
                    return true;
                }
                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (newFragment != null) {
                    ft.beginTransaction().remove(newFragment).commit();
                    FrameLayout f = findViewById(R.id.frame);
                    f.setVisibility(View.GONE);
                    searchView.setIconified(true);
                    return true;
                } else {
                    searchView.setIconified(true);
                    return true;
                }
            }
        });

        return true;
    }


}
