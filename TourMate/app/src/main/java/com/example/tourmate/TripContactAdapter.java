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
import android.widget.TextView;

import java.util.List;

public class TripContactAdapter extends RecyclerView.Adapter<TripContactAdapter.ViewHolder> {

    private List<TripContact> tripContactList;
    private Context context;
    private OnItemClickListener listenerContact;

    public TripContactAdapter(List<TripContact> tripContactList, Context context) {
        this.tripContactList = tripContactList;
        this.context = context;
    }

    @NonNull
    @Override
    public TripContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_trip_contact,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripContactAdapter.ViewHolder viewHolder, int i) {

        TripContact tripContactCurrentUser = tripContactList.get(i);

        viewHolder.tripContactNameTV.setText(tripContactCurrentUser.getTripContactName());
        viewHolder.tripContactPhoneTV.setText(tripContactCurrentUser.getTripContactPhone());
        viewHolder.tripContactAddressTV.setText(tripContactCurrentUser.getTripContactAddress());

    }

    @Override
    public int getItemCount() {
        return tripContactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener{

        private TextView tripContactNameTV, tripContactPhoneTV, tripContactAddressTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tripContactNameTV = itemView.findViewById(R.id.showContactNameTVID);
            tripContactPhoneTV = itemView.findViewById(R.id.showContactPhoneTVID);
            tripContactAddressTV = itemView.findViewById(R.id.showContactAddressTVID);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(listenerContact!=null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){

                    switch (item.getItemId()){

                       /* case 1:
                            listenerContact.onEdit(position);
                            return true;  */

                        case 1:
                            listenerContact.onCall(position);
                            return true;

                        case 2:
                            listenerContact.onDelete(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onClick(View v) {

            if(listenerContact!=null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    listenerContact.OnItemClick(position);
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Choose an action");
            //MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");
            MenuItem call = menu.add(Menu.NONE,1,1,"Call");
            MenuItem delete = menu.add(Menu.NONE,2,2,"Delete");

            //edit.setOnMenuItemClickListener(this);
            call.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }
    }

    public interface OnItemClickListener{

        void OnItemClick(int position);
        //void onEdit(int position);
        void onCall(int position);
        void onDelete(int position);
    }

    public void  setOnItemClickListener(TripContactAdapter.OnItemClickListener listenerContact){

        this.listenerContact = listenerContact;

    }

    public void removeContact(int position) {
        tripContactList.remove(position);
        notifyItemRemoved(position);
    }
}
