package com.example.dao;

import com.example.entity.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    User selectByPrimaryKey(String id);
    User findIdAndPassword(User user);
    String findId();
    int insertSelective(User user);
    List<User> findByEmail(String email);
    List<User> findAllUser();
    User find();
    String findLevel(String username);
    String findUserId(String email);
    int addFile(Map map);
    int addUserFile(Map map);
    List<Map<String,String>> findSC(String userId);
    List<Map> findShouC(Map map);

    Map findFtByName(Map map);
    int addFz(Map map);
    String findFileTypeName(String model);
    List<Map> findNoteBookByCifSeq(Map map);
    String findNoteBookByName(Map map);
    int addNoteBook(Map map);
    int addNote(Map map);
    List<Map> findNoteBySeq(Map map);
    Map findNoteById(Map map);
    int updateNote(Map map);

    int updateFileInfoState(Map map);

    List<Map> findFileBySeq(Map map);

    int updateFileInfoStateByseq(Map map);
    int updateFileTypeStateByseq(Map map);

    int updateNoteByNoteId(Map map);
    int updateNoteByNbseq(Map map);
    int updateNoteBookByNbseq(Map map);
}
