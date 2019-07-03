package com.szyz.rock.test;

import com.szyz.rock.handle.contract.RS;
import com.szyz.rock.model.Item;
import com.szyz.rock.model.Log;
import com.szyz.rock.model.Perm;
import com.szyz.rock.model.User;
import com.szyz.rock.util.Utils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.ClientTransactionManager;

import java.math.BigInteger;
import java.util.*;

public class AllTest {
    Web3j web3j = Web3j.build(new HttpService("http:\\\\172.16.201.191:8545"));
    Admin admin = Admin.build(new HttpService("http:\\\\172.16.201.191:8545"));

    private String nodeAddr="0xdf16134350a56a12cdf970465670434029a1e927";
    private String contractAddr = "0x5ed6385e4f2FedbCE411C678D620c85D617fc106";
    private static List<User> users = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    private static User adminUser;
    private static User user ;

    public static void main(String[] args){
        adminUser = new User();
        adminUser.setUserName("admin");
        Scanner scanner = new Scanner(System.in);
        AllTest all = new AllTest();
        while (true){
            System.out.println("===========================================");
            System.out.println("===========================================");
            System.out.println(user ==null?"未登录":"当前用户：" + user.getUserName());
            if(user == null)
                System.out.println("请输入：");
            System.out.println("0.登录：");
            System.out.println("1.创建用户：");
            System.out.println("2.上传的资源：");
            System.out.println("3.授权：");
            System.out.println("4.资源列表：");
            System.out.println("5.操作资源：");
            if(user!=null)
                System.out.println("9.退出");
            String input = scanner.next();
            switch (input){
                case "0":
                    System.out.println("请输入用户名：");
                    String username = scanner.next();
                    for(User u : users){
                        if(u.getUserName().equals(username))
                            user = u;
                    }
                    if(user==null)
                        user = all.getUser(username);
                    if(user==null)
                        System.out.println("用户不存在");
                    break;
                case "1":
                    System.out.println("请输入用户名：");
                    username = scanner.next();
                    System.out.println("创建用户中...");
                    all.createUser(username);
                    break;
                case "2":
                   /* for(User u : users){
                        System.out.println(u.getUserName());
                    }
                    System.out.println("请选择用户:");
                    username = scanner.next();
                    for(User u : users){
                        if(u.getUserName().equals(username))
                            user = u;
                    }*/
                    System.out.println("请输入文件title");
                    all.addItem(scanner.next());
                    break;
                case "3":
                    for(int i = 0;i< users.size();i++){
                        System.out.println((i+1)+"." + users.get(i).getUserName());
                    }
                    System.out.println("请选择被授权用户:");
                    int toUserIndex = scanner.nextInt();
                    User toUser = users.get(toUserIndex-1);
                    for(int i=0;i<items.size();i++){
                        if(items.get(i).getUperId().equals(user.getUserName())){
                            System.out.println((i+1)+"." + items.get(i).getTitle() );
                        }
                    }
                    System.out.println("请选择资源");
                    int itemIndex = scanner.nextInt();
                    Item toItem= items.get(itemIndex-1);

                    all.addPerm(toUser.getUserName(),toItem.getIid());
                    break;
                case "4":
                    List<Item> items = all.getItems();
                    for(Item item : items)
                        System.out.println("iid:" + item.getIid());
                    break;
                case "5":
                    List<Item> itemByPerm = all.getItemByPerm(user.getUserName());
                    for(int i = 0 ;i<itemByPerm.size();i++){
                        System.out.println( (i+1)+"." + itemByPerm.get(i).getIid());
                    }
                    System.out.println("请选择资源：");
                    itemIndex = scanner.nextInt();
                    Item item = itemByPerm.get(itemIndex - 1);
                    long curtime = 0;
                    while (true){
                        System.out.println("请选择操作：");

                        System.out.println("1.play");
                        System.out.println("2.stop");
                        System.out.println("3.download");
                        System.out.println("4.退出");
                        int actionIndex = scanner.nextInt();
                        if(actionIndex ==4)
                            break;
                        Log log = new Log();
                        log.setUserId(user.getUserName());
                        if(actionIndex == 1) {
                            curtime = System.currentTimeMillis();
                            log.setAction("play");
                            log.setDuration(BigInteger.valueOf(0));
                        }
                        if(actionIndex == 2) {
                            log.setAction("stop");
                            log.setDuration(BigInteger.valueOf((System.currentTimeMillis() - curtime)/1000));
                            curtime = 0;
                        }
                        if(actionIndex ==3) {
                            log.setAction("download");
                            log.setDuration(BigInteger.valueOf(-1));
                        }
                        log.setItemId(item.getIid());
                        all.addLog(log);
                    }

                    break;
                case "9":
                    user = null;
                    System.out.println("退出成功");
                    break;
            }
        }
    }

