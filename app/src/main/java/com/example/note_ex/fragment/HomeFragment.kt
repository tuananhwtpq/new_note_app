package com.example.note_ex.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.util.query
import com.example.note_ex.R
import com.example.note_ex.adapter.NoteAdapter
import com.example.note_ex.database.NoteDatabase
import com.example.note_ex.databinding.FragmentHomeBinding
import com.example.note_ex.model.Note
import com.example.note_ex.repository.NoteRepository
import com.example.note_ex.viewmodel.NoteViewModel
import com.example.note_ex.viewmodel.NoteViewModelFactory

class HomeFragment : Fragment(), SearchView.OnQueryTextListener, MenuProvider {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteAdapter: NoteAdapter

    private var isListLayout: Boolean = false

    private val viewmodel: NoteViewModel by activityViewModels() {
        val noteRepo = NoteRepository(NoteDatabase(requireActivity()))
        NoteViewModelFactory(noteRepo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        setupFabBtn()
        setupMenu()
        observeData()
        onChangeLayoutClicked()
    }

    private fun observeData(){
        viewmodel.allNotes.observe(viewLifecycleOwner, Observer{ notes ->
            noteAdapter.submitData(notes)

            if(notes.isEmpty()){
                binding.rvAllNotes.visibility = View.GONE
                binding.ivEmptyNote.visibility = View.VISIBLE
            } else {
                binding.rvAllNotes.visibility = View.VISIBLE
                binding.ivEmptyNote.visibility = View.GONE
            }

            binding.rvAllNotes.scrollToPosition(0)
        })
    }

    private fun setupMenu(){
        var menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner ,Lifecycle.State.RESUMED)
    }

    private fun onChangeLayoutClicked(){
        binding.ivChangeLayout.setOnClickListener { changeLayout() }
    }

    private fun initRecyclerView() {
        noteAdapter = NoteAdapter()
        binding.rvAllNotes.apply {
            adapter = noteAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        noteAdapter.onItemClick = {note ->

            if (findNavController().currentDestination?.id == R.id.homeFragment){

                val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note)
                findNavController().navigate(action)
            }
        }
    }

    private fun changeLayout(){
        if (!isListLayout){
            noteAdapter.setViewType(NoteAdapter.LIST_LAYOUT)
            binding.rvAllNotes.layoutManager = LinearLayoutManager(context)
            isListLayout = true
            binding.ivChangeLayout.setImageResource(R.drawable.list_layout)
        } else {
            noteAdapter.setViewType(NoteAdapter.GRID_LAYOUT)
            binding.rvAllNotes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            isListLayout = false
            binding.ivChangeLayout.setImageResource(R.drawable.grid)
        }

    }

    private fun setupFabBtn() {
        binding.fabBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewmodel.clearSeach()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            viewmodel.searchNote(newText)
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val mMenuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = false
        mMenuSearch.setOnQueryTextListener(this)

        mMenuSearch.setOnCloseListener {
            viewmodel.clearSeach()
            false
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

}