package com.example.matheus.dataglove;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.rajawali3d.view.ISurface;
import org.rajawali3d.view.SurfaceView;
import org.w3c.dom.Text;

public class FirstTab extends Fragment {
    SurfaceView surface;

    public FirstTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout mLayout = (FrameLayout) inflater.inflate(R.layout.first_tab, container, false);
        surface = mLayout.findViewById(R.id.rajwali_surface);
        surface.setFrameRate(60.0);
        surface.setRenderMode(ISurface.RENDERMODE_WHEN_DIRTY);
        RajawaliRenderer renderer = new RajawaliRenderer(getContext(), surface);
        surface.setSurfaceRenderer(renderer);
        ((MainActivity) getActivity()).setRenderer(renderer);
        return mLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
