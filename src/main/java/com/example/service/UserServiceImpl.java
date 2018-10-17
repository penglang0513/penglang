package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    public User getUserById(String userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    @Override
    public User findIdAndPassword(User user) {
        return userDao.findIdAndPassword(user);
    }

    @Override
    public String findId() {
        return userDao.findId();
    }

    @Override
    public int insertSelective(User user) {
        return userDao.insertSelective(user);
    }

    @Override
    public List<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public List<User> findAllUser() {
        return userDao.findAllUser();
    }
}
