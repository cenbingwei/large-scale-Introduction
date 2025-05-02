create table Courses
(
   courseId             varchar(16) not null,
   teacherID            varchar(16),
   courseName           varchar(256),
   courseDescription    varchar(2560),
   credits              real,
   semesterOffered      varchar(16),
   remarks              varchar(256),
   courseCategory       smallint,
   courseCapacity       int
);

alter table Courses
   add primary key (courseId);

/*==============================================================*/
/* Table: CurriculumPlans                                       */
/*==============================================================*/
create table CurriculumPlans
(
   curriculumPlanId     varchar(16) not null,
   major                varchar(256),
   planName             varchar(256),
   planDescription      varchar(2560),
   creditRequirement    real,
   remarks              varchar(256),
   courseCategory_one   real,
   courseCategory_two   real,
   courseCategory_three real,
   courseCategory_four  real,
   courseCategory_five  real,
   courseCategory_six   real,
   courseCategory_seven real
);

alter table CurriculumPlans
   add primary key (curriculumPlanId);

/*==============================================================*/
/* Table: CurriculumPlansCourse                                 */
/*==============================================================*/
create table CurriculumPlansCourse
(
   curriculumPlanId     varchar(16) not null,
   courseId             varchar(16) not null
);

alter table CurriculumPlansCourse
   add primary key (curriculumPlanId, courseId);

/*==============================================================*/
/* Table: Grades                                                */
/*==============================================================*/
create table Grades
(
   gradeId              varchar(16) not null,
   studentId            varchar(16) not null,
   courseId             varchar(16) not null,
   score                real,
   enrollmentStatus     smallint,
   gpa                  real,
   assessmentMethod     varchar(16),
   remarks              varchar(256)
);

alter table Grades
   add primary key (studentId, courseId, gradeId);

/*==============================================================*/
/* Table: Student                                               */
/*==============================================================*/
create table Student
(
   studentId            varchar(16) not null,
   studentName          varchar(256),
   studentTelephone     varchar(16),
   sex                  smallint,
   college              varchar(256),
   administrativeClass  varchar(256),
   idNumber             varchar(38),
   email                varchar(28),
   studentStatus        smallint,
   educationalSystem    smallint,
   enrollmentDate       date,
   major                varchar(256)
);

alter table Student
   add primary key (studentId);

/*==============================================================*/
/* Table: Teachers                                              */
/*==============================================================*/
create table Teachers
(
   teacherID            varchar(16) not null,
   name                 varchar(256),
   sex                  smallint,
   birthDate            date,
   title                varchar(256),
   telephone            varchar(16),
   remarks              varchar(256)
);

alter table Teachers
   add primary key (teacherID);

alter table Courses add constraint FK_getClass foreign key (teacherID)
      references Teachers (teacherID) on delete restrict on update restrict;

alter table CurriculumPlansCourse add constraint FK_CurriculumPlansCourse foreign key (curriculumPlanId)
      references CurriculumPlans (curriculumPlanId) on delete restrict on update restrict;

alter table CurriculumPlansCourse add constraint FK_CurriculumPlansCourse2 foreign key (courseId)
      references Courses (courseId) on delete restrict on update restrict;

alter table Grades add constraint FK_Grades foreign key (studentId)
      references Student (studentId) on delete restrict on update restrict;

alter table Grades add constraint FK_Grades2 foreign key (courseId)
      references Courses (courseId) on delete restrict on update restrict;



INSERT INTO Teachers (teacherID, name, sex, birthDate, title, telephone, remarks) VALUES
('T001', '张教授', 1, '1975-03-15', '教授', '13800138001', '计算机科学系主任'),
('T002', '李副教授', 1, '1980-07-22', '副教授', '13800138002', '研究方向：人工智能'),
('T003', '王讲师', 2, '1985-11-30', '讲师', '13800138003', '新入职教师'),
('T004', '赵教授', 1, '1972-05-18', '教授', '13800138004', '数学系副主任'),
('T005', '刘副教授', 2, '1983-09-10', '副教授', '13800138005', '研究方向：数据科学');

INSERT INTO Courses (courseId, teacherID, courseName, courseDescription, credits, semesterOffered, remarks, courseCategory, courseCapacity) VALUES
('CS101', 'T001', '计算机科学导论', '计算机科学基础课程，介绍计算机系统的基本概念和原理', 3.0, '秋季', '必修课', 1, 100),
('CS201', 'T002', '数据结构', '研究数据组织、管理和存储方式的课程', 4.0, '春季', '核心课程', 1, 80),
('CS301', 'T001', '算法分析', '深入分析计算机算法的设计与效率', 4.0, '秋季', '高阶课程', 1, 60),
('MATH101', 'T004', '高等数学', '大学数学基础课程', 5.0, '全年', '公共基础课', 2, 120),
('DS401', 'T005', '数据科学基础', '数据科学入门课程', 3.0, '春季', '选修课', 3, 50),
('CS401', 'T003', '软件工程', '软件开发生命周期和方法论', 3.0, '秋季', '专业选修', 1, 70),
('PHY101', 'T004', '大学物理', '物理学基础课程', 4.0, '全年', '公共基础课', 2, 100);

