var history_total_page_datagrid;
	var history_total_bmodule_datagrid;
	var history_total_user_datagrid;
	var audit_Detail_datagrid;
	$(function() {
//1.西部page
	  	   $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydUserPage/VisitedHistoryByPage4chart.do",
	             async: false,
	            dataType: "json",
	            data : $('#page_searchForm').serialize(),
	             success : function(msg) {
	          	   $("#history_total_page_container").empty();
	          	 console.log(msg);
								if (msg.rows) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.pid , item.visitedTotalTimes];
	                                    ids.push(obj);
	                                }
								var chart = anychart.pieChart( ids);
								chart.title('页面历史访问量统计');
			                    chart.container('history_total_page_container').draw(); 					                        		
			                    
													                        		
									}else{
										console.log("返回值错误");
									} 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
	           
	         });//ajax  
	  	 history_total_page_datagrid = $("#history_total_page_datagrid").datagrid
         ({     
        	    url:"./ydUserPage/VisitedHistoryByPage.do",
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
				columns : [ [ {
		            title :'编号',
		            field :'id',
		            width : 10 ,
		            checkbox : true    
		            },{
					title : '页面编号',
					field : 'pid',
					width : 80,
					fit : true
					
				},{
					title : '访问次数',
					field : 'visitedTotalTimes',
					width : 18,
					fit : true,
					sortable : true
				}
	            ] ],
	            toolbar:[{
		            text:'详细情况',
		            iconCls : 'icon-large-chart',
		            handler : function(){
		            var rows = history_total_page_datagrid.datagrid('getSelections');
					if(rows.length >1) {
					var names = [];
					for(var i =0; i<rows.length; i++){
					names.push(rows[i].sid);
					}
					$.messager.show({	
					msg : '只能选择一条记录,您已经选了【'+names.join(",")+'】' + rows.length +'条记录',
					title : '提示'
					});
					} else if(rows.length == 0){
					$.messager.show({
					msg : '请选择一条记录',
					title : '提示'
					});
					}else if(rows.length == 1){
						$('#audit_DetailDialog').dialog({});
						audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
							url:'./ydUserPage/getDetailByPid4history.do?pid='+encodeURI(encodeURI(rows[0].pid)),
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
						});
						history_total_page_datagrid.datagrid('unselectAll');
					  };
				 }
		            },'-',
//		            {
//		            text:'导出统计表文件',
//		            iconCls : 'icon-save',
//		            handler : function(){
//		            	$.messager.show({	
//							msg : '功能开发中....',
//							title : '提示'
//							});
//		            }
//		            },'-',
		            {
		            text:' 取消选中',
		            iconCls : 'icon-cancel',
		            handler : function(){
		           
		            //取消选中，更改内容回滚
		            	history_total_page_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            	history_total_page_datagrid.datagrid('unselectAll');
		            }
		            }],
			
			});
	
/*************************************历史服务*************************/
	//2.东部用户  	
	
	  	 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydUserPage/getAllUserHistory4chart.do",
             async: false,
            dataType: "json",
            data : $('#page_searchForm').serialize(),
             success : function(msg) {
          	   $("#history_total_user_container").empty();
          	
							if (msg.rows) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.typeName , item.visiteTimes];
                                    ids.push(obj);
                                }
							var chart = anychart.pieChart( ids);
							chart.title('页面历史访问量统计');
		                    chart.container('history_total_user_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax  
	  	history_total_user_datagrid = $("#history_total_user_datagrid").datagrid
     ({     
    	    url:"./ydUserPage/getAllUserHistory.do",
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
			columns : [ [ {
	            title :'编号',
	            field :'id',
	            width : 10 ,
	            checkbox : true    
	            },{
				title : '用户类型',
				field : 'typeName',
				width : 80,
				fit : true
				
			},{
				title : '访问次数',
				field : 'visiteTimes',
				width : 18,
				fit : true,
				sortable : true
			}
            ] ],
            toolbar:[{
	            text:'详细情况',
	            iconCls : 'icon-large-chart',
	            handler : function(){
	            var rows = history_total_user_datagrid.datagrid('getSelections');
				if(rows.length >1) {
				var names = [];
				for(var i =0; i<rows.length; i++){
				names.push(rows[i].typeName);
				}
				$.messager.show({	
				msg : '只能选择一条记录,您已经选了【'+names.join(",")+'】' + rows.length +'条记录',
				title : '提示'
				});
				} else if(rows.length == 0){
				$.messager.show({
				msg : '请选择一条记录',
				title : '提示'
				});
				}else if(rows.length == 1){
					$('#audit_DetailDialog').dialog({});
					audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
						url:'./ydUserPage/getDetailByUsers.do?uid='+encodeURI(encodeURI(rows[0].typeName)),
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
                                
                            },{
                                title: '访问页面',
                                field: 'page',
                                width: 100,
                                sortable: true,
                                
                            }, {
                                title: '访问时间',
                                field: 'visitedTime',
                                width: 100,
                                sortable: true,
                                
                            },
							
			            ] ]		
					});
					history_total_user_datagrid.datagrid('unselectAll');
				  };
			 }
	            },'-',
