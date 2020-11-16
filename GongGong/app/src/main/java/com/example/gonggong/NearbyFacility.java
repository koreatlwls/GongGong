package com.example.gonggong;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.RestrictionsManager;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/*
주변 시설 프래그먼트
 */
public class NearbyFacility extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {
    private Context mContext;
    private ExtendedFloatingActionButton fab_main,fab_sub1,fab_sub2,fab_sub3;
    private FloatingActionButton fab_myLocation;
    private boolean isFabOpen=false; //Floating ActionButton의 상태
    public static final int freeFood=3,welfare=2,conStore=1;
    private static int showWhat=0; //현재 표시하고 있는 시설
    private static int colorOrange;
    private static int colorLightSkyBlue;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Location mCurrentLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private boolean mLocationPermissionGranted;
    private static final int GPS_ENABLE_REQUEST_CODE=2001;
    private static final int UPDATE_INTERVAL_MS= 1000 * 180;
    private static final int FASTEST_UPDATE_INTERVAL_MS=1000*120;
    private static final String KEY_CAMERA_POSITION="camera_position";
    private static final String KEY_LOCATION="location";
    private Location myLocation=null;
    private MapView mapView=null;
    private Marker currentMarker=null;
    private static GoogleMap map;
    private static NodeList constoreList;
    private static NodeList welfareList;
    private static NodeList freefoodList;
    private static boolean isClickedMyLocation=false;
    private static boolean isLoadingData=false;
    private String addressString="대한민국 충청북도";
    OpenApi openapi;

