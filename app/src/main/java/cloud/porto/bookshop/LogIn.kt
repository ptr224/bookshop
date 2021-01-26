package cloud.porto.bookshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = Firebase.auth

        loginButton = findViewById(R.id.loginButton)
        signUpTextView = findViewById(R.id.signUpTextView)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        // Set callbacks
        loginButton.setOnClickListener{
            onLoginClick()
        }
        signUpTextView.setOnClickListener {
            onSignUpClick()
        }
    }

    private fun onSignUpClick() {
        // Go to SignUp
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        finish()
    }

    private fun onLoginClick() {
        // Validate fields and create user
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        when {
            email.isEmpty() -> emailEditText.error = getString(R.string.email_edit_error)
            password.isEmpty() -> passwordEditText.error = getString(R.string.password_edit_error)
            else -> loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
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