package apps.lnsel.com.vhortexttest.helpers.chatutil;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by albert on 14-3-21.//    MultipartRequest
 */
public class MultipartRequest extends Request<JSONObject> {
    public static final String KEY_PICTURE = "mypicture";
    public static final String KEY_PICTURE_NAME = "filename";
    public static final String KEY_ROUTE_ID = "route_id";
    private  String mimeType = "";
    private String fileType ="";
    private HttpEntity mHttpEntity;

    private String mRouteId;
    private Response.Listener mListener;
    private ProgressPercentListener mProgressListener;
    private File mFile;
    private int progress = 0;


    public interface ProgressPercentListener {
        void progressPercentage(int progress);
    }
    public MultipartRequest(String url, String filePath, String mimeType, String fileType,
                            Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mimeType = mimeType;
        this.fileType = fileType;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePath);
    }

    public MultipartRequest(String url, File file, String mimeType, String fileType, String routeId,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.mimeType = mimeType;
        this.fileType = fileType;
        mRouteId = routeId;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(file);
    }

    public void setProgressListener(ProgressPercentListener progressListener) {
        this.mProgressListener = progressListener;
    }

    private HttpEntity buildMultipartEntity(String filePath) {
        mFile = new File(filePath);
        return buildMultipartEntity(mFile);
    }

    private HttpEntity buildMultipartEntity(File file) {
        CustomMultipartEntity builder = new CustomMultipartEntity(new CustomMultipartEntity.ProgressListener() {
            @Override
            public void transferred(long num) {
                if(mProgressListener!=null) {
                    if (progress != (int) ((num / (float) new FileBody(
                            mFile).getContentLength()) * 100)) {
                        progress = (int) ((num / (float) new FileBody(
                                mFile).getContentLength()) * 100);
                        mProgressListener.progressPercentage(progress);

                    }
                }
            }
        });
        String fileName = file.getName();
        if (file.exists()) {
            FileBody fileBody = new FileBody(file);
            builder.addPart("attachment", fileBody);
        }
        try {
            builder.addPart("mimetype", new StringBody(".jpg"));
            builder.addPart("fileType", new StringBody("1"));
            builder.addPart("messageId", new StringBody(String.valueOf(System.currentTimeMillis())));
            builder.addPart("attachmentId", new StringBody(String.valueOf(System.currentTimeMillis())));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return builder;
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        String apikey = "apikey";
        String apikeyValue = "hgfyhfyi87hgc67";
        headers.put(apikey, apikeyValue);
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {

        mListener.onResponse(response);
    }
}