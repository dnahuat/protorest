package com.baco.protorest.util;

import java.io.Serializable;

/**
 * Mensaje serializado
 */
public class ProtoEnvelope implements Serializable {

    private Object message;

    protected ProtoEnvelope(Object message) {
        this.message = message;
    }

    public Object getPayload() {
        return message;
    }

}
