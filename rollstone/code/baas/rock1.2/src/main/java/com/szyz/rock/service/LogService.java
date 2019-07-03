package com.szyz.rock.service;

import com.szyz.rock.model.entity.LogInfo;

import java.util.List;

public interface LogService {

    List<LogInfo> queryLogs(LogInfo logInfo);
}
