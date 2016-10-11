var FMG = $.extend({}, FMG);/* 定义全局变量 */



$(function() {

	$.fn.panel.defaults.loadingMessage = '数据加载中,请稍后...';
	$.fn.datagrid.defaults.loadMsg = '数据加载中,请稍后...';
	var easyuiErrorFunction = function(XMLHttpRequest) {
		$.messager.progress('close');
		$.messager.alert("错误", XMLHttpRequest.responseText);
	};
	$.fn.datagrid.defaults.onloadError = easyuiErrorFunction;
	$.fn.treegrid.defaults.onloadError = easyuiErrorFunction;
	$.fn.tree.defaults.onloadError = easyuiErrorFunction;
	$.fn.combogrid.defaults.onloadError = easyuiErrorFunction;
	$.fn.combobox.defaults.onloadError = easyuiErrorFunction;
	$.fn.form.defaults.onloadError = easyuiErrorFunction;


	var easyuiPanelOnMove = function(left, top) {
		/** 防止超出浏览器边界* */
		if (left < 0) {
			$(this).window('move', {
				left : 1
			});
		}
		if (top < 0) {
			$(this).window('move', {
				top : 1
			});
		}
	};
	

	$.fn.panel.defaults.onMove = easyuiPanelOnMove;
	// $.fn.windows.defaults.onMove = easyuiPanelOnMove;
	$.fn.dialog.defaults.onMove = easyuiPanelOnMove;

	/**
	 * 获得url参数
	 */
	FMG.getUrlParam = function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)([&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return unescape(r[2]);
		return null;
	};


	/**
	 * 扩展datetimeboxthe
	 */
	$.extend($.fn.datagrid.defaults.editors, {
		datetimebox : {
			init : function(container, options) {
				var input = $('<input  class="easyui-datetimebox">').appendTo(
						container);// 将
				options.editable = false;
				input.datetimebox(options);
				return input;
			},
			getValue : function(target) {
				return $(target).datetimebox('getValue');
			},
			setValue : function(target, value) {
				$(target).datetimebox('setValue');
			},
			resize : function(target, width) {
				var input = $(target);
				if ($.boxModel == true) {
					input.width(width - (input.outerWidth() - input.width()));
				} else {
					input.width(width);
				}
			},
			destory : function(target) {
				$(target).datetimebox('destroy');
			}
		}
	});
	
	
	$.extend($.fn.validatebox.defaults.rules, {
        idcard: {// 验证身份证
            validator: function (value) {
                return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
            },
            message: '身份证号码格式不正确'
        },
        minLength: {
            validator: function (value, param) {
                return value.length >= param[0];
            },
            message: '请输入至少（2）个字符.'
        },
        length: { validator: function (value, param) {
            var len = $.trim(value).length;
            return len >= param[0] && len <= param[1];
        },
            message: "输入内容长度必须介于{0}和{1}之间."
        },
        phone: {// 验证电话号码
            validator: function (value) {
                return /^((\d2,3)|(\d{3}\-))?(0\d2,3|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
            },
            message: '格式不正确,请使用下面格式:020-88888888'
        },
        mobile: {// 验证手机号码
            validator: function (value) {
                return /^(13|15|18)\d{9}$/i.test(value);
            },
            message: '手机号码格式不正确'
        },
        intOrFloat: {// 验证整数或小数
            validator: function (value) {
                return /^\d+(\.\d+)?$/i.test(value);
            },
            message: '请输入数字，并确保格式正确'
        },
        currency: {// 验证货币
            validator: function (value) {
                return /^\d+(\.\d+)?$/i.test(value);
            },
            message: '货币格式不正确'
        },
        qq: {// 验证QQ,从10000开始
            validator: function (value) {
                return /^[1-9]\d{4,9}$/i.test(value);
            },
            message: 'QQ号码格式不正确'
        },
        integer: {// 验证整数 可正负数
            validator: function (value) {
                //return /^[+]?[1-9]+\d*$/i.test(value);

                return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
            },
            message: '请输入整数'
        },
        age: {// 验证年龄
            validator: function (value) {
                return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
            },
            message: '年龄必须是0到120之间的整数'
        },

        chinese: {// 验证中文
            validator: function (value) {
                return /^[\Α-\￥]+$/i.test(value);
            },
            message: '请输入中文,并且不超过5个字符'
        },
        english: {// 验证英语
            validator: function (value) {
                return /^[A-Za-z]+$/i.test(value);
            },
            message: '请输入英文'
        },
        unnormal: {// 验证是否包含空格和非法字符
            validator: function (value) {
                return /.+/i.test(value);
            },
            message: '输入值不能为空和包含其他非法字符'
        },
        username: {// 验证用户名
            validator: function (value) {
                return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
            },
            message: '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
        },
        faxno: {// 验证传真
            validator: function (value) {
                //            return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
                return /^((\d2,3)|(\d{3}\-))?(0\d2,3|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
            },
            message: '传真号码不正确'
        },
        zip: {// 验证邮政编码
            validator: function (value) {
                return /^[1-9]\d{5}$/i.test(value);
            },
            message: '邮政编码格式不正确'
        },
        ip: {// 验证IP地址
            validator: function (value) {
                return /d+.d+.d+.d+/i.test(value);
            },
            message: 'IP地址格式不正确'
        },
        name: {// 验证姓名，可以是中文或英文
            validator: function (value) {
                return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
            },
            message: '请输入姓名'
        },
        
        date: {// 验证姓名，可以是中文或英文
            validator: function (value) {
                //格式yyyy-MM-dd或yyyy-M-d
                return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value);
            },
            message: '清输入合适的日期格式'
        },
        msn: {
            validator: function (value) {
                return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
            },
            message: '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
        },
        same: {
            validator: function (value, param) {
                if ($("#" + param[0]).val() != "" && value != "") {
                    return $("#" + param[0]).val() == value;
                } else {
                    return true;
                }
            },
            message: '两次输入的密码不一致！'
        }
    });
	/**
	 * 将form表单值序列成对象
	 */
	FMG.serializeObject = function(form) {
		var obj = {};
		// form.serializeArray():jquery方法，将表单对象序列化成一个list
		// $.each循环
		$.each(form.serializeArray(), function(index) {
			if (obj[this['name']]) {
				obj[this['name']] = obj[this['name']] + "," + this['value'];
			} else {
				obj[this['name']] = this['value'];
			}
		});
		return obj;
	};

	/**
	 * 清空表单
	 */
	FMG.clearForm = function(form) {
		form.form('clear');
	};

	/**
	 * 验证密码复杂度
	 */
	FMG.isAvailabilityPassword = function(psw) {
		var count = 0;
		if (psw.length < 6)
			return false;
		if (/[0-9]/.test(psw))
			count++;
		if (/[a-z]/.test(psw))
			count++;
		if (/[A-Z]/.test(psw))
			count++;
		if (/[~!@#$%^&*()_+]/.test(psw))
			count++;
		if (count >= 3)
			return true;
		else
			return false;
	};
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////	//右键编辑
	edit = function() {
		
	      var rows =$("#audit_service_datagrid").datagrid('getSelections');
	      console.log(rows.length);
			if(rows.length == 0){
			$.messager.show({
			msg : '请选择一个用户进行修改',
			title : '提示'
			});
			}else if(rows.length == 1){
				console.log("开始编辑");
				console.log(rows[0]);
									
									$("#audit_serviceDetail_datagrid").datagrid({
										url : './ydVisiterService/getDetailBySid.do?sid='+rows[0].sId,
										title : '',
										iconCls : 'icon-save',
										cache : false,
										pagination : true,
										pageSize : 10,
										pageList : [ 10, 20, 50 ],
										striped:true,
										fit : true,
										fitColumns : true,
										nowrap : false,
										border : false,
										treeField : 'text',
										idField : 'id',
										sortName : '',
										sortOrder : 'desc',
										striped:true,
										columns : [ [ {
								            title :'编号',
								            field :'id',				            
								            width : 20 ,
							 
								            },{
					                            title: '用户id',
					                            field: 'uid',
					                            width: 100,
					                            sortable: true,
					                            
					                        },{
					                            title: '访问ip',
					                            field: 'visiteIp',
					                            width: 100,
					                            sortable: true,
					                            
					                        }, {
					                            title: '访问时间',
					                            field: 'visisteDate',
					                            formatter: function(value, row, index) {
					                                if (row) {
					                                	 var date = new Date(row.visisteDate);
					                                     return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate()+" "+date.getHours()+":"+  date.getMinutes()+":"+date.getSeconds(); //秒;    
					                                } else {
					                                    return "";
					                                }
					                            },
					                            width: 100,
					                            sortable: true,
					                            
					                        },
											
							            ] ]		
									});
									audit_serviceDetailDialog = $('#audit_serviceDetailDialog').dialog({
									});
									$("#audit_service_datagrid").datagrid('unselectAll');

			};
	};
	//编辑选中的节点
	edit_tree=function(){
		
		 $('#TreeDialog').css('display','block');
		var nodes = $('#ResouceTree').tree('getSelected');
           $('#TreeEditeForm').form('load', {
	        	   ID :  nodes.id,
	        	   名称 :  nodes.text,
	        	   序号 :  '',
	        	  父节点 :  ''
           });
           TreeDialog = $('#TreeDialog').dialog({
               modal: true,
               buttons: [{
                   text: '提交',
                   handler: function() {
                       if ($('#TreeEditeForm').form('validate')) {
                    	   $.messager.progress();
                           $.ajax({
                               type: "POST",
                               url: './viewMenu/updateTree.do?id=' + nodes.id,
                               data: $('#TreeEditeForm').serialize(),
                               cache: false,
                               dataType: 'json',
                               success: function(msg) {
                                   if (msg && msg.success == true) {
                                	   $.messager.progress('close');
                                	 	  TreeDialog.dialog('close');
                                          $.messager.show({
                                              title: '提示',
                                              msg: '更新节点成功'
                                          });
                                          $('#tt').tree({    
                                        	    url: "./viewMenu/loadTree.do",//发送异步ajax请求，还会携带一个i额id参数    
                                        	    animate:true,
                                        	    checkbox:false,
                                        	    onlyLeafCheck:true,
                                        	    dnd:true,
                                        	   onContextMenu: function(e, node){
                                        			e.preventDefault();
                                        			// 查找节点
                                        			$('#ResouceTree').tree('select', node.target);
                                        			// 显示快捷菜单
                                        			$('#menu_tree').menu('show', {
                                        				left: e.pageX,
                                        				top: e.pageY
                                        			});
                                        		}
                                        	}); 
                                   } else {
                                	   $.messager.progress('close');
                                       $.messager.alert('标题', "更新节点失败");
                                   }
                               },
                               error: function(data) {
                            	   $.messager.progress('close');
                                   $.messager.alert('标题', "更新节点失败");
                                   alert(data, "error");
                               }
                           });
                       }
                   }
               },
               {
                   text: '重置',
                   handler: function() {
                       $('#addplayerForm').form('clear');
                   }
               }]
           });
	};
	
	//删除树的节点
	delet_tree= function(){
			
	var nodes = $('#ResouceTree').tree('getSelected');
	
                $.messager.confirm('请确认', '您确定要删除当前选择的项目吗?',
                function(b) { //当用户点击确定，b=true
                    if (b) {
                    	                
                        $.ajax({
                            type: "POST",
                            url: './viewMenu/deleteNode.do?id=' +nodes.id,
                            cache: false,
                            dataType: 'json',
                            success: function(msg) {
                                if (msg && msg.success == true) {
                                   console.log("删除节点");
                                        $.messager.show({
                                            title: '提示',
                                            msg: '删除节点:' + msg.result[0].text + '成功'
                                        });
                                    
                                        $('#ResouceTree').tree({    
                                    	    url: "./viewMenu/loadTree.do",//发送异步ajax请求，还会携带一个i额id参数    
                                    	    animate:true,
                                    	    checkbox:false,
                                    	    onlyLeafCheck:true,
                                    	    dnd:true,
                                    	   onContextMenu: function(e, node){
                                    			e.preventDefault();
                                    			// 查找节点
                                    			$('#ResouceTree').tree('select', node.target);
                                    			// 显示快捷菜单
                                    			$('#menu_tree').menu('show', {
                                    				left: e.pageX,
                                    				top: e.pageY
                                    			});
                                    		}
                                    	}); 
                                  //  player_datagrid.datagrid('load', FMG.serializeObject($('#player_searchForm').form()));
                                } else {
                                    $.messager.alert('标题', "删除节点失败");
                                }
                            },
                            error: function(data) {
                                $.messager.alert('标题', "删除用节点失败");
                                alert(data, "error");
                            }
                        });
                    }
                });

   
	};
	
	//树服务函数
	append_tree = function(){
		 $('#TreeEditeForm').form('clear');
		 $('#TreeDialog').css('display','block');
		TreeDialog = $('#TreeDialog').dialog({
			title: '添加模块',
			  modal: true,
              buttons: [{
                  text: '添加节点',
                  handler: function() {
                      if ($('#TreeEditeForm').form('validate')) {
 
                          $.ajax({
                              type: "POST",
                              url: './viewMenu/addTree.do',
                              data: $('#TreeEditeForm').serialize(),
                              cache: false,
                              dataType: 'json',
                              success: function(msg) {
                                  if (msg && msg.success == true) {
                                	  
                                	  TreeDialog.dialog('close');
                                      $.messager.show({
                                          title: '提示',
                                          msg: '添加节点成功'
                                      });
                                      $('#ResouceTree').tree({    
                                    	    url: "./viewMenu/loadTree.do",//发送异步ajax请求，还会携带一个i额id参数    
                                    	    animate:true,
                                    	    checkbox:false,
                                    	    onlyLeafCheck:true,
                                    	    dnd:true,
                                    	   onContextMenu: function(e, node){
                                    			e.preventDefault();
                                    			// 查找节点
                                    			$('#ResouceTree').tree('select', node.target);
                                    			// 显示快捷菜单
                                    			$('#menu_tree').menu('show', {
                                    				left: e.pageX,
                                    				top: e.pageY
                                    			});
                                    		}
                                    	}); 
                                  } else {
                                      $.messager.alert('标题',msg.result );
                                  }

                              },
                              error: function(data) {
                                  $.messager.alert('标题', "添加节点失败");
                                  alert(data, "error");
                              }
                          });
                      };
                  }
              },
              {
                  text: '重置',
                  handler: function() {
                      $('#TreeEditeForm').form('clear');
                     // FMG.editeRow = undefined;
                  }
              }]
		});
		  
	};
	
	FMG.getServiceDetail = function(){
		 window.location.href="ServiceDetail.html";
	};
	FMG.goindx = function(){
		 window.location.href="index.html";
	};
	FMG.getServiceCurrentTotal = function(){
		
		 window.location.href="ServiceTotal.html";
	};
	
	FMG.getServiceHistroyTotal = function(){
		
		 window.location.href="ServiceHistory.html";
	};
	
	FMG.goAudit = function(){
		
		 window.location.href="Audite.html";
	};

	FMG.goLogin = function(){
		
		 window.location.href="login_Audite.html";
	};
	
	FMG.goViewAdmin = function(){
		
		 window.location.href="AuditeManage.html";
	};
	
	FMG.getUsersOntimel = function(){
		window.location.href="UserDetail.html";
	};
	
	FMG.getUserTotal = function(){
		window.location.href="UserTotal.html";
	};
	FMG.getUserHistory = function(){
		window.location.href="UserHistory.html";
	};
	
	
	FMG.getDownloadOntime = function(){
		window.location.href="DataDetail.html";
	};
	
	FMG.getDataTotal = function(){
		window.location.href="DataTotal.html";
	};
	FMG.getPageOntime = function(){
		window.location.href="PageDetail.html";
	};
	
	FMG.getPageTotal = function(){
		window.location.href="PageTotal.html";
	};
	
	FMG.DataHistory = function(){
		window.location.href="DataHistory.html";
	};
	
	FMG.getPageHistory = function(){
		
		window.location.href="PageHistory.html";
	};
	FMG.logOut = function(){
		 $.cookie('username', null);
		 $.cookie('login_ip', null);
		 $.cookie('login_time', null);
		 FMG.goLogin();
		};
});
