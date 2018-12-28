package com.example.simulatorclient.server;

import com.example.grpcserver.service.Server;

public interface RpcService {

    Server.TestResponse getUserById(int id);
}
