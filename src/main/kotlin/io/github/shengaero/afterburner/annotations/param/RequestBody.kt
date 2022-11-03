package io.github.shengaero.afterburner.annotations.param

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.reflect.*
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

@ParamProvider("RequestBody")
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestBody {
    companion object : ParamProvider.Factory<RequestBody, Any> {
        override fun generate(param: KParameter, annotation: RequestBody): ParamHandler<Any> {
            // do not reflect type over and over again
            val type = param.type
            val info = TypeInfo(type.jvmErasure, type.platformType, type)
            return handler@{
                val bodyOrNull = call.receiveNullable<Any>(info)
                checkNotNull(bodyOrNull) { "Body could not be parsed into type: $info" }
                return@handler bodyOrNull
            }
        }
    }
}
