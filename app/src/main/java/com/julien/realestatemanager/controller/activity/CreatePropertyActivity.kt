package com.julien.realestatemanager.controller.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.julien.realestatemanager.R
import com.julien.realestatemanager.models.Media
import com.julien.realestatemanager.models.Property
import com.julien.realestatemanager.models.PropertyViewModel
import com.tayfuncesur.stepper.Stepper
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_create_property.*
import kotlinx.android.synthetic.main.fragment_new_property_fragment5.*
import java.util.*

class CreatePropertyActivity : AppCompatActivity() {

    var photoList: ArrayList<String> = ArrayList()
    var editTextList: ArrayList<EditText> = ArrayList()

    private lateinit var propertyViewModel: PropertyViewModel

    var photo = ""

    lateinit var city:String
    lateinit var type:String
    var price:Int = 0
    var area:Int = 0
    var numberOfRooms:Int = 0
    lateinit var description:String
    lateinit var adress:String
    lateinit var placeNearby:String
    lateinit var status:String
    var createdDate:Long = 0
    lateinit var realEstateAgent:String
    var numberOfBathrooms:Int = 0
    var numberOfBedrooms:Int =0
    lateinit var additionAdress:String
    lateinit var postalCode:String
    lateinit var country:String
    var dateOfSale:Long = 0
    var latitude = 0.0
    var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_property)

        propertyViewModel = ViewModelProviders.of(this).get(PropertyViewModel::class.java)
    }

    override fun onSupportNavigateUp() = NavHostFragment.findNavController(nav_host_fragment).navigateUp()


    fun insertInDatabase(){
        val newId = UUID.randomUUID().toString()
        val newProperty = Property(
            newId,
            city.trim().toLowerCase(),
            type.trim().toLowerCase(),
            price,
            area,
            numberOfRooms,
            description,
            adress,
            placeNearby.trim().toLowerCase(),
            status,
            createdDate,
            dateOfSale,
            realEstateAgent,
            photo,
            numberOfBathrooms,
            numberOfBedrooms,
            additionAdress,
            postalCode,
            country.trim().toLowerCase(),
            latitude,
            longitude
        )
        propertyViewModel.insert(newProperty)


        for (i in 0 until photoList.size) {
            val idMedia = UUID.randomUUID().toString()
            val media = Media(
                idMedia,
                editTextList[i].text.toString(),
                photoList[i],
                newProperty.id
            )
            propertyViewModel.insertMedia(media)
        }

        finish()
    }

}
