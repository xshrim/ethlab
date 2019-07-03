package com.szyz.rock;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.szyz.rock.handle.base.EthAccountHandle;
import com.szyz.rock.model.Page;
import com.szyz.rock.model.User;
import com.szyz.rock.model.entity.ItemInfo;
import com.szyz.rock.model.entity.PermInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RockApplicationTests {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void contextLoads() {

        Page page = new Page();
        page.setModel("hb111");


        Criteria criteria = Criteria.where("status").is(1);
        criteria.orOperator(Criteria.where("userid").is(page.getModel()),Criteria.where("sgerid").is(page.getModel()));

        long totalNum = mongoTemplate.count(new Query(criteria), PermInfo.class);
        page.setTotalSize(totalNum);

        Aggregation aggregation = null;
        if(page.getLtOrGt()<=0){
            if(page.getLtOrGt() !=0)
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
        List<PermInfo> mappedResults = aggregate.getMappedResults();
        for(PermInfo pi : mappedResults)
            System.out.println(pi.getPid());

    }

    @Test
    public void addUserCB(){
        EthAccountHandle ethAccountHandle = new EthAccountHandle();
        User user = new User();
        user.setUserName("test1112");
        user.setPwd("qwe12321");
        user.setCompanyId(1);
        user.setLevel((byte)2);
        user.setStatus((byte)1);
        user.setEthAddr("fdsfefefe");
        user.setBirthday(new Date());
        user.setSex((byte)1);
        user.setEmail("defesef");

         boolean b = ethAccountHandle.addUser(user);
         System.out.println("----------- " + b);
    }

}
