package com.waxjx.largescale.dao;

import com.waxjx.largescale.model.Grades;
import com.waxjx.largescale.model.GradesKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GradesMapper {

    int deleteByPrimaryKey(GradesKey key);
    int insert(Grades record);
    int insertSelective(Grades record);
    Grades selectByPrimaryKey(GradesKey key);
    int updateByPrimaryKeySelective(Grades record);
    int updateByPrimaryKey(Grades record);

    int deleteGradesByStudentId(String studentId);

    List<Grades> selectByStudentId(String studentId);
    List<Grades> selectByTeacherId(String teacherId);
    List<Grades> selectByCourseId(String courseId);

    int updateGradesByStudentIdCourseId(Grades grades);
    int deleteGradesByStudentIdCourseId(String studentId, String courseId);

}