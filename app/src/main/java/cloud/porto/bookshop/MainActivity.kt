package cloud.porto.bookshop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var rv: RecyclerView
    private lateinit var categories: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = getString(R.string.select_category)

        // Initialize recyclerview
        categories = ArrayList()
        val adapter = CategoryAdapter(categories) {
            view: View, item: String -> onItemClick(view, item)
        }

        rv = findViewById(R.id.booksList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return if (item.itemId == R.id.action_logout) {
            auth.signOut()
            finish()
            true
        }
        else
            super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        val currentUser = auth.currentUser
        if(currentUser == null) {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Load categories
        val database = FirebaseDatabase.getInstance().getReference("Books")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Rebuild list
                categories.clear()
                snapshot.children.forEach {
                    categories.add(it.key!!)
                }

                rv.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                val builder = AlertDialog.Builder(this@MainActivity)
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

    private fun onItemClick(view: View, item: String) {
        // Open category
        val intent = Intent(this, BooksActivity::class.java)
        intent.putExtra(BooksActivity.CATEGORY, item)
        startActivity(intent)
    }
}