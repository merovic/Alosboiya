package alosboiya.jeddahwave.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alosboiya.jeddahwave.Activities.HarageDetailsActivity;
import alosboiya.jeddahwave.Fragments.EditPostFragment;
import alosboiya.jeddahwave.Models.SalesItems;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.RequestHandler;
import alosboiya.jeddahwave.Utils.TinyDB;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.ProfilePostsViewHolder> {

    List<SalesItems> postsItems;

    Context context;

    TinyDB tinyDB;

    public ProfilePostsAdapter(List<SalesItems> postsItems) {
        this.postsItems = postsItems;
    }

    @NonNull
    @Override
    public ProfilePostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        tinyDB = new TinyDB(context);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_posts,parent,false);
        return new ProfilePostsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ProfilePostsViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        final SalesItems salesItems = postsItems.get(position);


        holder.location.setText(salesItems.getLocation());
        holder.salesname.setText(salesItems.getSalesname());
        holder.saller_name.setText(salesItems.getSallername());
        holder.salesdate.setText(postsItems.get(position).getDate());



        if(salesItems.getSellseimage().equals("images/imgposting.png"))
        {
            Glide.with(holder.salesimage.getContext()).load(R.drawable.imgposting).into(holder.salesimage);
        }else
        {
            Glide.with(holder.salesimage.getContext()).load(salesItems.getSellseimage()).into(holder.salesimage);
        }

        if(salesItems.getImage2().contains("videos"))
        {
            holder.videoicon.setVisibility(View.VISIBLE);
        }else
        {
            holder.videoicon.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HarageDetailsActivity.class);

                intent.putExtra("item_id",postsItems.get(position).getID());
                intent.putExtra("item_owner_id",postsItems.get(position).getIdMember());
                intent.putExtra("item_title",postsItems.get(position).getSalesname());
                intent.putExtra("item_owner",postsItems.get(position).getSallername());
                intent.putExtra("item_city",postsItems.get(position).getLocation());
                intent.putExtra("item_description",postsItems.get(position).getDescription());
                intent.putExtra("item_department",postsItems.get(position).getDepartment());
                intent.putExtra("item_phone",postsItems.get(position).getPhone());
                intent.putExtra("item_url",postsItems.get(position).getUrl());
                intent.putExtra("item_date",postsItems.get(position).getSalesdate());
                intent.putExtra("item_img1",postsItems.get(position).getSellseimage());
                intent.putExtra("item_img2",postsItems.get(position).getImage2());
                intent.putExtra("item_img3",postsItems.get(position).getImage3());
                intent.putExtra("item_img4",postsItems.get(position).getImage4());
                intent.putExtra("item_img5",postsItems.get(position).getImage5());
                intent.putExtra("item_img6",postsItems.get(position).getImage6());
                intent.putExtra("item_img7",postsItems.get(position).getImage7());
                intent.putExtra("item_img8",postsItems.get(position).getImage8());
                context.startActivity(intent);

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletePost(postsItems.get(position).getID(),position);
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //updatePost(postsItems.get(position).getID());
                //showMessage(postsItems.get(position).getDate());
                newUpdatePost(tinyDB.getString("user_id"),postsItems.get(position).getSalesname(),tinyDB.getString("user_img"),postsItems.get(position).getSallername(),postsItems.get(position).getPhone(),tinyDB.getString("user_country"),tinyDB.getString("user_email"),postsItems.get(position).getDescription(),postsItems.get(position).getLocation(),postsItems.get(position).getDepartment(),"",postsItems.get(position).getPhone(),postsItems.get(position).getSubdepartment(),postsItems.get(position).getSellseimage(),postsItems.get(position).getImage2(),postsItems.get(position).getImage3(),postsItems.get(position).getImage4(),postsItems.get(position).getImage5(),postsItems.get(position).getImage6(),postsItems.get(position).getImage7(),postsItems.get(position).getImage8(),postsItems.get(position).getDate());
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tinyDB.putString("postID",salesItems.getID());
                tinyDB.putString("postPhone",salesItems.getPhone());
                tinyDB.putString("postTitle",salesItems.getSalesname());
                tinyDB.putString("postDescription",salesItems.getDescription());

                final FragmentManager fm = ((Activity) context).getFragmentManager();
                EditPostFragment editPostFragment = new EditPostFragment();

                editPostFragment.show(fm,"TV_tag");

            }
        });

    }

    @Override
    public int getItemCount() {
        return postsItems.size();
    }


    private void deletePost(final String ID, final int position)
    {
        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/wsnew.asmx/delete_post?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("True"))
                        {
                            showMessage("تم حذف الاعلان");

                            postsItems.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,postsItems.size());

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
                params.put("id_post", ID);
                return params;
            }



        };

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void newUpdatePost(final String IDMember,final String postTitle,final String userImage,final String userName,final String userPhone,final String userCountry,final String userEmail,final String postDesc,final String postCity,final String postCat,final String postPrice,final String postTel,final String postSub,final String postImage,final String postImage2,final String postImage3,final String postImage4,final String postImage5,final String postImage6,final String postImage7,final String postImage8,final String date)
    {
        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/wsnew.asmx/update_to_harj?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        showMessage("تم تحديث الاعلان");
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
                params.put("id_member", IDMember);
                params.put("title", postTitle);
                params.put("imagemember", userImage);
                params.put("namemember", userName);
                params.put("Phone", userPhone);
                params.put("counttttry", userCountry);
                params.put("emailll", userEmail);
                params.put("des", postDesc);
                params.put("ciiiiiiiiiiiiiiiiiiiiiiity", postCity);
                params.put("category", postCat);
                params.put("price", postPrice);
                params.put("tel", postTel);
                params.put("sub", postSub);
                params.put("x", postImage);
                params.put("x_2", postImage2);
                params.put("x_3", postImage3);
                params.put("x_4", postImage4);
                params.put("x_5", postImage5);
                params.put("x_6", postImage6);
                params.put("x_7", postImage7);
                params.put("x_8", postImage8);
                params.put("device","Android");
                params.put("datee",date);
                return params;
            }

        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,0,0));

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }


    private void updatePost(final String ID)
    {
        String GET_JSON_DATA_HTTP_URL = "http://alosboiya.com.sa/wsnew.asmx/update_to_harj?";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JSON_DATA_HTTP_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        showMessage("تم تحديث الاعلان");
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
                params.put("id_post", ID);
                return params;
            }



        };

        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }


    public class ProfilePostsViewHolder extends RecyclerView.ViewHolder {

        TextView location , salesname, salesdate , saller_name;
        ImageView salesimage,videoicon;
        Button delete,edit,update;

        public ProfilePostsViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.location_name);
            salesname = itemView.findViewById(R.id.sales_name);
            salesdate = itemView.findViewById(R.id.sales_date);
            saller_name = itemView.findViewById(R.id.saller_name);
            salesimage = itemView.findViewById(R.id.sales_image);
            videoicon = itemView.findViewById(R.id.videoicon);
            delete = itemView.findViewById(R.id.deletepost);
            edit = itemView.findViewById(R.id.editpost);
            update = itemView.findViewById(R.id.updatepost);
        }
    }

    private void showMessage(String _s) {
        Toast.makeText(context.getApplicationContext(), _s, Toast.LENGTH_LONG).show();
    }
}
