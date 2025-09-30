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
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.note_ex.R
import com.example.note_ex.database.NoteDatabase
import com.example.note_ex.databinding.FragmentAddNoteBinding
import com.example.note_ex.model.Note
import com.example.note_ex.repository.NoteRepository
import com.example.note_ex.viewmodel.NoteViewModel
import com.example.note_ex.viewmodel.NoteViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class AddNoteFragment : Fragment(), MenuProvider {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private var isNoteSaved: Boolean = false

    private val viewModel: NoteViewModel by activityViewModels(){
        val noteRepo = NoteRepository(NoteDatabase(requireActivity()))
        NoteViewModelFactory(noteRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.fabBtn.setOnClickListener { handleFabClick() }

    }

    private fun handleFabClick() {

        checkoutData()
        if (isNoteSaved){
            Toast.makeText(requireContext(), "Note added successfully", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else{
            findNavController().popBackStack()
        }
    }

    private fun checkoutData(){
        val title = binding.edTitle.text.toString().trim()
        val description = binding.edDescription.text.toString().trim()

        val currentTime = LocalDateTime.now()
        var formattedTime = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy")
        var time = currentTime.format(formattedTime)

        val note = Note(0, title, description, time)
        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title is empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(requireContext(), "Description is empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.length > 120) {
            Toast.makeText(requireContext(), "Title is too long", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.addNote(note)
        isNoteSaved = true
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (!isNoteSaved){
            checkoutData()
        }
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.add_note_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.save_Menu -> {
                checkoutData()
                Toast.makeText(requireContext(), "Note added successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                true
            } else -> false
        }
        return true
    }


}