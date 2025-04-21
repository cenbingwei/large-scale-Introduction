package com.waxjx.largescale.dao;

import com.waxjx.largescale.model.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {

    int deleteByPrimaryKey(String studentid);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(String studentid);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    // 新增查询所有学生的方法
    List<Student> selectAllStudents();   // 方法名按需定义

    // 根据学生名字查询
    List<Student> selectByStudentName(String studentName);

}