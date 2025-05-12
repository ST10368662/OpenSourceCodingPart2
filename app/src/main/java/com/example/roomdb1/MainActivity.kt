package com.example.roomdb1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.roomdblatest.AppDatabase
import com.example.roomdblatest.User
import com.example.roomdblatest.UserDao
import kotlinx.coroutines.launch

private lateinit var db: AppDatabase
private lateinit var userDao: UserDao
private lateinit var editTextName: EditText
private lateinit var buttonSave: Button
private lateinit var textViewUsers: TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = AppDatabase.getDatabase(this)
        userDao = db.userDao()

        editTextName = findViewById(R.id.editTextName)
        buttonSave = findViewById(R.id.buttonSave)
        textViewUsers = findViewById(R.id.textViewUsers)

        val goToBooksBtn = findViewById<Button>(R.id.buttonGoToBooks)
        goToBooksBtn.setOnClickListener {
            startActivity(Intent(this, BookActivity::class.java))
        }

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            if (name.isNotBlank()) {
                lifecycleScope.launch {
                    userDao.insert(User(name = name))
                    showUsers()
                }
                editTextName.text.clear()
            }
        }

        lifecycleScope.launch {
            showUsers()
        }
    }

    private suspend fun showUsers() {
        val users = userDao.getAllUsers()
        val names = users.joinToString("\n") { "${it.id}: ${it.name}" }
        runOnUiThread {
            textViewUsers.text = names
        }
    }
}