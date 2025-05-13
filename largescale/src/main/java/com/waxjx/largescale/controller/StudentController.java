package com.waxjx.largescale.controller;

import com.waxjx.largescale.model.Student;
import com.waxjx.largescale.service.StudentService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/largescale/findStudentById")
    public Object getStudent(String studentId) {
        return studentService.getStudentById(studentId);
    }

    @GetMapping("largescale/findAllStudent")
    public List<Student> findAllStudent() {
        return studentService.findAllStudent();
    }

    @PutMapping("largescale/updateStudent")
    public int updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @PostMapping("largescale/insertStudent")
    public int addStudent(@RequestBody Student student) {
        return studentService.insertStudent(student);
    }

    @DeleteMapping("largescale/deleteStudent")
    public int deleteStudent(String studentId) {
        return studentService.deleteStudent(studentId);
    }

    @GetMapping("largescale/findStudentByStudentName")
    public List<Student> findStudentsByStudentName(String studentName) {
        return studentService.getStudentByStudentName(studentName);
    }

    @GetMapping("largescale/selectStudentGradesWithCourseInfo")
    public List<Map<String, Object>> selectStudentGradesWithCourseInfo(@RequestParam String studentId) {
        return studentService.selectStudentGradesWithCourseInfo(studentId);
    }

    @GetMapping("largescale/selectClassAverageAndTopStudent")
    public List<Map<String, Object>> selectClassAverageAndTopStudent() {
        return studentService.selectClassAverageAndTopStudent();
    }




}
