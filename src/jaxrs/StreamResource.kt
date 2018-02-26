package jaxrs

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import kotlin.coroutines.experimental.buildSequence
import kotlin.streams.asStream

@Path("stream")
class StreamResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun stream() = controlledSequence().asStream().onClose { println("CLOSING STREAM") }

    fun controlledSequence() = buildSequence {
        for (i in 1..5) {
            if (i == 4) {
                throw Exception("test")
            }

            yield(i)
        }
    }
}
