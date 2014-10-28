package com.whitneychampion.crashcourse.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.whitneychampion.crashcourse.Constants;
import com.whitneychampion.crashcourse.listener.AsyncTaskCompleteListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.Map;

/**
 * Created by Whitney Champion on 10/25/14.
 */
public class ApiHelper {

    // API URL
    private static int timeout = 10000;
    public static final String GET_RESTAURANTS = "restaurants.json";

    public static void get(String url, Context context, Type type,
                           AsyncTaskCompleteListener<Object> callback, Map<String, String> headers) {
        new ApiRequestAsyncTask(context, type, callback, headers).execute(url, "GET");
    }

    public static void post(String url, String jsonEntity, Context context, Type type,
                            AsyncTaskCompleteListener<Object> callback, Map<String, String> headers) {
        new ApiRequestWithEnclosureAsyncTask(context, type, callback, headers).execute(url, "POST", jsonEntity);
    }

    public static void multipartPost(String url, File file, Context context, Type type,
                            AsyncTaskCompleteListener<Object> callback, Map<String, String> headers) {
        new ApiRequestWithFileEnclosureAsyncTask(context, file, type, callback, headers).execute(url, "POST", null);
    }

    public static void multipartPut(String url, File file, Context context, Type type,
                                     AsyncTaskCompleteListener<Object> callback, Map<String, String> headers) {
        new ApiRequestWithFileEnclosureAsyncTask(context, file, type, callback, headers).execute(url, "PUT", null);
    }

    public static void put(String url, String jsonEntity, Context context, Type type,
                           AsyncTaskCompleteListener<Object> callback, Map<String, String> headers) {
        new ApiRequestWithEnclosureAsyncTask(context, type, callback, headers).execute(url, "PUT", jsonEntity);
    }

    public static void delete(String url, Context context, Type type,
                              AsyncTaskCompleteListener<Object> callback, Map<String, String> headers) {
        new ApiRequestAsyncTask(context, type, callback, headers).execute(url, "DELETE");
    }

    public static void head(String url, Context context, Type type,
                            AsyncTaskCompleteListener<Object> callback, Map<String, String> headers) {
        new ApiRequestAsyncTask(context, type, callback, headers).execute(url, "HEAD");
    }

    public static Object get(String url, Context context, Type type, Map<String, String> headers) {
        return getDelete(url, "GET", type, context, headers);
    }

    public static Object post(String url, String jsonEntity, Context context, Type type, Map<String, String> headers) {
        return putPost(url, "POST", jsonEntity, type, context, headers);
    }

    public static Object put(String url, String jsonEntity, Context context, Type type, Map<String, String> headers) {
        return putPost(url, "PUT", jsonEntity, type, context, headers);
    }

    public static Object delete(String url, Context context, Type type, Map<String, String> headers) {
        return getDelete(url, "DELETE", type, context, headers);
    }

    public static int head(String url) {
        return doHead(url);
    }

    private static class ApiRequestWithEnclosureAsyncTask extends AsyncTask<String, Void, Object> {

        private Context context;
        private Type type;
        private AsyncTaskCompleteListener<Object> callback;
        private Map<String, String> headerParams;

        public ApiRequestWithEnclosureAsyncTask(Context context, Type type,
                                                AsyncTaskCompleteListener<Object> callback,
                                                Map<String, String> headerParams) {
            this.context = context;
            this.type = type;
            this.callback = callback;
            this.headerParams = headerParams;
        }

        @Override
        protected Object doInBackground(String... params) {

            // Parse parameters
            String url = params[0];
            String requestType = params[1];
            String jsonEntity = params[2];

            return putPost(url, requestType, jsonEntity, type, context, headerParams);
        }

        @Override
        protected void onPostExecute(Object object) {
            callback.onTaskComplete(object);
        }
    }

    private static class ApiRequestWithFileEnclosureAsyncTask extends AsyncTask<String, Void, Object> {

        private Context context;
        private Type type;
        private AsyncTaskCompleteListener<Object> callback;
        private Map<String, String> headerParams;
        private File profilePic;

        public ApiRequestWithFileEnclosureAsyncTask(Context context, File file, Type type,
                                                AsyncTaskCompleteListener<Object> callback,
                                                Map<String, String> headerParams) {
            this.context = context;
            this.type = type;
            this.callback = callback;
            this.headerParams = headerParams;
            this.profilePic = file;
        }

        @Override
        protected Object doInBackground(String... params) {

            // Parse parameters
            String url = params[0];
            String requestType = params[1];

            return putPost(url, requestType, profilePic, type, context, headerParams);
        }

        @Override
        protected void onPostExecute(Object object) {
            callback.onTaskComplete(object);
        }
    }

    private static class ApiRequestAsyncTask extends AsyncTask<String, Void, Object> {

        private Context context;
        private Type type;
        private AsyncTaskCompleteListener<Object> callback;
        private Map<String, String> headerParams;

