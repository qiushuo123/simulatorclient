package com.example.simulatorclient.server.impl;

import com.example.grpcserver.service.Server;
import com.example.simulatorclient.server.RpcService;
import com.example.simulatorclient.server.UserService;
import com.example.simulatorclient.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("Userserver")
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final RpcService rpcService;

    @Autowired
    public UserServiceImpl(RpcService rpcService){
        this.rpcService=rpcService;
    }

    @Override
    public UserVO getUserById(int userId) {
        Server.TestResponse response=rpcService.getUserById(userId);
        UserVO userVO=new UserVO();
        userVO.setUsername(response.getUsername());
        userVO.setAge(response.getAge());
        userVO.setId(response.getId());
        return userVO;
    }
}
