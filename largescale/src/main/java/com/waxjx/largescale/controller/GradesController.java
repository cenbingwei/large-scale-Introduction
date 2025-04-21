package com.waxjx.largescale.controller;


import com.waxjx.largescale.model.Grades;
import com.waxjx.largescale.service.GradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GradesController {

    @Autowired
    private GradesService gradesService;

    @GetMapping("largescale/grades/findGradesByStudentId")
    public List<Grades> findGradesByStudentId(@RequestParam("studentId") String studentId) {
        return gradesService.getGradesByStudentId(studentId);
    }

    @GetMapping("largescale/grades/findGradesByTeacherId")
    public List<Grades> findGradesByTeacherId(@RequestParam("teacherId") String teacherId) {
        return gradesService.getGradesByTeacherId(teacherId);
    }

    @GetMapping("largescale/grades/findGradesByCourseId")
    public List<Grades> findGradesByCourseId(@RequestParam("courseId") String courseId) {
        return gradesService.getGradesByCourseId(courseId);
    }

    @PostMapping("largescale/grades/insertGrades")
    public int insertGrades(@RequestBody Grades grades) {
        return gradesService.insertGrades(grades);
    }

    @PutMapping("largescale/grades/updateGradesByStudentIdCourseId")
    public int updateGradesByStudentIdCourseId(@RequestBody Grades grades) {
        return gradesService.updateGradesByStudentIdCourseId(grades);
    }

    @DeleteMapping("largescale/grades/deleteGradesByStudentIdCourseId")
    public int deleteGradesByStudentIdCourseId(String studentId, String courseId) {
        return gradesService.deleteGradesByStudentIdCourseId(studentId, courseId);
    }

}
