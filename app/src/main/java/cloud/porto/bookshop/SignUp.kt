package cloud.porto.bookshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var registerButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = Firebase.auth

        registerButton = findViewById(R.id.registerButton)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginTextView = findViewById(R.id.loginTextView)

        // Set callbacks
        registerButton.setOnClickListener{
            onSignUpClick()
        }
        loginTextView.setOnClickListener {
            onLoginClick()
        }
    }

    private fun onLoginClick() {
        // Return to LogIn
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
        finish()
    }

    private fun onSignUpClick() {
        // Validate fields and create user
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val userName = nameEditText.text.toString().trim()

        when {
            userName.isEmpty() -> nameEditText.error = getString(R.string.username_edit_error)
            email.isEmpty() -> emailEditText.error = getString(R.string.email_edit_error)
            password.isEmpty() -> passwordEditText.error = getString(R.string.password_edit_error)
            else -> createUser(userName, email, password)
        }
    }

    private fun createUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val currentUser = auth.currentUser
                    val uid = currentUser!!.uid
                    val userMap = HashMap<String, String>()
                    userMap["name"] = userName
                    val database = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                    database.setValue(userMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    val builder = AlertDialog.Builder(this)
                    with(builder)
                    {
                        setTitle("Authentication failed")
                        setMessage(task.exception?.message)
                        setPositiveButton("OK", null)
                        show()
                    }
                }
            }
    }
}