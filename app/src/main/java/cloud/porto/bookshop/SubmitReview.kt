package cloud.porto.bookshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SubmitReview : AppCompatActivity() {

    companion object {
        const val BOOK = "BOOK"
    }

    private lateinit var id: String
    private lateinit var commentText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_review)
        setSupportActionBar(findViewById(R.id.toolbar))

        title = getString(R.string.submit_review)

        // Initialize variables and submit handler
        id = intent.getStringExtra(BookReviewActivity.BOOK)!!
        commentText = findViewById(R.id.commentText)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            view -> submit(view)
        }
    }

    private fun submit(view: View) {
        // Submit comment
        commentText.isEnabled = false
        submitButton.isEnabled = false

        FirebaseDatabase.getInstance().getReference("Comments")
            .child(id)
            .child(Firebase.auth.currentUser?.uid!!)
            .setValue(commentText.text.toString())
            .addOnCompleteListener {
                finish()
            }
    }
}