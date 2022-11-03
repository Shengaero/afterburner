package io.github.shengaero.afterburner.examples

import io.github.shengaero.afterburner.annotations.RequestHandler
import io.github.shengaero.afterburner.annotations.RouteHandler
import io.github.shengaero.afterburner.annotations.param.RequestBody
import io.github.shengaero.afterburner.annotations.param.RouteCall
import io.github.shengaero.afterburner.annotations.param.URLPath
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

@RouteHandler("/users")
class UserRoutes {
    private val users = hashMapOf<Int, User>()

    @RequestHandler(RequestHandler.Method.GET)
    suspend fun getUsers(@RouteCall call: ApplicationCall) {
        val users = users.values.toList()
        call.respond(users)
    }

    @RequestHandler(RequestHandler.Method.GET, "/{$USER_ID}")
    suspend fun getUser(@RouteCall call: ApplicationCall, @URLPath(USER_ID) userId: String) {
        val userIdInt = userId.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        val user = users[userIdInt] ?: return call.respond(HttpStatusCode.NotFound)
        call.respond(user)
    }

    @RequestHandler(RequestHandler.Method.POST)
    suspend fun postUser(@RouteCall call: ApplicationCall, @RequestBody user: User) {
        if(user.id in users) return call.respond(HttpStatusCode.BadRequest)
        users += user.id to user
        call.respond(HttpStatusCode.Created)
    }

    @RequestHandler(RequestHandler.Method.DELETE, "/{$USER_ID}")
    suspend fun deleteUser(@RouteCall call: ApplicationCall, @URLPath(USER_ID) userId: String) {
        val userIdInt = userId.toIntOrNull() ?: return call.respond(HttpStatusCode.BadRequest)
        if(userIdInt !in users) return call.respond(HttpStatusCode.OK)
        users -= userIdInt
        call.respond(HttpStatusCode.NoContent)
    }

    companion object {
        const val USER_ID = "user_id"
    }
}
