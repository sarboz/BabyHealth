package tj.zdaroviyRebonyk.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tj.zdaroviyRebonyk.Adapter.CategoryAdapter;
import tj.zdaroviyRebonyk.Adapter.RecyclerTouchListener;
import tj.zdaroviyRebonyk.MainActivity;
import tj.zdaroviyRebonyk.Models.Category;
import tj.zdaroviyRebonyk.R;
import tj.zdaroviyRebonyk.SubCatActivity;
import tj.zdaroviyRebonyk.Utils.DataServer;

public class CategoryFragment extends Fragment {

    RecyclerView rv_list;
    List<Category> list = new ArrayList<>();

    public static CategoryFragment newInstance(int index) {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_fragment, container, false);

        list = DataServer.getCategory(getActivity());
        rv_list = (RecyclerView) rootView.findViewById(R.id.rv_list);
        GridLayoutManager s = new GridLayoutManager(getContext(), 2);
        s.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == list.size() - 1 ? 2 : 1;
            }
        });
        rv_list.setLayoutManager(s);
        rv_list.setAdapter(new CategoryAdapter(getActivity(), list));

        rv_list.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_list, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(getContext(), SubCatActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("id", list.get(position).getId())
                        .putExtra("tit", list.get(position).getName()));

                MainActivity.log( list.get(position).getId(),list.get(position).getName());
            }
        }));

        if (list.isEmpty()) {
            rootView = inflater.inflate(R.layout.not_found, container, false);
            rootView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }
}