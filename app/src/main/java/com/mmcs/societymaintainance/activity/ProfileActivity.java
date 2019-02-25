package com.mmcs.societymaintainance.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.UploadImageResMeta;
import com.mmcs.societymaintainance.util.CircleTransform;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    LoginModel loginModel;
    Shprefrences sh;
    ImageView imgProfile;
    TextView text_edit;
    private static final int SELECT_PHOTO = 200;
    private static final int CAMERA_REQUEST = 1888;
    EditText edt_txt_first_name, edt_txt_last_name, edt_email_id, edt_Flate_no, edt_phone, edt_Address, edt_designation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgProfile = findViewById(R.id.imgProfile);
        edt_txt_first_name = findViewById(R.id.edt_txt_first_name);
        edt_txt_last_name = findViewById(R.id.edt_txt_last_name);
        edt_email_id = findViewById(R.id.edt_email_id);
        edt_Flate_no = findViewById(R.id.edt_Flate_no);
        edt_phone = findViewById(R.id.edt_phone);
        edt_designation = findViewById(R.id.edt_designation);
        edt_Address = findViewById(R.id.edt_Address);
        text_edit = findViewById(R.id.text_edit);
        sh = new Shprefrences(this);
        loginModel = sh.getLoginModel(getResources().getString(R.string.login_model));
        edt_txt_first_name.setText(loginModel.getName().split(" ")[0]);
        if (loginModel.getName().split(" ").length > 1)
            edt_txt_last_name.setText(loginModel.getName().split(" ")[1]);
        edt_email_id.setText(loginModel.getEmail());

        if (loginModel.getUnit()==null)
            edt_Flate_no.setVisibility(View.GONE);
        else
            edt_Flate_no.setText(loginModel.getUnit());
        edt_phone.setText(loginModel.getContact());
        edt_Address.setText(loginModel.getPre_address());
        edt_designation.setText(loginModel.getMember_type());
        text_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Glide.with(this).load(loginModel.getImage()).asBitmap().centerCrop().dontAnimate().placeholder(R.drawable.ic_userlogin).error(R.drawable.ic_userlogin).into(new BitmapImageViewTarget(imgProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imgProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
        if (loginModel.getType().equalsIgnoreCase("3"))
            edt_designation.setVisibility(View.VISIBLE);
        setTitle();
        back();
       Button btn_submit=findViewById(R.id.btn_submit);
       btn_submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });

    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.profile_title));
    }

    private void back() {
        RelativeLayout drawerIcon = (RelativeLayout) findViewById(R.id.drawerIcon);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Uri fileUri;

    private void selectImage() {
        final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.choose_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_photo))) {
                    String fileName = System.currentTimeMillis() + ".jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, CAMERA_REQUEST);
                } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    openGallery();
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    String imageImagePath = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                imageImagePath = getPath(fileUri);
                File file = new File(imageImagePath);
                resize(file, "");
                Picasso.get().load(fileUri).transform(new CircleTransform()).placeholder(R.drawable.ic_userlogin).into(imgProfile);
                updateUserProfile(imageImagePath);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    imageImagePath = getPath(selectedImage);
                    File file = new File(imageImagePath);
                    resize(file, "");
                    Picasso.get().load(file).transform(new CircleTransform()).placeholder(R.drawable.ic_userlogin).resize(100, 100).into(imgProfile);
                    updateUserProfile(imageImagePath);
                }
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();

        o.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(getContentResolver()
                .openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 72;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;

        int scale = 1;

        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;

            height_tmp /= 2;

            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();

        o2.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                .openInputStream(selectedImage), null, o2);

        return bitmap;
    }

    @SuppressWarnings("deprecation")
    private String getPath(Uri selectedImaeUri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(selectedImaeUri, projection, null, null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            return cursor.getString(columnIndex);
        }

        return selectedImaeUri.getPath();
    }

    BitmapFactory.Options bmOptions;
    Bitmap bitmap;

    public void resize(File file, String benchMark) {
        try {
            bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            bmOptions.inDither = true;
            bitmap = BitmapFactory.decodeFile(imageImagePath, bmOptions);
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Log.e("width & Height", "width " + bitmap.getWidth());
            if (bitmap.getWidth() > 1200) {
                w = (bitmap.getWidth() * 30) / 100;
                h = (bitmap.getHeight() * 30) / 100;
            }

            Log.e("width & Height", "width " + w + " height " + h);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);

            /*Canvas c = new Canvas(bitmap);
            Paint p = new Paint();
            p.setColor(Color.BLUE);
            p.setStyle(Paint.Style.FILL);
            p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            // paint.setColor(Color.BLACK);
            p.setTextSize(30);
            c.drawText(benchMark, 20, 40, p);*/

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);
            try {
                Log.e("Compressing", "Compressing");
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
                Log.e("Exception", "Image Resizing" + e.getMessage());
            }
        } catch (
                Exception e
                ) {
            Log.e("Exception", "Exception in resizing image");
        }
    }

    public void updateUserProfile(String fileUrl) {
        RequestBody imgFile = null;
        File imagPh = new File(fileUrl);
        Log.e("***********", "*************" + imagPh.getAbsolutePath());
        if (imagPh != null)
            imgFile = RequestBody.create(MediaType.parse("image/*"), imagPh);
        RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), loginModel.getId());
        RequestBody requesttype = RequestBody.create(MediaType.parse("text/plain"), loginModel.getType());
        RequestBody branch_id = RequestBody.create(MediaType.parse("text/plain"), loginModel.getBranch_id());
        Log.e("***********", "*************" + imagPh.getAbsolutePath());


        Singleton.getInstance().getApi().updateUserProfile(requestId, requesttype, branch_id, imgFile).enqueue(new Callback<UploadImageResMeta>() {
            @Override
            public void onResponse(Call<UploadImageResMeta> call, Response<UploadImageResMeta> response) {


                if (response.isSuccessful()) {

                    loginModel.setImage(response.body().getResponse().get(0).getImage());
                    sh.setLoginModel(getString(R.string.login_model), loginModel);
                    //Picasso.get().load(up.getImage()).transform(new CircleTransform()).placeholder(R.drawable.ic_userlogin).into(imgProfile);
                }
            }

            @Override
            public void onFailure(Call<UploadImageResMeta> call, Throwable t) {

            }
        });
    }


}
