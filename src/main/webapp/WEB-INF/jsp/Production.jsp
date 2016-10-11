<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>产品管理</title>
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
			var Production_datagrid; var i=0;var editeProductionDialog;var editeProductionForm;FMG.editeRow = undefined;//记录正在编辑的行号
			var providerId; var ProductionTypeId;
			$(function() {
				Production_datagrid = $("#Production_datagrid").datagrid({
					url : '${pageContext.request.contextPath}/production/getAllProductionsByFenye.do',
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
		            title :'产品编号',
		            field :'proid',
		            width : 60 ,
		            
		            }, 
		            {
		            title :'名称',
		            field :'name',
		            width : 60 ,
		            
		            },{
		            title :'url',
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
		            title :'产品类型',
		            field :'pType',
		            width : 100,
		               editor : {                
                            type: 'combobox',
                            options: {
                                required: true,
                                panelHeight : 'auto' ,
                                url: '${pageContext.request.contextPath}/production/getProductionTypelist.do',                       
                                valueField: 'id',
                                textField: 'text',
                                onSelect: function (record) {
                                   ProductionTypeId = record.id;
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
		            	Production_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	
		           $('#editeProductionForm').form('clear');
					 
					  editeProductionDialog = $('#editeProductionDialog').dialog({
					  title:'添加产品',
					modal : true,
					buttons : [ {
						text : '添加',
						handler : function() {
							if($('#editeProductionForm').form('validate')){
							FMG.editeRow = 0;
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/production/addProduction.do',
								data : $('#editeProductionForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeProductionDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '添加产品成功'
										});
										console.log(msg);
										Production_datagrid.datagrid('load',
					 FMG.serializeObject($('#Production_searchForm').form()));
					 
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
		                 $('#editeProductionForm').form('clear');
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
		             var rows = Production_datagrid.datagrid('getSelections');
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
								url : '${pageContext.request.contextPath}/production/delProduction.do?id='+ids.join(','),
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
										  Production_datagrid.datagrid('unselectAll');		
										Production_datagrid.datagrid('load',
					 FMG.serializeObject($('#Production_searchForm').form()));
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
		            var rows = Production_datagrid.datagrid('getSelections');
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
					$('#editeProductionForm').find('[name = id]').attr('readonly', 'readonly');
	 			    $('#editeProductionForm').form('clear');
					$('#editeProductionForm').form('load',{
					proid : rows[0].proid,
					name : rows[0].name,
					url: rows[0].url,
					typeId:rows[0].typeId,
					providerId:rows[0].providerId
					});
					  editeProductionDialog = $('#editeProductionDialog').dialog({
					  title:'编辑',
					modal : true,
					buttons : [ {
						text : '修改应用',
						handler : function() {
							if($('#editeProductionForm').form('validate')){
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/production/updateProduction.do?oldid='+rows[0].proid,
								data : $('#editeProductionForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeProductionDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '修改成功'
										});
										 Production_datagrid.datagrid('unselectAll');
										Production_datagrid.datagrid('load',
					 FMG.serializeObject($('#Production_searchForm').form()));
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
		                 $('#editeProductionForm').form('clear');
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
		            Production_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Production_datagrid.datagrid('unselectAll');
		            }
		            }
		            ],
				});
			
			
			searchProductionsByConditions = function() {
					Production_datagrid.datagrid('load',
					 FMG.serializeObject($('#Production_searchForm').form())
					);   
				};
				clearSearchProduction = function() {
					FMG.clearForm($('#Production_searchForm').form());
					Production_datagrid.datagrid('load',{}
					);
				};
			});
		</script>
		</head>
		
		<body style="width: 1093px;">
		<div class="easyui-Layout" fit="true"  border="false">
		<div title="查询" region = "north" collapsed = "true" border="false" style="height:130px;overflow: hidden;">
		<form id="Production_searchForm"action="" method="post">
		<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%">
		<tr><th>服务地址:</th><td><input name="url" type="text" style="width: 315px"/></td></tr>
		<tr><th>创建时间:</th><td><input name="registerDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="registerDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/></td></tr>
		<tr><th>最后更新时间:</th><td><input name="updateDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="updateDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="searchProductionsByConditions()">查询</a>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="clearSearchProduction()">清空</a>
		</td></tr>
		</table>
		</form>
		</div>
		<div title="产品管理" region= "center" border="false">
		<table id="Production_datagrid"></table>
		</div>
		</div>
		<div id="editeProductionDialog" 
				style="width: 300px;height: 200px;">
				<form id="editeProductionForm" action="" method="post">
					<table cellpadding="10" cellspacing="10">
					
					<tr>
							<th style="text-align: right">产品id:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="proid" />
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
							url: '${pageContext.request.contextPath}/production/getProductionTypelist.do',"
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