//	            {
//	            text:'导出统计表文件',
//	            iconCls : 'icon-save',
//	            handler : function(){
//	            	$.messager.show({	
//						msg : '功能开发中....',
//						title : '提示'
//						});
//	            }
//	            },'-',
	            {
	            text:' 取消选中',
	            iconCls : 'icon-cancel',
	            handler : function(){
	           
	            //取消选中，更改内容回滚
	            	history_total_page_datagrid.datagrid('rejectChanges');
	            //取消被选择的号
	            	history_total_page_datagrid.datagrid('unselectAll');
	            }
	            }],
		
		});

  
/*************************************按栏目历史*************************/
  	
  
  	 $.ajax({
         cache: true,
         type: "POST",
         url:"./ydUserPage/VisitedHistoryByPage4chart.do",
         async: false,
        dataType: "json",
        data : $('#page_searchForm').serialize(),
         success : function(msg) {
      	   $("#history_total_bmodule_container").empty();
      	 console.log(msg);
						if (msg.rows) {
							var ids = [];
							for (var i = 0; i < msg.rows.length; i++) {
								var item =  msg.rows[i];
								var obj = [item.name , item.visitedTotalTimes];
                                ids.push(obj);
                            }
						var chart = anychart.pieChart( ids);
						chart.title('页面历史访问量统计');
	                    chart.container('history_total_bmodule_container').draw(); 					                        		
	                    
											                        		
							}else{
								console.log("返回值错误");
							} 
					},
					error : function(data) {
						console.log("ajax调取错误");
					}
       
     });//ajax  
  	history_total_bmodule_datagrid = $("#history_total_bmodule_datagrid").datagrid
 ({     
	    url:"./ydUserPage/getbomulHistory.do",
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
		columns : [ [ {
            title :'编号',
            field :'Id',
            width : 10 ,
            checkbox : true    
            },{
			title : '栏目名称',
			field : 'name',
			width : 80,
			fit : true
			
		},{
			title : '访问次数',
			field : 'visitedTotalTimes',
			width : 18,
			fit : true,
			sortable : true
		}
        ] ],
        toolbar:[{
            text:'详细情况',
            iconCls : 'icon-large-chart',
            handler : function(){
            var rows = history_total_bmodule_datagrid.datagrid('getSelections');
			if(rows.length >1) {
			var names = [];
			for(var i =0; i<rows.length; i++){
			names.push(rows[i].sid);
			}
			$.messager.show({	
			msg : '只能选择一条记录,您已经选了【'+names.join(",")+'】' + rows.length +'条记录',
			title : '提示'
			});
			} else if(rows.length == 0){
			$.messager.show({
			msg : '请选择一条记录',
			title : '提示'
			});
			}else if(rows.length == 1){
				$('#audit_DetailDialog').dialog({});
				audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
					url:'./ydUserPage/getDetailBybmodule.do?bmName='+ encodeURI(encodeURI(rows[0].name)),
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
				});
				history_total_bmodule_datagrid.datagrid('unselectAll');
			  };
		 }
            },'-',
//            {
//            text:'导出统计表文件',
//            iconCls : 'icon-save',
//            handler : function(){
//            	$.messager.show({	
//					msg : '功能开发中....',
//					title : '提示'
//					});
//            }
//            },'-',
            {
            text:' 取消选中',
            iconCls : 'icon-cancel',
            handler : function(){
           
            //取消选中，更改内容回滚
            	history_total_bmodule_datagrid.datagrid('rejectChanges');
            //取消被选择的号
            	history_total_bmodule_datagrid.datagrid('unselectAll');
            }
            }],
	
	});
	 
  	
