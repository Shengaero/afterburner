package io.github.shengaero.afterburner.examples.annotations

import io.github.shengaero.afterburner.examples.Post
import io.github.shengaero.afterburner.annotations.param.ParamHandler
import io.github.shengaero.afterburner.annotations.param.ParamProvider
import io.ktor.server.application.*
import io.ktor.server.request.*

@ParamProvider("ProvidePost")
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvidePost {
    companion object : ParamProvider.SimpleFactory<ProvidePost, Post> {
        private var IDGEN = 0
        override fun generate(annotation: ProvidePost): ParamHandler<Post> = {
            call.receive<Post.Request>().toPost(++IDGEN)
        }
    }
}
