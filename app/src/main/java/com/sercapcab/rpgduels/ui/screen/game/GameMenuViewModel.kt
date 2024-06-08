package com.sercapcab.rpgduels.ui.screen.game

import androidx.lifecycle.ViewModel
import com.sercapcab.rpgduels.game.entity.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameMenuViewModel : ViewModel() {
    private val _charactersState = MutableStateFlow<List<Character>>(emptyList())
    private val _enemyCharactersState = MutableStateFlow<List<Character>>(emptyList())
    private val _selectedCharacterState = MutableStateFlow<Character?>(null)
    private val _selectedButtonState = MutableStateFlow<Int?>(null)

    val charactersState = _charactersState.asStateFlow()
    val enemyCharactersState = _enemyCharactersState.asStateFlow()
    val selectedCharacterState = _selectedCharacterState.asStateFlow()
    val selectedButtonState = _selectedButtonState.asStateFlow()

    fun updateCharacters(characters: List<Character>) {
        _charactersState.value = characters
    }

    fun updateEnemyCharacters(characters: List<Character>) {
        _enemyCharactersState.value = characters
    }

    fun selectCharacter(character: Character) {
        _selectedCharacterState.value = character
    }

    fun selectButton(button: Int) {
        _selectedButtonState.value = button
    }
}

