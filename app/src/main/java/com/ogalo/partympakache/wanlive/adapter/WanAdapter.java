package com.ogalo.partympakache.wanlive.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;


import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.duowan.mobile.netroid.toolbox.ImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ogalo.partympakache.wanlive.Payments;
import com.ogalo.partympakache.wanlive.R;
import com.ogalo.partympakache.wanlive.WanImageViewBlur;
import com.ogalo.partympakache.wanlive.app.WanLive;
import com.ogalo.partympakache.wanlive.data.WanItem;
//
//
import com.ogalo.partympakache.wanlive.data.Post;
import com.ogalo.partympakache.wanlive.data.WanItem;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WanAdapter extends BaseAdapter {
    private String uid;

    Animation scaleUp;
    private DatabaseReference mDatabase;
    private ViewHolder holder;
    private DatabaseReference firebref;
	private Activity activity;
	private LayoutInflater inflater;
    private int lastPosition = -1;
    Context context;
    private boolean isVisible;
    int radiusArr[];
	private List<WanItem> wanItems;
    HashMap<Integer,Boolean> toggleButtonStateTracker = new HashMap<Integer, Boolean>();
	ImageLoader imageLoader = WanLive.getInstance().getImageLoader();

	public WanAdapter(Activity activity, List<WanItem> wanItems) {
		this.activity = activity;
		this.wanItems = wanItems;
        radiusArr = new int[]{25, 23, 21, 19, 17};// decide blur amount


    }

    public void clear() {

        wanItems.clear();

        notifyDataSetChanged();

    }
    public void addAll(List<WanItem> wanItem) {

        wanItems.addAll(wanItem);

        notifyDataSetChanged();

    }
	@Override
	public int getCount() {
		return wanItems.size();
	}

	@Override
	public Object getItem(int location) {
		return wanItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


    static class ViewHolder {
        public WanImageViewBlur feedImageView;
        public TextView name;
        public TextView time;
        public TextView cost;
        public TextView longitude;
        public TextView latitude;
        public TextView rating;
        public TextView status;
        public TextView imge;
        public Button buy;
        public RatingBar rates;
        public ToggleButton toggle;
        public ToggleButton toggla;


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 500;
    }



    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {

        scaleUp = AnimationUtils.loadAnimation(activity, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);


        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.feetitemnew, null);
        convertView.startAnimation(scaleUp);
        lastPosition = position;

        if (imageLoader == null)
            imageLoader = WanLive.getInstance().getImageLoader();

        viewHolder.name = (TextView) convertView.findViewById(R.id.titles);
        viewHolder.buy = (Button) convertView.findViewById(R.id.buy);
        viewHolder.time = (TextView) convertView.findViewById(R.id.time);
        viewHolder.cost = (TextView) convertView.findViewById(R.id.cost);
        viewHolder.longitude = (TextView) convertView.findViewById(R.id.longitude);
        viewHolder.latitude = (TextView) convertView.findViewById(R.id.latitude);
        viewHolder.rating = (TextView) convertView.findViewById(R.id.rating);
        viewHolder.status = (TextView) convertView.findViewById(R.id.statuses);
        viewHolder.imge = (TextView) convertView.findViewById(R.id.imge);
        viewHolder.feedImageView = (WanImageViewBlur) convertView
                .findViewById(R.id.feedImage1);
       viewHolder.toggle = (ToggleButton) convertView.findViewById(R.id.toggleButton);
        viewHolder.toggla = (ToggleButton) convertView.findViewById(R.id.toggl);
        viewHolder.rates = (RatingBar) convertView.findViewById(R.id.myrating);
        firebref = mDatabase = FirebaseDatabase.getInstance().getReference();
        convertView.setTag(viewHolder);
    }

        else
        {
            holder =(ViewHolder) convertView.getTag();
        }
       holder = (ViewHolder) convertView.getTag();
		LayerDrawable stars = (LayerDrawable) holder.rates.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_ATOP);


        final WanItem item = wanItems.get(position);


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();









        if(item.getCost().toUpperCase().equals("FREE"))
        {
            holder.toggla.setVisibility(View.VISIBLE);
            holder.buy.setVisibility(View.GONE);




        }

        if (! toggleButtonStateTracker.containsKey(position)){
            // Now the HashMap definitely contains the key
            toggleButtonStateTracker.put(position,false);
        }

        boolean isChecked = toggleButtonStateTracker.get(position);

    holder.toggle.setChecked(isChecked);


        firebref.child("user-favos").child(uid).child(item.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    holder.toggle.setBackgroundResource(R.drawable.heart_liked);
                    holder.toggle.setChecked(true);
                }
                else
                {
                    holder.toggle.setChecked(false);
                    holder.toggle.setBackgroundResource(R.drawable.heart_notliked);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebref.child("reserved").child(uid).child(item.getName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    holder.toggla.setBackgroundResource(R.drawable.bg_greenf);
                    holder.toggla.setChecked(true);
                }
                else
                {
                    holder.toggla.setChecked(false);
                    holder.toggla.setBackgroundResource(R.drawable.bg_greenreal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        holder.toggla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    item.setChecked(true);





                    writePost(uid, item.getStatus(), item.getName(), item.getCost(), item.getRating());

//                    writeNewPost(uid, item.getStatus(), item.getName(), item.getCost(), item.getRating());
                    holder.toggla.setBackgroundResource(R.drawable.bg_greenf);



                    // The toggle is enabled means "like" in your case // call api for like
                } else if
                        (!isChecked){
                    mDatabase.child("reserved-universal").child(item.getName()).removeValue();
                    mDatabase.child("reserved").child(uid).child(item.getName()).removeValue();
//                    mDatabase.child("favos").child(item.getName()).removeValue();
//                    mDatabase.child("user-favos").child(uid).child(item.getName()).removeValue();
                    item.setChecked(false);

                    holder.toggla.setBackgroundResource(R.drawable.bg_greenreal);

                    // The toggle is disabled means "dislike" in your case // call api for dislike
                }
            }
        });













        holder.toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    item.setChecked(true);


                    toggleButtonStateTracker.put(position, isChecked);




                    writeNewPost(uid, item.getStatus(), item.getName(), item.getCost(), item.getRating());
                    holder.toggle.setBackgroundResource(R.drawable.heart_liked);



                    // The toggle is enabled means "like" in your case // call api for like
                } else if
                    (!isChecked){

                    mDatabase.child("favos").child(item.getName()).removeValue();
                    mDatabase.child("user-favos").child(uid).child(item.getName()).removeValue();
                    item.setChecked(false);

                    holder.toggle.setBackgroundResource(R.drawable.heart_notliked);

                    // The toggle is disabled means "dislike" in your case // call api for dislike
                }
            }
        });





		final String costens=item.getCost();




        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activity,Payments.class);
                i.putExtra("price", costens);
                activity.startActivity(i);
            }
        });


