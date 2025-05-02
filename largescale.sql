/*
Navicat MySQL Data Transfer

Source Server         : largescale
Source Server Version : 50650
Source Host           : 47.122.84.149:3306
Source Database       : largescale

Target Server Type    : MYSQL
Target Server Version : 50650
File Encoding         : 65001

Date: 2025-05-02 20:51:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for courses
-- ----------------------------
DROP TABLE IF EXISTS `courses`;
CREATE TABLE `courses` (
  `courseId` varchar(16) NOT NULL,
  `teacherID` varchar(16) DEFAULT NULL,
  `courseName` varchar(256) DEFAULT NULL,
  `courseDescription` varchar(2560) DEFAULT NULL,
  `credits` double DEFAULT NULL,
  `semesterOffered` varchar(16) DEFAULT NULL,
  `remarks` varchar(256) DEFAULT NULL,
  `courseCategory` smallint(6) DEFAULT NULL,
  `courseCapacity` int(11) DEFAULT NULL,
  PRIMARY KEY (`courseId`),
  KEY `FK_getClass` (`teacherID`),
  CONSTRAINT `FK_getClass` FOREIGN KEY (`teacherID`) REFERENCES `teachers` (`teacherID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of courses
-- ----------------------------
INSERT INTO `courses` VALUES ('CS101', 'T001', '计算机科学导论', '计算机科学基础课程，介绍计算机系统的基本概念和原理', '3', '秋季', '必修课', '1', '100');
INSERT INTO `courses` VALUES ('CS201', 'T002', '数据结构', '研究数据组织、管理和存储方式的课程', '4', '春季', '核心课程', '1', '80');
INSERT INTO `courses` VALUES ('CS301', 'T001', '算法分析', '深入分析计算机算法的设计与效率', '4', '秋季', '高阶课程', '1', '60');
INSERT INTO `courses` VALUES ('CS401', 'T003', '软件工程', '软件开发生命周期和方法论', '3', '秋季', '专业选修', '1', '70');
INSERT INTO `courses` VALUES ('DS401', 'T005', '数据科学基础', '数据科学入门课程', '3', '春季', '选修课', '3', '50');
INSERT INTO `courses` VALUES ('MATH101', 'T004', '高等数学', '大学数学基础课程', '5', '全年', '公共基础课', '2', '120');
INSERT INTO `courses` VALUES ('PHY101', 'T004', '大学物理', '物理学基础课程', '4', '全年', '公共基础课', '2', '100');

-- ----------------------------
-- Table structure for curriculumPlans
-- ----------------------------
DROP TABLE IF EXISTS `curriculumPlans`;
CREATE TABLE `curriculumPlans` (
  `curriculumPlanId` varchar(16) NOT NULL,
  `major` varchar(256) DEFAULT NULL,
  `planName` varchar(256) DEFAULT NULL,
  `planDescription` varchar(2560) DEFAULT NULL,
  `creditRequirement` double DEFAULT NULL,
  `remarks` varchar(256) DEFAULT NULL,
  `courseCategory_one` double DEFAULT NULL,
  `courseCategory_two` double DEFAULT NULL,
  `courseCategory_three` double DEFAULT NULL,
  `courseCategory_four` double DEFAULT NULL,
  `courseCategory_five` double DEFAULT NULL,
  `courseCategory_six` double DEFAULT NULL,
  `courseCategory_seven` double DEFAULT NULL,
  PRIMARY KEY (`curriculumPlanId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of curriculumPlans
-- ----------------------------
INSERT INTO `curriculumPlans` VALUES ('CP001', '计算机科学与技术', '2021计算机科学培养方案', '计算机科学专业2021级培养方案', '150', '最新修订版', '60', '30', '20', '15', '10', '10', '5');
INSERT INTO `curriculumPlans` VALUES ('CP002', '软件工程', '2021软件工程培养方案', '软件工程专业2021级培养方案', '145', '侧重软件开发', '55', '25', '25', '15', '10', '10', '5');
INSERT INTO `curriculumPlans` VALUES ('CP003', '数学与应用数学', '2021数学培养方案', '数学专业2021级培养方案', '160', '理论数学方向', '40', '50', '20', '20', '15', '10', '5');
INSERT INTO `curriculumPlans` VALUES ('CP004', '物理学', '2021物理培养方案', '物理专业2021级培养方案', '155', '基础物理方向', '35', '45', '25', '20', '15', '10', '5');

-- ----------------------------
-- Table structure for curriculumPlansCourse
-- ----------------------------
DROP TABLE IF EXISTS `curriculumPlansCourse`;
CREATE TABLE `curriculumPlansCourse` (
  `curriculumPlanId` varchar(16) NOT NULL,
  `courseId` varchar(16) NOT NULL,
  PRIMARY KEY (`curriculumPlanId`,`courseId`),
  KEY `FK_CurriculumPlansCourse2` (`courseId`),
  CONSTRAINT `FK_CurriculumPlansCourse` FOREIGN KEY (`curriculumPlanId`) REFERENCES `curriculumPlans` (`curriculumPlanId`),
  CONSTRAINT `FK_CurriculumPlansCourse2` FOREIGN KEY (`courseId`) REFERENCES `courses` (`courseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of curriculumPlansCourse
-- ----------------------------
INSERT INTO `curriculumPlansCourse` VALUES ('CP001', 'CS101');
INSERT INTO `curriculumPlansCourse` VALUES ('CP002', 'CS101');
INSERT INTO `curriculumPlansCourse` VALUES ('CP001', 'CS201');
INSERT INTO `curriculumPlansCourse` VALUES ('CP002', 'CS201');
INSERT INTO `curriculumPlansCourse` VALUES ('CP001', 'CS301');
INSERT INTO `curriculumPlansCourse` VALUES ('CP002', 'CS401');
INSERT INTO `curriculumPlansCourse` VALUES ('CP001', 'MATH101');
INSERT INTO `curriculumPlansCourse` VALUES ('CP002', 'MATH101');
INSERT INTO `curriculumPlansCourse` VALUES ('CP003', 'MATH101');
INSERT INTO `curriculumPlansCourse` VALUES ('CP004', 'MATH101');
INSERT INTO `curriculumPlansCourse` VALUES ('CP004', 'PHY101');

-- ----------------------------
-- Table structure for grades
-- ----------------------------
DROP TABLE IF EXISTS `grades`;
CREATE TABLE `grades` (
  `gradeId` varchar(16) NOT NULL,
  `studentId` varchar(16) NOT NULL,
  `courseId` varchar(16) NOT NULL,
  `score` double DEFAULT NULL,
  `enrollmentStatus` smallint(6) DEFAULT NULL,
  `gpa` double DEFAULT NULL,
  `assessmentMethod` varchar(16) DEFAULT NULL,
  `remarks` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`studentId`,`courseId`,`gradeId`),
  KEY `FK_Grades2` (`courseId`),
  CONSTRAINT `FK_Grades` FOREIGN KEY (`studentId`) REFERENCES `student` (`studentId`),
  CONSTRAINT `FK_Grades2` FOREIGN KEY (`courseId`) REFERENCES `courses` (`courseId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of grades
-- ----------------------------
INSERT INTO `grades` VALUES ('G001', 'S001', 'CS101', '85.5', '1', '3.7', '考试', '平时表现良好');
INSERT INTO `grades` VALUES ('G002', 'S001', 'MATH101', '92', '1', '4', '考试', '数学基础扎实');
INSERT INTO `grades` VALUES ('G003', 'S002', 'CS101', '78', '1', '3', '考试', '需要加强实践');
INSERT INTO `grades` VALUES ('G004', 'S002', 'MATH101', '88.5', '1', '3.7', '考试', '表现稳定');
INSERT INTO `grades` VALUES ('G005', 'S003', 'MATH101', '95', '1', '4', '考试', '数学天赋突出');
INSERT INTO `grades` VALUES ('G006', 'S004', 'CS101', '90', '1', '4', '考试', '编程能力强');
INSERT INTO `grades` VALUES ('G007', 'S004', 'CS201', '82', '1', '3.3', '考试', '数据结构掌握良好');
INSERT INTO `grades` VALUES ('G008', 'S005', 'PHY101', '87', '1', '3.7', '考试', '物理实验表现优秀');
INSERT INTO `grades` VALUES ('G009', 'S006', 'CS401', '76', '1', '2.7', '考试', '项目完成度一般');
INSERT INTO `grades` VALUES ('G010', 'S007', 'MATH101', '91', '1', '4', '考试', '数学思维敏捷');
INSERT INTO `grades` VALUES ('G011', 'S008', 'CS101', '94', '1', '4', '考试', '课程表现优异');
INSERT INTO `grades` VALUES ('G012', 'S008', 'CS201', '89', '1', '3.7', '考试', '算法实现能力强');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `studentId` varchar(16) NOT NULL,
  `studentName` varchar(256) DEFAULT NULL,
  `studentTelephone` varchar(16) DEFAULT NULL,
  `sex` smallint(6) DEFAULT NULL,
  `college` varchar(256) DEFAULT NULL,
  `administrativeClass` varchar(256) DEFAULT NULL,
  `idNumber` varchar(38) DEFAULT NULL,
  `email` varchar(28) DEFAULT NULL,
  `studentStatus` smallint(6) DEFAULT NULL,
  `educationalSystem` smallint(6) DEFAULT NULL,
  `enrollmentDate` date DEFAULT NULL,
  `major` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`studentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('S001', '岑秉蔚喜欢许嘉欣', '13800138000', '1', '计算机学院', '2023级1班', '123456789012345678', 'zhangsan@example.com', '1', '4', '2023-09-01', '软件工程');
INSERT INTO `student` VALUES ('S002', '李四', '13800138112', '1', '计算机学院', '计算机2101', '110101200002021235', 'lisi@edu.cn', '1', '4', '2021-09-01', '计算机科学与技术');
INSERT INTO `student` VALUES ('S003', '王五', '13800138113', '2', '数学学院', '数学2102', '110101200003031236', 'wangwu@edu.cn', '1', '4', '2021-09-01', '数学与应用数学');
INSERT INTO `student` VALUES ('S004', '赵六', '13800138114', '1', '计算机学院', '计算机2102', '110101200004041237', 'zhaoliu@edu.cn', '1', '4', '2021-09-01', '软件工程');
INSERT INTO `student` VALUES ('S005', '钱七', '13800138115', '2', '物理学院', '物理2101', '110101200005051238', 'qianqi@edu.cn', '1', '4', '2021-09-01', '物理学');
INSERT INTO `student` VALUES ('S006', '孙八', '13800138116', '1', '计算机学院', '计算机2102', '110101200006061239', 'sunba@edu.cn', '2', '4', '2021-09-01', '软件工程');
INSERT INTO `student` VALUES ('S007', '周九', '13800138117', '2', '数学学院', '数学2101', '110101200007071240', 'zhoujiu@edu.cn', '1', '4', '2021-09-01', '数学与应用数学');
INSERT INTO `student` VALUES ('S008', '吴十', '13800138118', '1', '计算机学院', '计算机2101', '110101200008081241', 'wushi@edu.cn', '1', '4', '2021-09-01', '计算机科学与技术');

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers` (
  `teacherID` varchar(16) NOT NULL,
  `name` varchar(256) DEFAULT NULL,
  `sex` smallint(6) DEFAULT NULL,
  `birthDate` date DEFAULT NULL,
  `title` varchar(256) DEFAULT NULL,
  `telephone` varchar(16) DEFAULT NULL,
  `remarks` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`teacherID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES ('T001', '张教授', '1', '1975-03-15', '教授', '13800138001', '计算机科学系主任');
INSERT INTO `teachers` VALUES ('T002', '李副教授', '1', '1980-07-22', '副教授', '13800138002', '研究方向：人工智能');
INSERT INTO `teachers` VALUES ('T003', '王讲师', '2', '1985-11-30', '讲师', '13800138003', '新入职教师');
INSERT INTO `teachers` VALUES ('T004', '赵教授', '1', '1972-05-18', '教授', '13800138004', '数学系副主任');
INSERT INTO `teachers` VALUES ('T005', '刘副教授', '2', '1983-09-10', '副教授', '13800138005', '研究方向：数据科学');