    private User createUser(String username){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, nodeAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        User user = new User();
        user.setUserName(username);
        user.setCompanyId(0);
        user.setStatus((byte)2);
        user.setLevel((byte)6);
        String ethAddr = createAccount(user.getUserName(),admin);
        System.out.println("生成区块链地址："+ethAddr);
        user.setEthAddr(ethAddr);
        user.setEmail("3rwfesf@net.com");
        user.setPhone("12333333333");
        user.setSex((byte)1);
        user.setBirthdayStr("2018-09-01");
        user.setIdcard("5933333333333333");
        user.setIdcardPhoto("pathidcard");
        user.setRealName("xiaofan");
        user.setCreateTime(new Date());
        user.setHash(user.buildHash());
        user.setDetail(user.buildDetail("add", "172.16.201.191:8545", ""));

        try{
            TransactionReceipt receipt = rs.addUser(ethAddr
                    , Arrays.asList(
                            Utils.bytes2bytes(user.getUserName().getBytes(), 32)
                            , Utils.bytes2bytes(user.getCompanyId() == null ? "0".getBytes() : user.getCompanyId().toString().getBytes(), 32)
                            , Utils.bytes2bytes("hash".getBytes(), 32)

                    )
                    , Arrays.asList(
                            BigInteger.valueOf(user.getLevel())
                            , BigInteger.valueOf(1)
                    )
                    , Utils.bytes2bytes(Utils.getUUID().getBytes(), 32)
                     , user.getDetail()
                    , getBalance(nodeAddr,web3j).divide(BigInteger.valueOf(10))
            ).send();
            System.out.println("创建用户："+ receipt.getStatus());
            if("0x1".equals(receipt.getStatus()))
                users.add(user);
        }catch (Exception e){
            e.printStackTrace();
        }

        return user;
    }

