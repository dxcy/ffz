var history_total_serviceName_datagrid;
	var history_total_serviceType_datagrid;
	var history_provider_datagrid;
	var history_total_dataprice_datagrid;
	anychart.onDocumentLoad(function() {

	  	   $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydVisiterData/HistoryByprovider4chart.do",
	             async: false,
	            dataType: "json",
	            data : $('#user_searchForm').serialize(),
	             success : function(msg) {
	          	   $("#history_provider_container").empty();
	          	 console.log(msg);
								if (msg.rows) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.Provider , item.downloadTotalTime];
	                                    ids.push(obj);
	                                }
								var chart = anychart.pieChart( ids);
								chart.title('数据历史下载量统计');
			                    chart.container('history_provider_container').draw(); 					                        		
			                    
													                        		
									}else{
										console.log("返回值错误");
									} 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
	           
	         });//ajax  
	  	 history_provider_datagrid = $("#history_provider_datagrid").datagrid
         ({     
        	    url:"./ydVisiterData/VisitedHistoryByProvider.do",
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
					title : '供应商',
					field : 'provider',
					width : 80,
					fit : true
				},{
					title : '下载次数',
					field : 'downloadTotalTime',
					width : 18,
					fit : true,
					sortable : true
				}
	            ] ],
	            toolbar:[{
		            text:'详细情况',
		            iconCls : 'icon-large-chart',
		            handler : function(){
		            var rows = history_provider_datagrid.datagrid('getSelections');
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
							url:'./ydVisiterData/getDetailByProvider4history.do?uid='+rows[0].provider,
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
	                                title: '下载数据',
	                                field: 'data',
	                                width: 100,
	                                sortable: true,
	                                
	                            }, {
	                                title: '访问时间',
	                                field: 'downloadDate',
	                                
	                                width: 100,
	                                sortable: true,
	                                
	                            },
								
				            ] ]		
						});
						history_provider_datagrid.datagrid('unselectAll');
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
		            	history_provider_datagrid.datagrid('rejectChanges');
		            //取消被选择的号
		            	history_provider_datagrid.datagrid('unselectAll');
		            }
		            }],
			
			});
	
/*************************************历史服务类型*************************/
	  	
	
  	   $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiterData/HistoryByprice4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
          	  
          	   $("#history_total_dataprice_container").empty();
							if (msg.rows) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.price , item.downloadTotalTime ];
                                    ids.push(obj);
                                }
								 var chart2 = anychart.columnChart();
							 chart2.column(ids);
							chart2.xAxis().title().text('数据价格');
							chart2.yAxis().title().text('下载量');
							chart2.title('数据历史下载量统计');
		                    chart2.container('history_total_dataprice_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
  	 
  	 history_total_dataprice_datagrid = $(
					"#history_total_dataprice_datagrid").datagrid({
							url : './ydVisiterData/HistoryByprice.do',
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
								title : '数据价格',
								field : 'price',
								width : 80,
								fit : true,
								
							},{
								title : '访问次数',
								field : 'downloadTotalTime',
								width : 18,
								fit : true,
								sortable : true,
							}
				            ] ],
				            toolbar:[{
					            text:'详细情况',
					            iconCls : 'icon-large-chart',
					            handler : function(){
					            var rows = history_total_dataprice_datagrid.datagrid('getSelections');
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
										url:'./ydVisiterData/getDetailByPrice4history.do?price='+rows[0].price,
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
				                                title: '下载数据',
				                                field: 'data',
				                                width: 100,
				                                sortable: true,
				                                
				                            },{
				                                title: '访问时间',
				                                field: 'downloadDate',
				                                
				                                width: 100,
				                                sortable: true,
				                                
				                            },
											
							            ] ]		
									});
									history_total_dataprice_datagrid.datagrid('unselectAll');
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
					            history_total_dataprice_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            history_total_dataprice_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});

  
/*************************************按数据价格历史*************************/
  	//数据类型
  
	   $.ajax({
        cache: true,
        type: "POST",
        url:"./ydVisiterData/HistoryBydataType4chart.do",
        async: false,
       dataType: "json",
       contentType : "application/json",
        success : function(msg) {
     	  
     	   $("#history_total_dataType_container").empty();
						if (msg.rows) {
							
							
							var ids = [];
							for (var i = 0; i < msg.rows.length; i++) {
								var item =  msg.rows[i];
								var obj = {x: item.typeName , value: item.downloadTotalTime};
                               ids.push(obj);
                           }
							 var chart3 = anychart.columnChart();
 							 chart3.column(ids);
 							chart3.title('数据历史下载量统计');
 							chart3.xAxis().title().text('数据类型');
 							chart3.yAxis().title().text('下载量');
 		                    chart3.container('history_total_dataType_container').draw(); 
			
							}else{
								console.log("返回值错误");
							}; 
					},
					error : function(data) {
						console.log("ajax调取错误");
					}
      
    });//ajax
	     

	   history_total_dataType_datagrid = $(
				"#history_total_dataType_datagrid").datagrid({
					url : './ydVisiterData/HistoryBydataType.do',
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
			            checkbox : true 
			            },{
						title : '数据类型',
						field : 'typeName',
						width : 80,
						fit : true,
					},{
						title : '下载次数',
						field : 'downloadTotalTime',
						width : 18,
						fit : true,
						sortable : true,
					}
		            ] ],
		            toolbar:[{
			            text:'详细情况',
			            iconCls : 'icon-large-chart',
			            handler : function(){
			            var rows = history_total_dataType_datagrid.datagrid('getSelections');
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
								url:'./ydVisiterData/getDetailBydataType4history.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
		                                title: '下载数据',
		                                field: 'data',
		                                width: 100,
		                                sortable: true,
		                                
		                            }, {
		                                title: '下载时间',
		                                field: 'downloadDate',
		                                
		                                width: 100,
		                                sortable: true,
		                                
		                            },
									
					            ] ]		
							});
							history_total_dataType_datagrid.datagrid('unselectAll');
							
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
			            history_total_dataType_datagrid.datagrid('rejectChanges');
			            //取消被选择的号
			            history_total_dataType_datagrid.datagrid('unselectAll');
			            }
			            }],

		});
	 
  	
