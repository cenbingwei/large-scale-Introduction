package com.waxjx.largescale.model;

public class CurriculumplanscourseKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column curriculumplanscourse.curriculumPlanId
     *
     * @mbg.generated
     */
    private String curriculumplanid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column curriculumplanscourse.courseId
     *
     * @mbg.generated
     */
    private String courseid;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column curriculumplanscourse.curriculumPlanId
     *
     * @return the value of curriculumplanscourse.curriculumPlanId
     *
     * @mbg.generated
     */
    public String getCurriculumplanid() {
        return curriculumplanid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column curriculumplanscourse.curriculumPlanId
     *
     * @param curriculumplanid the value for curriculumplanscourse.curriculumPlanId
     *
     * @mbg.generated
     */
    public void setCurriculumplanid(String curriculumplanid) {
        this.curriculumplanid = curriculumplanid == null ? null : curriculumplanid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column curriculumplanscourse.courseId
     *
     * @return the value of curriculumplanscourse.courseId
     *
     * @mbg.generated
     */
    public String getCourseid() {
        return courseid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column curriculumplanscourse.courseId
     *
     * @param courseid the value for curriculumplanscourse.courseId
     *
     * @mbg.generated
     */
    public void setCourseid(String courseid) {
        this.courseid = courseid == null ? null : courseid.trim();
    }
}