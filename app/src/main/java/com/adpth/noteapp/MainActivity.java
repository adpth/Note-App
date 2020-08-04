package com.adpth.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView Username_img,search_btn;
    FirebaseAuth auth;
    TextView Username;
    EditText search;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    DatabaseReference reference;
    ArrayList<Model> list;
    NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Lottie Animation Initialization
        final LottieAnimationView empty = findViewById(R.id.empty);

        //Going from MainActivity to ProfileActivity
        Username_img = findViewById(R.id.Username_img);
        Username_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(profile);
            }
        });

        Username = findViewById(R.id.Username);
        auth = FirebaseAuth.getInstance();

        //On Fab button click we will call NoteActivity to create to Note Task
        fab = findViewById(R.id.add_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NoteDetailActivity.class);
                startActivity(intent);
            }
        });

        //This code part is helpful in getting the User name and his profile Image
        //when he login with his Google Account
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account!=null)
        {
            String person_name = account.getDisplayName();
            String name = getResources().getString(R.string.Hello) + person_name;
            Username.setText(name);
            Uri person_img = account.getPhotoUrl();
            Glide.with(this).load(person_img).into(Username_img);
        }

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user!=null){
            reference = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        }
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Model model =dataSnapshot1.getValue(Model.class);
                    list.add(model);

                }
                adapter = new NoteAdapter(MainActivity.this,list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount()==0){
                    recyclerView.setVisibility(View.INVISIBLE);
                    empty.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        search = findViewById(R.id.search);
        search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searched = search.getText().toString();
                SearchData(searched);
            }
        });


    }

    private void SearchData(String searched) {
        //We will just Toast the Search Input Data of the User
        Toast.makeText(MainActivity.this,"You are searching "+ searched,Toast.LENGTH_LONG).show();
    }
}