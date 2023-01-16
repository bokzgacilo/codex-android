package com.example.codex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.sql.Array;
import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private ListView itemlistview;
    private Button googleSignoutButton;
    private TextView greeting;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userDisplayName;
    private ArrayList<String> dataTable = new ArrayList<String>();
//    private ArrayList<String> dataTable = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        googleSignoutButton = findViewById(R.id.GoogleSignoutButton);
        greeting = findViewById(R.id.Greeting);
        itemlistview = findViewById(R.id.ItemListView);

        googleSignoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Dashboard.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null){
            greeting.setText("This is your files, " + account.getDisplayName());
            userDisplayName = account.getDisplayName();

            db.collection(userDisplayName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    dataTable.add(document.getId());
                                }
                                ArrayAdapter adapter = new ArrayAdapter<>(Dashboard.this, android.R.layout.simple_list_item_1, dataTable);


//                                ArrayAdapter adapter = new ArrayAdapter<>(Dashboard.this, android.R.layout.simple_list_item_1, dataTable);
                                itemlistview.setAdapter(adapter);

                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }


    }

}