/*************************************按用户类型历史*************************/ 	
	   
	   
	  	searchUsersByConditions = function() {
	  		var s_data =  $('#data_searchForm').serialize();
	  	  $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydVisiterData/HistoryByprovider4chart.do",
	             async: false,
	            dataType: "json",
	            data : s_data,
	             success : function(msg) {
	          	   $("#history_provider_container").empty();
	          	 console.log(msg);
								if (msg.rows) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.Provider , item.downloadTotalTime];
	                                    ids.push(obj);
	                                }
								var chart = anychart.pieChart( ids);
								chart.title('数据历史下载量统计');
			                    chart.container('history_provider_container').draw(); 					                        		
			                    
													                        		
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
             url:"./ydVisiterData/HistoryByprice4chart.do",
             async: false,
            dataType: "json",
            data : s_data,
             success : function(msg) {
          	  
          	   $("#history_total_dataprice_container").empty();
							if (msg.rows) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.price , item.downloadTotalTime ];
                                    ids.push(obj);
                                }
								 var chart2 = anychart.columnChart();
							 chart2.column(ids);
							 chart2.title('数据历史下载量统计');
							chart2.xAxis().title().text('数据价格');
							chart2.yAxis().title().text('下载量');
		                    chart2.container('history_total_dataprice_container').draw(); 					                        		
		                    
												                        		
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
             url:"./ydVisiterData/HistoryBydataType4chart.do",
             async: false,
            dataType: "json",
            data : s_data,
             success : function(msg) {
          	  
          	   $("#history_total_dataType_container").empty();
							if (msg.rows) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = {x: item.typeName , value: item.downloadTotalTime };
                                    ids.push(obj);
                                }
								 var chart2 = anychart.columnChart();
							 chart2.column(ids);
							 chart2.title('数据历史下载量统计');
	   							chart2.xAxis().title().text('数据类型');
	   							chart2.yAxis().title().text('下载量');
		                    chart2.container('history_total_dataType_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax	
	  	history_provider_datagrid.datagrid('load',
   					 FMG.serializeObject($('#data_searchForm').form())
   					); 
	  	history_total_dataprice_datagrid.datagrid('load',
     					 FMG.serializeObject($('#data_searchForm').form())
     					);   
	  	history_total_dataType_datagrid.datagrid('load',
     					 FMG.serializeObject($('#data_searchForm').form())
     					); 
			 
			};//searchUsersByConditions
			
		clearSearchUser = function() {
			FMG.clearForm($('#data_searchForm').form());
			  $.ajax({
		             cache: true,
		             type: "POST",
		             url:"./ydVisiterData/HistoryByprovider4chart.do",
		             async: false,
		            dataType: "json",
		           
		             success : function(msg) {
		          	   $("#history_provider_container").empty();
		          	 console.log(msg);
									if (msg.rows) {
										var ids = [];
										for (var i = 0; i < msg.rows.length; i++) {
											var item =  msg.rows[i];
											var obj = [item.Provider , item.downloadTotalTime];
		                                    ids.push(obj);
		                                }
									var chart = anychart.pieChart( ids);
									chart.title('数据历史下载量统计');
				                    chart.container('history_provider_container').draw(); 					                        		
				                    
														                        		
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
	             url:"./ydVisiterData/HistoryByprice4chart.do",
	             async: false,
	            dataType: "json",
	           
	             success : function(msg) {
	          	  
	          	   $("#history_total_dataprice_container").empty();
								if (msg.rows) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.price , item.downloadTotalTime ];
	                                    ids.push(obj);
	                                }
									 var chart2 = anychart.columnChart();
								 chart2.column(ids);
								chart2.xAxis().title().text('数据价格');
								chart2.yAxis().title().text('下载量');
								chart2.title('数据历史下载量统计');
			                    chart2.container('history_total_dataprice_container').draw(); 					                        		
			                    
													                        		
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
		          url:"./ydVisiterData/HistoryBydataType4chart.do",
		          async: false,
		         dataType: "json",
		         contentType : "application/json",
		          success : function(msg) {
		       	  
		       	   $("#history_total_dataType_container").empty();
		  						if (msg.rows) {
		  							
		  							
		  							var ids = [];
		  							for (var i = 0; i < msg.rows.length; i++) {
		  								var item =  msg.rows[i];
		  								var obj = {x: item.typeName , value: item.downloadTotalTime};
		                                 ids.push(obj);
		                             }
		  							 var chart3 = anychart.columnChart();
		   							 chart3.column(ids);
		   							chart3.xAxis().title().text('数据类型');
		   							chart3.yAxis().title().text('下载量');
		   							chart3.title('数据历史下载量统计');
		   		                    chart3.container('history_total_dataType_container').draw(); 
		  			
		  							}else{
		  								console.log("返回值错误");
		  							}; 
		  					},
		  					error : function(data) {
		  						console.log("ajax调取错误");
		  					}
		        
		      });//ajax
			
		  	history_provider_datagrid.datagrid('load',{}); 
		  	history_total_dataprice_datagrid.datagrid('load',{}); 
		  	history_total_dataType_datagrid.datagrid('load',{}); 

		};//clearSearchUser
			});