package com.cio.gidservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cio.gidservice.network.NetworkService;
import com.cio.gidservice.network.UserManager;
import com.cio.gidservice.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST = 100;

    private int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImagesUrl = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout1);

        Button button = findViewById(R.id.open_media);
        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_FROM_GALLERY_REQUEST);
        });

        //initImageBitmap();
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    /**
     * requestPermissions and do something
     *
     */
    public void requestRead(Uri uri) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            uploadFile(uri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            requestRead(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    private void uploadFile(Uri uri) {
        Retrofit retrofit = NetworkService.getRetrofitInstance();

        File originalFile = FileUtils.getFile(this, uri);
        RequestBody filePart = FormBody.create(
                MediaType.parse(getContentResolver().getType(uri)),
                originalFile
        );

        MultipartBody.Part file = MultipartBody.Part.createFormData("photo", originalFile.getName(), filePart);


        UserManager userManager = retrofit.create(UserManager.class);

        Call<ResponseBody> call = userManager.uploadImage(file);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, "Ohh, you upload your image to the server!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Oops :( Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*private void initImageBitmap() {
        Log.d(TAG, "initImageBitmap: preparing bitmaps.");

        mImagesUrl.add("https://images.unsplash.com/photo-1441786485319-5e0f0c092803?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("James John");

        mImagesUrl.add("https://images.unsplash.com/photo-1489278353717-f64c6ee8a4d2?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("James Bond");

        mImagesUrl.add("https://images.unsplash.com/photo-1422568374078-27d3842ba676?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("William Woles");

        mImagesUrl.add("https://images.unsplash.com/photo-1514846326710-096e4a8035e0?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("Bill Gates");

        mImagesUrl.add("https://images.unsplash.com/photo-1487222477894-8943e31ef7b2?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("Brave Heart");

        mImagesUrl.add("https://images.unsplash.com/photo-1502323777036-f29e3972d82f?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("Selena Gomez");

        mImagesUrl.add("https://images.unsplash.com/photo-1484972759836-b93f9ef2b293?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("Igor Voitenko");

        mImagesUrl.add("https://images.unsplash.com/photo-1500336624523-d727130c3328?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("Yurii Surzhikov");

        mImagesUrl.add("https://images.unsplash.com/photo-1485893086445-ed75865251e0?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("Dmitriy Melnik");

        mImagesUrl.add("https://images.unsplash.com/photo-1473531761844-5a14668fc8f8?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("Ann Surzhikova");
        
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycler.");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, mNames, mImagesUrl);
        recyclerView.setAdapter(viewAdapter);
    }*/
}