    public NearbyFacility() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        colorOrange=getResources().getColor(R.color.colorOrange);
        colorLightSkyBlue=getResources().getColor(R.color.colorLightSkyBlue);
    }
    private void ConStoreData() throws ExecutionException, InterruptedException {
        String ServiceKey = "kHfFtRQnsh8Dm3LJi8a82MF%2F5vsGDD%2BZQHrmRfLqPs%2F6MHeISttv1xd%2Bz%2Bs3ShfRYYBITs6aBtPLgRneYSoPHw%3D%3D";
        String constoreurl = "http://api.data.go.kr/openapi/tn_pubr_public_chil_wlfare_mlsv_api?serviceKey="+ServiceKey+"&pageNo=0&numOfRows=500&type=xml&ctprvnNm="+addressString;
        openapi = new OpenApi(constoreurl);
        openapi.execute();
        constoreList = openapi.get();
    }
    private void WelFareData() throws ExecutionException, InterruptedException {
        String ServiceKey = "kHfFtRQnsh8Dm3LJi8a82MF%2F5vsGDD%2BZQHrmRfLqPs%2F6MHeISttv1xd%2Bz%2Bs3ShfRYYBITs6aBtPLgRneYSoPHw%3D%3D";
        String welfareurl = "http://api.data.go.kr/openapi/tn_pubr_public_oldnddspsnprt_carea_api?serviceKey="+ServiceKey+"&pageNo=0&numOfRows=500&type=xml&ctprvnNm="+addressString;
        openapi = new OpenApi(welfareurl);
        openapi.execute();
        welfareList = openapi.get();
    }
    private void FreeFoodData() throws ExecutionException, InterruptedException {
        String ServiceKey = "kHfFtRQnsh8Dm3LJi8a82MF%2F5vsGDD%2BZQHrmRfLqPs%2F6MHeISttv1xd%2Bz%2Bs3ShfRYYBITs6aBtPLgRneYSoPHw%3D%3D";
        String freefoodurl = "http://api.data.go.kr/openapi/tn_pubr_public_free_mlsv_api?serviceKey="+ServiceKey+"&pageNo=0&numOfRows=500&type=xml";
        openapi = new OpenApi(freefoodurl);
        openapi.execute();
        freefoodList = openapi.get();
        for (int temp = 0; temp <500; temp++){
            Node nNode = freefoodList.item(temp);
            if(nNode.getNodeType()==Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;
                //Log.d("태그","가맹정명:"+getTagValue("fcltyNm",eElement));
            }
        }
    }
    private String getTagValue(String tag, Element eElement){
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            mCurrentLocation=savedInstanceState.getParcelable(KEY_LOCATION);
            CameraPosition mCameraPosition=savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        View v=inflater.inflate(R.layout.fragment_nearby_facility,container,false);
        fab_main=v.findViewById(R.id.fab_Main);
        fab_sub1=v.findViewById(R.id.fab_Sub1);
        fab_sub2=v.findViewById(R.id.fab_Sub2);
        fab_sub3=v.findViewById(R.id.fab_Sub3);
        fab_myLocation=v.findViewById(R.id.btnMyLocation);
        mapView=(MapView)v.findViewById(R.id.map);
        mapView.getMapAsync(this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(mapView!=null)
            mapView.onCreate(savedInstanceState);
        locationRequest=new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(mContext);
        fab_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        fab_sub1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               onFabClicked(R.id.fab_Sub1);
            }
        });
        fab_sub2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onFabClicked(R.id.fab_Sub2);
            }
        });
        fab_sub3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onFabClicked(R.id.fab_Sub3);
            }
        });
        fab_myLocation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(myLocation!=null) {
                    setCurrentLocation(myLocation, "현재 위치", "");
                    addressString = getCurrentAddress(myLocation.getLatitude(), myLocation.getLongitude());
                    isClickedMyLocation=true;
                }
            }
        });


    }
    //Floating Action Button을 터치 시 서브 버튼들을 보여주거나 다시 숨긴다.
    private void toggleFab(){
        if(isFabOpen){
            ObjectAnimator.ofFloat(fab_sub1,"translationY",0f).start();
            ObjectAnimator.ofFloat(fab_sub2,"translationY",0f).start();
            ObjectAnimator.ofFloat(fab_sub3,"translationY",0f).start();
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            isFabOpen = false;
        }
        else{
            ObjectAnimator.ofFloat(fab_sub1, "translationY", +210f).start();
            ObjectAnimator.ofFloat(fab_sub2, "translationY", +420f).start();
            ObjectAnimator.ofFloat(fab_sub3,"translationY",+630f).start();
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            isFabOpen = true;
        }
    }
    private void onFabClicked(int id){
        if(!isClickedMyLocation){
            Toast.makeText(mContext,"현재 위치 버튼을 눌러주세요!",Toast.LENGTH_LONG).show();
            return;
        }
        if(!isLoadingData){
            try {
                String[] addressList=addressString.split(" ");
                addressString=addressList[1];
                ConStoreData();
                WelFareData();
                FreeFoodData();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            isLoadingData=true;
        }
        fab_main.setBackgroundColor(colorOrange);
        switch(id){
            case R.id.fab_Sub1:
                if(showWhat!=conStore) {
                    fab_main.setText("카드가맹점");
                    fab_main.setIcon(mContext.getDrawable(R.drawable.ic_store_white_24dp));
                    showWhat=conStore;
                    fab_sub1.setBackgroundColor(colorOrange);
                    fab_sub2.setBackgroundColor(colorLightSkyBlue);
                    fab_sub3.setBackgroundColor(colorLightSkyBlue);
                    toggleFab();
                    showConStore();
                }
                break;
            case R.id.fab_Sub2:
                if(showWhat!=welfare) {
                    fab_main.setText("복지 센터");
                    fab_main.setIcon(mContext.getDrawable(R.drawable.ic_accessible_24px));
                    showWhat=welfare;
                    fab_sub1.setBackgroundColor(colorLightSkyBlue);
                    fab_sub2.setBackgroundColor(colorOrange);
                    fab_sub3.setBackgroundColor(colorLightSkyBlue);
                    toggleFab();
                    showWelFare();
                }
                break;
            case R.id.fab_Sub3:
                if(showWhat!=freeFood) {
                    fab_main.setText("무료급식소");
                    fab_main.setIcon(mContext.getDrawable(R.drawable.ic_local_dining_24px));
                    showWhat=freeFood;
                    fab_sub1.setBackgroundColor(colorLightSkyBlue);
                    fab_sub2.setBackgroundColor(colorLightSkyBlue);
                    fab_sub3.setBackgroundColor(colorOrange);
                    toggleFab();
                    showFreeFood();
                }
                break;
        }
    }
    public void showConStore(){
        MarkerOptions markerOptions = new MarkerOptions();
        map.clear();
        for(int i=0;i<constoreList.getLength();i++) {
            Node nNode = constoreList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String storeName=getTagValue("mrhstNm",eElement);
                double latitude=Double.parseDouble(getTagValue("latitude",eElement));
                double longitude=Double.parseDouble(getTagValue("longitude",eElement));
                String rdnmadr=getTagValue("rdnmadr",eElement);
                LatLng latlng=new LatLng(latitude,longitude);
                markerOptions.position(latlng);
                markerOptions.title(storeName);
                markerOptions.snippet(rdnmadr);
                map.addMarker(markerOptions);
            }
        }
    }
    public void showFreeFood(){
        MarkerOptions markerOptions = new MarkerOptions();
        map.clear();
        for(int i=0;i<500;i++) {
            Node nNode = freefoodList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String storeName ="";
                double latitude =0;
                double longitude =0;
                try {
                    storeName=getTagValue("fcltyNm",eElement);
                    latitude=Double.parseDouble(getTagValue("latitude",eElement));
                    longitude=Double.parseDouble(getTagValue("longitude",eElement));
                    String rdnMadr=getTagValue("rdnmadr",eElement);
                    LatLng latlng=new LatLng(latitude,longitude);
                    markerOptions.position(latlng);
                    markerOptions.title(storeName);
                    markerOptions.snippet(rdnMadr);
                    map.addMarker(markerOptions);
                } catch (NullPointerException e){
                    storeName = null;
                }
            }
        }
    }
    public void showWelFare(){
        map.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        for(int i=0;i<welfareList.getLength();i++) {
            Node nNode = welfareList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String storeName=getTagValue("trgetFcltyNm",eElement);
                double latitude=Double.parseDouble(getTagValue("latitude",eElement));
                double longitude=Double.parseDouble(getTagValue("longitude",eElement));
                String rdnmadr=getTagValue("rdnmadr",eElement);
                LatLng latlng=new LatLng(latitude,longitude);
                markerOptions.position(latlng);
                markerOptions.title(storeName);
                markerOptions.snippet(rdnmadr);
                map.addMarker(markerOptions);
            }
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());
        map=googleMap;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.141233, 126.925594), 10);
        googleMap.animateCamera(cameraUpdate);
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        map.setOnMarkerClickListener(this);
    }
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                mCurrentLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(mContext, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(mContext, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(mContext, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        Toast.makeText(mContext,address.getAddressLine(0).toString(),Toast.LENGTH_LONG).show();
        return address.getAddressLine(0).toString()+"\n";

    }
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                 myLocation = locationList.get(locationList.size() - 1);

                //LatLng currentPosition= new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                //setCurrentLocation(myLocation, "현재 위치", "markerSnippet");setCurrentLocation(myLocation, "현재 위치", "markerSnippet");
                mCurrentLocation = myLocation;
            }
        }
    };
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng,10f);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(map!=null){
            outState.putParcelable(KEY_CAMERA_POSITION,map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION,mCurrentLocation);
        }
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        switch(showWhat){
            case conStore:
                BottomSheetDialog bottomSheetDialog1=new BottomSheetDialog(marker.getTitle(),marker.getSnippet(),conStore);
                bottomSheetDialog1.show(getFragmentManager(),"bottomSheet");
                break;
            case freeFood:
                BottomSheetDialog bottomSheetDialog2=new BottomSheetDialog(marker.getTitle(),marker.getSnippet(),freeFood);
                bottomSheetDialog2.show(getFragmentManager(),"bottomSheet");
                break;
            case welfare:
                BottomSheetDialog bottomSheetDialog3=new BottomSheetDialog(marker.getTitle(),marker.getSnippet(),welfare);
                bottomSheetDialog3.show(getFragmentManager(),"bottomSheet");
                break;
        }


        return true;
    }
}