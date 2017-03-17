package com.baco.protorest.util;

import io.protostuff.*;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper methods for serialization and deserialization
 */
public class ProtoRestDesSer {

    private static Schema schema;
    private static AtomicBoolean configured = new AtomicBoolean(false);
    private static final String LOGGER_NAME = "com.baco.protorest";

    private static final void configureRuntime() {
        if(!configured.get()) {
            Logger.getLogger(LOGGER_NAME).log(Level.FINE,"SER/DES: Applying initial serializer/deserializer configuration");
            DefaultIdStrategy dis = (DefaultIdStrategy) RuntimeEnv.ID_STRATEGY;
            Logger.getLogger(LOGGER_NAME).log(Level.FINE,"SER/DES: Registering date and time delegates");
            dis.registerDelegate(TIMESTAMP_DELEGATE);
            dis.registerDelegate(DATE_DELEGATE);
            dis.registerDelegate(TIME_DELEGATE);
            Logger.getLogger(LOGGER_NAME).log(Level.FINE,"SER/DES: Registering schema for data transport envelope");
            schema = RuntimeSchema.getSchema(ProtoEnvelope.class);
            configured.set(true);
        }
    }

    /**
     * Writes protorest data
     * @param obj
     * @param klass
     * @param os
     * @throws IOException
     * @throws WebApplicationException
     */
    public static final void writeProtorest(Object obj, Class klass, OutputStream os) throws IOException, WebApplicationException {
        configureRuntime();
        ProtoEnvelope o = new ProtoEnvelope(obj);
        LinkedBuffer buffer = null;
        try {
            buffer = BufferPool.takeBuffer();
            buffer.clear();
            int size = ProtostuffIOUtil.writeDelimitedTo(os, o, schema, buffer);
            Logger.getLogger(LOGGER_NAME).log(Level.FINE,"SER/DES: " + size + "B writen. Wraped class in envelope is '" + klass.getName() + "'");
        } catch (InterruptedException ex) {
            throw new InternalServerErrorException("SER/DES: Cannot obtain a buffer to write object.", ex);
        } finally {
            if(buffer != null) {
                try {
                    BufferPool.returnBuffer(buffer);
                } catch (InterruptedException ex) {
                    throw new InternalServerErrorException("SER/DES: Cannot return a buffer to write object.", ex);
                }
            }
        }
    }

    /**
     * Reads protorest data
     * @param klass
     * @param is
     * @return
     * @throws IOException
     * @throws WebApplicationException
     */
    public static final Object readProtorest(Class klass, InputStream is) throws IOException, WebApplicationException {
        configureRuntime();
        /**
         * Obtain decompressed input stream
         */
        ProtoEnvelope result = (ProtoEnvelope) schema.newMessage();
        int size = ProtostuffIOUtil.mergeDelimitedFrom(is, result, schema);
        Object unwrappedResult = result.getPayload();
        Logger.getLogger(LOGGER_NAME).log(Level.FINER,"SER/DES: " + size + "B have been read. Unwrapped class in envelope found is '" + unwrappedResult.getClass() + "'");
        return unwrappedResult;
    }

    /**
     * TIMESTAMP DELEGATE
     */
    static final Delegate<Timestamp> TIMESTAMP_DELEGATE = new Delegate<Timestamp>() {

        public WireFormat.FieldType getFieldType() {
            return WireFormat.FieldType.FIXED64;
        }

        public Class<?> typeClass() {
            return Timestamp.class;
        }

        public Timestamp readFrom(Input input) throws IOException {
            return new Timestamp(input.readFixed64());
        }

        public void writeTo(Output output, int number, Timestamp value,
                            boolean repeated)
                throws IOException {
            output.writeFixed64(number, value.getTime(), repeated);
        }

        public void transfer(Pipe pipe, Input input, Output output, int number,
                             boolean repeated)
                throws IOException {
            output.writeFixed64(number, input.readFixed64(), repeated);
        }

    };

    /**
     * TIME DELEGATE
     */
    static final Delegate<Time> TIME_DELEGATE = new Delegate<Time>() {

        public WireFormat.FieldType getFieldType() {
            return WireFormat.FieldType.FIXED64;
        }

        public Class<?> typeClass() {
            return Time.class;
        }

        public Time readFrom(Input input) throws IOException {
            return new Time(input.readFixed64());
        }

        public void writeTo(Output output, int number, Time value,
                            boolean repeated)
                throws IOException {
            output.writeFixed64(number, value.getTime(), repeated);
        }

        public void transfer(Pipe pipe, Input input, Output output, int number,
                             boolean repeated)
                throws IOException {
            output.writeFixed64(number, input.readFixed64(), repeated);
        }

    };

    /**
     * DATE DELEGATE
     */
    static final Delegate<Date> DATE_DELEGATE = new Delegate<Date>() {

        public WireFormat.FieldType getFieldType() {
            return WireFormat.FieldType.FIXED64;
        }

        public Class<?> typeClass() {
            return Date.class;
        }

        public Date readFrom(Input input) throws IOException {
            return new Date(input.readFixed64());
        }

        public void writeTo(Output output, int number, Date value,
                            boolean repeated)
                throws IOException {
            output.writeFixed64(number, value.getTime(), repeated);
        }

        public void transfer(Pipe pipe, Input input, Output output, int number,
                             boolean repeated)
                throws IOException {
            output.writeFixed64(number, input.readFixed64(), repeated);
        }

    };

}
