
//添加tab
function addTab(node) {
if ( $('#ttabs').tabs('exists' , node.text)) {
$('#ttabs').tabs('select' , node.text);//当node被选中，如果该tab已经展开，就选中它
} else { //如果未被展开就展开
	console.info(node.attributes.url);
if(node.attributes.url && node.attributes.url.length >0) {
$.messager.progress({
text : '页面加载中....',
interval : 100
});
$.parser.onComplete = function(){
		window.setTimeout(function(){
			$.messager.progress('close');
		},500);
	};
	console.log('<iframe id="iframe" src="'+node.attributes.url+'" frameborder="0" style="border:0;width:100%;height:99.4%;background:"></iframe>');
 $('#ttabs').tabs('add' , {
title : node.text,
closable : true,//是否有关闭按钮

content : '<iframe id="iframe" src="'+node.attributes.url+'" frameborder="0" style="border:0;width:100%;height:99.4%;background:"></iframe>',
fit : true,
tools : [{
     iconCls : 'icon-mini-refresh',
     handler : function(){
     refreshTab(node.text);
     }
}]
});

}else {
 $('#ttabs').tabs('add' , {
title : node.text,
closable : true,
//content : '<iframe src="${pageContext.request.contextPath}' + node.url + '" frameborder="0" style="border:0;width:100%;height:99.4%;"></iframe>',
content : '<h1>请求节点错误</h1>',
tools : [{
     iconCls : 'icon-mini-refresh',
     handler : function(){
     refreshTab(node.text);
     }
}]
});
}
}

}

//刷新方法
function refreshTab(title) {
var tab = $('#ttabs').tabs('getTab' , title);
console.info(tab);
$('#ttabs').tabs('update' , {
tab : tab,
options : tab.panel('options')
});

}
