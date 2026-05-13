$(function (){
  $("#gloUrl").attr("gloUValue","http://localhost:7002")
  // Network environment address below
  // $("#gloUrl").attr("gloUValue","http://www.young33.top:7002")
  // $.ajax({
  //   url: Glo_BaseUrl + "/selectRentEquipment",
  //   type: "get",
  //   data: {
  //     "userId":Glo_user
  //   },
  //   success: function (data) {
  //
  //   },
  //   error: function () {
  //     alert("Server error! Network error!")
  //   }
  // })
})
let  gloRequestUrl = "http://localhost:7002"
function getRole(){
  let Glo_BaseUrl = $("#gloUrl").attr("gloUValue")
  let Glo_user = localStorage.getItem("userId")
  let role = "";
  $.ajax({
    url: Glo_BaseUrl + "/sysuser/detail",
    type: "get",
    async:false,
    data: {
      "id":Glo_user
    },
    success: function (data) {
      role =  data.data.role
    },
    error: function () {
      alert("Server error! Network error!")
    }
  })
  return role;
}


function windowsAuto(code,msg){
  if (code === 200){
    swal("Success!", msg, "success");

  }else {
    swal("Failed!", msg, "warning");

  }
}

//获取到角色，然后隐藏对应的菜单和按钮
function getRoleSetMenu(){
  let role = getRole()
  if (role === "1"){//管理员
    $(".userFun ").css({display:"none"})
    $(".adminUnDo ").css({display:"none"})
    $(".workerFun ").css({display:"none"})
  }else if (role === "2"){//用户
    $(".adminFun ").css({display:"none"})
    $(".workerFun ").css({display:"none"})
    $(".userUnDo").css({display:"none"})
  }else if (role === "3"){//服务商
    $(".adminFun ").css({display:"none"})
    $(".workUnDo ").css({display:"none"})
    $(".userFun").css({display:"none"})
    // 服务商显示数据统计菜单
    $(".userUnDo").css({display:"block"})
  }
  return role
}

function handleTree(data, id, parentId, children) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  };

  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}
