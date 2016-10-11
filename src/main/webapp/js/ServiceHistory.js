var history_total_serviceName_datagrid;
	var history_total_serviceType_datagrid;
	var history_total_user_datagrid;
	anychart.onDocumentLoad(function() {

	  	   $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydVisiterService/VisitedHistoryByServices4chart.do",
	             async: false,
	            dataType: "json",
	            data : $('#user_searchForm').serialize(),
	             success : function(msg) {
	          	   $("#history_total_serviceName_container").empty();
	          	 console.log(msg);
								if (msg.items) {
									var ids = [];
									for (var i = 0; i < msg.items.length; i++) {
										var item =  msg.items[i];
										var obj = [item.sId , item.visiteTotalTime];
	                                    ids.push(obj);
	                                }
								var chart = anychart.pieChart( ids);
								chart.title('累计服务访问数统计');
			                    chart.container('history_total_serviceName_container').draw(); 					                        		
			                    
													                        		
									}else{
										console.log("返回值错误");
									} 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
	           
	         });//ajax  
	  	 history_total_serviceName_datagrid = $("#history_total_serviceName_datagrid").datagrid
         ({     
        	    url:"./ydVisiterService/SVisitedHistory.do",
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
				idField : 'Id',
				sortName : '',
				sortOrder : 'desc',
				columns : [ [ {
		            title :'编号',
		            field :'Id',
		            width : 10 ,
		            checkbox : true    
		            },{
					title : '服务编号',
					field : 'sId',
					width : 80,
					fit : true
				},{
					title : '访问次数',
					field : 'visiteTotalTime',
					width : 18,
					fit : true,
					sortable : true
				}
	            ] ],
	            toolbar:[{
		            text:'详细情况',
		            iconCls : 'icon-large-chart',
		            handler : function(){
		            var rows = history_total_serviceName_datagrid.datagrid('getSelections');
					if(rows.length >1) {
					var names = [];
					for(var i =0; i<rows.length; i++){
					names.push(rows[i].sid);
					}
					$.messager.show({	
					msg : '只能选择一条记录,您已经选了【'+names.join(",")+'】' + rows.length +'条',
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
							url:'./ydVisiterService/getDetail4SVisitedHistorybysid.do?sId='+rows[0].sId,
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
	                                width: 150,
	                              
	                                
	                            },{
	                                title: '访问ip',
	                                field: 'visiteIp',
	                                width: 100,
	                              
	                                
	                            }, {
	                                title: '访问时间',
	                                field: 'visisteDate',
	                                
	                                width: 100,
	                                sortable: true,
	                                
	                            },
								
				            ] ]		
						});
						history_total_serviceName_datagrid.datagrid('unselectAll');
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
		            history_total_serviceName_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            history_total_serviceName_datagrid.datagrid('unselectAll');
		            }
		            }],
			
			});
	
