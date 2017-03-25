package com.tistory.thankspizza.pizzawebbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.List;

public class PizzaWebBook extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //jmkimz 1
    private WebView mWebView;

    //jmkimz 4
    public String homeUrl = "file:///android_asset/index.html";
    public String clienUrl = "http://m.clien.net/cs3/board?bo_style=lists&bo_table=park";
    public String ppomppuUrl = "http://m.ppomppu.co.kr/new/bbs_list.php?id=climb";
    public String mlbparkUrl = "http://mlbpark.donga.com/mp/b.php?b=bullpen";
    public String thankspizzaUrl = "http://thankspizza.tistory.com";

    //jmkimz 9
    //public int Font_Size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_web_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //jmkimz 2
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.loadUrl(homeUrl);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setWebViewClient(new PizzaWebViewClient());
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
        //jmkimz 2

        //jmkimz 3
        mWebView.setWebChromeClient(new FullscreenableChromeClient(this));

        getFontSize();
    }


    //jmkimz 5
    private class PizzaWebViewClient extends WebViewClient {

        //@SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Toast.makeText(PizzaWebBook.this, "shouldOverrideUrlLoading 호출", Toast.LENGTH_SHORT).show();
            view.loadUrl(url);
            return true;
        }
    }
    //jmkimz 5

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pizza_web_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "설정", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_thankspizza) {
            callChrome(thankspizzaUrl);
        } else if (id == R.id.nav_clien) {
            goWebviewUrl(clienUrl);
        } else if (id == R.id.nav_climb) {
            goWebviewUrl(ppomppuUrl);
        } else if (id == R.id.nav_bullpen) {
            goWebviewUrl(mlbparkUrl);
        } else  if (id == R.id.nav_camera) {
            goWebviewUrl(homeUrl);
        } else if (id == R.id.nav_manage) {
            SharedPreferences pref = getSharedPreferences("Font_Size", MODE_PRIVATE);
            int Font_Size = pref.getInt("Font_Size", 100);
            if (Font_Size > 90) saveFontSize(Font_Size - 10);
        } else if (id == R.id.nav_share) {
            SharedPreferences pref = getSharedPreferences("Font_Size", MODE_PRIVATE);
            int Font_Size = pref.getInt("Font_Size", 100);
            if (Font_Size < 150) saveFontSize(Font_Size + 10);
        } else if (id == R.id.nav_send) {
            saveFontSize(100);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //jmkimz 6
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            if(FullscreenableChromeClient.isVideoFullscreen){ //jmkimz 전체화면에서 back 할 때 전체화면 종료 가능
                return true;
            }
            mWebView.goBack();
            return true;
        }

        //백할 페이가 없다면
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (!mWebView.canGoBack())){
            Toast.makeText(this, "버튼 클릭 이벤트", Toast.LENGTH_SHORT).show();

            //다이알로그박스 출력
            new AlertDialog.Builder(this)
                    .setTitle("핏짜의 등산 바이블")
                    .setMessage("그만 보시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setNegativeButton("아니오",  null).show();
        }

        return super.onKeyDown(keyCode, event);
    }
    //jmkimz 6

    //jmkimz 7
    public void callBrowser(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callChrome(String url){
        //크롬브라우저 패키지명
        String packageName = "com.android.chrome";

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage(packageName); //바로 이 부분
        i.setData(Uri.parse(url));

        //크롬브라우저가 설치되어있으면 호출, 없으면 마켓으로 설치유도
        List<ResolveInfo> activitiesList = getPackageManager().queryIntentActivities(i, -1);
        if(activitiesList.size() > 0) {
            startActivity(i);
        } else {
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
            playStoreIntent.setData(Uri.parse("market://details?id="+packageName));
            playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(playStoreIntent);
        }
    }
    //jmkimz 7

    //jmkimz 8
    // 값 불러오기
    private void getFontSize(){
        SharedPreferences pref = getSharedPreferences("Font_Size", MODE_PRIVATE);
        int Font_Size = pref.getInt("Font_Size", 100);
        mWebView.getSettings().setTextZoom(Font_Size);
        Toast.makeText(PizzaWebBook.this, "Font Size 불러오기 : " + Font_Size, Toast.LENGTH_LONG).show();
    }
    //jmkimz 8
    //jmkimz 11
    private void saveFontSize(int Font_Size) {
        SharedPreferences pref = getSharedPreferences("Font_Size", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Font_Size", Font_Size);
        editor.apply();
        Font_Size = pref.getInt("Font_Size", 100);
        mWebView.getSettings().setTextZoom(Font_Size);
        Toast.makeText(PizzaWebBook.this, "저장하기 : " + Font_Size, Toast.LENGTH_LONG).show();
    }
    //jmkimz 12
    private void goWebviewUrl(String siteUrl) {
        Toast.makeText(PizzaWebBook.this, siteUrl, Toast.LENGTH_SHORT).show();
        mWebView.clearHistory();
        if (siteUrl == homeUrl || siteUrl == thankspizzaUrl) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            Toast.makeText(PizzaWebBook.this, "TRUE", Toast.LENGTH_SHORT).show();
        } else {
            mWebView.getSettings().setJavaScriptEnabled(false);
            Toast.makeText(PizzaWebBook.this, "FALSE", Toast.LENGTH_SHORT).show();
        }
        mWebView.loadUrl(siteUrl);
    }
}