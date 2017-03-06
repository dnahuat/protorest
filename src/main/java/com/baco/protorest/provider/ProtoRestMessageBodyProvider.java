package com.baco.protorest.provider;

import com.baco.protorest.api.ProtoRestMediaType;
import com.baco.protorest.util.ProtoRestDesSer;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Provider de serializacion deserializacion
 */
@Provider
@Produces(ProtoRestMediaType.APPLICATION_PROTOREST)
@Consumes(ProtoRestMediaType.APPLICATION_PROTOREST)
public class ProtoRestMessageBodyProvider implements MessageBodyReader, MessageBodyWriter {


    @Override
    public boolean isReadable(Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        /**
         * Verificar compatibilidad del tipo de medio
         */
        return mediaType.isCompatible(ProtoRestMediaType.APPLICATION_PROTOREST_TYPE);
    }

    @Override
    public Object readFrom(Class aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        /**
         * Verificar compatibilidad del tipo de medio
         */
        if(mediaType.isCompatible(ProtoRestMediaType.APPLICATION_PROTOREST_TYPE)) {
            return ProtoRestDesSer.readProtorest(aClass, inputStream);
        } else {
            StringBuilder sb = new StringBuilder().append("PROTOREST: '").append(mediaType.getType()).append("/").append(mediaType.getSubtype())
                    .append("' is not supported. Only 'application/protorest is supported'");
            throw new BadRequestException(sb.toString());
        }
    }

    @Override
    public boolean isWriteable(Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        /**
         * Verificar compatibilidad del tipo de medio
         */
        return mediaType.isCompatible(ProtoRestMediaType.APPLICATION_PROTOREST_TYPE);
    }

    @Override
    public long getSize(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Object o, Class aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        /**
         * Verificar compatibilidad del tipo de medio
         */
        if(mediaType.isCompatible(ProtoRestMediaType.APPLICATION_PROTOREST_TYPE)) {
            ProtoRestDesSer.writeProtorest(o, aClass, outputStream);
        } else {
            StringBuilder sb = new StringBuilder().append("PROTOREST: '").append(mediaType.getType()).append("/").append(mediaType.getSubtype())
                    .append("' is not supported. Only 'application/protorest is supported'");
            throw new BadRequestException(sb.toString());
        }
    }
}
