package com.js.zolp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class ImageActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_ALBUM = 200;

    private static final String TAG = "IMAGE_ACTIVITY";

    private Context context;
    private ImageView imageView;
    private TextView textView;
    private Button cameraButton, albumButton;

    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment
                        .getExternalStorageDirectory(),
                        "test.jpg");
                outputFileUri = Uri.fromFile(file);
                Log.d("TAG", "outputFileUri intent"
                        + outputFileUri.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });


        albumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_ALBUM);

            }
        });
    }

    static {
        //System.loadLibrary();
    }

    public void initView() {
        textView = (TextView) findViewById(R.id.textView);
        cameraButton = (Button) findViewById(R.id.btn_camera);
        albumButton = (Button) findViewById(R.id.btn_album);
        imageView = (ImageView) findViewById(R.id.imageview);
    }

    /**
     * 사진의 URI 경로를 받는 메소드
     */
    public String getPath(Uri uri) {
        // uri가 null일경우 null반환
        if( uri == null ) {
            return null;
        }
        // 미디어스토어에서 유저가 선택한 사진의 URI를 받아온다.
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // URI경로를 반환한다.
        return uri.getPath();
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth,
                                             int reqHeight) {

        Bitmap bm = null;

        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        return bm;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAMERA) {
            Log.d("TAG", "outputFileUri RESULT_OK" + outputFileUri);
            if (outputFileUri != null) {

                Bitmap bitmap;
                bitmap = decodeSampledBitmapFromUri(outputFileUri,
                        imageView.getWidth(), imageView.getHeight());

                if (bitmap == null) {
                    Toast.makeText(getApplicationContext(),
                            "the image data could not be decoded",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Decoded Bitmap: " + bitmap.getWidth() + " x "
                                    + bitmap.getHeight(), Toast.LENGTH_LONG)
                            .show();
                    imageView.setImageBitmap(bitmap);
                }
                textView.setText(outputFileUri.toString());
            }
        }
        else if (requestCode == REQUEST_ALBUM) {
            Uri selectedImageUri = data.getData();
            //String selectedImagePath = getPath(selectedImageUri);
            if (selectedImageUri == null)
                return;
            else {
                textView.setText(selectedImageUri.toString());
                imageView.setImageURI(selectedImageUri);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Image load failure", Toast.LENGTH_SHORT).show();
        }
    }

}
