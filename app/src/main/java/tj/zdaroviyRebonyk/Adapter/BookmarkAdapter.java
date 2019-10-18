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

import tj.zdaroviyRebonyk.Models.Bookmark;
import tj.zdaroviyRebonyk.R;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.Holder> {
    List<Bookmark> list;
    Context mctx;
    IDeleteItem Idelete;

    public BookmarkAdapter(Context mctx, List<Bookmark> list, IDeleteItem i) {
        this.list = list;
        this.mctx = mctx;
        this.Idelete = i;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bookmark, viewGroup, false);
        return new Holder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {

        holder.name.setText(list.get(i).getText());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(mctx, v);
                p.getMenu().add(1, 1, 1, "Удалить");
                p.show();
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Idelete.onDeleteItem(i);
                        return true;
                    }
                });
            }
        });
        CardView cardView = (CardView) holder.more.getParent().getParent();
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Idelete.onItemClick(i);
            }
        });
        int id = mctx.getResources().getIdentifier(list.get(i).getImg(), "mipmap", mctx.getPackageName());
        holder.img.setImageResource(id);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView more, img;

        public Holder(@NonNull View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            more = (ImageView) v.findViewById(R.id.more);
            img = (ImageView) v.findViewById(R.id.img);
        }
    }

    public interface IDeleteItem {
        void onDeleteItem(int pos);

        void onItemClick(int pos);
    }
}
