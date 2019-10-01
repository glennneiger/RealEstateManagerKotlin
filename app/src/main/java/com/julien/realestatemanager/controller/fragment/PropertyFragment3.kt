package com.julien.realestatemanager.controller.fragment


import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController

import com.julien.realestatemanager.R
import com.julien.realestatemanager.Utils
import com.julien.realestatemanager.controller.activity.PropertyActivity
import com.julien.realestatemanager.models.PropertyViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_new_property_fragment3.*
import kotlinx.android.synthetic.main.fragment_new_property_fragment3.backArrow

/**
 * fragment create or edit property part 3
 * adress information
 */
class PropertyFragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_property_fragment3, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val propertyActivity: PropertyActivity = activity as PropertyActivity


        if (!propertyActivity.intent.getBooleanExtra("isNewProperty",true)){

            loadProperty(propertyActivity)
        }


        PushDownAnim.setPushDownAnimTo(nextToD).setScale(PushDownAnim.MODE_STATIC_DP,5F).setOnClickListener {


            if (validateForm()){
                save(propertyActivity)

                val fullAdress = propertyActivity.adress + " " + propertyActivity.additionAdress + " " + propertyActivity.city + " " + propertyActivity.postalCode + " " + propertyActivity.country

                if (Utils.isInternetAvailable(context)){
                    var geocoder= Geocoder(context)
                    var listAdress:List<Address> = geocoder.getFromLocationName(fullAdress,1)

                    if (listAdress.size>0) {
                        propertyActivity.latitude = listAdress[0].latitude
                        propertyActivity.longitude = listAdress[0].longitude
                        view.findNavController().navigate(R.id.fragmentCtoD)
                        //activity?.findViewById<Stepper>(R.id.Stepper)?.forward()

                    }else{
                        propertyActivity.latitude = 0.0
                        propertyActivity.longitude = 0.0
                        alertDialogWrongAdress()

                    }
                }else{
                    propertyActivity.latitude = 0.0
                    propertyActivity.longitude = 0.0
                    Toast.makeText(context,getString(R.string.no_connection_check_adress),Toast.LENGTH_SHORT).show()
                    view.findNavController().navigate(R.id.fragmentCtoD)
                }

            }





        }

        PushDownAnim.setPushDownAnimTo(backArrow).setScale(PushDownAnim.MODE_STATIC_DP,5F).setOnClickListener {
            view.findNavController().popBackStack()
            //activity?.findViewById<Stepper>(R.id.Stepper)?.back()
        }
    }
    fun alertDialogWrongAdress(){
        val builder = AlertDialog.Builder(context)

        builder.setTitle(getString(R.string.Address_not_found))

        builder.setMessage(getString(R.string.change_adress))

        builder.setPositiveButton(getString(R.string.yes)){ dialog, which ->

        }

        builder.setNegativeButton(getString(R.string.no)){ dialog, which ->
            view!!.findNavController().navigate(R.id.fragmentCtoD)
            //activity?.findViewById<Stepper>(R.id.Stepper)?.forward()
        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun save(propertyActivity: PropertyActivity){

        propertyActivity.adress = edit_adress_fragment_3.text.toString()
        propertyActivity.additionAdress = edit_additional_adress_fragment_3.text.toString()
        propertyActivity.country = edit_country_fragment_3.text.toString()
        propertyActivity.postalCode = edit_postal_code_fragment_3.text.toString()
        propertyActivity.city = edit_city_fragment_3.text.toString()
        propertyActivity.placeNearby = edit_place_nearby_fragment_3.text.toString()

    }

    private fun validateForm():Boolean{
        if (edit_adress_fragment_3.text.toString().trim() != "" &&
            edit_city_fragment_3.text.toString().trim() != "" &&
            edit_postal_code_fragment_3.text.toString().trim() != "" &&
            edit_postal_code_fragment_3.text.toString().trim() != ""){
            return true
        }else{
            if(edit_adress_fragment_3.text.toString().trim() == ""){
                edit_adress_fragment_3.error = getString(R.string.field_cannot_be_blank)
            }
            if(edit_city_fragment_3.text.toString().trim() == ""){
                edit_city_fragment_3.error = getString(R.string.field_cannot_be_blank)
            }
            if(edit_postal_code_fragment_3.text.toString().trim() == "" ){
                edit_postal_code_fragment_3.error = getString(R.string.field_cannot_be_blank)
            }
            if (edit_country_fragment_3.text.toString().trim() == ""){
                edit_country_fragment_3.error = getString(R.string.field_cannot_be_blank)
            }

            return false
        }
    }

    private fun loadProperty(propertyActivity: PropertyActivity){

        val propertyViewModel = ViewModelProviders.of(this).get(PropertyViewModel::class.java)



        propertyViewModel.getProperty(propertyActivity.intent.getStringExtra("id")).observe(this, Observer { property ->
            // Update the cached copy of the words in the adapter.
            property?.let {

                edit_adress_fragment_3.setText(property.adress)
                edit_additional_adress_fragment_3.setText(property.additionalAdress)
                edit_country_fragment_3.setText(property.country)
                edit_postal_code_fragment_3.setText(property.postalCode)
                edit_city_fragment_3.setText(property.city)
                edit_place_nearby_fragment_3.setText(property.placeNearby)

            }
        })

    }
}
