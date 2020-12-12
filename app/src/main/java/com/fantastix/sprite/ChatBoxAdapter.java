package com.fantastix.sprite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fantastix.android.sprite.R;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder> {
    private List<Message2> MessageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;

        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.nickname);
            message = (TextView) view.findViewById(R.id.message);

        }
    }

    // in this adapter constructor we add the list of message as a parameter so that
    // we will pass it when making an instance of the adapter object in our activity

    public ChatBoxAdapter(List<Message2>MessagesList) {
        this.MessageList = MessagesList;
    }

    @Override public int getItemCount() {
        return MessageList.size();
    }

    @Override public ChatBoxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ChatBoxAdapter.MyViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final ChatBoxAdapter.MyViewHolder holder) {
        // binding the data from our ArrayList of object to the item.xml using the view

        Message2 m = MessageList.get(position);
        holder.nickname.setText(m.getNickname());

        holder.message.setText(m.getMessage());
    }
}
