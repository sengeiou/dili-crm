package com.dili.alm.service.impl;

import com.dili.alm.dao.FilesMapper;
import com.dili.alm.domain.Files;
import com.dili.alm.service.FilesService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2017-10-20 14:47:50.
 */
@Service
public class FilesServiceImpl extends BaseServiceImpl<Files, Long> implements FilesService {

    public FilesMapper getActualDao() {
        return (FilesMapper)getDao();
    }

    @Override
    public int delete(Long aLong) {
        //先根据路径+文件名删除文件
        Files files = getActualDao().selectByPrimaryKey(aLong);
        File dest = new File(files.getUrl() + files.getName());
        dest.delete();
        return super.delete(aLong);
    }
}