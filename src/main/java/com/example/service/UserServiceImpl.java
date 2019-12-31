package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Override
    public String findLevel(String username) {
        return userDao.findLevel(username);
    }

    @Override
    public String findUserId(String email) {
        return userDao.findUserId(email);
    }

    @Override
    public int addFile(Map map) {
        return userDao.addFile(map);
    }

    @Override
    public int addUserFile(Map map) {
        return userDao.addUserFile(map);
    }

    @Override
    public List<Map<String, String>> findSC(String userId) {
        return userDao.findSC(userId);

    }

    @Override
    public List<Map> findShouC(Map map) {
        return userDao.findShouC(map);
    }

    @Override
    public Map findFtByName(Map map) {
        return userDao.findFtByName(map);
    }

    @Override
    public int addFz(Map map) {
        return userDao.addFz(map);
    }

    @Override
    public String findFileTypeName(String model) {
        return userDao.findFileTypeName(model);
    }

    @Override
    public List<Map> findNoteBookByCifSeq(Map map) {
        return userDao.findNoteBookByCifSeq(map);
    }

    @Override
    public String findNoteBookByName(Map map) {
        return userDao.findNoteBookByName(map);
    }

    @Override
    public int addNoteBook(Map map) {
        return userDao.addNoteBook(map);
    }

    @Override
    public int addNote(Map map) {
        return userDao.addNote(map);
    }

    @Override
    public List<Map> findNoteBySeq(Map map) {
        return userDao.findNoteBySeq(map);
    }

    @Override
    public Map findNoteById(Map map) {
        return userDao.findNoteById(map);
    }

    @Override
    public int updateNote(Map map) {
        return userDao.updateNote(map);
    }

    @Override
    public int updateFileInfoState(Map map) {
        return userDao.updateFileInfoState(map);
    }

    @Override
    public List<Map> findFileBySeq(Map map) {
        return userDao.findFileBySeq(map);
    }

    @Override
    public int updateFileInfoStateByseq(Map map) {
        return userDao.updateFileInfoStateByseq(map);
    }

    @Override
    public int updateFileTypeStateByseq(Map map) {
        return userDao.updateFileTypeStateByseq(map);
    }

    @Override
    public int updateNoteByNoteId(Map map) {
        return userDao.updateNoteByNoteId(map);
    }

    @Override
    public int updateNoteByNbseq(Map map) {
        return userDao.updateNoteByNbseq(map);
    }

    @Override
    public int updateNoteBookByNbseq(Map map) {
        return userDao.updateNoteBookByNbseq(map);
    }
}
