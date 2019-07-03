package com.szyz.rock.service.impl;

import com.szyz.rock.dao.LogDao;
import com.szyz.rock.model.entity.LogInfo;
import com.szyz.rock.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogDao logDao;
    @Override
    public List<LogInfo> queryLogs(LogInfo logInfo) {
        return logDao.queryLogs(logInfo);
    }
}
