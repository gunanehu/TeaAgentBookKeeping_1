package com.teaagent.ui.listEntries;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teaagent.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<String> data;
    private ItemClickListener itemClickListener;

    public CustomAdapter(List<String> data,ItemClickListener onNoteListener){
        this.data = data;
        this.itemClickListener = onNoteListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textView;
        ItemClickListener itemClickListener;

        public ViewHolder(View view,ItemClickListener itemClickListener){
            super(view);
            this.textView = (TextView) view.findViewById(R.id.textView);
            view.setOnClickListener(this);
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onClick(getAbsoluteAdapterPosition());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_design,parent,false);

        return new ViewHolder(view, this.itemClickListener);    }

    @Override
    public void onBindViewHolder( CustomAdapter.ViewHolder holder, int position) {
        holder.textView.setText(this.data.get(position));

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

}
