<!--授权记录页面-->
<template>
    <div>
        <Table border :columns="columns" :data="data"></Table>
        <Page :total="totalPage" show-elevator prev-text="上一页" next-text="下一页" @on-change="changePage" @on-page-size-change="pageSize"></Page>
        <Modal v-model="modal" fullscreen footer-hide title="授权记录">
          <div id="sample">
            <div id="myDiagramDiv" style="background-color: white; border: solid 1px black; width: 100%; height: 500px"></div>
          </div>
        </Modal>
        <Modal v-model="modalDetails" title="详情" >
          <div class="modalDetails">
            <p>{{this.modalMessage.key}}</p>
          </div>
        </Modal>
    </div>
</template>
<script>
export default {
  data() {
    return {
      modal: false,
      modalDetails: false,
      modalMessage: "",
      totalPage:"",
      columns: [
        {
          title: "资源名称",
          key: "title"
        },
        {
          title: "授权账户",
          key: "sgerid"
        },
        {
          title: "被授权账户",
          key: "userId"
        },
        {
          title: "授权类型",
          key: "ptype"
        },
        {
          title: "授权时间",
          key: "ptimestamp"
        },
        {
          title: "Action",
          key: "action",
          width: 100,
          align: "center",
          render: (h, params) => {
            return h("div", [
              h(
                "Button",
                {
                  props: {
                    type: "primary",
                    size: "small"
                  },
                  style: {
                    marginRight: "5px"
                  },
                  on: {
                    click: () => {
                      this.show(params.index);
                    }
                  }
                },
                "详情"
              )
            ]);
          }
        }
      ],
      data: []
    }
  },
  methods: {
    show(index) {
      this.modal = true;
      this.init();
    },
    // 获取资源列表
    getItemList() {
      var that = this
      var param = new URLSearchParams();
      param.append("itemId", '');
      param.append("mid", -1);
      param.append("curPage", 1);
      param.append("goPage", -1);
      param.append("pageSize", 10);
      this.$axios
        .post(this.imgsrc + "/rock/perm/getPermsList.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            that.totalPage = res.data.totalPage
            that.data = res.data.permList
          } else if (res.data.code === -1) {
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败!" + error);
        });
    },
    // 获取资源详情+分页
    getItemDetails(itemId,mid,curPage,goPage) {
      var that = this
      var param = new URLSearchParams();
      param.append("itemId",itemId);
      param.append("mid", mid);
      param.append("curPage", curPage);
      param.append("goPage", goPage);
      param.append("pageSize", 10);
      this.$axios
        .post(this.imgsrc + "/rock/perm/getPermsList.action", param, {
          xhrFields: {
            withCredentials: true
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            that.totalPage = res.data.totalPage
            that.data = res.data.permList
          } else if (res.data.code === -1) {
            that.$Message.error("获取数据失败!" + res.data.msg);
          }
        })
        .catch(function(error) {
          that.$Message.error("获取数据失败!" + error);
        });
    },
    changePage(event){

    },
    pageSize(event){

    },
    init() {
      var that = this;
      if (window.goSamples) goSamples(); // init for these samples -- you don't need to call this
      var $ = go.GraphObject.make; // for conciseness in defining templates in this function

      var myDiagram = $(
        go.Diagram,
        "myDiagramDiv", // must be the ID or reference to div
        { initialContentAlignment: go.Spot.Center }
      );

      // define all of the gradient brushes
      var graygrad = $(go.Brush, "Linear", { 0: "#F5F5F5", 1: "#F1F1F1" });
      var bluegrad = $(go.Brush, "Linear", { 0: "#CDDAF0", 1: "#91ADDD" });
      var yellowgrad = $(go.Brush, "Linear", { 0: "#FEC901", 1: "#FEA200" });
      var lavgrad = $(go.Brush, "Linear", { 0: "#EF9EFA", 1: "#A570AD" });

      // define the Node template for non-terminal nodes
      myDiagram.nodeTemplate = $(
        go.Node,
        "Auto",
        { isShadowed: true },
        // define the node's outer shape
        $(
          go.Shape,
          "RoundedRectangle",
          { fill: graygrad, stroke: "#D8D8D8" },
          new go.Binding("fill", "color")
        ),
        // define the node's text
        $(
          go.TextBlock,
          { margin: 5, font: "bold 11px Helvetica, bold Arial, sans-serif" },
          new go.Binding("text", "key")
        )
      );

      // define the Link template
      myDiagram.linkTemplate = $(
        go.Link, // the whole link panel
        { selectable: false },
        $(go.Shape)
      ); // the link shape

      // create the model for the double tree
      myDiagram.model = new go.TreeModel([
        // these node data are indented but not nested according to the depth in the tree
        { key: "Root", color: lavgrad },
        { key: "Left1", parent: "Root", dir: "left", color: bluegrad },
        { key: "leaf1", parent: "Left1" },
        { key: "leaf2", parent: "Left1" },
        { key: "Left2", parent: "Left1", color: bluegrad },
        { key: "leaf3", parent: "Left2" },
        { key: "leaf4", parent: "Left2" },
        { key: "Right1", parent: "Root", dir: "right", color: yellowgrad },
        { key: "Right2", parent: "Right1", color: yellowgrad },
        { key: "leaf5", parent: "Right2" },
        { key: "leaf6", parent: "Right2" },
        { key: "leaf7", parent: "Right2" },
        { key: "leaf8", parent: "Right1" },
        { key: "leaf9", parent: "Right1" }
      ]);
      //节点点击事件
      myDiagram.addDiagramListener("ObjectSingleClicked", function(e) {
        that.modalMessage = e.subject.part.data;
        that.modalDetails = true;
      });
      this.doubleTreeLayout(myDiagram);
    },
    doubleTreeLayout(diagram) {
      // Within this function override the definition of '$' from jQuery:
      var $ = go.GraphObject.make; // for conciseness in defining templates
      diagram.startTransaction("Double Tree Layout");

      // split the nodes and links into two Sets, depending on direction
      var leftParts = new go.Set(go.Part);
      var rightParts = new go.Set(go.Part);
      this.separatePartsByLayout(diagram, leftParts, rightParts);
      // but the ROOT node will be in both collections

      // create and perform two TreeLayouts, one in each direction,
      // without moving the ROOT node, on the different subsets of nodes and links
      var layout1 = $(go.TreeLayout, {
        angle: 180,
        arrangement: go.TreeLayout.ArrangementFixedRoots,
        setsPortSpot: false
      });

      var layout2 = $(go.TreeLayout, {
        angle: 0,
        arrangement: go.TreeLayout.ArrangementFixedRoots,
        setsPortSpot: false
      });

      layout1.doLayout(leftParts);
      layout2.doLayout(rightParts);

      diagram.commitTransaction("Double Tree Layout");
    },
    separatePartsByLayout(diagram, leftParts, rightParts) {
      var root = diagram.findNodeForKey("Root");
      if (root === null) return;
      // the ROOT node is shared by both subtrees!
      leftParts.add(root);
      rightParts.add(root);
      // look at all of the immediate children of the ROOT node
      root.findTreeChildrenNodes().each(function(child) {
        // in what direction is this child growing?
        var dir = child.data.dir;
        var coll = dir === "left" ? leftParts : rightParts;
        // add the whole subtree starting with this child node
        coll.addAll(child.findTreeParts());
        // and also add the link from the ROOT node to this child node
        coll.add(child.findTreeParentLink());
      });
    }
  }
};
</script>
<style>
.ivu-page {
  text-align: center;
  margin: 15px 0;
}
#tree {
  width: 1200px;
  height: 400px;
  margin: 0 auto;
}
</style>
