package com.szyz.rock.dao;

import com.szyz.rock.model.entity.LogInfo;

import java.util.List;

public interface LogDao {
    /**
     * 查询Log
     * @param logInfo
     * @return
     */
    List<LogInfo> queryLogs(LogInfo logInfo);
}
