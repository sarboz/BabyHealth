package tj.zdaroviyRebonyk.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import tj.zdaroviyRebonyk.Adapter.RecyclerTouchListener;
import tj.zdaroviyRebonyk.Adapter.SubCategoryAdapter;
import tj.zdaroviyRebonyk.DataActivity;
import tj.zdaroviyRebonyk.MainActivity;
import tj.zdaroviyRebonyk.Models.SubCategory;
import tj.zdaroviyRebonyk.R;
import tj.zdaroviyRebonyk.Utils.DataServer;

public class SubCategoryFragment extends Fragment {

    RecyclerView rv_list;
    List<SubCategory> list = new ArrayList<>();

    private String mobileAdsID = "ca-app-pub-2117126233262177~5138283053";
    private String interstitialID = "ca-app-pub-2117126233262177/3501127124";
    private InterstitialAd interstitial;


    public static SubCategoryFragment newInstance(String index, String author, int pos) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        Bundle b = new Bundle();
        b.putString("id", index);
        b.putString("author", author);
        b.putInt("pos", pos);
        fragment.setArguments(b);
        return fragment;
    }

    public static SubCategoryFragment newInstance(String text) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        Bundle b = new Bundle();
        b.putString("text", text);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        MobileAds.initialize(getActivity(), mobileAdsID);
        interstitial = new InterstitialAd(getActivity());
        interstitial.setAdUnitId(interstitialID);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                startActivity(i);
            }
        });

        if (!interstitial.isLoading() && !interstitial.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
        }
        super.onCreate(savedInstanceState);
    }

    public void showInterstitialAdmob(Intent i) {
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();
        } else {
            startActivity(i);
        }
    }

    Intent i = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_fragment, container, false);

        final String id = getArguments().getString("id", null);
        final String author = getArguments().getString("author", null);
        final String text = getArguments().getString("text", null);
        if (id == null && text != null)
            list = DataServer.getSubCategory(getActivity(), text);
        else
            list = DataServer.getSubCategory(getActivity(), id, author);
        rv_list = (RecyclerView) rootView.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_list.setAdapter(new SubCategoryAdapter(getActivity(), list));


        rv_list.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rv_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                i = new Intent(getContext(), DataActivity.class)
                        .putExtra("id", list.get(position).getId());
                showInterstitialAdmob(i);
            }
        }));


        if (list.isEmpty()) {
            rootView = inflater.inflate(R.layout.not_found, container, false);
            rootView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


}

