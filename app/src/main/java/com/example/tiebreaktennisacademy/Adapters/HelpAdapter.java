package com.example.tiebreaktennisacademy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import com.example.tiebreaktennisacademy.Models.Help;
import com.example.tiebreaktennisacademy.R;
import java.util.List;

public class HelpAdapter extends PagerAdapter {
    private Context context;
    private List<Help> helpList;

    public HelpAdapter(Context context, List<Help> helpList) {
        this.context = context;
        this.helpList = helpList;
    }

    @Override
    public int getCount() {
        return helpList.size();
    }

    @Override
    public boolean isViewFromObject(View view,Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutIntro = inflater.inflate(R.layout.item_help,null);

        ImageView image = (ImageView) layoutIntro.findViewById(R.id.image_icon);
        TextView titre = (TextView) layoutIntro.findViewById(R.id.titre);
        TextView description = (TextView) layoutIntro.findViewById(R.id.description);

        titre.setText(helpList.get(position).getTitre());
        description.setText(helpList.get(position).getDescription());
        image.setImageResource(helpList.get(position).getImage());

        container.addView(layoutIntro);
        return layoutIntro;
    }
}
