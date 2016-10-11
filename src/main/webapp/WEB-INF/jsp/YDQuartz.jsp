<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>定时调度</title>
    
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
 <script type="text/javascript" charset="UTF-8">
                            var Quartz_datagrid;
                            var i = 0;
                            var QuartzDialog;
                            var EditeQuartzForm;
                            FMG.editeRow = undefined; //记录正在编辑的行号
                            $(function() {
                                Quartz_datagrid = $("#Quartz_datagrid").datagrid({
                                    url: '${pageContext.request.contextPath}/quartz/getAllQuartz.do',
                                    title: '',
                                    iconCls: 'icon-save',
                                    cache: false,
                                    loadMsg: "第" + i+++"查找中，请等待 …",
                                  pagination: true,
                                  pageSize: 10,
                                  pageList: [10, 20, 50],
                                  fit: true,
                                  fitColumns: true,
                                  nowrap: false,
                                  border: false,
                                  treeField: 'text',
                                  idField: 'id',
                                  sortName: '',
                                  sortOrder: 'desc',
                                  columns: [[{
                                        title: '编号',
                                        field: 'id',
                                        width: 20,
                                        
                                        //添加点击排序   
                                        checkbox: true
                                    },
                                    {
                                        title: '名称',
                                        field: 'name',
                                        width: 100
                                    },
                                    {
                                        title: '表达式',
                                        field: 'cronExpression',
                                        width: 100
                                    },{
                                        title: '说明',
                                        field: 'discription',
                                        width: 100
                                    },
                                    {
                                        title: '状态',
                                        field: 'state',
                                        width: 50   
                                    }]],
                                    toolbar: [{
		            text:'添加定时器',
		            iconCls : 'icon-add',
		            handler : function(){
		            	if(FMG.editeRow != undefined){//只能开启一行编辑状态
		            	Quartz_datagrid.datagrid('endEdit' , FMG.editeRow);
		            	}
		            	if(FMG.editeRow == undefined){
		            	
		           $('#EditeQuartzForm').form('clear');
					  $('#QuartzDialog').css('display','block');
					  QuartzDialog = $('#QuartzDialog').dialog({
					  title:'添加定时器',
					modal : true,
					buttons : [ {
						text : '添加',
						handler : function() {
							if($('#EditeQuartzForm').form('validate')){
							FMG.editeRow = 0;
						$.ajax({
								type : "POST",
								url : '${pageContext.request.contextPath}/quartz/addQuartz.do',
								data : $('#EditeQuartzForm').serialize(),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									QuartzDialog.dialog('close');
										$.messager.show({
										title : '提示',
										msg : '添加计时器:'+msg.result.name + '成功'
										});
										console.log(msg);
										Quartz_datagrid.datagrid('load',{});
					                    Quartz_datagrid.datagrid('unselectAll');
										} else {
										$.messager.alert('标题', "添加失败");
									}
									
								},
								error : function(data) {
														$.messager.alert('标题',"添加失败");
															alert(data, "error");
														}
													});
													};
													 FMG.editeRow = undefined;
												}
											}, {
												text : '重置',
												handler : function() {
								                 $('#EditeQuartzForm').form('clear');
								                 FMG.editeRow = undefined;
												}
											} ]
										});
								          }
								            }
								            },'-',{
		            text:'删除定时器',
		            iconCls : 'icon-remove',
		            handler : function(){
		             var rows = Quartz_datagrid.datagrid('getSelections');
		             console.log("rows="+rows.length);
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
								url : '${pageContext.request.contextPath}/quartz/deluartz.do?id='+ids.join(','),
								cache : false,
								dataType : 'json',
								success : function(msg) {
									if (msg && msg.success == true) {
									for (i in msg.result){
								         $.messager.show({
												title : '提示',
												msg : '删除定时器:'+msg.result[i].name + '成功'
												});	
												 Quartz_datagrid.datagrid('unselectAll');
												}
	
										console.log(msg);
										Quartz_datagrid.datagrid('load', {});
										} else {
										$.messager.alert('标题', "删除失败");
									}
								},
								error : function(data) {
								$.messager.alert('标题',"删除失败");
									alert(data, "error");
								}
							});
		             }
		             });
		             
		             }else{
		             $.messager.alert('提示','请选择要删除的记录!','error');
		             }
					
		            }
		            },'-',{
                                        text: '启动',
                                        iconCls: 'icon-ok',
                                        handler: function() {
                                            var rows = Quartz_datagrid.datagrid('getSelections');
                                            console.log("rows=" + rows.length);
                                            if (rows.length > 0) {
                                                $.messager.confirm('请确认', '计时器即将启动。。。',
                                                function(b) { //当用户点击确定，b=true
                                                    if (b) {
                                                        $.ajax({
                                                            type: "POST",
                                                            url: '${pageContext.request.contextPath}/quartz/runQuartz.do?id=' + rows[0].id,
                                                            cache: false,
                                                            dataType: 'json',
                                                            success: function(msg) {
                                                                if (msg && msg.success == true) {
                                                 
                                                                        $.messager.show({
                                                                            title: '提示',
                                                                            msg: '更新:' + msg.result.name + '状态为:'+msg.result.state
                                                                        });
                                                                          Quartz_datagrid.datagrid('unselectAll');
                                                                  

                                                               
                                                                  Quartz_datagrid.datagrid('load', {});
                                                                } else {
                                                                    $.messager.alert('标题', "修改状态失败");
                                                                    Quartz_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Quartz_datagrid.datagrid('unselectAll');
                                                                }
                                                            },
                                                            error: function(data) {
                                                                $.messager.alert('标题', "修改状态失败");
                                                                Quartz_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Quartz_datagrid.datagrid('unselectAll');
                                                              
                                                            }
                                                        });
                                                    }
                                                });

                                            } else {
                                                $.messager.alert('提示', '请选择记录!', 'error');
                                            }

                                        }
                                    },
                                    '-', {
                                        text: '停止',
                                        iconCls: 'icon-no',
                                        handler: function() {
                                            var rows = Quartz_datagrid.datagrid('getSelections');
                                            console.log("rows=" + rows.length);
                                            if (rows.length > 0) {
                                                $.messager.confirm('请确认', '计时器即将关闭。。。',
                                                function(b) { //当用户点击确定，b=true
                                                    if (b) {
                                                        $.ajax({
                                                            type: "POST",
                                                            url: '${pageContext.request.contextPath}/quartz/stopQuartz.do?id=' + rows[0].id,
                                                            cache: false,
                                                            dataType: 'json',
                                                            success: function(msg) {
                                                                if (msg && msg.success == true) {
                                                             
                                                                        $.messager.show({
                                                                            title: '提示',
                                                                            msg: '更新:' + msg.result.name + '状态为:'+msg.result.state
                                                                        });
                                                                   Quartz_datagrid.datagrid('unselectAll');
                                                                     Quartz_datagrid.datagrid('load', {});
                                                                } else {
                                                                    $.messager.alert('标题', "修改状态失败");
                                                                    Quartz_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Quartz_datagrid.datagrid('unselectAll');
                                                                }
                                                            },
                                                            error: function(data) {
                                                                $.messager.alert('标题', "修改状态失败");
                                                                Quartz_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Quartz_datagrid.datagrid('unselectAll');
                                                              
                                                            }
                                                        });
                                                    }
                                                });

                                            } else {
                                                $.messager.alert('提示', '请选择记录!', 'error');
                                            }

                                        }
                                    },
                                    '-',
                                     {
                                        text: ' 取消编辑',
                                        iconCls: 'icon-redo',
                                        handler: function() {
                                            FMG.editeRow = undefined;
                                            //取消编辑，更改内容回滚
                                            Quartz_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Quartz_datagrid.datagrid('unselectAll');
                                        }
                                    }]
                                });

                            });
                        </script>
  </head>
 
 <body style="width: 1093px;">
	<div class="easyui-Layout" fit="true" border="false">
