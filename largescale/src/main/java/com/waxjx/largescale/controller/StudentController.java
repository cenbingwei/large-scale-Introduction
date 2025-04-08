package com.waxjx.largescale.controller;

import com.waxjx.largescale.model.Student;
import com.waxjx.largescale.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @RequestMapping("/largescale/student")
    public Object getStudent() {
        return studentService.getStudentById("S001");
    }

    @GetMapping("largescale/findAllStudent")
    public List<Student> findAllStudent() {
        return studentService.findAllStudent();
    }

}
