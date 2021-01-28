package cloud.porto.bookshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class BookReviewActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY = "CATEGORY"
        const val BOOK = "BOOK"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var rv: RecyclerView
    private lateinit var comments: ArrayList<Comment>

    private lateinit var category: String
    private lateinit var id: String

    private lateinit var bookTitle: TextView
    private lateinit var bookInfo: TextView
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
                view -> submitReview(view)
        }

        // Initialize recyclerview
        comments = ArrayList()
        val adapter = CommentAdapter(comments)

        rv = findViewById(R.id.reviewsList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Read category from intent
        category = intent.getStringExtra(CATEGORY)!!
        id = intent.getStringExtra(BOOK)!!

        bookTitle = findViewById(R.id.bookTitle)
        bookInfo = findViewById(R.id.bookInfo)
    }

    override fun onResume() {
        super.onResume()
        // Load book
        val database = FirebaseDatabase.getInstance().getReference("Books").child(category).child(id)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val book = snapshot.getValue<Book>()!!
                book.id = id
                // Load book details
                with (book) {
                    bookTitle.text = title
                    bookInfo.text = "${author} - ${publisher} - ${year}"
                }

                // Load comments
                loadComments(book)
            }

            override fun onCancelled(error: DatabaseError) {
                val builder = AlertDialog.Builder(this@BookReviewActivity)
                with(builder)
                {
                    setTitle("Loading failed")
                    setMessage(error.message)
                    setPositiveButton("OK", null)
                    show()
                }
            }
        })
    }

    private fun loadComments(book: Book) {
        val database = FirebaseDatabase.getInstance().getReference("Comments").child(book.id)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comments.clear()
                val currentUser = Firebase.auth.currentUser?.uid
                var enableFab = true

                snapshot.children.forEach {
                    val comment = Comment()
                    comment.user = it.key!!
                    comment.comment = it.getValue<String>()!!
                    comments.add(comment)

                    // Disable fab if already commented
                    if (currentUser.equals(it.key))
                        enableFab = false
                }

                rv.adapter?.notifyDataSetChanged()
                fab.isEnabled = enableFab
            }

            override fun onCancelled(error: DatabaseError) {
                val builder = AlertDialog.Builder(this@BookReviewActivity)
                with(builder)
                {
                    setTitle("Loading failed")
                    setMessage(error.message)
                    setPositiveButton("OK", null)
                    show()
                }
            }
        })
    }

    private fun submitReview(view: View) {
        // Open review submit
        val intent = Intent(this, SubmitReview::class.java)
        intent.putExtra(BOOK, id)
        startActivity(intent)
    }
}