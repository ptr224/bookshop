package cloud.porto.bookshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val categories: List<String>, private val listener: (View, String) -> Unit): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val category: TextView = itemView.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val bookView: View = inflater.inflate(R.layout.category_card, parent, false)

        // Return a new holder instance
        return ViewHolder(bookView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val category = categories[position]
        // Set item views based on your views and data model
        holder.category.text = category
        // Set click listener
        holder.itemView.setOnClickListener {
            listener(it, category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}