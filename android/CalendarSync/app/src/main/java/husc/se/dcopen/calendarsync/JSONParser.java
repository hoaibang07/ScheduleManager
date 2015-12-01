package husc.se.dcopen.calendarsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JSONParser {

    public static final String ADDRESS = "http://nckh.somee.com/Server.svc/rest/";

    private JSONParser() {}

    public static ArrayList<Task> syncDown(String userName) throws IOException, JSONException {
        ArrayList<Task> listTask = new ArrayList<>();
        String stringJSON = getStringJSON(ADDRESS+"DownSync/" +userName);

        JSONObject jsonObject = new JSONObject(stringJSON);
        JSONArray jsonArray = jsonObject.getJSONArray("DownSyncResult");

        for(int i = 0; i < jsonArray.length(); i++) {
            listTask.add(convertJSONtoTask(jsonArray.getJSONObject(i)));
        }

        return listTask;
    }

    public static boolean checkLogin(final String userName,final String password) throws IOException, JSONException {
        String urlString = ADDRESS + "Login/" + userName + "/" + password;
        String s = getStringJSON(urlString);
        JSONObject jsonObject = new JSONObject(s);
        return jsonObject.getBoolean("LoginResult");
    }

    private static String getStringJSON(final String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.connect();
        InputStream in = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder sb = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            sb.append(line);
        }

        if(urlConnection != null) {
            urlConnection.disconnect();
        }
        if(reader != null) {
            reader.close();
        }

        return sb.toString();
//        HttpClient httpClient=new DefaultHttpClient();
//
//        //Connect to the server
//        //here the 10.0.2.2 is the local host
//        HttpGet httpGet=new HttpGet(urlString);
//        //Get the response
//        HttpResponse httpResponse = null;
//        try {
//            httpResponse = httpClient.execute(httpGet);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        HttpEntity httpEntity = httpResponse.getEntity();
//        InputStream stream= null;
//        try {
//            stream = httpEntity.getContent();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//
//        StringBuilder sb = new StringBuilder();
//        String line;
//        try {
//            while((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return sb.toString();
//        return strResult[0];
    }

    public static void syncUp(String userName, Task task) {

    }

    private static Task convertJSONtoTask(JSONObject jsonObject) {
        Task task = null;
        try {
            String id = jsonObject.getString("ID");
            String taskName = jsonObject.getString("TaskName");
            String taskContent = jsonObject.getString("TaskContent");
            java.sql.Date beginDate = new java.sql.Date(jsonObject.getLong("BeginTime"));
            java.sql.Date endDate = new java.sql.Date(jsonObject.getLong("EndTime"));
            int type = jsonObject.getInt("Type");
            String accountName = jsonObject.getString("AccountName");
            String place = jsonObject.getString("Place");
            task = new Task(id, taskName, beginDate, endDate, place, taskContent, accountName, type, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return task;
    }
}
