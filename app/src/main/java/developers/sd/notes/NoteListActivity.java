package developers.sd.notes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends SingleFragmentActivity {


    private NavigationView navigationView;
    private DrawerLayout drawer;
    public static int navItemIndex = 0;
    private String[] activityTitles;
    private Toolbar tToolbar;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }


    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.fragment_container);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        tToolbar = (Toolbar) findViewById(R.id.tToolbar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        loadHomeFragment();
        setUpNavigationView();
        navigationView.getMenu().getItem(0).setChecked(true);
        new BackgroundFunctions().execute();
    }


    private void loadHomeFragment() {
        drawer.closeDrawers();
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_all_notes:
                        if (navItemIndex != 0) {
                            NoteListFragment noteListFragment = new NoteListFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noteListFragment).commit();
                        }
                        navigationView.getMenu().getItem(0).setChecked(true);
                        navItemIndex = 0;
                        break;
                    case R.id.nav_recent:
                        if (navItemIndex != 1) {
                            NoteListFragmentRecent noteListFragmentRecent = new NoteListFragmentRecent();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noteListFragmentRecent).commit();
                        }
                        navigationView.getMenu().getItem(1).setChecked(true);
                        navItemIndex = 1;
                        break;
                    case R.id.nav_fav:
                        if (navItemIndex != 2) {
                            NoteListFragmentFav noteListFragmentFav = new NoteListFragmentFav();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noteListFragmentFav).commit();
                        }
                        navigationView.getMenu().getItem(2).setChecked(true);
                        navItemIndex = 2;
                        break;
                    case R.id.nav_bin:
                        if (navItemIndex != 3) {
                            NoteListFragmentBin noteListFragmentBin = new NoteListFragmentBin();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noteListFragmentBin).commit();
                        }
                        navigationView.getMenu().getItem(3).setChecked(true);
                        navItemIndex = 3;
                        break;
                    case R.id.nav_upload_drive:
                        Intent intent1 = new Intent(NoteListActivity.this, UploadToDriveActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_restore_drive:
                        Intent intent2 = new Intent(NoteListActivity.this, RestoreDriveActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_about_us:
                        Intent intent3 = new Intent(NoteListActivity.this, AboutUs.class );
//                        navigationView.getMenu().getItem(4).setChecked(false);
//                        navigationView.getMenu().findItem(R.id.nav_about_us).setChecked(false);
                        startActivity(intent3);
                        break;
                    default:
                        navItemIndex = 0;

                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, tToolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                navigationView.getMenu().getItem(0).setChecked(true);
                NoteListFragment noteListFragment = new NoteListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, noteListFragment).commit();
            } else {
                super.onBackPressed();
            }
        }
    }

    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int audio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private class BackgroundFunctions extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground (Void...params) {
            checkAndRequestPermissions();
            return null;
        }

    }
}
