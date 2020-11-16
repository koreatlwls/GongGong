package com.example.gonggong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager=getSupportFragmentManager();
    private NearbyFacility menu1Fragment;
    private Notice menu2Fragment;
    private Settings menu3Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.navigation);
        menu1Fragment=new NearbyFacility();
        fragmentManager.beginTransaction().replace(R.id.frame,menu1Fragment).commit();

        //내비게이션 메뉴 선택 시, 선택된 프래그먼트를 보여주고, 나머지 프래그먼트들은 숨긴다.

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigationMenu1:{
                        if(menu1Fragment==null) {
                            menu1Fragment=new NearbyFacility();
                            fragmentManager.beginTransaction().
                                    add(R.id.frame, menu1Fragment).commit();
                        }
                        if(menu1Fragment!=null) fragmentManager.beginTransaction().show(menu1Fragment).commit();
                        if(menu2Fragment!=null) fragmentManager.beginTransaction().hide(menu2Fragment).commit();
                        if(menu3Fragment!=null) fragmentManager.beginTransaction().hide(menu3Fragment).commit();
                        break;
                    }
                    case R.id.navigationMenu2:{
                        if(menu2Fragment==null) {
                            menu2Fragment=new Notice();
                            fragmentManager.beginTransaction().
                                    add(R.id.frame, menu2Fragment).commit();

                        }
                        if(menu1Fragment!=null) fragmentManager.beginTransaction().hide(menu1Fragment).commit();
                        if(menu2Fragment!=null) fragmentManager.beginTransaction().show(menu2Fragment).commit();
                        if(menu3Fragment!=null) fragmentManager.beginTransaction().hide(menu3Fragment).commit();
                        break;
                    }
                    case R.id.navigationMenu3:{
                        if(menu3Fragment==null) {
                            menu3Fragment=new Settings();
                            fragmentManager.beginTransaction().
                                    add(R.id.frame, menu3Fragment).commit();
                        }
                        if(menu1Fragment!=null) fragmentManager.beginTransaction().hide(menu1Fragment).commit();
                        if(menu2Fragment!=null) fragmentManager.beginTransaction().hide(menu2Fragment).commit();
                        if(menu3Fragment!=null) fragmentManager.beginTransaction().show(menu3Fragment).commit();
                        break;
                    }
                }
                return true;
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }


}
