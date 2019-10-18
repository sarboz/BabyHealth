package tj.zdaroviyRebonyk.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tj.zdaroviyRebonyk.Adapter.BookmarkAdapter;
import tj.zdaroviyRebonyk.DataActivity;
import tj.zdaroviyRebonyk.MainActivity;
import tj.zdaroviyRebonyk.Models.Bookmark;
import tj.zdaroviyRebonyk.R;
import tj.zdaroviyRebonyk.Utils.DataServer;

public class BookmarkFragment extends Fragment implements BookmarkAdapter.IDeleteItem {

    private static final String SECTION_NUMBER = "section_number";
    RecyclerView rv_list;
    List<Bookmark> list = new ArrayList<>();
    BookmarkAdapter adapter;


    public static BookmarkFragment newInstance(int index) {
        return  new BookmarkFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_fragment, container, false);

        list = DataServer.getAllBookmark(getActivity());
        rv_list = (RecyclerView) rootView.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new BookmarkAdapter(getActivity(), list, this);
        rv_list.setAdapter(adapter);


        if (list.isEmpty()) {
            rootView = inflater.inflate(R.layout.not_found, container, false);
            rootView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    @Override
    public void onDeleteItem(int pos) {
        DataServer.DelBookmark(getActivity(), list.get(pos).getIdSubCategory());
        list.remove(pos);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(int pos) {
      startActivity(new Intent(getContext(), DataActivity.class).putExtra("id",list.get(pos).getIdSubCategory()) .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        MainActivity.log(list.get(pos).getIdSubCategory(),list.get(pos).getText());
    }
}

