package apps.lnsel.com.vhortexttest.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;

import apps.lnsel.com.vhortexttest.R;
import apps.lnsel.com.vhortexttest.data.ContactSetget;
import apps.lnsel.com.vhortexttest.data.GroupContactSetget;

/**
 * Created by apps2 on 7/14/2017.
 */
public class CommonMethods {

    private static final float BLUR_RADIUS = 10.0f;
    private static final float BITMAP_SCALE = 0.4f;

    @SuppressLint("InlinedApi")
    public static NetworkInfo networkInfo;

    public static DisplayMetrics getScreenWidth(Context mContext) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        return displaymetrics;
    }

    public static boolean checkBuildVersionAsyncTask() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ? true : false;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


    public static void MYToast(Context context, String txt) {
        Toast toast = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // get display width
    public static DisplayMetrics getScreen(Context mContext) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        return displaymetrics;
    }


    public static String getUTFDecodedString(String mStr) {

        try {
            mStr = URLDecoder.decode(mStr, "UTF-8");
            // mStr= StringEscapeUtils.unescapeJava(mStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mStr = iosCompatibleChartoSpace(mStr);
        return mStr;
    }

    private static String iosCompatibleChartoSpace(String mStr) {
        if (mStr.contains("%20"))
            mStr = mStr.replace("%20", " ");
        mStr = mStr.replace("¶¶2", ",");
        mStr = mStr.replace("¶¶1", "'");
        return mStr;
    }

    @SuppressWarnings("deprecation")
    public static long FreeInternalMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Free = ((long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize()) / 1048576;
        return Free;
    }

    @SuppressWarnings("deprecation")
    public static long FreeExternalMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long Free = ((long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize()) / 1048576;
        return Free;
    }

    public static ArrayList<ContactSetget> getContact(Activity mBaseActivity, Uri uriContact) {
        ArrayList<ContactSetget> mArrList = new ArrayList<ContactSetget>();
        ContactSetget mDataContact;
        String TAG = mBaseActivity.getClass().getSimpleName();
        String contactID = "";
        String contactNumber = "", Type = "";
        String contactName = "";
        Bitmap photo = null;

        // getting contacts ID
        Cursor cursorID = mBaseActivity.getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Cursor cursor = mBaseActivity.getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(mBaseActivity.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                assert inputStream != null;
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = mBaseActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                new String[]{contactID},
                null);
        if (cursorPhone != null && cursorPhone.getCount() > 0) {
            cursorPhone.moveToFirst();
            do {
                mDataContact = new ContactSetget();
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Type = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                System.out.println("Phone Type  ----   "+cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

                mDataContact.setContactName(contactName);
                mDataContact.setmBitmap(CommonMethods.encodeTobase64(photo, 100));
                mDataContact.setContactNumber(contactNumber);

                mDataContact.setContactType("Mobile");

                ArrayList<String> temp_contactList=new ArrayList<>();
                for(int j=0;j<mArrList.size();j++){
                    temp_contactList.add(mArrList.get(j).contactNumber);
                }
                if(!temp_contactList.contains(contactNumber)){
                    mArrList.add(mDataContact);
                }

               /* switch (Integer.parseInt(Type)) {
                    case Phone.TYPE_HOME:
                        mDataContact.setContactType("Home");
                        break;
                    case Phone.TYPE_MOBILE:
                        mDataContact.setContactType("Mobile");
                        break;
                    case Phone.TYPE_WORK:
                        mDataContact.setContactType("Work");
                        break;
                    default:
                        break;
                }*/

                // mArrList.add(mDataContact);
            }
            while (cursorPhone.moveToNext());
        }
        cursorPhone.close();




        return mArrList;
    }

    public static ArrayList<GroupContactSetget> getGroupContact(Activity mBaseActivity, Uri uriContact) {
        ArrayList<GroupContactSetget> mArrList = new ArrayList<GroupContactSetget>();
        GroupContactSetget mDataContact;
        String TAG = mBaseActivity.getClass().getSimpleName();
        String contactID = "";
        String contactNumber = "", Type = "";
        String contactName = "";
        Bitmap photo = null;

        // getting contacts ID
        Cursor cursorID = mBaseActivity.getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Cursor cursor = mBaseActivity.getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(mBaseActivity.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                assert inputStream != null;
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = mBaseActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                new String[]{contactID},
                null);
        if (cursorPhone != null && cursorPhone.getCount() > 0) {
            cursorPhone.moveToFirst();
            do {
                mDataContact = new GroupContactSetget();
                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Type = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                System.out.println("Phone Type  ----   "+cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

                mDataContact.setContactName(contactName);
                mDataContact.setmBitmap(CommonMethods.encodeTobase64(photo, 100));
                mDataContact.setContactNumber(contactNumber);

                mDataContact.setContactType("Mobile");

                ArrayList<String> temp_contactList=new ArrayList<>();
                for(int j=0;j<mArrList.size();j++){
                    temp_contactList.add(mArrList.get(j).contactNumber);
                }
                if(!temp_contactList.contains(contactNumber)){
                    mArrList.add(mDataContact);
                }

               /* switch (Integer.parseInt(Type)) {
                    case Phone.TYPE_HOME:
                        mDataContact.setContactType("Home");
                        break;
                    case Phone.TYPE_MOBILE:
                        mDataContact.setContactType("Mobile");
                        break;
                    case Phone.TYPE_WORK:
                        mDataContact.setContactType("Work");
                        break;
                    default:
                        break;
                }*/

                // mArrList.add(mDataContact);
            }
            while (cursorPhone.moveToNext());
        }
        cursorPhone.close();




        return mArrList;
    }

    public static String encodeTobase64(Bitmap image, int quality) {
        if (image == null)
            return "";
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.NO_WRAP);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        //  byte[] data = Base64.decode(input, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static int dpToPx(Context mContext, int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static String timeAMPM(String _24HourTime){

        String _12time="";
        try {
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm:ss a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            _12time=_12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _12time;
    }


    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static String convertedDateByTimezone(String date, String time, String fromTimezone2, int toTimezone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int fromTimezone = Integer.parseInt(fromTimezone2);
        try {
            Date mDate = sdf.parse(date+" "+time);
            long timeInMilliseconds = mDate.getTime();
            long timezoneAdd = toTimezone - fromTimezone;
            long convertedTime = timeInMilliseconds+timezoneAdd;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String convertedDate = formatter.format(new Date(convertedTime));
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String convertedTimeByTimezone(String date, String time, String fromTimezone2, int toTimezone) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // changes..
        int fromTimezone = Integer.parseInt(fromTimezone2);
        try {
            Date mDate = sdf.parse(date+" "+time);
            long timeInMilliseconds = mDate.getTime();
            long timezoneAdd = toTimezone - fromTimezone;
            long convertedTime = timeInMilliseconds+timezoneAdd;
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String convertedDate = formatter.format(new Date(convertedTime));
            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getCurrentUTCDate(){
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }

    public static String getCurrentUTCTime(){
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("HH:mm:ss.SSS"); //changes
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }

    public static String getCurrentUTCTimeZone(){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        int rawoffset = tz.getRawOffset();
        String timezone = String.valueOf(rawoffset);
        return timezone;
    }

    public static String getMonthNameForInt(int num) {
        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public static String getCurrentTime(){
        Calendar c = Calendar.getInstance();
       // System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
       // Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

        return  formattedDate;
    }

    public static void setListViewHeightBasedOnChildren(Activity mActivity,
                                                        ListView list) {

        ListAdapter listAdapter = list.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(list.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, list);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            mActivity.getWindowManager().getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    displayMetrics.widthPixels, View.MeasureSpec.EXACTLY);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    displayMetrics.heightPixels, View.MeasureSpec.AT_MOST);
            view.measure(widthMeasureSpec, heightMeasureSpec);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = totalHeight
                + (list.getDividerHeight() * (listAdapter.getCount() - 1));
        list.setLayoutParams(params);
        list.requestLayout();
    }


    public static String getAlertMessageFromException(Context context,
                                                      Throwable exception) {
        if (exception == null) {
            return context.getResources().getString(R.string.alert_failure_unknown_error);
        }
        if (exception instanceof SocketTimeoutException
                || exception instanceof ConnectTimeoutException) {

            return context.getResources().getString(R.string.alert_failure_network_time_out);
        } else if (exception instanceof HttpHostConnectException) {
            return context.getResources().getString(
                    R.string.alert_failure_network_connection_failed);
        } else if (exception instanceof UnknownHostException) {
            return context.getResources().getString(
                    R.string.alert_failure_network_connection_failed);
        } else if (exception instanceof SSLPeerUnverifiedException
                || exception instanceof SSLHandshakeException
                || exception instanceof NoResponseFromServerException) {
            return context.getResources().getString(
                    R.string.alert_failure_server_connection_failed_);
        } else {
            return context.getResources().getString(R.string.alert_failure_unknown_error);
        }

    }

    public static String getFormattedDate(String inputTime,String inputFormat,String outputFormat) {

        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                inputFormat);
        try {
            date = simpleDateFormat.parse(inputTime);
        } catch (ParseException e1) {
            e1.printStackTrace();
            return inputTime;
        }

        return new SimpleDateFormat(outputFormat).format(date);

        // in android device try.....
        // return new
        // SimpleDateFormat(dateTimeFormat,context.getResources().getConfiguration().locale).format(date);
    }

    public static Boolean  dateCompare(String msgDate){
        Boolean isTrue=false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date(System.currentTimeMillis());

            String currentDateUTC = CommonMethods.getCurrentUTCDate();
            String currenttimeUTC = CommonMethods.getCurrentUTCTime();

            Date date1 = sdf.parse(currentDateUTC);
            Date date2 = sdf.parse(msgDate);


            System.out.println("date1 : " + sdf.format(date1));
            System.out.println("date2 : " + sdf.format(date2));

            if (date1.compareTo(date2) > 0) {
                System.out.println("Date1 is after Date2");
                isTrue=false;
            } else if (date1.compareTo(date2) < 0) {
                System.out.println("Date1 is before Date2");
                isTrue=false;
            } else if (date1.compareTo(date2) == 0) {
                System.out.println("Date1 is equal to Date2");
                isTrue=true;
            } else {
                System.out.println("How to get here?");
            }
        }catch (Exception e){
            System.out.println("Exception");
        }

        return isTrue;


    }







}
