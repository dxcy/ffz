/**
 * 
 */
/**
	 * 用户访问量实时动态
	 */
	$(function() {
		var uuid;
		 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiter/findAllUserType4chart.do",
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
							$("#container").empty();
							
							var data = anychart.data.set( ids);
							var visiteTime = data.mapAs({x: [0], value: [1]});
							var downloadTime = data.mapAs({x: [0], value: [2]});
							var visitePageTime = data.mapAs({x: [0], value: [3]});
							  // chart type
							  var chart = anychart.column();
							  // turn on chart animation
							  chart.animation(true);
							  chart.title().padding([0,0,5,0]);
							  // set title
							  chart.title().text('用户访问最新动态');
							  // set axes titles
							  chart.xAxis().title().text('用户类型');
							  chart.yAxis().title().text('次数累计');
							  chart.xAxis().stroke(null);
							  chart.xAxis(1).stroke(null).orientation('top');
							

							  var zeroMarker = chart.lineMarker(0);
							  zeroMarker.stroke("#ddd");
							  zeroMarker.scale(chart.yScale());
							  zeroMarker.value(0);
							  // helper function to setup label settings for all series
							  var setupSeries = function(series, name) {
							    series.name(name);
							    series.labels().enabled(true);
							    series.tooltip().title(false);
							    series.tooltip().separator(false);
							   
							  };
							  var series;
							  // create first series with mapped data
							  series = chart.column(visiteTime);
							  setupSeries(series, '访问服务次数');

							  // create second series with mapped data
							  series = chart.column(downloadTime);
							  setupSeries(series, '下载数据次数');

							  // create third series with mapped data
							  series = chart.column(visitePageTime);
							  setupSeries(series, '访问页面次数');
							  // turn on legend
							  chart.legend().enabled(true).fontSize(13).padding([0,0,20,0]);

							  // turn on grids
							  var grid = chart.grid();
							  grid.enabled(true).stroke("#ddd");
							  grid.drawLastLine(false);
							  grid.layout("vertical");

							  // tune column paddings
							  chart.barsPadding(0.1);
							  chart.barGroupsPadding(0.9);
							  // draw
							  chart.container('container').draw();
				           				                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
		 
		 audite_datagrid = $("#audite_datagrid").datagrid({
			url : './ydVisiter/findAllUserType.do',
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
	            var rows = $("#audite_datagrid").datagrid('getSelections');
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
					 uuid = rows[0].typeName;
			        	console.log("uid="+uuid); 
				 $('#audit_DetailDialog').dialog({
						});
				 $('#userDetail_tt').tabs({
					 border:false,  
					     selected: 0,  
					      onSelect:function(title,index){   
					    	 
					        if(index==0){
					        	
					        	userServiceDetail_datagrid = $("#userServiceDetail_datagrid").datagrid
					                               ({
					                             	url : './ydVisiter/getServiceDetailByUid.do?uid='+encodeURI(encodeURI(uuid)),
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
					 						            width : 10 
					 						                
					 						            },{
					 									title : '用户编号',
					 									field : 'uid',
					 									width : 100,
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
					 									width : 50,
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
					 							url : './ydVisiter/getDataDetailByUid.do?uid='+encodeURI(encodeURI(uuid)),
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
				 						            width : 10 
				 						           
				 						            },{
				 									title : '用户编号',
				 									field : 'uid',
				 									width : 100,
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
				 									width : 50,
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
					 						url : './ydVisiter/getPageDetailByUid.do?uid='+encodeURI(encodeURI(uuid)),
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
			 						            width : 10 
			 						            
			 						            },{
			 									title : '用户编号',
			 									field : 'uid',
			 									width : 100,
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
			 									width : 50,
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

						
						$('#audite_datagrid').datagrid('unselectAll');
				  };
			 }
	            }, {
		            text:' 取消选中',
		            iconCls : 'icon-cancel',
		            handler : function(){
		            //取消被选择的号
		            	audite_datagrid.datagrid('unselectAll');
		            }
		            }]	
           
		
		});//audit_service_datagrid
	});//服务访问量实时动态
