package com.oryza.whatsappstatussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Picture extends AppCompatActivity {

    ImageView mparticularimage,download,mychatapp,share;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        getSupportActionBar().setTitle("Picture");

        mparticularimage = findViewById(R.id.particularimage);
        share = findViewById(R.id.share);
        download = findViewById(R.id.download);
//        mychatapp = findViewById(R.id.mychatapp);








        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Share is clicked",Toast.LENGTH_SHORT).show();
                    image();
            }
        });

        Intent intent = getIntent();
        String destpath = intent.getStringExtra("DEST_PATH");
        String file = intent.getStringExtra("FILE");
        String uri = intent.getStringExtra("URI");
        String filename = intent.getStringExtra("FILENAME");


        File destpath2 = new File(destpath);
        File file1 = new File(file);

        Glide.with(getApplicationContext()).load(uri).into(mparticularimage);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                   org.apache.commons.io.FileUtils.copyFileToDirectory(file1,destpath2);
                }catch (IOException e){

                    e.printStackTrace();


                }

                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{destpath + filename},
                        new String[]{"*/*"},
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {

                            }

                            @Override
                            public void onScanCompleted(String path, Uri uri) {

                            }
                        });


                Dialog dialog = new Dialog(Picture.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.show();
                Button button = dialog.findViewById(R.id.okbutton);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                });




            }
        });




    }

        private void image(){

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            BitmapDrawable drawable = (BitmapDrawable)mparticularimage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            File f = new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");
            Intent shareint;
            try {

                FileOutputStream outputStream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

                outputStream.flush();
                outputStream.close();

                shareint= new Intent(Intent.ACTION_SEND);
                shareint.setType("image/*");
                shareint.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(f));
                shareint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            }catch (Exception e){
                throw new RuntimeException(e);
            }

            startActivity(Intent.createChooser(shareint,"Share image"));

        }



}