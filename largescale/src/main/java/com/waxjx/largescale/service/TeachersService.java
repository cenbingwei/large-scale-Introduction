package com.waxjx.largescale.service;

import com.waxjx.largescale.Config.ReadWriteRoutingDataSource;
import com.waxjx.largescale.dao.TeachersMapper;
import com.waxjx.largescale.model.Teachers;
import com.waxjx.largescale.util.JdbcUtil;
import com.waxjx.largescale.util.TeacherSyncUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class TeachersService {

    @Autowired
    private TeachersMapper teachersMapper;

    // 实现 数据库 主从同步
    @Autowired
    private Map<String, DataSource> dataSourceMap;
    @Autowired
    private String masterIp;
    @Autowired
    private DataSource dataSource;



    /**
     * 根据教师id 查询教师信息 用于教师登录
     * @param teacherId
     * @return
     */
    public Teachers getTeacherById(String teacherId) {
        return teachersMapper.selectByPrimaryKey(teacherId);
    }

    public List<Teachers> getAllTeachers() {
        return teachersMapper.selectAllTeachers();
    }

    @Transactional
    public int updateTeachers(Teachers teachers) {
        try {
            String masterUrl = dataSource.getConnection().getMetaData().getURL();
            masterUrl = JdbcUtil.extractHostFromJdbcUrl(masterUrl);
            if (masterUrl.equals(masterIp)) {
                int result = teachersMapper.updateByPrimaryKeySelective(teachers);

                // update 信息同步到其他数据库
                TeacherSyncUtil.TeacherSyncUtilUpdate(teachers, dataSourceMap, masterIp, masterUrl);

                return result;
            }else {
                // update 信息同步到其他数据库
                TeacherSyncUtil.TeacherSyncUtilUpdate(teachers, dataSourceMap, masterIp, masterUrl);
                return 1;
            }
        }catch (Exception e) {
            return 0;
        }

    }

    @Transactional
    public int insertTeachers(Teachers teachers) {
        try {
            // System.err.println(dataSource + "00000000000000000000000000000000000000000000000000000");
            // System.err.println(dataSource.getConnection().getMetaData().getURL() + "1111111111111111111111111111111111111");
            // String masterUrl =  "";
            String masterUrl = dataSource.getConnection().getMetaData().getURL();
            masterUrl = JdbcUtil.extractHostFromJdbcUrl(masterUrl);
            // System.err.println(masterUrl+"     222222222222222222222222222222222222222222");
                    // jdbc:mysql://101.37.38.162:3306/largescale
            // System.err.println(dataSource);
            if (masterUrl.equals(masterIp)) {
                int result = teachersMapper.insertSelective(teachers);
                System.err.println("第一个数据库 ： 教师插入结束");
                // System.out.println(masterUrl);
                // System.out.println(dataSourceMap.get(1));
                // System.out.println(dataSource);
                // insert 信息同步到其他数据库
                TeacherSyncUtil.TeacherSyncUtilInsert(teachers, dataSourceMap, masterIp, masterUrl);
                return result;
            }else {
                TeacherSyncUtil.TeacherSyncUtilInsert(teachers, dataSourceMap, masterIp, masterUrl);
                return 1;
            }

        }catch (Exception e){
            // e.printStackTrace();
            return 0;
        }

    }

    @Transactional
    public int deleteTeachers(String teacherId) {
        try {
            String masterUrl = dataSource.getConnection().getMetaData().getURL();
            masterUrl = JdbcUtil.extractHostFromJdbcUrl(masterUrl);
            if (masterUrl.equals(masterIp)) {
                int result = teachersMapper.deleteByPrimaryKey(teacherId);

                System.err.println("第一个数据库 ： 教师删除结束" + "      " + result);
                // delete 信息同步到其他数据库
                System.err.println(masterUrl + "    删除数据库的masterUrl");
                TeacherSyncUtil.TeacherSyncUtilDelete(teacherId, dataSourceMap, masterIp, masterUrl);

                return result;
            }else {
                TeacherSyncUtil.TeacherSyncUtilDelete(teacherId, dataSourceMap, masterIp, masterUrl);
                return 1;
            }

        }catch (Exception e){
            return 0;
        }

    }

}
