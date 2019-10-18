package tj.zdaroviyRebonyk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tj.zdaroviyRebonyk.Models.SubCategory;
import tj.zdaroviyRebonyk.R;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.Holder> {
    List<SubCategory> list;
    Context mctx;

    public SubCategoryAdapter(Context mctx, List<SubCategory> list) {
        this.list = list;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new Holder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.name.setText(list.get(i).getName());

        holder.author.setText(list.get(i).getAuthor());
        int id = mctx.getResources().getIdentifier(list.get(i).getImg().isEmpty() ? "" : list.get(i).getImg(), "mipmap", mctx.getPackageName());
        holder.img.setImageResource(id);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView name, author;
        ImageView img;

        public Holder(@NonNull View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            img = (ImageView) v.findViewById(R.id.img);
            author = (TextView) v.findViewById(R.id.author);
        }
    }
}
