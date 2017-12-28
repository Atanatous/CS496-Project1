package com.example.user.project1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {

    private Button btnTEST;
    GridView gridView;
    ArrayList<String> imageList;
    public static List<Bitmap> bitmapList = new ArrayList<>();

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        Activity thisActivity = getActivity();
        int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
        Log.d("Fragment2", "Starting log");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(thisActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        imageList = getAllShownImagesPath(getActivity());

        //Convery uri to bitmap images
        for(int i = 0; i < imageList.size(); i++) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), Uri.fromFile(new File(imageList.get(i))));
                Log.d("Fragment2","Bitmap "+i+" added");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("Fragment2","Bitmap "+i+" EXCEPTION");
            }
            bitmapList.add(bitmap);
        }
        gridView = view.findViewById(R.id.gridview);
        gridView.setAdapter(new GridViewAdapter(this.getActivity(), bitmapList));
        Log.d("Fragment2","Adapter set");

        Log.d("Fragment2","Finished conclicklistener");




        /*bitmapList = GetBitmapImages (imageList);
        bitmapList.Adapter = new GridViewAdapter(this, bitmapList);
        for (int i = 0; i<imageList.size(); i++) {
            Log.d("Fragment2", i+"'rd imageList: "+imageList.get(i));
        }
        Log.d("Fragment2", "There were a total of "+imageList.size()+"images on this phone");
        ImageView imageView = (image)
        Uri imgUri = Uri.parse(imageList.get(0));
        imageView.setImageURI(null);
        imageView.setImageURI(imgUri);*/

        return view;
    }

    public ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while(cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;

    }




}
