package tj.zdaroviyRebonyk.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import tj.zdaroviyRebonyk.Adapter.RecyclerTouchListener;
import tj.zdaroviyRebonyk.Adapter.SavedDataAdapter;
import tj.zdaroviyRebonyk.DataActivity;
import tj.zdaroviyRebonyk.MainActivity;
import tj.zdaroviyRebonyk.Models.SavedData;
import tj.zdaroviyRebonyk.R;
import tj.zdaroviyRebonyk.Utils.DataServer;

public class SavedFragment extends Fragment implements SavedDataAdapter.IDeleteItem {

    RecyclerView rv_list;
    List<SavedData> list = new ArrayList<>();
    SavedDataAdapter savedDataAdapter;

    public static SavedFragment newInstance(int index) {
        return new SavedFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_fragment, container, false);

        list = DataServer.getSavedData(getActivity());
        rv_list = (RecyclerView) rootView.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        savedDataAdapter = new SavedDataAdapter(getActivity(), list, this);
        rv_list.setAdapter(savedDataAdapter);


        if (list.isEmpty()) {
            rootView = inflater.inflate(R.layout.not_found, container, false);
            rootView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    @Override
    public void onDeleteItem(int pos) {

        String html = DataServer.getHtml(getActivity(), list.get(pos).getIdSubCat());

        Document document = Jsoup.parse(html);

        Elements s = document.select("span#" + list.get(pos).getIdElem());
        if (s.size() == 0)
            s = document.select("span");
        for (Element item : s) {
            if (item.attr("id").equals(list.get(pos).getIdElem()))
                item.removeAttr("style");
            else if (item.text().equals(list.get(pos).getText())) {
                item.removeAttr("style");
            }
        }

        DataServer.InsertHtml(getActivity(), list.get(pos).getIdSubCat(), document.toString(), true);
        DataServer.DelSavedData(getActivity(), list.get(pos).getId());
        list.remove(pos);
        savedDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int pos) {
        startActivity(new Intent(getActivity(), DataActivity.class)
                .putExtra("id", list.get(pos).getIdSubCat())
                .putExtra("pos",list.get(pos).getPos())
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        MainActivity.log(list.get(pos).getIdSubCat(),list.get(pos).getText());
    }
}

