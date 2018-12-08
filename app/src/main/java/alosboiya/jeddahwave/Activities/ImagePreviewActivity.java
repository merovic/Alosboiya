package alosboiya.jeddahwave.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import alosboiya.jeddahwave.R;

public class ImagePreviewActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SliderLayout imgslider;

    List<String> pics;

    String img1,img2,img3,img4,img5,img6,img7,img8;

    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);

        imgslider = findViewById(R.id.myslider);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {


            img1 = extras.getString("item_img1");
            img2 = extras.getString("item_img2");
            img3 = extras.getString("item_img3");
            img4 = extras.getString("item_img4");
            img5 = extras.getString("item_img5");
            img6 = extras.getString("item_img6");
            img7 = extras.getString("item_img7");
            img8 = extras.getString("item_img8");

           // position = extras.getInt("item_position");

        }

        pics = new ArrayList<>();

        pics.add(img1);
        pics.add(img2);
        pics.add(img3);
        pics.add(img4);
        pics.add(img5);
        pics.add(img6);
        pics.add(img7);
        pics.add(img8);

        HashMap<String,String> url_maps = new HashMap<>();
        for(int i=0;i<pics.size();i++)
        {
            if(!pics.get(i).equals("images/imgposting.png"))
            {
                url_maps.put("Picture "+String.valueOf(i + 1),pics.get(i));
            }
        }


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            imgslider.addSlider(textSliderView);
        }

       // imgslider.setCurrentPosition(position);

        imgslider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imgslider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imgslider.setCustomAnimation(new DescriptionAnimation());
        imgslider.setDuration(4000);
        imgslider.addOnPageChangeListener(this);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
