package com.fbasegizi.statusgizi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fbasegizi.statusgizi.fragment.AccountSetingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UbahProfil extends BaseActivity implements View.OnClickListener {
    private int STORAGE_PERMISSION_CODE = 1;
    private int WRITE_PERMISSION_CODE = 2;
    private int CAMERA_PERMISSION_CODE = 3;
    private int GALLERY = 4, CAMERA = 5;
    private String imageLocation = "";

    private Uri contentURI;
    private Bitmap bitmap;

    private EditText editText;
    private TextInputLayout textInputLayout;
    private Button buttonSave, buttonChoose;
    private DatabaseReference databaseReference;
    private ImageView imageView;
    private ProgressBar progressBar;

    private FirebaseStorage storage;
    private StorageReference storageReference, path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        path = storageReference.child("profiles").child(getUid());

        editText = findViewById(R.id.editUbahNama);
        textInputLayout = findViewById(R.id.textLayoutChangeName);
        buttonSave = findViewById(R.id.btn_change_profil);
        buttonChoose = findViewById(R.id.btn_choose);
        imageView = findViewById(R.id.imageProfile);
        progressBar = findViewById(R.id.ProgressGambar);

        buttonSave.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Ubah Profil");
        }
        imageView.setVisibility(View.INVISIBLE);

        loadImage();
    }

    private void loadImage() {
        path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try {
                    if (task.getResult() != null) {
                        String download = task.getResult().toString();
                        Picasso.get().load(download).fit().centerCrop().into(imageView);
                    }
                    imageView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            databaseReference.child("users").child(getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String nama = dataSnapshot.child("Nama").getValue(String.class);
                            editText.setText(nama);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Snackbar.make(UbahProfil.this.findViewById(android.R.id.content),
                                    "Server error, coba ulangi beberapa saat lagi!",
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void ResetProfile() {
        final String nama = editText.getText().toString();
        databaseReference.child("users").child(getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("Nama").setValue(nama.replaceFirst("\\s++$", ""));
                        Intent intent = new Intent(getBaseContext(), AccountSetingsActivity.class);
                        intent.putExtra("profile_change", "update");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(UbahProfil.this.findViewById(android.R.id.content),
                                "Server error, coba ulangi beberapa saat lagi!",
                                Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void ResetDialog() {
        String nama = editText.getText().toString();
        if (TextUtils.isEmpty(nama)) {
            textInputLayout.setError("Nama tidak boleh kosong!");
            return;
        } else if (nama.length() < 2) {
            textInputLayout.setError("Panjang nama minimal 2 karakter !");
            return;
        } else if (nama.length() > 100) {
            textInputLayout.setError("Panjang nama maksimal 100 karakter !");
            return;
        } else if (!nama.matches("[a-zA-Z ]+")) {
            textInputLayout.setError("Nama hanya dapat diisi dengan huruf alphabet");
            return;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Ubah profil");
        alertDialogBuilder
                .setMessage("Apakah Anda ingin mengubah profil?")
                .setCancelable(false)
                .setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            dialog.cancel();
                            Snackbar.make(UbahProfil.this.findViewById(android.R.id.content),
                                    "Koneksi internet tidak tersedia",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        showProgressDialog();
                        uploadFile();
                        hideProgressDialog();
                        loadImage();
                        ResetProfile();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_text));
            }
        });
        alertDialog.show();
    }

    private void uploadFile() {
        if (contentURI != null) {
            path.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideProgressDialog();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgressDialog();
                }
            });
        } else if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            byte[] bytes = stream.toByteArray();
            path.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideProgressDialog();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgressDialog();
                }
            });
        } else {
            Toast.makeText(UbahProfil.this, "Gambar tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPictureDialog() {
        final AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Pilih aksi");
        String[] pictureDialogItems = {
                "Pilih gambar dari galeri",
                "Ambil foto dari kamera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            checkPermissionStorage();
                        } else if (which == 1) {
                            checkPermissionCamera();
                        }
                    }
                });
        pictureDialog.show();
    }

    private void checkPermissionStorage() {
        if (ContextCompat.checkSelfPermission(UbahProfil.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            choosePhotoFromGallery();
        } else {
            requestStoragePermission();
        }
    }

    private void checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(UbahProfil.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(UbahProfil.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            takePhotoFromCamera();
        } else {
            requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Diperlukan izin")
                    .setMessage("Izin ini diperlukan untuk membuka kamera pada perangkat Anda")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UbahProfil.this,
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Diperlukan izin")
                    .setMessage("Izin ini diperlukan untuk menyimpan gambar pada perangkat Anda")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UbahProfil.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Diperlukan izin")
                    .setMessage("Izin ini diperlukan untuk membaca file pada perangkat Anda")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UbahProfil.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhotoFromGallery();
            } else {
                Toast.makeText(this, "Izin dibatalkan", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestWritePermission();
            } else {
                Toast.makeText(this, "Izin dibatalkan", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WRITE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhotoFromCamera();
            } else {
                Toast.makeText(this, "Izin dibatalkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentURI);
        File photofile = null;
        try {
            photofile = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);*/
            /*bitmap = BitmapFactory.decodeFile(imageLocation);
            imageView.setImageBitmap(bitmap);*/
            reducedSize();
        }
    }

    File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("DDMMyyyy_HHmmss").format(new Date());
        String imageFileName = "GAMBAR_Gizi_" + timestamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageLocation = image.getAbsolutePath();
        return image;
    }

    void reducedSize() {
        int ImgWidth = imageView.getWidth();
        int ImgHeight = imageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageLocation, bmOptions);
        int CameraWidth = bmOptions.outWidth;
        int CameraHeight = bmOptions.outHeight;

        bmOptions.inSampleSize = Math.min(CameraWidth / ImgWidth, CameraHeight / ImgHeight);
        bmOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imageLocation, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_change_profil) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {
            }
            ResetDialog();
        } else if (i == R.id.btn_choose) {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            } catch (Exception ignored) {
            }
            showPictureDialog();
        }
    }
}
