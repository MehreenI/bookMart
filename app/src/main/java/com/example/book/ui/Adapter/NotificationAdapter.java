package com.example.book.ui.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.R;
import com.example.book.manager.FirebaseManager;
import com.example.book.ui.Model.Bid;
import com.example.book.manager.UserRepository;
import com.example.book.AppController;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    
    private List<Bid> notificationList;
    private OnItemClickListener onItemClickListener;
    private Context context;
    
    public interface OnItemClickListener {
        void onDismissClick(int position);
        void onStartChatClick(int position);
    }
    
    public void setNotificationList(List<Bid> notifications) {
        notificationList = notifications;
        notifyDataSetChanged();
    }
    
    public List<Bid> getNotificationList() {
        return notificationList;
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.activity_notification, parent, false);
        return new NotificationViewHolder(view, onItemClickListener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Bid notification = notificationList.get(position);
        holder.bind(notification);
    }
    
    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }
    
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private TextView bookName;
        private TextView userName;
        private Button dismissButton;
        private Button startChatButton;
        
        NotificationViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            bookName = itemView.findViewById(R.id.bookname);
            userName = itemView.findViewById(R.id.UserName);
            dismissButton = itemView.findViewById(R.id.dissmiss);
            startChatButton = itemView.findViewById(R.id.startChat);
            
            dismissButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDismissClick(position);
                    }
                }
            });
            
            startChatButton.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onStartChatClick(position);
                    }
                }
            });
        }
        
        void bind(Bid notification) {
            text.setText("A Notification from buyer");
    
            UserRepository.getInstance().getBookNameById(notification.getBidderId(), new UserRepository.UserNameFetchCallback() {
                @Override
                public void onUserNameFetched(String bookName) {
                    userName.setText(bookName);
                }
        
                @Override
                public void onUserNameFetchFailed(String errorMessage) {
                    userName.setText("Book Name: Not Found");
                }
            });
    
            if (notification.getbookName() != null) {
                bookName.setText(notification.getbookName());
            } else {
                bookName.setText("BookName");
            }
        }
    }
}