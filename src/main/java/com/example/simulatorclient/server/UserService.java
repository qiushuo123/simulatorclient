package com.example.simulatorclient.server;

import com.example.simulatorclient.vo.UserVO;

public interface UserService {
    public UserVO getUserById(int userId);
}
