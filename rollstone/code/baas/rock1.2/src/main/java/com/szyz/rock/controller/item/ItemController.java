package com.szyz.rock.controller.item;

import com.szyz.rock.model.Item;
import com.szyz.rock.model.User;
import com.szyz.rock.model.entity.ItemInfo;
import com.szyz.rock.model.entity.PermInfo;
import com.szyz.rock.service.ItemService;
import com.szyz.rock.service.PermService;
import com.szyz.rock.util.ConfigKeys;
import com.szyz.rock.util.MD5;
import com.szyz.rock.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("**.action")
public class ItemController {



    @Autowired
    private ItemService itemService;
    @Autowired
    private PermService permService;

    /**
     * 添加资源入链接口
     * @param item 实体类
     * @return code:0成功 -1失败 msg: code为-1时返回
     */
    @RequestMapping("item/addItem")
    public Map<String,Object> addItem(Item item){

        Map<String,Object> resultMap = new HashMap<>();
        try{
            if(Utils.isBlank(item.getPath())
                    //|| item.getIopen() == null || item.getLevel() ==null
                    ){
                resultMap.put("code",-1);
                resultMap.put("msg","参数不正确");
                return resultMap;
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("code",-1);
            resultMap.put("msg","参数不正确");
            return resultMap;
        }


        User user = (User) SecurityUtils.getSubject().getPrincipal();

        item.setIid(Utils.getUUID());

        item.setPath(item.getPath().replace(ConfigKeys.basepath,""));

        item.setCipher("HmacSHA1");
        item.setIkey("szyz");
        item.setXhash(MD5.crypt("szyz"));
        item.setShash(MD5.crypt("szyz"));

        item.setTid("");
        item.setIopen(0);
        item.setLevel(0);
        item.setStatus(1);
        item.setUserId(user.getUserName());
        item.setUperId(user.getUserName());


        int res = itemService.saveItem(item);



        if(res >0)
            resultMap.put("code",0);
        else{
            resultMap.put("code",-1);
            resultMap.put("msg","添加失败");
        }
        return resultMap;
    }

    /**
     * 获取资源列表接口
     * @param type:1查自己上传的视频 2查授权的视频 3同时都查询
     * @return code：0成功
     * 	preFix：地址前缀
     * 	list[
     * 		{
     * 			iid:资源id
     * 			tid：资源父id
     * 			uperId：资源上传者id
     * 			userId:资源所有者id
     * 			iopen：资源公开度
     * 			level：资源安全级别
     * 			timestamp：资源生成时间戳
     * 			status:资源状态(0: 不存在, 1: 正常, 2：冻结， 6: 不可用)
     * 			title:标题
     * 			content：描述
     * 			type：类别 1视频 2文本
     * 			path：资源网络路径
     * 			permId:下级授权权限父id
     * 			permInfo:{
     * 				sgerid:授权者
     * 				ptimestamp：下标为0的为授权时间
     * 			}
     * 		}
     */
    @RequestMapping("item/getItemList")
    public Map<String,Object> getItemList(Integer type ){
        if(type == null)
            type = 1;
        Map<String,Object> resultMap = new HashMap<>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        //----
        //EthItemHandle ethItemHandle = new EthItemHandle();
       // List<Item> items = ethItemHandle.getAllItemByBC(); // FIXME 后面改成从MongoDB获取
        //-----
        List<Item> items = null;
        if(type ==1){ //查自己
            List<ItemInfo> itemInfos = itemService.queryItemsByUserIdPageList(user.getUserName(), 1, 100);
            if(itemInfos.size()>0)
                items = new ArrayList<>();
            for(ItemInfo itemInfo:itemInfos){
                Item item = itemService.getItemById(itemInfo.getIid());
                item.setTimestamp(itemInfo.getTimestamp());
                //item.setPath(ConfigKeys.basepath + item.getPath());
                items.add(item);
            }
        }else if(type ==2) { //查授权
            List<PermInfo> permInfos = permService.queryItemsByUserIdPageList(user.getUserName(), 1, 100);
            if(permInfos.size()>0){
                items = new ArrayList<>();
                for(PermInfo permInfo : permInfos){

                    //先去MongoDB查item，然后再去mysql补充信息

                    //mysql
                    Item item = itemService.getItemById(permInfo.getItemid());
                    item.setPermId(permInfo.getPid());
                    item.setPermInfo(permInfo);
                    items.add(item);
                }
            }
        }else { //都查

        }
        resultMap.put("code",0);
        resultMap.put("list",items);
        resultMap.put("preFix",ConfigKeys.basepath);
        return resultMap;
    }
}
