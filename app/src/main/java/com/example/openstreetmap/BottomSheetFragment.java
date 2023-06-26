package com.example.openstreetmap;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private Drawable image;
    private String string1;
    private String string2;
    private String string3;

    public static BottomSheetFragment newInstance(Drawable image, String string1, String string2, String string3) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        fragment.image = image;
        fragment.string1 = string1;
        fragment.string2 = string2;
        fragment.string3 = string3;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_fragment, container, false);

        ImageView imageView = view.findViewById(R.id.iv_person);
        TextView name = view.findViewById(R.id.tv_name);
        TextView year = view.findViewById(R.id.tv_year);
        TextView time = view.findViewById(R.id.tv_time);
        imageView.setImageDrawable(image);
        name.setText(string1);
        year.setText(string2);
        time.setText(string3);

        return view;
    }
}
