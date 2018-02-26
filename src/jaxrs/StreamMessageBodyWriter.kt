package jaxrs

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.OutputStream
import java.lang.reflect.Type
import java.util.stream.Stream
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.ext.MessageBodyWriter
import javax.ws.rs.ext.Provider

@Provider
class StreamMessageBodyWriter : MessageBodyWriter<Stream<Any>> {
    override fun isWriteable(type: Class<*>,
                             genericType: Type,
                             annotations: Array<out Annotation>,
                             mediaType: MediaType): Boolean {
        return mediaType == MediaType.APPLICATION_JSON_TYPE
    }

    override fun writeTo(stream: Stream<Any>,
                         type: Class<*>,
                         genericType: Type,
                         annotations: Array<out Annotation>,
                         mediaType: MediaType,
                         httpHeaders: MultivaluedMap<String, Any>,
                         entityStream: OutputStream) {
        val factory = JsonFactory(ObjectMapper())

        val generator = factory.createGenerator(entityStream, JsonEncoding.UTF8)

        generator.writeStartArray()

        try {
            stream.use {
                it.forEach {
                    generator.writeObject(it)
                    entityStream.flush()
                }
            }
        } catch (e:Exception) { }

        generator.writeEndArray()
        generator.close()
    }
}