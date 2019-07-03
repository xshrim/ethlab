package com.szyz.rock.dao;

import com.szyz.rock.model.Log;
import com.szyz.rock.model.Page;
import com.szyz.rock.model.entity.LogInfo;
import com.szyz.rock.model.entity.PermInfo;

import java.util.List;

public interface PermDao {
    /**
     * 通过用户id查询权限列表
     * @param userId
     * @param curPage
     * @param pageSize
     * @return
     */
    List<PermInfo> queryPermsByUserIdPageList(String userId,Integer curPage,Integer pageSize);

    /**
     * 通过用户id和资源id查询权限列表
     * @param userId
     * @param log
     * @return
     */
    List<PermInfo> queryPermsByUserIdAndItemId(String userId, Log log);

    /**
     * 通过资源id查询权限列表
     * @param itemId
     * @return
     */
    List<PermInfo> queryPermsByItemId(String itemId);

    List<PermInfo> queryPermsPageList(Page page);

    /**
     * 通过权限id查询权限
     * @param pid
     * @return
     */
    PermInfo queryPermById(String pid);

    List<PermInfo> queryPermByTid(String tid);

}
