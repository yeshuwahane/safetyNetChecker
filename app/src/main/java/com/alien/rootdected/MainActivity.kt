package com.alien.rootdected

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.alien.rootdected.ui.theme.RootDectedTheme
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RootDectedTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        val testKey = detectTestKeys()
        val suExist = checkSuExists()
        val suBinary = checkForSuBinary()
        val busyBoxyBinary = checkForBusyBoxBinary()

        if (testKey || suExist || suBinary || busyBoxyBinary){
            Toast.makeText(this, "Device is rooted", Toast.LENGTH_SHORT).show()
            Log.d("alien","Device is rooted")
        }else{
            Toast.makeText(this, "Device is safe", Toast.LENGTH_SHORT).show()
            Log.d("alien","Device is safe")

        }

    }

    private fun checkSuExists(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec(arrayOf("/system /xbin/which", "su"))
            val `in` = BufferedReader(
                InputStreamReader(process.inputStream)
            )
            val line = `in`.readLine()
            process.destroy()
            line != null
        } catch (e: Exception) {
            process?.destroy()
            false
        }
    }

    private fun detectTestKeys(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkForSuBinary(): Boolean {
        return checkForBinary("su")
    }

    private fun checkForBusyBoxBinary(): Boolean {
        return checkForBinary("busybox")
    }


    private fun checkForBinary(filename: String): Boolean {
        for (path in binaryPaths) {
            val f = File(path, filename)
            val fileExists = f.exists()
            if (fileExists) {
                return true
            }
        }
        return false
    }

    private val binaryPaths = arrayOf(
        "/data/local/",
        "/data/local/bin/",
        "/data/local/xbin/",
        "/sbin/",
        "/su/bin/",
        "/system/bin/",
        "/system/bin/.ext/",
        "/system/bin/failsafe/",
        "/system/sd/xbin/",
        "/system/usr/we-need-root/",
        "/system/xbin/",
        "/system/app/Superuser.apk",
        "/cache",
        "/data",
        "/dev"
    )

}


