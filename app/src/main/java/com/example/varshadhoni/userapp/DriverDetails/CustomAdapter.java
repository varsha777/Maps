package com.example.varshadhoni.userapp.DriverDetails;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.varshadhoni.userapp.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by VarshaDhoni on 10/13/2017.
 */

public class CustomAdapter extends PagerAdapter {

    List<CarImageAdapter> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context ctx;

    public CustomAdapter(Context ctx, List<CarImageAdapter> data) {
        this.ctx = ctx;
        this.data = data;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        CarImageAdapter current = data.get(position);
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.swipe, container, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        Glide.with(ctx).load(current.booksImages)
                .into(imageView);
        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.invalidate();

    }
}
