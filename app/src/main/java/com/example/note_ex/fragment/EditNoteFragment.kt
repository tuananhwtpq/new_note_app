package com.example.note_ex.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.note_ex.R
import com.example.note_ex.database.NoteDatabase
import com.example.note_ex.databinding.FragmentEditNoteBinding
import com.example.note_ex.model.Note
import com.example.note_ex.repository.NoteRepository
import com.example.note_ex.viewmodel.NoteViewModel
import com.example.note_ex.viewmodel.NoteViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class EditNoteFragment : Fragment(), MenuProvider {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private var isNoteChanged = false

    private val args: EditNoteFragmentArgs by navArgs()
    private val viewModel: NoteViewModel by activityViewModels(){
        val noteRepo = NoteRepository(NoteDatabase(requireActivity()))
        NoteViewModelFactory(noteRepo)
    }
    private lateinit var currentNote: Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleDataFromArgs()
        handleDoneBtnClicked()

        var menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, androidx.lifecycle.Lifecycle.State.RESUMED)

    }

    private fun handleDoneBtnClicked(){
        binding.fabBtn.setOnClickListener { saveNote() }
    }

    private fun saveNote(){

        checkoutData()
        if (isNoteChanged){
            Toast.makeText(requireContext(), "Note updated successfully", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else{
            findNavController().popBackStack()
        }

    }

    private fun handleDataFromArgs(){
        val noteFromArgs = args.note
        if (noteFromArgs == null){
            Toast.makeText(requireContext(), "Note is null", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else{
            currentNote = noteFromArgs
            binding.edTitle.setText(currentNote.note_title)
            binding.edDescription.setText(currentNote.note_content)
        }
    }

    private fun checkoutData(){
        val title = binding.edTitle.text.toString().trim()
        val content = binding.edDescription.text.toString().trim()

        val currentTime = LocalDateTime.now()
        val formattedTime = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")
        val time = currentTime.format(formattedTime)

        if (title.isNotEmpty() || content.isNotEmpty()) {
            val note = Note(currentNote.id, title, content, time)
            viewModel.updateNote(note)
            isNoteChanged = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if(!isNoteChanged){
            checkoutData()
        }
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_note_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.edit_menu -> {
                deleteNote()
                true
            }
            else -> false
        }

        return true
    }

    private fun deleteNote(){
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Note")
            setMessage("Do you want to delete this note?")
            setPositiveButton("Delete") { _, _ ->
                viewModel.deleteNote(currentNote)
                Toast.makeText(requireContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }



}