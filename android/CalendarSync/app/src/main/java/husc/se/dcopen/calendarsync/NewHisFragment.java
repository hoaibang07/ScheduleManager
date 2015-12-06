package husc.se.dcopen.calendarsync;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Wikoln on 12/5/2015.
 */
public class NewHisFragment extends Fragment {
    private ListView listView;
    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_his, container, false);
        listView = (ListView)view.findViewById(R.id.lv_new_his);
        tv = (TextView)view.findViewById(R.id.tv_new_his);
        if(ListSend.listHistories != null) {
            tv.setText("Các công việc vừa được đồng bộ");
            HisArrayAdapter hisArrayAdapter = new HisArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, ListSend.listHistories);
            listView.setAdapter(hisArrayAdapter);
        } else {
            tv.setText("Không có công việc nào được đồng bộ");

        }

        return view;
    }
}
