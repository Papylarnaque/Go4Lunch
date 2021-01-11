package com.edescamp.go4lunch.util;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.edescamp.go4lunch.R;
import com.edescamp.go4lunch.activity.MainActivity;
import com.edescamp.go4lunch.model.User;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.ALARM_SERVICE;
import static com.edescamp.go4lunch.R.string.notifications_no_internet_connection;

public class NotificationHelper {

    private static final String TAG = "NOTIFICATION";
    private final Context context;

    private final static AtomicInteger c = new AtomicInteger(0);
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    private PendingIntent pendingIntent;
    private String messageBody;

    private static final int[] TIME_NOTIFICATION = {12, 0};

    public NotificationHelper(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    public static void setAlarmForNotifications(Context context, Boolean notifications) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, TIME_NOTIFICATION[0]);
        calendar.set(Calendar.MINUTE, TIME_NOTIFICATION[1]);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            if (notifications) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            } else {
                alarmManager.cancel(pendingIntent);
            }
        }
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        setUpIntent();
    }

    private void setUpIntent() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        setUpMessage();
    }

    private void setUpMessage() {

        if (CheckConnectivity.isConnected(context)) {
            //  INTERNET ENABLED
            UserHelper.getAllUsers()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        ArrayList<User> users = new ArrayList<>();

                        for (DocumentSnapshot workmate : queryDocumentSnapshots.getDocuments()) {
                            users.add(workmate.toObject(User.class));
                        }
                        messageBody = setUpMessageWithUsersData(users);

                        sendNotification();
                    })
                    .addOnFailureListener(e -> {
                        messageBody = context.getString(R.string.notifications_firebase_failure);
                        sendNotification();
                    });

        } else {
            // INTERNET DISABLED
            messageBody = context.getString(notifications_no_internet_connection);
            sendNotification();
        }
    }

    public void sendNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_g4l)
                .setContentTitle(context.getString(R.string.notification_lunch_title))
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


        notificationManager.notify(getID(), builder.build());
    }


    private String setUpMessageWithUsersData(ArrayList<User> users) {

        ArrayList<User> usersWithoutCurrentUser = new ArrayList<>();

        String userId = SharedPrefs.getUserId(context);
        String chosenRestaurantId = SharedPrefs.getRestaurantId(context);
        String chosenRestaurantName = SharedPrefs.getRestaurantName(context);
        String chosenRestaurantAddress = SharedPrefs.getRestaurantAddress(context);

        for (User user : users) {
            if (!user.getUid().equals(userId)) {
                usersWithoutCurrentUser.add(user);
            }
        }

        if (chosenRestaurantName == null||chosenRestaurantName.equals("")) {
            // in case no restaurant is chosen
            // OR if user logged out but did not chose another restaurant after login
            return context.getString(R.string.notification_no_lunch_chosen);
        } else {
            ArrayList<String> lunchWorkmates = getWorkmates(usersWithoutCurrentUser, chosenRestaurantId);

            if (lunchWorkmates.size() > 0) {
                // At least one another workmate lunching with currentUser
                if (lunchWorkmates.size() == 1) {
                    // if 1 workmate joining
                    return context.getString(
                            R.string.notification_lunch_with_one_workmate,
                            chosenRestaurantName,
                            chosenRestaurantAddress,
                            lunchWorkmates.get(0));

                } else {
                    // if 1+ workmates joining
                    StringBuilder workmatesString = new StringBuilder();
                    for (String workmate : lunchWorkmates) {
                        if (workmate.equals(lunchWorkmates.get(lunchWorkmates.size() - 1))) {
                            workmatesString.append(workmate);
                        } else if (workmate.equals(lunchWorkmates.get(lunchWorkmates.size() - 2))) {
                            workmatesString.append(workmate);
                            workmatesString.append(context.getString(R.string.notification_workmatesstring_builder_and));
                        } else {
                            workmatesString.append(workmate);
                            workmatesString.append(", ");
                        }
                    }
                    return context.getString(
                            R.string.notification_lunch_with_few_workmate,
                            chosenRestaurantName,
                            chosenRestaurantAddress,
                            lunchWorkmates.size(),
                            workmatesString.toString());

                }
            } else {
                // No workmate lunching with currentUser
                return context.getString(R.string.notification_lunch_alone, chosenRestaurantName, chosenRestaurantAddress);
            }
        }
    }

    // provides workmates who are joining the same restaurant
    private ArrayList<String> getWorkmates(ArrayList<User> users, String restaurantId) {
        ArrayList<String> lunchWorkmates = new ArrayList<>();
        for (User user : users) {
            if (user.getChosenRestaurantId().equals(restaurantId)) {
                lunchWorkmates.add(user.getUsername());
            }
        }
        return lunchWorkmates;
    }

    // getID is a unique int for each notification
    public static int getID() {
        return c.incrementAndGet();
    }


}
