package com.zgamelogic.app.servermanager.rcon;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class RconPacketUtil {

    private RconPacketUtil() {}

    public static ByteBuf createPacket(int requestId, int type, String payload) {
        byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);

        ByteBuf buf = Unpooled.buffer();

        // Packet length excludes the first 4 bytes (the length field itself)
        int length = 4 + 4 + payloadBytes.length + 2;

        buf.writeIntLE(length);
        buf.writeIntLE(requestId);
        buf.writeIntLE(type);
        buf.writeBytes(payloadBytes);

        // Two null terminators per RCON protocol
        buf.writeByte(0);
        buf.writeByte(0);

        return buf;
    }
}