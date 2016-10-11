<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>页面管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/index.css">
	<link id="easyuiTheme_tab" rel="stylesheet"
	href="${pageContext.request.contextPath}/css/themes/metro-blue/easyui.css" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/icon.css" type="text/css"></link>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/Base.css" type="text/css"></link>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/anychart.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/TreeMenu.js"></script>
 <script type="text/javascript" charset="UTF-8">
			var Page_datagrid; var i=0;var editePageDialog;var editePageForm;FMG.editeRow = undefined;//记录正在编辑的行号
			var BID;
			$(function() {
			
				Page_datagrid = $("#Page_datagrid").datagrid({
					url : '${pageContext.request.contextPath}/ydPage/getAllPages.do',
		            title : '',
		            iconCls : 'icon-save',
		            cache : false,
		            loadMsg: "查找中，请等待 …",
		            pagination : true,
		            pageSize : 10,
		            pageList : [10,20,50],
		            fit : true,
		            fitColumns : true,
		            nowrap : false,
		            border : false,
		            treeField : 'text',
		            idField : 'id',
		            sortName:'',
		            sortOrder: 'desc',
		            columns :[ [ 
		             {
		            title :'编号',
		            field :'id',
		            width : 20 ,
		           //添加点击排序   
		            checkbox : true    
		            }, {
		            title :'页面标识',
		            field :'pid',
		            width : 100 ,
		           //添加点击排序   
		              
		            },{
		            title :'访问地址',
		            field :'url',
		            width : 280, 
		            editor: {
                        type: 'validatebox',
                         options: {
                          required: true
                                   }
                            } 
		            },
		              {
		            title :'所属栏目',
		            field :'bmName',
		       
		            width : 100,
		             
		               editor : {                
                            type: 'combobox',
                            options: {
                                required: true,
                                panelHeight : 'auto' ,
                                url: '${pageContext.request.contextPath}/ydPage/getModulelist.do',                       
                                valueField: 'id',
                                textField: 'text',
                                onSelect: function (record) {
                                   BID = record.id;   
                                },                                     
                            }
                        }		            
		            },{
		            title :'注册日期',
		            field :'registerDate',
		            width : 100,
		              
		              
		            }, {
		            title :'最后更新日期',
		            field :'updateDate',
		            width : 100,
		              
		              
		            },{
		            title :'累计访问次数',
		            field :'visitedTimes',
		            width : 50,
		               
		            }
		            ] ],
		            toolbar:[{
		            text:'添加页面',
		            iconCls : 'icon-add',
		            handler : function(){
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	Page_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	
		           $('#editePageForm').form('clear');
					  editePageDialog = $('#editePageDialog').dialog({
					  title:'添加页面',
					modal : true,
					buttons : [ {
						text : '添加',
						handler : function() {
							if($('#editePageForm').form('validate')){
							FMG.editeRow = 0;
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydPage/addYdPage.do',
								data : $('#editePageForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editePageDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '添加页面:'+msg.result.url + '成功'
										});
										console.log(msg);
										Page_datagrid.datagrid('load',
					 FMG.serializeObject($('#Page_searchForm').form()));
					 
										} else {
										$.messager.alert('标题', "添加页面失败");
										 Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
									}
									
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
									  //取消编辑，更改内容回滚
		            Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
								}
							});
							};
							 FMG.editeRow = undefined;
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editePageForm').form('clear');
		                 FMG.editeRow = undefined;
						}
					} ]
				});   	  	
		            	}
		            }
		            },'-',{
		            text:'删除服务',
		            iconCls : 'icon-remove',
		            handler : function(){
		             var rows = Page_datagrid.datagrid('getSelections');
		             if(rows.length>0){
		             $.messager.confirm('请确认','您确定要删除当前选择的项目吗?',function(b){//当用户点击确定，b=true
		             if(b){
		             
		             var ids = [];
		             for(var i =0; i<rows.length; i++){
		             ids.push(rows[i].id);
					}
					console.log(ids.join(','));
					$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydPage/delPage.do?id='+ids.join(','),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									for (i in msg.result){
								         $.messager.show({
												title : '提示',
												msg : '删除页面:'+msg.result[i].Url + '成功'
												});	
												}
										  Page_datagrid.datagrid('unselectAll');		
										Page_datagrid.datagrid('load',
					 FMG.serializeObject($('#Page_searchForm').form()));
										} else {
										$.messager.alert('标题', "删除页面失败");
										 Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
									  //取消编辑，更改内容回滚
		            Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
								}
							});
		             }
		             });
		             
		             }else{
		             $.messager.alert('提示','请选择要删除的记录!','error');
		             }
					
		            }
		            },'-',{
		            text:'更新页面',
		            iconCls : 'icon-edit',
		            handler : function(){
		            var rows = Page_datagrid.datagrid('getSelections');
					if(rows.length >1) {
					
					$.messager.show({	
					msg : '只能选择一个服务进行编辑',
					title : '提示'
					});
					} else if(rows.length == 0){
					$.messager.show({
					msg : '请选择一个服务进行修改',
					title : '提示'
					});
					}else if(rows.length == 1){
					
					$('#editePageForm').find('[name = id]').attr('readonly', 'readonly');
					
	 			
	 								$('#editePageForm').form('clear');
	 				
					$('#editePageForm').form('load',{
					
					pid:rows[0].pid,
				    Url:rows[0].url,
				    BID:rows[0].id
					});
					  editePageDialog = $('#editePageDialog').dialog({
					  title:'修改',
					modal : true,
					buttons : [ {
						text : '修改服务',
						handler : function() {
							if($('#editePageForm').form('validate')){
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydPage/updatePage.do?id='+rows[0].id,
								data : $('#editePageForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editePageDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '修改页面:'+msg.result.url + '成功'
										});
										 Page_datagrid.datagrid('unselectAll');
										Page_datagrid.datagrid('load',
					 FMG.serializeObject($('#Page_searchForm').form()));
										} else {
										$.messager.alert('标题', "修改失败");
										 Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
								 Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
									  //取消编辑，更改内容回滚
		            Page_datagrid.datagrid('rejectChanges');
								}
							});
							}
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editePageForm').form('clear');
						}
					} ]
				});
	
					};
					
		            }
		            },'-',{
		            text:' 取消编辑',
		            iconCls : 'icon-redo',
		            handler : function(){
		            FMG.editeRow = undefined;
		            //取消编辑，更改内容回滚
		            Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
		            }
		            },'-',{
		            text:'保存',
		            iconCls : 'icon-save',
		            handler : function(){
		            console.log("结束编辑的行="+FMG.editeRow);
		           // console.log("BID="+rows[0].BID);
		            Page_datagrid.datagrid('endEdit' , FMG.editeRow);
		    	FMG.editeRow = undefined;
		            }}],
		            //编辑关闭后触发的事件
		            onAfterEdit : function(rowIndex, rowData, changes){//编辑行索引，编辑行数据，原先数据和新数据的键值对
		            console.log("进入.....");
		            console.log(rowData);
		            var url = '${pageContext.request.contextPath}/ydPage/updatePage.do?Url='+rowData.url+'&BID='+rowData.bmName;
		            console.log(url);
		           	$.ajax({
								type : "POST",
								url : url,
								data : rowData,
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
										$.messager.show({
										title : '提示',
										msg : '修改页面:'+msg.result.url + '成功'
										});
										 Page_datagrid.datagrid('endEdit' , FMG.editeRow);
										Page_datagrid.datagrid('load',
					 FMG.serializeObject($('#Page_searchForm').form()));
										} else {
										$.messager.alert('标题', msg.content,error);
                   Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题', 'ajax获取失败','error');
						 Page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Page_datagrid.datagrid('unselectAll');
								}
							});
		          					
		            },
		            
		            
		          //双击行触发的事件
		    /*      onDblClickRow : function(rowIndex, rowData){
		          
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	Page_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	console.log("开启编辑");
		            	Page_datagrid.datagrid('beginEdit',rowIndex);
		            	FMG.editeRow = rowIndex;
		            	console.log("rowIndex="+rowIndex);
		            	}
		            	
		            	
		          } ,*/

				});
			
			
			searchPagesByConditions = function() {
					Page_datagrid.datagrid('load',
					 FMG.serializeObject($('#Page_searchForm').form())
					);   
		
				};
		
				clearSearchPage = function() {
					FMG.clearForm($('#Page_searchForm').form());
					Page_datagrid.datagrid('load',{}
					);
				};
				
				
			});
		</script>
		</head>
		
		<body style="width: 1093px;">
		<div class="easyui-Layout" fit="true"  border="false">
		<div title="查询" region = "north" collapsed = "true" border="false" style="height:130px;overflow: hidden;">
		<form id="Page_searchForm"action="" method="post">
		<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%">
		<tr><th>所属 模块:</th><td><input class="easyui-combobox" required="true" name="BID" style="width: 315px;" data-options=" 
							valueField: 'id',
							textField: 'text',
							url: '${pageContext.request.contextPath}/ydPage/getModulelist.do',"
							 /></td></tr>
		<tr><th>创建时间:</th><td><input name="registerDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="registerDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/></td></tr>
		<tr><th>最后更新时间:</th><td><input name="updateDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="updateDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="searchPagesByConditions()">查询</a>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="clearSearchPage()">清空</a>
		</td></tr>
		</table>
		</form>
		</div>
		<div title="页面管理" region= "center" border="false">
		<table id="Page_datagrid"></table>
		</div>
		</div>
			<div id="editePageDialog" 
				style="width: 300px;height: 200px;">
				<form id="editePageForm" action="" method="post">
					<table cellpadding="10" cellspacing="10">
					
<tr>
							<th style="text-align: right">页面标识:</th>
							<td><input class="easyui-validatebox" required="true"  type="text" name="pid" />
							</td>
						</tr>

						<tr>
							<th style="text-align: right">访问路径:</th>
							<td><input class="easyui-validatebox" required="true"  type="text" name="Url" />
							</td>
						</tr>
					
						<tr>
							<th style="text-align: right">所属栏目:</th>
							<td><input class="easyui-combobox" required="true" name="BID" data-options=" 
							valueField: 'id',
							textField: 'text',
							url: '${pageContext.request.contextPath}/ydPage/getModulelist.do',"
							 />
							</td>
						</tr>
					</table>
				</form>
			</div>
</body>
</html>