    private Item addItem(String title){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, user.getEthAddr(), 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        Item item = new Item();
        item.setIid(Utils.getUUID());
        System.out.println("资源id"+item.getIid());
        item.setTid("");
        item.setUserId(user.getUserName());
        item.setUperId(user.getUserName());
        item.setShash(Utils.getUUID());
        item.setXhash(Utils.getUUID());
        item.setCipher("sha1");
        item.setIkey("szyz");
        item.setIopen(0);
        item.setLevel(1);
        item.setStatus(1);
        item.setTitle(title);
        item.setContent("this ...");
        item.setType(1);
        item.setPath("thisispath");

        item.setIhash(item.buildHash());
        try{
            List<byte[]> bts = Arrays.asList(
                    Utils.bytes2bytes(item.getIid().getBytes(), 32),
                    Utils.bytes2bytes(item.getTid().getBytes(), 32),
                    // Utils.bytes2bytes(item.getUperId().getBytes(), 32),
                    Utils.bytes2bytes(item.getUserId().getBytes(), 32),
                    Utils.bytes2bytes(item.getXhash().getBytes(), 32),
                    Utils.bytes2bytes(item.getShash().getBytes(), 32),
                    Utils.bytes2bytes(item.getIhash().getBytes(), 32),
                    Utils.bytes2bytes(item.getCipher().getBytes(), 32),
                    Utils.bytes2bytes(item.getIkey().getBytes(), 32)

            );
            //@param its: int64类型字段[资源公开度, 资源安全级别, 资源状态]
            List<BigInteger> its = Arrays.asList(
                    BigInteger.valueOf(item.getIopen()),
                    BigInteger.valueOf(item.getLevel()),
                    BigInteger.valueOf(item.getStatus())
            );
            try{
                //解锁
                PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
                System.out.println("用户解锁："+ unlockAccount.getResult());
                TransactionReceipt receipt = rs.uplItem(bts, its, Utils.bytes2bytes(Utils.getUUID().getBytes(), 32), item.buildDetail("add","172.16.201.189:8454", "")).send();
                System.out.println("添加资源：" +receipt.getStatus());
                if("0x1".equals(receipt.getStatus()))
                    items.add(item);
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return item;
    }

    private Perm addPerm(String toUserName ,String itemId){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, user.getEthAddr(), 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        Perm perm = new Perm();
        perm.setId(Utils.getUUID());
        System.out.println("权限id：" + perm.getId());
        perm.setTid("");
        perm.setUserId(toUserName);
        perm.setItemId(itemId);
        perm.setPhash(Utils.getUUID());
        perm.setDevice("");
        perm.setPmark("cxfsy");
        perm.setPrvs(Arrays.asList(BigInteger.valueOf(1),BigInteger.valueOf(1),BigInteger.valueOf(1),BigInteger.valueOf(1)));
        perm.setPtime(Arrays.asList(BigInteger.valueOf(300),BigInteger.valueOf(300)));
        perm.setPtimes(Arrays.asList(BigInteger.valueOf(4),BigInteger.valueOf(4)));
        perm.setPslice(Arrays.asList(BigInteger.valueOf(-1),BigInteger.valueOf(-1)));
        perm.setPtimestamp(Arrays.asList(BigInteger.valueOf(System.currentTimeMillis()/1000),BigInteger.valueOf(System.currentTimeMillis()/1000+1000)));
        perm.setPtype(0);
        perm.setStatus(1);

        String detail = perm.buildDetail("add", "172.16.201.191:8545", "");
        System.out.println(detail);



        List<byte[]> bts = Arrays.asList(
                Utils.bytes2bytes(perm.getId().getBytes(), 32),
                Utils.bytes2bytes(perm.getTid().getBytes(), 32),
                Utils.bytes2bytes(perm.getUserId().getBytes(), 32),
                Utils.bytes2bytes(perm.getItemId().getBytes(), 32),
                Utils.bytes2bytes(perm.getPhash().getBytes(), 32),
                Utils.bytes2bytes(perm.getDevice().getBytes(), 32),
                Utils.bytes2bytes(perm.getPmark().getBytes(), 32)
        );

//int64类型字段[授权查看时间, 授权查看次数, 授权查看起始时间段, 授权查看终止时间段,
// 授权过期时间戳, 授权类型, 授权状态]
        List<BigInteger> its = Arrays.asList(
                perm.getPtime().get(1),
                perm.getPtimes().get(1),
                perm.getPslice().get(0),
                perm.getPslice().get(1),
                perm.getPtimestamp().get(1),
                BigInteger.valueOf(perm.getPtype()),
                BigInteger.valueOf(perm.getStatus())
        );

        try{


            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println("解锁：" + unlockAccount.getResult());
            TransactionReceipt receipt = rs.addPerm(
                    bts,
                    perm.getPrvs(),
                    its,
                    Utils.bytes2bytes(Utils.getUUID().getBytes(), 32),
                    perm.buildDetail("add","172.16.201.191:8454","")
            ).send();
            System.out.println("授权："+receipt.getStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
        return perm;
    }

    private List<Item> getItems(){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, nodeAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));
        List<Item> is = new ArrayList<>();
        try{

            int num = rs.getItemNum().send().intValue();
            for(int i=0;i<num;i++){

                Tuple2<List<byte[]>, List<BigInteger>> result = rs.getItemByIdx(BigInteger.valueOf(i))
                        .send();
                Item item = new Item();
                item.setIid(new String(result.getValue1().get(0)));
                item.setTid(new String(result.getValue1().get(1)).trim());
                item.setUperId(new String(result.getValue1().get(2)).trim());
                item.setUserId(new String(result.getValue1().get(3)).trim());
                item.setXhash(new String(result.getValue1().get(4)).trim());
                item.setShash(new String(result.getValue1().get(5)).trim());
                item.setIhash(new String(result.getValue1().get(6)).trim());
                item.setCipher(new String(result.getValue1().get(7)).trim());
                item.setIkey(new String(result.getValue1().get(8)).trim());

                item.setIopen(result.getValue2().get(0).intValue());
                item.setLevel(result.getValue2().get(1).intValue());
                item.setTimestamp(result.getValue2().get(2).longValue());
                item.setStatus(result.getValue2().get(3).intValue());
                is.add(item);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }

    private User getUser(String username){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, nodeAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));
        try{
            Tuple7<String, byte[], byte[], byte[], BigInteger, BigInteger, BigInteger> result = rs.getUserById(Utils.bytes2bytes(username.getBytes(), 32)).send();
            User user = new User();
            user.setUserName(username);
            user.setEthAddr(result.getValue1());
            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private List<Item> getItemByPerm(String userName){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, nodeAddr, 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));
        List<Item> is  = new ArrayList<>();
        try{
            int num = rs.getPermNum().send().intValue();
            for(int i= 0 ;i<num;i++){
                Tuple4<List<byte[]>, List<BigInteger>, List<BigInteger>, List<BigInteger>> perm = rs.getPermByIdx(BigInteger.valueOf(i)).send();
                String userid = new String( perm.getValue1().get(3)).trim();
                //String itemid = new String( perm.getValue1().get(4)).trim();
                if(userid.equals(userName)){
                    Item item = getItemById(perm.getValue1().get(4));
                    if(item!=null)
                        is.add(item);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }

    private Item getItemById(byte[] id){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, user.getEthAddr(), 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));

        try{
            Tuple2<List<byte[]>, List<BigInteger>> result = rs.getItemById(id)
                    .send();
            Item item = new Item();
            item.setIid(new String(result.getValue1().get(0)));
            item.setTid(new String(result.getValue1().get(1)).trim());
            item.setUperId(new String(result.getValue1().get(2)).trim());
            item.setUserId(new String(result.getValue1().get(3)).trim());
            item.setXhash(new String(result.getValue1().get(4)).trim());
            item.setShash(new String(result.getValue1().get(5)).trim());
            item.setIhash(new String(result.getValue1().get(6)).trim());
            item.setCipher(new String(result.getValue1().get(7)).trim());
            item.setIkey(new String(result.getValue1().get(8)).trim());

            item.setIopen(result.getValue2().get(0).intValue());
            item.setLevel(result.getValue2().get(1).intValue());
            item.setTimestamp(result.getValue2().get(2).longValue());
            item.setStatus(result.getValue2().get(3).intValue());

            return item;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void addLog(Log log){
        RS rs = RS.load(contractAddr, web3j, new ClientTransactionManager(web3j, user.getEthAddr(), 40, 200), BigInteger.valueOf(1), BigInteger.valueOf(5000000));
        log.setId(Utils.getUUID());
        List<byte[]> btn = Arrays.asList(
                Utils.bytes2bytes(log.getUserId().getBytes(),32),
                Utils.bytes2bytes(log.getItemId().getBytes(),32),
                Utils.bytes2bytes(log.getAction().getBytes(),32)
        );
        List<byte[]> pids = Arrays.asList(Utils.bytes2bytes("".getBytes(),32));
        try{

            PersonalUnlockAccount unlockAccount = admin.personalUnlockAccount(user.getEthAddr(), user.getUserName()).send();// 暂时userName是密码
            System.out.println("解锁：" + unlockAccount.getResult());
            TransactionReceipt receipt = rs.addLog(
                    btn,
                    pids,
                    log.getDuration(),
                    Utils.bytes2bytes(log.getId().getBytes(), 32),
                    log.buildDetails("172.16.201.191:8545", "")).send();

            System.out.println("addLog:" + receipt.getStatus());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String createAccount(String userPassword ,Admin admin){
        try{
            NewAccountIdentifier account = admin.personalNewAccount(userPassword).send();
            if(account != null && Utils.isNotBlank(account.getAccountId()))
                return account.getAccountId() ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public BigInteger getBalance(String accountAddress, Web3j web3j){
        try{
            EthGetBalance ethGetBalance = web3j.ethGetBalance(accountAddress, DefaultBlockParameter.valueOf("latest")).send();
            if(ethGetBalance !=null)
                return ethGetBalance.getBalance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return BigInteger.valueOf(0);
    }
}
