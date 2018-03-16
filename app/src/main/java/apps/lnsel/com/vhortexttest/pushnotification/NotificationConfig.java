package apps.lnsel.com.vhortexttest.pushnotification;


import java.util.ArrayList;

public class NotificationConfig {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final int NOTIFICATION_ID_CREATOR = 102;

    public static final int PRIVATE_NOTIFICATION_ID = 103;
    public static final int GROUP_NOTIFICATION_ID = 104;

    public static final String SHARED_PREF = "ah_firebase";

    public static int private_message_count=0;
    public static int group_message_count=0;
    public static ArrayList<String> private_chat_count=new ArrayList<>();
    public static ArrayList<String> group_chat_count=new ArrayList<>();
}
