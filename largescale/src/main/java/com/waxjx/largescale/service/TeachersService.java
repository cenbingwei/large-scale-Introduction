package com.waxjx.largescale.service;

import com.waxjx.largescale.dao.TeachersMapper;
import com.waxjx.largescale.model.Teachers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeachersService {

    @Autowired
    private TeachersMapper teachersMapper;

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
        return teachersMapper.updateByPrimaryKeySelective(teachers);
    }

    @Transactional
    public int insertTeachers(Teachers teachers) {
        return teachersMapper.insertSelective(teachers);
    }

    @Transactional
    public int deleteTeachers(String teacherId) {
        return teachersMapper.deleteByPrimaryKey(teacherId);
    }

}
