package com.example.horizon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import util.JournalApi;

public class PostJournalActivity extends AppCompatActivity {
public static final int GALLERY_CODE=1;
    private Button saveButton;
    private TextView usernameText;
    private TextView dateText;
    private ImageView backgroundImage;
    private EditText editTitle;
    private EditText editThought;
    private ProgressBar postProgressBar;
    private ImageView cameraPost;
    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //Connection to FireStore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference=db.collection("Journal");
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);
saveButton=findViewById(R.id.savePost);
usernameText=findViewById(R.id.post_username_textView);
dateText=findViewById(R.id.post_date_texxtView);
backgroundImage=findViewById(R.id.backgroundImage);
editTitle=findViewById(R.id.post_title_editText);
editThought=findViewById(R.id.edit_post_thought);
postProgressBar=findViewById(R.id.postProgressBar);
cameraPost=findViewById(R.id.postCameraButton);
firebaseAuth = FirebaseAuth.getInstance();


if(JournalApi.getInstance()!=null){
    currentUserId=JournalApi.getInstance().getUserId();
    currentUserName=JournalApi.getInstance().getUsername();

    usernameText.setText(currentUserName);
}
authStateListener=new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user =firebaseAuth.getCurrentUser();
        if(user!=null){

        }else{

        }
    }
};



cameraPost.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.savePost:
               //save Post
               break;
           case R.id.postCameraButton:
               //get image from gallery

               Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
               galleryIntent.setType("image/*");
               startActivityForResult(galleryIntent, GALLERY_CODE);
               break;
       }
    }



});

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && requestCode==RESULT_OK){
            if(data!=null){
                imageUri=data.getData();
                backgroundImage.setImageURI(imageUri);  //show image
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }


    }
}
