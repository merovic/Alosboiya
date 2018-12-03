package alosboiya.jeddahwave.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import alosboiya.jeddahwave.Activities.HarageDetailsActivity;
import alosboiya.jeddahwave.Models.SalesItems;
import alosboiya.jeddahwave.R;

public class SugesstionAdapter extends RecyclerView.Adapter<SugesstionAdapter.SuggestionViewHolder>{

    private ArrayList<SalesItems> oursuggestionItems;

    private Context context;

    public SugesstionAdapter (ArrayList<SalesItems> suggestionItems)
    {
        oursuggestionItems =suggestionItems;
    }


    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sugesstionitems,parent,false);

        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder,  final int position) {

        SalesItems suggestionItems = oursuggestionItems.get(position);

        if(suggestionItems.getSellseimage().equals("images/imgposting.png"))
        {
            Glide.with(holder.sugimg.getContext()).load(R.drawable.imgposting).into(holder.sugimg);
        }else
        {
            Glide.with(holder.sugimg.getContext()).load(suggestionItems.getSellseimage()).into(holder.sugimg);
        }

        holder.sugdis.setText(suggestionItems.getSalesname());
        holder.sugloc.setText(suggestionItems.getLocation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HarageDetailsActivity.class);

                intent.putExtra("item_id",oursuggestionItems.get(position).getID());
                intent.putExtra("item_title",oursuggestionItems.get(position).getSalesname());
                intent.putExtra("item_owner",oursuggestionItems.get(position).getSallername());
                intent.putExtra("item_city",oursuggestionItems.get(position).getLocation());
                intent.putExtra("item_description",oursuggestionItems.get(position).getDescription());
                intent.putExtra("item_department",oursuggestionItems.get(position).getDepartment());
                intent.putExtra("item_phone",oursuggestionItems.get(position).getPhone());
                intent.putExtra("item_url",oursuggestionItems.get(position).getUrl());
                intent.putExtra("item_date",oursuggestionItems.get(position).getSalesdate());
                intent.putExtra("item_img1",oursuggestionItems.get(position).getSellseimage());
                intent.putExtra("item_img2",oursuggestionItems.get(position).getImage2());
                intent.putExtra("item_img3",oursuggestionItems.get(position).getImage3());
                intent.putExtra("item_img4",oursuggestionItems.get(position).getImage4());
                intent.putExtra("item_img5",oursuggestionItems.get(position).getImage5());
                intent.putExtra("item_img6",oursuggestionItems.get(position).getImage6());
                intent.putExtra("item_img7",oursuggestionItems.get(position).getImage7());
                intent.putExtra("item_img8",oursuggestionItems.get(position).getImage8());
                intent.putExtra("item_img8",oursuggestionItems.get(position).getImage8());

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return oursuggestionItems.size();
    }


    class SuggestionViewHolder extends RecyclerView.ViewHolder {

        ImageView sugimg;
        TextView sugdis, sugloc;

        SuggestionViewHolder(View itemView) {
            super(itemView);
            sugimg = itemView.findViewById(R.id.sug_img);
            sugdis = itemView.findViewById(R.id.sug_dis);
            sugloc= itemView.findViewById(R.id.sug_location);
        }
    }

}
