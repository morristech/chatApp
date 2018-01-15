package com.example.prabhat.chatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prabhat.chatapp.chat.ChatActivity;

import java.util.ArrayList;

import static com.example.prabhat.chatapp.MainActivity.RECEIVERID;
import static com.example.prabhat.chatapp.MainActivity.SENDERID;

/**
 * Created by prabhat on 13/1/18.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    private ArrayList<UserNameModel> itemList;
    private Context context;
    private String uid;

    // Constructor of the class
    public Adapter(ArrayList<UserNameModel> itemList, Context context, String uid) {
        this.context = context;
        this.itemList = itemList;
        this.uid = uid;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        item.setText(itemList.get(listPosition).getName());
    }

    // Static inner class to initialize the views of rows
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            item = itemView.findViewById(R.id.row_item);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(RECEIVERID, itemList.get(getAdapterPosition()).getUid());
            context.startActivity(intent);
        }
    }

}