<!-- 		<div title="查询" region="north" collapsed="true" border="false"split="true" -->
<!-- 			style="height:100px;overflow: hidden;"> -->
<!-- <form id="Quartz_searchForm" action="" method="post"> -->
<!-- 	<table class="tableForm datagrid-toolbar" -->
<!-- 		style="width: 100%;height: 100%"> -->
<!-- <tr><th>名称:</th><td><input name="name" type="text" style="width: 315px"/></td></tr> -->
<!-- <tr> -->
<!-- 	<th>更新时间:</th> -->
<!-- 	<td><input class="easyui-datetimebox" required="true" editable="false" name="updateDateStart" /> - <input class="easyui-datetimebox" required="true" editable="false" name="updateDateEnd" /> -->
<!--  <a href="javascript:void(0);" -->
<!-- class="easyui-linkbutton" onclick="searchQuartzsByConditions()"> -->
<!-- 	查询 </a> <a href="javascript:void(0);" class="easyui-linkbutton" -->
<!-- onclick="clearSearchQuartz()"> 清空 </a></td> -->
<!-- 			</tr> -->
<!-- 		</table> -->
<!-- 	</form> -->
<!-- </div>查询 -->




<div title="服务管理" region="center" border="false">
	<table id="Quartz_datagrid">
	</table>
</div><div id="p" style="width:400px;"></div>
<div id="QuartzDialog"
	style="width: 300px;height: 200px;display: none;text-align: center;
	">
<form id="EditeQuartzForm" action="" method="post">
	<table cellpadding="10" cellspacing="10">

<tr>
	<th style="text-align: right">名称</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="name" />
	</td>
</tr>	
<tr>
	<th style="text-align: right">表达式:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="cronExpression" />
	</td>
</tr>
<tr>
	<th style="text-align: right">描述:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="discription" />
	</td>
</tr>	
			</table>
		</form>
	</div>
</div><!-- layout -->
<div id="p" class="easyui-progressbar" data-options="value:0" style="width:400px;"></div>
</body>
</html>
