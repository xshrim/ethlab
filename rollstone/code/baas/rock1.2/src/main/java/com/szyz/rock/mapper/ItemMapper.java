package com.szyz.rock.mapper;

import com.szyz.rock.model.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface ItemMapper {

    @Insert("INSERT INTO item_info VALUES(#{iid},#{userId},#{type},#{title},#{content},#{path},#{tid},#{uperId},#{xhash},#{shash},#{ihash},#{cipher},#{ikey},#{iopen},#{level},#{status},NOW())")
    int insertItem(Item item);
    @Select("SELECT id iid , user_id as userId,type,title,content,path,tid,uperid,xhash,shash,ihash,`cipher`,ikey,iopen,`level` ,`status`,create_time createTime FROM item_info where id = #{id}")
    Item selectItemById(String id);

}
