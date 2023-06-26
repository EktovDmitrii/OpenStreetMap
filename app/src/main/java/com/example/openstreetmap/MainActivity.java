package com.example.openstreetmap;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private MapView mapView;
    private LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Marker currentLocationMarker;
    private Marker firstMarker;
    private ImageView myLocationButton;
    private ImageView zoomInButton;
    private ImageView zoomOutButton;
    List<CustomInfoWindow> infoWindows = new ArrayList<>();

    GeoPoint baku = new GeoPoint(40.36656047865317, 49.83455969370569);
    GeoPoint pointOne = new GeoPoint(40.36656047865317, 49.83455969370569);
    GeoPoint pointTwo = new GeoPoint(40.36668714767159, 49.83568782339009);
    GeoPoint pointThree = new GeoPoint(40.365997251028105, 49.834016410199794);
    GeoPoint currentLocation = new GeoPoint(0.0, 0.0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration.getInstance().load(getApplicationContext(), getPreferences(MODE_PRIVATE));
        setContentView(R.layout.activity_main);
        mapSettings();
        initButtons();
        setAllBinds();
        addMarkersOne();
        addMarkerTwo();
        addMarkerThree();
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2f;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void addMarkersOne() {
        Drawable blueMarkerDrawable = ContextCompat.getDrawable(this, R.mipmap.ic_tracker_75dp);
        Drawable innerDrawableOne = ContextCompat.getDrawable(this, R.drawable.person_one);

        int markerWidth = blueMarkerDrawable.getIntrinsicWidth();
        int markerHeight = blueMarkerDrawable.getIntrinsicHeight();
        Bitmap combinedBitmap = Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);
        blueMarkerDrawable.setBounds(0, 0, markerWidth, markerHeight);
        blueMarkerDrawable.draw(canvas);
        int innerSize = Math.min(markerWidth, markerHeight) - dpToPx(25);
        int innerLeft = (markerWidth - innerSize) / 2;
        int innerTop = (markerHeight - innerSize) / 2 - dpToPx(5);
        Bitmap innerBitmap = Bitmap.createBitmap(innerSize, innerSize, Bitmap.Config.ARGB_8888);
        Canvas innerCanvas = new Canvas(innerBitmap);
        innerDrawableOne.setBounds(0, 0, innerSize, innerSize);
        innerDrawableOne.draw(innerCanvas);
        Bitmap roundedInnerBitmap = getCircularBitmap(innerBitmap);
        canvas.drawBitmap(roundedInnerBitmap, innerLeft, innerTop, null);
        Drawable combinedDrawableOne = new BitmapDrawable(getResources(), combinedBitmap);

        OverlayItem overlayItemOne = new OverlayItem("Илья", "GPS, 14:00", pointOne);
        firstMarker = new Marker(mapView);
        firstMarker.setIcon(combinedDrawableOne);
        firstMarker.setTitle(overlayItemOne.getTitle());
        firstMarker.setSnippet(overlayItemOne.getSnippet());
        firstMarker.setPosition(pointOne);
        CustomInfoWindow infoWindow = new CustomInfoWindow(R.layout.custom_marker_info_window, mapView);
        firstMarker.setInfoWindow(infoWindow);
        infoWindows.add(infoWindow);
        firstMarker.setInfoWindowAnchor(1.45f, 1.35f);
        mapView.getOverlayManager().add(firstMarker);
        mapView.invalidate();
        firstMarker.showInfoWindow();
        firstMarker.setOnMarkerClickListener((marker, mapView) -> {
            firstMarker.setInfoWindow(infoWindow);
            BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(innerDrawableOne, marker.getTitle(), marker.getSnippet().substring(5), "02.10.95");
            bottomSheetFragment.show(getSupportFragmentManager(), "bottom_sheet_fragment");
            return true;
        });
    }

    private void addMarkerTwo() {
        Drawable blueMarkerDrawable = ContextCompat.getDrawable(this, R.mipmap.ic_tracker_75dp);
        Drawable innerDrawableTwo = ContextCompat.getDrawable(this, R.drawable.person_two);

        int markerWidth = blueMarkerDrawable.getIntrinsicWidth();
        int markerHeight = blueMarkerDrawable.getIntrinsicHeight();
        Bitmap combinedBitmap = Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);
        blueMarkerDrawable.setBounds(0, 0, markerWidth, markerHeight);
        blueMarkerDrawable.draw(canvas);
        int innerSize = Math.min(markerWidth, markerHeight) - dpToPx(25);
        int innerLeft = (markerWidth - innerSize) / 2;
        int innerTop = (markerHeight - innerSize) / 2 - dpToPx(5);
        Bitmap innerBitmap = Bitmap.createBitmap(innerSize, innerSize, Bitmap.Config.ARGB_8888);
        Canvas innerCanvas = new Canvas(innerBitmap);
        innerDrawableTwo.setBounds(0, 0, innerSize, innerSize);
        innerDrawableTwo.draw(innerCanvas);
        Bitmap roundedInnerBitmap = getCircularBitmap(innerBitmap);
        canvas.drawBitmap(roundedInnerBitmap, innerLeft, innerTop, null);
        Drawable combinedDrawableTwo = new BitmapDrawable(getResources(), combinedBitmap);

        OverlayItem overlayItemTwo = new OverlayItem("Валера", "GPS, 16:00", pointTwo);
        firstMarker = new Marker(mapView);
        firstMarker.setIcon(combinedDrawableTwo);
        firstMarker.setTitle(overlayItemTwo.getTitle());
        firstMarker.setSnippet(overlayItemTwo.getSnippet());
        firstMarker.setPosition(pointTwo);
        CustomInfoWindow infoWindow = new CustomInfoWindow(R.layout.custom_marker_info_window, mapView);
        firstMarker.setInfoWindow(infoWindow);
        infoWindows.add(infoWindow);
        firstMarker.setInfoWindowAnchor(1.45f, 1.35f);
        mapView.getOverlayManager().add(firstMarker);
        mapView.invalidate();
        firstMarker.showInfoWindow();
        firstMarker.setOnMarkerClickListener((marker, mapView) -> {
            BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(innerDrawableTwo, marker.getTitle(), marker.getSnippet().substring(5), "26.07.03");
            bottomSheetFragment.show(getSupportFragmentManager(), "bottom_sheet_fragment");
            return true;
        });
    }

    private void addMarkerThree() {
        Drawable blueMarkerDrawable = ContextCompat.getDrawable(this, R.mipmap.ic_tracker_75dp);
        Drawable innerDrawableThree = ContextCompat.getDrawable(this, R.drawable.person_three);

        int markerWidth = blueMarkerDrawable.getIntrinsicWidth();
        int markerHeight = blueMarkerDrawable.getIntrinsicHeight();
        Bitmap combinedBitmap = Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);
        blueMarkerDrawable.setBounds(0, 0, markerWidth, markerHeight);
        blueMarkerDrawable.draw(canvas);
        int innerSize = Math.min(markerWidth, markerHeight) - dpToPx(25);
        int innerLeft = (markerWidth - innerSize) / 2;
        int innerTop = (markerHeight - innerSize) / 2 - dpToPx(5);
        Bitmap innerBitmap = Bitmap.createBitmap(innerSize, innerSize, Bitmap.Config.ARGB_8888);
        Canvas innerCanvas = new Canvas(innerBitmap);
        innerDrawableThree.setBounds(0, 0, innerSize, innerSize);
        innerDrawableThree.draw(innerCanvas);
        Bitmap roundedInnerBitmap = getCircularBitmap(innerBitmap);
        canvas.drawBitmap(roundedInnerBitmap, innerLeft, innerTop, null);
        Drawable combinedDrawableThree = new BitmapDrawable(getResources(), combinedBitmap);
        OverlayItem overlayItemThree = new OverlayItem("Костя", "GPS, 10:00", pointThree);
        firstMarker = new Marker(mapView);
        firstMarker.setIcon(combinedDrawableThree);
        firstMarker.setTitle(overlayItemThree.getTitle());
        firstMarker.setSnippet(overlayItemThree.getSnippet());
        firstMarker.setPosition(pointThree);
        CustomInfoWindow infoWindow = new CustomInfoWindow(R.layout.custom_marker_info_window, mapView);
        firstMarker.setInfoWindow(infoWindow);
        infoWindows.add(infoWindow);
        firstMarker.setInfoWindowAnchor(1.45f, 1.35f);
        mapView.getOverlayManager().add(firstMarker);
        mapView.invalidate();
        firstMarker.showInfoWindow();
        firstMarker.setOnMarkerClickListener((marker, mapView) -> {
            BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(innerDrawableThree, marker.getTitle(), marker.getSnippet().substring(5), "15.05.99");
            bottomSheetFragment.show(getSupportFragmentManager(), "bottom_sheet_fragment");
            return true;
        });
    }


    private void initButtons() {
        myLocationButton = findViewById(R.id.iv_my_location);
        zoomInButton = findViewById(R.id.iv_zoom_in);
        zoomOutButton = findViewById(R.id.iv_zoom_out);
    }

    private void mapSettings() {
        mapView = findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mapView.setBuiltInZoomControls(false);
        mapView.setMultiTouchControls(true);
        mapView.getController().animateTo(baku);
        mapView.getController().setZoom(18.0);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void setAllBinds() {
        myLocationButton.setOnClickListener(v -> {
            mapView.getController().animateTo(currentLocation);
            mapView.getController().setZoom(18.0);
        });
        zoomInButton.setOnClickListener(v -> mapView.getController().zoomIn());
        zoomOutButton.setOnClickListener(v -> mapView.getController().zoomOut());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        requestLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDetach();
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    private void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentLocation = new GeoPoint(latitude, longitude);

        if (currentLocationMarker != null) {
            mapView.getOverlayManager().remove(currentLocationMarker);
        }

        Drawable markerIcon = ContextCompat.getDrawable(this, R.mipmap.ic_my_tracker_46dp);

        currentLocationMarker = new Marker(mapView);
        currentLocationMarker.setIcon(markerIcon);
        currentLocationMarker.setInfoWindow(null);
        currentLocationMarker.setPosition(currentLocation);
        mapView.getOverlayManager().add(currentLocationMarker);
        mapView.invalidate();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}