pragma solidity >=0.5.0;

contract Coop {

    struct Project {
        bytes32 id;                // 项目ID
        bytes32 pid;               // 父项目ID (存在父项目表示该项目为一个项目的子项目, 不存在父项目的项目为顶级项目)
        // bytes32 rid;            // 根项目ID
        address payable sponsor;   // 发起人(如果项目非顶级项目, 则表示认定的模块认领者)
        string title;              // 标题
        string cata;               // 类别(如果项目非顶级项目,则表示其模块类别, 如开发,推广,销售等)
        string desc;               // 描述
        string attachment;         // 项目附件
        uint pratio;               // 在父项目中的分成比例(顶级项目分成比例为100), 顶级项目中表示售价
        // uint64 price;           // 价格(以太币)
        bytes32[] modules;         // 子项目列表
        address[] claimers;        // 认领者列表(顶级项目不允许认领, 只可认领顶级项目的模块)
        uint status;               // 状态(0: 已废弃; 1:已发起; 2:已认领; 3:已交付; 4:已完成)
        uint timestamp;            // 发起时间
    }

    string public version;         // 合约版本

    address public owner;          // 合约发布者

    uint pronum = 0;               // 项目总数

    mapping (uint => bytes32) public idxtoids;       // 项目索引到项目id的映射

    mapping (bytes32 => Project) public projects;   // 项目列表(包括子项目)

    mapping (address => bytes32[]) public tasks;    // 与地址相关的项目列表(包括发起的, 认领的, 认定的)

    /*@ 交易凭据日志
    //@desc:
    //@param sn: 交易流水号/日志id
    //@param from: 交易发起者地址
    //@param itemid: 交易资源id
    //@param to: 交易接收者地址
    //@param action: 交易动作/调用函数
    //@param amount: 交易金额
    //@param timestamp: 交易时间戳
    //@param details: 交易详细信息
    */
    event Receipt (bytes32 indexed sn, address indexed from, bytes32 indexed itemid, address to, string action, uint amount, uint timestamp, string details);

    constructor(string memory ver) public {
        owner = msg.sender;
        version = ver;
    }


    /*@ 权限判断
    //@id: 项目ID(用于判断项目存在性)
    //@addr: 与msg.sender比较的地址
    //@flag: 是否要求addr与msg.sender一致
    //@message: 错误消息
    */
    function confine(bytes32 id, address addr, bool flag, string memory message) internal view{
        require (id != 0x00, "project id can not be empty");
        if (flag == true) {
            require (msg.sender == addr, message);
        } else {
            require (msg.sender != addr, message);
        }
    }

    /*@ 订单号生成器
    //@id: 项目ID
    //@return 0: 订单号
    */
    function sngen(bytes32 id) internal view returns (bytes32) {
        return keccak256(abi.encodePacked(msg.sender, id, block.timestamp));
    }

    /*@ 将两个bytes32变量进行hash
    //@desc: bytes32类型pack后进行sha3
    //@param x: 变量x
    //@param y: 变量y
    //@return 0: hash值
    */
    //function concat(bytes32 x, bytes32 y) internal pure returns (bytes32) {
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
        //return keccak256(abi.encodePacked(x, y));
    //}


    /* 字符串拼接
    //@param _a: 字符串a
    //@param _b: 字符串b
    //@param _c: 字符串c
    //@param _d: 字符串d
    //@param _e: 字符串e
    //@return 0: string类型返回值
    */
    /*
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
    */


    /*@ 转换bytes32类型数据为string
    //@desc: 直接转换
    //@param data: bytes32类型数据
    //@return 0: string类型返回值
    */
    /*
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
    */

    /*@ 转换bytes32数组数据为string
    //@desc: 拼接以逗号作为分隔符
    //@param data: bytes32数组数据
    //@param idx: 使用bytes32数组的前几个元素
    //@return 0: string类型返回值
    */
    /*
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

            //if (separator != 0x00) {
            //    bytesString[urlLength++] = byte(separator);
            //}

        }
        bytes memory bytesStringTrimmed = new bytes(urlLength);
        for (uint i = 0; i < urlLength; i++) {
            bytesStringTrimmed[i] = bytesString[i];
        }
        return string(bytesStringTrimmed);
    }
    */

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

    /*@ 判断项目剩余分成比例以及指定的子项目是否存在
    //@id: 项目ID
    //@sid: 子项目ID
    //@return 0: 剩余分成比例
    //@return 1: 子项目是否存在
    */
    function remainRatio(bytes32 id, bytes32 sid) internal view returns (uint, bool) {
        uint remain = 100;
        bool existed = false;
        Project memory proj = projects[id];
        for (uint i = 0; i < proj.modules.length; i++) {
            Project memory subproj = projects[proj.modules[i]];
            if (subproj.id == sid) {
                existed = true;
            }
            if (remain <= subproj.pratio) {
                return (0, existed);
            } else {
                remain -= subproj.pratio;
            }
        }

        return (remain, existed);
    }

    /*@ 发布项目
    //@pid: 父项目ID
    //@title: 项目标题
    //@cata: 项目类别
    //@desc: 项目描述
    //@attachment: 项目附件哈希(多个附件哈希以逗号分隔)
    //@pratio: 项目分成比例(顶级项目表示售价)
    //@return 0: 是否执行成功
    */
    function publish(bytes32 pid, string memory title, string memory cata, string memory desc, string memory attachment, uint pratio) public returns (bool) {
        bytes32 id = keccak256(abi.encodePacked(msg.sender, title, block.timestamp));
        // bytes32 rid;
        address payable sponsor = address(0);

        if (pid != 0x00) {   // 发布的不是顶级项目, 表示添加子项目, 作相应判定
            Project storage pproj = projects[pid];
            // 判断父项目是否存在以及状态是否允许添加子项目
            //require(pproj.id != 0x00, "parent project id can not be empty");

            require(pproj.status == 1, "parent project status must be 1");

            //require(pproj.sponsor == msg.sender, "msg sender must be sponsor of parent project"); // 只有父项目发起人才能发布子项目
            confine(pproj.id, pproj.sponsor, true, "msg sender must be sponsor of parent project");

            uint remain;
            bool existed;
            (remain, existed) = remainRatio(pid, id);

            require(pratio <= 100, "ratio illegal"); // 分成比例合法性检查

            require(remain >= pratio, "remain ratio insufficient"); // 判断剩余分成是否满足该子项目分成比例要求

            require(existed == false, "project existed"); // 已存在的子项目不允许再发布

            // rid = pproj.rid;

            pproj.modules.push(id);  // 项目添加到父项目的子项目列表中

        } else {
            // require(ratio == 100, "the ratio of root project must be 100"); // 顶级项目的pratio表示以太币售价

            sponsor = msg.sender;

            // rid = id;
        }

        projects[id] = Project(id, pid, sponsor, title, cata, desc, attachment, pratio, new bytes32[](0), new address[](0), 1, block.timestamp);

        idxtoids[pronum] = id;
        pronum++;

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, address(0), "publish", 0, block.timestamp, "");

        return true;
    }

    /*@ 设置项目售价
    //@id: 项目ID
    //@price: 项目售价
    //@return 0: 是否执行成功
    */
    function setPrice(bytes32 id, uint price) public returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        //require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid == 0x00, "project can not be subproject"); // 顶级项目才允许修改售价

        require(proj.status != 0, "project status can not be 0");  // 项目必须是可认领状态

        //require(proj.sponsor == msg.sender, "msg sender must be project sponsor");
        confine(proj.id, proj.sponsor, true, "msg sender must be project sponsor");

        proj.pratio = price;

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, address(0), "setPrice", 0, block.timestamp, "");

        return true;
    }

    /*@ 认领项目
    //@id: 项目ID
    //@return 0: 是否执行成功
    */
    function claim(bytes32 id) public returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        //require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许认领

        require(proj.status == 1, "project status muste be 1");  // 项目必须是可认领状态

        require(projects[proj.pid].sponsor != msg.sender, "msg sender can not be parent project sponsor"); // 项目发起人不允许认领

        for (uint i = 0; i < proj.claimers.length; i++) {
            //require(msg.sender != proj.claimers[i], "msg sender has already claimed"); // 已认领过的地址不允许重复认领
            confine(proj.id, proj.claimers[i], false, "msg sender has already claimed");
        }

        proj.claimers.push(msg.sender);

        tasks[msg.sender].push(id);

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, address(0), "claim", 0, block.timestamp, "");

        return true;
    }

    /*@ 指定项目认领者
    //@id: 项目ID
    //@claimer: 项目认领者
    //@return 0: 是否执行成功
    */
    function claimSign(bytes32 id, address payable claimer) public returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        //require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许认领

        require(proj.status == 1, "project status muste be 1");  // 项目必须是可认领状态

        //require(projects[proj.pid].sponsor == msg.sender, "msg sender must be parent project sponsor"); // 只有父项目发起人才可进行子项目的认领审批
        confine(proj.id, projects[proj.pid].sponsor, true, "msg sender must be parent project sponsor");

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

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, claimer, "claimSign", 0, block.timestamp, "");

        return true;
    }

    /*@ 交付项目
    //@id: 项目ID
    //@return 0: 是否执行成功
    */
    function claimDeliver(bytes32 id) public returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        //require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许交付

        require(proj.status == 2, "project status must be 2"); //已认定的项目才允许进行交付

        //require(proj.sponsor == msg.sender, "msg sender must be project sponsor");  // 非项目发起或认领者不允许交付项目
        confine(proj.id, proj.sponsor, true, "msg sender must be project sponsor");

        for (uint i = 0; i < proj.modules.length; i++) {
            require(projects[proj.modules[i]].status == 4, "subproject status must be 4");  // 项目交付要求子项目均已完成
        }

        proj.status = 3;

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, address(0), "claimDeliver", 0, block.timestamp, "");

        return true;
    }

    /*@ 确认项目交付
    //@id: 项目ID
    //@ok: 接收or拒绝
    //@return 0: 是否执行成功
    */
    function deliverySign(bytes32 id, bool ok) public returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        //require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid != 0x00, "project must be subproject"); // 顶级项目不允许交付审核

        require(proj.status == 3, "project status must be 3"); //已交付的项目才允许进行交付审核

        //require(projects[proj.pid].sponsor == msg.sender, "msg sender must be parent project sponsor"); // 只有父项目的发起或认定者可以交付审核

        confine(proj.id, projects[proj.pid].sponsor, true, "msg sender must be parent project sponsor");

        if (ok == true) {
            proj.status = 4;
        } else {
            proj.status = 2;
        }

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, address(0), "deliverySign", 0, block.timestamp, "");

        return true;
    }

    /*@ 取消项目
    //@id: 项目ID
    //@return 0: 是否执行成功
    */
    function projectCancel(bytes32 id) public returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.status <= 2, "project status must be <=2"); // 已交付和已完成的项目不允许取消

        if (proj.sponsor == msg.sender) {   // 项目发起者或者认定者放弃项目要求项目下所有子项目均为废弃或未认领状态

            for (uint i = 0; i < proj.modules.length; i++) {
                require(projects[proj.modules[i]].status <= 1, "subproject status must be <=1");   // 项目子项目状态
            }

            if (proj.pid == 0x00) {  // 顶级项目取消时只标记项目状态为已废弃
                proj.status = 0;
            } else {
                proj.modules = new bytes32[](0);  // 非顶级项目的子项目随认领的取消而删除
                proj.status = 1;   // 重新回到发起状态
                proj.sponsor = address(0);
            }
        }

        // 从认领者列表删除
        for (uint i = 0; i < proj.claimers.length; i++) {
            if (proj.claimers[i] == msg.sender) {
                delete proj.claimers[i];
            }
        }

        // 不从地址关联的项目列表中删除可以保留地址的项目记录

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, address(0), "projectCancel", 0, block.timestamp, "");

        return true;
    }

    /*@ 确认顶级项目完成
    //@id: 项目ID
    //@return 0: 是否执行成功
    */
    function projectComplete(bytes32 id) public returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        //require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid == 0x00, "project can not be subproject"); // 只允许操作顶级项目

        require(proj.status <= 3, "project status must be <=3"); // 项目必须是未完成状态

        //require(proj.sponsor == msg.sender, "msg sender must be project sponsor"); // 只有项目发起人可以完成项目

        confine(proj.id, proj.sponsor, true, "msg sender must be project sponsor");

        for (uint i = 0; i < proj.modules.length; i++) {
            require(projects[proj.modules[i]].status == 4, "subproject status must be 4");   // 项目子项目状态必须全部完成
        }

        proj.status = 4;

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        emit Receipt(sngen(id), msg.sender, id, address(0), "projectComplete", 0, block.timestamp, "");

        return true;
    }

    /*@ 购买项目
    //@id: 项目ID
    //@return 0: 是否执行成功
    */
    function purchase(bytes32 id) public payable returns (bool) {
        //require(id != 0x00, "id can not be empty");

        Project storage proj = projects[id];

        //require(proj.id != 0x00, "project id can not be empty"); // 项目必须存在

        require(proj.pid == 0x00, "project can not be subproject"); // 只允许购买顶级项目

        require(proj.status == 4, "project status must be 4"); // 只允许购买已完成的项目

        //require(proj.sponsor != msg.sender, "msg sender can not be project sponsor"); // 项目发起者不允许购买

        confine(proj.id, proj.sponsor, false, "msg sender can not be project sponsor");

        require(msg.sender.balance >= proj.pratio, "insufficient balance");  // 余额必须充足

        //bytes32 sn = keccak256(abi.encodePacked(msg.sender, id, block.timestamp));

        bytes32 sn = sngen(id);

        require(liquidate(sn, proj.id, proj.pratio) == true, "purchase failed");

        emit Receipt(sn, msg.sender, id, proj.sponsor, "purchase", proj.pratio, block.timestamp, "");

        return true;
    }

    /*@ 购买项目后的递归支付(无需主动调用)
    //@sn: 订单号
    //@id: 项目ID
    //@pratio: 当前项目支付额
    //@return 0: 是否执行成功
    */
    function liquidate(bytes32 sn, bytes32 id, uint pratio) public payable returns (bool) {
        uint amount = pratio;

        Project memory proj = projects[id];
        if (msg.sender.balance < amount) {
            return false;
        }

        for (uint i = 0; i < proj.modules.length; i++) {
            Project memory sproj = projects[proj.modules[i]];
            uint tamount = amount * sproj.pratio / 100;
            amount -= tamount;
            if (liquidate(sn, sproj.id, tamount) != true) {
                return false;
            }
        }

        proj.sponsor.transfer(amount);

        emit Receipt(sn, msg.sender, id, proj.sponsor, "liquidate", amount, block.timestamp, "");

        return true;
    }

    /*@ 获取个人关联项目数
    //@return 0: 个人关联项目数
    */
    function getTaskNum() public view returns (uint) {
        return tasks[msg.sender].length;
    }

    /*@ 获取个人指定索引处关联项目ID
    //@return 0: 个人指定关联项目ID
    */
    function getTaskId(uint idx) public view returns (bytes32) {
        return tasks[msg.sender][idx];
    }

    /*@ 获取项目总数
    //@return 0: 项目数量
    */
    function getProjectNum() public view returns (uint) {
        return pronum;
    }

    /*@ 获取指定索引处项目ID
    //@return 0: 指定项目ID
    */
    function getProjectId(uint idx) public view returns (bytes32) {
        return idxtoids[idx];
    }

    /*@ 获取项目信息
    //@id: 项目ID
    //@return 0: 项目ID
    //@return 1: 父项目ID
    //@return 2: 项目发起者(子项目认领者)
    //@return 3: 项目标题
    //@return 4: 项目类别
    //@return 5: 项目描述
    //@return 6: 项目附件
    //@return 7: 项目售价(子项目分成比例)
    //@return 8: 项目子项目数
    //@return 9: 项目认领申请者数
    //@return 10: 项目状态
    //@return 11: 项目时间戳
    */
    function getProject(bytes32 id) public view returns (bytes32, bytes32, address, string memory, string memory, string memory, string memory, uint, uint[2] memory, uint, uint) {
        Project memory proj = projects[id];
        return (proj.id, proj.pid, proj.sponsor, proj.title, proj.cata, proj.desc, proj.attachment, proj.pratio, [proj.modules.length, proj.claimers.length], proj.status, proj.timestamp);
    }

    /*@ 获取项目子项目ID
    //@id: 项目ID
    //@idx: 子项目索引
    //@return 0: 子项目ID
    */
    function getModuleId(bytes32 id, uint idx) public view returns (bytes32) {
        Project memory proj = projects[id];
        return proj.modules[idx];
    }

    /*@ 获取项目认领申请者地址
    //@id: 项目ID
    //@idx: 认领申请者索引
    //@return 0: 认领申请者地址
    */
    function getClaimerAddr(bytes32 id, uint idx) public view returns (address) {
        Project memory proj = projects[id];
        return proj.claimers[idx];
    }
}
