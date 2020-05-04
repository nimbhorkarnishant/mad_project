package com.example.mad_project;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mad_project.ui.faculty_access.register_candi_form;
import com.example.mad_project.ui.user_authentication.login_activity;
import com.example.mad_project.ui.user_authentication.user;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.UserHandle;
import android.preference.Preference;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView full_name,textView,textView1,textView2,textView3;
    String user_name,user_email_id,user_prn,user_access,user_block,user_dept,user_year,user_id;
    Menu menu;
    user user_sharep;




    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        user_email_id=fAuth.getCurrentUser().getEmail();
        DocumentReference documentReference = fStore.collection("users").document(userId);
        DatabaseReference user_db_reff= FirebaseDatabase.getInstance().getReference().child("mad_project").child("user").child(userId);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        menu=navigationView.getMenu();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        full_name = headerView.findViewById(R.id.fullName);
        textView=headerView.findViewById(R.id.textView);
        textView1=headerView.findViewById(R.id.textView1);
        textView2=headerView.findViewById(R.id.textView2);
        textView3=headerView.findViewById(R.id.textView3);

        user_db_reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("------------------- database_---------------------");
                user_name=dataSnapshot.child("full_name").getValue().toString();
                user_prn=dataSnapshot.child("prn_no").getValue().toString();
                user_access=dataSnapshot.child("user_access").getValue().toString();
                user_block=dataSnapshot.child("user_block").getValue().toString();
                user_dept=dataSnapshot.child("user_dept").getValue().toString();
                user_id=dataSnapshot.child("user_id").getValue().toString();
                user_year=dataSnapshot.child("user_year").getValue().toString();


                System.out.println("Name id---->"+user_name);

                full_name.setText(user_name);
                textView.setText(user_email_id);
                textView1.setText(user_prn);
                textView2.setText(user_access+" Of MITAOE");
                textView3.setText(user_year+" Btech"+" "+user_dept);


                if (user_access.equals("faculty")){
                }
                else if (user_access.equals("HOD")){

                }
                else{
                    menu.removeItem(R.id.nav_faculty_acces);
                }
                SharedPreferences sharedPreferences=getSharedPreferences("user_detail", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id",user_id);
                editor.putString("user_name",user_name);
                editor.putString("user_prn",user_prn);
                editor.putString("user_access",user_access);
                editor.putString("user_year",user_year);
                editor.putString("user_dept",user_dept);
                editor.putString("user_block",user_block);
                editor.putString("user_email_id",user_email_id);
                editor.apply();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPreferences sharedPreferences=getSharedPreferences("user_detail", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                FirebaseAuth.getInstance().signOut();
                editor.clear();
                editor.apply();
                Toast.makeText(getBaseContext(), "You Logout Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),login_activity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("count Fragment--->"+getSupportFragmentManager().getBackStackEntryCount());
        getSupportFragmentManager().popBackStack();

    }


}
