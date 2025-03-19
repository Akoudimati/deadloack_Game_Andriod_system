package com.example.deadlockgametimer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var gameTimeDisplay: TextView
    private lateinit var currentMinutes: TextInputEditText
    private lateinit var currentSeconds: TextInputEditText
    private lateinit var startButton: MaterialButton
    private lateinit var reminderTimesList: RecyclerView
    private lateinit var mediaPlayer1: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    
    private var gameTimer: CountDownTimer? = null
    private var gameTime = 0L
    private val reminderTimes = listOf(9.5, 14.5, 19.5, 24.5, 29.5, 34.5, 39.5, 44.5, 49.5, 54.5, 59.5)
    private val triggeredReminders = mutableSetOf<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupMediaPlayers()
        setupRecyclerView()
        setupStartButton()
    }

    private fun initializeViews() {
        gameTimeDisplay = findViewById(R.id.gameTimeDisplay)
        currentMinutes = findViewById(R.id.currentMinutes)
        currentSeconds = findViewById(R.id.currentSeconds)
        startButton = findViewById(R.id.startButton)
        reminderTimesList = findViewById(R.id.reminderTimesList)
    }

    private fun setupMediaPlayers() {
        mediaPlayer1 = MediaPlayer.create(this, R.raw.sigma_boy)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.sigma_boy)
    }

    private fun setupRecyclerView() {
        reminderTimesList.layoutManager = LinearLayoutManager(this)
        reminderTimesList.adapter = ReminderTimesAdapter(reminderTimes)
    }

    private fun setupStartButton() {
        startButton.setOnClickListener {
            startGame()
        }
    }

    private fun startGame() {
        val minutes = currentMinutes.text.toString().toIntOrNull() ?: 0
        val seconds = currentSeconds.text.toString().toIntOrNull() ?: 0

        if (minutes < 0 || seconds < 0 || seconds > 59) {
            // Show error message
            return
        }

        gameTimer?.cancel()
        triggeredReminders.clear()

        gameTime = (minutes * 60 + seconds) * 1000L

        gameTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                gameTime += 1000
                updateDisplay()
                checkReminders()
            }

            override fun onFinish() {
                // Timer finished
            }
        }.start()
    }

    private fun updateDisplay() {
        val minutes = (gameTime / 1000) / 60
        val seconds = (gameTime / 1000) % 60
        gameTimeDisplay.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun checkReminders() {
        val currentGameTime = gameTime / 1000.0 / 60.0
        reminderTimes.forEach { time ->
            if (Math.abs(currentGameTime - time) < 0.1 && !triggeredReminders.contains(time)) {
                playAlternatingSound()
                triggeredReminders.add(time)
            }
        }
    }

    private fun playAlternatingSound() {
        if (mediaPlayer1.isPlaying) {
            mediaPlayer2.start()
        } else {
            mediaPlayer1.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameTimer?.cancel()
        mediaPlayer1.release()
        mediaPlayer2.release()
    }
} 