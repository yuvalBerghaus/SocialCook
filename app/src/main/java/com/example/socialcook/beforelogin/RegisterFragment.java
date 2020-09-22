package com.example.socialcook.beforelogin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.socialcook.R;
import com.example.socialcook.classes.User;
import com.example.socialcook.firebase.FireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class RegisterFragment extends Fragment implements FireBase.IRegister {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final MainActivity main = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button registerButton = view.findViewById(R.id.signUpButton);
        Button photoButton = view.findViewById(R.id.photoButton);
        ImageView photoImage = view.findViewById(R.id.photoImage);
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String userID = user.getUid();
         */
        final FirebaseDatabase database = FireBase.getDataBase();
        final DatabaseReference myRef = database.getReference("users");
        final EditText nameSignUp = view.findViewById(R.id.nameReg);
        final EditText emailSignUp = view.findViewById(R.id.emailReg);
        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_country);
// Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);

        final TextView passwordSignUp = view.findViewById(R.id.passwordReg);
        final EditText addressSignUp = view.findViewById(R.id.addressReg);
        final EditText birthdaySignUp = view.findViewById(R.id.birthdayReg);
        final User userSignUp = new User();

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    userSignUp.setAddress(addressSignUp.getText().toString());
                    userSignUp.setEmail(emailSignUp.getText().toString());
                    userSignUp.setName(nameSignUp.getText().toString());
                    userSignUp.setBirthday(birthdaySignUp.getText().toString());
                    register(emailSignUp , passwordSignUp , myRef, userSignUp);
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
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
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    photoImage.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Context applicationContext = MainActivity.getContextOfApplication();
                Cursor c = applicationContext.getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of gallery image", picturePath+"");
                photoImage.setImageBitmap(thumbnail);
            }
        }
    }
    @Override
    public void register(TextView email , TextView password , final DatabaseReference myRef, final User userSignUp) {
        final MainActivity main = (MainActivity)getActivity();
        FireBase.getAuth().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(main, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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