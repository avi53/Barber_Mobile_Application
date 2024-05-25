package edu.tacoma.uw.barber_mobile_application.ui.portfolio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import edu.tacoma.uw.barber_mobile_application.R;

public class PortfolioFragment extends Fragment {

    private GridView mGridView;
    private String[] mImageNames = {"Mid Taper Fade", "Mid Taper Fade", "Mid Taper Fade",
            "Low Taper","Low Taper",
    "Taper Fade", "Taper Fade",
    "Fringe",
    "Low Fade","Low Fade","Low Fade","Low Fade",
    "Mid Fade with Beard Trim", "Mid Fade with Beard Trim","Beard Trim","Beard Trim","Low Taper", "Low Fade",
    "Low Fade", "Regular", "Taper W Dreads","Fade", "High Taper"


    };
    private int[] mImageIds = {R.drawable.q, R.drawable.l, R.drawable.a,
            R.drawable.r,R.drawable.s,
            R.drawable.t,R.drawable.u,R.drawable.v,R.drawable.h,R.drawable.c,R.drawable.w,R.drawable.o,R.drawable.p,
            R.drawable.d,R.drawable.e,R.drawable.g,R.drawable.f,R.drawable.i,R.drawable.b,R.drawable.m,R.drawable.n,
            R.drawable.o,R.drawable.j,
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_portfolio, container, false);

        mGridView = root.findViewById(R.id.gridView);
        mGridView.setAdapter(new ImageAdapter(getContext(), mImageIds, mImageNames));

        mGridView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(getActivity(), mImageNames[position], Toast.LENGTH_SHORT).show()
        );

        return root;
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int[] mImageIds;
        private String[] mImageNames;

        public ImageAdapter(Context context, int[] imageIds, String[] imageNames) {
            mContext = context;
            mImageIds = imageIds;
            mImageNames = imageNames;
        }

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("WrongConstant")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout layout;
            if (convertView == null) {
                layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(8, 8, 8, 8);

                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.addView(imageView);

                TextView textView = new TextView(mContext);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setTextColor(mContext.getResources().getColor(R.color.Gold));
                textView.setTextSize(18);
                textView.setTypeface(null, Typeface.BOLD);
                layout.addView(textView);

                convertView = layout;
            } else {
                layout = (LinearLayout) convertView;
            }

            ImageView imageView = (ImageView) layout.getChildAt(0);
            TextView textView = (TextView) layout.getChildAt(1);

            imageView.setImageResource(mImageIds[position]);
            textView.setText(mImageNames[position]);

            return convertView;
        }
    }
}
