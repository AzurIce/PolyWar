package com.azurice.polywar.network.packet;

import java.io.*;
import java.util.List;

public abstract class AbstractObjectPacket extends Packet {
    public AbstractObjectPacket() {
        super();
    }

    public AbstractObjectPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public AbstractObjectPacket(byte[] data) {
        super(data);
    }

    protected static byte[] bytesOf(Object object) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public abstract Type getType();

    public Object getObject() {
        Object object;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                object = objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    @Override
    public String toString() {
        return getObject().toString();
    }
}
