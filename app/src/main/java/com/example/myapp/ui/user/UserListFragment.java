package com.example.myapp.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapp.R;
import com.example.myapp.core.domain.User;
import com.example.myapp.core.exeption.JSONResultException;
import com.example.myapp.core.provider.UserProvider;
import com.example.myapp.network.SafeAsyncTask;

import java.util.List;

/**
 * Created by kicks on 2017-07-18.
 */

public class UserListFragment extends ListFragment {
    private UserListArrayAdapter userListArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userListArrayAdapter = new UserListArrayAdapter( getActivity() );
        setListAdapter( userListArrayAdapter  );

        return inflater.inflate( R.layout.fragment_user_list, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new FetchUserListAsyncTask().execute();
    }

    private class FetchUserListAsyncTask extends SafeAsyncTask<List<User>> {
        @Override
        public List<User> call() throws Exception {
            List<User> users = new UserProvider( getActivity() ).fetchUserList();
            return users;
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            if( e instanceof JSONResultException) {
                Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_SHORT ).show();
            } else {
            }
        }

        @Override
        protected void onSuccess(List<User> users) throws Exception {
            super.onSuccess(users);


            userListArrayAdapter.add( users );
            getActivity().findViewById( R.id.progress ).setVisibility( View.GONE );
        }
    }
}
