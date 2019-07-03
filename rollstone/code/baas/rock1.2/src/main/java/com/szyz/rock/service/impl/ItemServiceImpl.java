package com.szyz.rock.service.impl;

import com.szyz.rock.handle.base.EthItemHandle;
import com.szyz.rock.mapper.ItemMapper;
import com.szyz.rock.dao.ItemDao;
import com.szyz.rock.model.Item;
import com.szyz.rock.model.entity.ItemInfo;
import com.szyz.rock.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDao itemDao;

    @Override
    public int saveItem(Item item) {

        EthItemHandle ethItemHandle = new EthItemHandle();
        String res = ethItemHandle.uplItem(item);
        System.out.println(res);
        if("0x1".equals(res)){
            return itemMapper.insertItem(item);
        }else{
            return -1;
        }
    }

    @Override
    public List<ItemInfo> queryItemsByUserIdPageList(String userId, int curPage, int pageSize) {
        return itemDao.queryItemsByUserIdPageList(userId,curPage,pageSize);
    }

    @Override
    public Item getItemById(String id) {
        return itemMapper.selectItemById(id);
    }
}
