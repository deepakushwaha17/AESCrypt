package com.deepa.aescrypt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageEncrypt extends AppCompatActivity {

    Button encryptBtn, decryptBtn;
    ImageView imageView;
    EditText encImgToText;
    ClipboardManager clipboardManager;

    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_CODE = 200;

    String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_encrypt);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Crypt Image");
        }

        encryptBtn = findViewById(R.id.enc_img_btn);
        decryptBtn = findViewById(R.id.dec_img_btn);
        imageView = findViewById(R.id.imageView);
        encImgToText = findViewById(R.id.encText);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        encryptBtn.setOnClickListener(v -> checkPermission());
        decryptBtn.setOnClickListener(v -> decryptImage());
    }

    // ------------------------- CHECK PERMISSION -------------------------
    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        PERMISSION_CODE);
            }
        } else {
            // Android 12 and below
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_CODE);
            }
        }
    }

    // ------------------------- PERMISSION RESULT -------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ------------------------- SELECT IMAGE -------------------------
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    // ------------------------- HANDLE RESULT -------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            encodeImage(uri);
        }
    }

    // ------------------------- ENCRYPT IMAGE -------------------------
    private void encodeImage(Uri uri) {
        try {
            Bitmap bitmap;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.Source source =
                        ImageDecoder.createSource(getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            byte[] bytes = stream.toByteArray();

            encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            encImgToText.setText(encodedImage);

            Toast.makeText(this, "Image Encrypted Successfully!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Encrypting Image!", Toast.LENGTH_SHORT).show();
        }
    }

    // ------------------------- DECRYPT IMAGE -------------------------
    private void decryptImage() {

        String base64 = encImgToText.getText().toString().trim();

        if (base64.isEmpty()) {
            Toast.makeText(this, "Encoded text is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            if (bitmap == null) {
                Toast.makeText(this, "Invalid image data!", Toast.LENGTH_SHORT).show();
                return;
            }

            imageView.setImageBitmap(bitmap);
            Toast.makeText(this, "Image Decrypted Successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error Decoding Image!", Toast.LENGTH_SHORT).show();
        }
    }

    // ------------------------- COPY TEXT -------------------------
    public void copyImgCode(View view) {
        String data = encImgToText.getText().toString().trim();

        if (!data.isEmpty()) {
            ClipData clip = ClipData.newPlainText("image", data);
            clipboardManager.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        }
    }
}
