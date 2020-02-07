package com.example.horizon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import util.JournalApi;

public class CreateAccount extends AppCompatActivity {

    private Button createAccountButton;
    private EditText enterEmailCreate;
    private EditText enterPasswordCreate;
    private ProgressBar progressBarCreate;
    private EditText userNameNew;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    // FireStore connection
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    private CollectionReference collectionReference=db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firebaseAuth=FirebaseAuth.getInstance();



        createAccountButton=findViewById(R.id.create_account_button_new);
        progressBarCreate=findViewById(R.id.create_account_progress);
        enterEmailCreate=findViewById(R.id.email_account);
        enterPasswordCreate=findViewById(R.id.password_account);
        userNameNew=findViewById(R.id.username_account);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser=firebaseAuth.getCurrentUser();
                if(currentUser!=null){
                    // user is already logged in

                }
                else{
                    // no user yet
                }
            }
        };
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(enterEmailCreate.getText().toString()) && !TextUtils.isEmpty(enterPasswordCreate.getText().toString()) && !TextUtils.isEmpty(userNameNew.getText().toString())) {
                    String email=enterEmailCreate.getText().toString().trim();
                    String password=enterPasswordCreate.getText().toString().trim();
                    String username=userNameNew.getText().toString().trim();
                    createUserEmailAccount(email, password, username);
                }else{
                    Toast.makeText(CreateAccount.this,"Empty Fields Not Allowed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void createUserEmailAccount(String email, final String password, final String username){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)){
            progressBarCreate.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //we take user to add journal
                        currentUser=firebaseAuth.getCurrentUser();
                        final String currentUserTd=currentUser.getUid();


                        //Create a user Map so we can create a user in the User collection

                        Map<String,String> userObj=new HashMap<>();
                        userObj.put("userId",currentUserTd);
                        userObj.put("username",username);
                        userObj.put("password",password);

                        //save to our fireStore document
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.getResult().exists()){
                                            progressBarCreate.setVisibility(View.INVISIBLE);
                                            String name=task.getResult().getString("username");

                                            JournalApi journalApi=JournalApi.getInstance();                           //Global API
                                            journalApi.setUserId(currentUserTd);
                                            journalApi.setUserId(name);

                                            Intent intent=new Intent(CreateAccount.this,PostJournalActivity.class);
                                            intent.putExtra("username",name);
                                            intent.putExtra("userId",currentUserTd);
                                            startActivity(intent);
                                        }else{
                                            progressBarCreate.setVisibility(View.INVISIBLE);

                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                    else {
                        // something went wrong
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else{

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}
