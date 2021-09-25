package com.smolianinovasiuzanna.hw18

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.smolianinovasiuzanna.hw18.databinding.FragmentPermissionBinding


class AskDangerousPermissionFragment: Fragment(R.layout.fragment_permission) {

    private val binding by viewBinding(FragmentPermissionBinding::bind)
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){isGranted ->
            // Handle Permission granted/rejected
            if (isGranted) {
                // Permission is granted
               checkGooglePlayServices()
            } else {
                // Permission is denied
                Toast.makeText(
                    requireContext(),
                    "Невозможно получить локацию без разрешения",
                    Toast.LENGTH_SHORT
                ) .show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            showCurrentLocationPermissionCheck()
        }

    private fun showCurrentLocationPermissionCheck() {
        val isLocationPermissionGranted = ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (isLocationPermissionGranted) { //если разрешения получены, начинаем проверку Google P;ay Services
            checkGooglePlayServices()
        } else {
            binding.accessButton.setOnClickListener {   // проверяем при запуске, есть ли разрешения
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )//если разрешений нет, запрашиваем разрешения
            }
        }
    }

    private fun checkGooglePlayServices(){
        val googleApi = GoogleApiAvailability.getInstance()
        val status = googleApi.isGooglePlayServicesAvailable(requireContext())
        if (status==ConnectionResult.SUCCESS) {
            showLocationInfoFragment() // если google play services установлены, показываем фрагмент с данными локации
            Log.i("status", googleApi.getErrorString(status))
        } else if (status==ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            Log.e("status", googleApi.getErrorString(status))
            // требуется обновление Google play services
            val updateDialog = AlertDialog.Builder(requireContext())
                .setMessage("Tребуется обновить Google Play Services")
                .setPositiveButton("OK") { dialog, which ->
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://" + LINK_TO_GOOGLE_PLAY_SERVICES)
                        )
                    )
                }
        } else {
            Log.e("status", googleApi.getErrorString(status))
            // Диалог, запрашивающий установку Google Play Services
            val dialog: Dialog? = googleApi.getErrorDialog(this, status, PLAY_SERVICES_RESOLUTION_REQUEST)
            dialog?.setCancelable(false)
            dialog?.show()
        }
    }

    private fun showLocationInfoFragment(){
        with(binding) {
            accessButton.isGone = true
            container.isVisible = true
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    android.R.animator.fade_in,
                    android.R.animator.fade_out
                )
                .replace(R.id.container, LocationInfoFragment())
                .addToBackStack("LocationInfo")
                .commit()
        }
    }
    companion object{
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 12345
        private const val LINK_TO_GOOGLE_PLAY_SERVICES = "play.google.com/store/apps/details?id=com.google.android.gms&hl=en"
    }
}