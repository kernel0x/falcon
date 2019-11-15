package com.kernel.falcon.utils;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ByteUtil {

    private ByteUtil() {
    }

    @Nullable
    public static byte[] encode(Object object) {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("Object must be serializable");
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(object);
                out.flush();
                return bos.toByteArray();
            } finally {
                bos.close();
            }
        } catch (IOException ex) {
            // ignore
        }
        return null;
    }

    @Nullable
    public static Object decode(byte[] array) {
        ByteArrayInputStream bis = new ByteArrayInputStream(array);
        ObjectInput in = null;
        try {
            try {
                in = new ObjectInputStream(bis);
                return in.readObject();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException ex) {
            // ignore
        } catch (ClassNotFoundException e) {
            // ignore
        }
        return null;
    }

}
