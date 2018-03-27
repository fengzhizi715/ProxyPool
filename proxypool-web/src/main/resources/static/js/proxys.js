//自定义：时间戳的处理
layui.laytpl.toDateString = function(d, format){
  var date = new Date(d || new Date())
  ,ymd = [
    this.digit(date.getFullYear(), 4)
    ,this.digit(date.getMonth() + 1)
    ,this.digit(date.getDate())
  ]
  ,hms = [
    this.digit(date.getHours())
    ,this.digit(date.getMinutes())
    ,this.digit(date.getSeconds())
  ];

  format = format || 'yyyy-MM-dd HH:mm:ss';

  return format.replace(/yyyy/g, ymd[0])
  .replace(/MM/g, ymd[1])
  .replace(/dd/g, ymd[2])
  .replace(/HH/g, hms[0])
  .replace(/mm/g, hms[1])
  .replace(/ss/g, hms[2]);
};

//自定义：数字前置补零
layui.laytpl.digit = function(num, length, end){
  var str = '';
  num = String(num);
  length = length || 2;
  for(var i = num.length; i < length; i++){
    str += '0';
  }
  return num < Math.pow(10, length) ? str + (num|0) : num;
};

$(function(){
    var ipType = $('#ipType').val() == '' ? 'all':$('#ipType').val();
    var ipAddress = $('#ipAddress').val() == '' ? 'all':$('#ipAddress').val();
    var minPort = $('#minPort').val() == '' ? 0:$('#minPort').val();
    var maxPort = $('#maxPort').val() == '' ? 65535:$('#maxPort').val();
    if(ipAddress === ".") ipAddress = "all";
    var apiPath = 'proxyController/proxys/'+ipType+'/'+ipAddress+'/'+minPort+'/'+maxPort;
    layui.table.render({
        id: 'resultTable',
        elem: '#resultTable',
        url: apiPath,
        page: true,
        cols: [[
            {type:'numbers'},
            {field:'proxyType', title: 'IP类型', sort: true, align:'center'},
            {field:'proxyAddress', title: 'IP地址', sort: true, align:'center'},
            {field:'proxyPort', title: '端口', sort: true, align:'center'},
            {field:'lastSuccessfulTime', title: '最新验证时间', sort: true, align:'center', templet: '<div>{{ layui.laytpl.toDateString(d.lastSuccessfulTime) }}</div>'},
            {toolbar: '#barTpl', title: '操作', align:'center'}
        ]]
    });

    //绑定查询按钮
    $("#queryBtn").click(function() {
        doSearch();
    });

   //绑定工具条
    layui.table.on('tool(resultTable)', function(obj){
        var data = obj.data;
        var layEvent = obj.event;

        if(layEvent === 'verify'){ //验证
		    checkProxy(data);
        }
    });
});

function doSearch() {
    var ipType = $('#ipType').val() == '' ? 'all':$('#ipType').val();
    var ipAddress = $('#ipAddress').val() == '' ? 'all':$('#ipAddress').val();
    var minPort = $('#minPort').val() == '' ? 0:$('#minPort').val();
    var maxPort = $('#maxPort').val() == '' ? 65535:$('#maxPort').val();
    if(ipAddress === ".") ipAddress = "all";
    var apiPath = 'proxyController/proxys/'+ipType+'/'+ipAddress+'/'+minPort+'/'+maxPort;

	layui.table.reload('resultTable', {
	                                        url: apiPath
	                                    }
	                   );
}

function checkProxy(rowData) {
     var proxyInfo = rowData.proxyType+"://"+rowData.proxyAddress+":"+rowData.proxyPort;
     axios.get("proxyController/checkproxy/"+rowData.proxyType+"/"+rowData.proxyAddress+"/"+rowData.proxyPort)
                .then(function(response){
                    var result = response.data;
                    if(result.code == 200) {
                        layer.alert('验证成功  '+proxyInfo, {
                          skin: 'layui-layer-molv'
                          ,anim: 1
                        });
                    }else{
                      layer.alert('验证失败  '+proxyInfo, {
                        skin: 'layui-layer-lan'
                        ,anim: 2
                      });
                    }
                })
                .catch(function(err){
                    layer.msg("验证异常", {icon: 2});
                });
}