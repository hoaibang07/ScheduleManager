package husc.se.dcopen.calendarsync;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUserName;
    private EditText txtPassword;
    private CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserName = (EditText)findViewById(R.id.txt_username);
        txtPassword = (EditText)findViewById(R.id.txt_password);
        ckbRemember = (CheckBox)findViewById(R.id.ckb_remember);
        final Button btnLogin = (Button)findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncLogin().execute();
            }
        });
    }

    private class AsyncLogin extends AsyncTask<Void, Void, Integer> {
        private String userName;
        private String password;

        @Override
        protected void onPreExecute() {
            userName = txtUserName.getText().toString();
            password = txtPassword.getText().toString();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return checkLogin();
        }

        @Override
        protected void onPostExecute(Integer integer) {

            switch (integer) {
                case 1: {
                    //Đăng nhập thành công
                    final Settings settings = new Settings(LoginActivity.this);
                    settings.putRemember(ckbRemember.isChecked());
                    settings.putUserName(userName);
                    settings.putPassword(password);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("UserName", userName);
                    intent.putExtra("Account", bundle);

                    startActivity(intent);
                    finish();
                    break;
                }
                case 0: {
                    //Đăng nhập thất bại
                    createAlertDialog("Đăng nhập", "Sai tên đăng nhập hoặc mật khẩu", false).show();
                    break;
                }
                case -1: {
                    //Thông tin đăng nhập bị để trống
                    createAlertDialog("Đăng nhập", "Không được để trống thông tin đăng nhập", false).show();
                    break;
                }
                case -2: {
                    createAlertDialog("Đăng nhập", "IOException", false).show();
                    break;
                }
                case -3: {
                    createAlertDialog("Đăng nhập", "JSONException", false).show();
                    break;
                }
            }
        }

        private int checkLogin() {
            Integer result = 0;
            if(userName.equals("") || password.equals("")) result = -1;
            try {
                if(JSONParser.checkLogin(userName, password)) result = 1;
            } catch (IOException e) {
                result = -2;
            } catch (JSONException e) {
                result = -3;
            }
            return result;
        }

        private AlertDialog createAlertDialog(String title, String message, boolean cancelable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setCancelable(cancelable);
            builder.setPositiveButton("OK", null);
            return builder.create();
        }
    }

    @Override
    public void onBackPressed() {
        createExitDialog().show();
    }

    private AlertDialog createExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thoát");
        builder.setMessage("Bạn có thực sự muốn thoát?");
        builder.setCancelable(false);
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        builder.setNegativeButton("Không", null);

        return builder.create();
    }
}
