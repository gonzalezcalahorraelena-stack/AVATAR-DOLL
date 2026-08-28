package com.example.avatar.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.avatar.data.AvatarDatabase
import com.example.avatar.data.AvatarRepository
import com.example.avatar.data.SavedAvatarEntity
import com.example.avatar.model.ACCESSORIES_LIST
import com.example.avatar.model.AmbientLighting
import com.example.avatar.model.ArtStyle
import com.example.avatar.model.AvatarConfig
import com.example.avatar.model.BANGS_OPTIONS
import com.example.avatar.model.BodyType
import com.example.avatar.model.COMPANION_OPTIONS
import com.example.avatar.model.EYELINER_STYLES
import com.example.avatar.model.EYE_SHAPES
import com.example.avatar.model.EyeExpression
import com.example.avatar.model.Gender
import com.example.avatar.model.LIPSTICK_FINISHES
import com.example.avatar.model.NAIL_DESIGNS
import com.example.avatar.model.NAIL_SHAPES
import com.example.avatar.model.OUTFITS_LIST
import com.example.avatar.model.ROOM_OPTIONS
import com.example.avatar.model.SHOES_LIST
import com.example.avatar.model.SKIN_TONES
import com.example.avatar.model.TOPS_LIST
import com.example.avatar.util.JsonUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Stack

class AvatarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AvatarRepository

    init {
        val database = AvatarDatabase.getDatabase(application)
        repository = AvatarRepository(database.avatarDao())
    }

    val savedAvatars: StateFlow<List<SavedAvatarEntity>> = repository.allAvatars
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current State
    private val _config = MutableStateFlow(AvatarConfig())
    val config: StateFlow<AvatarConfig> = _config.asStateFlow()

    // Undo / Redo Stacks (Command pattern history)
    private val undoStack = Stack<AvatarConfig>()
    private val redoStack = Stack<AvatarConfig>()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun updateConfig(transform: (AvatarConfig) -> AvatarConfig) {
        val current = _config.value
        undoStack.push(current)
        redoStack.clear()

        val next = transform(current)
        _config.value = next
        updateUndoRedoStates()
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            redoStack.push(_config.value)
            _config.value = undoStack.pop()
            updateUndoRedoStates()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            undoStack.push(_config.value)
            _config.value = redoStack.pop()
            updateUndoRedoStates()
        }
    }

    private fun updateUndoRedoStates() {
        _canUndo.value = undoStack.isNotEmpty()
        _canRedo.value = redoStack.isNotEmpty()
    }

    fun switchArtStyle(artStyle: ArtStyle) {
        updateConfig { current ->
            current.copy(artStyle = artStyle)
        }
    }

    fun switchGender(gender: Gender) {
        updateConfig { current ->
            current.copy(gender = gender)
        }
    }

    fun randomize() {
        val randomStyle = ArtStyle.entries.random()
        val randomGender = Gender.entries.random()
        val randomSkin = SKIN_TONES.random().hex
        val randomTop = TOPS_LIST.random()
        val randomShoes = SHOES_LIST.random()
        val randomRoom = ROOM_OPTIONS.random()
        val randomCompanion = COMPANION_OPTIONS.random()
        val randomLighting = AmbientLighting.entries.random()

        updateConfig { current ->
            current.copy(
                artStyle = randomStyle,
                gender = randomGender,
                skinToneHex = randomSkin,
                topClothingId = randomTop.id,
                topColorHex = randomTop.defaultColorHex,
                shoesId = randomShoes.id,
                shoesColorHex = randomShoes.defaultColorHex,
                roomId = randomRoom.id,
                companionId = randomCompanion.id,
                ambientLighting = randomLighting,
                eyelinerStyle = EYELINER_STYLES.random(),
                lipstickFinish = LIPSTICK_FINISHES.random(),
                nailShape = NAIL_SHAPES.random(),
                nailDesign = NAIL_DESIGNS.random()
            )
        }
        _userMessage.value = "¡Avatar aleatorio generado con éxito!"
    }

    fun saveCurrentAvatar(name: String) {
        viewModelScope.launch {
            val json = JsonUtils.toJson(_config.value)
            val entity = SavedAvatarEntity(
                name = if (name.isBlank()) _config.value.name else name,
                artStyle = _config.value.artStyle.displayName,
                gender = _config.value.gender.displayName,
                configJson = json
            )
            repository.saveAvatar(entity)
            _userMessage.value = "¡Avatar guardado en la Galería!"
        }
    }

    fun loadAvatarFromEntity(entity: SavedAvatarEntity) {
        val parsed = JsonUtils.fromJson(entity.configJson)
        if (parsed != null) {
            undoStack.push(_config.value)
            _config.value = parsed
            updateUndoRedoStates()
            _userMessage.value = "Avatar '${entity.name}' cargado"
        } else {
            _userMessage.value = "Error al deserializar el avatar"
        }
    }

    fun importJson(jsonStr: String): Boolean {
        val parsed = JsonUtils.fromJson(jsonStr)
        return if (parsed != null) {
            undoStack.push(_config.value)
            _config.value = parsed
            updateUndoRedoStates()
            _userMessage.value = "¡Configuración JSON importada con éxito!"
            true
        } else {
            _userMessage.value = "Formato JSON no válido"
            false
        }
    }

    fun deleteAvatar(entity: SavedAvatarEntity) {
        viewModelScope.launch {
            repository.deleteAvatar(entity)
            _userMessage.value = "Avatar eliminado"
        }
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
