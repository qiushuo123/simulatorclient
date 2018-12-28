package com.example.simulatorclient.server.impl;

import com.example.grpcserver.service.Server;
import com.example.grpcserver.service.UserRpcServiceGrpc;
import com.example.simulatorclient.server.RpcService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("RpcServer")
public class RpcServiceImpl implements RpcService {
    private static final Logger logger = LoggerFactory.getLogger(RpcServiceImpl.class);

    @Value("${grpcserver.host}")
    private String host;
    @Value("${grpcserver.port}")
    private int port;
    @Value("${grpcserver.timeout}")
    private long timeout;

    private static ManagedChannel channel;

    /**
     * 创建一个管道
     * @return ManagedChannel 通信管道
     */
    private ManagedChannel openChannel() {
        if (channel == null || channel.isShutdown() || channel.isTerminated()) {
            synchronized (this) {
                if (channel == null || channel.isShutdown() || channel.isTerminated()) {
                    channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
                }
            }
        }
        return channel;
    }

    /**
     * 获取一个设置了默认超时时间的stub
     * @param channel   ManagedChannel  通信管道
     * @return LinkServiceBlockingStub  stub
     */
    private UserRpcServiceGrpc.UserRpcServiceBlockingStub getBlockStub(ManagedChannel channel) {
        return UserRpcServiceGrpc.newBlockingStub(channel).withDeadlineAfter(timeout, TimeUnit.MILLISECONDS);
    }

    private UserRpcServiceGrpc.UserRpcServiceFutureStub getFutureStub(ManagedChannel channel) {
        return UserRpcServiceGrpc.newFutureStub(channel).withDeadlineAfter(timeout, TimeUnit.MILLISECONDS);
    }

    private void shutdown(ManagedChannel channel) {

        channel.shutdown();
    }

    @Override
    public Server.TestResponse getUserById(int id){
        logger.debug("RPC -> getUserById id : {}",id);
        openChannel();
        UserRpcServiceGrpc.UserRpcServiceBlockingStub stub=getBlockStub(channel);
        Server.TestRequest request= Server.TestRequest.newBuilder()
                .setId(id)
                .build();
        Server.TestResponse response =stub.getUserById(request);
        logger.debug("RPC -> getUserById -> response: {}", response);
        return response;

    }
}
