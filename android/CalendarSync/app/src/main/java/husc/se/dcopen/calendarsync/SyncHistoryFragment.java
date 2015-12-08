package husc.se.dcopen.calendarsync;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class SyncHistoryFragment extends Fragment {
    private ListView lvHisSyncUp;
    private ListView lvHisSyncDown;
    private BroadcastReceiver settingReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                HisArrayAdapter hisArrayAdapterUp = (HisArrayAdapter)lvHisSyncUp.getAdapter();
                hisArrayAdapterUp.notifyDataSetChanged();

                HisArrayAdapter hisArrayAdapterDown = (HisArrayAdapter)lvHisSyncDown.getAdapter();
                hisArrayAdapterDown.notifyDataSetChanged();
            }
        };
        getActivity().registerReceiver(settingReceiver, new IntentFilter("Setting"));
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(settingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync_history, container, false);
        lvHisSyncUp = (ListView)view.findViewById(R.id.lv_his_sync_up);
        lvHisSyncDown = (ListView)view.findViewById(R.id.lv_his_sync_down);
        view.findViewById(R.id.btn_clear_syncup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xóa lịch sử đồng bộ lên");
                builder.setMessage("Bạn có thực sự muốn xóa hay không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper db = new DatabaseHelper(getActivity());
                        boolean result = db.deleteHistory("HisType = '0'");

                        if (result) {
                            HisArrayAdapter hisArrayAdapter = (HisArrayAdapter) lvHisSyncUp.getAdapter();
                            hisArrayAdapter.clear();
                            hisArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Không", null);
                builder.create().show();

            }
        });
        view.findViewById(R.id.btn_clear_syncdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xóa lịch sử đồng bộ xuống");
                builder.setMessage("Bạn có thực sự muốn xóa hay không?");
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean b = new DatabaseHelper(getActivity()).deleteHistory("HisType = '1'");
                        if(b) {
                            HisArrayAdapter hisArrayAdapter = (HisArrayAdapter) lvHisSyncDown.getAdapter();
                            hisArrayAdapter.clear();
                            hisArrayAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Không", null);
                builder.create().show();
            }
        });

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
