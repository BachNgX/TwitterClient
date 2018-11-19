package usth.edu.vn.twitterclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.TrendViewHolder> {

    private List<ListItem> listItems;

    public TrendAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private Context context;


    @NonNull
    @Override
    public TrendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item,viewGroup,false);
        return new TrendViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrendViewHolder trendViewHolder, int i) {
        ListItem listItem = listItems.get(i);
//        trendViewHolder.textViewTrend.setText(listItem.getHead());
//        trendViewHolder.textViewDesc.setText(listItem.getDesc());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class TrendViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView textViewTrend;
        public TextView textViewDesc;
        public TrendViewHolder(View v) {
            super(v);
            mView= v;
        }
        public void setHead (String head) {
            textViewTrend = mView.findViewById(R.id.trend);
            textViewTrend.setText(head);
        }
        public void setDesc (String desc) {
            textViewDesc = mView.findViewById(R.id.trend_description);
            textViewDesc.setText(desc);
        }

    }
}