INSERT INTO Student (studentId, studentName, studentTelephone, sex, college, administrativeClass, idNumber, email, studentStatus, educationalSystem, enrollmentDate, major) VALUES
('S001', '张三', '13800138111', 1, '计算机学院', '计算机2101', '110101200001011234', 'zhangsan@edu.cn', 1, 4, '2021-09-01', '计算机科学与技术'),
('S002', '李四', '13800138112', 1, '计算机学院', '计算机2101', '110101200002021235', 'lisi@edu.cn', 1, 4, '2021-09-01', '计算机科学与技术'),
('S003', '王五', '13800138113', 2, '数学学院', '数学2102', '110101200003031236', 'wangwu@edu.cn', 1, 4, '2021-09-01', '数学与应用数学'),
('S004', '赵六', '13800138114', 1, '计算机学院', '计算机2102', '110101200004041237', 'zhaoliu@edu.cn', 1, 4, '2021-09-01', '软件工程'),
('S005', '钱七', '13800138115', 2, '物理学院', '物理2101', '110101200005051238', 'qianqi@edu.cn', 1, 4, '2021-09-01', '物理学'),
('S006', '孙八', '13800138116', 1, '计算机学院', '计算机2102', '110101200006061239', 'sunba@edu.cn', 2, 4, '2021-09-01', '软件工程'),
('S007', '周九', '13800138117', 2, '数学学院', '数学2101', '110101200007071240', 'zhoujiu@edu.cn', 1, 4, '2021-09-01', '数学与应用数学'),
('S008', '吴十', '13800138118', 1, '计算机学院', '计算机2101', '110101200008081241', 'wushi@edu.cn', 1, 4, '2021-09-01', '计算机科学与技术');

INSERT INTO CurriculumPlans (curriculumPlanId, major, planName, planDescription, creditRequirement, remarks, courseCategory_one, courseCategory_two, courseCategory_three, courseCategory_four, courseCategory_five, courseCategory_six, courseCategory_seven) VALUES
('CP001', '计算机科学与技术', '2021计算机科学培养方案', '计算机科学专业2021级培养方案', 150.0, '最新修订版', 60.0, 30.0, 20.0, 15.0, 10.0, 10.0, 5.0),
('CP002', '软件工程', '2021软件工程培养方案', '软件工程专业2021级培养方案', 145.0, '侧重软件开发', 55.0, 25.0, 25.0, 15.0, 10.0, 10.0, 5.0),
('CP003', '数学与应用数学', '2021数学培养方案', '数学专业2021级培养方案', 160.0, '理论数学方向', 40.0, 50.0, 20.0, 20.0, 15.0, 10.0, 5.0),
('CP004', '物理学', '2021物理培养方案', '物理专业2021级培养方案', 155.0, '基础物理方向', 35.0, 45.0, 25.0, 20.0, 15.0, 10.0, 5.0);

INSERT INTO CurriculumPlansCourse (curriculumPlanId, courseId) VALUES
('CP001', 'CS101'),
('CP001', 'CS201'),
('CP001', 'CS301'),
('CP001', 'MATH101'),
('CP002', 'CS101'),
('CP002', 'CS201'),
('CP002', 'CS401'),
('CP002', 'MATH101'),
('CP003', 'MATH101'),
('CP004', 'MATH101'),
('CP004', 'PHY101');

INSERT INTO Grades (gradeId, studentId, courseId, score, enrollmentStatus, gpa, assessmentMethod, remarks) VALUES
('G001', 'S001', 'CS101', 85.5, 1, 3.7, '考试', '平时表现良好'),
('G002', 'S001', 'MATH101', 92.0, 1, 4.0, '考试', '数学基础扎实'),
('G003', 'S002', 'CS101', 78.0, 1, 3.0, '考试', '需要加强实践'),
('G004', 'S002', 'MATH101', 88.5, 1, 3.7, '考试', '表现稳定'),
('G005', 'S003', 'MATH101', 95.0, 1, 4.0, '考试', '数学天赋突出'),
('G006', 'S004', 'CS101', 90.0, 1, 4.0, '考试', '编程能力强'),
('G007', 'S004', 'CS201', 82.0, 1, 3.3, '考试', '数据结构掌握良好'),
('G008', 'S005', 'PHY101', 87.0, 1, 3.7, '考试', '物理实验表现优秀'),
('G009', 'S006', 'CS401', 76.0, 1, 2.7, '考试', '项目完成度一般'),
('G010', 'S007', 'MATH101', 91.0, 1, 4.0, '考试', '数学思维敏捷'),
('G011', 'S008', 'CS101', 94.0, 1, 4.0, '考试', '课程表现优异'),
('G012', 'S008', 'CS201', 89.0, 1, 3.7, '考试', '算法实现能力强');



