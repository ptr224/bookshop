package cloud.porto.bookshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private val mBooks: List<Book>, private val listener: (View, Book) -> Unit): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val title: TextView = itemView.findViewById(R.id.bookTitle)
        val info: TextView = itemView.findViewById(R.id.bookInfo)
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
        val book = mBooks[position]

        with (book) {
            // Set item views based on your views and data model
            holder.title.text = title
            holder.info.text = "${author} - ${publisher} - ${year}"
        }

        // Set click listener
        holder.itemView.setOnClickListener {
            listener(it, book)
        }
    }

    override fun getItemCount(): Int {
        return mBooks.size
    }
}