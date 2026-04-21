package com.ola.noteBook

import com.ola.noteBook.notes.CreateNoteCommand
import com.ola.noteBook.notes.Note
import com.ola.noteBook.notes.NoteRepository
import com.ola.noteBook.notes.NoteService
import org.junit.jupiter.api.Test
import java.time.Instant
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals

class NoteServiceTest {
    private val noteRepository = mockk<NoteRepository>()
    private val noteService = NoteService(noteRepository)

    @Test
    fun `should create and save new note`(){
        val title = "zakupy"
        val content = "zrobic zakupy"
        val color = 123L
        val ownerId = "testOwner123"
        val command = CreateNoteCommand(title = title,content=content,color=color,ownerId=ownerId)
        every {noteRepository.save(any())} answers{
            val savedNote = firstArg<Note>()
            savedNote.copy(id="wygenerowane_id_1", createdAt = Instant.now())
        }

        val result = noteService.createNote(command)

        assertEquals(title, result.title)
        assertEquals(content,result.content)
        assertEquals(color,result.color)
        assertEquals(ownerId,result.ownerId)
        assertNotNull(result.id)
        assertNotNull(result.createdAt)

        verify(exactly = 1){
            noteRepository.save(withArg { noteToSave ->
                assertEquals(title,noteToSave.title)
                assertEquals(content,noteToSave.content)
                assertEquals(color,noteToSave.color)
                assertEquals(ownerId,noteToSave.ownerId)

                assertNull(noteToSave.id)
                assertNull(noteToSave.createdAt)
            })
        }
    }

    @Test
    fun `should get all user's notes`() {
        val ownerId = "PierwszyWłaściciel"
        val expectedNotes = listOf(
            Note(title = "pierwsza", content = "pierwsza treść", color = 1L, ownerId=ownerId, id="note1"),
            Note(title = "druga", content = "druga treść", color = 1L, ownerId=ownerId, id="note2")
        )
        every{noteRepository.findByOwnerId(ownerId)} returns expectedNotes

        val result = noteService.getUserNotes(ownerId)

        assertEquals(2,result.size)
        assertEquals(expectedNotes,result)

        verify(exactly = 1){
            noteRepository.findByOwnerId(ownerId)
        }
    }

}