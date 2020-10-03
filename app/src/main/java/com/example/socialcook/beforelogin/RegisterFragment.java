package com.example.socialcook.beforelogin;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.socialcook.R;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

public class RegisterFragment extends Fragment implements FireBase.IRegister, ActivityCompat.OnRequestPermissionsResultCallback {

    static final int REQUEST_CODE = 123;
    Uri image_uri;
    StorageReference mStorageRef;
    String randomKey = UUID.randomUUID().toString();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final MainActivity main = (MainActivity)getActivity();
        final View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = view.findViewById(R.id.signUpButton);
        Button photoButton = view.findViewById(R.id.photoButton);
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String userID = user.getUid();
         */
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imagesRef = mStorageRef.child("images/"+randomKey);
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference myRef = database.getReference("users");
        final EditText nameSignUp = view.findViewById(R.id.nameReg);
        final EditText emailSignUp = view.findViewById(R.id.emailReg);
        final TextView passwordSignUp = view.findViewById(R.id.passwordReg);
        final EditText addressSignUp = view.findViewById(R.id.addressReg);
        final EditText birthdaySignUp = view.findViewById(R.id.birthdayReg);
        final Spinner countrySignUp = view.findViewById(R.id.countrySpinner);
        final User userSignUp = new User();

        Locale[] locales = Locale.getAvailableLocales();
        final ArrayList<String> countries = new ArrayList<String>();

        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);
        countries.add(0, "Choose Country");
        /*for (String country : countries) {
            System.out.println(country);
        }*/
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySignUp.setAdapter(countryAdapter);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) +
                    ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) +
                    ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                        builder.setTitle("Please grant permissions:");
                        builder.setMessage("Camera, Read Storage, Write Storage");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[] {
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        }, REQUEST_CODE);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                    else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[] {
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                }, REQUEST_CODE);
                        selectImage();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean isDefault = false;
                    userSignUp.setAddress(addressSignUp.getText().toString());
                    userSignUp.setEmail(emailSignUp.getText().toString());
                    userSignUp.setName(nameSignUp.getText().toString());
                    userSignUp.setBirthday(birthdaySignUp.getText().toString());
                    userSignUp.setDescription("");
                    if (countrySignUp.getSelectedItem().toString() == "Choose Country") {
                        userSignUp.setCountry("Afghanistan");
                    }
                    else {
                        userSignUp.setCountry(countrySignUp.getSelectedItem().toString());
                    }
                    if (image_uri == null) {
                            FireBase.storageRef.child("images/defaultUserImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    image_uri = uri;
                                }
                            });
                            isDefault = true;
                    }
                    register(emailSignUp , passwordSignUp , myRef, userSignUp , imagesRef , image_uri, isDefault);
                }
                catch (Exception NullPointerException) {
                    Toast.makeText(getContext(), "you need to fill everything!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
                    image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                    startActivityForResult(cameraIntent, 1);
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1); */
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView photoImage = (ImageView)getView().findViewById(R.id.photoImage);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.d("IMAGECAPTURED", "IMAGECAPTURED!!");
            if (requestCode == 1) {
                photoImage.setImageURI(image_uri);
            }
            else if (requestCode == 2) {
                image_uri = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Context applicationContext = MainActivity.getContextOfApplication();
                Cursor c = applicationContext.getContentResolver().query(image_uri,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                photoImage.setImageBitmap(thumbnail);
            }
        }
    }

    @Override
    public void register(TextView email , TextView password , final DatabaseReference myRef, final User userSignUp , final StorageReference imagesRef , final Uri image_uri, final boolean isDefault) {
        final MainActivity main = (MainActivity)getActivity();
        userSignUp.setImagePath("images/"+randomKey);
        FireBase.getAuth().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(main, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (!isDefault) {
                                imagesRef.putFile(image_uri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot2) {
                                                // Get a URL to the uploaded content
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                // ...
                                            }
                                        })
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                                                Log.d("downloadUrl", ""+downloadUrl);
                                            }
                                        });
                            }
                            else {
                                userSignUp.setImagePath("images/defaultUserImage.jpg");
                            }

                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getContext(), "Register Succeed.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FireBase.getAuth().getCurrentUser();
                            user.updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(userSignUp.name).build());
                            userSignUp.setUID(user.getUid());
                            myRef.child(user.getUid()).setValue(userSignUp);
                            main.loadLoginFrag();
                        }
                         else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(main, "Register failed.",
                                    Toast.LENGTH_SHORT).show();
                            main.loadLoginFrag();
                        }

                        // ...
                    }
                });
    }
}