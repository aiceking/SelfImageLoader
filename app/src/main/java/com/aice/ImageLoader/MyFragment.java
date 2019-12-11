package com.aice.ImageLoader;

import android.util.Log;

import androidx.fragment.app.Fragment;

public class MyFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("xixi=",        getContext()+"onDestroy");
    }
}
