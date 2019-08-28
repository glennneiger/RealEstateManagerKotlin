package com.julien.realestatemanager.controller.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.julien.realestatemanager.R
import com.julien.realestatemanager.models.Property
import com.julien.realestatemanager.models.PropertyViewModel
import kotlinx.android.synthetic.main.fragment_property_detail.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.google.android.gms.maps.MapView
import com.squareup.picasso.Picasso


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PropertyDetailFragment : androidx.fragment.app.Fragment(),OnMapReadyCallback {


    private lateinit var propertyViewModel: PropertyViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private var adress ="France"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_property_detail, container, false)
        mMapView = rootView.findViewById(R.id.mapView) as MapView
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume()
        //mapView = childFragmentManager.findFragmentById(R.id.mapView)

        mMapView.getMapAsync(this)

        return rootView
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        propertyViewModel = ViewModelProviders.of(this).get(PropertyViewModel::class.java)


        if (tag != null) {




            propertyViewModel.getProperty(tag!!.toString()).observe(this, Observer { property ->
                // Update the cached copy of the words in the adapter.
                property?.let {

                    description_text_view.text = property.description
                    area_text_view.text = property.area
                    number_of_rooms_text_view.text = property.numberOfRooms
                    bedrooms_text_view.text = property.numberOfBedrooms
                    bathrooms_text_view.text = property.numberOfBathrooms
                    adress_text_view.text = property.adress
                    additional_adress_text_view.text = property.additionalAdress
                    city_text_view.text = property.city
                    postal_code_text_view.text = property.postalCode
                    country_text_view.text = property.country

                    updateMap(property.latitude,property.longitude)
                }


            })

            propertyViewModel.getMedia(tag!!.toString()).observe(this, Observer { media ->
                // Update the cached copy of the words in the adapter.
                media?.let {
                    //Log.e("size media", media.size.toString())

                    for (photo in media) {

                        var image = ImageView(context)
                        Picasso.get().load(Uri.parse(photo.photo)).resize(200,200).into(image)
                        Log.e("photo", photo.photo)
                        fragment_property_detail_media.addView(image)
                    }

                }


            }
            )


        }


    }
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        // Add a marker in Sydney and move the camera


    }

    fun updateMap(latitude:Double,longitude:Double){

        if (latitude == 0.0 && longitude == 0.0){
            Toast.makeText(context,"pas d'adresse valide",Toast.LENGTH_SHORT).show()
        }else{
            val location = LatLng(latitude,longitude)
            mMap.addMarker(MarkerOptions().position(location).title("Marker"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        }


    }


}
