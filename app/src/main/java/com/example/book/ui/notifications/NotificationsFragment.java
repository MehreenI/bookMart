package com.example.book.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.ChatActivity;
import com.example.book.R;
import com.example.book.ui.Adapter.NotificationAdapter;
import com.example.book.ui.Model.Bid;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    FirebaseAuth sellerId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        notificationAdapter = new NotificationAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(notificationAdapter);

        notificationAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onDismissClick(int position) {
                // Handle dismissal click
                dismissClick(position);
            }

            @Override
            public void onStartChatClick(int position) {
                chatStart(position);
            }
        });

        // Load and set your notification data from Firebase
        getNotificationData();

        return root;
    }

    private void getNotificationData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            // User is logged in
            String currentUserId = firebaseAuth.getCurrentUser().getUid();

            DatabaseReference bidsRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(currentUserId)
                    .child("bids_Notification");

            bidsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<Bid> notificationList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Bid bid = snapshot.getValue(Bid.class);
                        if (bid != null) {
                            bid.setBidId(snapshot.getKey()); // Set the bidId here
                            notificationList.add(bid);
                        }
                    }

                    // Reverse the list to show the latest bids at the top
                    List<Bid> reversedList = new ArrayList<>(notificationList);
                    Collections.reverse(reversedList);

                    notificationAdapter.setNotificationList(reversedList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the error
                }
            });
        } else {
            // User is not logged in, or there are no notifications
            List<Bid> emptyList = Collections.emptyList();
            notificationAdapter.setNotificationList(emptyList);
        }
    }

    private void dismissClick(int position) {
        Log.e("notification", "remove Notification is calling");
        // Handle dismissal logic directly without calling removeNotification
        List<Bid> currentList = notificationAdapter.getNotificationList();
        if (position >= 0 && position < currentList.size()) {
            Bid removedBid = currentList.remove(position);
            notificationAdapter.setNotificationList(currentList);

            Log.d("notification", "Bid ID: " + removedBid.getBidId());

            if (removedBid != null && removedBid.getBidId() != null) {
                Log.e("notification", "remove Notification21 is calling");

                Log.d("notification", "Bid ID: " + removedBid.getBidId());

                // Get the Firebase reference for the bid you want to remove
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (firebaseAuth.getCurrentUser() != null) {
                    String bidId = removedBid.getBidId();
                    String currentUserId = firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference bidsRef = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(currentUserId)
                            .child("bids_Notification")
                            .child(bidId);

                    // Remove the bid from Firebase
                    bidsRef.removeValue()
                            .addOnSuccessListener(aVoid -> {
                                // Successfully removed from Firebase
                                // You may add additional logic here if needed
                            })
                            .addOnFailureListener(e -> {
                                // Failed to remove from Firebase
                                // You may add error handling logic here if needed
                                Log.e("notification", "Failed to remove from Firebase: " + e.getMessage());
                            });
                } else {
                    Log.e("notification", "Firebase user is null");
                }
            } else {
                Log.e("notification", "Invalid bid or bid ID is null");
            }
        }
    }

    private void chatStart(int position) {
        Log.e("notification", "Start Chat is calling");
        // Handle logic for starting a chat
        Bid notification = notificationAdapter.getNotificationList().get(position);

        // Start the chat with the bidder
        startChat(notification);
    }

    private void startChat(Bid notification) {
        // Example: Start a chat with the bidder

        // Get necessary information for starting the chat
        String postId = notification.getbookName();
        String bidderId = notification.getBidderId();

        // Replace this with your logic to start a chat using postId and bidderId
        // For example, you might navigate to a chat activity or fragment
        // or use Firebase Realtime Database to create a chat room

        // Example: Navigating to a ChatActivity
        Intent intent = new Intent(requireContext(), ChatActivity.class);
        intent.putExtra("postId", postId);
        intent.putExtra("bidderId", bidderId);
        startActivity(intent);
    }
}