        public ApiRequestAsyncTask(Context context, Type type,
                                   AsyncTaskCompleteListener<Object> callback,
                                   Map<String, String> headerParams) {
            this.context = context;
            this.type = type;
            this.callback = callback;
            this.headerParams = headerParams;
        }

        @Override
        protected Object doInBackground(String... params) {

            // Parse parameters
            String url = params[0];
            String requestType = params[1];

            if (requestType.equals("HEAD")) {
                return doHead(url);
            } else {
                return getDelete(url, requestType, type, context, headerParams);
            }
        }

        @Override
        protected void onPostExecute(Object object) {
            callback.onTaskComplete(object);
        }
    }

    private static int doHead(String url) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpRequestBase httpRequest;
        httpRequest = new HttpHead(getAbsoluteUrl(url));

        // Set Secure Header Information
        Date timestamp = new Date();
        setHttpRequestHeader(httpRequest, timestamp, null);

        // Execute HTTP Request
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest);
        } catch (IOException e) {
            Log.e("error", "Could not execute API HttpRequest.", e);
        }

        return response.getStatusLine().getStatusCode();
    }

    private static Object putPost(String url, String requestType, String jsonEntity, Type type,
                                  Context context, Map<String, String> extraHeaderParams) {

        // Create HttpClient
        HttpClient httpClient = new DefaultHttpClient();
        HttpEntityEnclosingRequestBase httpRequest;
        if(requestType.equalsIgnoreCase("POST")) {
            httpRequest = new HttpPost(getAbsoluteUrl(url));
        } else if(requestType.equalsIgnoreCase("PUT")) {
            httpRequest = new HttpPut(getAbsoluteUrl(url));
        } else {
            Log.e("error", "Incorrect HTTP Request was made (" + requestType + "). Expecting POST or PUT");
            throw new IllegalArgumentException("Incorrect HTTP Request was made (" + requestType + "). " +
                    "Expecting POST or PUT");
        }

        // Set Secure Header Information
        Date timestamp = new Date();
        if(url.contains("?")){
            url = url.substring(0, url.indexOf("?"));
        }
        setHttpRequestHeader(httpRequest, timestamp, extraHeaderParams);

        // Create and set JSON Entity
        setHttpRequestEntity(httpRequest, jsonEntity);

        // Execute HTTP Request
        Object result = null;
        try {
            HttpResponse response = httpClient.execute(httpRequest);
            String responseJson = parseHttpResponse(response);
            Gson gson = new Gson();

            // If an error occurs due to expiring token, refresh & retry
            if (responseJson.contains(Constants.TOKEN_AUTH_ERROR_MESSAGE)) {
                responseJson = retryRequest(httpRequest, httpClient, context);
            }

            // If an error occurs when interacting with the API
            Boolean isErrorResponse = responseJson.contains(Constants.API_ERROR_MESSAGE);
            if(responseJson == null || isErrorResponse) {
                result = gson.fromJson(responseJson, ApiError.class);
            }

            //check that we're expecting a result before we attempt a transform
            if (type != null && !isErrorResponse) {
                result = gson.fromJson(responseJson, type);
            }

        } catch (IOException e) {
            Log.e("error", "Could not execute API HttpRequest.", e);
        } catch (JsonSyntaxException e) {
            Log.e("error", "Could not parse JSON response.", e);
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

        return result;
    }

    private static Object putPost(String url, String requestType, File file, Type type,
                                  Context context, Map<String, String> extraHeaderParams) {

        // Create HttpClient

        HttpParams httpParams = new BasicHttpParams();

        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpEntityEnclosingRequestBase httpRequest;
        if(requestType.equalsIgnoreCase("POST")) {
            httpRequest = new HttpPost(getAbsoluteUrl(url));
        } else if(requestType.equalsIgnoreCase("PUT")) {
            httpRequest = new HttpPut(getAbsoluteUrl(url));
        } else {
            Log.e("error", "Incorrect HTTP Request was made (" + requestType + "). Expecting POST or PUT");
            throw new IllegalArgumentException("Incorrect HTTP Request was made (" + requestType + "). " +
                    "Expecting POST or PUT");
        }

        // Set Secure Header Information
        Date timestamp = new Date();
        if(url.contains("?")){
            url = url.substring(0, url.indexOf("?"));
        }
        setHttpRequestHeader(httpRequest, timestamp, extraHeaderParams);

        // Create and set JSON Entity
        setHttpRequestEntity(httpRequest, file);

        // Execute HTTP Request
        Object result = null;
        try {
            HttpResponse response = httpClient.execute(httpRequest);
            String responseJson = parseHttpResponse(response);
            Gson gson = new Gson();

            // If an error occurs due to expiring token, refresh & retry
            if (responseJson.contains(Constants.TOKEN_AUTH_ERROR_MESSAGE)) {
                responseJson = retryRequest(httpRequest, httpClient, context);
            }

            // If an error occurs when interacting with the API
            Boolean isErrorResponse = responseJson.contains(Constants.API_ERROR_MESSAGE);
            if(responseJson == null || isErrorResponse) {
                result = gson.fromJson(responseJson, ApiError.class);
            }

            //check that we're expecting a result before we attempt a transform
            if (type != null && !isErrorResponse) {
                result = gson.fromJson(responseJson, type);
            }

        } catch (IOException e) {
            Log.e("error", "Could not execute API HttpRequest.", e);
        } catch (JsonSyntaxException e) {
            Log.e("error", "Could not parse JSON response.", e);
        }

        return result;
    }

    private static Object getDelete(String url, String requestType, Type type, Context context,
                                    Map<String, String> extraHeaderParams) {


        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
        HttpConnectionParams.setSoTimeout(httpParams, timeout);

        // Create HttpClient
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpRequestBase httpRequest;

        if(requestType.equalsIgnoreCase("GET")) {
            httpRequest = new HttpGet(url);
        } else if(requestType.equalsIgnoreCase("DELETE")) {
            httpRequest = new HttpDelete(url);
        } else {
            Log.e("error", "Incorrect HTTP Request was made (" + requestType + "). Expecting GET or DELETE");
            throw new IllegalArgumentException("Incorrect HTTP Request was made (" + requestType + "). " +
                    "Expecting GET or DELETE");
        }

        // Set Secure Header Information
        Date timestamp = new Date();
        if(url.contains("?")){
            url = url.substring(0, url.indexOf("?"));
        }
        setHttpRequestHeader(httpRequest, timestamp, extraHeaderParams);

        // Execute HTTP Request
        Object result = null;
        try {
            HttpResponse response = httpClient.execute(httpRequest);
            String responseJson = parseHttpResponse(response);
            Gson gson = new Gson();

            // If an error occurs due to expiring token, refresh & retry
            if (responseJson.contains(Constants.TOKEN_AUTH_ERROR_MESSAGE)) {
                responseJson = retryRequest(httpRequest, httpClient, context);
            }

            // If an error occurs when interacting with the API
            Boolean isErrorResponse = responseJson.contains(Constants.API_ERROR_MESSAGE) || responseJson.contains("401 Unauthorized");
            if(responseJson == null || isErrorResponse) {
                result = gson.fromJson(responseJson, ApiError.class);
            }

            // If 401 Unauthorized, spit back error
            Boolean isErrorResponse401 = responseJson.equals("");
            if (isErrorResponse401) {
            }

            //check that we're expecting a result before we attempt a transform
            if (type != null && !isErrorResponse && !isErrorResponse401) {
                result = gson.fromJson(responseJson, type);
            }

        }
        catch (SocketTimeoutException e) {
            ApiError error = new ApiError();
            error.setErrorCode("500");
            error.setErrorMessage("Socket timed out");
            result = error;
        }
        catch (ConnectTimeoutException e) {
            ApiError error = new ApiError();
            error.setErrorCode("500");
            error.setErrorMessage("Connection timed out");
            result = error;
        }
        catch (IOException e) {
            Log.e("error", "Could not execute API HttpRequest.", e);
        } catch (JsonSyntaxException e) {
            Log.e("error", "Could not parse JSON response.", e);
        }

        return result;
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return Constants.API_URL+relativeUrl;
    }

    private static void setHttpRequestHeader(HttpRequestBase httpRequest, Date timestamp,
                                             Map<String, String> extraHeaders) {
        httpRequest.setHeader("X-Stream-Timestamp", timestamp.getTime() / 1000 + "");
        if (extraHeaders!=null) {
            for (String extraHeaderKey:extraHeaders.keySet()) {
                httpRequest.setHeader(extraHeaderKey, extraHeaders.get(extraHeaderKey));
            }
        }
    }

    private static void setHttpRequestEntity(HttpEntityEnclosingRequestBase httpRequest, File file) {
        if(file!=null) {
            httpRequest.setEntity(new FileEntity(new File(file.getAbsolutePath()), "binary/octet-stream"));
        }
    }

    private static void setHttpRequestEntity(HttpEntityEnclosingRequestBase httpRequest, String jsonEntity) {
        if(StringUtils.isNotEmpty(jsonEntity)) {
            try {
                StringEntity entity = new StringEntity(jsonEntity, HTTP.UTF_8);
                entity.setContentEncoding(HTTP.UTF_8);
                entity.setContentType("application/json");
                httpRequest.setHeader("Content-Type", "application/json");
                httpRequest.setHeader("Accept", "application/json");
                httpRequest.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                Log.e("error", "Could not convert jsonObject to a StringEntity", e);
            }
        }
    }

    private static String retryRequest(HttpRequestBase request, HttpClient client,
                                             Context context) throws IOException {

        HttpResponse response = client.execute(request);
        return parseHttpResponse(response);
    }


    private static String parseHttpResponse(HttpResponse httpResponse) {

        try {
            // Parse Response into a String and convert to JSONObject
            if (httpResponse.getEntity() == null) {
                return null;
            } else {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader((httpResponse.getEntity().getContent())));

                String output;
                StringBuilder builder = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    builder.append(output);
                }

                return builder.toString();
            }
        } catch (IOException e) {
            Log.e("error", "Could not read API HttpResponse.", e);
            return null;
        }
    }
}