package tj.zdaroviyRebonyk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tj.zdaroviyRebonyk.Models.SavedData;
import tj.zdaroviyRebonyk.R;

public class SavedDataAdapter extends RecyclerView.Adapter<SavedDataAdapter.Holder> {
    List<SavedData> list;
    Context mctx;
    IDeleteItem iDeleteItem;

    public SavedDataAdapter(Context mctx, List<SavedData> list, IDeleteItem i) {
        this.list = list;
        this.mctx = mctx;
        this.iDeleteItem = i;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_saved, viewGroup, false);
        return new Holder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {

        if (list.get(i).getComent().length() > 0)
            holder.comment.setText(list.get(i).getComent());
        else {
            holder.comment.setVisibility(View.GONE);
        }
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(mctx, v);
                p.getMenu().add(1, 1, 1, "Удалить");
                p.show();
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        iDeleteItem.onDeleteItem(i);
                        return true;
                    }
                });
            }
        });

        holder.name.setText(list.get(i).getText());
        if (list.get(i).getText().length() > 30)
            holder.name.setText(list.get(i).getText().substring(0, 30) + "...");
        CardView c = (CardView) holder.more.getParent().getParent();
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDeleteItem.onItemClick(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comment;
        ImageView more;

        public Holder(@NonNull View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            comment = (TextView) v.findViewById(R.id.comment);
            more = (ImageView) v.findViewById(R.id.more);
        }
    }

    public interface IDeleteItem {
        void onDeleteItem(int pos);

        void onItemClick(int pos);

    }
}
