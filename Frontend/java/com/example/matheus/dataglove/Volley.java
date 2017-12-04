package com.example.matheus.dataglove;

import android.content.Context;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class Volley {
    private Context context;
    private int[] gpioPorts = {11, 13, 15, 29, 31, 33, 35, 37, 8, 16, 18, 22, 32, 36, 38, 12};
    private int sensorQtd = gpioPorts.length;
    private RajawaliRenderer renderer;
    private boolean stop = false;

    Volley(Context context, RajawaliRenderer renderer) {
        this.context = context;
        this.renderer = renderer;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    void getJSON() {
        String url = "http://10.1.1.1:8080";

        makeRequest(url, new JSONCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                float[] convertedAngleY = new float[sensorQtd];
                for (int i = 0; i < result.length(); i++) {
                    convertedAngleY[i] = Float.valueOf(result.getString(""+gpioPorts[i]));

                }
                renderer.rotate(convertedAngleY);
                if (!stop) {
                    getJSON();
                }
            }

            @Override
            public void onError(String result) throws Exception {
                Toast.makeText(context, "Erro inesperado.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void makeRequest(final String url, final JSONCallback callback) {
        JSONHandler rq = new JSONHandler(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String err = null;
                        if (error instanceof com.android.volley.NoConnectionError){
                            err = "Sem conexão com a Dataglove.";
                        }
                        try {
                            if(err != null) {
                                callback.onError(err);
                            }
                            else {
                                callback.onError(error.getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };
        rq.setPriority(Request.Priority.HIGH);
        VolleyController.getInstance(context).addToRequestQueue(rq);
    }
}
