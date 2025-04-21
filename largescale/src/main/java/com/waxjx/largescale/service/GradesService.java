package com.waxjx.largescale.service;

import com.waxjx.largescale.dao.GradesMapper;
import com.waxjx.largescale.model.Grades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradesService {

    @Autowired
    private GradesMapper gradesMapper;

    @Transactional
    public int deleteGradesByStudentId(String id) {
        return gradesMapper.deleteGradesByStudentId(id);
    }

    public List<Grades> getGradesByStudentId(String id) {
        return gradesMapper.selectByStudentId(id);
    }

    public  List<Grades> getGradesByTeacherId(String id) {
        return gradesMapper.selectByTeacherId(id);
    }

    public List<Grades> getGradesByCourseId(String id) {
        return gradesMapper.selectByCourseId(id);
    }

    @Transactional
    public int insertGrades(Grades grades) {
        return gradesMapper.insert(grades);
    }

    @Transactional
    public int updateGradesByStudentIdCourseId(Grades grades){
        return gradesMapper.updateGradesByStudentIdCourseId(grades);
    }

    @Transactional
    public int deleteGradesByStudentIdCourseId(String studentId, String courseId){
        return gradesMapper.deleteGradesByStudentIdCourseId(studentId, courseId);
    }

}