//        buy.setText("Buy tickets at "+item.getCost()+" /=");
        holder.buy.setText("Buy Tickets");









        holder.name.setText(item.getName());

        holder.imge.setText(item.getImge());
        String costCon=item.getCost().toUpperCase();
//        if(item.getCost().equals("FREE")) {
        if(costCon.equals("FREE")) {
            holder.cost.setText("" + item.getCost());
        }
        else
        {
            holder.cost.setText("KSH. " + item.getCost());
        }
        holder.time.setText(item.getTimes());
        holder.longitude.setText(item.getLongitude());
        holder.latitude.setText(item.getLatitude());
        holder.rating.setText(item.getRating());



        holder.rates.setRating(Float.parseFloat(item.getRating()));

        holder.status.setText(item.getStatus());




//		name.setText(item.getName());

		// Converting timestamp into x ago format
//		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//				Long.parseLong(item.getTimeStamp()),
//				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
//		timestamp.setText(timeAgo);

		// Chcek for empty status message
//		if (!TextUtils.isEmpty(item.getStatus())) {
//			statusMsg.setText(item.getStatus());
//			statusMsg.setVisibility(View.VISIBLE);
//		}
//
// else {
//			// status is empty, remove from view
//			statusMsg.setHeight(0);
//			statusMsg.setWidth(0);
//			statusMsg.setTextColor(R.color.white);
//			statusMsg.setVisibility(View.GONE);
//		}





//        // Chcek for empty location
//        if (!TextUtils.isEmpty(item.getLocations())) {
//            locations.setText(item.getLocations());
//            locations.setVisibility(View.VISIBLE);
//        }
//
//        else {
//            // location is empty, remove from view
//
//            locations.setVisibility(View.GONE);
//        }




        // Chcek for empty time
//        if (!TextUtils.isEmpty(item.getTimes())) {
//            times.setText(item.getTimes());
//            times.setVisibility(View.VISIBLE);
//        }
//
//        else {
//            // time is empty, remove from view
//            times.setVisibility(View.GONE);
//        }

        // Chcek for empty status message
//        if (!TextUtils.isEmpty(item.getCost())) {
//            cost.setText(item.getCost());
//            cost.setVisibility(View.VISIBLE);
//        }
//
//        else {
//            // cost is empty, remove from view
//
//            cost.setVisibility(View.GONE);
//        }


//		if (!TextUtils.isEmpty(item.getName())) {
//			name.setText(item.getName());
//			name.setVisibility(View.VISIBLE);
//		}
//
//		else {
//			// cost is empty, remove from view
//
//			name.setVisibility(View.GONE);
//		}



		// Checking for null feed url
//		if (item.getUrl() != null) {
//			url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//					+ item.getUrl() + "</a> "));
//
//			// Making url clickable
//			url.setMovementMethod(LinkMovementMethod.getInstance());
//			url.setVisibility(View.VISIBLE);
//		} else {
//			// url is null, remove from the view
//			url.setVisibility(View.GONE);
//		}
//
//		// user profile pic
//		profilePic.setImageUrl(item.getProfilePic(), imageLoader);

		// Feed image
		if (item.getImge() != null) {
            holder.feedImageView.setImageUrl(item.getImge(), imageLoader);
            holder.feedImageView.setVisibility(View.VISIBLE);
//            String num="0.3";
//            feedImageView.setAlpha(Float.parseFloat(num));




            holder.feedImageView
					.setResponseObserver(new WanImageViewBlur.ResponseObserver() {
						@Override
						public void onError() {


						}

						@Override
						public void onSuccess() {


//                            BitmapDrawable drawable = (BitmapDrawable) feedImageView.getDrawable();
//                            Bitmap bitmap = drawable.getBitmap();
//                            Bitmap blurred = blurRenderScript(bitmap, radiusArr[3]);//second parametre is radius
//                            feedImageView.setImageBitmap(blurred);

						}
					});
		} else {
            holder.feedImageView.setVisibility(View.GONE);
		}

		return convertView;
	}









    private void writePost(String userId, String username, String title, String body, String rating) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        mDatabase=FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body,  rating);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/reserved-universal/" + title, postValues);
        childUpdates.put("/reserved/" + userId + "/" + title, postValues);

        mDatabase.updateChildren(childUpdates);
    }






    private void writeNewPost(String userId, String username, String title, String body, String rating) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        mDatabase=FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body,  rating);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/favos/" + title, postValues);
        childUpdates.put("/user-favos/" + userId + "/" + title, postValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void delete()
    {

    }

    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }



    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
