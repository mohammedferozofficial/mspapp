package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.StudyMaterial
import com.example.data.StudyMaterialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudyViewModel(private val repository: StudyMaterialRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedSubject = MutableStateFlow("All")
    val selectedSubject: StateFlow<String> = _selectedSubject

    private val _selectedBranch = MutableStateFlow("All")
    val selectedBranch: StateFlow<String> = _selectedBranch

    private val _selectedSemester = MutableStateFlow("All")
    val selectedSemester: StateFlow<String> = _selectedSemester

    val allMaterials: StateFlow<List<StudyMaterial>> = repository.allMaterials
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredMaterials: StateFlow<List<StudyMaterial>> = combine(
        allMaterials,
        _searchQuery,
        _selectedSubject,
        _selectedBranch,
        _selectedSemester
    ) { materials, query, subject, branch, semester ->
        materials.filter { material ->
            val matchesQuery = material.title.contains(query, ignoreCase = true) ||
                    material.content.contains(query, ignoreCase = true) ||
                    material.description.contains(query, ignoreCase = true) ||
                    material.subject.contains(query, ignoreCase = true)
            
            val matchesSubject = subject == "All" || material.subject.equals(subject, ignoreCase = true)
            val matchesBranch = branch == "All" || material.branch.equals(branch, ignoreCase = true)
            val matchesSemester = semester == "All" || material.semester.equals(semester, ignoreCase = true)

            matchesQuery && matchesSubject && matchesBranch && matchesSemester
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun setSelectedBranch(branch: String) {
        _selectedBranch.value = branch
    }

    fun setSelectedSemester(semester: String) {
        _selectedSemester.value = semester
    }

    fun toggleFavorite(material: StudyMaterial) {
        viewModelScope.launch {
            repository.update(material.copy(isFavorite = !material.isFavorite))
        }
    }

    fun insertMaterial(
        title: String,
        subject: String,
        branch: String,
        semester: String,
        description: String,
        pdfUrl: String? = null,
        content: String = ""
    ) {
        viewModelScope.launch {
            repository.insert(
                StudyMaterial(
                    title = title,
                    subject = subject,
                    branch = branch,
                    semester = semester,
                    description = description,
                    type = "PDF",
                    content = content.ifEmpty { "### $title\n\n#### Description\n$description\n\n---\n*This is an educational reference document shared by a student on MereSathPadhlo.*" },
                    pdfUrl = pdfUrl ?: "uploaded_notes_${System.currentTimeMillis()}",
                    isFavorite = false
                )
            )
        }
    }

    fun deleteMaterial(material: StudyMaterial) {
        viewModelScope.launch {
            repository.delete(material)
        }
    }
}

class StudyViewModelFactory(private val repository: StudyMaterialRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
