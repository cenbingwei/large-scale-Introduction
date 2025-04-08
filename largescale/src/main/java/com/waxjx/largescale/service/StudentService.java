package com.waxjx.largescale.service;

import com.waxjx.largescale.dao.StudentMapper;
import com.waxjx.largescale.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService  {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 根据学生 id 查询 学生信息
     * @param studentId
     * @return
     */
    public Student getStudentById(String studentId) {
        return studentMapper.selectByPrimaryKey(studentId);
    }

    /**
     * 更新学生信息 （具有事务管理@Transactional————进行更新后如果发生报错，数据库将不会更改数据，防止恶意更改数据）
     * 具有事务回滚功能
     * @param student
     * @return
     */
    @Transactional
    public int updateStudent(Student student) {
        return studentMapper.updateByPrimaryKeySelective(student);
    }


    public List<Student> findAllStudent(){
        List<Student> students = studentMapper.selectAllStudents();
        return students;
    }
}
