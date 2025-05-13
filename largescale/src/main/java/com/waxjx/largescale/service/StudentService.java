package com.waxjx.largescale.service;

import com.waxjx.largescale.dao.GradesMapper;
import com.waxjx.largescale.dao.StudentMapper;
import com.waxjx.largescale.model.Student;
import com.waxjx.largescale.util.StudentSyncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
public class    StudentService  {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private GradesMapper gradesMapper;

    // 实现 主从同步
    @Autowired
    private Map<String, DataSource> dataSourceMap;
    @Autowired
    private String masterIp;

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
        int result = studentMapper.updateByPrimaryKeySelective(student);
        // update 同步其他数据库
        StudentSyncUtil.syncStudentUpdate(student, dataSourceMap, masterIp);

        // for (Map.Entry<String,DataSource> entry : dataSourceMap.entrySet()) {
        //     String ip = entry.getKey();
        //     DataSource ds = entry.getValue();
        //
        //     // 主数据库已经处理了，跳过
        //     if (ip.equals(masterIp)) {
        //         continue;
        //     }
        //
        //     try (Connection conn = ds.getConnection()){
        //         PreparedStatement statement = conn.prepareStatement(
        //                 "update student set studentName = ?, studentTelephone = ?, sex = ?, college = ?, " +
        //                         "administrativeClass = ?, idNumber = ?, email = ?, studentStatus = ?, " +
        //                         "educationalSystem = ?, enrollmentDate = ?, major = ? WHERE studentId = ?"
        //         );
        //         statement.setString(1, student.getStudentname());
        //         statement.setString(2, student.getStudenttelephone());
        //         statement.setShort(3, student.getSex());
        //         statement.setString(4, student.getCollege());
        //         statement.setString(5, student.getAdministrativeclass());
        //         statement.setString(6, student.getIdnumber());
        //         statement.setString(7, student.getEmail());
        //         statement.setShort(8, student.getStudentstatus());
        //         statement.setShort(9, student.getEducationalsystem());
        //         statement.setDate(10, student.getEnrollmentdate());
        //         statement.setString(11, student.getMajor());
        //         statement.setString(12, student.getStudentid());
        //
        //         statement.executeUpdate();
        //
        //     } catch (SQLException e) {
        //         System.out.println("其他数据库修改学生数据失败");
        //
        //     }
        // }

        return result;
    }


    public List<Student> findAllStudent(){
        List<Student> students = studentMapper.selectAllStudents();
        return students;
    }

    @Transactional
    public int insertStudent(Student student) {
        try {
            int result = studentMapper.insertSelective(student);
            StudentSyncUtil.syncStudentInsert(student, dataSourceMap, masterIp);

            return result;
        }catch (Exception e){
            return 0;
        }

    }

    @Transactional
    public int deleteStudent(String studentId) {
        // System.out.println("1111111111111111111111111111111111");
        // System.out.println(studentId);
        /**
         * 先删除对应学生的成绩信息
         */
        int count = gradesMapper.deleteGradesByStudentId(studentId);
        // System.out.println(count);
        // System.out.println("2222222222222222222222222222222222");

        // 同步其他数据库
        StudentSyncUtil.syncStudentDelete(studentId, dataSourceMap, masterIp);

        return count;
    }

    public List<Student> getStudentByStudentName(String studentName) {
        return studentMapper.selectByStudentName(studentName);
    }

}
