package alosboiya.jeddahwave.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import alosboiya.jeddahwave.Fragments.AboutUsFragment;
import alosboiya.jeddahwave.Fragments.AddPostFragment;
import alosboiya.jeddahwave.Fragments.ContactUsFragment;
import alosboiya.jeddahwave.Fragments.MainFragment;
import alosboiya.jeddahwave.Fragments.MembersFragment;
import alosboiya.jeddahwave.Fragments.MyAccountFragment;
import alosboiya.jeddahwave.Fragments.MyAdsFragment;
import alosboiya.jeddahwave.Fragments.NavigationDrawerFragment;
import alosboiya.jeddahwave.Fragments.PricesFragment;
import alosboiya.jeddahwave.Fragments.RechargeFragment;
import alosboiya.jeddahwave.Fragments.SearchFragment;
import alosboiya.jeddahwave.Fragments.TermsFragment;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.AddButtonClick;
import alosboiya.jeddahwave.Utils.NavigationDrawerCallbacks;
import alosboiya.jeddahwave.Utils.TinyDB;


public class Main2Activity extends AppCompatActivity implements NavigationDrawerCallbacks {


    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mToolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        tinyDB = new TinyDB(this);


        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("الاسبوعية");
        getActionBarTextView().setGravity(Gravity.CENTER_HORIZONTAL);
        getActionBarTextView().setText("الاسبوعية");
        getActionBarTextView().setVisibility(View.GONE);

        TextView textView = mToolbar.findViewById(R.id.toolbartext);
        textView.setText("الاسبوعية");

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        mNavigationDrawerFragment.closeDrawer();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Fragment fragment;
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        switch(position){
            case 0: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(MainFragment.TAG);
                if (fragment == null) {
                    fragment = new MainFragment();
                }
                trans.replace(R.id.container, fragment, MainFragment.TAG);
                break;

            case 1: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(SearchFragment.TAG);
                if (fragment == null) {
                    fragment = new SearchFragment();
                }
                trans.replace(R.id.container, fragment, SearchFragment.TAG);
                break;

            case 2: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(AboutUsFragment.TAG);
                if (fragment == null) {
                    fragment = new AboutUsFragment();
                }
                trans.replace(R.id.container, fragment, AboutUsFragment.TAG);
                break;

            case 3: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(ContactUsFragment.TAG);
                if (fragment == null) {
                    fragment = new ContactUsFragment();
                }
                trans.replace(R.id.container, fragment, ContactUsFragment.TAG);
                break;

            case 4: //الرئيسية//todo
                if(tinyDB.getString("isLoggedIn").equals("False"))
                {
                    showMessage("سجل الدخول اولا");
                }else
                    {
                        fragment = getSupportFragmentManager().findFragmentByTag(AddPostFragment.TAG);
                        if (fragment == null) {
                            fragment = new AddPostFragment();
                        }
                        trans.replace(R.id.container, fragment, AddPostFragment.TAG);
                    }

                break;

            case 5: //الرئيسية//todo
                if(tinyDB.getString("isLoggedIn").equals("False"))
                {
                    showMessage("سجل الدخول اولا");
                }else
                {
                    fragment = getSupportFragmentManager().findFragmentByTag(MyAdsFragment.TAG);
                    if (fragment == null) {
                        fragment = new MyAdsFragment();
                    }
                    trans.replace(R.id.container, fragment, MyAdsFragment.TAG);
                }

                break;

            case 6: //الرئيسية//todo
                if(tinyDB.getString("isLoggedIn").equals("False"))
                {
                    showMessage("سجل الدخول اولا");
                }else
                {
                    fragment = getSupportFragmentManager().findFragmentByTag(MyAccountFragment.TAG);
                    if (fragment == null) {
                        fragment = new MyAccountFragment();
                    }
                    trans.replace(R.id.container, fragment, MyAccountFragment.TAG);
                }

                break;

            case 7: //الرئيسية//todo
                if(tinyDB.getString("isLoggedIn").equals("False"))
                {
                    showMessage("سجل الدخول اولا");
                }else
                {
                    fragment = getSupportFragmentManager().findFragmentByTag(RechargeFragment.TAG);
                    if (fragment == null) {
                        fragment = new RechargeFragment();
                    }
                    trans.replace(R.id.container, fragment, RechargeFragment.TAG);
                }

                break;

            case 8: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(TermsFragment.TAG);
                if (fragment == null) {
                    fragment = new TermsFragment();
                }
                trans.replace(R.id.container, fragment, TermsFragment.TAG);
                break;

            case 9: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(PricesFragment.TAG);
                if (fragment == null) {
                    fragment = new PricesFragment();
                }
                trans.replace(R.id.container, fragment, PricesFragment.TAG);
                break;

            case 10: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(MembersFragment.TAG);
                if (fragment == null) {
                    fragment = new MembersFragment();
                }
                trans.replace(R.id.container, fragment, MembersFragment.TAG);
                break;

            case 11: //الرئيسية//todo
                fragment = getSupportFragmentManager().findFragmentByTag(MembersFragment.TAG);
                if (fragment == null) {
                    fragment = new MembersFragment();
                }
                trans.replace(R.id.container, fragment, MembersFragment.TAG);
                break;

            case 12: //الرئيسية//todo

                tinyDB.putString("isLoggedIn","False");

                showMessage("تم تسجيل الخروج");

                EventBus.getDefault().post(new AddButtonClick(String.valueOf(position)));

                break;
        }

        trans.commit();


    }

    private void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_LONG).show();
    }

    private TextView getActionBarTextView() {

        TextView titleTextView = null;

        try {
            Field f = mToolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(mToolbar);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return titleTextView;
    }

    public void alartExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("هل انت واثق من انك تريد الخروج ؟").setCancelable(false).setPositiveButton("نعم", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);

                //Main2Activity.this.finish();
            }
        }).setNegativeButton("لا", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            alartExit();
    }



}
