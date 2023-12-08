package cn.shef.msc5.todo.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import cn.shef.msc5.todo.base.BaseActivity
import cn.shef.msc5.todo.ui.theme.AppTheme
import cn.shef.msc5.todo.ui.view.MainScreen

/**
 * @author Zhecheng Zhao
 * @email zzhao84@sheffield.ac.uk
 * @date Created in 02/11/2023 06:45
 */
class MainActivity : BaseActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //TODO request permissions
        if (!hasPermission()) {
            requestFineLocationPermission()
        }
        Log.d(TAG, "onStart: ")
    }

    private val GPS_LOCATION_PERMISSION_REQUEST = 1
    private fun requestFineLocationPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf( android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION ),
            GPS_LOCATION_PERMISSION_REQUEST
        )
    }

    private fun hasPermission(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION )
    }

}





