package com.waxjx.largescale.controller;


import com.waxjx.largescale.model.Teachers;
import com.waxjx.largescale.service.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TeachersController {

    @Autowired
    private TeachersService teachersService;

    @GetMapping("largescale/teacher/findByTeachersId")
    public Object getTeacher(String teachersId) {
        return teachersService.getTeacherById(teachersId);
    }

    @GetMapping("largescale/teacher/findAllTeachers")
    public Object getAllTeachers() {
        return teachersService.getAllTeachers();
    }

    @PutMapping("largescale/teacher/updateTeachers")
    public int updateTeachers(@RequestBody Teachers teachers) {
        return teachersService.updateTeachers(teachers);
    }

    @PostMapping("largescale/teacher/insertTeachers")
    public int insertTeachers(@RequestBody Teachers teachers) {
        return teachersService.insertTeachers(teachers);
    }

    @DeleteMapping("largescale/teacher/deleteTeachers")
    public int deleteTeachers(String teacherId) {
        return teachersService.deleteTeachers(teacherId);
    }
}
