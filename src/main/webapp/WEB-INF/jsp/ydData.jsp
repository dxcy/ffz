<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>数据管理</title>
    
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
			var Data_datagrid; var i=0;var editeDataDialog;var editeDataForm;FMG.editeRow = undefined;//记录正在编辑的行号
			var did;var dUrl;var DT_ID;var Provider_ID;var price;
			$(function() {
			
				Data_datagrid = $("#Data_datagrid").datagrid({
					url : '${pageContext.request.contextPath}/yddata/getAllData.do',
		            title : '',
		            iconCls : 'icon-save',
		            cache : false,
		            loadMsg: "第"+ i++ +"查找中，请等待 …",
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
		           
		           //添加点击排序   
		            checkbox : true    
		            }, 
		            {
		            title :'数据标识',
		            field :'dId'
		            },{
		            title :'访问路径',
		            field :'url'
		            },
		              {
		            title :'数据类型',
		            field :'typeName'
		            },
		              {
		            title :'供应商',
		            field :'provider'		           		            
		            },{
		            title :'访问次数',
		            field :'downloadTotalTime'
		            },{
		            title :'注册日期',
		            field :'registerDate'  
		            }, {
		            title :'最后更新日期',
		            field :'updateDate'  
		            }
		            ] ],
		            toolbar:[{
		            text:'添加数据',
		            iconCls : 'icon-add',
		            handler : function(){
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	Data_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	
		           $('#editeDataForm').form('clear');
					  $('#editeDataDialog').css('display','block');
					  editeDataDialog = $('#editeDataDialog').dialog({
					  title:'添加数据',
					modal : true,
					buttons : [ {
						text : '添加',
						handler : function() {
							if($('#editeDataForm').form('validate')){
							FMG.editeRow = 0;
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/yddata/addYdData.do',
								data : $('#editeDataForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeDataDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '添加数据:'+msg.result.dId + '成功'
										});
										console.log(msg);
										Data_datagrid.datagrid('load',
					 FMG.serializeObject($('#Data_searchForm').form()));
					 
										} else {
										$.messager.alert('标题', "添加数据失败");
										Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
									}
									
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
									  //取消编辑，更改内容回滚
		            Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
								}
							});
							};
							 FMG.editeRow = undefined;
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editeDataForm').form('clear');
		                 FMG.editeRow = undefined;
						}
					} ]
				});   	  	
		            	}
		            }
		            },
		            '-',{
		            text:'删除数据',
		            iconCls : 'icon-remove',
		            handler : function(){
		             var rows = Data_datagrid.datagrid('getSelections');
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
								url : '${pageContext.request.contextPath}/yddata/delData.do?id='+ids.join(','),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									for (i in msg.result){
								         $.messager.show({
												title : '提示',
												msg : '删除数据:'+msg.result[i].dId + '成功'
												});	
												}
										  Data_datagrid.datagrid('unselectAll');		
										Data_datagrid.datagrid('load',
					 FMG.serializeObject($('#Data_searchForm').form()));
										} else {
										$.messager.alert('标题', "删除数据失败");
										Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
									  //取消编辑，更改内容回滚
		            Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
								}
							});
		             }
		             });
		             
		             }else{
		             $.messager.alert('提示','请选择要删除的记录!','error');
		             }
					
		            }
		            },'-',{
		            text:'修改数据',
		            iconCls : 'icon-edit',
		            handler : function(){
		            var rows = Data_datagrid.datagrid('getSelections');
					if(rows.length >1) {
					
					$.messager.show({	
					msg : '只能选择一个数据进行编辑',
					title : '提示'
					});
					} else if(rows.length == 0){
					$.messager.show({
					msg : '请选择一个数据进行修改',
					title : '提示'
					});
					}else if(rows.length == 1){
					$('#editeDataForm').find('[name = id]').attr('readonly', 'readonly');
	 				
	 								$('#editeDataForm').form('clear');
	 				
					$('#editeDataForm').form('load',{
					id: rows[0].id,
					did : rows[0].dId,
				    dUrl : rows[0].url,
				    price: rows[0].price,
				    Provider_ID : rows[0].provider,
					});
					  editeDataDialog = $('#editeDataDialog').dialog({
					  title: '修改',
					modal : true,
					buttons : [ {
						text : '修改数据',
						handler : function() {
							if($('#editeDataForm').form('validate')){
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/yddata/updateData.do?ddid='+rows[0].dId,
								data : $('#editeDataForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									editeDataDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '修改数据:'+msg.result.dId + '成功'
										});
										 Data_datagrid.datagrid('unselectAll');
										Data_datagrid.datagrid('load',
					 FMG.serializeObject($('#Data_searchForm').form()));
										} else {
										$.messager.alert('标题', "修改数据失败");
										Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题',"ajax获取失败");
									  //取消编辑，更改内容回滚
		            Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
								}
							});
							}
						}
					}, {
						text : '重置',
						handler : function() {
		                 $('#editeDataForm').form('clear');
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
		            Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
		            }
		            },'-',{
		            text:'保存',
		            iconCls : 'icon-save',
		            handler : function(){
		            console.log("结束编辑的行="+FMG.editeRow);
		            Data_datagrid.datagrid('endEdit' , FMG.editeRow);
		    	FMG.editeRow = undefined;
		            }}],
		            //编辑关闭后触发的事件
		           onAfterEdit : function(rowIndex, rowData, changes){//编辑行索引，编辑行数据，原先数据和新数据的键值对
		            
		            var url = '${pageContext.request.contextPath}/yddata/updateData.do?did='+rowData.dId+'&Provider_ID='+rowData.provider+'&DT_ID='+rowData.typeName+'&dUrl='+rowData.url+'&price='+rowData.price;
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
										msg : '修改数据:'+msg.result.dId + '成功'
										});
										 Data_datagrid.datagrid('endEdit' , FMG.editeRow);
										Data_datagrid.datagrid('load',
					 FMG.serializeObject($('#Data_searchForm').form()));
										} else {
										$.messager.alert('标题', msg.content,error);
										 //取消编辑，更改内容回滚
		            Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
									}
								},
								error : function(data) {
								$.messager.alert('标题', '修改服务失败','error');
						 //取消编辑，更改内容回滚
		            Data_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            Data_datagrid.datagrid('unselectAll');
								}
							});
		          					
		            },
		           
		            
		          //双击行触发的事件
		   /*       onDblClickRow : function(rowIndex, rowData){
		          
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	Data_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	console.log("开启编辑");
		            	Data_datagrid.datagrid('beginEdit',rowIndex);
		            	FMG.editeRow = rowIndex;
		            	console.log("rowIndex="+rowIndex);
		            	}
		            	
		            	
		          } ,
*/
				});
			
			
			searchDatasByConditions = function() {
					Data_datagrid.datagrid('load',
					 FMG.serializeObject($('#Data_searchForm').form())
					);   
		
				};
		
				clearSearchData = function() {
					FMG.clearForm($('#Data_searchForm').form());
					Data_datagrid.datagrid('load',{}
					);
				};
				
				
			});
		</script>
		</head>
		
		<body style="width: 1093px;">
		<div class="easyui-Layout" fit="true"  border="false">
		<div title="查询" region = "north" collapsed = "true" border="false" style="height:130px;overflow: hidden;">
		<form id="Data_searchForm"action="" method="post">
		<table class="tableForm datagrid-toolbar" style="width: 100%;height: 100%">
		<tr><th>数据标识:</th><td><input name="did" type="text" style="width: 315px"/></td></tr>
		<tr><th>供应商:</th><td><input name="Provider_ID" type="text" style="width: 315px"/></td></tr>
		<tr><th>创建时间:</th><td><input name="registerDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="registerDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/></td></tr>
		<tr><th>最后更新时间:</th><td><input name="updateDateStart" class="easyui-datetimebox" editable="false" style="width: 155px"/>至<input name="updateDateEnd" class="easyui-datetimebox" editable="false" style="width: 155px"/>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="searchDatasByConditions()">查询</a>
		<a href="javascript:void(0);"class="easyui-linkbutton" onclick="clearSearchData()">清空</a>
		</td></tr>
		</table>
		</form>
		</div>
		<div title="数据管理" region= "center" border="false">
		<table id="Data_datagrid"></table>
		</div>
		</div>
		<div id="editeDataDialog" 
				style="width: 330px;height: 350px;">
				<form id="editeDataForm" action="" method="post">
					<table cellpadding="10" cellspacing="10">
					<tr>
			<th style="text-align: right">ID:</th>
	<td><input class="easyui-validatebox" disabled="disabled"
		type="text" name="id" />
	</td>
</tr>
					<tr>
							<th style="text-align: right">数据标识:</th>
							<td><input class="easyui-validatebox" required="true" type="text" name="did" />
							</td>
						</tr>
						<tr>
							<th style="text-align: right">获取路径:</th>
							<td><input class="easyui-validatebox" required="true"  type="text" name="dUrl" />
							</td>
						</tr>
					
						<tr>
							<th style="text-align: right">数据类型</th>
							<td><input class="easyui-combobox" required="true" name="DT_ID" data-options=" 
							valueField: 'id',
							textField: 'text',
							url: '${pageContext.request.contextPath}/yddata/getDataTypelist.do',"
							 />
							</td>
						</tr>
						<tr>
							<th style="text-align: right">供应商</th>
							<td><input class="easyui-combobox" required="true" name="Provider_ID" data-options=" 
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