/*************************************历史服务类型*************************/
	  	
	
  	   $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiterService/VisitedHistoryBySType4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
          	  
          	   $("#history_total_serviceType_container").empty();
							if (msg.rows) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var obj = [msg.rows[i].name , msg.rows[i].visitedTotalTimes ];
                                    ids.push(obj);
                                }
								 var chart2 = anychart.columnChart();
							 chart2.column(ids);
							 chart2.title('累计服务访问数统计');
							chart2.xAxis().title().text('服务类型');
							chart2.yAxis().title().text('访问量');
		                    chart2.container('history_total_serviceType_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
  	   
  	 history_total_serviceType_datagrid = $(
					"#history_total_serviceType_datagrid").datagrid({
							url : './ydVisiterService/VisitedHistoryBySType.do',
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
							idField : 'Id',
							sortName : '',
							sortOrder : 'desc',
							columns : [ [ {
					            title :'编号',
					            field :'Id',
					            width : 10 ,
					            checkbox : true    
					            },{
								title : '服务类型',
								field : 'name',
								width : 80,
								fit : true,
								
							},{
								title : '访问次数',
								field : 'visitedTotalTimes',
								width : 18,
								fit : true,
								sortable : true,
							}
				            ] ],
				            toolbar:[{
					            text:'详细情况',
					            iconCls : 'icon-large-chart',
					            handler : function(){
					            var rows = history_total_serviceType_datagrid.datagrid('getSelections');
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
										url:'./ydVisiterService/getDetailBysType4history.do?tName='+encodeURI(encodeURI(rows[0].name)),
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
									history_total_serviceType_datagrid.datagrid('unselectAll');
								  };
							 }
					            },'-',
//					            {
//					            text:'导出统计表文件',
//					            iconCls : 'icon-save',
//					            handler : function(){
//					            	$.messager.show({	
//										msg : '功能开发中....',
//										title : '提示'
//										});
//					            }
//					            },'-',
					            {
					            text:' 取消选中',
					            iconCls : 'icon-cancel',
					            handler : function(){
					            FMG.editeRow = undefined;
					            //取消选中，更改内容回滚
					            history_total_serviceType_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            history_total_serviceType_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});

  
/*************************************按服务名称历史*************************/
  	
  
	   $.ajax({
        cache: true,
        type: "POST",
        url:"./ydVisiterService/VisitedHistoryByUsers4chart.do",
        async: false,
       dataType: "json",
       contentType : "application/json",
        success : function(msg) {
     	  
     	   $("#history_total_user_container").empty();
						if (msg.rows) {
							
							
							var ids = [];
							for (var i = 0; i < msg.rows.length; i++) {
								var item =  msg.rows[i];
								var obj = {x: item.typeName , value: item.visiteTimes};
                               ids.push(obj);
                           }
							 var chart3 = anychart.columnChart();
 							 chart3.column(ids);
 							chart3.title('累计服务访问数统计');
 							chart3.xAxis().title().text('用户类型');
 							chart3.yAxis().title().text('访问量');
 		                    chart3.container('history_total_user_container').draw(); 
			
							}else{
								console.log("返回值错误");
							}; 
					},
					error : function(data) {
						console.log("ajax调取错误");
					}
      
    });//ajax
	     

	   history_total_user_datagrid = $(
				"#history_total_user_datagrid").datagrid({
					url : './ydVisiterService/VisitedHistoryByUsers.do',
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
			            width : 20 ,
			           
			            },{
						title : '用户类型',
						field : 'typeName',
						width : 80,
						fit : true,
					},{
						title : '访问次数',
						field : 'visiteTimes',
						width : 18,
						fit : true,
						sortable : true,
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
								url:'./ydVisiterService/getDetailByuid4history.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
		                                title: '访问服务',
		                                field: 'service',
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
//			            {
//			            text:'导出统计表文件',
//			            iconCls : 'icon-save',
//			            handler : function(){
//			            	$.messager.show({	
//								msg : '功能开发中....',
//								title : '提示'
//								});
//			            }
//			            },'-',
			            {
			            text:' 取消选中',
			            iconCls : 'icon-cancel',
			            handler : function(){
			            FMG.editeRow = undefined;
			            //取消选中，更改内容回滚
			            history_total_user_datagrid.datagrid('rejectChanges');
			            //取消被选择的号
			            history_total_user_datagrid.datagrid('unselectAll');
			            }
			            }],

		});
	 
  	
/*************************************按用户类型历史*************************/ 	
	   
	   
	   searchServicesByConditions = function() {
	  		var s_data =  $('#services_searchForm').serialize();
	  	  $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydVisiterService/VisitedHistoryByServices4chart.do",
	             async: false,
	            dataType: "json",
	            data :s_data,
	             success : function(msg) {
	          	   $("#history_total_serviceName_container").empty();
	          	 console.log(msg);
								if (msg.items) {
									var ids = [];
									for (var i = 0; i < msg.items.length; i++) {
										var item =  msg.items[i];
										var obj = [item.sId , item.visiteTotalTime];
	                                    ids.push(obj);
	                                }
								var chart = anychart.pieChart( ids);
								chart.title('累计服务访问数统计');
			                    chart.container('history_total_serviceName_container').draw(); 					                        		
			                    
													                        		
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
	             url:"./ydVisiterService/VisitedHistoryBySType4chart.do",
	             async: false,
	            dataType: "json",
	            data :s_data,
	             success : function(msg) {
	          	  
	          	   $("#history_total_serviceType_container").empty();
								if (msg.rows) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.name , item.visitedTotalTimes ];
	                                    ids.push(obj);
	                                }
									 var chart2 = anychart.columnChart();
								 chart2.column(ids);
								chart2.xAxis().title().text('服务级别');
								chart2.yAxis().title().text('访问量');
								chart2.title('累计服务访问数统计');
			                    chart2.container('history_total_serviceType_container').draw(); 					                        		
			                    
													                        		
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
	        url:"./ydVisiterService/VisitedHistoryByUsers4chart.do",
	        async: false,
	       dataType: "json",
	       data :s_data,
	        success : function(msg) {
	     	  
	     	   $("#history_total_user_container").empty();
							if (msg.rows) {
								
								
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = {x: item.uid , value: item.visiteTimes};
	                               ids.push(obj);
	                           }
								 var chart3 = anychart.columnChart();
	 							 chart3.column(ids);
	 							chart3.title('累计服务访问数统计');
	 							chart3.xAxis().title().text('用户标识');
	 							chart3.yAxis().title().text('访问量');
	 		                    chart3.container('history_total_user_container').draw(); 
				
								}else{
									console.log("返回值错误");
								}; 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
	      
	    });//ajax
			 history_total_serviceName_datagrid.datagrid('load',
   					 FMG.serializeObject($('#services_searchForm').form())
   					); 
			   history_total_serviceType_datagrid.datagrid('load',
     					 FMG.serializeObject($('#services_searchForm').form())
     					);   
			   history_total_user_datagrid.datagrid('load',
     					 FMG.serializeObject($('#services_searchForm').form())
     					); 
			 
			};//searchUsersByConditions
			
			clearSearchService = function() {
			FMG.clearForm($('#services_searchForm').form());
			  $.ajax({
		             cache: true,
		             type: "POST",
		             url:"./ydVisiterService/VisitedHistoryByServices4chart.do",
		             async: false,
		            dataType: "json",
		             success : function(msg) {
		          	   $("#history_total_serviceName_container").empty();
		          	 console.log(msg);
									if (msg.items) {
										var ids = [];
										for (var i = 0; i < msg.items.length; i++) {
											var item =  msg.items[i];
											var obj = [item.sId , item.visiteTotalTime];
		                                    ids.push(obj);
		                                }
									var chart = anychart.pieChart( ids);
									chart.title('累计服务访问数统计');
				                    chart.container('history_total_serviceName_container').draw(); 					                        		
				                    
														                        		
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
		             url:"./ydVisiterService/VisitedHistoryBySType4chart.do",
		             async: false,
		            dataType: "json",
		             success : function(msg) {
		          	  
		          	   $("#history_total_serviceType_container").empty();
									if (msg.rows) {
										var ids = [];
										for (var i = 0; i < msg.rows.length; i++) {
											var item =  msg.rows[i];
											var obj = [item.name , item.visitedTotalTimes ];
		                                    ids.push(obj);
		                                }
										 var chart2 = anychart.columnChart();
									 chart2.column(ids);
									 chart2.title('累计服务访问数统计');
									chart2.xAxis().title().text('服务级别');
									chart2.yAxis().title().text('访问量');
				                    chart2.container('history_total_serviceType_container').draw(); 					                        		
				                    
														                        		
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
		        url:"./ydVisiterService/VisitedHistoryByUsers4chart.do",
		        async: false,
		       dataType: "json",
		        success : function(msg) {
		     	  
		     	   $("#history_total_user_container").empty();
								if (msg.rows) {
									
									
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = {x: item.uid , value: item.visiteTimes};
		                               ids.push(obj);
		                           }
									 var chart3 = anychart.columnChart();
		 							 chart3.column(ids);
		 							chart3.title('累计服务访问数统计');
		 							chart3.xAxis().title().text('用户标识');
		 							chart3.yAxis().title().text('访问量');
		 		                    chart3.container('history_total_user_container').draw(); 
					
									}else{
										console.log("返回值错误");
									}; 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
		      
		    });//ajax
		   history_total_serviceName_datagrid.datagrid('load',{}); 
		   history_total_serviceType_datagrid.datagrid('load',{}); 
		   history_total_user_datagrid.datagrid('load',{}); 

		};//clearSearchUser
			});