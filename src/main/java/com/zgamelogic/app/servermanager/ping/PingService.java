package com.zgamelogic.app.servermanager.ping;

import com.zgamelogic.app.servermanager.db.MinecraftServerData;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class PingService {
    public Optional<PingData> pingServer(MinecraftServerData mcServer){
        String hostname = "localhost";
        int port = mcServer.getPort();

        try (Socket socket = new Socket(hostname, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Handshake packet
            ByteArrayOutputStream handshake = new ByteArrayOutputStream();
            DataOutputStream handshakeData = new DataOutputStream(handshake);
            writeVarInt(handshakeData, 0x00);           // packet id
            writeVarInt(handshakeData, 767); // e.g. 767 for 1.21.x, or supported value
            writeString(handshakeData, hostname);
            handshakeData.writeShort(port);             // unsigned short, big-endian is fine
            writeVarInt(handshakeData, 1);
            byte[] handshakeBytes = handshake.toByteArray();
            writeVarInt(out, handshakeBytes.length);
            out.write(handshakeBytes);

            // Status request packet
            ByteArrayOutputStream statusRequest = new ByteArrayOutputStream();
            DataOutputStream statusData = new DataOutputStream(statusRequest);
            statusData.writeByte(0x00);

            byte[] statusBytes = statusRequest.toByteArray();
            writeVarInt(out, statusBytes.length);
            out.write(statusBytes);
            out.flush();

            // Read response
            int responseLength = readVarInt(in);
            int packetId = readVarInt(in);

            if (packetId == 0x00) {
                int jsonLength = readVarInt(in);
                byte[] jsonBytes = new byte[jsonLength];
                in.readFully(jsonBytes);
                String jsonResponse = new String(jsonBytes, StandardCharsets.UTF_8);
                ObjectMapper objectMapper = new ObjectMapper();
                PingData pd = objectMapper.readValue(jsonResponse, PingData.class);
                return Optional.of(pd);
            }
        } catch (IOException _) {}
        return Optional.empty();
    }

    private void writeString(DataOutputStream out, String str) throws IOException {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out, bytes.length);
        out.write(bytes);
    }

    private void writeVarInt(DataOutputStream out, int value) throws IOException {
        while ((value & 0xFFFFFF80) != 0) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value & 0x7F);
    }

    private int readVarInt(DataInputStream in) throws IOException {
        int value = 0;
        int size = 0;
        int b;
        while (((b = in.readByte()) & 0x80) == 0x80) {
            value |= (b & 0x7F) << (size++ * 7);
        }
        return value | ((b & 0x7F) << (size * 7));
    }
}
