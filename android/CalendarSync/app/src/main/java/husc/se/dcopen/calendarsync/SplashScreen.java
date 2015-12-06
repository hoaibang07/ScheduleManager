package husc.se.dcopen.calendarsync;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity {

    public static final int SPLASH_TIME_OUT = 4000;
    private WebView webView;
    private TextView tvKhoiDong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        webView = (WebView) findViewById(R.id.web_view);
        tvKhoiDong = (TextView)findViewById(R.id.tv_khoi_dong);

        new AsyncSplashScreen().execute();
    }

    private class AsyncSplashScreen extends AsyncTask<Void, Void, Integer> {
        private String userName;
        private String password;
        private boolean isRemember;

        @Override
        protected void onPreExecute() {
            runLoadingLogo();

            final Settings settings = new Settings(SplashScreen.this);
            userName = settings.getUserName();
            password = settings.getPassword();
            isRemember = settings.isRemember();

            if(isRemember) tvKhoiDong.setText("Đang đăng nhập vào hệ thống...");
            else tvKhoiDong.setText("Đang khởi động hệ thống...");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return checkLogin();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            moveActivity(integer);
        }

        private void runLoadingLogo() {
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            webView.loadUrl("file:///android_asset/splash_screen_4.gif");
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
        }

        private int checkLogin() {
            Integer result = 0; //mac dinh la login sai
            if(isRemember) {
                try {
                    if(JSONParser.checkLogin(userName, password)) result = 1;
                } catch (IOException e) {
                    result = -2;
                } catch (JSONException e) {
                    result = -3;
                }
            } else {
                return -1;
            }
            return result;
        }

        private void moveActivity(int truongHop) {
            switch (truongHop) {
                case 1: {
                    //đăng nhập thành công
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("UserName", userName);
                    intent.putExtra("Account", bundle);

                    delay(intent);
                    break;
                }
                case 0: {
                    //đăng nhập thất bại (sai tên hoặc mật khẩu) -> load login activity
                    createAlertDialog("Đăng nhập", "Sai tên đăng nhập hoặc mật khẩu", false).show();
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                    break;
                }
                case -1: {
                    //Không ghi nhớ tên đăng nhập và mật khẩu -> load main activity
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                    break;
                }
                case -2: {
                    //đăng nhập thất bại (IOException) ->load login activity
                    createAlertDialog("Đăng nhập", "Kết nối bị lỗi", false).show();
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                    break;
                }
                case -3: {
                    //đăng nhập thất bại (JSONException) ->load login activity
                    createAlertDialog("Đăng nhập", "Lỗi hệ thống", false).show();
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                    break;
                }
            }
        }

        private AlertDialog createAlertDialog(String title, String message, boolean cancelable) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setCancelable(cancelable);
            builder.setPositiveButton("Đóng", null);
            return builder.create();
        }

        private void delay(final Intent intent) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
