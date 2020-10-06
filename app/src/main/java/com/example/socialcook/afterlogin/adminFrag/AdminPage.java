package com.example.socialcook.afterlogin.adminFrag;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import android.widget.ListView;
import android.widget.Toast;
import static com.example.socialcook.afterlogin.adminFrag.NotificationApp.CHANNEL_1_ID;
import static com.example.socialcook.afterlogin.adminFrag.NotificationApp.CHANNEL_2_ID;
import com.example.socialcook.R;
import com.example.socialcook.afterlogin.activities.MainPage;
import com.example.socialcook.beforelogin.MainActivity;
import com.example.socialcook.classes.Recipe;
import com.example.socialcook.firebase.FireBase;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class AdminPage extends Fragment {//:)
    EditText recipeName;
    Uri image_uri;
    StorageReference mStorageRef;
    String randomKey = UUID.randomUUID().toString();
    String downloadUrl;
    ImageView recipePhotoImage;
    private NotificationManagerCompat notificationManager;//this class shows the actual notification
    static final int REQUEST_CODE = 123;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        notificationManager = NotificationManagerCompat.from(getContext());
        final Recipe recipe = new Recipe();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Write a message to the database
        final FirebaseDatabase database = FireBase.getDataBase();
        ArrayList<DatabaseReference>a;
        final DatabaseReference myRef = database.getReference("recipes");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imagesRef = mStorageRef.child("images/"+randomKey);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_page, container, false);
        MainPage mainPage = (MainPage) getActivity();
        final ListView listView = view.findViewById(R.id.listviewmain);
        Button recipePhotoButton = view.findViewById(R.id.recipePhotoButton);
        Button addAmount = view.findViewById(R.id.buttonAddAmount);
        Button addMl = view.findViewById(R.id.buttonAddMl);
        Button addG = view.findViewById(R.id.buttonAddMg);
        Button saveButton = view.findViewById(R.id.buttonSend);
        recipeName = view.findViewById(R.id.recipeNameInput);
        final EditText recipeDescription = view.findViewById(R.id.descriptionID);
        final EditText recipeType = view.findViewById(R.id.recipeTypeInput);
        final EditText recipeImage = view.findViewById(R.id.urlInput);
        final EditText recipeAmountKey = view.findViewById(R.id.keyAmount);
        final EditText recipeAmountValue = view.findViewById(R.id.valueAmount);
        final EditText recipeGKey = view.findViewById(R.id.keyMg);
        final EditText recipeGValue = view.findViewById(R.id.valueMg);
        final EditText recipeMlKey = view.findViewById(R.id.keyMl);
        final EditText recipeMlValue = view.findViewById(R.id.valueMl);
        final ArrayList<String>arrayList;
        ArrayAdapter<String>adapter;
        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_list_item_1 , arrayList);
        listView.setAdapter(adapter);

        recipePhotoButton.setOnClickListener(new View.OnClickListener() {
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

        //Button add for the amount - if it is empty it wont add to the object type Recipe otherwise everything will be added and a toast will confirm so
        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipeAmountKey.getText().toString().equals("") || recipeAmountValue.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter item and amount!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    recipe.setAmount(recipeAmountKey.getText().toString() , Integer.parseInt(recipeAmountValue.getText().toString()));
                    Toast.makeText(getContext(), recipeAmountValue.getText().toString()+" "+recipeAmountKey.getText().toString()+" was added to the list", Toast.LENGTH_SHORT).show();
                    recipeAmountKey.getText().clear();
                    recipeAmountValue.getText().clear();
                }
            }
        });
        //Adding grams
        addG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeGKey.getText().toString().equals("") || recipeGValue.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter item and Grams", Toast.LENGTH_SHORT).show();
                    return;
                }
                recipe.setG(recipeGKey.getText().toString() , Integer.parseInt(recipeGValue.getText().toString()));
                Toast.makeText(getContext(), recipeGValue.getText().toString()+" grams of "+recipeGKey.getText().toString()+" was added to the list", Toast.LENGTH_SHORT).show();
                recipeGKey.getText().clear();
                recipeGValue.getText().clear();
            }
        });
        addMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recipeMlKey.getText().toString().equals("") || recipeMlValue.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter item and ML", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    recipe.setML(recipeMlKey.getText().toString() , Integer.parseInt(recipeMlValue.getText().toString()));
                    Toast.makeText(getContext(), recipeMlValue.getText().toString()+" ML of "+recipeMlKey.getText().toString()+" was added to the list", Toast.LENGTH_SHORT).show();
                    recipeMlKey.getText().clear();
                    recipeMlValue.getText().clear();
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    recipePhotoImage = (ImageView)getView().findViewById(R.id.recipePhotoImage);
                    recipe.setName(recipeName.getText().toString());
                    recipe.setType(recipeType.getText().toString());
                    Log.d("laChupacaBraaaaa",image_uri+"");
                    Log.d("loNishmar" , recipeImage.getText().toString());
                    if((recipeImage.getText().toString().matches("") && image_uri == null)|| recipeName.getText().toString().matches("") || recipeType.getText().toString().matches("")) {
                        throw new NullPointerException();
                    }
                    if (image_uri != null && !recipeImage.getText().toString().matches("")) {
                        Toast.makeText(getContext(), "You can't upload an image and use an image URL at the same time!", Toast.LENGTH_SHORT).show();
                        image_uri = null;
                        recipeImage.getText().clear();
                        recipePhotoImage.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                        return;
                    }

                    if (image_uri != null && recipeImage.getText().toString().matches("")) {
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
                                        downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                                        Log.d("downloadUrl", ""+downloadUrl);
                                    }
                                });
                    }
                    else if (image_uri == null && !recipeImage.getText().toString().matches("")){
                        recipe.setImageUrl(recipeImage.getText().toString());
                    }
                    if (image_uri != null) {
                        recipe.setImageUrl("images/"+randomKey);
                    }
                    recipe.setRecipeDescription(recipeDescription.getText().toString());
                    String result = "recipe name: "+recipe.getRecipeName()+"\n recipe Type : "+recipe.getRecipeType()+"\nRecipeAmount : "+recipe.convertRecipeAmountIteration()+"\nRecipe ML : "+recipe.convertRecipeMLIteration()+"\nRecipe Grams : "+recipe.convertRecipeGIteration();
                    arrayList.add(result);
                    myRef.child(recipe.getRecipeName()).setValue(recipe);
                    sendOnChannel1();
                    recipeName.getText().clear();
                    recipeImage.getText().clear();
                    recipeType.getText().clear();
                    recipeAmountKey.getText().clear();
                    recipeAmountValue.getText().clear();
                    recipeGKey.getText().clear();
                    recipeGValue.getText().clear();
                    recipeMlKey.getText().clear();
                    recipeMlValue.getText().clear();
                    recipeDescription.getText().clear();
                    image_uri = null;
                    recipePhotoImage.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                    recipe.clear();
                    Toast.makeText(getContext(), "Recipe added successfully!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception NullPointerException) {
                    Toast.makeText(getContext(), "you need to fill everything!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        return view;
    }
    public void sendOnChannel1() {
        String title = recipeName.getText().toString();
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(title+" was added to the list!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }
    public void sendOnChannel2(View v) {
        String title = recipeName.getText().toString();
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle(title)
                .setContentText(title+" was added to the list!")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(2, notification);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Photo!");
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
        super.onActivityResult(requestCode, resultCode, data);
        recipePhotoImage = (ImageView)getView().findViewById(R.id.recipePhotoImage);
        if (resultCode == Activity.RESULT_OK) {
            Log.d("IMAGECAPTURED", "IMAGECAPTURED!!");
            if (requestCode == 1) {
                recipePhotoImage.setImageURI(image_uri);
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
                recipePhotoImage.setImageBitmap(thumbnail);
            }
        }
    }
}