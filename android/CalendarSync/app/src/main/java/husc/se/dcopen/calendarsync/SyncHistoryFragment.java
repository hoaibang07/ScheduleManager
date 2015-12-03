package husc.se.dcopen.calendarsync;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

public class SyncHistoryFragment extends Fragment {
    private TextView tvHisSyncUp;
    private TextView tvHisSyncDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_history, container, false);

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

        tvHisSyncUp = (TextView)view.findViewById(R.id.tv_his_sync_up);
        tvHisSyncUp.setMovementMethod(new ScrollingMovementMethod());
        tvHisSyncDown = (TextView)view.findViewById(R.id.tv_his_sync_down);
        tvHisSyncDown.setMovementMethod(new ScrollingMovementMethod());

        loadHistory();
        return view;
    }

    private void loadHistory() {
        DatabaseHelper db = new DatabaseHelper(getActivity());
        ArrayList<String> listHisSyncUp = db.getHistorys(db.GET_HISTORY_SYNC_UP);
        ArrayList<String> listHisSyncDown = db.getHistorys(db.GET_HISTORY_SYNC_DOWN);

        for(String item : listHisSyncUp) {
            tvHisSyncUp.append(item + "\n");
        }

        for (String item : listHisSyncDown) {
            tvHisSyncDown.append(item + "\n");
        }
    }
}
