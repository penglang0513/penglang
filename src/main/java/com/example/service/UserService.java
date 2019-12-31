package com.example.service;

import com.example.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    public User getUserById(String userId);

    public User findIdAndPassword(User user);

    public String findId();

    public int insertSelective(User user);

    public List<User> findByEmail(String email);

    public List<User> findAllUser();

    public String findLevel(String username);

    public String findUserId(String email);

    public int addFile(Map map);

    public int addUserFile(Map map);

    public List<Map<String,String>> findSC(String userId);
    public List<Map> findShouC(Map map);

    public Map findFtByName(Map map);
    public int addFz(Map map);
    public String findFileTypeName(String model);
    public List<Map> findNoteBookByCifSeq(Map map);
    public String findNoteBookByName(Map map);
    public int addNoteBook(Map map);
    public int addNote(Map map);
    public List<Map> findNoteBySeq(Map map);
    public Map findNoteById(Map map);
    public int updateNote(Map map);
    public int updateFileInfoState(Map map);
    public List<Map> findFileBySeq(Map map);

    public int updateFileInfoStateByseq(Map map);
    public int updateFileTypeStateByseq(Map map);

    public int updateNoteByNoteId(Map map);
    public int updateNoteByNbseq(Map map);
    public int updateNoteBookByNbseq(Map map);
}
