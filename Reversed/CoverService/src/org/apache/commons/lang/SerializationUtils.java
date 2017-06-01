package org.apache.commons.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class SerializationUtils {
    public static Object clone(Serializable object) {
        return deserialize(serialize(object));
    }

    public static void serialize(Serializable obj, OutputStream outputStream) {
        Throwable ex;
        Throwable th;
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out = null;
        try {
            ObjectOutputStream out2 = new ObjectOutputStream(outputStream);
            try {
                out2.writeObject(obj);
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (IOException e) {
                    }
                }
            } catch (IOException e2) {
                ex = e2;
                out = out2;
                try {
                    throw new SerializationException(ex);
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            ex = e4;
            throw new SerializationException(ex);
        }
    }

    public static byte[] serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        serialize(obj, baos);
        return baos.toByteArray();
    }

    public static Object deserialize(InputStream inputStream) {
        Throwable ex;
        Throwable th;
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream objectInputStream = null;
        try {
            ObjectInputStream in = new ObjectInputStream(inputStream);
            try {
                Object readObject = in.readObject();
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
                return readObject;
            } catch (ClassNotFoundException e2) {
                ex = e2;
                objectInputStream = in;
                try {
                    throw new SerializationException(ex);
                } catch (Throwable th2) {
                    th = th2;
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e3) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e4) {
                ex = e4;
                objectInputStream = in;
                throw new SerializationException(ex);
            } catch (Throwable th3) {
                th = th3;
                objectInputStream = in;
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                throw th;
            }
        } catch (ClassNotFoundException e5) {
            ex = e5;
            throw new SerializationException(ex);
        } catch (IOException e6) {
            ex = e6;
            throw new SerializationException(ex);
        }
    }

    public static Object deserialize(byte[] objectData) {
        if (objectData != null) {
            return deserialize(new ByteArrayInputStream(objectData));
        }
        throw new IllegalArgumentException("The byte[] must not be null");
    }
}
