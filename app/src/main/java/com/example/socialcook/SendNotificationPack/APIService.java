package com.example.socialcook.SendNotificationPack;
// use to send request for notification to firebase server
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA8W53yXc:APA91bHXoE3oXyuCG1GixUIOoy9A8M3EiBbXIF5QH-nrHRTTgZ8l-RSExlX4ALFVnFWFXfGg7YWKZZzPQ9IR_kxksiLDguhRoTBmUfEHGC6qD1UfBTAMalL3WU-MCarVxh36EDCTNG3u" // Your server key refer to video for finding your server key
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
