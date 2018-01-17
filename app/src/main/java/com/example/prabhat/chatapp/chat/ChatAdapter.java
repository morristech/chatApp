package com.example.prabhat.chatapp.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prabhat.chatapp.R;
import com.example.prabhat.chatapp.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhat on 13/1/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_REC = 1;
    private static final int TYPE_SENDER = 2;
    private Context context;
    private List<ChatModel> list = new ArrayList<>();
    private String senderId;

    public ChatAdapter(Context context, List<ChatModel> list, String id) {
        this.context = context;
        this.list = list;
        this.senderId = id;
    }


    @Override
    public int getItemViewType(int position) {
        if (!isPositionReciver(position)) {
            return TYPE_REC;
        } else {
            return TYPE_SENDER;
        }
    }

    private boolean isPositionReciver(int position) {
        return TextUtils.equals(list.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENDER) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.sender_view, parent, false);

            return new SenderViewHolder(itemView);
        } else {
            View itemView2 = LayoutInflater.from(context)
                    .inflate(R.layout.reciver_view, parent, false);
            return new RecViewHolder(itemView2);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.text.setText(list.get(position).getMessage());
        } else if (holder instanceof RecViewHolder) {
            RecViewHolder recViewHolder = (RecViewHolder) holder;
            recViewHolder.textView.setText(list.get(position).getMessage());
        }

    }

    private class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView text;

        private SenderViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.senderTextView);
        }
    }

    private class RecViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        private RecViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recTextView);
        }
    }

    public void notifyMe() {
        notifyItemInserted(list.size() - 1);
    }
}
