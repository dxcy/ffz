<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>应用管理</title>
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
			var App_datagrid; var i=0;var editeAppDialog;var editeAppForm;FMG.editeRow = undefined;//记录正在编辑的行号
			var providerId; var AppTypeId;
			$(function() {
				App_datagrid = $("#App_datagrid").datagrid({
					url : '${pageContext.request.contextPath}/app/getAllAppsByFenye.do',
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
		            }, 
		            {
		            title :'app编号',
		            field :'aid',
		            width : 60 ,
		            
		            }, 
		            {
		            title :'名称',
		            field :'name',
		            width : 60 ,
		            
		            },{
		            title :'url',
		            field :'url',
		            width : 200, 
		            editor: {
                        type: 'validatebox',
                         options: {
                          required: true
                                   }
                            } 
		            },
		              {
		            title :'app类型',
		            field :'aType',
		            width : 100,
		               editor : {                
                            type: 'combobox',
                            options: {
                                required: true,
                                panelHeight : 'auto' ,
                                url: '${pageContext.request.contextPath}/ydVisiter/getAppTypelist.do',                       
                                valueField: 'id',
                                textField: 'text',
                                onSelect: function (record) {
                                   AppTypeId = record.id;
                                },                                
                                  
                            }
                        }		            
		            },
		              {
		            title :'供应者',
		            field :'provider',
		          
		            width : 100,
		             
		               editor : {                
                            type: 'combobox',
                            options: {
                                required: true,
                                panelHeight : 'auto' ,
                                url: '${pageContext.request.contextPath}/ydVisiter/getProviderlist.do',                       
                                valueField: 'id',
                                textField: 'text',
                                onSelect: function (record) {
                                   providerId = record.id;
                                },                                
                                  
                            }
                        }		            
		            },
		            {
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
		            text:'添加服务',
		            iconCls : 'icon-add',
		            handler : function(){
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	App_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	
		           $('#editeAppForm').form('clear');
					 
					  editeAppDialog = $('#editeAppDialog').dialog({
					  title:'添加服务',
					modal : true,
					buttons : [ {
						text : '添加',
						handler : function() {
							if($('#editeAppForm').form('validate')){
							FMG.editeRow = 0;
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/app/addApp.do',
								data : $('#editeAppForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeAppDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '添加服务:'+msg.result.aid + '成功'
										});
										console.log(msg);
										App_datagrid.datagrid('load',
					 FMG.serializeObject($('#App_searchForm').form()));
					 
										} else {
										$.messager.alert('标题', "添加服务失败");
									}
									
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
								}
							});
							};
							 FMG.editeRow = undefined;
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editeAppForm').form('clear');
		                 FMG.editeRow = undefined;
						}
					} ]
				});   	  	
		            	}
		            }
		            },'-',{
		            text:'删除应用',
		            iconCls : 'icon-remove',
		            handler : function(){
		             var rows = App_datagrid.datagrid('getSelections');
		             if(rows.length>0){
		             $.messager.confirm('请确认','您确定要删除吗?',function(b){//当用户点击确定，b=true
		             if(b){
		             
		             var ids = [];
		             for(var i =0; i<rows.length; i++){
		             ids.push(rows[i].id);
					}
					console.log(ids.join(','));
					$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/app/delApp.do?id='+ids.join(','),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									for (i in msg.result){
								         $.messager.show({
												title : '提示',
												msg : '删除成功'
												});	
												}
										  App_datagrid.datagrid('unselectAll');		
										App_datagrid.datagrid('load',
					 FMG.serializeObject($('#App_searchForm').form()));
										} else {
										$.messager.alert('标题', "删除失败");
									}
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
									
								}
							});
		             }
		             });
		             
		             }else{
		             $.messager.alert('提示','请选择要删除的记录!','error');
		             }
					
		            }
		            },'-',{
		            text:'更新应用',
		            iconCls : 'icon-edit',
		            handler : function(){
		            var rows = App_datagrid.datagrid('getSelections');
					if(rows.length >1) {
					
					$.messager.show({	
					msg : '只能选择一个应用进行编辑',
					title : '提示'
					});
					} else if(rows.length == 0){
					$.messager.show({
					msg : '请选择一个应用进行修改',
					title : '提示'
					});
					}else if(rows.length == 1){
					$('#editeAppForm').find('[name = id]').attr('readonly', 'readonly');
	 			    $('#editeAppForm').form('clear');
					$('#editeAppForm').form('load',{
					aid : rows[0].aid,
					name : rows[0].name,
					url: rows[0].url,
					typeId:rows[0].typeId,
					providerId:rows[0].providerId
					});
					  editeAppDialog = $('#editeAppDialog').dialog({
					  title:'编辑',
					modal : true,
					buttons : [ {
						text : '修改应用',
						handler : function() {
							if($('#editeAppForm').form('validate')){
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/app/updateApp.do?oldid='+rows[0].aid,
								data : $('#editeAppForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeAppDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '修改成功'
										});
										 App_datagrid.datagrid('unselectAll');
										App_datagrid.datagrid('load',
					 FMG.serializeObject($('#App_searchForm').form()));
										} else {
										$.messager.alert('标题', "修改失败");
									}
								},
								error : function(data) {
								$.messager.alert('标题',"修改失败");
									alert(data, "error");
								}
							});
							}
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editeAppForm').form('clear');
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
		            App_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            App_datagrid.datagrid('unselectAll');
		            }
		            }
		            ],
				});
			
			
			searchAppsByConditions = function() {
					App_datagrid.datagrid('load',
					 FMG.serializeObject($('#App_searchForm').form())
					);   
				};
				clearSearchApp = function() {
					FMG.clearForm($('#App_searchForm').form());
					App_datagrid.datagrid('load',{}
					);
				};
			});
		</script>
		</head>
		
		<body style="width: 1093px;">
		<div class="easyui-Layout" fit="true"  border="false">
		<div title="查询" region = "north" collapsed = "true" border="false" style="height:130px;overflow: hidden;">
		<form id="App_searchForm"action="" method="post">
		<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%">
		<tr><th>服务地址:</th><td><input name="url" type="text" style="width: 315px"/></td></tr>
		<tr><th>创建时间:</th><td><input name="registerDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="registerDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/></td></tr>
		<tr><th>最后更新时间:</th><td><input name="updateDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="updateDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="searchAppsByConditions()">查询</a>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="clearSearchApp()">清空</a>
		</td></tr>
		</table>
		</form>
		</div>
		<div title="应用管理" region= "center" border="false">
		<table id="App_datagrid"></table>
		</div>
		</div>
		<div id="editeAppDialog" 
				style="width: 300px;height: 200px;">
				<form id="editeAppForm" action="" method="post">
					<table cellpadding="10" cellspacing="10">
					
					<tr>
							<th style="text-align: right">应用id:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="aid" />
							</td>
						</tr>
						
							<tr>
							<th style="text-align: right">名称:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="name" />
							</td>
						</tr>
						<tr>
							<th style="text-align: right">url:</th>
							<td><input class="easyui-validatebox" required="true"  type="text" name="url" />
							</td>
						</tr>
					<tr>
							<th style="text-align: right">类型</th>
							<td><input class="easyui-combobox" required="true" name="typeId" data-options=" 
							valueField: 'id',
							textField: 'text',
							url: '${pageContext.request.contextPath}/app/getAppTypelist.do',"
							 />
							</td>
						</tr>
						<tr>
							<th style="text-align: right">供应商</th>
							<td><input class="easyui-combobox" required="true" name="providerId" data-options=" 
							valueField: 'id',
							textField: 'text',
							url: '${pageContext.request.contextPath}/ydVisiter/getProviderlist.do',"
							 />
							</td>
						</tr>
					</table>
				</form>
			</div>
</body>
</html>
