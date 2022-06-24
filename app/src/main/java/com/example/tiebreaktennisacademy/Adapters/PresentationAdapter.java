package com.example.tiebreaktennisacademy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.example.tiebreaktennisacademy.Models.Presentation;
import com.example.tiebreaktennisacademy.R;
import java.util.List;

public class PresentationAdapter extends PagerAdapter {
    private Context context;
    private List<Presentation> presentationItemList;

    public PresentationAdapter(Context context, List<Presentation> presentationItemList) {
        this.context = context;
        this.presentationItemList = presentationItemList;
    }

    @Override
    public int getCount() {
        return presentationItemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutIntro = inflater.inflate(R.layout.items_presentation,null);

        ImageView image = (ImageView) layoutIntro.findViewById(R.id.image_icon);
        TextView titre = (TextView) layoutIntro.findViewById(R.id.titre);
        TextView description = (TextView) layoutIntro.findViewById(R.id.description);

        titre.setText(presentationItemList.get(position).getTitre());
        description.setText(presentationItemList.get(position).getDescription());
        image.setImageResource(presentationItemList.get(position).getImage());

        container.addView(layoutIntro);
        return layoutIntro;
    }
}
