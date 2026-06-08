package com.zgamelogic.app.servermanager.rcon;

import com.zgamelogic.app.servermanager.db.MinecraftServerData;
import com.zgamelogic.app.servermanager.db.MinecraftServerDataRepository;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RconService {

    private static final int LOGIN_TYPE = 3;
    private static final int COMMAND_TYPE = 2;
    private static final int LOGIN_REQUEST_ID = 1;
    private static final long LOGIN_TIMEOUT_SECONDS = 5L;
    private static final long COMMAND_TIMEOUT_SECONDS = 8L;

    private final MinecraftServerDataRepository msdr;
    private final EventLoopGroup group;
    private final ConcurrentHashMap<UUID, RconClient> rconConnections;

    public RconService(MinecraftServerDataRepository msdr) {
        this.msdr = msdr;
        this.group = new NioEventLoopGroup();
        this.rconConnections = new ConcurrentHashMap<>();

        msdr.findAll().forEach(server -> {
            try {
                connectIfNeeded(server);
            } catch (Exception _) {}
        });
    }

    public String sendCommand(MinecraftServerData mcServer, String command) {
        RconClient client = connectIfNeeded(mcServer);

        int requestId = client.nextRequestId.incrementAndGet();
        CompletableFuture<String> responseFuture = new CompletableFuture<>();
        client.pendingResponses.put(requestId, responseFuture);

        try {
            ByteBufHolder.writeAndFlush(client.channel, requestId, COMMAND_TYPE, command);
            return responseFuture.get(COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            client.pendingResponses.remove(requestId);
            throw new RuntimeException("Timed out waiting for RCON response", e);
        } catch (Exception e) {
            client.pendingResponses.remove(requestId);
            throw new RuntimeException("Failed to send RCON command", e);
        }
    }

    private RconClient connectIfNeeded(MinecraftServerData server) {
        return rconConnections.compute(server.getId(), (id, existing) -> {
            if (existing != null && existing.channel != null && existing.channel.isActive()) {
                return existing;
            }
            return createAndLogin(server);
        });
    }

    private RconClient createAndLogin(MinecraftServerData server) {
        String host = "127.0.0.1";
        int port = server.getRconPort();
        String password = server.getRconPass();

        ConcurrentHashMap<Integer, CompletableFuture<String>> pendingResponses = new ConcurrentHashMap<>();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new RconHandler(pendingResponses));
                        }
                    });

            Channel channel = bootstrap.connect(host, port).sync().channel();

            CompletableFuture<String> loginFuture = new CompletableFuture<>();
            pendingResponses.put(LOGIN_REQUEST_ID, loginFuture);

            ByteBufHolder.writeAndFlush(channel, LOGIN_REQUEST_ID, LOGIN_TYPE, password);

            try {
                loginFuture.get(LOGIN_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                pendingResponses.remove(LOGIN_REQUEST_ID);
                channel.close();
                throw new RuntimeException("Timed out during RCON login for server " + server.getId(), e);
            }

            return new RconClient(channel, pendingResponses, new AtomicInteger(LOGIN_REQUEST_ID + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while connecting to RCON for server " + server.getId(), e);
        } catch (CompletionException e) {
            throw new RuntimeException("RCON login failed for server " + server.getId(), e.getCause());
        } catch (Exception e) {
            throw new RuntimeException("Could not establish RCON connection for server " + server.getId(), e);
        }
    }


    private static class RconClient {
        private final Channel channel;
        private final ConcurrentHashMap<Integer, CompletableFuture<String>> pendingResponses;
        private final AtomicInteger nextRequestId;

        private RconClient(
                Channel channel,
                ConcurrentHashMap<Integer, CompletableFuture<String>> pendingResponses,
                AtomicInteger nextRequestId
        ) {
            this.channel = channel;
            this.pendingResponses = pendingResponses;
            this.nextRequestId = nextRequestId;
        }
    }

    // Small helper to avoid repeating packet creation boilerplate
    private static class ByteBufHolder {
        private static void writeAndFlush(Channel channel, int requestId, int type, String payload) {
            channel.writeAndFlush(RconPacketUtil.createPacket(requestId, type, payload));
        }
    }
}