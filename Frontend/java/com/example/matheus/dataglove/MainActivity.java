package com.example.matheus.dataglove;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.rajawali3d.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Volley volley;
    private TextView[] tabela;
    private RajawaliRenderer renderer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomViewPager viewPager = findViewById(R.id.myViewPager);
        viewPager.setPagingEnabled(false);

        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    void setRenderer(RajawaliRenderer renderer) {
       this.renderer = renderer;
    }

    void setTabela(TextView[] tabela) {
        this.tabela = tabela;
    }

    void start() {
        renderer.setTabela(tabela);
        volley = new Volley(this, renderer);
        volley.setStop(false);
        volley.getJSON();
    }

    private void setupViewPager(CustomViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FirstTab(), "Simulação");
        adapter.addFragment(new SecondTab(), "Tabela");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ativado:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    volley.setStop(false);
                    volley.getJSON();
                }
                return true;
            case R.id.pausado:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    volley.setStop(true);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
