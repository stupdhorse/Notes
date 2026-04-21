package com.ola.noteBook.notes

import jakarta.validation.constraints.NotBlank
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(collection = "notes")
data class Note (
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val content: String,
    val color: Long,
    @CreatedDate
    @Field("created_at")
    val createdAt: Instant? = null,
    @Id
    val id: String? = null,
    val ownerId : String
)