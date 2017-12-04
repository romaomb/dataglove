package com.example.matheus.dataglove;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONCallback {
    void onSuccess(JSONObject result) throws JSONException;
    void onError(String result) throws Exception;
}

