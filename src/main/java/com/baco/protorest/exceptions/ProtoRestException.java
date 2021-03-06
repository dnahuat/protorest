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
package com.baco.protorest.exceptions;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Proto RPC exception
 *
 * @author deiby.nahuat
 */
public class ProtoRestException
        extends Exception {

    private final String serializableStacktrace;
    private String message;

    public ProtoRestException(final String message) {
        super(message);
        serializableStacktrace = "";
    }

    public ProtoRestException(final String message, final Throwable cause) {
        super(message);
        serializableStacktrace = (cause == null) ? "" : stacktraceToString(cause);
    }

    /**
     * Obtener el stacktrace en formato serializable
     *
     * @return Cadena de stacktrace
     */
    protected String getSerializableStacktrace() {
        return serializableStacktrace;
    }

    @Override
    public String getMessage() {
        if (message == null) {
            message = new StringBuilder().append(super.getMessage()).append(
                    "\n").append("REMOTE_STACKTRACE: ").append(
                            getSerializableStacktrace())
                    .toString();
        }
        return message;
    }

    @Override
    public void printStackTrace() {
        System.err.println(serializableStacktrace);
    }

    @Override
    public void printStackTrace(PrintStream s) {
        s.println(serializableStacktrace);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        s.println(serializableStacktrace);
    }

    public static final String stacktraceToString(Throwable thr) {
        StringWriter sw = new StringWriter();
        thr.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
