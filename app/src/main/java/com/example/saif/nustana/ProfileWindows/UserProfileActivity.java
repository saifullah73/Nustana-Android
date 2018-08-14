package com.example.saif.nustana.ProfileWindows;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saif.nustana.R;
import com.example.saif.nustana.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.example.saif.nustana.User.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "USER PROFILE";
    private static final int nameUpdate = 1;
    private static final int emailUpdate = 2;
    private static final int passwordUpdate = 3;
    private static final int phoneUpdate = 4;
    private static final int addressUpdate = 5;

    private Button signOutButton;
    private Button deleteButton;
    private TextView userNameView;
    private TextView emailView;
    private TextView passwordView;
    private TextView phoneView;
    private TextView addressView;
    private ImageView photoView;
    private ProgressDialog progressDialog;

    private FirebaseUser current_user;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("My Profile");


        photoView = findViewById(R.id.user_photo_view);
        signOutButton = findViewById(R.id.sign_out);
        deleteButton = findViewById(R.id.delete);
        userNameView = findViewById(R.id.user_name);
        emailView = findViewById(R.id.user_email);
        passwordView = findViewById(R.id.user_password);
        phoneView = findViewById(R.id.user_number);
        addressView = findViewById(R.id.user_address);

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference("users/" + UserInfo.userID);

        //loading screen until user is authenticated
        signOutButton.setEnabled(false);
        deleteButton.setEnabled(false);
        progressDialog = new ProgressDialog(UserProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(UserInfo.userEmail, UserInfo.userPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signOutButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                        progressDialog.hide();
                        Log.i(TAG, "user-reauthenticated");
                    }
                });

        updateUserAndUI();
        setOnClickListenersOnViews();

    }

    private void setOnClickListenersOnViews() {
        userNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder("Enter new Name", null, UserInfo.userName, nameUpdate);
            }
        });
        emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder("Enter new email", null, UserInfo.userEmail, emailUpdate);
            }
        });
        passwordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder("Enter new Password", null, UserInfo.userPassword, passwordUpdate);
            }
        });
        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder("Enter new Phone No.", null, UserInfo.userPhone, phoneUpdate);
            }
        });
        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder("Enter a new Address", null, UserInfo.userAddress, addressUpdate);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Delete Successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
                //finishing this activity opens the main activity and onAuthStateChanged will get called
            }
        });
    }

    public void dialogBuilder(String title, String message, final String displayText, final int mode) {

        // creates a dialog to edit various fields

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(message);
        final EditText input = new EditText(this);
        input.setText(displayText);
        dialog.setView(input);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface mdialog, int which) {
                String string = input.getText().toString().trim();
                if (!string.equals(displayText)) {
                    switch (mode) {
                        case nameUpdate:
                            if (!string.isEmpty())
                                updateName(string);
                            else
                                Toast.makeText(getApplicationContext(), "Please Enter a new Name", Toast.LENGTH_SHORT).show();
                            break;

                        case emailUpdate:
                            if (!string.isEmpty())
                                updateEmail(string);
                            else
                                Toast.makeText(getApplicationContext(), "Please Enter a new Email", Toast.LENGTH_SHORT).show();

                            break;

                        case passwordUpdate:
                            if (!(string.length() < 6))
                                updatePassword(string);
                            else
                                Toast.makeText(getApplicationContext(), "Please Enter a Stronger Password", Toast.LENGTH_SHORT).show();
                            break;
                        case phoneUpdate:
                            if (!(string.length() < 11))
                                updatePhone(string);
                            else
                                Toast.makeText(getApplicationContext(), "Please Enter a correct Phone Number", Toast.LENGTH_SHORT).show();
                            break;
                        case addressUpdate:
                            if (!(string.isEmpty()))
                                updateAddress(string);
                            else
                                Toast.makeText(getApplicationContext(), "Please Enter a new Address", Toast.LENGTH_SHORT).show();
                            break;


                    }
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void updateName(final String newName) {
        // user also need updating
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newName).build();
        current_user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUserDataInDatabase("userName", newName, "Name Changed");
                        }
                    }
                });
    }

    private void updatePhone(String newPhone) {
        // only database need updating
        updateUserDataInDatabase("userPhone", newPhone, "Phone Number Changed");
    }

    private void updateAddress(String newAddress) {
        // only database need updating
        updateUserDataInDatabase("userAddress", newAddress, "Address Changed");
    }

    private void updateEmail(final String newEmail) {
        // user also need updating
        current_user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            current_user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                updateUserDataInDatabase("userEmail", newEmail, "Email Change, Please verify your new email");
                                            }
                                        }
                                    });

                        }
                    }
                });
    }

    private void updatePassword(final String newPassword) {
        // user also need updating
        current_user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUserDataInDatabase("userPassword", newPassword, "Password Changed");
                        }
                    }
                });
    }

    private void updateUserDataInDatabase(String key, String value, final String toastMessage) {
        mReference.child(key).setValue(value, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    updateUserAndUI();
                    Log.i(TAG, "User Updated in Database");
                } else {
                    Toast.makeText(getApplicationContext(), "Error updating profile", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Error updating user");
                }
            }
        });

    }

    private void updateUserAndUI() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User updatedUser = dataSnapshot.getValue(User.class);
                setCurrentUserProfile(updatedUser);
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Error loading user data");
            }
        });

    }

    private void setCurrentUserProfile(User updatedUser) {

        UserInfo.setUserName(updatedUser.getUserName());
        UserInfo.setUserAddress(updatedUser.getUserAddress());
        UserInfo.setUserEmail(updatedUser.getUserEmail());
        UserInfo.setUserPhone(updatedUser.getUserPhone());
        UserInfo.setUserPassword(updatedUser.getUserPassword());
    }

    private void updateUI() {
        userNameView.setText(UserInfo.userName);
        emailView.setText(UserInfo.userEmail);
        passwordView.setText(UserInfo.userPassword);
        phoneView.setText(UserInfo.userPhone);
        addressView.setText(UserInfo.userAddress);
    }

}
