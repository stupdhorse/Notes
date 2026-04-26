package com.ola.noteBook.notes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
data class NoteRequest(
    val title: String,
    val content: String,
    val color : Long
)

data class NoteResponse(
    val id: String,
    val title: String,
    val content: String,
    val color: Long,
    val createdAt : Instant
)
@RestController
@RequestMapping("/notes")
public class NoteController(private val noteService: NoteService) {


    @PostMapping
    suspend fun create(@RequestBody body: NoteRequest) : NoteResponse{
        val ownerId = "tymaczasowyId"
        val command = CreateNoteCommand(
            title = body.title,
            content = body.content,
            color = body.color,
            ownerId = ownerId
        )
        val savedNote = noteService.createNote(command)
        return NoteResponse(
            id = savedNote.id?:throw IllegalStateException("Database didn't generate id"),
            title = savedNote.title,
            content = savedNote.content,
            color = savedNote.color,
            createdAt = savedNote.createdAt?: throw IllegalStateException("Database didn't generate created at date")
        )
    }
    @GetMapping
    fun get(@RequestParam ownerId : String) : Flow<NoteResponse> {
        val response = noteService.getUserNotes(ownerId)
        return response.map {note ->
            NoteResponse(
                id = note.id?: throw IllegalStateException("Note from DB has no id"),
                title = note.title,
                content = note.content,
                color = note.color,
                createdAt = note.createdAt?: throw IllegalStateException("Note from DB has no creation date")
            )
        }
    }
    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id : String) {
        noteService.deleteNote(id)
    }
}