package com.deerbrain.googlemapsbase

import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.deerbrain.googlemapsbase.MapCache.MapCacheManager
import com.deerbrain.googlemapsbase.MapCache.MapCacheTileProvider
import com.deerbrain.googlemapsbase.Parcel.ParcelCacheDummy
import com.deerbrain.googlemapsbase.Parcel.ParcelCacheTileProvider
import com.deerbrain.googlemapsbase.Parcel.ParcelDataManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.net.MalformedURLException
import java.net.URL
import java.util.*


enum class ActualSystemState { initial, mapTiles, parcelTiles, parcelData }

private const val TAG = "MapsActivity"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener,
    DrawPolyLines {

    lateinit var mMap: GoogleMap
    lateinit var progressDialog: ProgressDialog


    var currentSystemState: ActualSystemState = ActualSystemState.initial


    var casheTileLayerShown: Boolean = false
    var parcelTileLayerShown: Boolean = false
    var mapCacheTileOverlay: TileOverlay? = null
    lateinit var polyLines: Polyline


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle(resources.getString(R.string.pleaseWait))
        progressDialog.setMessage(resources.getString(R.string.pleaseWaitDescription))
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(34.0, -90.0)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(34.0, -90.0), 15f))
        /*  mMap.addMarker(MarkerOptions().position(sydney).title("Marker"))*/
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setOnMapClickListener(this)



        mapCacheTileOverlay =
            mMap.addTileOverlay(TileOverlayOptions().tileProvider(ParcelCacheTileProvider()))
        // mapCacheTileOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(MapCacheTileProvider()))
    }

    override fun onMapClick(coordinate: LatLng?) {
        Log.i("MapClick", "onMapClick")
        if (coordinate != null) {
            didTapParcelData(coordinate)
        }
        /* if (currentSystemState == ActualSystemState.parcelData) {
             Log.i("Maps", "Parcel Data Tap")

         }*/
    }

    fun setSystemState(state: ActualSystemState) {

        currentSystemState = state
        when (state) {
            ActualSystemState.initial -> {
                // hideFrament(parcelTap)
            }
            ActualSystemState.mapTiles -> {
                //hideFragment(parcelTap)
                casheTileLayerShown = true
                parcelTileLayerShown = false
                mapCacheTileOverlay = mMap.addTileOverlay(
                    TileOverlayOptions().tileProvider(
                        MapCacheTileProvider()
                    )
                )
            }
            ActualSystemState.parcelTiles -> {
                //hideFragment(parcelTap)
                casheTileLayerShown = false
                parcelTileLayerShown = true
                mapCacheTileOverlay = mMap.addTileOverlay(
                    TileOverlayOptions().tileProvider(
                        ParcelCacheTileProvider()
                    )
                )
            }

            ActualSystemState.parcelData -> {
                //showFragment(parcelTap)
            }

        }
    }

    fun didTapParcelData(coordinate: LatLng) {
        progressDialog.show()
        //Show an activity Blocker with spinning wheel and "loading Data"
        ParcelDataManager.fetchParcelData(coordinate) {
            //thisis a completion block
            progressDialog.dismiss()

            DetailInfoFragment.newInstance(it!!,this).show(supportFragmentManager, "")

            //Main Thread
            //hide activity blocker
            //let shape = 2Dshape from Parcel data
            //draw shape on map
            //Present Parcel Detail Fragment
        }
    }




    override fun drawPolyLine(list: List<LatLng>) {
        Log.e(TAG, "drawPolyLine")
        var rectOption = PolylineOptions().width(5f).color(Color.RED).addAll(list).geodesic(true)
        if (rectOption != null)
            polyLines = mMap.addPolyline(rectOption)
    }

    override fun removePolyLine() {
        Log.e(TAG, "removePolyLine")
        if (this::polyLines.isInitialized) {
            polyLines.remove()
        }
    }
}