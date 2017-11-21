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

function mydateformatter(date){
	var y = date.getFullYear();
	var m = date.getMonth()+1;
	var d = date.getDate();
	return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}

function mydateparser(s){
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}

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

function initDataGrid(datagridObj, dblClickFun) {
    datagridObj.datagrid({
        // url:dgurl,
        onDblClickRow:dblClickFun,
        method:"get",
        loadMsg: "数据加载中...",
        rownumbers:true,
        striped: true,
        fitColumns:true,
        singleSelect:true,
        autoRowHeight:false,
        pagination:true,
        collapsible:true,
        minimizable:false,
        maximizable:false
    }).datagrid({loadFilter: pagerFilter});

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

function doAjaxSubmit(formObj, actionurl, successcb, errorcb) {
	var options = {
					url: actionurl,
					type:'POST',
					dataType:'json',
					success: successcb,
					error: errorcb
				   };
	
	formObj.ajaxSubmit(options);
}

function doPostRequest(jsonstring, actionurl, successcb, errorcb) {
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

function getCurrentDate() {
	var dateObj = new Date();
	var strDate = dateObj.getFullYear()+"-";

	if(parseInt(dateObj.getMonth()) < 9) {
		strDate += "0"+(parseInt(dateObj.getMonth())+1)+"-";
	} else {
		strDate += dateObj.getMonth()+1+"-";
	}
	
	if(parseInt(dateObj.getDate()) < 10) {
		strDate += "0"+dateObj.getDate();
	} else {
		strDate += dateObj.getDate();
	}

	return strDate;
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

function date2string(datetime){
	var year = datetime.getFullYear();
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
	var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
	var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
	var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
	var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
	return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

function formatFloat(src, pos) {
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
}
