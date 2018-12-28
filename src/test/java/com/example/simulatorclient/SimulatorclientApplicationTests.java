package com.example.simulatorclient;


import com.example.grpcserver.service.Server;
import com.example.grpcserver.service.UserRpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimulatorclientApplicationTests {

    private static ManagedChannel channel;
    /**
     * 创建一个管道
     * @return ManagedChannel 通信管道
     */
    private ManagedChannel openChannel() {
        if (channel == null || channel.isShutdown() || channel.isTerminated()) {
            synchronized (this) {
                if (channel == null || channel.isShutdown() || channel.isTerminated()) {
                    channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50010).usePlaintext(true).build();
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
        return UserRpcServiceGrpc.newBlockingStub(channel).withDeadlineAfter(5000, TimeUnit.MILLISECONDS);
    }

    private UserRpcServiceGrpc.UserRpcServiceFutureStub getFutureStub(ManagedChannel channel) {
        return UserRpcServiceGrpc.newFutureStub(channel).withDeadlineAfter(5000, TimeUnit.MILLISECONDS);
    }

    private void shutdown(ManagedChannel channel) {

        channel.shutdown();
    }

    @Test
    public void main() {
        openChannel();
        UserRpcServiceGrpc.UserRpcServiceBlockingStub stub=getBlockStub(channel);
        Server.TestRequest request= Server.TestRequest.newBuilder()
                .setId(1)
                .build();
        Server.TestResponse response =stub.getUserById(request);
        System.out.println(response);
        shutdown(channel);
    }

}

