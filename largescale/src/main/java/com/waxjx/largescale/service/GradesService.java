package com.waxjx.largescale.service;

import com.waxjx.largescale.dao.GradesMapper;
import com.waxjx.largescale.model.Grades;
import com.waxjx.largescale.util.GradeSyncUtil;
import com.waxjx.largescale.util.JdbcUtil;
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
    @Autowired
    private DataSource dataSource;

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
            String masterUrl = dataSource.getConnection().getMetaData().getURL();
            masterUrl = JdbcUtil.extractHostFromJdbcUrl(masterUrl);
            if (masterUrl.equals(masterIp)){
                int result = gradesMapper.insert(grades);
                GradeSyncUtil.GradeSyncUtilInsert(grades, dataSourceMap, masterIp);

                return result;
            }else {
                GradeSyncUtil.GradeSyncUtilInsert(grades, dataSourceMap, masterIp);
                return 1;
            }

        }catch (Exception e) {
            return 0;
        }

    }

    @Transactional
    public int updateGradesByStudentIdCourseId(Grades grades){
        try {
            String masterUrl = dataSource.getConnection().getMetaData().getURL();
            masterUrl = JdbcUtil.extractHostFromJdbcUrl(masterUrl);
            if (masterUrl.equals(masterIp)){
                int result = gradesMapper.updateGradesByStudentIdCourseId(grades);
                GradeSyncUtil.GradeSyncUtilUpdate(grades, dataSourceMap, masterIp);

                return result;
            }else {
                GradeSyncUtil.GradeSyncUtilUpdate(grades, dataSourceMap, masterIp);
                return 1;
            }

        }catch (Exception e) {
            return 0;
        }

    }

    @Transactional
    public int deleteGradesByStudentIdCourseId(String studentId, String courseId){
        try {
            String masterUrl = dataSource.getConnection().getMetaData().getURL();
            masterUrl = JdbcUtil.extractHostFromJdbcUrl(masterUrl);
            if (masterUrl.equals(masterIp)){
                int result = gradesMapper.deleteGradesByStudentIdCourseId(studentId, courseId);
                GradeSyncUtil.GradeSyncUtilDelete(studentId, courseId, dataSourceMap, masterIp);

                return result;
            }else {
                GradeSyncUtil.GradeSyncUtilDelete(studentId, courseId, dataSourceMap, masterIp);
                return 1;
            }

        }catch (Exception e) {
            return 0;
        }

    }

}
