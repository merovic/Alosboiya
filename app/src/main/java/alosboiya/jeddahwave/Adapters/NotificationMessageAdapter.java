package alosboiya.jeddahwave.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import alosboiya.jeddahwave.Activities.HarageDetailsActivity;
import alosboiya.jeddahwave.Activities.MyPostDetails;
import alosboiya.jeddahwave.Fragments.ReplyMessageFragment;
import alosboiya.jeddahwave.Fragments.SendMessageFragment;
import alosboiya.jeddahwave.Models.NotificationMessageItem;
import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.TinyDB;

public class NotificationMessageAdapter extends RecyclerView.Adapter<NotificationMessageAdapter.NotificationMessageViewHolder> {

    List<NotificationMessageItem> notificationMessageItems;
    Context context;

    TinyDB tinyDB;

    public NotificationMessageAdapter(List<NotificationMessageItem> notificationMessageItems) {
        this.notificationMessageItems = notificationMessageItems;
    }

    @NonNull
    @Override
    public NotificationMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        context = parent.getContext();

        tinyDB = new TinyDB(context);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificationmessage,parent,false);
        return new NotificationMessageAdapter.NotificationMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationMessageViewHolder holder, final int position) {

        final NotificationMessageItem notificationMessageItem = notificationMessageItems.get(position);

        holder.title.setText(notificationMessageItem.getHeader());
        holder.body.setText(notificationMessageItem.getBody());
        holder.date.setText(notificationMessageItem.getDate());

        Glide.with(context).load(notificationMessageItem.getImg()).placeholder(R.drawable.imgposting).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(tinyDB.getString("notype").equals("notifications"))
                {
                   // Intent intent = new Intent(context, MyPostDetails.class);
                   // intent.putExtra("postID",notificationMessageItem.getPostID());
                   // context.startActivity(intent);



                    Intent intent = new Intent(context, HarageDetailsActivity.class);

                    intent.putExtra("item_id",notificationMessageItems.get(position).getID());
                    intent.putExtra("item_owner_id",notificationMessageItems.get(position).getIdMember());
                    intent.putExtra("item_title",notificationMessageItems.get(position).getSalesname());
                    intent.putExtra("item_owner",notificationMessageItems.get(position).getSallername());
                    intent.putExtra("item_city",notificationMessageItems.get(position).getLocation());
                    intent.putExtra("item_description",notificationMessageItems.get(position).getDescription());
                    intent.putExtra("item_department",notificationMessageItems.get(position).getDepartment());
                    intent.putExtra("item_phone",notificationMessageItems.get(position).getPhone());
                    intent.putExtra("item_url",notificationMessageItems.get(position).getUrl());
                    intent.putExtra("item_date",notificationMessageItems.get(position).getSalesdate());
                    intent.putExtra("item_img1",notificationMessageItems.get(position).getSellseimage());
                    intent.putExtra("item_img2",notificationMessageItems.get(position).getImage2());
                    intent.putExtra("item_img3",notificationMessageItems.get(position).getImage3());
                    intent.putExtra("item_img4",notificationMessageItems.get(position).getImage4());
                    intent.putExtra("item_img5",notificationMessageItems.get(position).getImage5());
                    intent.putExtra("item_img6",notificationMessageItems.get(position).getImage6());
                    intent.putExtra("item_img7",notificationMessageItems.get(position).getImage7());
                    intent.putExtra("item_img8",notificationMessageItems.get(position).getImage8());
                    context.startActivity(intent);




                }
            }
        });

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tinyDB.putString("messagesenderID",notificationMessageItem.getSenderID());
                tinyDB.putString("messagePostID",notificationMessageItem.getPostID());

                FragmentManager fm = ((AppCompatActivity)context).getFragmentManager();
                ReplyMessageFragment sendMessageFragment = new ReplyMessageFragment();

                sendMessageFragment.show(fm,"TV_tag");
            }
        });
    }

    private void showMessage(String _s) {
        Toast.makeText(context, _s, Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return notificationMessageItems.size();
    }

    class NotificationMessageViewHolder extends RecyclerView.ViewHolder {

        TextView title,body,date;
        ImageView img;
        Button reply;

        NotificationMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            date = itemView.findViewById(R.id.date);
            img = itemView.findViewById(R.id.img);
            reply = itemView.findViewById(R.id.reply);

            if(tinyDB.getString("notype").equals("notifications"))
            {
                reply.setVisibility(View.GONE);
            }
        }
    }
}
