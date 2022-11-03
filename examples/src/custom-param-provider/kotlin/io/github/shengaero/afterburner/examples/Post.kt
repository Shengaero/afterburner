package io.github.shengaero.afterburner.examples

import kotlinx.serialization.Serializable

@Serializable
data class Post(val id: Int, val message: String) {
    @Serializable
    data class Request(val message: String) {
        fun toPost(id: Int): Post = Post(id, message)
    }
}
