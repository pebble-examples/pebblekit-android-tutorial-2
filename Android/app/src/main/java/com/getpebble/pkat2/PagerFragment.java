package com.getpebble.pkat2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class PagerFragment extends Fragment {

    private int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.pager_item, container, false);

        // Set page color
        RelativeLayout layout = (RelativeLayout)rootView.findViewById(R.id.pager_item_layout);
        Random random = new Random();
        layout.setBackgroundColor(Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255)));

        // Use child Views
        ImageView imageView = (ImageView)rootView.findViewById(R.id.image_view);

        // Set the page label
        TextView textView = (TextView)rootView.findViewById(R.id.text_view);
        Bundle args = getArguments();
        mPosition = args.getInt("position");
        textView.setText("Page " + mPosition);

        return rootView;
    }

}
