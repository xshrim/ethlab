package com.szyz.rock.dao;

import com.szyz.rock.model.entity.ItemInfo;

import java.util.List;

public interface ItemDao {

    /**
     * 查询item
     * @param userId
     * @param curPage
     * @param pageSize
     * @return
     */
    List<ItemInfo> queryItemsByUserIdPageList(String userId, int curPage , int pageSize);
}