/*************************************按用户类型历史*************************/ 	
	   
	   
  	searchPagesByConditions = function() {
	  		var s_data =  $('#page_searchForm').serialize();
	  	 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydUserPage/VisitedHistoryByPage4chart.do",
             async: false,
            dataType: "json",
            data :s_data,
             success : function(msg) {
          	   $("#history_total_page_container").empty();
          	 console.log(msg);
							if (msg.rows) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.pid , item.visitedTotalTimes];
                                    ids.push(obj);
                                }
								 var chart = anychart.columnChart();
								 chart.column(ids);
								 chart.title('页面历史访问量统计');
							chart.title('页面历史访问量统计');
		                    chart.container('history_total_page_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax 

	  	   $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydUserPage/getAllUserHistory4chart.do",
	             async: false,
	            dataType: "json",
	            data :s_data,
	             success : function(msg) {
	          	  
	          	   $("#history_total_user_container").empty();
								if (msg.rows) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.typeName , item.visiteTimes ];
	                                    ids.push(obj);
	                                }
									 var chart2 = anychart.columnChart();
								 chart2.column(ids);
								 chart2.title('页面历史访问量统计');
								chart2.xAxis().title().text('用户类型');
								chart2.yAxis().title().text('访问量');
			                    chart2.container('history_total_user_container').draw(); 					                        		
			                    
													                        		
									}else{
										console.log("返回值错误");
									} 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
	           
	         });//ajax
      

		   $.ajax({
	        cache: true,
	        type: "POST",
	        url:"./ydUserPage/getbomulHistory4chart.do",
	        async: false,
	       dataType: "json",
	       data :s_data,
	        success : function(msg) {
	     	  
	     	   $("#history_total_bmodule_container").empty();
							if (msg.rows) {
								
								
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = {x: item.name , value: item.visitedTotalTimes};
	                               ids.push(obj);
	                           }
								 var chart3 = anychart.columnChart();
	 							 chart3.column(ids);
	 							chart3.xAxis().title().text('栏目名称');
	 							chart3.yAxis().title().text('访问量');
	 							chart3.title('页面历史访问量统计');
	 		                    chart3.container('history_total_bmodule_container').draw(); 
				
								}else{
									console.log("返回值错误");
								}; 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
	      
	    });//ajax
		   history_total_page_datagrid.datagrid('load',
   					 FMG.serializeObject($('#page_searchForm').form())
   					); 
		   history_total_bmodule_datagrid.datagrid('load',
     					 FMG.serializeObject($('#page_searchForm').form())
     					);   
		   history_total_user_datagrid.datagrid('load',
     					 FMG.serializeObject($('#page_searchForm').form())
     					); 
			 
			};//searchUsersByConditions
			
			clearSearchPage = function() {
			FMG.clearForm($('#user_searchForm').form());
			  $.ajax({
		             cache: true,
		             type: "POST",
		             url:"./ydUserPage/VisitedHistoryByPage4chart.do",
		             async: false,
		            dataType: "json",
		             success : function(msg) {
		          	   $("#history_total_page_container").empty();
		          	 console.log(msg);
									if (msg.rows) {
										var ids = [];
										for (var i = 0; i < msg.rows.length; i++) {
											var item =  msg.rows[i];
											var obj = [item.pid , item.visitedTotalTimes];
		                                    ids.push(obj);
		                                }
										 var chart = anychart.columnChart();
										 chart.column(ids);
										 chart.title('页面历史访问量统计');
									chart.title('页面历史访问量统计');
				                    chart.container('history_total_page_container').draw(); 					                        		
				                    
														                        		
										}else{
											console.log("返回值错误");
										} 
								},
								error : function(data) {
									console.log("ajax调取错误");
								}
		           
		         });//ajax 
			

		  	   $.ajax({
		             cache: true,
		             type: "POST",
		             url:"./ydUserPage/getAllUserHistory4chart.do",
		             async: false,
		            dataType: "json",
		             success : function(msg) {
		          	  
		          	   $("#history_total_user_container").empty();
									if (msg.rows) {
										var ids = [];
										for (var i = 0; i < msg.rows.length; i++) {
											var item =  msg.rows[i];
											var obj = [item.typeName , item.visiteTimes ];
		                                    ids.push(obj);
		                                }
										 var chart2 = anychart.columnChart();
									 chart2.column(ids);
									 chart2.title('页面历史访问量统计');
									chart2.xAxis().title().text('用户类型');
									chart2.yAxis().title().text('访问量');
				                    chart2.container('history_total_user_container').draw(); 					                        		
				                    
														                        		
										}else{
											console.log("返回值错误");
										} 
								},
								error : function(data) {
									console.log("ajax调取错误");
								}
		           
		         });//ajax
	      

			   $.ajax({
		        cache: true,
		        type: "POST",
		        url:"./ydUserPage/getbomulHistory4chart.do",
		        async: false,
		       dataType: "json",
		        success : function(msg) {
		     	  
		     	   $("#history_total_bmodule_container").empty();
								if (msg.rows) {
									
									
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = {x: item.name , value: item.visitedTotalTimes};
		                               ids.push(obj);
		                           }
									 var chart3 = anychart.columnChart();
		 							 chart3.column(ids);
		 							chart3.title('页面历史访问量统计');
		 							chart3.xAxis().title().text('栏目名称');
		 							chart3.yAxis().title().text('访问量');
		 		                    chart3.container('history_total_bmodule_container').draw(); 
					
									}else{
										console.log("返回值错误");
									}; 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
		      
		    });//ajax
			   history_total_page_datagrid.datagrid('load',{}); 
			   history_total_bmodule_datagrid.datagrid('load',{}); 
			   history_total_user_datagrid.datagrid('load',{}); 

		};//clearSearchUser
			});