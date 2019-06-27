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

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {

    private List<WalletInfo> walletInfoList;
    private Context context;
    private OnItemClickListener listenerWallet;

    public WalletAdapter(List<WalletInfo> walletInfoList, Context context) {
        this.walletInfoList = walletInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public WalletAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_wallet_details,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletAdapter.ViewHolder viewHolder, int i) {

        WalletInfo walletInfoCurrentUser = walletInfoList.get(i);

        viewHolder.showExpenseNameTV.setText(walletInfoCurrentUser.getTripWalletName());
        viewHolder.showExpenseAmountTV.setText(walletInfoCurrentUser.getTripWalletAmount()+" tk");
        viewHolder.showExpenseDateTV.setText(walletInfoCurrentUser.getTripWalletDate());
    }

    @Override
    public int getItemCount() {
        return walletInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private TextView showExpenseNameTV, showExpenseAmountTV, showExpenseDateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showExpenseNameTV = itemView.findViewById(R.id.showExpenseNameTVID);
            showExpenseAmountTV = itemView.findViewById(R.id.showExpenseAmountTVID);
            showExpenseDateTV = itemView.findViewById(R.id.showExpenseDateTVID);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {

            if(listenerWallet!=null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    listenerWallet.OnItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose an action");

            MenuItem delete = menu.add(Menu.NONE,1,1,"Delete");
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listenerWallet!=null){
                int position = getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){

                    switch (item.getItemId()){
                        case 1:
                            listenerWallet.onDelete(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{

        void OnItemClick(int position);
        void onDelete(int position);
    }

    public void  setOnItemClickListener(WalletAdapter.OnItemClickListener listenerWallet){

        this.listenerWallet = listenerWallet;

    }

    public void removeContact(int position) {
        walletInfoList.remove(position);
        notifyItemRemoved(position);
    }
}
