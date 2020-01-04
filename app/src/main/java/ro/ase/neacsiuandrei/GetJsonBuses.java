package ro.ase.neacsiuandrei;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ro.ase.neacsiuandrei.adapters.BusAdapter;
import ro.ase.neacsiuandrei.models.Bus;

import static java.net.HttpURLConnection.HTTP_OK;

public class GetJsonBuses extends AsyncTask<String, Void, String> {
    private static final String TAG = GetJsonSubway.class.getName();
    private Handler mHandler;
    private BusAdapter busAdapter;

    public GetJsonBuses(Handler handler, BusAdapter busAdapter) {
        this.mHandler = handler;
        this.busAdapter = busAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String jsonFile) {
        super.onPostExecute(jsonFile);
        try {
            JSONObject jsonObject = new JSONObject(jsonFile);
            JSONArray jsonArray = jsonObject.getJSONArray("buses");
            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                Bus newBus = new Bus(jsonItem.getInt("number"),
                        jsonItem.getInt("capacity"),
                        jsonItem.getInt("avgTime"),
                        jsonItem.getInt("rating"),
                        jsonItem.getBoolean("recommend"));
                boolean check = false;
                for(Bus b : busAdapter.busList){
                    if(b.getNumber()==newBus.getNumber())
                        check=true;
                }
                if(check==false){
                    busAdapter.busList.add(newBus);
                    busAdapter.notifyDataSetChanged();
                    busAdapter.notifyDatabaseChanged(newBus);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... strings) {
        String jsonFile = null;

        HttpURLConnection hcon = null;
        try {
            URL u = new URL(strings[0]);
            URLConnection con = u.openConnection();
            if (con instanceof HttpURLConnection) {
                hcon = (HttpURLConnection) con;
                hcon.connect();
                int resultCode = hcon.getResponseCode();
                if (resultCode == HTTP_OK) {
                    InputStream is = hcon.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int noBytes = 0;
                    while ((noBytes = is.read(buffer, 0, buffer.length)) != -1) {
                        baos.write(buffer, 0, noBytes);
                    }
                    jsonFile = baos.toString();

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", jsonFile);
                    message.setData(bundle);
                    mHandler.handleMessage(message);

                }
            } else {
                throw new Exception("Invalid HTTP Connection");
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        } finally {
            if (hcon != null)
                hcon.disconnect();
        }

        return jsonFile;
    }
}
