package com.waxjx.largescale.dao;

import com.waxjx.largescale.model.CurriculumplanscourseKey;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CurriculumplanscourseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table curriculumplanscourse
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(CurriculumplanscourseKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table curriculumplanscourse
     *
     * @mbg.generated
     */
    int insert(CurriculumplanscourseKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table curriculumplanscourse
     *
     * @mbg.generated
     */
    int insertSelective(CurriculumplanscourseKey record);
}