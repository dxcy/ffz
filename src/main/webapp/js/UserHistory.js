var history_total_userType_datagrid;
var userServiceDetail_datagrid;
var userDataDetail_datagrid;
var userPageDetail_datagrid;
var uuType;
	anychart.onDocumentLoad(function() {

		 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiter/getUserTypeHistory4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
							if (msg && msg.success == true) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.typeName , item.visiteTimes,item.downloadTimes,item.visitePageTimes];
                                    ids.push(obj);
                                }
							$("#history_total_userType_container").empty();
							
							var data = anychart.data.set( ids);
							var visiteTime = data.mapAs({x: [0], value: [1]});
							var downloadTime = data.mapAs({x: [0], value: [2]});
							var visitePageTime = data.mapAs({x: [0], value: [3]});
							 // create column chart
							  chart = anychart.column();
							  // turn on chart animation
							  chart.animation(true);
							  // set chart title text settings
							  chart.title('用户访问历史动态'); 
							  // set titles for Y-axis
							  chart.xAxis().title().text('用户类型'); chart.yAxis().title().text('访问量');
							  // helper function to setup label settings for all series
							  var setupSeriesLabels = function(series, name) {
							    series.name(name);
							    series.tooltip().titleFormatter(function () {
							      return this.x;
							    });
							  
							  };

							  // temp variable to store series instance
							  var series;	 
							// create first series with mapped data
							  series = chart.column(visiteTime);
							  setupSeriesLabels(series, '服务访问次数');

							  // create second series with mapped data
							  series = chart.column(downloadTime);
							  setupSeriesLabels(series, '数据下载次数');

							  // create third series with mapped data
							  series = chart.column(visitePageTime);
							  setupSeriesLabels(series, '页面访问次数');
							  // turn on legend
							  chart.legend().enabled(true).fontSize(13).padding([0,0,5,0]);

							 
							 

							  chart.grid();
							  chart.grid(1).layout('vertical');

							 
							 

							  // initiate chart drawing
							  chart.container('history_total_userType_container').draw();
									           				                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
	
		
		 history_total_userType_datagrid = $("#history_total_userType_datagrid").datagrid({
			url : './ydVisiter/getUserTypeHistory.do',
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
				title : '用户类型',
				field : 'typeName',
				width : 100,
				fit : true,
			},{
				title : '数据下载次数',
				field : 'downloadTimes',
				width : 30,
				sortable : true
			},{
				title : '服务访问次数',
				field : 'visiteTimes',
				width : 30,
				sortable : true,
			},{
				title : '页面访问次数',
				field : 'visitePageTimes',
				width : 30,
				sortable : true,
			}
            ] ], toolbar:[{
	            text:'详细情况',
	            iconCls : 'icon-large-chart',
	            handler : function(){
	            var rows = $("#history_total_userType_datagrid").datagrid('getSelections');
				if(rows.length >1) {
				var names = [];
				for(var i =0; i<rows.length; i++){
				names.push(rows[i].typeName);
				}
				$.messager.show({	
				msg : '只能选择一个服务进行查看或者导出,您已经选了【'+names.join(",")+'】' + rows.length +'个服务',
				title : '提示'
				});
				} else if(rows.length == 0){
				$.messager.show({
				msg : '请选择一个服务进行查看',
				title : '提示'
				});
				}else if(rows.length == 1){
					uuType = rows[0].typeName;
				 $('#audit_DetailDialog').dialog({
						});
				 $('#userDetail_tt').tabs({
					 border:false,  
					     selected: 0,  
					      onSelect:function(title,index){    
					        if(index==0){
					     	 
					        	userServiceDetail_datagrid = $("#userServiceDetail_datagrid").datagrid
					                               ({
					                             	url : './ydVisiterService/getUserServiceDetailByUid.do?uid='+encodeURI(encodeURI(uuType)),
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
					 						             
					 						            },{
					 									title : '用户编号',
					 									field : 'uid',
					 									width : 40,
					 									fit : true,
					 								},{
					 									title : '访问服务',
					 									field : 'service',
					 									width : 40,
					 									fit : true,
					 									sortable : true,
					 								},{
					 									title : '用户ip',
					 									field : 'uip',
					 									width : 100,
					 									fit : true,
					 									sortable : true,
					 								},{
					 									title : '访问时间',
					 									field : 'visitedTime',
					 									width : 100,
					 									fit : true,
					 									sortable : true,
					 								}
					 					            ] ]
					 							});
					        
					        }else if(index==1){//本年度按数据价格
					     	  
					        	userDataDetail_datagrid = $("#userDataDetail_datagrid").datagrid({
					 							url : './ydVisiterData/getUserDataDetailByUid.do?uid='+encodeURI(encodeURI(uuType)),
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
				 						            
				 						            },{
				 									title : '用户编号',
				 									field : 'uid',
				 									width : 50,
				 									fit : true,
				 								},{
				 									title : '下载数据',
				 									field : 'data',
				 									width : 50,
				 									fit : true,
				 									sortable : true,
				 								},{
				 									title : '用户ip',
				 									field : 'uip',
				 									width : 100,
				 									fit : true,
				 									sortable : true,
				 								},{
				 									title : '下载时间',
				 									field : 'downloadDate',
				 									width : 100,
				 									fit : true,
				 									sortable : true,
				 								}
				 					            ] ]
					 						
					 						});

					 			
					        
					        }  else if(index==2){//本年度供应商
					     	  
					 			/**
					 			 * 中部表格
					 			 */
					        	userPageDetail_datagrid = $(
					 					"#userPageDetail_datagrid").datagrid({
					 						url : './ydUserPage/getUserPageDetailByUid.do?uid='+encodeURI(encodeURI(uuType)),
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
			 						         
			 						            },{
			 									title : '用户编号',
			 									field : 'uid',
			 									width : 50,
			 									fit : true,
			 								},{
			 									title : '访问页面',
			 									field : 'page',
			 									width : 50,
			 									fit : true,
			 									sortable : true,
			 								},{
			 									title : '用户ip',
			 									field : 'uip',
			 									width : 100,
			 									fit : true,
			 									sortable : true,
			 								},{
			 									title : '访问时间',
			 									field : 'visitedTime',
			 									width : 100,
			 									fit : true,
			 									sortable : true,
			 								}
			 					            ] ]
					 			});
					        }
					     }
					 });

						
						$('#history_total_userType_datagrid').datagrid('unselectAll');
				  };
			 }
	            }]	
           
		
		});//audit_service_datagrid
	
	  	

  	
