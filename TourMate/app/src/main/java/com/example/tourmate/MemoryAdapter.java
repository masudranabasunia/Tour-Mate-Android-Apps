package com.example.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploadList;

    public MemoryAdapter(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public MemoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_memory_details, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryAdapter.ViewHolder viewHolder, int i) {

        Upload upload = uploadList.get(i);
        Picasso.with(context)
                .load(upload.getImageUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .centerCrop()
                .rotate(90)
                .into(viewHolder.imageViewMA);
        viewHolder.imageCaptionMA.setText(upload.getImageCaption());

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewMA;
        private TextView imageCaptionMA;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewMA = itemView.findViewById(R.id.imageViewMAID);
            imageCaptionMA = itemView.findViewById(R.id.imageCaptionMAID);

        }

   /* public interface OnItemClickListenerMemory{

        void OnItemClick(int position);
        void onDelete(int position);

    }

    public void  setOnItemClickListenerMemory(MemoryAdapter.OnItemClickListenerMemory listenerMemory){

        this.listenerMemory = listenerMemory;

    } */
    }
}
