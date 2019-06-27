package com.example.tourmate;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;

public class AddMemoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveMemoryBtn, chooseImageBtn;
    private ImageView memoryImage;
    private EditText imageCaptionAMA;
    private Uri imageUri;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private String uId;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    private static final int IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memory);

        saveMemoryBtn = findViewById(R.id.saveMemoryBtnID);
        memoryImage = findViewById(R.id.memoryImageIVID);
        imageCaptionAMA = findViewById(R.id.imageCaptionETID);
        chooseImageBtn = findViewById(R.id.chooseImageBtnID);

        saveMemoryBtn.setOnClickListener(this);
        chooseImageBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.chooseImageBtnID:
                openFileChooser();
                break;
            case R.id.saveMemoryBtnID:
                if(uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(this, "Uploading in progress", Toast.LENGTH_LONG).show();
                }else{
                    saveMemory();
                }

                break;

        }
    }

    private void saveMemory() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Upload");
        storageReference = FirebaseStorage.getInstance().getReference("Upload");

        user = mAuth.getInstance().getCurrentUser();
        uId = user.getUid();

        final String imgCaption = imageCaptionAMA.getText().toString().trim();
        if(imgCaption.isEmpty()){
            imageCaptionAMA.setError("Enter Caption");
            imageCaptionAMA.requestFocus();
            return;
        }

        final StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

        final ProgressDialog pd = new ProgressDialog(AddMemoryActivity.this);
        pd.setTitle("Uploading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = String.valueOf(uri);

                                String uploadId = databaseReference.push().getKey();
                                Upload upload = new Upload(uploadId, imgCaption, imageUrl, uId);


                                databaseReference.child(uploadId).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(AddMemoryActivity.this, "Image is uploaded successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(),TripMemoryActivity.class);
                                            startActivity(intent);
                                            pd.dismiss();
                                            finish();
                                        }else{
                                            Toast.makeText(AddMemoryActivity.this, "Image is not uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            imageUri = data.getData();
            Picasso.with(this).load(imageUri).rotate(90).into(memoryImage);

        }
    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    public void backBtnAddTrip(View view) {
        onBackPressed();
    }


    public void imageRotate(View view) {
        float deg = memoryImage.getRotation() + 90F;
        memoryImage.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
    }
}
