package com.szyz.rock.dao.impl;

import com.kenai.jffi.Aggregate;
import com.szyz.rock.dao.PermDao;
import com.szyz.rock.model.Log;
import com.szyz.rock.model.Page;
import com.szyz.rock.model.entity.LogInfo;
import com.szyz.rock.model.entity.PermInfo;
import com.szyz.rock.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermDaoImpl implements PermDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<PermInfo> queryPermsByUserIdPageList(String userId, Integer curPage, Integer pageSize) {
        Query query = new Query(Criteria.where("userid").is(userId));
        return mongoTemplate.find(query,PermInfo.class);
        //return mongoTemplate.findAll(PermInfo.class);
    }

    @Override
    public List<PermInfo> queryPermsByUserIdAndItemId(String userId, Log log) {
        Criteria criteria = Criteria.where("userid").is(userId).and("status").is(1);

        if(Utils.isNotBlank(log.getPermId())) {
            criteria.and("pid").is(log.getPermId());
        }else{
            criteria.and("itemid").is(log.getItemId());
        }
        Query query = new Query(criteria);
        return mongoTemplate.find(query,PermInfo.class);
    }

    @Override
    public List<PermInfo> queryPermsByItemId(String itemId) {
        Query query = new Query(Criteria.where("itemid").is(itemId));
        return mongoTemplate.find(query,PermInfo.class);
    }

    @Override
    public PermInfo queryPermById(String pid) {
        Query query = new Query(Criteria.where("pid").is(pid));
        return mongoTemplate.findOne(query,PermInfo.class);
    }

    @Override
    public List<PermInfo> queryPermByTid(String tid) {
        Query query = new Query(Criteria.where("tid").is(tid));
        return mongoTemplate.find(query,PermInfo.class);
    }

    public List<PermInfo> queryPermsPageList_(Page page){
        //Query query = new Query();
        Criteria criteria = Criteria.where("status").is(1);
        criteria.orOperator(Criteria.where("userid").is(page.getModel()),Criteria.where("sgerid").is(page.getModel()));

        long totalNum = mongoTemplate.count(new Query(criteria), PermInfo.class);
        page.setTotalSize(totalNum);

        Aggregation aggregation = null;
        if(page.getLtOrGt()<=0){
            if(page.getGoPage() !=0)
                criteria.and("mid").lt(page.getMid());
            aggregation = Aggregation.newAggregation(Aggregation.match(criteria)
                    , Aggregation.sort(new Sort(Sort.Direction.DESC, "mid"))
                    , Aggregation.skip(page.getSkip())
                    , Aggregation.limit(page.getPageSize())
            );
        }else if(page.getLtOrGt()>0){
            criteria.and("mid").gt(page.getMid());
            aggregation = Aggregation.newAggregation(Aggregation.match(criteria)
                    , Aggregation.sort(new Sort(Sort.Direction.ASC, "mid"))
                    , Aggregation.skip(page.getSkip())
                    , Aggregation.limit(page.getPageSize())
            );
        }
        aggregation.withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
        //mongoTemplate.aggregate
        AggregationResults<PermInfo> aggregate = mongoTemplate.aggregate(aggregation, "perm", PermInfo.class);
        return aggregate.getMappedResults();
    }

    public List<PermInfo> queryPermsPageList(Page page){


      //  Criteria criteria = Criteria.where("userid").is(page.getModel()).and("status").is(1);
        Criteria criteria = Criteria.where("status").is(1);
        criteria.orOperator(Criteria.where("userid").is(page.getModel()),Criteria.where("sgerid").is(page.getModel()));

        // criteria.orOperator()


        long totalNum = mongoTemplate.count(new Query(criteria), PermInfo.class);
        page.setTotalSize(totalNum);

        Query query = new Query();
        if(page.getGoPage()<=0){ //第一次
            query.skip(page.getSkip()).limit(page.getPageSize());
            query.with(new Sort(Sort.Direction.DESC,"mid"));
            if(page.getGoPage()>0){
                criteria.and("mid").lt(page.getMid());
            }
            query.addCriteria(criteria);
        }else{
            query.skip(page.getSkip()).limit(page.getPageSize());
            query.with(new Sort(Sort.Direction.ASC,"mid"));
            criteria.and("mid").gt(page.getMid());
            query.addCriteria(criteria);
        }
        return mongoTemplate.find(query,PermInfo.class);
    }
}
