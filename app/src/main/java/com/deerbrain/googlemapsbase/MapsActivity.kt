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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

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


        val tileProvider: TileProvider = object : UrlTileProvider(256, 256) {
            @Synchronized
            override fun getTileUrl(x: Int, y: Int, zoom: Int): URL? {
                Log.e(TAG, "titleProvider")
                Log.e(TAG, x.toString() + ",,,,," + y + "...." + zoom)
                // The moon tile coordinate system is reversed.  This is not normal.
                val reversedY = (1 shl zoom) - y - 1
                val s = String.format(
                    Locale.US,
                    "https://reportallusa.com/dyn/tile.py?map=siteroot/Base_Layers.map&layer=Parcels&mode=tile&tilemode=gmap&tile=8935+12980+15&client=ozEw4rZCd9",
                    zoom,
                    x,
                    reversedY
                )
                var url: URL? = null
                url = try {
                    URL(s)
                } catch (e: MalformedURLException) {
                    throw AssertionError(e)
                }
                Thread(Runnable {
                    var tile = MapCacheTileProvider().getTile(x, y, zoom)

                }).start()
                return url
            }
        }
        mapCacheTileOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(tileProvider))
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
            Log.e(TAG, it?.results?.get(0)?.geom_as_wkt.toString())
            progressDialog.dismiss()
            showDialog(
                it?.results?.get(0)?.owner.toString(),
                it?.results?.get(0)?.mail_address1.toString(),
                it?.results?.get(0)?.acreage_calc.toString(),
                it?.results?.get(0)?.geom_as_wkt.toString()
            )
            //Main Thread
            //hide activity blocker
            //let shape = 2Dshape from Parcel data
            //draw shape on map
            //Present Parcel Detail Fragment
        }
    }

    fun showDialog(Name: String, Address: String, calculated: String, polygone: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.detail_dialog)
        var ownerName = dialog.findViewById<TextView>(R.id.ownerName)
        ownerName.setText(resources.getString(R.string.owner) + " :" + Name)

        var mailingAddress = dialog.findViewById<TextView>(R.id.mailingAddress)
        mailingAddress.setText(resources.getString(R.string.mailingaddress) + " " + Address)

        var calculatedAcres = dialog.findViewById<TextView>(R.id.calculatedAcres)

        calculatedAcres.setText(resources.getString(R.string.calculated_acres) + "  " + calculated)
        var cancel = dialog.findViewById<Button>(R.id.close).setOnClickListener({
            if (this::polyLines.isInitialized) {
                polyLines.remove()
            }
            dialog.dismiss()
        })
        var storeShape = dialog.findViewById<Button>(R.id.storeShapeSize).setOnClickListener({
            var list = mutableListOf<LatLng>()
            var polygoneValue =
                polygone.substring(polygone.indexOf("(((") + 3, polygone.indexOf(")))") - 3)
                    .replace("(", "").replace(")", "")

            var array: List<String> = polygoneValue.split(",")
            for (i in 0 until array.size) {
                var latlngArray: List<String> = array.get(i).split(" ")
                list.add(LatLng(latlngArray[1].toDouble(), latlngArray[0].toDouble()))
            }
            drawPolyGone(list)
            dialog.dismiss()
        })

        dialog.show()
    }

    fun drawPolyGone(list: List<LatLng>) {
        Log.e(TAG, list.toString())
        var rectOption = PolylineOptions().width(5f).color(Color.RED).addAll(list).geodesic(true)
        if (rectOption != null)
            polyLines = mMap.addPolyline(rectOption)


    }

    private fun checkTileExists(x: Int, y: Int, zoom: Int): Boolean {
        val minZoom = 12
        val maxZoom = 16
        return zoom in minZoom..maxZoom
    }
}