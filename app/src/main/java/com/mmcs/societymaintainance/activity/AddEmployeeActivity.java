package com.mmcs.societymaintainance.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mmcs.societymaintainance.R;
import com.mmcs.societymaintainance.adaptor.DesignationAdapter;
import com.mmcs.societymaintainance.adaptor.PlaceArrayAdapter;
import com.mmcs.societymaintainance.model.DesignationModel;
import com.mmcs.societymaintainance.model.DesignationRestMeta;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.LoginResMeta;
import com.mmcs.societymaintainance.util.Shprefrences;
import com.mmcs.societymaintainance.util.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, SearchView.OnQueryTextListener {
    EditText edt_joining_date, edt_ending_date, edt_name, edt_email, edt_mobile, edt_designation, edt_password, edt_national_id;
    Calendar calendar;
    int DD, MM, YY;
    ImageView imageView;
    LoginModel loginModel;
    Button btn_take_photo, btn_save;
    private static final int CAMERA_REQUEST = 1888;
    static final int DATE_DIALOG_ID = 1;
    static final int DATE_DIALOG_ID2 = 2;
    int cur = 0;
    ProgressBar progress;
    public static String imgUrl;
    final int MY_PERMISSIONS_REQUEST_WRITE = 103;
    private static final int SELECT_PHOTO = 200;
    Shprefrences sh;
    Spinner spnIDType;
    String curr_date;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView edt_present_address, edt_permanent_address;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "AddMemberActivity";
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        edt_joining_date = findViewById(R.id.edt_joining_date);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        sh = new Shprefrences(this);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_password = findViewById(R.id.edt_password);
        edt_present_address = findViewById(R.id.edt_present_address);
        edt_permanent_address = findViewById(R.id.edt_permanent_address);
        edt_national_id = findViewById(R.id.edt_national_id);
        edt_designation = findViewById(R.id.edt_designation);
        progress = findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        edt_ending_date = findViewById(R.id.edt_ending_date);
        btn_save = findViewById(R.id.btn_save);
        imageView = findViewById(R.id.imageView);
        spnIDType = findViewById(R.id.spnIDType);
        btn_take_photo = findViewById(R.id.btn_take_photo);
        calendar = Calendar.getInstance();
        DD = calendar.get(Calendar.DAY_OF_MONTH);
        loginModel = sh.getLoginModel(getResources().getString(R.string.login_model));
        MM = calendar.get(Calendar.MONTH);
        YY = calendar.get(Calendar.YEAR);
        mGoogleApiClient = new GoogleApiClient.Builder(AddEmployeeActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        edt_present_address.setOnItemClickListener(mAutocompleteClickListener);
        edt_permanent_address.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(
                this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        edt_present_address.setAdapter(mPlaceArrayAdapter);
        edt_present_address.setThreshold(1);
        edt_permanent_address.setAdapter(mPlaceArrayAdapter);
        edt_permanent_address.setThreshold(1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE);
        }
        if ((MM + 1) < 10)
            curr_date = (String.valueOf(YY) + "-0" + String.valueOf(MM + 1) + "-" + String.valueOf(DD));
        else
            curr_date = (String.valueOf(YY) + "-" + String.valueOf(MM + 1) + "-" + String.valueOf(DD));
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_name.getText().toString();
                String email = edt_email.getText().toString();
                String mobile = edt_mobile.getText().toString();
                String password = edt_password.getText().toString();
                String present_add = edt_present_address.getText().toString();
                String permanent_add = edt_permanent_address.getText().toString();
                String national_id = edt_national_id.getText().toString();
                //  String designation = edt_designation.getText().toString();
                String joining_date = edt_joining_date.getText().toString();
                String ending_date = edt_ending_date.getText().toString();
                if (name.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mobile.trim().isEmpty() || mobile.length() < 10 || mobile.length() > 12) {
                    Toasty.error(AddEmployeeActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (present_add.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Enter Present Address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (permanent_add.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Enter Permanent Address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (spnIDType.getSelectedItemPosition() == 0) {
                    Toasty.error(AddEmployeeActivity.this, "Select ID Type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (national_id.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Enter National Id", Toast.LENGTH_SHORT).show();
                    return;
                } else if (DesiId.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Select Designation", Toast.LENGTH_SHORT).show();
                    return;
                } else if (joining_date.equals("")) {
                    Toasty.error(AddEmployeeActivity.this, "Select Joining Date", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progress.setVisibility(View.VISIBLE);
                    postEmployee(loginModel.getId(), loginModel.getType(), loginModel.getBranch_id(), name, email, mobile, present_add, permanent_add, national_id, joining_date, password, imageImagePath);
                }
            }
        });
        edt_designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDesignationPopup();
            }
        });
        edt_joining_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        edt_ending_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG_ID2);
            }
        });
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        getDesignationList(loginModel.getId(), loginModel.getType(), loginModel.getBranch_id());
        setTitle();
        back();

    }

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.add_employee));
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddEmployeeActivity.this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_photo))) {
                    /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
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

                Bitmap b = decodeUri(fileUri);
                imageView.setImageBitmap(b);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    imageView.setImageURI(selectedImage);
                    imageImagePath = getPath(selectedImage);
                    File file = new File(imageImagePath);
                    resize(file, "");

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
                w = bitmap.getWidth() * 30 / 100;
                h = bitmap.getHeight() * 30 / 100;
            }

            Log.e("width & Height", "width " + w + " height " + h);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);

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


    AlertDialog alertDialog;
    ArrayList<DesignationModel> designationModels;
    DesignationAdapter designationAdapter;
    String DesiId = "";
    private int popupId = 0;

    private void showDesignationPopup() {

        designationAdapter = new DesignationAdapter(AddEmployeeActivity.this, designationModels);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.meeting_popup, null);
        final ListView listDesign = dialogView.findViewById(R.id.listFloor);
        TextView title = dialogView.findViewById(R.id.title);
        final SearchView editTextName = dialogView.findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);
        title.setText(getString(R.string.select_Designation));
        //Button btnUpgrade = (Button) dialogView.findViewById(R.id.btnUpgrade);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        popupId = 1;
        alertDialog.show();
        listDesign.setAdapter(designationAdapter);

        listDesign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DesignationModel obj = (DesignationModel) listDesign.getAdapter().getItem(position);
                edt_designation.setText(obj.getDesignation());
                DesiId = obj.getId();
                alertDialog.dismiss();
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                System.out.println("onCreateDialog  : " + id);
                cur = DATE_DIALOG_ID;
                return new DatePickerDialog(this, onDateSetListener, YY, MM, DD);
            case DATE_DIALOG_ID2:
                cur = DATE_DIALOG_ID2;
                System.out.println("onCreateDialog2  : " + id);
                return new DatePickerDialog(this, onDateSetListener, YY, MM, DD);

        }

        return null;
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int d, int m, int y) {
            if (cur == DATE_DIALOG_ID) {
                // set selected date into textview
                if ((m + 1) < 10)
                    edt_joining_date.setText(String.valueOf(d) + "-0" + String.valueOf(m + 1) + "-" + String.valueOf(y));
                else
                    edt_joining_date.setText(String.valueOf(d) + "-" + String.valueOf(m + 1) + "-" + String.valueOf(y));

            } else {
                if ((m + 1) < 10)
                    edt_ending_date.setText(String.valueOf(d) + "-0" + String.valueOf(m + 1) + "-" + String.valueOf(y));
                else
                    edt_ending_date.setText(String.valueOf(d) + "-" + String.valueOf(m + 1) + "-" + String.valueOf(y));
            }
        }
    };


    public void getDesignationList(String userid, String type, String branchid) {

        Singleton.getInstance().getApi().getDesignationList(userid, type, branchid).enqueue(new Callback<DesignationRestMeta>() {
            @Override
            public void onResponse(Call<DesignationRestMeta> call, Response<DesignationRestMeta> response) {
                designationModels = response.body().getResponse();
                for (int i = 0; i < designationModels.size(); i++) {
                    if (designationModels.get(i).getDesignation().equalsIgnoreCase("Driver") || designationModels.get(i).getDesignation().equalsIgnoreCase("Maid"))
                        designationModels.remove(i);
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DesignationRestMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        s = s.toLowerCase();
        switch (popupId) {
            case 1:
                ArrayList<DesignationModel> newlist = new ArrayList<>();
                for (DesignationModel list : designationModels) {
                    String getPurpose = list.getDesignation().toLowerCase();

                    if (getPurpose.contains(s)) {
                        newlist.add(list);
                    }
                }
                designationAdapter.filter(newlist);
                break;

        }
        return false;
    }

    private void postEmployee(String userid, String type, String branchid, String name, String email, String mobile, String preaddress, String peradress, String national_id, String date, String password, String fileUrl) {
        LoginModel model = sh.getLoginModel(getString(R.string.login_model));
        RequestBody imgFile = null;
        File imagPh = new File(fileUrl);
        Log.e("****image*******", "*****imagepath********" + fileUrl);
        if (imagPh != null && (fileUrl != null && !fileUrl.equalsIgnoreCase("")))
            imgFile = RequestBody.create(MediaType.parse("image/*"), imagPh);
        RequestBody requestUserId = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody requestUserbranch = RequestBody.create(MediaType.parse("text/plain"), "" + branchid);
        RequestBody requesttype = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody requesttxtName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody requestJoiningDate = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody requestEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody requestMobile = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody requestpreAddress = RequestBody.create(MediaType.parse("text/plain"), preaddress);
        RequestBody requestper_Address = RequestBody.create(MediaType.parse("text/plain"), peradress);
        RequestBody requestIDType = RequestBody.create(MediaType.parse("text/plain"), spnIDType.getSelectedItem() + "");
        RequestBody requestNational_id = RequestBody.create(MediaType.parse("text/plain"), national_id);
        RequestBody requestpassword = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody requestcurrentdate = RequestBody.create(MediaType.parse("text/plain"), curr_date);
        RequestBody requestdesign = RequestBody.create(MediaType.parse("text/plain"), DesiId);
        RequestBody requestStatus = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody requestEndingDate = RequestBody.create(MediaType.parse("text/plain"), "");


        Singleton.getInstance().getApi().postEmployee(requestUserId, requesttype, requestUserbranch, requesttxtName, requestEmail, requestMobile, requestpreAddress,
                requestper_Address, requestIDType, requestNational_id, requestdesign, requestJoiningDate, requestEndingDate, requestpassword, requestStatus, requestcurrentdate, imgFile).enqueue(new Callback<LoginResMeta>() {
            @Override
            public void onResponse(Call<LoginResMeta> call, Response<LoginResMeta> response) {
                Toasty.success(AddEmployeeActivity.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toasty.error(AddEmployeeActivity.this, "Sorry Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            //Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            Toast.makeText(AddEmployeeActivity.this, place.getAddress(), Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }


}