/*************************************按用户类型历史*************************/ 	
	   
	   
	  	searchUsersByConditions = function() {
	  		var s_data =  $('#user_searchForm').serialize();

			 $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydVisiter/getUserTypeHistory4chart.do",
	             async: false,
	            dataType: "json",
	            data : s_data,
	             success : function(msg) {
								if (msg && msg.success == true) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.uid , item.visiteTimes,item.downloadTimes,item.visitePageTimes];
	                                    ids.push(obj);
	                                }
								$("#history_total_userType_container").empty();
								
								var data = anychart.data.set( ids);
								var visiteTime = data.mapAs({x: [0], value: [1]});
								var downloadTime = data.mapAs({x: [0], value: [2]});
								var visitePageTime = data.mapAs({x: [0], value: [3]});
								  // chart type
								  var chart = anychart.columnChart();

								  // set title
								  chart.title().text('用户访问历史动态');

								  // set series data
								  chart.column(visiteTime);
								  chart.column(downloadTime);
								  chart.column(visitePageTime);

								  // set axes titles
								  chart.xAxis().title().text('用户访问');
								  chart.yAxis().title().text('次数累计');

								  // draw
								  chart.container('history_total_userType_container').draw();
					           				                        		
									}else{
										console.log("返回值错误");
									} 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
	           
	         });//ajax
	  	

	  
		
	  	history_total_userType_datagrid.datagrid('load',
   					 FMG.serializeObject($('#user_searchForm').form())
   					); 
	  	
			 
			};//searchUsersByConditions
			
		clearSearchUser = function() {
			FMG.clearForm($('#data_searchForm').form());
			 
			 $.ajax({
	             cache: true,
	             type: "POST",
	             url:"./ydVisiter/getUserTypeHistory4chart.do",
	             async: false,
	            dataType: "json",
	             success : function(msg) {
								if (msg && msg.success == true) {
									var ids = [];
									for (var i = 0; i < msg.rows.length; i++) {
										var item =  msg.rows[i];
										var obj = [item.uid , item.visiteTimes,item.downloadTimes,item.visitePageTimes];
	                                    ids.push(obj);
	                                }
								$("#history_total_userType_container").empty();
								
								var data = anychart.data.set( ids);
								var visiteTime = data.mapAs({x: [0], value: [1]});
								var downloadTime = data.mapAs({x: [0], value: [2]});
								var visitePageTime = data.mapAs({x: [0], value: [3]});
								  // chart type
								  var chart = anychart.columnChart();

								  // set title
								  chart.title().text('用户访问历史动态');

								  // set series data
								  chart.column(visiteTime);
								  chart.column(downloadTime);
								  chart.column(visitePageTime);

								  // set axes titles
								  chart.xAxis().title().text('用户访问');
								  chart.yAxis().title().text('次数累计');

								  // draw
								  chart.container('history_total_userType_container').draw();
					           				                        		
									}else{
										console.log("返回值错误");
									} 
							},
							error : function(data) {
								console.log("ajax调取错误");
							}
	           
	         });//ajax
			
		  	history_total_userType_datagrid.datagrid('load',{}); 
		

		};//clearSearchUser
			});