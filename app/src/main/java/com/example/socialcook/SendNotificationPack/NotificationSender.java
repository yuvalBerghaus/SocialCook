package com.example.socialcook.SendNotificationPack;
// use wrap two parts : data object and the
// token of the user to whom you want to send the notification.
public class NotificationSender {
    public Data data;
    public String to;
    public NotificationSender(Data data, String to) {
        System.out.println("the message in notification sender is "+data.getMessage()+" and token is"+to);
        this.data = data;
        this.to = to;
    }
}
