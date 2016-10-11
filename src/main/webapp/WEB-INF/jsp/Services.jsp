<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>服务管理</title>
    
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
			var Services_datagrid; var i=0;var editeServicesDialog;var editeServicesForm;FMG.editeRow = undefined;//记录正在编辑的行号
			var providerId;
			$(function() {
				Services_datagrid = $("#Services_datagrid").datagrid({
					url : '${pageContext.request.contextPath}/ydService/getAllService.do',
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
		            title :'服务编号',
		            field :'sId',
		            },{
		            title :'服务名称',
		            field :'sname',
		            },{
		            title :'服务url',
		            field :'surl',
		            },
		              {
		            title :'供应者',
		            field :'provider'		            
		            },
		            {
		            title :'注册日期',
		            field :'registerDate',
		            }, {
		            title :'最后更新日期',
		            field :'updateDate'
		            },{
		            title :'累计访问次数',
		            field :'visiteTotalTime'
		            }
		            ] ],
		            toolbar:[{
		            text:'添加服务',
		            iconCls : 'icon-add',
		            handler : function(){
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	Services_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	
		           $('#editeServicesForm').form('clear');
					 
					  editeServicesDialog = $('#editeServicesDialog').dialog({
					  title:'添加服务',
					modal : true,
					buttons : [ {
						text : '添加',
						handler : function() {
							if($('#editeServicesForm').form('validate')){
							FMG.editeRow = 0;
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydService/addYdServices.do',
								data : $('#editeServicesForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeServicesDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '添加服务:'+msg.result.sname + '成功'
										});
										console.log(msg);
										Services_datagrid.datagrid('load',
					 FMG.serializeObject($('#Services_searchForm').form()));
					 
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
		                 $('#editeServicesForm').form('clear');
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
		             var rows = Services_datagrid.datagrid('getSelections');
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
								url : '${pageContext.request.contextPath}/ydService/delServices.do?id='+ids.join(','),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									for (i in msg.result){
								         $.messager.show({
												title : '提示',
												msg : '删除服务:'+msg.result[i].id + '成功'
												});	
												}
										  Services_datagrid.datagrid('unselectAll');		
										Services_datagrid.datagrid('load',
					 FMG.serializeObject($('#Services_searchForm').form()));
										} else {
										$.messager.alert('标题', "删除服务失败");
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
		            text:'更新服务',
		            iconCls : 'icon-edit',
		            handler : function(){
		            var rows = Services_datagrid.datagrid('getSelections');
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
					$('#editeServicesForm').find('[name = id]').attr('readonly', 'readonly');
	 			    $('#editeServicesForm').form('clear');
	 				
					$('#editeServicesForm').form('load',{
					sid : rows[0].sId,
					sname:rows[0].sname,
					surl: rows[0].surl,
					stId: rows[0].stId,
				    providerId:rows[0]. providerId
					});
					  editeServicesDialog = $('#editeServicesDialog').dialog({
					  title:'编辑',
					modal : true,
					buttons : [ {
						text : '修改服务',
						handler : function() {
							if($('#editeServicesForm').form('validate')){
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydService/updateServices.do?sId='+rows[0].sId,
								data : $('#editeServicesForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeServicesDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '修改用户:'+msg.result.sId + '成功'
										});
										 Services_datagrid.datagrid('unselectAll');
										Services_datagrid.datagrid('load',
					 FMG.serializeObject($('#Services_searchForm').form()));
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
		                 $('#editeServicesForm').form('clear');
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
		            Services_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Services_datagrid.datagrid('unselectAll');
		            }
		            }
		            ],
				});
			
			
			searchServicessByConditions = function() {
					Services_datagrid.datagrid('load',
					 FMG.serializeObject($('#Services_searchForm').form())
					);   
		
				};
		
				clearSearchServices = function() {
					FMG.clearForm($('#Services_searchForm').form());
					Services_datagrid.datagrid('load',{}
					);
				};
				
				
			});
		</script>
		</head>
		
		<body style="width: 1093px;">
		<div class="easyui-Layout" fit="true"  border="false">
		<div title="查询" region = "north" collapsed = "true" border="false" style="height:130px;overflow: hidden;">
		<form id="Services_searchForm"action="" method="post">
		<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%">
		<tr><th>服务地址:</th><td><input name="surl" type="text" style="width: 315px"/></td></tr>
		<tr><th>创建时间:</th><td><input name="registerDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="registerDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/></td></tr>
		<tr><th>最后更新时间:</th><td><input name="updateDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="updateDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="searchServicessByConditions()">查询</a>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="clearSearchServices()">清空</a>
		</td></tr>
		</table>
		</form>
		</div>
		<div title="服务管理" region= "center" border="false">
		<table id="Services_datagrid"></table>
		</div>
		</div>
		<div id="editeServicesDialog" 
				style="width: 300px;height: 200px;">
				<form id="editeServicesForm" action="" method="post">
					<table cellpadding="10" cellspacing="10">
					
					<tr>
							<th style="text-align: right">服务标识:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="sid" />
							</td>
						</tr>
						<tr>
							<th style="text-align: right">服务名称:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="sname" />
							</td>
						</tr>
						<tr>
							<th style="text-align: right">服务url:</th>
							<td><input class="easyui-validatebox" required="true"  type="text" name="surl" />
							</td>
						</tr>
					<tr>
							<th style="text-align: right">服务类型</th>
							<td><input class="easyui-combobox" required="true" name="stId" data-options=" 
							valueField: 'id',
							textField: 'text',
							url: '${pageContext.request.contextPath}/ydService/getServiceTypelist.do',"
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
