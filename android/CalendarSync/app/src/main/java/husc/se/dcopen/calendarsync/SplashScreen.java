package husc.se.dcopen.calendarsync;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity {

    public static final int SPLASH_TIME_OUT = 8000;
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
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("UserName", userName);
                    intent.putExtra("Account", bundle);

                    delay(intent);
                    break;
                }
                case 0: {
                    //thong bao dang nhap that bai (sai ten hoac mat khau) -> load login activity
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                }
                case -1: {
                    //load login activity
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                }
                case -2: {
                    Toast.makeText(SplashScreen.this, "IOException", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                    break;
                }
                case -3: {
                    Toast.makeText(SplashScreen.this, "JSONException", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    delay(intent);
                    break;
                }
            }
        }
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
