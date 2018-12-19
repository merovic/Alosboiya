package alosboiya.jeddahwave.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import alosboiya.jeddahwave.Activities.AddPostActivity;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.PathUtil;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

import static android.app.Activity.RESULT_OK;

public class AddPostFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    public static final String TAG = "ass3";

    private static final String[] elmadena = {"الرياض" ,"مكة المكرمة" ,  "الدمام",
            "المدينة المنورة","جده","الأحساء","الطائف","بريدة","تبوك","القطيف","خميس مشيط","حائل","حفر الباطن","الجبيل","الخرج","أبها","نجران","ينبع","القنفذة","جازان","القصيم","عسير"};

    List<String> categories = new ArrayList<>();
    List<String> subCategories = new ArrayList<>();

    ArrayAdapter<String> adapter1,adapter2;

    Spinner add_madena,add_department,add_sub;

    TinyDB tinyDB;

    final int PICK_IMAGE_REQUEST_CAMERA = 71;

    final int PICK_IMAGE_REQUEST_GALLERY = 72;

    final int SELECT_VIDEO = 1;

    FirebaseStorage storage;
    StorageReference storageReference;

    Button add_post;

    String user_id,GET_JSON_DATA_HTTP_URL,post_department,post_sub;

    EditText title,pic,phone,desc;

    ImageView profilePic;

    TextView userName,userBalance;

    String pic1,pic2,pic3,pic4,pic5,pic6,pic7,pic8;

    Uri filePath;

    Toolbar toolbar;

    Uri selectedVideoPath;

    public AddPostFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_add_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        tinyDB = new TinyDB(getActivity());

        user_id = tinyDB.getString("user_id");

        FirebaseApp.initializeApp(getActivity());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePic = getActivity().findViewById(R.id.profile_image);
        userName = getActivity().findViewById(R.id.username);
        userBalance = getActivity().findViewById(R.id.userbalance);

        userName.setText(tinyDB.getString("user_name"));
        userBalance.setText(tinyDB.getString("user_balance"));


        if(tinyDB.getString("user_img").equals("images/imgposting.png")  || tinyDB.getString("user_img").equals(""))
        {
            Glide.with(this).load(R.drawable.user).into(profilePic);

        }else if (tinyDB.getString("user_img").contains("~")) {
            String replaced = tinyDB.getString("user_img").replace("~", "");
            String finalstring = "http://alosboiya.com.sa" + replaced;
            Glide.with(this).load(finalstring).into(profilePic);

        }else
        {
            Glide.with(this).load(tinyDB.getString("user_img")).into(profilePic);
        }

        title = getActivity().findViewById(R.id.add_title);
        pic = getActivity().findViewById(R.id.add_imgs);
        phone = getActivity().findViewById(R.id.add_phone);
        desc = getActivity().findViewById(R.id.add_desc);

        pic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showPicturDialog();
            }
        });

        add_post = getActivity().findViewById(R.id.add_post);
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyConnection();
            }
        });

        categories.add("القسم");
        categories.add("سيارات");
        categories.add("عقارات");
        categories.add("الاجهزه");
        categories.add("مواشى وحيوانات وطيور");
        categories.add("اثاث");
        categories.add("الاسرة المنتجة");
        categories.add("قسم غير مصنف");
        categories.add("الخدمات");


        elmadenaSpinner();
        categoriesSpinner();
        subcategoriesSpinner();

        pic1 = "images/imgposting.png";
        pic2 = "images/imgposting.png";
        pic3 = "images/imgposting.png";
        pic4 = "images/imgposting.png";
        pic5 = "images/imgposting.png";
        pic6 = "images/imgposting.png";
        pic7 = "images/imgposting.png";
        pic8 = "images/imgposting.png";

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void  showPicturDialog()
    {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("قم بألختيار");
        String[] pictureDlialogItem={"اختر من المعرض" ,
                "اختر فيديو","قم بألتقاط صورة"};
        pictureDialog.setItems(pictureDlialogItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0 :
                        choosePhotoFromGallary();
                        break;
                    case 1:
                        chooseVideo();
                        break;
                    case 2:
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

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(Objects.requireNonNull(getContext()).getPackageManager()) != null) {
            startActivityForResult(pictureIntent, PICK_IMAGE_REQUEST_CAMERA);
        }

    }

    @SuppressLint("IntentReset")
    public void chooseVideo()
    {
        @SuppressLint("IntentReset") Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.putExtra(MediaStore.EXTRA_DURATION_LIMIT,3);
        startActivityForResult(i, SELECT_VIDEO);
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
                try {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    filePath = getImageUri(getContext(),bitmap);

                    if(pic1.equals("images/imgposting.png") && pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار عدد ١ صورة");
                        uploadImage(filePath);
                    }else if(!pic1.equals("images/imgposting.png") && pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار عدد ٢ صورة");
                        uploadImage(filePath);
                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار عدد ٣ صورة");
                        uploadImage(filePath);
                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار عدد ٤ صورة");
                        uploadImage(filePath);
                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار عدد ٥ صورة");
                        uploadImage(filePath);
                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار عدد ٦ صورة");
                        uploadImage(filePath);
                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && !pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار عدد ٧ صورة");
                        uploadImage(filePath);
                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && !pic6.equals("images/imgposting.png") && !pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                    {
                        pic.setText("تم اختيار الحد الأقصى من الصور");
                        pic.setEnabled(false);
                        uploadImage(filePath);
                    }

                }catch (Exception e)
                {
                    pic.setText("");
                }

            }else {
                filePath = data.getData();

                if(pic1.equals("images/imgposting.png") && pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار عدد ١ صورة");
                    uploadImage(filePath);
                }else if(!pic1.equals("images/imgposting.png") && pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار عدد ٢ صورة");
                    uploadImage(filePath);
                }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار عدد ٣ صورة");
                    uploadImage(filePath);
                }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار عدد ٤ صورة");
                    uploadImage(filePath);
                }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار عدد ٥ صورة");
                    uploadImage(filePath);
                }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار عدد ٦ صورة");
                    uploadImage(filePath);
                }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && !pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار عدد ٧ صورة");
                    uploadImage(filePath);
                }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && !pic6.equals("images/imgposting.png") && !pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                {
                    pic.setText("تم اختيار الحد الأقصى من الصور");
                    pic.setEnabled(false);
                    uploadImage(filePath);
                }
            }

        }else if(requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data!= null && data.getData() != null)
        {
            pic.setText("تم اختيار فيديو");
            pic.setEnabled(false);
            selectedVideoPath = data.getData();


            try {
                String filePath = PathUtil.getPath(getContext(),selectedVideoPath);
                MediaPlayer mp = MediaPlayer.create(getContext(), Uri.parse(filePath));
                int duration = mp.getDuration();

                if(duration>31)
                {
                    uploadVideo(selectedVideoPath);
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                    Uri ass = getImageUri(getContext(),bMap);
                    uploadThumb(ass);
                }else
                {
                    showMessage("الفيديو اطول من ٣٠ ثانية");
                }

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
    }




    private void uploadImage(Uri customfilepath) {

        if(customfilepath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("جارى الرفع٠٠٠");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

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

                                    if(pic1.equals("images/imgposting.png") && pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic1 = uri.toString();
                                        //showMessage(pic1);
                                    }else if(!pic1.equals("images/imgposting.png") && pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic2 = uri.toString();
                                        //showMessage(pic2);
                                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic3 = uri.toString();
                                        //showMessage(pic3);
                                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic4 = uri.toString();
                                        //showMessage(pic4);
                                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic5 = uri.toString();
                                        //showMessage(pic5);
                                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic6 = uri.toString();
                                        //showMessage(pic6);
                                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && !pic6.equals("images/imgposting.png") && pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic7 = uri.toString();
                                        //showMessage(pic7);
                                    }else if(!pic1.equals("images/imgposting.png") && !pic2.equals("images/imgposting.png") && !pic3.equals("images/imgposting.png") && !pic4.equals("images/imgposting.png") && !pic5.equals("images/imgposting.png") && !pic6.equals("images/imgposting.png") && !pic7.equals("images/imgposting.png") && pic8.equals("images/imgposting.png"))
                                    {
                                        pic8 = uri.toString();
                                        //showMessage(pic8);
                                    }


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


    private void uploadThumb(Uri customfilepath) {

        if(customfilepath != null)
        {

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(customfilepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {

                                    pic1 = uri.toString();

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }


    private void uploadVideo(Uri video)
    {
        if(video != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            final StorageReference ref = storageReference.child("videos/"+ UUID.randomUUID().toString());
            ref.putFile(video)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {

                                    progressDialog.dismiss();

                                    showMessage("تم الرفع بنجاح");

                                    pic2 = uri.toString();
                                    pic3 = "images/imgposting.png";
                                    pic4 = "images/imgposting.png";
                                    pic5 = "images/imgposting.png";
                                    pic6 = "images/imgposting.png";
                                    pic7 = "images/imgposting.png";
                                    pic8 = "images/imgposting.png";
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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


/////////////////////////////////////////////////////////////////////////////////////////////////////

    public void volleyConnection()
    {
        GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/webs.asmx/add_post?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        showMessage(response);

                        volleyConnection2();

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
                params.put("title", title.getText().toString());
                params.put("des", desc.getText().toString());
                params.put("ciiiiiiiiiiiiiiiiiiiiiiity", add_madena.getSelectedItem().toString());
                params.put("category", post_department);
                params.put("price", "");
                params.put("tel", getUSNumber(phone.getText().toString()));
                params.put("sub", post_sub);
                params.put("x", pic1);
                params.put("x_2", pic2);
                params.put("x_3", pic3);
                params.put("x_4", pic4);
                params.put("x_5", pic5);
                params.put("x_6", pic6);
                params.put("x_7", pic7);
                params.put("x_8", pic8);
                return params;
            }



        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    //--------------------------------------------------

    private void volleyConnection2()
    {
        GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/webs.asmx/login?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (Objects.equals(response, "False"))
                        {

                            showMessage("Invalid email or password ");

                        }
                        else
                        {

                            try {

                                JSONArray js = new JSONArray(response);

                                JSONObject userdate = js.getJSONObject(0);

                                String user_balance =  userdate.getString("Balance");

                                tinyDB.putString("user_balance",user_balance);

                                userBalance.setText(user_balance);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                showMessage(e.getMessage());
                            }


                        }
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
                params.put("email", tinyDB.getString("user_email"));
                params.put("password", tinyDB.getString("user_pass"));

                return params;
            }



        };

        RequestHandler.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    private void showMessage(String _s) {
        Toast.makeText(getActivity(), _s, Toast.LENGTH_LONG).show();
    }


    private static String getUSNumber(String Numtoconvert){

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        try {
            if(Numtoconvert.contains("٫"))
                Numtoconvert=formatter.parse(Numtoconvert.split("٫")[0].trim())+"."+formatter.parse(Numtoconvert.split("٫")[1].trim());
            else
                Numtoconvert=formatter.parse(Numtoconvert).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Numtoconvert;
    }



    public void elmadenaSpinner()
    {
        add_madena = getActivity().findViewById(R.id.add_madena);
        ArrayAdapter<String> add_madenaAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, elmadena);
        add_madenaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_madena.setAdapter(add_madenaAdapter);
        add_madena.setOnItemSelectedListener(this);
        add_madenaAdapter.notifyDataSetChanged();
    }

    public void categoriesSpinner()
    {
        add_department = getActivity().findViewById(R.id.add_depart);
        adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, categories);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_department.setAdapter(adapter1);
        add_department.setOnItemSelectedListener(this);
        adapter1.notifyDataSetChanged();
    }

    public void subcategoriesSpinner()
    {
        add_sub = getActivity().findViewById(R.id.add_sub);
        add_sub.setVisibility(View.GONE);
        adapter2 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, subCategories);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_sub.setAdapter(adapter2);
        add_sub.setOnItemSelectedListener(this);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {

            case R.id.add_depart:

                post_department = add_department.getSelectedItem().toString();

                if(add_department.getSelectedItemPosition()==0)
                {

                    subCategories.clear();
                    post_sub = "none";
                    add_sub.setVisibility(View.GONE);


                }else if(add_department.getSelectedItemPosition()==1)
                {

                    subCategories.clear();

                    subCategories.add("تويوتا");
                    subCategories.add("فورد");
                    subCategories.add("شيفروليه");
                    subCategories.add("نيسان");
                    subCategories.add("هيونداى");
                    subCategories.add("ليكسز");
                    subCategories.add("جى ام سى");
                    subCategories.add("شاحنات و معدات ثقيله");
                    subCategories.add("مرسيدس");
                    subCategories.add("هوندا");
                    subCategories.add("بى ام دبليو");
                    subCategories.add("قطع غيار و ملحقات");
                    subCategories.add("دبابات");
                    subCategories.add("كيا");
                    subCategories.add("دودج");
                    subCategories.add("كريسلر");
                    subCategories.add("جيب");
                    subCategories.add("ميتسوبيشى");
                    subCategories.add("ماذدا");
                    subCategories.add("لاندروفر");
                    subCategories.add("ايسوزو");
                    subCategories.add("كاديلاك");
                    subCategories.add("بورش");
                    subCategories.add("اودى");
                    subCategories.add("سوزوكى");
                    subCategories.add("انفينتى");
                    subCategories.add("هامار");
                    subCategories.add("لينكولن");
                    subCategories.add("فولكس واجن");
                    subCategories.add("دايهاتسو");
                    subCategories.add("جيلى");
                    subCategories.add("ميركورى");
                    subCategories.add("فولفو");
                    subCategories.add("بيجو");
                    subCategories.add("بنتلى");
                    subCategories.add("جاجوار");
                    subCategories.add("سوبارو");
                    subCategories.add("ام جى");
                    subCategories.add("زد اكس اوتو");
                    subCategories.add("رينو");
                    subCategories.add("بيوك");
                    subCategories.add("مازيراتى");
                    subCategories.add("رولز رويس");
                    subCategories.add("لامبورجينى");
                    subCategories.add("اوبل");
                    subCategories.add("سكودا");
                    subCategories.add("فيرارى");
                    subCategories.add("ستروين");
                    subCategories.add("شيرى");
                    subCategories.add("سيات");
                    subCategories.add("دايو");
                    subCategories.add("ساب");
                    subCategories.add("فيات");
                    subCategories.add("سانج يونج");
                    subCategories.add("استون مارتن");
                    subCategories.add("بروتون");
                    subCategories.add("سيارات مصدومة");
                    subCategories.add("سيارات للتنازل");
                    subCategories.add("سيارات تراثية");

                    add_sub.setVisibility(View.VISIBLE);

                }else if(add_department.getSelectedItemPosition()==2)
                {
                    subCategories.clear();

                    subCategories.add("اراضى للبيع");
                    subCategories.add("شقق للأيجار");
                    subCategories.add("فلل للبيع");
                    subCategories.add("شقق للبيع");
                    subCategories.add("بيوت للبيع");
                    subCategories.add("أراضى تجارية للبيع");
                    subCategories.add("أستراحات للأيجار");
                    subCategories.add("محلات للتقبيل");
                    subCategories.add("محلات للأيجار");
                    subCategories.add("عمارة للأيجار");
                    subCategories.add("استراحات للبيع");
                    subCategories.add("فلل للأيجار");
                    subCategories.add("مزارع للبيع");
                    subCategories.add("ادوار للأيجار");
                    subCategories.add("بيوت للأيجار");

                    add_sub.setVisibility(View.VISIBLE);

                }else if(add_department.getSelectedItemPosition()==3)
                {
                    subCategories.clear();

                    subCategories.add("أبل");
                    subCategories.add("سامسونج");
                    subCategories.add("سونى");
                    subCategories.add("بلاك بيرى");
                    subCategories.add("كانون");
                    subCategories.add("أل جى");
                    subCategories.add("نوكيا");
                    subCategories.add("مايكروسوفت");
                    subCategories.add("نيكون");
                    subCategories.add("أتش تى سى");
                    subCategories.add("توشيبا");
                    subCategories.add("ديل");
                    subCategories.add("هواوى");
                    subCategories.add("اسوس");
                    subCategories.add("أيسر");
                    subCategories.add("باناسونيك");
                    subCategories.add("فوجى فيلم");
                    subCategories.add("هيتاشى");
                    subCategories.add("اوليمباس");
                    subCategories.add("اجهزه غير مصنفة");
                    subCategories.add("ارقام مميزه");

                    add_sub.setVisibility(View.VISIBLE);

                }else if(add_department.getSelectedItemPosition()==4)
                {
                    subCategories.clear();

                    subCategories.add("غنم");
                    subCategories.add("بغبغاء");
                    subCategories.add("حمام");
                    subCategories.add("قطط");
                    subCategories.add("دجاج");
                    subCategories.add("ماعز");
                    subCategories.add("أبل");
                    subCategories.add("خيل");
                    subCategories.add("كلاب");

                    add_sub.setVisibility(View.VISIBLE);

                }else if(add_department.getSelectedItemPosition()==5)
                {
                    subCategories.clear();
                    post_sub = "none";
                    add_sub.setVisibility(View.GONE);

                }else if(add_department.getSelectedItemPosition()==6)
                {
                    subCategories.clear();
                    post_sub = "none";
                    add_sub.setVisibility(View.GONE);

                }else if(add_department.getSelectedItemPosition()==7)
                {
                    subCategories.clear();
                    post_sub = "none";
                    add_sub.setVisibility(View.GONE);

                }else if(add_department.getSelectedItemPosition()==8)
                {
                    subCategories.clear();

                    subCategories.add("خدمات أخرى");
                    subCategories.add("خدمات مقاولات");
                    subCategories.add("خدمات تعقيب");
                    subCategories.add("خدمات توصيل");
                    subCategories.add("خدمات نقل عفش");
                    subCategories.add("خدمات نظافة");
                    subCategories.add("خدمات الشراء من المواقع العالمية");

                    add_sub.setVisibility(View.VISIBLE);

                }

                add_sub.setAdapter(adapter2);

                break;

            case R.id.add_sub:

                post_sub = add_sub.getSelectedItem().toString();

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }





}
