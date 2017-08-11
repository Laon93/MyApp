package kr.ac.skuniv.myapp.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import kr.ac.skuniv.myapp.R;
import kr.ac.skuniv.myapp.core.domain.User;
import kr.ac.skuniv.myapp.core.exception.JSONResultException;
import kr.ac.skuniv.myapp.core.network.SafeAsyncTask;
import kr.ac.skuniv.myapp.core.provider.UserProvider;

/**
 * Created by cs618 on 2017-07-18.
 */

public class UserListFragment extends ListFragment {
    private UserListArrayAdapter userListArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userListArrayAdapter = new UserListArrayAdapter( getActivity() );
        setListAdapter( userListArrayAdapter );
        return inflater.inflate(
                R.layout.fragment_user_list,
                container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new FetchUserListAsyncTask().execute();
    }

    private class FetchUserListAsyncTask extends SafeAsyncTask<List<User>>{

        @Override
        public List<User> call() throws Exception {
            List<User> users = new UserProvider( getActivity() ).fetchUserList();
            return users;
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            if( e instanceof JSONResultException ) {
                Toast.makeText( getActivity(), e.getMessage(), Toast.LENGTH_LONG ).show();
            } else {
                Log.e("FetchUserListAsyncTask", "error:" + e);
            }
        }

        @Override
        protected void onSuccess(List<User> users) throws Exception {
            super.onSuccess(users);
//            for( User user : users ) {
//                System.out.println( user );
//            }
            userListArrayAdapter.add( users );
            getView().findViewById( R.id.progress ).
                    setVisibility( View.GONE );
        }
    }
}
