package io.github.shengaero.afterburner.examples

import io.github.shengaero.afterburner.examples.annotations.ProvidePost
import io.github.shengaero.afterburner.annotations.RequestHandler
import io.github.shengaero.afterburner.annotations.RouteHandler
import io.github.shengaero.afterburner.annotations.param.RouteCall
import io.ktor.server.application.*
import io.ktor.server.response.*

@RouteHandler("/posts")
class PostRoutes {
    private val posts = mutableListOf<Post>()

    @RequestHandler(RequestHandler.Method.GET)
    suspend fun getPosts(@RouteCall call: ApplicationCall) {
        call.respond(posts.toList())
    }

    @RequestHandler(RequestHandler.Method.POST)
    suspend fun postPost(@RouteCall call: ApplicationCall, @ProvidePost post: Post) {
        posts += post
        call.respond(post)
    }
}
