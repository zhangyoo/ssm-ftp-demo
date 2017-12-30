package com.nj.nfhy.dao;

import com.nj.nfhy.pojo.SysDepartment;

public interface SysDepartmentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysDepartment record);

    int insertSelective(SysDepartment record);

    SysDepartment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysDepartment record);

    int updateByPrimaryKey(SysDepartment record);
}