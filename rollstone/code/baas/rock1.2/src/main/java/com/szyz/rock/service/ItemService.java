package com.szyz.rock.service;

import com.szyz.rock.model.Item;
import com.szyz.rock.model.entity.ItemInfo;

import java.util.List;

public interface ItemService {

    int saveItem(Item item);

    List<ItemInfo> queryItemsByUserIdPageList(String userId, int curPage , int pageSize);

    Item getItemById(String id);
}
