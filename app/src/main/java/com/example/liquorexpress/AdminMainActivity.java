package com.example.liquorexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.liquorexpress.adapters.PagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabItem tabProducts, tabCategories, tabUsers, tabOrders;

    private TextView drawerName;
    private TextView drawerEmail;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private PagerAdapter pagerAdapter;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String[] titles = new String[]{"Products", "Categories", "Users", "Orders"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        toolbar = findViewById(R.id.adminMainToolbar);
        drawerLayout = findViewById(R.id.adminMainDrawer);
        navigationView = findViewById(R.id.adminMainNavigationView);
        viewPager2 = findViewById(R.id.adminMainViewPager);
        tabLayout = findViewById(R.id.adminMainTabLayout);
        tabProducts = findViewById(R.id.tabAdminProducts);
        tabCategories = findViewById(R.id.tabAdminCategories);
        tabUsers = findViewById(R.id.tabAdminUsers);
        tabOrders = findViewById(R.id.tabAdminOrders);

        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getApplicationContext().getSharedPreferences("liquorExpress", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUserName = sharedPreferences.getString("loggedUserName", "");

        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeader = navigationView.getHeaderView(0);

        drawerName = navigationHeader.findViewById(R.id.drawerHeaderName);
        drawerEmail = navigationHeader.findViewById(R.id.drawerHeaderEmail);

        drawerName.setText(currentUserName +" (Admin)");
        drawerEmail.setText(currentUser != null && currentUser.getEmail() != null ? currentUser.getEmail() : "-");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        pagerAdapter = new PagerAdapter(this, tabLayout.getTabCount(), true);
        viewPager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionAdd) {
            int index = tabLayout.getSelectedTabPosition();

            switch (index) {
                case 0:
                    Intent intent1 = new Intent(getApplicationContext(), FoodEditActivity.class);
                    intent1.putExtra("id", "");
                    startActivity(intent1);
                    break;
                case 1:
                    Intent intent2 = new Intent(getApplicationContext(), CategoryItemActivity.class);
                    intent2.putExtra("id", "");
                    startActivity(intent2);
                    break;
                case 2:
                    Intent intent3 = new Intent(getApplicationContext(), UserItemActivity.class);
                    intent3.putExtra("id", "");
                    startActivity(intent3);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.itemAdminProducts) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
        }

        if (item.getItemId() == R.id.itemAdminCat) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
        }

        if (item.getItemId() == R.id.itemAdminUsers) {
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            tab.select();
        }
        if (item.getItemId() == R.id.itemAdminOrders) {
            TabLayout.Tab tab = tabLayout.getTabAt(3);
            tab.select();
        }
        if (item.getItemId() == R.id.itemAdminSignOut) {
            firebaseAuth.signOut();
            editor.putString("loggedUserName", "");
            editor.putBoolean("isLoggedIn", false);
            editor.putBoolean("isAdmin", false);
            editor.apply();

            Intent intent = new Intent(AdminMainActivity.this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        return false;
    }
}