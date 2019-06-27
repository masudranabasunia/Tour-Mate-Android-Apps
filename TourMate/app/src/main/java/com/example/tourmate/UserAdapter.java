package com.example.tourmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_user_details,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder viewHolder, int i) {

        User currentUser = userList.get(i);
        viewHolder.nameTVUA.setText(currentUser.getName());
        viewHolder.emailTVUA.setText(currentUser.getEmail());
        viewHolder.phoneTVUA.setText(currentUser.getPhone());
        viewHolder.idTVUA.setText(currentUser.getId());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTVUA,emailTVUA,phoneTVUA,idTVUA;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTVUA = itemView.findViewById(R.id.nameTVUAID);
            emailTVUA = itemView.findViewById(R.id.emailTVUAID);
            phoneTVUA = itemView.findViewById(R.id.phoneTVUAID);
            idTVUA = itemView.findViewById(R.id.idTVUAID);
        }
    }
}
