/*
 *   Copyright (c) 2012, Deiby Dathat Nahuat Uc
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  3. All advertising materials mentioning features or use of this software
 *  must display the following acknowledgement:
 *  This product includes software developed by Deiby Dathat Nahuat.
 *  4. Neither the name of Deiby Dathat Nahuat Uc nor the
 *  names of its contributors may be used to endorse or promote products
 *  derived from this software without specific prior written permission.

 *  THIS SOFTWARE IS PROVIDED BY DEIBY DATHAT NAHUAT UC ''AS IS'' AND ANY
 *  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL DEIBY DATHAT NAHUAT UC BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package com.baco.protorest.util;

import io.protostuff.LinkedBuffer;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Pool of LinkedBuffers implemented through a Deque
 *
 * @author deiby.nahuat
 */
public class BufferPool {

    public static final int MAX_CURRENT_BUFFERS = 100;
    private static BufferPool instance;
    private final LinkedBlockingDeque<LinkedBuffer> bufferDeque;
    private static final String LOGGER_NAME = "com.baco.protorest";

    private BufferPool() {
        bufferDeque = new LinkedBlockingDeque<LinkedBuffer>(
            MAX_CURRENT_BUFFERS);
        for (int i = 0; i < MAX_CURRENT_BUFFERS; i++) {
            bufferDeque.push(LinkedBuffer.allocate(
                    LinkedBuffer.DEFAULT_BUFFER_SIZE));
        }
        Logger.getLogger(LOGGER_NAME).log(Level.FINE,"BUFFERPOOL: The pool has been created.");
    }

    public static final void resetPool() {
        if(instance != null) {
            instance.bufferDeque.clear();
            instance = new BufferPool();
            Logger.getLogger(LOGGER_NAME).log(Level.FINE,"BUFFERPOOL: The pool has been reset.");
        }
    }

    public static final int availableBuffers() {
        if(instance == null) {
            instance = new BufferPool();
        }
        return instance.bufferDeque.size();
    }

    public static final LinkedBuffer takeBuffer() throws InterruptedException {
        if(instance == null) {
            instance = new BufferPool();
        }
        try {
            LinkedBuffer buffer = instance.bufferDeque.poll(1, TimeUnit.MINUTES);
            if (buffer != null) {
                buffer.clear();
                return buffer;
            } else {
                throw new InterruptedException("Cannot obtain a buffer after waiting 1 minute.");
            }
        } finally {
            if(instance != null) {
                Logger.getLogger(LOGGER_NAME).log(Level.FINE, "BUFFERPOOL: Buffer taken. The pool has " + instance.bufferDeque.size() + "' buffer's availables now.");
            }
        }
    }

    public static final void returnBuffer(LinkedBuffer buffer) throws InterruptedException {
        if(instance == null) {
            instance = new BufferPool();
        }
        try {
            buffer.clear();
            if (!instance.bufferDeque.offer(buffer, 1, TimeUnit.MINUTES)) {
                throw new InterruptedException("Cannot return a buffer to pool after waiting 1 minute.");
            }
        } finally {
            if(instance != null) {
                Logger.getLogger(LOGGER_NAME).log(Level.FINE, "BUFFERPOOL: Buffer taken. The pool has " + instance.bufferDeque.size() + "' buffer's availables now.");
            }
        }
    }

}
