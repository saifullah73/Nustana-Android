package com.example.saif.nustana;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saif.nustana.Login.WelcomeScreenActivity;
import com.example.saif.nustana.Menu.ShopMenuActivity;
import com.example.saif.nustana.Order.OrdersTerminalActivity;
import com.example.saif.nustana.Order.ShoppingCart.ShoppingCart;
import com.example.saif.nustana.ProfileWindows.UserProfileActivity;
import com.example.saif.nustana.Shop.Shop;
import com.example.saif.nustana.Shop.ShopHomeActivity;
import com.example.saif.nustana.Shop.ShopInfo;
import com.example.saif.nustana.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    //constants declaration
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "Main Activity";
    static boolean calledAlready = false;

    //UI instances
    private TextView mDescription, mAbout, mPhone, mAddress;
    private Button mButton, viewMenuButton;

    //instance declarations
    private FirebaseAuth mAuthentication;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser current_user;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //enable local data storage
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }


        mDescription = findViewById(R.id.mDescription);
        mAbout = findViewById(R.id.mAbout);
        mPhone = findViewById(R.id.mPhoneNumber);
        mAddress = findViewById(R.id.mAddress);
        mButton = findViewById(R.id.mCallButton);
        viewMenuButton = findViewById(R.id.mViewMenuButton);

        mAuthentication = FirebaseAuth.getInstance();
        current_user = mAuthentication.getCurrentUser();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        loadShop();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open dialer
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ShopInfo.shopNumber));
                startActivity(intent);
            }
        });
        viewMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open menu
                viewMenuButton.setEnabled(false);
                ApplicationMode.currentMode = "customer";
                Intent i1 = new Intent(getApplicationContext(), ShopMenuActivity.class);
                startActivity(i1);
            }
        });

        //keeps user logged in
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    User.loadCurrentUser(user.getUid());

                } else {
                    progressDialog.hide();
                    progressDialog.dismiss();
                    Intent i2 = new Intent(MainActivity.this, WelcomeScreenActivity.class);
                    i2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivityForResult(i2, RC_SIGN_IN);
                }
            }
        };

    }

    private void inquiryDialog() {
        //dialog to verify owner
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are You a owner?");
        dialog.setMessage("Please enter password to verify, or proceed as a visitor");
        final EditText input = new EditText(this);
        dialog.setView(input);
        dialog.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface mdialog, int which) {

                if (input.getText().toString().trim().equals(BuildConfig.Key)) {
                    ApplicationMode.currentMode = "owner";
                    Intent i = new Intent(getApplicationContext(), ShopHomeActivity.class);
                    startActivity(i);
                } else {

                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setNeutralButton("Proceed anyway", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ApplicationMode.currentMode = "visitor";
                Intent i2 = new Intent(getApplicationContext(), ShopHomeActivity.class);
                startActivity(i2);
            }
        });
        dialog.create();
        dialog.show();
    }

    private void loadShop() {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("/shopInfo");
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shop shop = dataSnapshot.getValue(Shop.class);
                setCurrentShopProfile(shop);
                updateUI();
                progressDialog.hide();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "Error loading shop data");
            }
        });
    }

    private void setCurrentShopProfile(Shop shop) {
        ShopInfo.setShopName(shop.getShopName());
        ShopInfo.setShopNumber(shop.getShopNumber());
        ShopInfo.setShopAddress(shop.getShopAddress());
        ShopInfo.setShopDescription(shop.getShopDescription());
        ShopInfo.setShopAbout(shop.getShopAbout());

    }

    private void updateUI() {
        Log.i(TAG, "Shop Info" + ShopInfo.shopName);
        mPhone.setText(ShopInfo.shopNumber);
        mAddress.setText(ShopInfo.shopAddress);
        mDescription.setText(ShopInfo.shopDescription);
        mAbout.setText(ShopInfo.shopAbout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.i(TAG, "result");

            if (resultCode == Activity.RESULT_OK) {
                // nothing required
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "result cancelled");
                finish();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        viewMenuButton.setEnabled(true);
        mAuthentication.addAuthStateListener(mAuthStateListener);
        loadShop();
        ApplicationMode.currentMode = "customer";
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mAuthentication.removeAuthStateListener(mAuthStateListener);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_option:
                Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.shop_option:
                inquiryDialog();
                return true;
            case R.id.shoppingCart:
                Intent shoppingCartIntent = new Intent(getApplicationContext(), ShoppingCart.class);
                startActivity(shoppingCartIntent);
                return true;
            case R.id.myOrders:
                ApplicationMode.ordersViewer = "customer";
                Intent myOrders = new Intent(getApplicationContext(), OrdersTerminalActivity.class);
                startActivity(myOrders);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
