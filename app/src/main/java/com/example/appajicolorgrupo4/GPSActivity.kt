package com.example.appajicolorgrupo4

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GPSActivity : AppCompatActivity(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)
        title = "GPS Location"

        tvGpsLocation = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.getLocation)

        button.setOnClickListener {
            getLocation()
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Verificar si tiene permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionCode
            )
            return
        }

        // Verificar si el GPS está habilitado
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Por favor, activa el GPS", Toast.LENGTH_LONG).show()
            return
        }

        try {
            // Intentar obtener la última ubicación conocida
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                updateLocationDisplay(lastKnownLocation)
            } else {
                tvGpsLocation.text = "Obteniendo ubicación..."
            }

            // Solicitar actualizaciones de ubicación
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,  // 5 segundos
                5f,    // 5 metros
                this
            )

            Toast.makeText(this, "Buscando ubicación GPS...", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Error de permisos: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLocationChanged(location: Location) {
        updateLocationDisplay(location)
        Toast.makeText(this, "Ubicación actualizada", Toast.LENGTH_SHORT).show()
    }

    private fun updateLocationDisplay(location: Location) {
        tvGpsLocation.text = "Latitud: ${location.latitude}\nLongitud: ${location.longitude}"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                getLocation()
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede obtener la ubicación.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Detener actualizaciones de ubicación cuando la activity no está visible
        if (::locationManager.isInitialized) {
            locationManager.removeUpdates(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar recursos
        if (::locationManager.isInitialized) {
            locationManager.removeUpdates(this)
        }
    }
}

