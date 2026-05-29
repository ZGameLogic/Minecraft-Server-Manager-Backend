package com.zgamelogic.app.msm.rcon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RconHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private final ConcurrentHashMap<Integer, CompletableFuture<String>> pendingResponses;

    public RconHandler(ConcurrentHashMap<Integer, CompletableFuture<String>> pendingResponses) {
        this.pendingResponses = pendingResponses;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        // RCON responses can arrive as one or more frames in a ByteBuf.
        // Parse as many complete packets as are currently available.
        while (msg.readableBytes() >= 4) {
            msg.markReaderIndex();

            int length = msg.readIntLE();
            if (msg.readableBytes() < length) {
                // Wait for remaining bytes
                msg.resetReaderIndex();
                return;
            }

            int requestId = msg.readIntLE();
            int type = msg.readIntLE();

            int payloadLength = length - 10; // minus requestId(4), type(4), two null bytes(2)
            if (payloadLength < 0) {
                // Corrupt packet, skip this frame
                msg.skipBytes(Math.max(length - 8, 0));
                continue;
            }

            byte[] payloadBytes = new byte[payloadLength];
            msg.readBytes(payloadBytes);

            // Consume two trailing null bytes if present
            if (msg.readableBytes() >= 2) {
                msg.readByte();
                msg.readByte();
            }

            String payload = new String(payloadBytes, StandardCharsets.UTF_8);

            CompletableFuture<String> future = pendingResponses.remove(requestId);
            if (future != null) {
                if (requestId == -1) {
                    future.completeExceptionally(new IllegalStateException("RCON authentication failed"));
                } else {
                    future.complete(payload);
                }
            } else {
                // Optional: you can log unmatched packets here if needed
                // System.out.println("Unmatched RCON packet type=" + type + " requestId=" + requestId + " payload=" + payload);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Fail all pending futures on channel error
        for (CompletableFuture<String> future : pendingResponses.values()) {
            future.completeExceptionally(cause);
        }
        pendingResponses.clear();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // Fail all pending futures if connection drops
        IllegalStateException ex = new IllegalStateException("RCON channel closed");
        for (CompletableFuture<String> future : pendingResponses.values()) {
            future.completeExceptionally(ex);
        }
        pendingResponses.clear();
    }
}