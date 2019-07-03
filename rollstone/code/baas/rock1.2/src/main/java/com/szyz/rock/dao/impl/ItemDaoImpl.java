package com.szyz.rock.dao.impl;

import com.szyz.rock.dao.ItemDao;
import com.szyz.rock.model.entity.ItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class ItemDaoImpl implements ItemDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<ItemInfo> queryItemsByUserIdPageList(String userId, int curPage, int pageSize) {
//       Query query = new Query(Criteria.where("uperid").is(userId).orOperator(Criteria.where("userid").is(userId)));
//       return mongoTemplate.find(query, ItemInfo.class);
       Query query = new Query(Criteria.where("uperid").is(userId).orOperator(Criteria.where("userid").is(userId)));
       return mongoTemplate.find(query, ItemInfo.class);
      // return  mongoTemplate.findAll(ItemInfo.class);
    }
}
