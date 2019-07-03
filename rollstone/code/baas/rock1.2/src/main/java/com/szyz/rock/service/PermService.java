package com.szyz.rock.service;

import com.szyz.rock.model.Log;
import com.szyz.rock.model.Page;
import com.szyz.rock.model.Perm;
import com.szyz.rock.model.entity.LogInfo;
import com.szyz.rock.model.entity.PermInfo;

import java.util.List;

public interface PermService {

    int savePerm(Perm perm);
    List<PermInfo> queryItemsByUserIdPageList(String userId, Integer curPage, Integer pageSize);

    List<PermInfo> queryPermsByUserIdAndItemId(String userId, Log log);
    List<PermInfo> queryPermsByItemId(String itemId);
    List<PermInfo> queryPermsPageList(Page page);

    PermInfo queryPermById(String id);

    int setPermStatusById(String permId,Integer status);
    List<PermInfo> queryPermByTid(String tid);


}
