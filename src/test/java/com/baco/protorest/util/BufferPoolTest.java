package com.baco.protorest.util;

import io.protostuff.LinkedBuffer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by deiby.nahuat on 16/03/2017.
 */
public class BufferPoolTest {

    @Before
    public void before() {
        BufferPool.resetPool();
    }

    @Test
    public void takeMaxBuffersTest() throws Exception {
        List<LinkedBuffer> buffers = new ArrayList();

        for(int i = 0; i < BufferPool.MAX_CURRENT_BUFFERS; ++i) {
            buffers.add(BufferPool.takeBuffer());
        }
        /**
         * Verify max buffers were obtained
         */
        assertEquals(BufferPool.MAX_CURRENT_BUFFERS, buffers.size());
    }

    @Test
    public void isEmptyAfterTakeAllTest() throws Exception {
        List<LinkedBuffer> buffers = new ArrayList();

        for(int i = 0; i < BufferPool.MAX_CURRENT_BUFFERS; ++i) {
            buffers.add(BufferPool.takeBuffer());
        }
        /**
         * Verify no buffers left in pool
         */
        assertEquals(0, BufferPool.availableBuffers());
    }

    @Test
    public void containsMaxBuffersTest() throws Exception {
        assertEquals(BufferPool.MAX_CURRENT_BUFFERS, BufferPool.availableBuffers());
    }

    @Test(expected =  InterruptedException.class)
    public void testMaxBufferTakeErrorTest() throws Exception {
        List<LinkedBuffer> buffers = new ArrayList();
        for(int i = 0; i < BufferPool.MAX_CURRENT_BUFFERS + 1; ++i) {
            buffers.add(BufferPool.takeBuffer());
        }
    }

    @Test(expected = InterruptedException.class)
    public void testMaxBufferReturnErrorTest() throws Exception {
        LinkedBuffer buffer = LinkedBuffer.allocate();
        BufferPool.returnBuffer(buffer);
    }

}