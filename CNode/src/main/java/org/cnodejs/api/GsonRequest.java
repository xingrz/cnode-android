package org.cnodejs.api;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public abstract class GsonRequest<T> extends Request<T> {

    public static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new Iso8601DateTypeAdapter())
            .create();

    private final Class<T> type;

    public GsonRequest(int method, Class<T> type, String url) {
        super(method, url.startsWith("/") ? Constants.API_V1 + url : url, null);
        this.type = type;
    }

    public GsonRequest(int method, Class<T> type, String url, Object... segments) {
        this(method, type, String.format(url, segments));
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, type),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    public void enqueue(RequestQueue queue) {
        queue.add(this);
    }

}