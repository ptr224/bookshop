package cloud.porto.bookshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private val mBooks: List<Book>): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val title = itemView.findViewById<TextView>(R.id.bookTitle)
        val image = itemView.findViewById<ImageView>(R.id.bookImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val bookView: View = inflater.inflate(R.layout.book_resume, parent, false)

        // Return a new holder instance
        return ViewHolder(bookView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val contact: Book = mBooks[position]
        // Set item views based on your views and data model
        val title = holder.title
        title.text = "yoo"
        //val image = holder.image
    }

    override fun getItemCount(): Int {
        return mBooks.size
    }
}