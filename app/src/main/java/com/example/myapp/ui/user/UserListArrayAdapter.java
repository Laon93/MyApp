package com.example.myapp.ui.user;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.R;
import com.example.myapp.core.domain.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by kicks on 2017-07-18.
 */

public class UserListArrayAdapter extends ArrayAdapter<User> {
    private LayoutInflater layoutInflater;
    private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
            // .showImageOnLoading( R.drawable.ic_default_profile )// resource or drawable
            .showImageForEmptyUri( R.drawable.ic_default_profile )// resource or drawable
            .showImageOnFail( R.drawable.ic_default_profile )// resource or drawable
            //.resetViewBeforeLoading( false )// default
            .delayBeforeLoading( 0 )
            //.cacheInMemory( false )// default
            .cacheOnDisc( true )// false is default
            //.preProcessor(...)
            //.postProcessor(...)
            //.extraForDownloader(...)
            //.considerExifParams( false )// default
            //.imageScaleType( ImageScaleType.IN_SAMPLE_POWER_OF_2 )// default
            //.bitmapConfig( Bitmap.Config.ARGB_8888 )// default
            //.decodingOptions(...)
            //.displayer( new SimpleBitmapDisplayer() )// default
            //.handler( new Handler() )// default
            .build();

    public UserListArrayAdapter(@NonNull Context context) {
        super( context, R.layout.row_user_list );
        layoutInflater = LayoutInflater.from( context );
    }

    public void add( List<User> list ) {
        if( list == null ) {
            return;
        }

        for( User user : list ) {
            add( user );
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {

        View view = convertView;

        if( view == null ) {
            view = layoutInflater.inflate( R.layout.row_user_list, parent, false );
        }

        User user = getItem( position );

        ImageLoader.getInstance().displayImage( user.getProfile(), (ImageView)view.findViewById( R.id.profile ), displayImageOptions );
        ( (TextView) view.findViewById( R.id.name ) ).setText( user.getName() );

        return view;
    }
}
