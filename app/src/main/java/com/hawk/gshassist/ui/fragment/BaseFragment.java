package com.hawk.gshassist.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hawk.gshassist.ui.MainActivity;

import java.util.Map;

public abstract class BaseFragment extends Fragment {
    public OnFragmentInteractionListener mListener;
    private MainActivity mMainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mMainActivity= (MainActivity) getContext();
    }

    public MainActivity getMainActivity(){
        return mMainActivity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void switchToFragment(String fragmentTag, Bundle arguments,boolean removeFormer);
        void setCookies(Map cookies,int flag);
        Map getCookies(int flag);
        void showProgress(String title,String msg);
        void dismissProgress();
        void setTitleForFragment(String title);
    }

    public abstract void refreshFragment();
}
