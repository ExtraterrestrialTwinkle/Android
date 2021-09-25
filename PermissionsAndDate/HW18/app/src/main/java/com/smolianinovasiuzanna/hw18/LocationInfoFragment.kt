package com.smolianinovasiuzanna.hw18

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.*
import com.smolianinovasiuzanna.hw18.databinding.FragmentLocationInfoBinding
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import kotlin.random.Random

class LocationInfoFragment: Fragment(R.layout.fragment_location_info) {

    private val binding by viewBinding(FragmentLocationInfoBinding::bind)
    private var state = LocationInfoState(listOf())
    private var selectedInstant: Instant? = null
    private var locationInfo = listOf<LocationInfo>()
    private lateinit var userLocationInfo: LocationInfo
    private lateinit var locationRequest: LocationRequest
    private var locationInfoAdapter: LocationInfoAdapter? = null

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            if (savedInstanceState == null) {
                Log.d("savedInstanceState", "null")
                initList()
                val listOfLocationInfo = locationInfo
                locationInfoAdapter?.setList(listOfLocationInfo)
                state = LocationInfoState(listOfLocationInfo)
            }
            if (savedInstanceState != null) {
                Log.d("savedInstanceState", "not null")
                initList()
                state = savedInstanceState.getParcelable(KEY) ?: error("error state")
                locationInfoAdapter?.setList(state.locationInfoList)
            }

            getLocationButton.setOnClickListener { // получаем последнюю локацию с помощью Google Play Services
                LocationServices.getFusedLocationProviderClient(requireContext())
                    .lastLocation
                    .addOnSuccessListener {
                        noLocationTextView.isGone = true
                        it?.let {
                            onLocationChanged(it)
                        }
                    }
                    .addOnCanceledListener {
                        Toast.makeText(
                            requireContext(),
                            "Запрос отменен",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(
                            requireContext(),
                            "Ошибка получения локации",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            startLocationButton.setOnClickListener {
                noLocationTextView.isGone = true
                startLocationUpdates()
            }
            stopLocationButton.setOnClickListener {
                stoplocationUpdates()
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() { // отслеживает локацию по нажатию на кнопку "Отследить локацию"
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 1000
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(requireContext())
        settingsClient.checkLocationSettings(locationSettingsRequest)

       LocationServices.getFusedLocationProviderClient(requireContext())
           .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }
    fun onLocationChanged(location: Location) { // Создает объект LocationInfo и записывает его в список
        location.let {
            userLocationInfo =
                LocationInfo(
                    id = Random.nextLong(),
                    info = """
                                        Lat = ${it.latitude}
                                        Lng = ${it.longitude}
                                        Alt = ${it.altitude}
                                        Speed = ${it.speed}
                                        Accuracy = ${it.accuracy}
                                    """.trimIndent(),
                    imageLink = "https://w7.pngwing.com/pngs/346/870/png-transparent-cracked-screens-location-link-free-city-map-map-map-location-link-free.png",
                    timestamp = Instant.now()
                )
            locationInfo += listOf(userLocationInfo)
            locationInfoAdapter?.setList(locationInfo)
            state = LocationInfoState(locationInfo)
            selectedInstant = null
        }
    }

    private fun stoplocationUpdates() { // Останавливает отслеживание локации по нажатию на кнопку "Остановить"
        LocationServices.getFusedLocationProviderClient(requireContext()).removeLocationUpdates(locationCallback)
    }

    private fun initList() {
        locationInfoAdapter = LocationInfoAdapter { position -> pickNewTime(position) }
        with(binding.recyclerView) {
            adapter = locationInfoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            itemAnimator = ScaleInAnimator()
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
    }

    private fun pickNewTime(position: Int) { // Устанавливает новое время/дату
        val currentDateTime = LocalDateTime.now()

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        val zonedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                            .atZone(ZoneId.systemDefault())

                        Toast.makeText(
                            requireContext(),
                            "Выбрано время: $zonedDateTime",
                            Toast.LENGTH_SHORT
                        ).show()
                        selectedInstant = zonedDateTime.toInstant()

                        Log.d ("selectedInstant", "$selectedInstant")
                        Log.d("locationInfo1", "$locationInfo")

                        var newList = arrayListOf<LocationInfo>()
                        newList = arrayListOf<LocationInfo>()
                        locationInfo.forEach{ element -> newList.add(element.copy())}
                        val selectedInfo = newList.elementAt(position)
                        newList.removeAt(position)
                        selectedInfo.timestamp = selectedInstant as Instant
                        selectedInfo.id = Random.nextLong()
                        newList.add(position, selectedInfo)
                        Log.d("timestamp", "${newList[position].timestamp}")

                        locationInfoAdapter?.setList(newList)
                        Log.d("locationInfo3 = newList", "$newList")
                        Log.d("locationInfo1 = locationInfo", "$locationInfo")

                        //locationInfoAdapter?.notifyItemChanged(position) // Почему только так работает???
                        state = LocationInfoState(newList)
                    },
                    currentDateTime.hour,
                    currentDateTime.minute,
                    true
                )
                    .show()

            },
            currentDateTime.year,
            currentDateTime.month.value - 1,
            currentDateTime.dayOfMonth
        )
            .show()

    }

    companion object{
        private const val KEY = "key"

    }

}