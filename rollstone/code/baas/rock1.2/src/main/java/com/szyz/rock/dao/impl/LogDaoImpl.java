package com.szyz.rock.dao.impl;

import com.szyz.rock.dao.LogDao;
import com.szyz.rock.model.entity.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogDaoImpl implements LogDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<LogInfo> queryLogs(LogInfo logInfo) {
        Criteria criteria = Criteria.where("timestamp").lte(logInfo.getTimeEnd()).gte(logInfo.getTimeStart()).and("senderid").is(logInfo.getSender());
//         Criteria.where("timestamp").gte(logInfo.getTimeStart())
//               // ,Criteria.where("senderid").is(logInfo.getSender())
      //  );

        Query query = new Query(criteria);
        return mongoTemplate.find(query,LogInfo.class);
    }
}
