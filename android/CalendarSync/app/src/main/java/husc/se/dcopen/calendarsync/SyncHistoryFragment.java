package husc.se.dcopen.calendarsync;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class SyncHistoryFragment extends Fragment {
    private ListView lvHisSyncUp;
    private ListView lvHisSyncDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_history, container, false);
        lvHisSyncUp = (ListView)view.findViewById(R.id.lv_his_sync_up);

        lvHisSyncDown = (ListView)view.findViewById(R.id.lv_his_sync_down);

        final TabHost tabHost = (TabHost)view.findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec("tabHisSyncUp");
        spec.setContent(R.id.tab_his_sync_up);
        spec.setIndicator("Lịch sử đồng bộ lên");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tabHisSyncDown");
        spec.setContent(R.id.tab_his_sync_down);
        spec.setIndicator("Lịch sử đồng bộ xuống");
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        loadHistory();
        return view;
    }

    private void loadHistory() {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList<History> listHisSyncUp = db.getHistorys(db.GET_HISTORY_SYNC_UP);
        ArrayList<History> listHisSyncDown = db.getHistorys(db.GET_HISTORY_SYNC_DOWN);

        HisArrayAdapter array1 = new HisArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listHisSyncUp);
        HisArrayAdapter array2 = new HisArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listHisSyncDown);

        lvHisSyncUp.setAdapter(array1);
        lvHisSyncDown.setAdapter(array2);
    }
}
