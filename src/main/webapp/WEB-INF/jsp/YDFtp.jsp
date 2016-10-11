<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   
    
    <title>FTP管理</title>
    
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
                            var Ftp_datagrid;
                            var i = 0;
                            var FtpDialog;
                            var EditeFtpForm;
                            FMG.editeRow = undefined; //记录正在编辑的行号
                            $(function() {

                                Ftp_datagrid = $("#Ftp_datagrid").datagrid({
                                    url: '${pageContext.request.contextPath}/ydftp/getAllYDFtp.do',
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
                                        title: 'FID',
                                        field: 'fid',
                                        width: 50,
                                        
                    
                                    },
                                    {
                                        title: '地址',
                                        field: 'url',
                                        width: 100,
                                       
                                      
                                    },
                                    {
                                        title: '端口',
                                        field: 'port',
                                        width: 30,
                                       
                                        
                                    }, {
                                        title: '路径',
                                        field: 'path',
                                        width: 100,
                                       
                                        
                                    }, {
                                        title: '用户名',
                                        field: 'userName',
                                        width: 100,
                                       
                                        
                                    },{
                                        title: '密码',
                                        field: 'passwd',
                                        width: 100,
                                       
                                        
                                    }, {
                                        title: '状态',
                                        field: 'state',
                                        width: 50,
                                       
                                        
                                    }]],
                                    toolbar: [
                                    {
                                        text: '启动',
                                        iconCls: 'icon-ok',
                                        handler: function() {
                                            var rows = Ftp_datagrid.datagrid('getSelections');
                                            console.log("rows=" + rows.length);
                                            if (rows.length > 0) {
                                                $.messager.confirm('请确认', '即将启动。。。',
                                                function(b) { //当用户点击确定，b=true
                                                    if (b) {
                                                        $.ajax({
                                                            type: "POST",
                                                            url: '${pageContext.request.contextPath}/ydftp/ocFtp.do?id=' + rows[0].id+'&state=1',
                                                            cache: false,
                                                            dataType: 'json',
                                                            success: function(msg) {
                                                                if (msg && msg.success == true) {
                                                 
                                                                        $.messager.show({
                                                                            title: '提示',
                                                                            msg: '更新:' + msg.result.fid + '状态为:'+msg.result.state
                                                                        });
                                                                          Ftp_datagrid.datagrid('unselectAll');
                                                                  

                                                               
                                                                  Ftp_datagrid.datagrid('load', {});
                                                                } else {
                                                                    $.messager.alert('标题', "修改状态失败");
                                                                    Ftp_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Ftp_datagrid.datagrid('unselectAll');
                                                                }
                                                            },
                                                            error: function(data) {
                                                                $.messager.alert('标题', "修改状态失败");
                                                                Ftp_datagrid.datagrid('rejectChanges');
                                                                 Ftp_datagrid.datagrid('unselectAll');
                                        
                                                            }
                                                        });
                                                    }
                                                });

                                            } else {
                                                $.messager.alert('提示', '请选择记录!', 'error');
                                            }

                                        }
                                    }, {
                                        text: '关闭',
                                        iconCls: 'icon-no',
                                        handler: function() {
                                            var rows = Ftp_datagrid.datagrid('getSelections');
                                            if (rows.length > 0) {
                                                $.messager.confirm('请确认', '即将关闭。。。',
                                                function(b) { //当用户点击确定，b=true
                                                    if (b) {
                                                        $.ajax({
                                                            type: "POST",
                                                            url: '${pageContext.request.contextPath}/ydftp/ocFtp.do?id=' + rows[0].id+'&state=0',
                                                            cache: false,
                                                            dataType: 'json',
                                                            success: function(msg) {
                                                                if (msg && msg.success == true) {
                                                 
                                                                        $.messager.show({
                                                                            title: '提示',
                                                                            msg: '更新:' + msg.result.fid + '状态为:'+msg.result.state
                                                                        });
                                                                          Ftp_datagrid.datagrid('unselectAll');
                                                                  

                                                               
                                                                  Ftp_datagrid.datagrid('load', {});
                                                                } else {
                                                                    $.messager.alert('标题', "修改状态失败");
                                                                    Ftp_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Ftp_datagrid.datagrid('unselectAll');
                                                                }
                                                            },
                                                            error: function(data) {
                                                                $.messager.alert('标题', "修改状态失败");
                                                                Ftp_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Ftp_datagrid.datagrid('unselectAll');
                                                              
                                                            }
                                                        });
                                                    }
                                                });

                                            } else {
                                                $.messager.alert('提示', '请选择记录!', 'error');
                                            }

                                        }
                                    },{
                                        text: '添加模块',
                                        iconCls: 'icon-add',
                                        handler: function() {
                                            if (FMG.editeRow != undefined) { //只能开启一行编辑状态
                                                Ftp_datagrid.datagrid('endEdit', FMG.editeRow);
                                            }
                                            if (FMG.editeRow == undefined) {

                                                $('#EditeFtpForm').form('clear');
                                                
                                                FtpDialog = $('#FtpDialog').dialog({
                                                title:'添加',
                                                    modal: true,
                                                    buttons: [{
                                                        text: '添加',
                                                        handler: function() {
                                                        
                                                            if ($('#EditeFtpForm').form('validate')) {
                                                                FMG.editeRow = 0;
                                                                $.ajax({
                                                                    type: "POST",
                                                                    url: '${pageContext.request.contextPath}/ydftp/addFtp.do',
                                                                    data: $('#EditeFtpForm').serialize(),
                                                                    cache: false,
                                                                    dataType: 'json',
                                                                    success: function(msg) {
                                                                        if (msg && msg.success == true) {
                                                                            FtpDialog.dialog('close');
                                                                            $.messager.show({
                                                                                title: '提示',
                                                                                msg: '添加:' + msg.result.fid + '成功'
                                                                            });
                                                                            Ftp_datagrid.datagrid('unselectAll');
                                                                            Ftp_datagrid.datagrid('load', FMG.serializeObject($('#Ftp_searchForm').form()));

                                                                        } else {
                                                                            $.messager.alert('标题','添加失败，请重新尝试' );
                                                                        }

                                                                    },
                                                                    error: function(data) {
                                                                        $.messager.alert('标题','ajax获取失败');
                                                                    }
                                                                });
                                                            };
                                                            FMG.editeRow = undefined;
                                                        }
                                                    },
                                                    {
                                                        text: '重置',
                                                        handler: function() {
                                                            $('#EditeFtpForm').form('clear');
                                                            FMG.editeRow = undefined;
                                                        }
                                                    }]
                                                });

                                            }
                                        }
                                    },
                                    '-', {
                                        text: '删除',
                                        iconCls: 'icon-remove',
                                        handler: function() {
                                            var rows = Ftp_datagrid.datagrid('getSelections');
                                           
                                            if (rows.length > 0) {
                                                $.messager.confirm('请确认', '您确定要删除吗?',
                                                function(b) { //当用户点击确定，b=true
                                                    if (b) {
                                                        var ids = [];
                                                        for (var i = 0; i < rows.length; i++) {
                                                            ids.push(rows[i].id);
                                                        }
                                                        console.log(ids.join(','));
                                                        $.ajax({
                                                            type: "POST",
                                                            url: '${pageContext.request.contextPath}/ydftp/delYDFtp.do?id=' + ids.join(','),
                                                            cache: false,
                                                            dataType: 'json',
                                                            success: function(msg) {
                                                                if (msg && msg.success == true) {
                                                                    for (i in msg.result) {
                                                                        $.messager.show({
                                                                            title: '提示',
                                                                            msg: '删除:' + msg.result[i].fid + '成功'
                                                                        });
                                                                    }

                                                                Ftp_datagrid.datagrid('unselectAll');
                                                                    Ftp_datagrid.datagrid('load', {});
                                                                } else {
                                                                    $.messager.alert('标题', "删除模块失败");
                                                                }
                                                            },
                                                            error: function(data) {
                                                                $.messager.alert('标题', "ajax获取失败");
                                                              
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
                                        text: '修改模块',
                                        iconCls: 'icon-edit',
                                        handler: function() {
                                            var rows = Ftp_datagrid.datagrid('getSelections');
                                            if (rows.length > 1) {
                                                var names = [];
                                                for (var i = 0; i < rows.length; i++) {
                                                    names.pash(rows[i].name);
                                                }
                                                $.messager.show({
                                                    msg: '只能选择一条记录,您已经选了【' + names.join(",") + '】' + rows.length + '个用户',
                                                    title: '提示'
                                                });
                                            } else if (rows.length == 0) {
                                                $.messager.show({
                                                    msg: '请选择一条记录',
                                                    title: '提示'
                                                });
                                            } else if (rows.length == 1) {
                                           
                                                $('#EditeFtpForm').find('[name = id]').attr('readonly', 'readonly');
                                                $('#EditeFtpForm').form('load', {
                                                    id: rows[0].id,
                                                    fid: rows[0].fid,
                                                    url:rows[0].url,
                                                    port:rows[0].port,
                                                    path:rows[0].path,
                                                    userName:rows[0].userName,
                                                    passwd: rows[0].passwd,                                                                                                                               
                                                });
                                                FtpDialog = $('#FtpDialog').dialog({
                                                title: '修改',
                                                    modal: true,
                                                    buttons: [{
                                                        text: '修改',
                                                        handler: function() {
                                                            if ($('#EditeFtpForm').form('validate')) {
                                                                $.ajax({
                                                                    type: "POST",
                                                                    url: '${pageContext.request.contextPath}/ydftp/updateYDFtp.do?id=' + rows[0].id,
                                                                    data: $('#EditeFtpForm').serialize(),
                                                                    cache: false,
                                                                    dataType: 'json',
                                                                    success: function(msg) {
                                                                        if (msg && msg.success == true) {
                                                                            FtpDialog.dialog('close');
                                                                            $.messager.show({
                                                                                title: '提示',
                                                                                msg: '修改:' + msg.result.fid + '成功'
                                                                            });
                                                                             Ftp_datagrid.datagrid('unselectAll');
                                                                            Ftp_datagrid.datagrid('load', FMG.serializeObject($('#Ftp_searchForm').form()));
                                                                        } else {
                                                                            $.messager.alert('标题', "修改失败");
                                                                        }
                                                                    },
                                                                    error: function(data) {
                                                                        $.messager.alert('标题', "修改失败");
                                                                        alert(data, "error");
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    },
                                                    {
                                                        text: '重置',
                                                        handler: function() {
                                                            $('#EditeFtpForm').form('clear');
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
                                            Ftp_datagrid.datagrid('rejectChanges');
                                            //取消被选择的号
                                            Ftp_datagrid.datagrid('unselectAll');
                                        }
                                    },
                                    '-', {
                                        text: '保存',
                                        iconCls: 'icon-save',
                                        handler: function() {
                                            console.log("结束编辑的行=" + FMG.editeRow);
                                            Ftp_datagrid.datagrid('endEdit', FMG.editeRow);
                                            FMG.editeRow = undefined;
                                        }
                                    }],
                                    //编辑关闭后触发的事件
                                    onAfterEdit: function(rowIndex, rowData, changes) { //编辑行索引，编辑行数据，原先数据和新数据的键值对
                                        console.log(rowData);
                                     
                                        var url = '${pageContext.request.contextPath}/Ftp/updateFtp.do?id=' + rowData.id;
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
                                                        Ftp_datagrid.datagrid('load', FMG.serializeObject($('#Ftp_searchForm').form()));
                                                    } else {
                                                       $.messager.alert('标题', '修改失败', 'error');
                                                    Ftp_datagrid.datagrid('rejectChanges');
                                                    }
                                                },
                                                error: function(data) {
                                                    $.messager.alert('标题', '修改失败', 'error');
                                                    Ftp_datagrid.datagrid('rejectChanges');
                                                }
                                            });
                                       	
                                    },

                                  
                                    //双击行触发的事件
                                    onDblClickRow: function(rowIndex, rowData) {

                                        if (FMG.editeRow != undefined) { //只能开启一行编辑状态
                                            Ftp_datagrid.datagrid('endEdit', FMG.editeRow);
                                        }
                                        if (FMG.editeRow == undefined) {
                                            Ftp_datagrid.datagrid('beginEdit', rowIndex);
                                            FMG.editeRow = rowIndex;
                                        }

                                    },
                                });


                                searchFtpsByConditions = function() {
                                    Ftp_datagrid.datagrid('load', FMG.serializeObject($('#Ftp_searchForm').form()));
                                   

                                };

                                clearSearchFtp = function() {
                                    FMG.clearForm($('#Ftp_searchForm').form());
                                    Ftp_datagrid.datagrid('load', {});
                                };

                            });
                        </script>
  </head>
 
 <body style="width: 1093px;">
	<div class="easyui-Layout" fit="true" border="false">

<div title="FTP管理" region="center" border="false">
	<table id="Ftp_datagrid">
	</table>
</div>
</div><!-- layout -->
<div id="FtpDialog"
	style="width: 300px;height: 200px;text-align: center;
	">
<form id="EditeFtpForm" action="" method="post">
	<table cellpadding="10" cellspacing="10">
<tr>
			<th style="text-align: right">FID:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="fid" />
	</td>
</tr>
<tr>
	<th style="text-align: right">URL</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="url" />
	</td>
</tr>	
<tr>
	<th style="text-align: right">PORT:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="port" />
	</td>
</tr>
<tr>
	<th style="text-align: right">PATH:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="path" />
	</td>
</tr>	
<tr>
	<th style="text-align: right">用户名:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="userName" />
	</td>
</tr>
<tr>
	<th style="text-align: right">密码:</th>
	<td><input class="easyui-validatebox" required="true"
		type="text" name="passwd" />
	</td>
</tr>		
			</table>
		</form>
	</div>
</body>
</html>
