<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>日志管理</title>   
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
                            var Logs_datagrid;
                            var i = 0;
                            var LogsDialog;
                            var EditeLogsForm;
                            FMG.editeRow = undefined; //记录正在编辑的行号
                            $(function() {
                                Logs_datagrid = $("#Logs_datagrid").datagrid({
                                    url: '${pageContext.request.contextPath}/logsServices/getLogs.do',
                                    title: '',
                                    iconCls: 'icon-save',
                                    cache: false,
                                    loadMsg: "查找中，请等待 …",
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
                                        title: '文件名',
                                        field: 'filename',
                                        width: 100,
                                        
                                   
                                    },
                                    {
                                        title: '大小',
                                        field: 'size',
                                        width: 100,
                                        
                                   
                                    },
                                    {
                                        title: '最后更新时间',
                                        field: 'updateDate',
                                        width: 100,
                                        
                                        
                                    }]],
                                    toolbar: [ {
                                        text: '删除日志',
                                        iconCls: 'icon-remove',
                                        handler: function() {
                                            var rows = Logs_datagrid.datagrid('getSelections');
                                            console.log("rows=" + rows.length);
                                            if (rows.length > 0) {
                                                $.messager.confirm('请确认', '您确定要删除当前选择的日志吗?',
                                                function(b) { //当用户点击确定，b=true
                                                    if (b) {
                                                        var ids = [];
                                                        for (var i = 0; i < rows.length; i++) {
                                                            ids.push(rows[i].filename);
                                                        }
                                                       
                                                        $.ajax({
                                                            type: "POST",
                                                            url: '${pageContext.request.contextPath}/logsServices/delLogs.do?filename=' + ids.join(','),
                                                            cache: false,
                                                            dataType: 'json',
                                                            success: function(msg) {
                                                                if (msg && msg.success == true) {
                                                                    for (i in msg.result) {
                                                                        $.messager.show({
                                                                            title: '提示',
                                                                            msg: '删除:' + msg.result[i].filename + '成功'
                                                                        });
                                                                    }

                                                                 
                                                                     //取消被选择的号
                                                                    Logs_datagrid.datagrid('unselectAll');
                                                                    Logs_datagrid.datagrid('load', FMG.serializeObject($('#Logs_searchForm').form()));
                                                                    
                                                                } else {
                                                                    $.messager.alert('标题', "删除失败");
                                                                }
                                                            },
                                                            error: function(data) {
                                                                $.messager.alert('标题', "ajax获取失败");
                                                              
                                                            }
                                                        });
                                                    }
                                                });

                                            } else {
                                                $.messager.alert('提示', '请选择要删除的日志!', 'error');
                                            }

                                        }
                                    },
                                    '-', {
                                        text: '查看日志',
                                        iconCls: 'icon-edit',
                                        handler: function() {
                                            var rows = Logs_datagrid.datagrid('getSelections');
                                            if (rows.length > 1) {
                                                var names = [];
                                                for (var i = 0; i < rows.length; i++) {
                                                    names.pash(rows[i].name);
                                                }
                                                $.messager.show({
                                                    msg: '只能选择一篇日志进行查看,您已经选了'+ rows.length + '篇',
                                                    title: '提示'
                                                });
                                            } else if (rows.length == 0) {
                                                $.messager.show({
                                                    msg: '请选择一个日志进行查看',
                                                    title: '提示'
                                                });                                                                                
                                            } else if (rows.length == 1) {
/*									                          $('#LogsDialog').css('display','block');
									                            				                          
														  LogsDialog = $('#LogsDialog').dialog({
														  title:'查看日志',
														    width: 900,    
                                                            height: 500,  
														  closed: false,    
                                                          cache: false, 
														modal : true,
														resizable: true
														});
                                            $.ajax({
									                type: "post",
									                url: '${pageContext.request.contextPath}/logsServices/showLogs.do?filename=' + rows[0].filename,
									                contentType: "application/json; charset=utf-8",
									                dataType: "html",
									                success: function (xml) {
									                console.log(xml);
									                
									                $("#logText").empty();
									               
									                 $("#logText").text(xml);   
									            //取消编辑，更改内容回滚
                                            Logs_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Logs_datagrid.datagrid('unselectAll');
									                },
									                error: function (err) {
									                    alert("error"+err);
									                    return false;
									                }
									            }
                                                   );//ajax 
                                                   */  
                                              window.location.href="${pageContext.request.contextPath}/logs/"+rows[0].filename;                                                                         
                                            };
                                        }
                                    },
                                    '-',
                                     {
                                        text: ' 取消编辑',
                                        iconCls: 'icon-redo',
                                        handler: function() {
                                            FMG.editeRow = undefined;
                                            //取消编辑，更改内容回滚
                                            Logs_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Logs_datagrid.datagrid('unselectAll');
                                        }
                                    },
                                    '-', {
                                        text: '导出',
                                        iconCls: 'icon-save',
                                        handler: function() {
                                              var rows = Logs_datagrid.datagrid('getSelections');
                                            if (rows.length > 0) {
                                               
                                                        var ids = [];
                                                        for (var i = 0; i < rows.length; i++) {
                                                            ids.push(rows[i].filename);
                                                        }
                                                        window.location.href="${pageContext.request.contextPath}/logsServices/exportLogs.do?filename=" + ids.join(',');
                                                          Logs_datagrid.datagrid('unselectAll');
                                                                    Logs_datagrid.datagrid('load', FMG.serializeObject($('#Logs_searchForm').form())); 
                                            } else {
                                                $.messager.alert('提示', '请选择要导出的日志!', 'error');
                                            }
                                        }
                                    }],
                              
                               
                                });


                                searchLogssByConditions = function() {
                                    Logs_datagrid.datagrid('load', FMG.serializeObject($('#Logs_searchForm').form()));
                                   

                                };

                                clearSearchLogs = function() {
                                    FMG.clearForm($('#Logs_searchForm').form());
                                    Logs_datagrid.datagrid('load', {});
                                };

                            });
                        </script>
  </head>
 
 <body style="width: 1093px;">
	<div class="easyui-Layout" fit="true" border="false">
		<div title="查询" region="north" collapsed="true" border="false"split="true"
			style="height:100px;overflow: hidden;">
<form id="Logs_searchForm" action="" method="post">
	<table class="tableForm datagrid-toolbar"
		style="width: 100%;height: 100%">

<tr>
	<th>生成时间:</th>
	<td><input class="easyui-datetimebox" required="true" editable="false" name="updateDateStart" /> - <input class="easyui-datetimebox" required="true" editable="false" name="updateDateEnd" />
 <a href="javascript:void(0);"
class="easyui-linkbutton" onclick="searchLogssByConditions()">
	查询 </a> <a href="javascript:void(0);" class="easyui-linkbutton"
onclick="clearSearchLogs()"> 清空 </a></td>
			</tr>
		</table>
	</form>
</div><!-- 查询 -->















<div title="日志管理" region="center" border="false">
	<table id="Logs_datagrid">
	
	</table>
</div>
</div><!-- layout -->
<div id="LogsDialog"
	style="text-align: left;">
<pre id="logText"  style="width:800px;height:500px;text-align: left;"></pre>
	</div>
</body>
</html>
