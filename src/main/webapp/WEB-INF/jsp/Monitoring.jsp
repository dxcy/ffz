<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>服务地址监控管理</title>
    
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
                            var Monitoring_datagrid;
                            var i = 0;
                            var MonitoringDialog;
                            var EditeMonitoringForm;
                            FMG.editeRow = undefined; //记录正在编辑的行号
                            $(function() {

                                Monitoring_datagrid = $("#Monitoring_datagrid").datagrid({
                                    url: '${pageContext.request.contextPath}/monitoring/getAllMonitoring.do',
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
                                        title: '服务名称',
                                        field: 'name',
                                        width: 100,
                                       
                                        editor: {
                                            type: 'validatebox',
                                            options: {
                                                required: true,
                                                validType: 'chinese'
                                            }
                                        }
                                    },{
                                        title: '访问地址',
                                        field: 'url',
                                        width: 400,
                                       
                                        editor: {
                                            type: 'validatebox',
                                            options: {
                                                required: true,
                                                 
                                            }
                                        }
                                    },
                                    {
                                        title: '注册时间',
                                        field: 'registerDate',
                                        width: 100,
                                       
                                      
                                    },
                                    {
                                        title: '最后更新时间',
                                        field: 'updateDate',
                                        width: 100,
                                       
                                        
                                    }]],
                                    toolbar: [{
                                        text: '添加监控地址',
                                        iconCls: 'icon-add',
                                        handler: function() {
                                            if (FMG.editeRow != undefined) { //只能开启一行编辑状态
                                                Monitoring_datagrid.datagrid('endEdit', FMG.editeRow);
                                            }
                                            if (FMG.editeRow == undefined) {

                                                $('#EditeMonitoringForm').form('clear');
                                                 $('#MonitoringDialog').css('display','block');
                                                MonitoringDialog = $('#MonitoringDialog').dialog({
                                                title:'添加',
                                                    modal: true,
                                                    buttons: [{
                                                        text: '添加监控地址',
                                                        handler: function() {
                                                        
                                                            if ($('#EditeMonitoringForm').form('validate')) {
                                                                FMG.editeRow = 0;
                                                                $.ajax({
                                                                    type: "POST",
                                                                    url: '${pageContext.request.contextPath}/monitoring/addMonitoring.do',
                                                                    data: $('#EditeMonitoringForm').serialize(),
                                                                    cache: false,
                                                                    dataType: 'json',
                                                                    success: function(msg) {
                                                                        if (msg && msg.success == true) {
                                                                            MonitoringDialog.dialog('close');
                                                                            $.messager.show({
                                                                                title: '提示',
                                                                                msg: '添加:' + msg.result.name + '成功'
                                                                            });
                                                                               Monitoring_datagrid.datagrid('unselectAll');
                                                                            Monitoring_datagrid.datagrid('load', FMG.serializeObject($('#Monitoring_searchForm').form()));

                                                                        } else {
                                                                            $.messager.alert('标题', msg.result);
                                                                               Monitoring_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Monitoring_datagrid.datagrid('unselectAll');
                                                                        }

                                                                    },
                                                                    error: function(data) {
                                                                     
                                                                        $.messager.alert('标题','ajax获取失败');
                                                                           Monitoring_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Monitoring_datagrid.datagrid('unselectAll');
                                                                    }
                                                                });
                                                            };
                                                            FMG.editeRow = undefined;
                                                        }
                                                    },
                                                    {
                                                        text: '重置',
                                                        handler: function() {
                                                            $('#EditeMonitoringForm').form('clear');
                                                            FMG.editeRow = undefined;
                                                        }
                                                    }]
                                                });

                                            }
                                        }
                                    },
                                    '-', {
                                        text: '删除监控地址',
                                        iconCls: 'icon-remove',
                                        handler: function() {
                                            var rows = Monitoring_datagrid.datagrid('getSelections');
                                            console.log("rows=" + rows.length);
                                            if (rows.length > 0) {
                                                $.messager.confirm('请确认', '您确定要删除当前选择的项目吗?',
                                                function(b) { //当用户点击确定，b=true
                                                    if (b) {
                                                        var ids = [];
                                                        for (var i = 0; i < rows.length; i++) {
                                                            ids.push(rows[i].id);
                                                        }
                                                        console.log(ids.join(','));
                                                        $.ajax({
                                                            type: "POST",
                                                            url: '${pageContext.request.contextPath}/monitoring/delMonitoring.do?id=' + ids.join(','),
                                                            cache: false,
                                                            dataType: 'json',
                                                            success: function(msg) {
                                                                if (msg && msg.success == true) {
                                                                    for (i in msg.result) {
                                                                        $.messager.show({
                                                                            title: '提示',
                                                                            msg: '删除:' + msg.result[i].name + '成功'
                                                                        });
                                                                    }

                                                                Monitoring_datagrid.datagrid('unselectAll');
                                                                    Monitoring_datagrid.datagrid('load', FMG.serializeObject($('#Monitoring_searchForm').form()));
                                                                } else {
                                                                    $.messager.alert('标题', "删除失败");
                                                                    Monitoring_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Monitoring_datagrid.datagrid('unselectAll');
                                                                }
                                                            },
                                                            error: function(data) {
                                                                $.messager.alert('标题', "ajax获取失败");
                                                                Monitoring_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Monitoring_datagrid.datagrid('unselectAll');
                                                              
                                                            }
                                                        });
                                                    }
                                                });

                                            } else {
                                                $.messager.alert('提示', '请选择要删除的记录!', 'error');
                                            }

                                        }
                                    },
                                    '-', {
                                        text: '修改',
                                        iconCls: 'icon-edit',
                                        handler: function() {
                                            var rows = Monitoring_datagrid.datagrid('getSelections');
                                            if (rows.length > 1) {
                                                var names = [];
                                                for (var i = 0; i < rows.length; i++) {
                                                    names.pash(rows[i].name);
                                                }
                                                $.messager.show({
                                                    msg: '只能选择一项进行编辑,您已经选了【' + names.join(",") + '】' + rows.length + '项',
                                                    title: '提示'
                                                });
                                            } else if (rows.length == 0) {
                                                $.messager.show({
                                                    msg: '请选择一项进行修改',
                                                    title: '提示'
                                                });
                                            } else if (rows.length == 1) {
                                             

                                                $('#EditeMonitoringForm').find('[name = id]').attr('readonly', 'readonly');
                                                $('#EditeMonitoringForm').form('clear');

                                                $('#EditeMonitoringForm').form('load', {
                                                    id: rows[0].id,
                                                    name: rows[0].name,
                                                    url:rows[0].url
                                                });
                                                MonitoringDialog = $('#MonitoringDialog').dialog({
                                                title:'修改',
                                                    modal: true,
                                                    buttons: [{
                                                        text: '修改监控',
                                                        handler: function() {
                                                            if ($('#EditeMonitoringForm').form('validate')) {
                                                                $.ajax({
                                                                    type: "POST",
                                                                    url: '${pageContext.request.contextPath}/monitoring/updateMonitoring.do?id=' + rows[0].id,
                                                                    data: $('#EditeMonitoringForm').serialize(),
                                                                    cache: false,
                                                                    dataType: 'json',
                                                                    success: function(msg) {
                                                                        if (msg && msg.success == true) {
                                                                            MonitoringDialog.dialog('close');
                                                                            $.messager.show({
                                                                                title: '提示',
                                                                                msg: '修改:' + msg.result.name + '成功'
                                                                            });
                                                                             Monitoring_datagrid.datagrid('unselectAll');
                                                                            Monitoring_datagrid.datagrid('load', FMG.serializeObject($('#Monitoring_searchForm').form()));
                                                                        } else {
                                                                            $.messager.alert('标题', "修改失败");
                                                                        }
                                                                    },
                                                                    error: function(data) {
                                                                        $.messager.alert('标题', "ajax获取失败");
                                                                        alert(data, "error");
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    },
                                                    {
                                                        text: '重置',
                                                        handler: function() {
                                                          $('#EditeMonitoringForm').form('clear');
                                                            FMG.editeRow = undefined;
                                                            
                                                        }
                                                    }]
                                                });

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
                                            Monitoring_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Monitoring_datagrid.datagrid('unselectAll');
                                        }
                                    },
                                    '-', {
                                        text: '保存',
                                        iconCls: 'icon-save',
                                        handler: function() {
                                            console.log("结束编辑的行=" + FMG.editeRow);
                                            Monitoring_datagrid.datagrid('endEdit', FMG.editeRow);
                                            FMG.editeRow = undefined;
                                        }
                                    }],
                                    //编辑关闭后触发的事件
                                    onAfterEdit: function(rowIndex, rowData, changes) { //编辑行索引，编辑行数据，原先数据和新数据的键值对
                                        console.log(rowData);
                                     
                                        var url = '${pageContext.request.contextPath}/monitoring/updateMonitoring.do?id=' + rowData.id;
                                            $.ajax({
                                                type: "POST",
                                                url: url,
                                                data: rowData,
                                                cache: false,
                                                dataType: 'json',
                                                success: function(msg) {
                                                    if (msg && msg.success == true) {
                                                        $.messager.show({
                                                            title: '提示',
                                                            msg: '修改:' + msg.result.name + '成功'
                                                        });
                                                         Monitoring_datagrid.datagrid('unselectAll');
                                                        Monitoring_datagrid.datagrid('load', FMG.serializeObject($('#Monitoring_searchForm').form()));
                                                    } else {
                                                       $.messager.alert('标题', '修改失败', 'error');
                                                    Monitoring_datagrid.datagrid('rejectChanges');
                                                     Monitoring_datagrid.datagrid('unselectAll');
                                                    }
                                                },
                                                error: function(data) {
                                                    $.messager.alert('标题', 'ajax获取失败', 'error');
                                                    Monitoring_datagrid.datagrid('rejectChanges');
                                                     Monitoring_datagrid.datagrid('unselectAll');
                                                }
                                            });
                                       	
                                    },

                                   
                                    //双击行触发的事件
                               /*     onDblClickRow: function(rowIndex, rowData) {

                                        if (FMG.editeRow != undefined) { //只能开启一行编辑状态
                                            Monitoring_datagrid.datagrid('endEdit', FMG.editeRow);
                                        }
                                        if (FMG.editeRow == undefined) {
                                            Monitoring_datagrid.datagrid('beginEdit', rowIndex);
                                            FMG.editeRow = rowIndex;
                                        }

                                    },*/
                                });


                                searchMonitoringsByConditions = function() {
                                    Monitoring_datagrid.datagrid('load', FMG.serializeObject($('#Monitoring_searchForm').form()));
                                   

                                };

                                clearSearchMonitoring = function() {
                                    FMG.clearForm($('#Monitoring_searchForm').form());
                                    Monitoring_datagrid.datagrid('load', {});
                                };

                            });
                        </script>
  </head>
 
 <body style="width: 1093px;">
	<div class="easyui-Layout" fit="true" border="false">
		<div title="查询" region="north" collapsed="true" border="false"split="true"
			style="height:100px;overflow: hidden;">
<form id="Monitoring_searchForm" action="" method="post">
	<table class="tableForm datagrid-toolbar"
		style="width: 100%;height: 100%">
<tr><th>名称:</th><td><input name="name" type="text" style="width: 315px"/></td></tr>
<tr>
	<th>更新时间:</th>
	<td><input class="easyui-datetimebox" required="true" editable="false" name="updateDateStart" /> - <input class="easyui-datetimebox" required="true" editable="false" name="updateDateEnd" />
 <a href="javascript:void(0);"
class="easyui-linkbutton" onclick="searchMonitoringsByConditions()">
	查询 </a> <a href="javascript:void(0);" class="easyui-linkbutton"
onclick="clearSearchMonitoring()"> 清空 </a></td>
			</tr>
		</table>
	</form>
</div><!-- 查询 -->















<div title="服务监控管理" region="center" border="false">
	<table id="Monitoring_datagrid">
	</table>
</div>
</div><!-- layout -->
<div id="MonitoringDialog"
	style="width: 300px;height: 200px;text-align: center;
	">
<form id="EditeMonitoringForm" action="" method="post">
	<table cellpadding="10" cellspacing="10">
		
<tr>
	<th style="text-align: right">服务名称:</th>
	<td><input class="easyui-validatebox" required="true" validType="chinese"
		type="text" name="name" />
	</td>
</tr>	
<tr>
	<th style="text-align: right">访问地址:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="url" />
	</td>
</tr>		
			</table>
		</form>
	</div>
</body>
</html>
