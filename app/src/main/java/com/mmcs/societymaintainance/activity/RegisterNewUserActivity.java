package com.mmcs.societymaintainance.activity;

import android.Manifest;
import android.app.AlertDialog;
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
import com.mmcs.societymaintainance.adaptor.FloorPopupAdapter;
import com.mmcs.societymaintainance.adaptor.PlaceArrayAdapter;
import com.mmcs.societymaintainance.adaptor.UnitAdapter;
import com.mmcs.societymaintainance.model.FloorModel;
import com.mmcs.societymaintainance.model.LoginModel;
import com.mmcs.societymaintainance.model.LoginResMeta;
import com.mmcs.societymaintainance.model.ResponseMeta;
import com.mmcs.societymaintainance.model.UnitModel;
import com.mmcs.societymaintainance.model.UnitRestMeta;
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

public class RegisterNewUserActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, SearchView.OnQueryTextListener {
    int DD, MM, YY;
    Calendar calendar;
    ImageView imageView;
    Button btn_take_photo, btn_save;
    EditText edt_owner_name, edt_branch, edt_email, edt_mobile, edt_password, edt_national_id, edt_floor, edt_unit_no;
    Spinner spnUserType,spnIDType;
    private static final int CAMERA_REQUEST = 1888;
    ProgressBar progress;
    private static final int SELECT_PHOTO = 200;
    Shprefrences sh;
    LoginModel loginModel;
    final int MY_PERMISSIONS_REQUEST_WRITE = 103;
    private AutoCompleteTextView edt_present_address, edt_permanent_address;
    private GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final String TAG = "AddOwnerActivity";
    private PlaceArrayAdapter mPlaceArrayAdapter;
    String userType = "0";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newuser);
        spnUserType = findViewById(R.id.spnUserType);
        spnIDType= findViewById(R.id.spnIDType);
        imageView = findViewById(R.id.imageView);
        edt_owner_name = findViewById(R.id.edt_owner_name);
        edt_email = findViewById(R.id.edt_email);
        edt_branch = findViewById(R.id.edt_branch);
        sh = new Shprefrences(this);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_password = findViewById(R.id.edt_password);
        edt_present_address = findViewById(R.id.edt_present_address);
        edt_permanent_address = findViewById(R.id.edt_permanent_address);
        edt_national_id = findViewById(R.id.edt_national_id);
        edt_floor = findViewById(R.id.edt_floor);
        edt_unit_no = findViewById(R.id.edt_unit_no);
        progress = findViewById(R.id.progress);
        loginModel = sh.getLoginModel(getResources().getString(R.string.login_model));
        progress.setVisibility(View.VISIBLE);
        btn_take_photo = findViewById(R.id.btn_take_photo);
        btn_save = findViewById(R.id.btn_save);
        calendar = Calendar.getInstance();
        DD = calendar.get(Calendar.DAY_OF_MONTH);
        MM = calendar.get(Calendar.MONTH);
        YY = calendar.get(Calendar.YEAR);
        mGoogleApiClient = new GoogleApiClient.Builder(RegisterNewUserActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        edt_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBranchPopup();
            }
        });
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
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        edt_floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFloorPopup();
            }
        });
        edt_unit_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnitPopup();
            }
        });
        spnUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getBranchID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getFloorList("0", "3", "1");
        getUnitList("0", "1", "1");
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_owner_name.getText().toString();
                String email = edt_email.getText().toString();
                String mobile = edt_mobile.getText().toString();
                String password = edt_password.getText().toString();
                String present_add = edt_present_address.getText().toString();
                String permanent_add = edt_permanent_address.getText().toString();
                String national_id = edt_national_id.getText().toString();
                String floor = edt_floor.getText().toString();
                String unit = edt_unit_no.getText().toString();
                if (spnUserType.getSelectedItemPosition() == 0) {
                    Toasty.error(RegisterNewUserActivity.this, "Select Register Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (branchId.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Select Branch", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Enter Owner Name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mobile.trim().isEmpty() || mobile.length() < 10 || mobile.length() > 12) {
                    Toasty.error(RegisterNewUserActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (present_add.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Enter Present Address", Toast.LENGTH_SHORT).show();
                    return;
                } else if (permanent_add.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Enter Permanent Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spnIDType.getSelectedItemPosition() == 0) {
                    Toasty.error(RegisterNewUserActivity.this, "Select ID Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (national_id.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Enter National Id", Toast.LENGTH_SHORT).show();
                    return;
                }/* else if (floor.equals("")) {
                    Toasty.error(AddOwnerActivity.this, "Select Floor", Toast.LENGTH_SHORT).show();
                    return;
                }*/ else if (unit.equals("")) {
                    Toasty.error(RegisterNewUserActivity.this, "Select Unit No.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    progress.setVisibility(View.VISIBLE);

                    postOwner("", userType, branchId, name, email, mobile, present_add, permanent_add, national_id, password, imageImagePath);
                }
            }
        });
        setTitle();
        back();
    }

    private void postOwner(String userid, String type, String branchid, String txtName, String email, String txtMobile, String txtPreAddress, String txtPerAddress, String NID, String pass, String fileUrl) {
        LoginModel model = sh.getLoginModel(getString(R.string.login_model));
        RequestBody imgFile = null;
        File imagPh = new File(fileUrl);
        Log.e("***********", "*************" + fileUrl);
        if (imagPh != null && (fileUrl != null && !fileUrl.equalsIgnoreCase("")))
            imgFile = RequestBody.create(MediaType.parse("image/*"), imagPh);
        RequestBody requestUserId = RequestBody.create(MediaType.parse("text/plain"), userid);
        RequestBody requestUserbranch = RequestBody.create(MediaType.parse("text/plain"), "" + branchid);
        RequestBody requestType = RequestBody.create(MediaType.parse("text/plain"), type);
        RequestBody requesttxtName = RequestBody.create(MediaType.parse("text/plain"), txtName);
        RequestBody requestEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody requestMobile = RequestBody.create(MediaType.parse("text/plain"), txtMobile);
        RequestBody requestPreAddress = RequestBody.create(MediaType.parse("text/plain"), txtPreAddress);
        RequestBody requestPerAddress = RequestBody.create(MediaType.parse("text/plain"), txtPerAddress);
        RequestBody requestFloor = RequestBody.create(MediaType.parse("text/plain"), floorId);
        RequestBody requestUnit = RequestBody.create(MediaType.parse("text/plain"), unitId);
        RequestBody requestIDType = RequestBody.create(MediaType.parse("text/plain"), spnIDType.getSelectedItem()+"");
        RequestBody requestNational = RequestBody.create(MediaType.parse("text/plain"), NID);
        RequestBody requestPass = RequestBody.create(MediaType.parse("text/plain"), pass);

        Singleton.getInstance().getApi().postOwner(requestUserId, requestType, requestUserbranch, requesttxtName, requestEmail, requestMobile, requestPreAddress, requestPerAddress, requestIDType,requestNational, requestPass, requestFloor, requestUnit, imgFile).enqueue(new Callback<LoginResMeta>() {
            @Override
            public void onResponse(Call<LoginResMeta> call, Response<LoginResMeta> response) {
                Toasty.success(RegisterNewUserActivity.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Toasty.error(RegisterNewUserActivity.this, "Sorry Try Again", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.register));
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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RegisterNewUserActivity.this);
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


    AlertDialog alertDialog;
    ArrayList<FloorModel> floorList = new ArrayList<>();
    FloorPopupAdapter floorPopupAdapter;
    private int popupId = 0;
    String floorId;

    private void showFloorPopup() {

        floorPopupAdapter = new FloorPopupAdapter(RegisterNewUserActivity.this, floorList);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.meeting_popup, null);
        final ListView listFloor = dialogView.findViewById(R.id.listFloor);
        //Button btnUpgrade = (Button) dialogView.findViewById(R.id.btnUpgrade);
        final SearchView editTextName = dialogView.findViewById(R.id.edt);
        TextView title = dialogView.findViewById(R.id.title);
        editTextName.setQueryHint(getString(R.string.search_here));
        title.setText(getString(R.string.select_floor));
        editTextName.setOnQueryTextListener(this);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        popupId = 1;
        listFloor.setAdapter(floorPopupAdapter);

        listFloor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FloorModel obj = (FloorModel) listFloor.getAdapter().getItem(position);
                edt_floor.setText(obj.getFloor_no());
                floorId = obj.getFid();
                Log.e("fid" + obj.getFid(), "obj.getNum" + obj.getFloor_no());
                alertDialog.dismiss();
            }
        });

    }

    ArrayList<UnitModel> unitModels;
    UnitAdapter unitAdapter;
    String unitId;

    private void showUnitPopup() {

        unitAdapter = new UnitAdapter(RegisterNewUserActivity.this, unitModels);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.meeting_popup, null);
        final ListView listUnit = dialogView.findViewById(R.id.listFloor);
        TextView title = dialogView.findViewById(R.id.title);
        final SearchView editTextName = dialogView.findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);
        title.setText(getString(R.string.select_unit));
        //Button btnUpgrade = (Button) dialogView.findViewById(R.id.btnUpgrade);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        popupId = 2;
        alertDialog.show();
        listUnit.setAdapter(unitAdapter);

        listUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UnitModel obj = (UnitModel) listUnit.getAdapter().getItem(position);
                edt_unit_no.setText(obj.getFloor_no() + " " + obj.getUnit_no());
                unitId = obj.getUid();
                floorId = obj.getFid();
                Log.e("uid" + obj.getUid(), "obj.getNum" + obj.getUnit_no());
                Log.e("floorno" + obj.getUnit_no(), "floorno" + obj.getUnit_no());
                alertDialog.dismiss();
            }
        });

    }

    public void getFloorList(String userid, String type, String branchid) {
        Singleton.getInstance().getApi().getFloorList(userid, type, branchid).enqueue(new Callback<ResponseMeta>() {
            @Override
            public void onResponse(Call<ResponseMeta> call, Response<ResponseMeta> response) {
                if (response != null && response.body() != null) {
                    floorList = response.body().getResponse();
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);

            }
        });
    }

    public void getUnitList(String userid, String type, String branchid) {

        Singleton.getInstance().getApi().getUnitList(userid, type, branchid).enqueue(new Callback<UnitRestMeta>() {
            @Override
            public void onResponse(Call<UnitRestMeta> call, Response<UnitRestMeta> response) {
                unitModels = response.body().getResponse();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UnitRestMeta> call, Throwable t) {
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

            case 2:
                ArrayList<UnitModel> newlist1 = new ArrayList<>();
                for (UnitModel list : unitModels) {
                    String unit = list.getUnit_no().toLowerCase();
                    String fl = list.getFloor_no().toLowerCase();
                    String unitf=unit+" "+fl;
                    s = s.replaceAll("\\s+", "");
                    unit = unit.replaceAll("\\s+", "");
                    fl = fl.replaceAll("\\s+", "");
                    unitf = unitf.replaceAll("\\s+", "");

                    if (unit.contains(s) || fl.contains(s) || unitf.contains(s)) {
                        newlist1.add(list);
                    }
                }
                unitAdapter.filter(newlist1);
                break;
        }
        return false;
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
            Toast.makeText(RegisterNewUserActivity.this, place.getAddress(), Toast.LENGTH_SHORT).show();

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

    ArrayList<UnitModel> branchModels;
    UnitAdapter branchAdapter;
    String branchId="1";

    private void getBranchID() {
        if (spnUserType.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select Register Type", Toast.LENGTH_SHORT).show();
            return;
        }
        userType = spnUserType.getSelectedItemPosition()+"";

        Singleton.getInstance().getApi().getBranchId("0000" + spnUserType.getSelectedItemPosition()).enqueue(new Callback<UnitRestMeta>() {
            @Override
            public void onResponse(Call<UnitRestMeta> call, Response<UnitRestMeta> response) {
                branchModels = response.body().getResponse();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UnitRestMeta> call, Throwable t) {
                progress.setVisibility(View.GONE);

            }
        });
    }


    private void showBranchPopup() {

        branchAdapter = new UnitAdapter(RegisterNewUserActivity.this, branchModels);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.meeting_popup, null);
        final ListView listUnit = dialogView.findViewById(R.id.listFloor);
        TextView title = dialogView.findViewById(R.id.title);
        final SearchView editTextName = dialogView.findViewById(R.id.edt);
        editTextName.setQueryHint(getString(R.string.search_here));
        editTextName.setOnQueryTextListener(this);
        title.setText(R.string.select_branch);
        //Button btnUpgrade = (Button) dialogView.findViewById(R.id.btnUpgrade);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        popupId = 2;
        alertDialog.show();
        listUnit.setAdapter(branchAdapter);

        listUnit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UnitModel obj = (UnitModel) listUnit.getAdapter().getItem(position);
                edt_branch.setText(obj.getFloor_no() + " " + obj.getUnit_no());
                branchId = obj.getFloor_no();
                alertDialog.dismiss();
            }
        });

    }


}