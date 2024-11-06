package com.example.project4

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.project4.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var playButton : Button
    private lateinit var stopButton : Button
    private lateinit var navigationMenu: NavigationView
    private lateinit var descriptionText : TextView
    private lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        mediaPlayer = MediaPlayer()

        playButton = binding.playButton
        stopButton = binding.stopButton

        navigationMenu = binding.navigationMenu

        descriptionText = binding.descriptionText
        drawerLayout = binding.drawerLayout


        playButton.setOnClickListener{ playButtonOnclick() }

        stopButton.setOnClickListener { stopAudio() }

        navigationMenu.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemClick(menuItem.itemId)
        }

    }

    private fun playButtonOnclick(){
        // Get the selected mode description
        // Choose the appropriate storage path based on the selected mode
        var storagePath = when (descriptionText.text.toString()) {
            getString(R.string.easyModeDescription) -> "Music/Easy/easy0.mp3"
            getString(R.string.mediumModeDescription) -> "Music/Medium/medium0.mp3"
            getString(R.string.hardModeDescription) -> "Music/Hard/hard0.mp3"
            getString(R.string.studyModeDescription) -> "Music/Study/study0.mp3"
            else -> null
        }

        if (storagePath != null) {
            stopAudio()

            val storageReference: StorageReference = Firebase.storage.reference.child(storagePath)
            val localFile = File.createTempFile("temp", "mp3")

            // Download and play the audio file
            storageReference.getFile(localFile).addOnSuccessListener {
                playAudio(localFile.absolutePath)
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show()
            }
        } else {
            descriptionText.text = "Nothing has been chosen"
        }
    }

    private fun playAudio(filePath: String){
        try {
            mediaPlayer?.setDataSource(filePath)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun stopAudio(){
        mediaPlayer?.apply {
            if (isPlaying){
                stop()
                prepareAsync()
            }
            reset()
        }
    }

    private fun updateTextView(message: String) {
        descriptionText.text = message
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    private fun handleNavigationItemClick(itemId: Int): Boolean {
        when (itemId) {
            R.id.easyMode -> {
                updateTextView(getString(R.string.easyModeDescription))
                drawerLayout.closeDrawer(GravityCompat.START)
                true

            }
            R.id.mediumMode -> {
                updateTextView(getString(R.string.mediumModeDescription))
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            R.id.hardMode -> {
                updateTextView(getString(R.string.hardModeDescription))
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            R.id.studyMode -> {
                updateTextView(getString(R.string.studyModeDescription))
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            else -> false
        }
        return false
    }


}