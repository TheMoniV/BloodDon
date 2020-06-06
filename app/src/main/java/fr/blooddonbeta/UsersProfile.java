package fr.blooddonbeta;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UsersProfile extends AppCompatActivity {

    private DatabaseReference rootRef;
    private DatabaseReference users;
    private DatabaseReference recommendation;

    private String mUserId;
    private String otherUserId;
    private ArrayList<ImageView> medalsRef;
    private CircleImageView usersProfilePic ;
    private TextView usersProfileName;
    private TextView usersProfileAdrs ;
    private TextView usersProfileBloodType;
    private TextView usersPoints ;
    private LikeButton reccomendUserButton ;
    private String current_reccomend_ref ;
    private long point = 0 ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);
        medalsRef = new ArrayList<ImageView> ();
        medalsRef.add((ImageView)findViewById(R.id.medal1));
        medalsRef.add((ImageView)findViewById(R.id.medal2));
        medalsRef.add((ImageView)findViewById(R.id.medal3));
        medalsRef.add((ImageView)findViewById(R.id.medal4));
        medalsRef.add((ImageView)findViewById(R.id.medal6));
        medalsRef.add((ImageView)findViewById(R.id.medal6));


        usersProfilePic        = findViewById(R.id.usersProfilePic      );
        usersProfileName       = findViewById(R.id.usersProfileName     );
        usersProfileAdrs       = findViewById(R.id.usersProfileAdrs     );
        usersProfileBloodType  = findViewById(R.id.usersProfileBloodType);
        reccomendUserButton    =  findViewById(R.id.star_button);
        usersPoints            = findViewById(R.id.usersPoints);


        // Firebase Init
        rootRef         = FirebaseDatabase.getInstance().getReference();
        users           = FirebaseDatabase.getInstance().getReference("users");
        mUserId         = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUserId     = getIntent().getExtras().getString("otherUserId");
        recommendation  = FirebaseDatabase.getInstance().getReference("recommendations");

        current_reccomend_ref = "recommendations/" + mUserId + "/" +otherUserId;



        if(otherUserId.isEmpty() && otherUserId == null)
            finish();
        users.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child("fullname").getValue() != null)
                {
                    try
                    {
                        testDialog(dataSnapshot.child("profileUrl").toString());
                        StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(dataSnapshot.child("profileUrl").getValue().toString());

                        GlideApp.with(getApplicationContext())
                                .load(gsReference)
                                .override(100, 100)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(usersProfilePic);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                    usersProfileName.setText(dataSnapshot.child("fullname").getValue().toString());
                    usersProfileBloodType.setText(dataSnapshot.child("bloodtype").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recommendation.child(otherUserId).child("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                {
                    giveMedal(point = (long)dataSnapshot.getValue());

                        usersPoints.setText("" + point);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        recommendation.child(mUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(otherUserId).exists())
                {
                    if (dataSnapshot.child(otherUserId).child("pushed").getValue().equals(true))
                    {
                        reccomendUserButton.setLiked(true);
                        reccomendUserButton.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        reccomendUserButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                rootRef.child("recommendations").child(mUserId).child(otherUserId).child("pushed").setValue(true);
                rootRef.child("recommendations").child(otherUserId).child("points").setValue(point + 1);

                reccomendUserButton.setLiked(true);
                reccomendUserButton.setEnabled(false);
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });



    }

    public void recommendUser(View view)
    {
        rootRef.child("recommendations").child(mUserId).child(otherUserId).child("pushed").setValue(true);
        rootRef.child("recommendations").child(otherUserId).child("points").setValue(point + 1);

    }

    public void giveMedal(long point)
    {
        if (point > 50)
            medalsRef.get(0).setVisibility(View.VISIBLE);

        if (point > 100)
            medalsRef.get(1).setVisibility(View.VISIBLE);


        if (point > 250)
            medalsRef.get(2).setVisibility(View.VISIBLE);


        if (point > 400)
            medalsRef.get(3).setVisibility(View.VISIBLE);


        if (point > 600)
            medalsRef.get(4).setVisibility(View.VISIBLE);


        if (point > 1000)
            medalsRef.get(5).setVisibility(View.VISIBLE);

    }

    private void testDialog(String message) {
        new android.support.v7.app.AlertDialog.Builder(UsersProfile.this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }



}
