<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>用户管理</title>
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
			var user_datagrid; var i=0;var editeUserDialog;var editeUserForm;FMG.editeRow = undefined;//记录正在编辑的行号
			var utId;
			$(function() {
			
				user_datagrid = $("#user_datagrid").datagrid({
					url : '${pageContext.request.contextPath}/ydVisiter/findAllUserByFenye.do',
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
		            sortable : true,//添加点击排序   
		            checkbox : true    
		            }, 
		           {
		            title :'用户名',
		            field :'uname',
		            width : 60,
		             sortable : true 
		            },
		             {
		            title :'用户IP',
		            field :'visiteIp',
		            width : 60 ,
		            }, 
		            {
		            title :'用户类型',
		            field :'userType',
		            formatter: function(value,row,index){
				if (row.typeName){
				console.log(row);
					return row.typeName;
				} else {
				     return row.userType;
					
				}
			},  
		            width : 40,            
		            },
		            {
		            title :'注册日期',
		            field :'registerDate',
		            width : 100,  
		            },{
		            title :'累计访问服务次数',
		            field :'visitServiceTimes',
		            width : 50
		          
		            },{
		            title :'访问应用次数',
		            field :'visitAppTimes',
		            width : 50
		      
		            },{
		            title :'访问图集次数',
		            field :'visitAtlasTimes',
		            width : 50
		      
		            },{
		            title :'访问数据次数',
		            field :'visitDataTimes',
		            width : 50
		      
		            },{
		            title :'访问产品次数',
		            field :'visitProductionTimes',
		            width : 50
		      
		            },{
		            title :'累计下载图集次数',
		            field :'downloadAtlasTime',
		            width : 50
		      
		            },{
		            title :'累计搜索次数',
		            field :'seartchTimes',
		            width : 50
		             
		            }
		            ] ],
		           toolbar:[{
		            text:'添加用户',
		            iconCls : 'icon-add',
		            handler : function(){
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	user_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	
		           $('#editeUserForm').form('clear');
					  editeUserDialog = $('#editeUserDialog').dialog({
					  title:'添加用户',
					modal : true,
					buttons : [ {
						text : '添加用户',
						handler : function() {
							if($('#editeUserForm').form('validate')){
							FMG.editeRow = 0;
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydVisiter/addYduser.do',
								data : $('#editeUserForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeUserDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '添加用户:'+msg.result.uid + '成功'
										});
					
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
										user_datagrid.datagrid('load',
					 FMG.serializeObject($('#user_searchForm').form()));
					 
										} else {
										$.messager.alert('标题', "添加用户失败");
										 //取消编辑，更改内容回滚
		            user_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
									}
									
								},
								error : function(data) {
								$.messager.alert('标题',"ajax调用失败");
									 //取消编辑，更改内容回滚
		            user_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
								}
							});
							};
							 FMG.editeRow = undefined;
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editeUserForm').form('clear');
		                 FMG.editeRow = undefined;
						}
					} ]
				});
		            	
		            	
		            	}
		            }
		            },'-',{
		            text:'删除用户',
		            iconCls : 'icon-remove',
		            handler : function(){
		             var rows = user_datagrid.datagrid('getSelections');
		             console.log("rows="+rows.length);
		             if(rows.length>0){
		             $.messager.confirm('请确认','您确定要删除当前选择的项目吗?',function(b){//当用户点击确定，b=true
		             if(b){
		             var ids = [];
		             for(var i =0; i<rows.length; i++){
		             ids.push(rows[i].uid);
					}
					console.log(ids.join(','));
					$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydVisiter/delUser.do?uid='+ids.join(','),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									for (i in msg.result){
								         $.messager.show({
												title : '提示',
												msg : '删除用户:'+msg.result[i].uid + '成功'
												});	
												}
	
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
										
										user_datagrid.datagrid('load',
					 FMG.serializeObject($('#user_searchForm').form()));
										} else {
										$.messager.alert('标题', "删除用户失败");
										 //取消编辑，更改内容回滚
		            user_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题',"ajax调用失败");
									
									 //取消编辑，更改内容回滚
		            user_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
								}
							});
		             }
		             });
		             
		             }else{
		             $.messager.alert('提示','请选择要删除的记录!','error');
		             }
					
		            }
		            },'-',{
		            text:'修改用户',
		            iconCls : 'icon-edit',
		            handler : function(){
		            var rows = user_datagrid.datagrid('getSelections');
					if(rows.length >1) {
					
					$.messager.show({	
					msg : '只能选择一个用户进行编辑',
					title : '提示'
					});
					} else if(rows.length == 0){
					$.messager.show({
					msg : '请选择一个用户进行修改',
					title : '提示'
					});
					}else if(rows.length == 1){
	 				$('#editeUserForm').find('[name = id]').attr('readonly', 'readonly');
	 								$('#editeUserForm').form('clear');
	 				
					$('#editeUserForm').form('load',{
					id  : rows[0].id,
					uid : rows[0].uid,
					uname:rows[0].uname,
					UserIp : rows[0].visiteIp,
				    utId: rows[0].userType
					});
					editeUserDialog = $('#editeUserDialog').dialog({
					title: '修改用户',
					modal : true,
					buttons : [ {
						text : '修改用户',
						handler : function() {
							if($('#editeUserForm').form('validate')){
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/ydVisiter/updateUser.do?uuid='+rows[0].uid,
								data : $('#editeUserForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeUserDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '修改用户:'+msg.result.uid + '成功'
										});
										console.log(msg);
										 //取消编辑，更改内容回滚
		         
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
										user_datagrid.datagrid('load',
					 FMG.serializeObject($('#user_searchForm').form()));
										} else {
										$.messager.alert('标题', "修改用户失败");
										 //取消编辑，更改内容回滚
		            user_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题',"修改用户失败");
									 //取消编辑，更改内容回滚
		            user_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
								}
							});
							}
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editeUserForm').form('clear');
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
		            user_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            user_datagrid.datagrid('unselectAll');
		            }
		            }],
				});
			
			searchUsersByConditions = function() {
					user_datagrid.datagrid('load',
					 FMG.serializeObject($('#user_searchForm').form())
					);   
		
				};
		
				clearSearchUser = function() {
					FMG.clearForm($('#user_searchForm').form());
					user_datagrid.datagrid('load',{}
					);
				};
				
				
			});
		</script>
		</head>
		
		<body style="width: 1093px;">
		<div class="easyui-Layout" fit="true"  border="false">
		<div title="查询" region = "north" collapsed = "true" border="false" style="height:130px;overflow: hidden;">
		<form id="user_searchForm"action="" method="post">
		<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%">
		<tr><th>用户名:</th><td><input name="uname" type="text" style="width: 315px"/></td></tr>
		<tr><th>创建时间:</th><td><input name="registerDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="registerDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/></td></tr>
		<tr><th>最后登录时间:</th><td><input name="loginDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="loginDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="searchUsersByConditions()">查询</a>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="clearSearchUser()">清空</a>
		</td></tr>
		</table>
		</form>
		</div>
		<div title="用户管理" region= "center" border="false">
		<table id="user_datagrid"></table>
		
		</div>
		</div>
		<div id="editeUserDialog" 
				style="width: 300px;height: 200px;">
				<form id="editeUserForm" action="" method="post">
					<table cellpadding="10" cellspacing="10">
					<tr>
							<th style="text-align: right">用户ID:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="uid" />
							</td>
						</tr>
					<tr>
							<th style="text-align: right">用户名:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="uname" />
							</td>
						</tr>
						<tr>
							<th style="text-align: right">用户IP:</th>
							<td><input class="easyui-validatebox" required="true"  type="text" name="UserIp" />
							</td>
						</tr>
					
				<tr> 
							<th style="text-align: right">用户类型</th>
							<td><input class="easyui-combobox" required="true" name="utId" data-options=" 
							valueField: 'id',
							textField: 'text',
							url: '${pageContext.request.contextPath}/ydVisiter/getUserTypelist.do',"
							 />
							</td>
						</tr> 
					</table>
				</form>
			</div>
</body>
</html>
