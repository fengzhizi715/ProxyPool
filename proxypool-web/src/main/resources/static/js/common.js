//基于jquery的方法，用于从form表单获取json格式的对象
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$.ajaxSetup({
    cache: false
});

function info_alert(msg,callback) {
    $.messager.alert('提示',msg,'info',callback);
}

function warn_alert(msg,callback) {
    $.messager.alert('警告',msg,'warning',callback);
}

function error_alert(msg,callback) {
    $.messager.alert('错误',msg,'error',callback);
}

function confirm(title, msg, callback) {
    $.messager.confirm(title, msg, callback);
}

function initDataGrid(datagridObj, dgurl, dblClickFun) {
    datagridObj.datagrid({
        url:dgurl,
        onDblClickRow:dblClickFun,
        method:"get",
        loadMsg: "数据加载中,请稍等...",
        rowNumbers:true,
        striped: true,
        fitColumns:true,
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        collapsible:true,
        minimizable:false,
        maximizable:false
    });

    //设置分页控件
    var p = datagridObj.datagrid('getPager');
    $(p).pagination({
        pageNumber: 1,
        pageSize: 15,
        pageList: [15,30,45],
        beforePageText: '第',
        afterPageText: '页    共 {pages} 页',
        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
    });

    datagridObj.datagrid('options').pageNumber = 1;  //默认从第一个开始

    return datagridObj;
}

function initDataGrid_1(datagridObj, dgurl, toolbar, dblClickFun) {
    datagridObj.datagrid({
        url:dgurl,
        toolbar:toolbar,
        onDblClickRow:dblClickFun,
        method:"get",
        loadMsg: "数据加载中，请稍等...",
        rowNumbers:true,
        striped: true,
        fitColumns:true,
        singleSelect:true,
        autoRowHeight:false,
        pagination:false,
        collapsible:true,
        minimizable:false,
        maximizable:false
    });

    return datagridObj;
}

function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){// is array
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if (!data.originalRows){
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
}

function doPostRequest(actionurl, jsonstring, successcb, errorcb) {
    $.ajax({
        type : 'POST',
        cache: false,
        url : actionurl,
        data: jsonstring,
        contentType : 'application/json',
        //contentType : 'application/x-www-form-urlencoded',
        dataType : 'json',
        success: successcb,
        error: errorcb
    });
}

function doGetRequest(actionurl, successcb, errorcb) {
    $.ajax({
        type : 'GET',
        cache: false,
        url : actionurl,
        dataType : 'json',
        success: successcb,
        error: errorcb
    });
}

function fillSelectComp(selectObj, height, actionUrl, textName, valueName, loadSuccessCallback) {
    selectObj.combobox({
        url:actionUrl,
        method:'get',
        textField:textName,
        valueField:valueName,
        panelHeight:height,
        onLoadSuccess:loadSuccessCallback
    });
}

function timestamp2string(time){
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}