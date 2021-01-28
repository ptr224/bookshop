package cloud.porto.bookshop

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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

class BooksActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY = "CATEGORY"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var rv: RecyclerView
    private lateinit var books: ArrayList<Book>

    private lateinit var category: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Initialize recyclerview
        books = ArrayList()
        val adapter = BookAdapter(books) {
            view: View, item: Book -> onItemClick(view, item)
        }

        rv = findViewById(R.id.booksList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Read category from intent
        category = intent.getStringExtra(CATEGORY)
        title = category
    }

    override fun onResume() {
        super.onResume()
        // Load books
        val database = FirebaseDatabase.getInstance().getReference("Books").child(category)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Rebuild list
                books.clear()
                snapshot.children.forEach {
                    val book = it.getValue<Book>()!!
                    book.id = it.key!!
                    books.add(book)
                }

                rv.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                val builder = AlertDialog.Builder(this@BooksActivity)
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

    private fun onItemClick(view: View, item: Book) {
        // Open book review
        val intent = Intent(this, BookReviewActivity::class.java)
        intent.putExtra(BookReviewActivity.CATEGORY, category)
        intent.putExtra(BookReviewActivity.BOOK, item.id)
        startActivity(intent)
    }
}