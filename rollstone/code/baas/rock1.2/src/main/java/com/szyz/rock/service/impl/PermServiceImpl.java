package com.szyz.rock.service.impl;

import com.szyz.rock.handle.base.EthPermHandle;
import com.szyz.rock.mapper.ItemMapper;
import com.szyz.rock.mapper.PermMapper;
import com.szyz.rock.dao.PermDao;
import com.szyz.rock.model.Item;
import com.szyz.rock.model.Log;
import com.szyz.rock.model.Page;
import com.szyz.rock.model.Perm;
import com.szyz.rock.model.entity.LogInfo;
import com.szyz.rock.model.entity.PermInfo;
import com.szyz.rock.service.PermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermServiceImpl implements PermService {

    @Autowired
    private PermMapper permMapper;
    @Autowired
    private PermDao permDao;
    @Autowired
    private ItemMapper itemMapper;
    @Override
    public int savePerm(Perm perm) {

        EthPermHandle ethPermHandle = new EthPermHandle();
       // perm.setPhash(perm.);
        String res = ethPermHandle.addPerm(perm);
        if("0x1".equals(res)){
            permMapper.insertPerm(perm);
        }else
            return -1;
        return 1;
    }

    @Override
    public List<PermInfo> queryItemsByUserIdPageList(String userId, Integer curPage, Integer pageSize) {
        return permDao.queryPermsByUserIdPageList(userId,curPage,pageSize);
    }

    @Override
    public List<PermInfo> queryPermsByUserIdAndItemId(String userId, Log log) {
        return permDao.queryPermsByUserIdAndItemId(userId,log);
    }

    @Override
    public List<PermInfo> queryPermsByItemId(String itemId) {
        return permDao.queryPermsByItemId(itemId);
    }

    @Override
    public List<PermInfo> queryPermsPageList(Page page) {
        List<PermInfo> permInfos = permDao.queryPermsPageList(page);
        for(PermInfo p : permInfos){
            Item item = itemMapper.selectItemById(p.getItemid());
            item.setPath(null);
            item.setLevel(null);
            item.setIopen(null);
            item.setIkey(null);
            item.setCipher(null);
            p.setItem(item);
        }
        return permInfos;
    }

    @Override
    public PermInfo queryPermById(String id) {
        return permDao.queryPermById(id);
    }

    @Override
    public int setPermStatusById(String permId, Integer status) {
        EthPermHandle ethPermHandle = new EthPermHandle();
        String resutl = ethPermHandle.setPermStatusById(permId, status);
        if("0x1".equals(resutl)){
            return 0;
        }
        return -1;
    }

    @Override
    public List<PermInfo> queryPermByTid(String tid) {
        return permDao.queryPermByTid(tid);
    }
}
