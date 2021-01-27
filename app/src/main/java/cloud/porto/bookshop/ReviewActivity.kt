package cloud.porto.bookshop

import android.os.Bundle
import android.util.Log
import android.view.View
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

class ReviewActivity : AppCompatActivity() {

    companion object {
        const val BOOK = "BOOK"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var rv: RecyclerView
    private lateinit var comments: ArrayList<Comment>

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Initialize recyclerview
        comments = ArrayList()
        val adapter = CommentAdapter(comments)

        rv = findViewById(R.id.reviewsList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Read category from intent
        id = intent.getStringExtra(BOOK)
    }

    override fun onStart() {
        super.onStart()
        // Load book
        val database = FirebaseDatabase.getInstance().getReference("Books").child(id)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val book = snapshot.getValue<Book>()!!
                book.id = id
                loadComments(book)
            }

            override fun onCancelled(error: DatabaseError) {
                val builder = AlertDialog.Builder(this@ReviewActivity)
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
        val database = FirebaseDatabase.getInstance().getReference("Comments").child(book!!.id)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                comments.clear()

                snapshot.children.forEach {


                    val comment = Comment()
                    comment.comment = book.id

                    comments.add(comment)
                }

                rv.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                val builder = AlertDialog.Builder(this@ReviewActivity)
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
}