pragma solidity >=0.5.0 <0.7.0;

contract Coop {

    struct Project {
        bytes32 id;             // 项目ID
        bytes32 pid;            // 父项目ID (存在父项目表示该项目为一个项目的子项目, 不存在父项目的项目为顶级项目)
        // bytes32 rid;            // 根项目ID
        address sponsor;        // 发起人(如果项目非顶级项目, 则表示认定的模块认领者)
        string title;           // 标题
        string class;           // 类别(如果项目非顶级项目,则表示其模块类别, 如开发,推广,销售等)
        string desc;            // 描述
        uint ratio;             // 在父项目中的分成比例(顶级项目分成比例为100)
        bytes32[] modules;      // 子项目列表
        address[] claimers;     // 认领者列表(顶级项目不允许认领, 只可认领顶级项目的模块)
        bytes32[] orders;       // 订单列表
        uint status;            // 状态(0: 已废弃; 1:已发起; 2:已认领; 3:已交付; 4:已完成)
        uint timestamp;         // 发起时间
    }

    struct Order {
        bytes32 id;             // 订单ID
        bytes32 pid;            // 项目ID
        address buyer;          // 购买者
        uint amount;            // 金额
        uint status;            // 状态(0: 已废弃; 1:已发起; 2:已完成)
        uint timestamp;         // 订单时间
    }

    string public version;      // 合约版本

    address public owner;       // 合约发布者

    mapping (bytes32 => Project) public projects;   // 项目列表(包括子项目)

    mapping (address => bytes32[]) public tasks;    // 与地址相关的项目列表(包括发起的, 认领的, 认定的)

    mapping (bytes32 => Order) public orders;     // 订单列表

    mapping (address => bytes32[]) public addords;  // 与地址相关的订单列表

    mapping (address => uint) public balances;

    // Events allow clients to react to specific
    // contract changes you declare
    event Sent(address from, address to, uint amount);

    // Constructor code is only run when the contract
    // is created
    constructor(string memory ver) public {
        owner = msg.sender;
        version = ver;
    }

    /*@ 将两个bytes32变量进行hash
    //@desc: bytes32类型pack后进行sha3
    //@param x: 变量x
    //@param y: 变量y
    //@return 0: hash值
    */
    function concat(bytes32 x, bytes32 y) internal pure returns (bytes32) {
        /*
        bytes memory s = new bytes(64);
        for (uint i = 0; i < 32; i++){
            s[i] = x[i];
            s[i+32] = y[i];
        }
        return keccak256(s);
        */
        /*
        assembly {
            result := mload(add(s, 32))
        }
        */

        // abi.encodePacked() 和abi.encode() 可以支持任意数量的任意不同类型的参数
        return keccak256(abi.encodePacked(x, y));
    }


    /* 字符串拼接
    //@param _a: 字符串a
    //@param _b: 字符串b
    //@param _c: 字符串c
    //@param _d: 字符串d
    //@param _e: 字符串e
    //@return 0: string类型返回值
    */
    function strConcat(string memory _a, string memory _b, string memory _c, string memory _d, string memory _e) public pure returns (string memory){
        bytes memory _ba = bytes(_a);
        bytes memory _bb = bytes(_b);
        bytes memory _bc = bytes(_c);
        bytes memory _bd = bytes(_d);
        bytes memory _be = bytes(_e);
        string memory abcde = new string(_ba.length + _bb.length + _bc.length + _bd.length + _be.length);
        bytes memory babcde = bytes(abcde);
        uint i = 0;
        uint k = 0;
        for (i = 0; i < _ba.length; i++) babcde[k++] = _ba[i];
        for (i = 0; i < _bb.length; i++) babcde[k++] = _bb[i];
        for (i = 0; i < _bc.length; i++) babcde[k++] = _bc[i];
        for (i = 0; i < _bd.length; i++) babcde[k++] = _bd[i];
        for (i = 0; i < _be.length; i++) babcde[k++] = _be[i];
        return string(babcde);
    }


    /*@ 转换bytes32类型数据为string
    //@desc: 直接转换
    //@param data: bytes32类型数据
    //@return 0: string类型返回值
    */
    function bytes32ToString(bytes32 data) public pure returns (string memory) {
        bytes memory bytesString = new bytes(32);
        uint charCount = 0;
        for (uint j = 0; j < 32; j++) {
            byte char = byte(bytes32(uint(data) * 2 ** (8 * j)));
            if (char != 0) {
                bytesString[charCount] = char;
                charCount++;
            }
        }
        bytes memory bytesStringTrimmed = new bytes(charCount);
        for (uint j = 0; j < charCount; j++) {
            bytesStringTrimmed[j] = bytesString[j];
        }
        return string(bytesStringTrimmed);
    }

    /*@ 转换bytes32数组数据为string
    //@desc: 拼接以逗号作为分隔符
    //@param data: bytes32数组数据
    //@param idx: 使用bytes32数组的前几个元素
    //@return 0: string类型返回值
    */
    function bytes32ArrayToString(bytes32[] memory data, int64 idx) public pure returns (string memory) {
        int64 len = idx;
        bytes memory bytesString;
        if (len < 0 || len > int64(data.length)) {
            len = int64(data.length);
        } else if (len == 0) {
            return "";
        }
        bytesString = new bytes(uint(len) * 33);     // data+分隔符长度

        uint urlLength = 0;
        for (uint i = 0; i < uint(len); i++) {
            for (uint j = 0; j < 32; j++) {
                byte char = byte(bytes32(uint(data[i]) * 2 ** (8 * j)));
                if (char != 0) {
                    bytesString[urlLength++] = char;
                }
            }
            bytesString[urlLength++] = 0x2c;     //分隔符 0x2c: 逗号, 0x20: 空格
            /*
            if (separator != 0x00) {
                bytesString[urlLength++] = byte(separator);
            }
            */
        }
        bytes memory bytesStringTrimmed = new bytes(urlLength);
        for (uint i = 0; i < urlLength; i++) {
            bytesStringTrimmed[i] = bytesString[i];
        }
        return string(bytesStringTrimmed);
    }

    /* 功能函数
    function StringToBytesVer1(string memory source) public pure returns (bytes result) {
        return bytes(source);
    }

    function stringToBytesVer2(string memory source) public pure returns (bytes32 result) {
        assembly {
          result := mload(add(source, 32))
        }
    }
    */

    // Sends an amount of newly created coins to an address
    // Can only be called by the contract creator
    function mint(address receiver, uint amount) public {
        //require(msg.sender == minter);
        //require(amount < 1e60);
        balances[receiver] += amount;
    }

    // Sends an amount of existing coins
    // from any caller to an address
    function send(address receiver, uint amount) public {
        require(amount <= balances[msg.sender], "Insufficient balance.");
        balances[msg.sender] -= amount;
        balances[receiver] += amount;
        emit Sent(msg.sender, receiver, amount);
    }

    // 判断项目剩余分成比例以及指定的子项目是否存在
    function remainRatio(bytes32 id, bytes32 sid) internal view returns (uint, bool) {
        uint remain = 100;
        bool existed = false;
        Project memory proj = projects[id];
        for (uint i = 0; i < proj.modules.length; i++) {
            Project memory subproj = projects[proj.modules[i]];
            if (subproj.id == sid) {
                existed = true;
            }
            if (remain <= subproj.ratio) {
                return (0, existed);
            } else {
                remain -= subproj.ratio;
            }
        }

        return (remain, existed);
    }

    // 发布
    function publish(bytes32 pid, string memory title, string memory class, string memory desc, uint ratio) public returns (bool) {
        bytes32 id = keccak256(abi.encodePacked(msg.sender, title, block.timestamp));
        // bytes32 rid;
        address sponsor = address(0);
        if (pid != 0x00) {   // 发布的不是顶级项目, 表示添加子项目, 作相应判定
            Project storage pproj = projects[pid];
            // 判断父项目是否存在以及状态是否允许添加子项目
            require(pproj.id != 0x00, "parent project id can not be empty");

            require(pproj.status == 1, "parent project status must be 1");

            require(pproj.sponsor == msg.sender, "msg sender must be sponsor of parent project"); // 只有父项目发起人才能发布子项目

            uint remain;
            bool existed;
            (remain, existed) = remainRatio(pid, id);

            require(remain >= ratio, "remain ratio insufficient"); // 判断剩余分成是否满足该子项目分成比例要求

            require(existed == false, "project existed"); // 已存在的子项目不允许再发布

            // rid = pproj.rid;

            pproj.modules.push(id);  // 项目添加到父项目的子项目列表中

        } else {
            require(ratio == 100, "the ratio of root project must be 100"); // 顶级项目的分成比例必须为100

            sponsor = msg.sender;

            // rid = id;
        }

        projects[id] = Project(id, pid, sponsor, title, class, desc, ratio, new bytes32[](0), new address[](0), new bytes32[](0), 1, block.timestamp);

        return true;
    }

    // 认领
    function claim(bytes32 id) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许认领

        require(proj.status == 1, "project status muste be 1");  // 项目必须是可认领状态

        require(projects[proj.pid].sponsor != msg.sender, "msg sender can not be parent project sponsor"); // 项目发起人不允许认领

        for (uint i = 0; i < proj.claimers.length; i++) {
            require(msg.sender != proj.claimers[i], "msg sender has already claimed"); // 已认领过的地址不允许重复认领
        }

        proj.claimers.push(msg.sender);

        tasks[msg.sender].push(id);

        return true;
    }

    // 认定
    function claimSign(bytes32 id, address claimer) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许认领

        require(proj.status == 1, "project status muste be 1");  // 项目必须是可认领状态

        require(projects[proj.pid].sponsor == msg.sender, "msg sender must be parent project sponsor"); // 只有父项目发起人才可进行子项目的认领审批

        require(proj.sponsor == address(0), "project has been claimed"); // 已被认领的项目不允许进行认领审批

        bool flag = false;
        for (uint i = 0; i < proj.claimers.length; i++) {
            if (proj.claimers[i] == claimer) {   // 已认领过的地址不允许重复认领
                flag = true;
                break;
            }
        }

        if (flag == false) {         // 只有处于认领列表中的地址才可以被批准
            revert("failed");
        }

        proj.sponsor = claimer;

        proj.status = 2;

        return true;
    }

    // 交付
    function claimDeliver(bytes32 id) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许交付

        require(proj.status == 2, "project status must be 2"); //已认定的项目才允许进行交付

        require(proj.sponsor == msg.sender, "msg sender must be project sponsor");  // 非项目发起或认领者不允许交付项目

        bool flag = true;
        for (uint i = 0; i < proj.modules.length; i++) {
            if (projects[proj.modules[i]].status != 4) {   // 项目交付要求子项目均已完成
                flag = false;
                break;
            }
        }

        if (flag != true) {
            revert("failed");
        }

        proj.status = 3;

        return true;
    }

    // 交付审核
    function deliverySign(bytes32 id, bool ok) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许交付审核

        require(proj.status == 3, "project status must be 3"); //已交付的项目才允许进行交付审核

        require(projects[proj.pid].sponsor == msg.sender, "msg sender must be parent project sponsor"); // 只有父项目的发起或认定者可以交付审核

        if (ok == true) {
            proj.status = 4;
        } else {
            proj.status = 2;
        }

        return true;
    }

    // 项目取消
    function projectCancel(bytes32 id) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.status <= 2, "project status must be <=2"); // 已交付和已完成的项目不允许取消

        if (proj.sponsor == msg.sender) {   // 项目发起者或者认定者放弃项目要求项目下所有子项目均为废弃或未认领状态
            bool flag = true;
            for (uint i = 0; i < proj.modules.length; i++) {
                if (projects[proj.modules[i]].status > 1) {   // 项目子项目状态
                    flag = false;
                    break;
                }
            }

            if (flag == false) {         // 项目子项目状态不符合要求
                revert("failed");
            } else {
                if (proj.pid == 0x00) {  // 顶级项目取消时只标记项目状态为已废弃
                    proj.status = 0;
                } else {
                    proj.modules = new bytes32[](0);  // 非顶级项目的子项目随认领的取消而删除
                    proj.status = 1;   // 重新回到发起状态
                    proj.sponsor = address(0);
                }
            }
        }

        // 从认领者列表删除
        for (uint i = 0; i < proj.claimers.length; i++) {
            if (proj.claimers[i] == msg.sender) {
                delete proj.claimers[i];
            }
        }

        // 不从地址关联的项目列表中删除可以保留地址的项目记录

        return true;
    }

    // (顶级)项目完成
    function projectComplete(bytes32 id) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid == 0x00, "project can not be subproject"); // 只允许操作顶级项目

        require(proj.sponsor == msg.sender, "msg sender must be project sponsor"); // 只有项目发起人可以完成项目

        bool flag = true;
        for (uint i = 0; i < proj.modules.length; i++) {
            if (projects[proj.modules[i]].status != 4) {   // 项目子项目状态必须全部完成
                flag = false;
                break;
            }
        }

        if (flag != true) {
            revert("failed");
        }

        proj.status = 4;

        return true;
    }

    // 购买
    function purchase(bytes32 pid, uint amount) public returns (bool) {
        require(pid != 0x00, "id can not be empty");

        Project storage proj = projects[pid];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid == 0x00, "project can not be subproject"); // 只允许购买顶级项目

        require(proj.status == 4, "project status must be 4"); // 只允许购买已完成的项目

        require(proj.sponsor != msg.sender, "msg sender can not be project sponsor"); // 项目发起者不允许购买

        require(balances[msg.sender] >= amount, "insufficient balance");  // 余额必须充足

        bytes32 id = keccak256(abi.encodePacked(pid, msg.sender, block.timestamp));   // 生成order id

        bool flag = false;
        for (uint i = 0; i < addords[msg.sender].length; i++) {   // 不允许重复购买
            Order memory order = orders[addords[msg.sender][i]];
            if (order.pid == pid && order.status > 0) {
                flag = true;
                break;
            }
        }

        if (flag == true) {
            revert("failed");
        }

        orders[id] = Order(id, pid, msg.sender, amount, 1, block.timestamp);

        proj.orders.push(id);

        addords[msg.sender].push(id);

        return true;
    }

    // 订单取消
    function orderCancel(bytes32 id) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Order storage order = orders[id];

        require(order.id != 0x00, "order id can not be empty"); // 订单必须存在

        require(order.status == 1, "order status must be 1");  //  仅已发起的订单才可以取消

        require(order.buyer == msg.sender, "msg sender must be order buyer"); // 仅订单发起者才可以取消订单

        order.status = 0;
    }

    // 订单确认
    function orderComplete(bytes32 id, bool ok) public returns (bool) {
        require(id != 0x00, "id can not be empty");

        Order storage order = orders[id];

        require(order.id != 0x00, "order id can not be empty"); // 订单必须存在

        Project memory proj = projects[order.pid];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.sponsor == msg.sender, "msg sender must be project sponsor");  // 仅项目发起者才可以确认订单

        require(proj.status == 4, "project status must be 4");  // 仅完成的项目才可以购买

        require(balances[order.buyer] >= order.amount, "insufficient balance"); // 余额不足

        bool flag = true;

        if (ok == true) {
            flag = pay(id, msg.sender, order.pid, order.amount);
        } else {
            order.status = 0;   // 支付出错, 订单废弃
        }

        return flag;
    }

    // 递归支付
    function pay(bytes32 oid, address payer, bytes32 pid, uint amount) internal returns (bool) {
        Project memory proj = projects[pid];
        if (balances[payer] < amount) {
            return false;
        }

        balances[payer] -= amount * proj.ratio / 100;
        balances[proj.sponsor] += amount * proj.ratio / 100;

        for (uint i = 0; i < proj.modules.length; i++) {
            if (pay(oid, proj.sponsor, proj.modules[i], amount * proj.ratio / 100) != true) {
                return false;
            }
        }

        return true;
    }
}