package com.example.saif.nustana.Menu.MenuItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.saif.nustana.ApplicationMode;
import com.example.saif.nustana.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class itemEditor extends AppCompatActivity {
    private static final String TAG = "itemEditor";
    private static final int RC_PHOTO_PICKER = 1;

    private Spinner mCategorySpinner;
    private EditText itemNameEdit, itemDescriptionEdit, itemPriceEdit;
    private ImageView itemImageEdit;
    private ImageButton removeButton, addButton;

    private boolean mode;
    private Uri selectedImageUri;
    private Item item;
    private ProgressDialog progressDialog;

    private DatabaseReference mReference;
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_editor);

        mCategorySpinner = findViewById(R.id.item_category_selector);
        itemNameEdit = findViewById(R.id.item_name_edit_view);
        itemPriceEdit = findViewById(R.id.item_price_edit_view);
        itemDescriptionEdit = findViewById(R.id.item_description_edit_view);
        itemImageEdit = findViewById(R.id.item_image_view_edit);
        removeButton = findViewById(R.id.remove_button);
        addButton = findViewById(R.id.add_item_image);

        mStorageReference = FirebaseStorage.getInstance().getReference().child("itemPictures");
        mReference = FirebaseDatabase.getInstance().getReference().child("items");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open gallery to retrieve an image
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePicture();
            }
        });


        setupSpinner();
        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.itemName))) {
            setTitle("Edit Item");
            mode = true; //indicates an edit function
            ItemInfo.setItemInfo(intent);
            mReference.child(ItemInfo.itemCategory);
            setFields();

        } else {
            //no need to set itemInfo in this case but rather we will build an Item object using ItemInfo set by data entered
            setTitle("New Item");
            mode = false; // indicates a new item function
        }

    }


    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter categorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mCategorySpinner.setAdapter(categorySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.burger))) {
                        ItemInfo.itemCategory = getString(R.string.burger);
                    } else if (selection.equals(getString(R.string.pizza))) {
                        ItemInfo.itemCategory = getString(R.string.pizza);
                    } else if (selection.equals(getString(R.string.dessert))) {
                        ItemInfo.itemCategory = getString(R.string.dessert);
                    } else if (selection.equals(getString(R.string.drink))) {
                        ItemInfo.itemCategory = getString(R.string.drink);
                    } else ItemInfo.itemCategory = getString(R.string.others);
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setFields() {
        itemNameEdit.setText(ItemInfo.itemName);
        itemPriceEdit.setText(ItemInfo.itemPrice);
        itemDescriptionEdit.setText(ItemInfo.itemDescription);
        switch (ItemInfo.itemCategory) {
            case "Burger":
                mCategorySpinner.setSelection(0);
                break;
            case "Pizza":
                mCategorySpinner.setSelection(1);
                break;
            case "Dessert":
                mCategorySpinner.setSelection(2);
                break;
            case "Drink":
                mCategorySpinner.setSelection(3);
                break;
            default:
                mCategorySpinner.setSelection(4);
                break;
        }
        mCategorySpinner.setEnabled(false);
        if (!ItemInfo.imageUrl.isEmpty()) {
            mCategorySpinner.setEnabled(false);
            Glide.with(itemImageEdit.getContext())
                    .load(ItemInfo.imageUrl)
                    .into(itemImageEdit);
        }
    }

    private void setupProgressDialog() {

        //dialog which shows progress as item is uploaded to database
        progressDialog = new ProgressDialog(itemEditor.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                // if user cancels upload then don't continue uploading in background
                progressDialog.hide();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(itemEditor.this);
                alertDialog.setTitle("Cancel Upload ?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        List<UploadTask> tasks = mStorageReference.getActiveUploadTasks();
                        if (tasks.size() > 0) {
                            // Get the task monitoring the upload
                            UploadTask task = tasks.get(0);
                            task.addOnCanceledListener(new OnCanceledListener() {
                                @Override
                                public void onCanceled() {
                                    Toast.makeText(getApplicationContext(), "Upload Cancelled", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                            task.cancel();

                        }
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();
                    }
                });
                alertDialog.create();
                alertDialog.show();
            }
        });
    }

    private boolean validateEditFields(String name, String description, String price) {
        if (name.isEmpty()) {
            itemNameEdit.setError("Please Enter a name");
            return false;
        } else if (description.isEmpty()) {
            itemDescriptionEdit.setError("Please enter some suitable description");
            return false;
        } else if (price.isEmpty()) {
            itemPriceEdit.setError("Please enter some price");
            return false;
        }
        return true;
    }

    private void saveItem() {
        if (ApplicationMode.checkConnectivity(itemEditor.this)) {
            // allow save only if internet is connected
            setupProgressDialog();
            String name = itemNameEdit.getText().toString().trim();
            String description = itemDescriptionEdit.getText().toString().trim();
            String price = itemPriceEdit.getText().toString().trim();

            if (!validateEditFields(name, description, price)) {
                return;
            }

            item = new Item(name, description, price, ItemInfo.itemCategory);

            if (mode) {
                //edit item
                progressDialog.setTitle("Updating...");
                progressDialog.show();
                StorageReference newReference = mStorageReference.child(ItemInfo.itemId);
                //selectedImageUri shows whether a new image has been updated or not
                if (selectedImageUri == null) {
                    // if no new image is selected simply write to database
                    item.setImageUrl(ItemInfo.imageUrl);
                    Map<String, Object> newItem = item.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + ItemInfo.itemId, newItem);
                    mReference.child(ItemInfo.itemCategory).updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                deleteImageFromStorage();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error updating", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    // else upload image besides writing
                    newReference.putFile(selectedImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> urlTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    //only allows code to progress further when image has been uploaded completely and URL is retrieved
                                    Uri downloadUrl = urlTask.getResult();
                                    item.setImageUrl(downloadUrl.toString());
                                    Map<String, Object> newItem = item.toMap();
                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("/" + ItemInfo.itemId, newItem);
                                    mReference.child(ItemInfo.itemCategory).updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error updating", Toast.LENGTH_SHORT).show();
                                            }
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    });
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Number to display in progress dialog
                            float progress = ((100 * (taskSnapshot.getBytesTransferred())) / taskSnapshot.getTotalByteCount());
                            int i = Math.round(progress);
                            progressDialog.setProgress(i);
                        }
                    });
                }
            } else {
                //new item
                progressDialog.setTitle("Creating a new Item...");
                progressDialog.show();
                final String key = mReference.child(ItemInfo.itemCategory).push().getKey();
                StorageReference newReference = mStorageReference.child(key);
                if (selectedImageUri == null) {
                    mReference.child(ItemInfo.itemCategory).child(key).setValue(item, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(), "New item added Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error inserting new item", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    // upload image if it has been given
                    newReference.putFile(selectedImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> urlTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    Uri downloadUrl = urlTask.getResult();
                                    item.setImageUrl(downloadUrl.toString());
                                    mReference.child(ItemInfo.itemCategory).child(key).setValue(item, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            if (databaseError == null) {
                                                Toast.makeText(getApplicationContext(), "New item added Successfully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error inserting new item", Toast.LENGTH_SHORT).show();
                                            }
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    });

                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // number to display in progress dialog
                            float progress = ((100 * (taskSnapshot.getBytesTransferred())) / taskSnapshot.getTotalByteCount());
                            int i = Math.round(progress);
                            progressDialog.setProgress(i);
                        }
                    });

                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteItem() {
        if (ApplicationMode.checkConnectivity(itemEditor.this)) {
            // only allow deletion if internet is connected, disallow cached writes
            if (selectedImageUri == null) {
                mReference.child(ItemInfo.itemCategory).child(ItemInfo.itemId).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Deleting", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // delete image if item has it
                mStorageReference.child(ItemInfo.itemId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mReference.child(ItemInfo.itemCategory).child(ItemInfo.itemId).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error Deleting", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error deleting", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            Toast.makeText(getApplicationContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void removePicture() {
        // removes picture from UI and nullifies its reference
        itemImageEdit.setImageResource(R.drawable.no_image);
        ItemInfo.imageUrl = null;
        selectedImageUri = null;
    }

    private void deleteImageFromStorage() {
        mStorageReference.child(ItemInfo.itemId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "image removed from database");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.getData();
            Glide.with(itemImageEdit.getContext())
                    .load(selectedImageUri)
                    .into(itemImageEdit);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!mode) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (ApplicationMode.currentMode.equals("owner"))
                    saveItem();
                else
                    Toast.makeText(getApplicationContext(), "You aren't authorized for this", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete:
                if (ApplicationMode.currentMode.equals("owner")) {
                    deleteItem();
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "You aren't authorized for this", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

