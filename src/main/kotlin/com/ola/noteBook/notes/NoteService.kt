package com.ola.noteBook.notes

import org.springframework.stereotype.Service

data class CreateNoteCommand(
    val title: String,
    val content: String,
    val color : Long,
    val ownerId : String
)
@Service
class NoteService(private val noteRepository : NoteRepository) {
    fun createNote(command: CreateNoteCommand) : Note{
        val newNote = Note(
            title = command.title,
            content = command.content,
            color = command.color,
            ownerId = command.ownerId
        )
        return noteRepository.save(newNote)
    }

    fun getUserNotes(ownerId: String) : List<Note>{
        return noteRepository.findByOwnerId(ownerId = ownerId)
    }
    fun deleteNote(noteId: String){
        noteRepository.deleteById(noteId)
    }
}