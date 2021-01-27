package cloud.porto.bookshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(private val comments: List<Comment>): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val user: TextView = itemView.findViewById(R.id.comment_user)
        val text: TextView = itemView.findViewById(R.id.comment_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val bookView: View = inflater.inflate(R.layout.comment_view, parent, false)

        // Return a new holder instance
        return ViewHolder(bookView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val comment = comments[position]
        // Set item views based on your views and data model
        holder.user.text = comment.user
        holder.text.text = comment.comment
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}