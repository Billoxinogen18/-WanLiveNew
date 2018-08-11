package com.ogalo.partympakache.wanlive;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ogalo.partympakache.wanlive.app.AppController;
import com.ogalo.partympakache.wanlive.data.WanItem;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private String uid;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private DatabaseReference mDatabase;
    private TextView times;
    private  View becauseRailaIsGod;
    private DatabaseReference firebref;
    public BottomSheetFragment() {
        // Required empty public constructor
    }
    public Boolean ischeck=getIscheckedw();
    public Boolean ischeckedn=getIschecked();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        firebref=mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();










        becauseRailaIsGod=inflater.inflate(R.layout.bottom_sheetfrag, container, false);

//        ((View) becauseRailaIsGod.getParent()).setBackgroundColor(Color.TRANSPARENT);




        ScrollView bottom=(ScrollView) becauseRailaIsGod.findViewById(R.id.bottom);
        LinearLayout imag=(LinearLayout)becauseRailaIsGod.findViewById(R.id.imageviw);
        String num="0.3";

//        bottom.setAlpha(Float.parseFloat(num));
        TextView titl=(TextView)becauseRailaIsGod.findViewById(R.id.title);
        times=(TextView)becauseRailaIsGod.findViewById(R.id.timenu);
        RatingBar ratingBar=(RatingBar)becauseRailaIsGod.findViewById(R.id.myratingu);
        TextView content=(TextView)becauseRailaIsGod.findViewById(R.id.status);
        Button view=(Button)becauseRailaIsGod.findViewById(R.id.view);
        WanImageView wanImageView=(WanImageView)becauseRailaIsGod.findViewById(R.id.wanImage);
        final ToggleButton toggleButton=(ToggleButton)becauseRailaIsGod.findViewById(R.id.toggleSheet);



















        Bundle bundle = this.getArguments();
//        String myValue = bundle.getString("title");

//
//        Toast.makeText(getContext(), myValue, Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        WanMaps myActiv=(WanMaps)getActivity();
        final String title=myActiv.getTitleso();
        final String contents=myActiv.getContentso();
        final String ratingss=myActiv.getRatingso();
        final String time=myActiv.getTimeso();
        final String imge=myActiv.getImgso();
        final String costso=myActiv.getCostso();
        final String latitude=myActiv.getLatits();
        final String longitude=myActiv.getLongits();

        Boolean trus;






        firebref.child("user-favos").child(uid).child(title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    toggleButton.setChecked(true);
                }
                else
                {
                    toggleButton.setChecked(false);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        wanImageView.setImageUrl(imge, imageLoader);
        wanImageView.setVisibility(View.VISIBLE);
        wanImageView
                .setResponseObserver(new WanImageView.ResponseObserver() {
                    @Override
                    public void onError() {
                        Toast.makeText(getContext(), "Check your connection", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                    }
                });


        firebref.child("user-favos").child(uid).child(title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    WanItem wanItem=new WanItem();
                    wanItem.setUrl("true");



                }
                else
                {
                    WanItem wanItem=new WanItem();
                    wanItem.setUrl("false");



                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        WanItem wanItems=new WanItem();

        Boolean truth=toggleButton.isChecked();
        final String trun=truth.toString();



//        Toast.makeText(getContext(), "Try "+wanItems.getUrl(), Toast.LENGTH_SHORT).show();



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getContext(), DetailsActivity.class);
                i.putExtra("checkeds", trun);
                i.putExtra("longitude", longitude);
                i.putExtra("status", contents);
                i.putExtra("latitude", latitude);
                i.putExtra("rating", ratingss);
                i.putExtra("cost", costso);
                i.putExtra("time", time);
                i.putExtra("imge", imge);
                i.putExtra("titles", title);
                startActivity(i);


            }
        });



//        Toast.makeText(getContext(), "Is checked is "+ischecked, Toast.LENGTH_SHORT).show();




        times.setText(time);
ratingBar.setRating(Float.parseFloat(ratingss));


        titl.setText(title);
        content.setText(contents);

        applyBlur();
//        String contentso=myActiv.getContentso();
//
//        Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();


        return becauseRailaIsGod;




    }

    public void setIschecked(String ischeckedn)
    {

    }

    public Boolean getIschecked()
    {

        return false;
    }
    public Boolean getIscheckedw()
    {

        return true;
    }




    private void applyBlur() {
        final String text="Test";
        becauseRailaIsGod.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                becauseRailaIsGod.getViewTreeObserver().removeOnPreDrawListener(this);
                becauseRailaIsGod.buildDrawingCache();

                Bitmap bmp =   becauseRailaIsGod.getDrawingCache();
                blur(bmp, times);
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        long startMs = System.currentTimeMillis();

        float radius = 25;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
                (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getTop());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(getContext());

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(radius);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

        view.setBackground(new BitmapDrawable(
                getResources(), overlay));

        rs.destroy();
//        times.setText(System.currentTimeMillis() - startMs + "ms");
    }

    @Override
    public String toString() {
        return "RenderScript";
    }

    private TextView addStatusText(ViewGroup container) {
        TextView result = new TextView(getContext());
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setTextColor(0xFFFFFFFF);
        container.addView(result);
        return result;
    }


    }
