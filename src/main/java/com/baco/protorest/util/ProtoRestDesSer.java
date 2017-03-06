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

/**
 * Metodos para serializacion y deseralizacion
 */
public class ProtoRestDesSer {

    public static final void writeProtorest(Object o, Class klass, OutputStream os) throws IOException, WebApplicationException {
        /**
         * Prepare schemas
         */
        DefaultIdStrategy dis = (DefaultIdStrategy) RuntimeEnv.ID_STRATEGY;
        dis.registerDelegate(TIMESTAMP_DELEGATE);
        dis.registerDelegate(DATE_DELEGATE);
        dis.registerDelegate(TIME_DELEGATE);
        Schema schema = RuntimeSchema.getSchema(klass);
        LinkedBuffer buffer = null;
        int outputLen = 0;

        try {
            buffer = BufferPool.takeBuffer();
            buffer.clear();
            outputLen = ProtostuffIOUtil.writeDelimitedTo(os, o, schema, buffer);
        } catch (InterruptedException ex) {
            throw new InternalServerErrorException("PROTOREST: Cannot obtain a buffer to write object.");
        } finally {
            if(buffer != null) {
                try {
                    BufferPool.returnBuffer(buffer);
                } catch (InterruptedException ex) {
                    throw new InternalServerErrorException("PROTOREST: Cannot return a buffer to write object.");
                }
            }
        }
    }

    public static final Object readProtorest(Class klass, InputStream is) throws IOException, WebApplicationException {
        /**
         * Prepare schemas
         */
        DefaultIdStrategy dis = (DefaultIdStrategy) RuntimeEnv.ID_STRATEGY;
        dis.registerDelegate(TIMESTAMP_DELEGATE);
        dis.registerDelegate(DATE_DELEGATE);
        dis.registerDelegate(TIME_DELEGATE);
        Schema schema = RuntimeSchema.getSchema(klass);
        /**
         * Obtain decompressed input stream
         */
        Object result = schema.newMessage();
        ProtostuffIOUtil.mergeDelimitedFrom(is, result, schema);
        return result;
    }

    private static final Object getFromProtorest(byte[] protorest, Class klass) throws IOException {
        /**
         * Prepare schemas
         */
        DefaultIdStrategy dis = (DefaultIdStrategy) RuntimeEnv.ID_STRATEGY;
        dis.registerDelegate(TIMESTAMP_DELEGATE);
        dis.registerDelegate(DATE_DELEGATE);
        dis.registerDelegate(TIME_DELEGATE);
        Schema schema = RuntimeSchema.getSchema(klass);

        ByteArrayInputStream is = new ByteArrayInputStream(protorest);
        Object result = schema.newMessage();
        ProtostuffIOUtil.mergeDelimitedFrom(is, result, schema);
        is.close();
        return result;
    }

    private static final byte[] getProtorest(Object o, Class klass) throws IOException {
        /**
         * Prepare schemas
         */
        DefaultIdStrategy dis = (DefaultIdStrategy) RuntimeEnv.ID_STRATEGY;
        dis.registerDelegate(TIMESTAMP_DELEGATE);
        dis.registerDelegate(DATE_DELEGATE);
        dis.registerDelegate(TIME_DELEGATE);
        Schema schema = RuntimeSchema.getSchema(klass);

        LinkedBuffer buffer = null;
        byte[] protorest;
        try {
            buffer = BufferPool.takeBuffer();
            buffer.clear();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeDelimitedTo(os, o, schema, buffer);
            os.close();
            protorest = os.toByteArray();
            return protorest;
        } catch (InterruptedException ex) {
            throw new IOException("PROTOREST: Cannot obtain a buffer to write object.");
        } finally {
            if(buffer != null) {
                try {
                    BufferPool.returnBuffer(buffer);
                } catch(InterruptedException ex ) {
                    throw new IOException("PROTOREST: Cannot return a buffer to write object.");
                }
            }
        }
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
