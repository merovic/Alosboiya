package alosboiya.jeddahwave.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;

import alosboiya.jeddahwave.R;
import alosboiya.jeddahwave.Utils.AddButtonClick;

public class ConfirmFragment extends DialogFragment {

    ImageView quit;
    EditText text;
    Button submit,submit2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_confirm,container);

        this.getDialog().setTitle("تأكيد التسجيل");

        quit = rootView.findViewById(R.id.quit);
        text = rootView.findViewById(R.id.text);
        submit = rootView.findViewById(R.id.submit);
        submit2 = rootView.findViewById(R.id.submit2);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new AddButtonClick(text.getText().toString()));
                dismiss();
            }
        });

        submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new AddButtonClick("nosms"));
                dismiss();
            }
        });


        return rootView;


    }
}
