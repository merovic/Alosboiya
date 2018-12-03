package alosboiya.jeddahwave.OldProject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Field;

import alosboiya.jeddahwave.R;
import im.delight.android.webview.AdvancedWebView;

import static alosboiya.jeddahwave.R.id.webView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,AdvancedWebView.Listener  {

    //WebView myWebView;
    AdvancedWebView myWebView;
    AdvancedWebView myWebViewsearch;
    TextView textView2,textView4;
    Boolean check=false;
    Toolbar toolbar ;
    DrawerLayout drawer ;
    ProgressBar progressBar,progressBar2;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout layout1,layout2,layout3;
    LinearLayout btn1,btn2,btn3,btn4;
    ActionBarDrawerToggle toggle;
    private GoogleApiClient mGoogleApiClient;
    NavigationView navigationView;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

   // private ValueCallback<Uri> mUploadMessage;
    /** File upload callback for platform versions prior to Android 5.0 */
    protected ValueCallback<Uri> mFileUploadCallbackFirst;
    /** File upload callback for Android 5.0+ */
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;
    //private static final int FILE_CHOOSER_RESULT_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkNetwork()){

            toolweb();
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
           GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleApiClient with access to the Google Sign-In API and the
            // options specified by gso.
           mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener*/ )
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


            myWebView.setWebChromeClient(new WebChromeClient() {

                /*public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mFileUploadCallbackFirst = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image*//*");
                   // startActivityForResult(i, 0);
                    startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);

                }*/
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    //  Toast.makeText(MainActivity.this, title, Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            setContentView(R.layout.drawer_no_connect);
            alart();

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //toolbar.setTitleTextColor(Color.rgb(22,107,182));
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle("تطبيق الاسبوعية");
            getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

            drawer = findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getItemIconTintList();
            navigationView.setItemIconTintList(null);

        }

        if(checkNetwork()) {
            layout1 = findViewById(R.id.layout1);
            layout2 = findViewById(R.id.layout2);
            layout3 = findViewById(R.id.layout3);
            btn1 = findViewById(R.id.button1);
            btn2 = findViewById(R.id.button2);
            btn3 = findViewById(R.id.button3);
            btn4 = findViewById(R.id.button4);
            textView2 = findViewById(R.id.text2);
            textView4 = findViewById(R.id.text4);

            swipeRefreshLayout.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.mainblue));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchgray));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutgray));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactgray));

                    if(swipeRefreshLayout.getVisibility() == View.VISIBLE)
                    {
                        startWebView("http://alosboiya.com.sa/");
                    }else
                        {
                            toolbar = findViewById(R.id.toolbar_one);
                            toolbar.setTitleTextColor(Color.WHITE);
                            getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                            mTitle.setText("تطبيق الاسبوعية");
                            mTitle.setTextSize(25);
                            mTitle.setTypeface(Typeface.DEFAULT_BOLD);


                            layout1.setVisibility(View.GONE);
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            layout2.setVisibility(View.GONE);
                            layout3.setVisibility(View.GONE);
                        }


                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.maingray));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchblue));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutgray));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactgray));

                    toolbar = findViewById(R.id.toolbar_one);
                    toolbar.setTitleTextColor(Color.WHITE);
                    getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                    mTitle.setText("البحث");
                    mTitle.setTextSize(25);
                    mTitle.setTypeface(Typeface.DEFAULT_BOLD);

                    layout1.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.maingray));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchgray));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutblue));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactgray));

                    toolbar = findViewById(R.id.toolbar_one);
                    toolbar.setTitleTextColor(Color.WHITE);
                    getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                    mTitle.setText("نبذه عنا");
                    mTitle.setTextSize(25);
                    mTitle.setTypeface(Typeface.DEFAULT_BOLD);

                    layout1.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    layout3.setVisibility(View.GONE);

                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.maingray));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchgray));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutgray));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactblue));

                    toolbar = findViewById(R.id.toolbar_one);
                    toolbar.setTitleTextColor(Color.WHITE);
                    getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                    mTitle.setText("إتصل بنا");
                    mTitle.setTextSize(25);
                    mTitle.setTypeface(Typeface.DEFAULT_BOLD);

                    layout1.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.VISIBLE);
                }
            });

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@alosboya.com.sa"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "Type your Message here!");

                    intent.setType("message/rfc822");

                    startActivity(Intent.createChooser(intent, "Select Email Sending App :"));

                }
            });

            textView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:966920020110"));
                    startActivity(intent);
                }
            });



        }
    }

    private TextView getActionBarTextView() {
        TextView titleTextView = null;

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return titleTextView;
    }

    public void toolweb(){
        if (checkNetwork()){

            setContentView(R.layout.drawer_connect);
            toolbar = findViewById(R.id.toolbar_one);
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle("تطبيق الاسبوعية");
            getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

            TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
            ImageView refresh = toolbar.findViewById(R.id.refresh);

            setSupportActionBar(toolbar);
            mTitle.setText(toolbar.getTitle());
            mTitle.setTextSize(25);
            mTitle.setTypeface(Typeface.DEFAULT_BOLD);

            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myWebView.reload();
                }
            });

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            layout1 = findViewById(R.id.layout1);
            layout2 = findViewById(R.id.layout2);
            layout3 = findViewById(R.id.layout3);
            btn1 = findViewById(R.id.button1);
            btn2 = findViewById(R.id.button2);
            btn3 = findViewById(R.id.button3);
            btn4 = findViewById(R.id.button4);
            textView2 = findViewById(R.id.text2);
            textView4 = findViewById(R.id.text4);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.mainblue));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchgray));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutgray));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactgray));

                    if(swipeRefreshLayout.getVisibility() == View.VISIBLE)
                    {
                        startWebView("http://alosboiya.com.sa/");
                    }else
                    {
                        toolbar = findViewById(R.id.toolbar_one);
                        toolbar.setTitleTextColor(Color.WHITE);
                        getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                        mTitle.setText("تطبيق الاسبوعية");
                        mTitle.setTextSize(25);
                        mTitle.setTypeface(Typeface.DEFAULT_BOLD);


                        layout1.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout2.setVisibility(View.GONE);
                        layout3.setVisibility(View.GONE);
                    }
                }
            });

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.maingray));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchblue));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutgray));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactgray));

                    toolbar = findViewById(R.id.toolbar_one);
                    toolbar.setTitleTextColor(Color.WHITE);
                    getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                    mTitle.setText("البحث");
                    mTitle.setTextSize(25);
                    mTitle.setTypeface(Typeface.DEFAULT_BOLD);

                    layout1.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.GONE);
                }
            });

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.maingray));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchgray));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutblue));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactgray));

                    toolbar = findViewById(R.id.toolbar_one);
                    toolbar.setTitleTextColor(Color.WHITE);
                    getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                    mTitle.setText("نبذه عنا");
                    mTitle.setTextSize(25);
                    mTitle.setTypeface(Typeface.DEFAULT_BOLD);

                    layout1.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    layout3.setVisibility(View.GONE);

                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //btn1.setImageDrawable(getResources().getDrawable(R.drawable.maingray));
                    //btn2.setImageDrawable(getResources().getDrawable(R.drawable.searchgray));
                    //btn3.setImageDrawable(getResources().getDrawable(R.drawable.aboutgray));
                    //btn4.setImageDrawable(getResources().getDrawable(R.drawable.contactblue));

                    toolbar = findViewById(R.id.toolbar_one);
                    toolbar.setTitleTextColor(Color.WHITE);
                    getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);

                    TextView mTitle = toolbar.findViewById(R.id.toolbar_title);

                    mTitle.setText("إتصل بنا");
                    mTitle.setTextSize(25);
                    mTitle.setTypeface(Typeface.DEFAULT_BOLD);

                    layout1.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    layout2.setVisibility(View.GONE);
                    layout3.setVisibility(View.VISIBLE);
                }
            });

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_SEND);

                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@alosboya.com.sa"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "Type your Message here!");

                    intent.setType("message/rfc822");

                    startActivity(Intent.createChooser(intent, "Select Email Sending App :"));

                }
            });

            textView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(textView4.toString()));
                    startActivity(intent);
                }
            });



            drawer = findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setItemIconTintList(null);

            progressBar = findViewById(R.id.progressBar1);

            myWebView = findViewById(webView);
            myWebView.setListener(this, this);
            myWebView.getSettings().setSupportZoom(false);
            myWebView.getSettings().setDisplayZoomControls(false);

            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

            myWebView.getSettings().setBuiltInZoomControls(true);
            myWebView.getSettings().setUseWideViewPort(true);
            myWebView.getSettings().setLoadWithOverviewMode(true);

            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            myWebView.getSettings().setSupportMultipleWindows(true);

            startWebView("http://alosboiya.com.sa/");

            swipeRefreshLayout = findViewById(R.id.swipeToRefresh);

            if(!myWebView.getUrl().contains(".pdf"))
            {
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        myWebView.reload();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            progressBar2 = findViewById(R.id.progressBar2);

            myWebViewsearch = findViewById(R.id.webViewsearch);
            myWebViewsearch.setListener(this, this);
            myWebViewsearch.getSettings().setSupportZoom(false);
            myWebViewsearch.getSettings().setDisplayZoomControls(false);

            myWebViewsearch.getSettings().setJavaScriptEnabled(true);
            myWebViewsearch.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

            myWebViewsearch.getSettings().setBuiltInZoomControls(true);
            myWebViewsearch.getSettings().setUseWideViewPort(true);
            myWebViewsearch.getSettings().setLoadWithOverviewMode(true);

            myWebViewsearch.getSettings().setJavaScriptEnabled(true);
            myWebViewsearch.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            myWebViewsearch.getSettings().setSupportMultipleWindows(true);

            startWebViewsearch("http://alosboiya.com.sa/search.aspx");

        }


    }
    @Override
    public void onBackPressed() {
        if(checkNetwork()) {
            myWebView = findViewById(webView);
            myWebViewsearch = findViewById(R.id.webViewsearch);
            drawer = findViewById(R.id.drawer_layout);
            //layout1 = (RelativeLayout) findViewById(R.id.layout1);

            if(swipeRefreshLayout.getVisibility()==View.VISIBLE)
            {
                if(drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);

                }else if(myWebView.canGoBack())
                {
                    if (myWebView.getUrl().contains("http://alosboiya.com.sa")) {

                                myWebView.goBack();

                    }else {
                                for (int i = 0; i < 1; i++) {
                                    myWebView.goBack();
                                    myWebView.goBack();
                                }
                    }
                }else
                    {
                        alartExit();
                    }


                /*if (myWebView.canGoBack()) {
                    if (myWebView.getUrl().contains("http://alosboiya.com.sa")) {

                        myWebView.goBack();

                    } else {
                        for (int i = 0; i < 1; i++) {
                            myWebView.goBack();
                            myWebView.goBack();
                        }
                    }

                } else {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                        //alartExit();
                    } else {
                        alartExit();
                    }

                }*/

            }



            if(layout1.getVisibility()==View.VISIBLE)
            {
                if(drawer.isDrawerOpen(GravityCompat.START))
                {
                    drawer.closeDrawer(GravityCompat.START);

                }else if(myWebViewsearch.canGoBack())
                {
                    myWebViewsearch.goBack();

                }
                else
                {
                    alartExit();
                }

            }



            myWebView.getSettings().setSupportZoom(false);
        }else
            {
                super.onBackPressed();
            }

    }


    private void startWebView(final String url) {

        WebSettings settings = myWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.getSettings().setSupportMultipleWindows(true);
        myWebView.setWebViewClient(new YourWebClient());

        myWebView.loadUrl(url);
    }

    private void startWebViewsearch(final String url) {

        WebSettings settings = myWebViewsearch.getSettings();

        settings.setJavaScriptEnabled(true);
        myWebViewsearch.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        myWebViewsearch.getSettings().setBuiltInZoomControls(true);
        myWebViewsearch.getSettings().setUseWideViewPort(true);
        myWebViewsearch.getSettings().setLoadWithOverviewMode(true);

        myWebViewsearch.getSettings().setJavaScriptEnabled(true);
        myWebViewsearch.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        myWebViewsearch.getSettings().setSupportMultipleWindows(true);
        myWebViewsearch.setWebViewClient(new YourWebClientsearch());

        myWebViewsearch.loadUrl(url);
    }





    public void alart() {

        AlertDialog al = new AlertDialog.Builder(this).create();
        al.setMessage("No Internet connection. Would you like to enable Internet connection ?");
        al.setButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        al.setButton2("MOBILEDATA", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.startActivity(new Intent("android.settings.DATA_ROAMING_SETTINGS"));
            }
        });
        al.setButton3("WIFI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
            }
        });
        al.show();
    }

    private Boolean checkNetwork(){

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        check = info != null && info.isConnected();
        return check;
    }

    public void alartExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

   private void handleSignInResult(GoogleSignInResult result) {
         Log.d(TAG, "handleSignInResult:" + result.isSuccess());
         if (result.isSuccess()) {
             // Signed in successfully, show authenticated UI.
             GoogleSignInAccount acct = result.getSignInAccount();
             Toast.makeText(MainActivity.this, acct.getDisplayName(), Toast.LENGTH_SHORT).show();
         }
     }

      @Override
      public void onClick(View v) {
          Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
          startActivityForResult(signInIntent, RC_SIGN_IN);

      }

      @Override
      public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
          Log.d(TAG, "onConnectionFailed:" + connectionResult);
      }

      //--------------------------

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }




    public class YourWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //Toast.makeText(MainActivity.this, "Started", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressBar.getVisibility()==View.VISIBLE) {

                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
            }else if (url.endsWith(".pdf")) {
                swipeRefreshLayout.setEnabled(false);
                String googleDocs = "https://docs.google.com/viewer?url=";
                myWebView.getSettings().setJavaScriptEnabled(true);
                myWebView.loadUrl(googleDocs + url);
                myWebView.getSettings().setSupportZoom(true);
               // view.loadUrl(url);
                return true;
            } else {
                // Load all other urls normally.
                view.loadUrl(url);
            }

            return true;

        }
    }


    public class YourWebClientsearch extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //Toast.makeText(MainActivity.this, "Started", Toast.LENGTH_SHORT).show();
            progressBar2.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressBar2.getVisibility()==View.VISIBLE) {

                progressBar2.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
            }else {
                // Load all other urls normally.
                view.loadUrl(url);
            }

            return true;

        }

    }



    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (item.getItemId() == R.id.exit) {
            alartExit();
        } else {
            checkNetwork();
            toolweb();
            if (check) {
                switch (item.getItemId()) {
                    case R.id.home :
                        this.myWebView.loadUrl("http://alosboiya.com.sa/");
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        break;
                    case R.id.service:
                        this.myWebView.loadUrl("http://alosboiya.com.sa/about_services.aspx");
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        break;
                    case R.id.parts:
                        this.myWebView.loadUrl("http://alosboiya.com.sa/department.aspx");
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        break;
                    case R.id.archive:
                        this.myWebView.loadUrl("http://alosboiya.com.sa/archives.aspx");
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        break;
                    case R.id.call_us:
                        this.myWebView.loadUrl("http://alosboiya.com.sa/contact.aspx");
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        break;
                    case R.id.sign_up:
                        this.myWebView.loadUrl("http://alosboiya.com.sa/registration.aspx");
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        break;
                    case R.id.sign_in:
                        this.myWebView.loadUrl("http://alosboiya.com.sa/login.aspx");
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        layout1.setVisibility(View.GONE);
                        break;
                    /*case R.id.share :
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.SUBJECT", "share app");
                        intent.putExtra("android.intent.extra.TEXT", "Download our application from: http://alosboiya.com.sa/");
                        startActivity(Intent.createChooser(intent, "Sharing Meppro Application using: "));
                        break;*/

                    default:
                        break;
                }
            }else
          alart();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        myWebView.onActivityResult(requestCode, resultCode, intent);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
       if (requestCode == RC_SIGN_IN) {
           GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
           handleSignInResult(result);
      }

    }

}
