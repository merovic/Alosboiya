package alosboiya.jeddahwave.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment {

    TextView username,userbalance;
    ImageView userimage;
    EditText usernameedit,userphoneedit,userimageedit,userpasswordedit;
    Spinner usercityedit;
    Button savebutton;

    String usercity,imageURL,user_id;

    TinyDB tinyDB;

    List<String> cities;

    final int PICK_IMAGE_REQUEST_CAMERA = 71;

    final int PICK_IMAGE_REQUEST_GALLERY = 72;

    FirebaseStorage storage;
    StorageReference storageReference;

    Uri filePath;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tinyDB = new TinyDB(getContext());

        FirebaseApp.initializeApp(getContext());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        username = Objects.requireNonNull(getActivity()).findViewById(R.id.username);
        userbalance = getActivity().findViewById(R.id.userbalance);
        userimage = getActivity().findViewById(R.id.userimage);
        usernameedit = getActivity().findViewById(R.id.usernameedit);
        userphoneedit = getActivity().findViewById(R.id.userphoneedit);
        userimageedit = getActivity().findViewById(R.id.userimageedit);
        userpasswordedit = getActivity().findViewById(R.id.userpasswordedit);
        usercityedit = getActivity().findViewById(R.id.usercityedit);
        savebutton = getActivity().findViewById(R.id.savebutton);


        usernameedit.setText(tinyDB.getString("user_name"));
        username.setText(tinyDB.getString("user_name"));
        userpasswordedit.setText(tinyDB.getString("user_pass"));
        userphoneedit.setText(tinyDB.getString("user_phone"));
        userbalance.setText(tinyDB.getString("user_balance"));

        usercity = tinyDB.getString("user_city");
        user_id = tinyDB.getString("user_id");

        setupSpinner();


        if(tinyDB.getString("user_img").equals("images/imgposting.png") || tinyDB.getString("user_img").equals(""))
        {
            Glide.with(this).load(R.drawable.user).into(userimage);

        }else if (tinyDB.getString("user_img").contains("~")) {
            String replaced = tinyDB.getString("user_img").replace("~", "");
            String finalstring = "http://alosboiya.com.sa" + replaced;
            Glide.with(this).load(finalstring).into(userimage);
            imageURL = finalstring;

        }else
        {
            Glide.with(this).load(tinyDB.getString("user_img")).into(userimage);
            imageURL = tinyDB.getString("user_img");
        }

        userimageedit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showPicturDialog();
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }

    private void saveData() {

        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/webs.asmx/edite_profile?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            tinyDB.putString("user_id",user_id);
                            tinyDB.putString("user_name",usernameedit.getText().toString());
                            tinyDB.putString("user_pass",userpasswordedit.getText().toString());
                            tinyDB.putString("user_phone",userphoneedit.getText().toString());
                            tinyDB.putString("user_city",usercityedit.getSelectedItem().toString());
                            tinyDB.putString("user_img",imageURL);

                            username.setText(usernameedit.getText().toString());

                            showMessage("تم تحديث البيانات");


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                showMessage(error.toString());

            }
        }) {

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id_member", user_id);
                params.put("city", usercityedit.getSelectedItem().toString());
                params.put("password", userpasswordedit.getText().toString());
                params.put("image", imageURL);
                params.put("phone", userphoneedit.getText().toString());
                params.put("name", usernameedit.getText().toString());
                return params;
            }



        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void  showPicturDialog()
    {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("قم بألختيار");
        String[] pictureDlialogItem={"اختر من المعرض" ,
                "قم بألتقاط صورة"};
        pictureDialog.setItems(pictureDlialogItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0 :
                        choosePhotoFromGallary();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                }
            }
        });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,PICK_IMAGE_REQUEST_GALLERY);
    }

    private void takePhotoFromCamera() {

        //From Camera
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivityForResult(pictureIntent, PICK_IMAGE_REQUEST_CAMERA);
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST_CAMERA || requestCode == PICK_IMAGE_REQUEST_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            if(requestCode == PICK_IMAGE_REQUEST_CAMERA)
            {
                Bitmap bitmap = (Bitmap)Objects.requireNonNull(data.getExtras()).get("data");
                filePath = getImageUri(getContext(),bitmap);

                userimage.setImageBitmap(bitmap);

                uploadImage(filePath);

            }else {

                filePath = data.getData();

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), filePath);
                    userimage.setImageBitmap(bitmap);

                    uploadImage(filePath);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }




    private void uploadImage(Uri customfilepath) {

        if(customfilepath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("جارى الرفع٠٠٠");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(customfilepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {

                                    progressDialog.dismiss();

                                    showMessage("تم الرفع بنجاح");

                                    imageURL = uri.toString();

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            showMessage("فشل");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }


    private void showMessage(String _s) {
        Toast.makeText(getContext(), _s, Toast.LENGTH_LONG).show();
    }


    public void setupSpinner()
    {
        cities = new ArrayList<>();

        cities.add("الرياض");
        cities.add("مكة المكرمة");
        cities.add("الدمام");
        cities.add("جده");
        cities.add("المدينة المنورة");
        cities.add("الأحساء");
        cities.add("الطائف");
        cities.add("بريدة");
        cities.add("تبوك");
        cities.add("القطيف");
        cities.add("خميس مشيط");
        cities.add("حائل");
        cities.add("حفر الباطن");
        cities.add("الجبيل");
        cities.add("الخرج");
        cities.add("أبها");
        cities.add("نجران");
        cities.add("ينبع");
        cities.add("القنفذة");
        cities.add("جازان");
        cities.add("القصيم");
        cities.add("عسير");

        usercityedit = getActivity().findViewById(R.id.usercityedit);
        ArrayAdapter<String> elmadenaAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, cities);
        elmadenaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usercityedit.setAdapter(elmadenaAdapter);

        usercityedit.setSelection(cities.indexOf(usercity));

    }
}
