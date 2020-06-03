package com.example.socialcook.afterlogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.socialcook.R;
import com.example.socialcook.afterlogin.admin.AdminPage;
import com.example.socialcook.beforelogin.LoginFragment;
import com.example.socialcook.beforelogin.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
///////////////////////////////////////////////////////////////////////////////////////////////////
            if (findViewById(R.id.fragment_mainPage) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                // Create a new Fragment to be placed in the activity layout
                Fragment firstFragment = new MainPageFrag();
                Bundle args = new Bundle();
                args.putString("email", email);
                firstFragment.setArguments(args);
                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                    /*
                    firstFragment.setArguments(getIntent().getExtras());
                    */
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_mainPage, firstFragment).commit();
            }
        } else {
            Toast.makeText(MainPage.this, "User not logged In",
                    Toast.LENGTH_SHORT).show();
        }
    }   //:)
    public void signout() {
//        Toast.makeText(this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
//        mAuth.signOut();
//        new Intent(MainPage.this, MainActivity.class);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent i = new Intent(MainPage.this, MainActivity.class);
        startActivity(i);
    }
    public void loadAdminPage() {
        // Create fragment and give it an argument specifying the article it should show
        Fragment newFragment = new AdminPage();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_mainPage, newFragment);
        transaction.addToBackStack(null);
// Commit the transaction
        transaction.commit();
    }
}