package fr.blooddonbeta;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class SearchFragment extends android.app.Fragment
{

    View inputFragmentView;

    GifImageView animatedImage ;


    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
         inputFragmentView = inflater.inflate(R.layout.fragment_search,container, false);

        try {

            belka.us.androidtoggleswitch.widgets.ToggleSwitch toggleSwitch;


            toggleSwitch = inputFragmentView.findViewById(R.id.donationSwitcher);

            toggleSwitch.setCheckedTogglePosition(SearchActivity.userOperation);

            toggleSwitch.setOnToggleSwitchChangeListener(new belka.us.androidtoggleswitch.widgets.ToggleSwitch.OnToggleSwitchChangeListener(){

                @Override
                public void onToggleSwitchChangeListener(int position, boolean isChecked)
                {
                    SearchActivity.userOperation = position;
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        animatedImage =  inputFragmentView.findViewById(R.id.imageView4);


        animatedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.searchanimation);
                    animatedImage.setImageDrawable(gifDrawable);

                    ((SearchActivity) getActivity()).startNewSearch();

                } catch (Resources.NotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });


        return inputFragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }


}
