package com.indielink.indielink;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.indielink.indielink.Profile.BandProfileContent;
import com.indielink.indielink.Profile.ProfileContent;
import com.indielink.indielink.Profile.UserRole;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RootPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    BandProfileContent band1 = new BandProfileContent("Band1");
    BandProfileContent band2 = new BandProfileContent("Band2");
    ArrayList<BandProfileContent> UserBand = new ArrayList<BandProfileContent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_page);

        //TODO: HTTP POST Request for User's band info.  the below is hardcoded testing
        UserBand.add(band1);
        UserBand.add(band2);

        UserRole.IsMusician();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu NavDrawer = navigationView.getMenu();
        //SubMenu MyBand = NavDrawer.addSubMenu(0,1,0,"My Band");
        int order = 0;
        for(BandProfileContent userBand : UserBand)
        {
            NavDrawer.add(0, userBand.BandName.hashCode(), order, userBand.BandName).setIcon(getResources().
                    getDrawable(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha));
        }
        navigationView.setNavigationItemSelectedListener(this);

        //Initial fragment when starting the app
        getSupportFragmentManager().beginTransaction().addToBackStack("Search")
        .replace(R.id.frame_container, new SearchFragment()).commit();
    }

    //Back-Press handler
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.root_page, menu);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        fragmentManager = getSupportFragmentManager();
        int popBackStackCount = fragmentManager.getBackStackEntryCount();
        fragmentTransaction = fragmentManager.beginTransaction();
        String CurrentFragment = fragmentManager.getBackStackEntryAt(popBackStackCount-1).getName();
        int id = item.getItemId();
        switch (id){
            case (R.id.Profile): {
                if(CurrentFragment != "Profile" && CurrentFragment != "EditProfile")
                {
                    GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    Fragment fragment = new ProfileFragment();
                                    ProfileContent.InitializeProfile(object);
                                    fragmentManager.beginTransaction().addToBackStack("Profile")
                                            .replace(R.id.frame_container, fragment).commit();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,gender,birthday,picture.width(300).height(300)");
                request.setParameters(parameters);
                request.executeAsync();
                }
                break;
            }
            case (R.id.Search): {
                if(CurrentFragment != "Search") {
                    fragmentTransaction.addToBackStack("Search");
                    fragment = new SearchFragment();
                }
                break;
            }
            case (R.id.Music):
            {
                if(CurrentFragment != "Music") {
                    fragmentTransaction.addToBackStack("Music");
                    fragment = new AudioRecorderFragment();
                }
                break;
            }
            case (R.id.CreateBand):
            {
                if(CurrentFragment != "CreateBand") {
                    fragmentTransaction.addToBackStack("CreateBand");
                    fragment = new CreateBandFragment();
                }
                break;
            }
            default :
            {
                for(BandProfileContent userBand : UserBand)
                {
                    if(id == userBand.BandName.hashCode() && CurrentFragment != userBand.BandName)
                    {
                        fragmentTransaction.addToBackStack(userBand.BandName);
                        fragment = new BandProfileFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("userBand",userBand);
                        fragment.setArguments(bundle);
                        break;
                    }
                }
            }
        }
        if (fragment != null && id != R.id.Profile) {
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
