package com.waxjx.largescale.service;

import com.waxjx.largescale.dao.GradesMapper;
import com.waxjx.largescale.model.Grades;
import com.waxjx.largescale.util.GradeSyncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class GradesService {

    @Autowired
    private GradesMapper gradesMapper;

    @Autowired
    private Map<String, DataSource> dataSourceMap;
    @Autowired
    private String masterIp;

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
        try {
            int result = gradesMapper.insert(grades);
            GradeSyncUtil.GradeSyncUtilInsert(grades, dataSourceMap, masterIp);

            return result;
        }catch (Exception e) {
            return 0;
        }

    }

    @Transactional
    public int updateGradesByStudentIdCourseId(Grades grades){
        int result = gradesMapper.updateGradesByStudentIdCourseId(grades);
        GradeSyncUtil.GradeSyncUtilUpdate(grades, dataSourceMap, masterIp);

        return result;
    }

    @Transactional
    public int deleteGradesByStudentIdCourseId(String studentId, String courseId){
        int result = gradesMapper.deleteGradesByStudentIdCourseId(studentId, courseId);
        GradeSyncUtil.GradeSyncUtilDelete(studentId, courseId, dataSourceMap, masterIp);

        return result;
    }

}
