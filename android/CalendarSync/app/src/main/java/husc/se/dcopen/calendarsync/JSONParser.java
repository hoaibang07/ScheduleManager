package husc.se.dcopen.calendarsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class JSONParser {

    public static final String ADDRESS = "http://nckh.somee.com/Server.svc/rest/";

    private JSONParser() {}

    public static ArrayList<Task> syncDown(String userName, int soNgay) throws IOException, JSONException {
        ArrayList<Task> listTask = new ArrayList<>();
        String stringJSON = getStringJSON(ADDRESS+"DownSync/" +userName + "/" + soNgay);

        if(stringJSON != null) {
            JSONObject jsonObject = new JSONObject(stringJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("DownSyncResult");

            for (int i = 0; i < jsonArray.length(); i++) {
                listTask.add(convertJSONtoTask(jsonArray.getJSONObject(i)));
            }
            return listTask;
        } else {
            return null;
        }
    }

    public static boolean checkLogin(final String userName,final String password) throws IOException, JSONException {
        String urlString = ADDRESS + "Login/" + userName + "/" + password;
        String s = getStringJSON(urlString);
        if(s != null) {
            JSONObject jsonObject = new JSONObject(s);
            return jsonObject.getBoolean("LoginResult");
        }
        return false;
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

        urlConnection.disconnect();
        reader.close();
        return sb.toString();
    }

    private static Task convertJSONtoTask(JSONObject jsonObject) {
        try {
            String accountName = jsonObject.getString("AccountName");
            java.util.Date beginDate = convertJSONDateToDate(jsonObject.getString("BeginTime"));
            java.util.Date endDate = convertJSONDateToDate(jsonObject.getString("EndTime"));
            String id = jsonObject.getLong("ID") + "";
            String place = jsonObject.getString("Place");
            String taskContent = jsonObject.getString("TaskContent");
            String taskName = jsonObject.getString("TaskName");
            int type = jsonObject.getInt("Type");

            return new Task(id, taskName, beginDate, endDate, place, taskContent, accountName, type, 1);
        } catch (JSONException e) {
            return null;
        }
    }

    private static java.util.Date convertJSONDateToDate(String jsonDate) {
        int idx1 = jsonDate.indexOf("(");
        int idx2 = jsonDate.indexOf(")") - 5;
        String s = jsonDate.substring(idx1+1, idx2);
        long l = Long.valueOf(s);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(jsonDate.substring(idx2, idx2 + 5)));
        cal.setTimeInMillis(l);
        return cal.getTime();
    }

    public static boolean syncUp(String userName, String password, Task task) throws IOException, JSONException {
        String urlString = "http://nckh.somee.com/Server.svc/rest/UpSync";
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
//        urlConnection.setUseCaches(false);
        urlConnection.setRequestMethod("POST");
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.connect();


        JSONStringer jsonStringer = new JSONStringer();
        jsonStringer.object();
        jsonStringer.key("password").value(password);
        jsonStringer.key("task");
        jsonStringer.object();
        jsonStringer.key("AccountName").value(task.getAccountName());
        jsonStringer.key("BeginTime").value("/Date(" + task.getBeginTime().getTime() + "+0700)/");
        jsonStringer.key("EndTime").value("/Date(" + task.getEndTime().getTime() + "+0700)/");
        jsonStringer.key("ID").value(task.getId());
        jsonStringer.key("Place").value(task.getPlace());
        jsonStringer.key("TaskContent").value(task.getTaskContent());
        jsonStringer.key("TaskName").value(task.getTaskName());
        jsonStringer.key("Type").value(task.getType());
        jsonStringer.endObject();
        jsonStringer.key("userName").value(userName);
        jsonStringer.endObject();

        OutputStream outputStream = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(jsonStringer.toString());
        writer.close();
        outputStream.close();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();

        return sb.toString().contains("OK");
    }
}
