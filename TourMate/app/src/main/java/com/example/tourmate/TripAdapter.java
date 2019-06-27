package com.example.tourmate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private List<TripInfo> tripInfoList;
    private Context context;
    private OnItemClickListener listener;

    public TripAdapter(List<TripInfo> tripInfoList, Context context) {
        this.tripInfoList = tripInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_trip_details,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripAdapter.ViewHolder viewHolder, final int i) {

        TripInfo tCurrentUser = tripInfoList.get(i);
        viewHolder.showTripName.setText(tCurrentUser.getTrpName());
        viewHolder.showTripStartDate.setText(tCurrentUser.getTrpStartDate());
        viewHolder.showTripEndDate.setText(tCurrentUser.getTrpEndDate());

        /* viewHolder.popUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context,viewHolder.popUpMenu);
                popupMenu.getMenuInflater().inflate(R.menu.trip_popup_menu,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){

                            case R.id.edit_menu:
                                break;
                            case R.id.delete_menu:

                                break;
                            case R.id.details_menu:
                                break;
                        }
                        return false;
                    }
                });
            }
        });  */

    }

    @Override
    public int getItemCount() {
        return tripInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {

        private TextView showTripName, showTripStartDate, showTripEndDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showTripName = itemView.findViewById(R.id.showTripNameTVID);
            showTripStartDate = itemView.findViewById(R.id.showTripStartDateTVID);
            showTripEndDate = itemView.findViewById(R.id.showTripEndDateTVID);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    listener.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Choose an action");
            //MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
            MenuItem delete = menu.add(Menu.NONE,1,1,"Delete");
            MenuItem details = menu.add(Menu.NONE,2,2,"Details");

            //edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
            details.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(listener!=null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){

                    switch (item.getItemId()){

                        /*case 1:
                            listener.onEdit(position);
                            return true; */

                        case 1:
                            listener.onDelete(position);
                            return true;

                        case 2:
                            listener.onDetails(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{

        void OnItemClick(int position);
        //void onEdit(int position);
        void onDelete(int position);
        void onDetails(int position);
    }

    public void  setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;

    }

    public void remove(int position) {
        tripInfoList.remove(position);
        notifyItemRemoved(position);
    }
}
