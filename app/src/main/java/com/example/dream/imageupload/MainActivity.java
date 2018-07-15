package com.example.dream.imageupload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dream.imageupload.Interfacee.RestClient;
import com.example.dream.imageupload.Model.Example;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class MainActivity extends AppCompatActivity {
    Uri selectedFileUri = null;
    String type;
    private static final int PICK_FILE_REQUEST = 1;
    private String selectedFilePath="";
    String from;
    ProgressDialog progressDialog;
    private String authenticate_key = "6391699ACC4A";
    private int user_id = 63;
    ImageView userPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteCache(this);

        userPic = (ImageView) findViewById(R.id.imageView_userImage);

        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFileUri = null;
                selectedFilePath="";
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FILE_REQUEST);
            }
        });
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data == null) {
                    //no data present
                    return;
                }
                 selectedFileUri = data.getData();

                //Log.i(TAG,"Selected File Path:" + selectedFilePath);
                selectedFilePath = FilePath.getPath(this, selectedFileUri);
                type = "image";

            }


            if (selectedFilePath != null && !selectedFilePath.equals("")) {
                //tv_file_name.setText(selectedFilePath);
                //Picasso.with(MainActivity.this).load(selectedFileUri).into(userPic);
                uploadUserImage();
            } else {
                Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadUserImage() {

        if (selectedFilePath != null) {

          //  userPic.setImageBitmap(null);
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {


                    TypedFile file = new TypedFile("multipart/form-data", new File(selectedFilePath));


                    new RestClient(getApplicationContext()).getDataService().uploadImage(user_id ,
                            file , authenticate_key ,new Callback<Example>() {

                                @Override
                                public void success(Example suggetionResultBean, Response response) {
                                    progressDialog.dismiss();
                                    if (suggetionResultBean.getStatus().equalsIgnoreCase("success")) {


                                        Log.e("image.........................",suggetionResultBean.getImage());

                                        Toast.makeText(getApplicationContext(), "Picture Successfully Uploaded" , Toast.LENGTH_SHORT).show();

                                        Picasso.with(MainActivity.this).load(selectedFileUri).into(userPic);

                                    }

                                    else if (suggetionResultBean.getStatus().equalsIgnoreCase("unsuccess"))

                                        Toast.makeText(getApplicationContext(), "Something Went Wrong" , Toast.LENGTH_SHORT).show();


                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    progressDialog.dismiss();
                                    Log.d("error", error.toString());
                                    Toast.makeText(getApplicationContext(), "Network Error occurs...Try again!", Toast.LENGTH_SHORT).show();

                                                    }

                            });
                }


            }).start();
        } else {
            Toast.makeText(MainActivity.this, "Please choose a File First", Toast.LENGTH_SHORT).show();
        }

    }
    
}
