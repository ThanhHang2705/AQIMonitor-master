package fimo.uet.fairapp.Data;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 4/24/2017.
 */

public class GetDataFromAPI extends AsyncTask<String,String,String>{
    String ID;
    String JsonResult=null;
    Context context;
    KQNode kqNode;
    @Override
    protected String doInBackground(String... params) {
        return null;
    }

    public String getJsonFromServer(String url) throws IOException {
        //String url= "https://api.waqi.info/feed/@"+ID+"/?token=1b4e3291a2257461524a84a5b6e7b7097f2203fa";
        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));

        // read the JSON results into a string
        JsonResult = inputStream.readLine();
        return JsonResult;
    }
}
