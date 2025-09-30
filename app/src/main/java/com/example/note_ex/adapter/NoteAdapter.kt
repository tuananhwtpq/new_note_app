package com.example.note_ex.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.note_ex.databinding.NoteItemBinding
import com.example.note_ex.databinding.NoteListItemBinding
import com.example.note_ex.fragment.HomeFragmentDirections
import com.example.note_ex.model.Note

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = mutableListOf<Note>()

    var onItemClick: ((Note) -> Unit)? = null

    private var viewType: Int = GRID_LAYOUT

    companion object {
        const val GRID_LAYOUT = 1
        const val LIST_LAYOUT = 2
    }

    sealed class NoteViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(note: Note)

        class GridViewHolder(private val binding: NoteItemBinding) : NoteViewHolder(binding) {
            override fun bind(note: Note) {
                binding.tvTitle.text = note.note_title
                binding.tvDescription.text = note.note_content
                binding.tvTime.text = note.dateTime
            }
        }

        class ListViewHolder(private val binding: NoteListItemBinding) : NoteViewHolder(binding) {
            override fun bind(note: Note) {
                binding.tvTitle.text = note.note_title
                binding.tvContent.text = note.note_content
                binding.tvTime.text = note.dateTime

            }
        }
    }

    fun submitData(data: List<Note>) {
        notes.clear()
        notes.addAll(data)
        notifyDataSetChanged()
    }

    fun setViewType(newViewType: Int) {
        if (viewType != newViewType) {
            viewType = newViewType
            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            GRID_LAYOUT -> {
                val binding = NoteItemBinding.inflate(inflater, parent, false)
                return NoteViewHolder.GridViewHolder(binding)
            }

            LIST_LAYOUT -> {
                val binding = NoteListItemBinding.inflate(inflater, parent, false)
                return NoteViewHolder.ListViewHolder(binding)
            }

        }

        return NoteViewHolder.GridViewHolder(
            NoteItemBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        val note = notes[position]
        holder.bind(note)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(note)
        }

    }

    override fun getItemCount(): Int = notes.size

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

}