package ui.dona.winwel.com.employee;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class VolleyHelper {

    public static final String SERVER_API = "http://192.168.1.36:5000/api";

    private final int TIMEOUT = 5000;

    private RequestQueue queue;

    private static VolleyHelper mInstance = new VolleyHelper();

    public static synchronized VolleyHelper getInstance () {
        if (null == mInstance) {
            mInstance = new VolleyHelper();
        }
        return mInstance;
    }

    public interface OnGetDataListener {
        void onSucess(String data);
        void onError(String error);
    }

    public void getAll(Context context, String path, final OnGetDataListener listener){

        queue = Volley.newRequestQueue(context);
        StringRequest stringRequest =  new StringRequest(StringRequest.Method.GET,
                SERVER_API + path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onSucess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error.toString());
                }
        });

        queue.add(stringRequest);
    }

    public interface OnAddListener {
        void onSucess(String data);
        void onError(String error);
    }

    public void add(Context context, String path, JSONObject jsonObject, final OnAddListener listener){
        queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest =  new JsonObjectRequest(
                JsonObjectRequest.Method.POST,
                SERVER_API + path,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onSucess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.toString());
            }
        });
    }


}
