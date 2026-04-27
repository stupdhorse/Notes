package com.ola.noteBook.notes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class NoteService(private val noteRepository : NoteRepository) {
    suspend fun createNote(command: CreateNoteCommand) : NoteResponse{
        val newNote = Note(
            title = command.title,
            content = command.content,
            color = command.color,
            ownerId = command.ownerId
        )
        val savedNote = noteRepository.save(newNote)
        return NoteResponse(
            id = savedNote.id?: throw IllegalStateException("Database didn't generate id"),
            title = savedNote.title,
            content = savedNote.content,
            color = savedNote.color,
            createdAt = savedNote.createdAt?: throw IllegalStateException("Database didn't generate creation date")
        )
    }

    fun getUserNotes(ownerId: String) : Flow<NoteResponse> {
        val notes = noteRepository.findByOwnerId(ownerId = ownerId)
        return notes.map { note -> NoteResponse(
            id = note.id?: throw IllegalStateException("Note from DB has no id"),
            title = note.title,
            content = note.content,
            color = note.color,
            createdAt = note.createdAt?: throw IllegalStateException("Note from DB has no creation date")
        )
        }
    }
    suspend fun deleteNote(noteId: String){
        noteRepository.deleteById(noteId)
    }
    fun getBacklinks(noteId: String) : Flow<NoteSummaryResponse>
    {
        val notes = noteRepository.findByBacklinksContains(noteId)
        return notes.map { note -> NoteSummaryResponse(
            id = note.id,
            title = note.title,
        ) }
    }
}