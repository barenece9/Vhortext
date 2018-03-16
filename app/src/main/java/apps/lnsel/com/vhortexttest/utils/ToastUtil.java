package apps.lnsel.com.vhortexttest.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import apps.lnsel.com.vhortexttest.R;

/**
 * Created by apps2 on 7/11/2017.
 */
public class ToastUtil {

    public static void showAlertToast(Context context, String text_msg,
                                      ToastType type) {
        Toast toast = new Toast(context);
        View row = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.custom_toast, null);
        toast.setView(row);
        setToast(context, type,
                (TextView) row.findViewById(R.id.text_toast_msg), text_msg);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }

    private static void setToast(Context context, ToastType toastType,
                                 TextView textView, String _msg) {
        switch (toastType) {
            case FAILURE_ALERT:
                (textView).setText(_msg);
                (textView).setTextColor(context.getResources().getColor(
                        R.color.view_gray_color));
                break;
            case SUCCESS_ALERT:
                (textView).setText(_msg);
                (textView).setTextColor(context.getResources().getColor(
                        R.color.green));
                break;
            default:
                break;
        }
    }

    /**
     * if no data is there after parsing
     * **/
    public static void showToastForNoData(Context context) {
        Toast toast = new Toast(context);
        View row = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.custom_toast, null);

        toast.setView(row);
        ((TextView) row.findViewById(R.id.text_toast_msg)).setText("No data available");
        ((TextView) row.findViewById(R.id.text_toast_msg)).setTextColor(context
                .getResources().getColor(R.color.view_gray_color));
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * if section is still not complete
     * **/
    public static void showToastForUnderDevelopment(Context context) {
        Toast toast = new Toast(context);
        View row = ((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.custom_toast, null);
        toast.setView(row);
        ((TextView) row.findViewById(R.id.text_toast_msg)).setText("under Development");
        ((TextView) row.findViewById(R.id.text_toast_msg)).setTextColor(context
                .getResources().getColor(R.color.view_yellow_color));
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}