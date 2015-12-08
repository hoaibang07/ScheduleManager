package husc.se.dcopen.calendarsync;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingDialog extends DialogFragment {
    private CustomSeekBar seekBarSyncUp;
    private CustomSeekBar seekBarSyncDown;
    private TextView lblSyncUp;
    private TextView lblSyncDown;
    private Button btnCancel;
    private Button btnSave;
    private LineColorPicker pickerSync;
    private LineColorPicker pickerNoSync;
    private LineColorPicker pickerTaskToDate;

    private Settings settings;
    private int numberDateSyncUp, numberDateSyncDown;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_setting);
        setCancelable(false);

        seekBarSyncUp = (CustomSeekBar)dialog.findViewById(R.id.seek_sync_up);
        seekBarSyncDown = (CustomSeekBar)dialog.findViewById(R.id.seek_sync_down);
        lblSyncUp = (TextView)dialog.findViewById(R.id.lbl_sync_up);
        lblSyncDown = (TextView)dialog.findViewById(R.id.lbl_sync_down);
        btnCancel = (Button)dialog.findViewById(R.id.btn_setting_cancel);
        btnSave = (Button)dialog.findViewById(R.id.btn_setting_save);
        pickerSync = (LineColorPicker)dialog.findViewById(R.id.picker_sync);
        pickerNoSync = (LineColorPicker)dialog.findViewById(R.id.picker_nosync);
        pickerTaskToDate = (LineColorPicker)dialog.findViewById(R.id.picker_task_todate);

        seekBarSyncUp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberDateSyncUp = progress - 30;
                if(numberDateSyncUp > 0) {
                    lblSyncUp.setText(numberDateSyncUp + " ngày tiếp theo");
                } else if(numberDateSyncUp == 0) {
                    lblSyncUp.setText(numberDateSyncUp + " ngày");
                } else {
                    lblSyncUp.setText(Math.abs(numberDateSyncUp) + " ngày trước");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBarSyncDown.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberDateSyncDown = progress - 30;
                if(numberDateSyncDown > 0) {
                    lblSyncDown.setText(numberDateSyncDown + " ngày tiếp theo");
                } else if(numberDateSyncDown == 0) {
                    lblSyncDown.setText(numberDateSyncDown + " ngày");
                } else {
                    lblSyncDown.setText(Math.abs(numberDateSyncDown) + " ngày trước");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.putSyncColor(pickerSync.getColor());
                settings.putNoSyncColor(pickerNoSync.getColor());
                settings.putTaskToDateColor(pickerTaskToDate.getColor());
                settings.putNumberDateSyncUp(numberDateSyncUp);
                settings.putNumberDateSyncDown(numberDateSyncDown);

                Intent intent = new Intent("Setting");
                getContext().sendBroadcast(intent);

                dismiss();
            }
        });


        settings = new Settings(getActivity());
        int ngayUp = settings.getNumberDateSyncUp();
        int ngayDown = settings.getNumberDateSyncDown();
        int colorSync = settings.getSyncColor();
        int colorNoSync = settings.getNoSyncColor();
        int colorTaskToDate = settings.getTaskToDateColor();

        seekBarSyncUp.setProgress(ngayUp + 30);
        seekBarSyncDown.setProgress(ngayDown + 30);
        pickerSync.setSelectedColor(colorSync);
        pickerNoSync.setSelectedColor(colorNoSync);
        pickerTaskToDate.setSelectedColor(colorTaskToDate);

        return dialog;
    }
}