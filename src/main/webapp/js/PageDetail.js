
/**
	 * 页面访问量实时动态
	 */
	$(function() {
		
		 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydPage/getAllPages4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
							if (msg && msg.success == true) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.pid , item.visitedTimes  ];
                                    ids.push(obj);
                                }
							$("#container").empty();
							var chart = anychart.pieChart( ids);
				            chart.title('页面最新访问动态');
		                    chart.container('container'); 					                        		
		                    chart.draw(); 
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
	
		
		 audite_datagrid = $("#audite_datagrid").datagrid({
			url : './ydPage/getAllPages.do',
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
			striped:true,
			sortOrder : 'desc',
			columns : [ [ {
	            title :'编号',
	            field :'id',
	            width : 20 ,
	            checkbox : true    
	            },{
				title : '页面标识',
				field : 'pid',
				width : 50,
				fit : true,
				
			},{
				title : '访问路径',
				field : 'url',
				width : 200,
				fit : true,
				
			},{
				title : '所属栏目',
				field : 'bmName',
				width : 100,
				fit : true,
				
			},{
				title : '服务访问次数',
				field : 'visitedTimes',
				width : 30,
				sortable : true,
			}
            ] ],
            toolbar:[{
	            text:'详细情况',
	            iconCls : 'icon-large-chart',
	            handler : function(){
	            var rows = audite_datagrid.datagrid('getSelections');
				if(rows.length >1) {
				var names = [];
				for(var i =0; i<rows.length; i++){
				names.push(rows[i].pid);
				}
				$.messager.show({	
				msg : '只能选择一个页面进行查看或者导出,您已经选了【'+names.join(",")+'】' + rows.length +'个服务',
				title : '提示'
				});
				} else if(rows.length == 0){
				$.messager.show({
				msg : '请选择一个页面',
				title : '提示'
				});
				}else if(rows.length == 1){
					console.log("获得详细信息");
					audit_DetailDialog = $('#audit_DetailDialog').dialog({
						});
						$("#audit_Detail_datagrid").datagrid({
						url:'./ydUserPage/getDetailByPid.do?pid='+encodeURI(encodeURI(rows[0].pid)),
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
                                field: 'uip',
                                width: 100,
                                sortable: true,
                                
                            }, {
                                title: '访问时间',
                                field: 'visitedTime',
                                width: 100,
                                sortable: true,
                                
                            },
							
			            ] ]		
					});audite_datagrid.datagrid('unselectAll');
				  };
			 }
	            },'-',
//	            {
//	            text:'导出统计表文件',
//	            iconCls : 'icon-save',
//	            handler : function(){
//	            	// window.location.href="./ydVisiterService/dategrid2excel4ServiceOntime.do";
//	            }
//	            },'-',
	            {
	            text:' 取消选中',
	            iconCls : 'icon-cancel',
	            handler : function(){
	            //取消被选择的号
	            	audite_datagrid.datagrid('unselectAll');
	            }
	            }],

		});//audit_service_datagrid
	});//服务访问量实时动态
