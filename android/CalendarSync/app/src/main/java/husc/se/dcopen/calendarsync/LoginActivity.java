package husc.se.dcopen.calendarsync;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private EditText txtUserName;
    private EditText txtPassword;
    private CheckBox ckbRemember;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUserName = (EditText)findViewById(R.id.txt_username);
        txtPassword = (EditText)findViewById(R.id.txt_password);
        ckbRemember = (CheckBox)findViewById(R.id.ckb_remember);
        btnLogin = (Button)findViewById(R.id.btn_login);

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
                    Toast.makeText(LoginActivity.this, "Dang nhap that bai", Toast.LENGTH_LONG).show();
                    break;
                }
                case -1: {
                    Toast.makeText(LoginActivity.this, "Trong", Toast.LENGTH_LONG).show();
                    break;
                }
                case -2: {
                    Toast.makeText(LoginActivity.this, "IOException", Toast.LENGTH_LONG).show();
                    break;
                }
                case -3: {
                    Toast.makeText(LoginActivity.this, "JSONException", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }

        private int checkLogin() {
            Integer result = 0;
            if(userName.equals("") || password.equals("")) result = -1;
            try {
                if(JSONParser.checkLogin(userName, password)) result = 1;
                Log.e("Login", result + "");
            } catch (IOException e) {
                result = -2;
            } catch (JSONException e) {
                result = -3;
            }
            return result;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog exitDialog = createAlertDialog();
        exitDialog.show();
    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        builder.setNegativeButton("No", null);

        return builder.create();
    }
}
