package com.example.openstreetmap;

import android.widget.TextView;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class CustomInfoWindow extends InfoWindow {

    public CustomInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }

    @Override
    public void onOpen(Object item) {
        Marker marker = (Marker) item;
        TextView titleTextView = mView.findViewById(R.id.tv_title);
        TextView snippetTextView = mView.findViewById(R.id.tv_snippet);
        titleTextView.setText(marker.getTitle());
        snippetTextView.setText(marker.getSnippet());
    }

    @Override
    public void onClose() {
    }
}