package com.example.futplay.Controllers.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.futplay.Controllers.Items.PlayersItem;
import com.example.futplay.R;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.PlayersViewHolder> {

    private ArrayList<PlayersItem> requestsList;

    public static class PlayersViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgVwCrdVwPlayer;

        public TextView txtVwCrdVwPlayerName;

        public PlayersViewHolder(@NonNull View view) {
            super(view);

            // Additional Settings
            viewsMatching(view);
        }

        private void viewsMatching(View view){
            imgVwCrdVwPlayer = view.findViewById(R.id.imgVwCrdVwPlayer);
            txtVwCrdVwPlayerName = view.findViewById(R.id.txtVwCrdVwPlayerName);
        }
    }

    public RequestsAdapter(ArrayList<PlayersItem> playersList) {
        this.requestsList = playersList;
    }

    @NonNull
    @Override
    public PlayersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        return new PlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayersViewHolder holder, int position) {
        PlayersItem currentItem = requestsList.get(position);

        holder.imgVwCrdVwPlayer.setImageResource(currentItem.getImage());
        holder.txtVwCrdVwPlayerName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }
}
