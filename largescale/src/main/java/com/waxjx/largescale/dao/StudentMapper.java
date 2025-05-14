package com.waxjx.largescale.dao;

import com.waxjx.largescale.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    // 查询学生及其所有课程成绩信息（包含课程名称）
    List<Map<String, Object>> selectStudentGradesWithCourseInfo(@Param("studentId") String studentId);

    // 查询每个班级的平均成绩和最高分学生信息
    List<Map<String, Object>> selectClassAverageAndTopStudent();
}