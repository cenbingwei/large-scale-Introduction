package com.waxjx.largescale.service;

import com.waxjx.largescale.dao.TeachersMapper;
import com.waxjx.largescale.model.Teachers;
import com.waxjx.largescale.util.TeacherSyncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class TeachersService {

    @Autowired
    private TeachersMapper teachersMapper;

    // 实现 数据库 主从同步
    @Autowired
    private Map<String, DataSource> dataSourceMap;
    @Autowired
    private String masterIp;

    /**
     * 根据教师id 查询教师信息 用于教师登录
     * @param teacherId
     * @return
     */
    public Teachers getTeacherById(String teacherId) {
        return teachersMapper.selectByPrimaryKey(teacherId);
    }

    public List<Teachers> getAllTeachers() {
        return teachersMapper.selectAllTeachers();
    }

    @Transactional
    public int updateTeachers(Teachers teachers) {
        int result = teachersMapper.updateByPrimaryKeySelective(teachers);
        // update 信息同步到其他数据库
        TeacherSyncUtil.TeacherSyncUtilUpdate(teachers, dataSourceMap, masterIp);

        return result;
    }

    @Transactional
    public int insertTeachers(Teachers teachers) {
        int result = teachersMapper.insertSelective(teachers);
        // insert 信息同步到其他数据库
        TeacherSyncUtil.TeacherSyncUtilInsert(teachers, dataSourceMap, masterIp);

        return result;
    }

    @Transactional
    public int deleteTeachers(String teacherId) {
        int result = teachersMapper.deleteByPrimaryKey(teacherId);
        // delete 信息同步到其他数据库
        TeacherSyncUtil.TeacherSyncUtilDelete(teacherId, dataSourceMap, masterIp);

        return result;
    }

}
