package husc.se.dcopen.calendarsync;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Wikoln on 12/5/2015.
 */
public class HisArrayAdapter extends ArrayAdapter<History> {
    private List<History> listHistory;
    private LayoutInflater inflater;

    public HisArrayAdapter(Context context, int resource, List<History> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listHistory = objects;
    }

    public int getCount() {
        return listHistory.size();
    }

    public History getItem(int position) {
        return listHistory.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final History history = listHistory.get(position);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_history, parent, false);
        }

        final TextView tvHisTime = (TextView)convertView.findViewById(R.id.tv_his_time);
        final TextView tvHisTen = (TextView)convertView.findViewById(R.id.tv_his_ten);
        final LinearLayout linearHis = (LinearLayout)convertView.findViewById(R.id.linear_his);

        tvHisTime.setText(convertDateToString(history.getNgayDongBo(), "dd/MM/yyyy hh:mm aa"));
        tvHisTen.setText(history.getHisName() + " - " + history.getHisContent());

        linearHis.setBackgroundColor(new Settings(getContext()).getSyncColor());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Tên công việc: "+history.getHisName() + "\n" +
                        "Nội dung công việc: " + history.getHisContent() + "\n" +
                        "Thời gian: " + convertDateToString(history.getHisBTime(), "dd/MM/yyyy hh:mm aa") + " - " +
                        convertDateToString(history.getHisETime(), "dd/MM/yyyy hh:mm aa") + "\n" +
                        "Địa điểm: " + history.getHisPlace();
                createAlertDialog("Thông tin chi tiết", message, false).show();
            }
        });

        return convertView;
    }

    private String convertDateToString(java.util.Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    private AlertDialog createAlertDialog(String title, String message, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(cancelable);
        builder.setPositiveButton("Đóng", null);

        return builder.create();
    }
}
