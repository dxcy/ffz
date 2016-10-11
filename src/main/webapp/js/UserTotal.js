
var thisyear_userType_datagrid;
var audit_Detail_datagrid;
var thismoth_userType__datagrid;
var today_userType_datagrid;
var uuType;

$(function(){
	
//本年累计用户访问数统计 0.用户类型
$('#thisyear_east_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiter/getUserTypeThisYear4chart.do",
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
  							$("#thisyear_userType_container").empty();
  							
  							var data = anychart.data.set( ids);
  							var visiteTime = data.mapAs({x: [0], value: [1]});
  							var downloadTime = data.mapAs({x: [0], value: [2]});
  							var visitePageTime = data.mapAs({x: [0], value: [3]});
  							  // chart type
  							  var chart = anychart.columnChart();

  							  // set title
  							  chart.title().text('用户访问最新动态');
  							   chart.animation(true);
  							 chart.xAxis().stroke(null);
							 
  							  // set axes titles
  							  chart.xAxis().title().text('用户类型').fontSize(5);
  							  chart.yAxis().title().text('次数累计');
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
							  chart.legend().enabled(true).fontSize(5).padding([0,0,5,0]);

							  // turn on grids
							  var grid = chart.grid();
							  grid.enabled(true).stroke("#ddd");
							  grid.drawLastLine(false);
							  grid.layout("vertical");

							  // tune column paddings
							  chart.barsPadding(0.1);
							  chart.barGroupsPadding(0.9);
  							  // draw
  							  chart.container('thisyear_userType_container').draw();
  				           				                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   thisyear_userType_datagrid = $("#thisyear_userType_datagrid").datagrid
                              ({
                            	url : './ydVisiter/getUserTypeThisYear.do',
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
									width : 50,
									fit : true,
								},{
									title : '访问服务次数',
									field : 'visiteTimes',
									width : 50,
									fit : true,
								},{
									title : '下载数据次数',
									field : 'downloadTimes',
									width : 50,
									fit : true,
									sortable : true,
								},{
									title : '访问页面次数',
									field : 'visitePageTimes',
									width : 50,
									fit : true,
								},
					            ] ],
					            toolbar:[{
						            text:'详细情况',
						            iconCls : 'icon-large-chart',
						            handler : function(){
						            var rows = thisyear_userType_datagrid.datagrid('getSelections');
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
									msg : '请选择一个服务进行修改',
									title : '提示'
									});
									}else if(rows.length == 1){
										uuType = rows[0].typeName;
										$('#audit_DetailDialog').dialog({});
										$('#audit_DetailDialog').css('display','block');
										 $('#userDetail_tt').tabs({
											 border:false,  
											     selected: 0,  
											      onSelect:function(title,index){    
											        if(index==0){
											        	userServiceDetail_datagrid = $("#userServiceDetail_datagrid").datagrid
											                               ({
											                             	url : './ydVisiterService/getDetailService4yearByuType.do?uType='+encodeURI(encodeURI(uuType)),
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
											 									width :100,
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
											        
											        } 
											        if(index==1){//
											     	  
											        	userDataDetail_datagrid = $("#userDataDetail_datagrid").datagrid({
											 							url : './ydVisiterData/getDetailData4yearuType.do?uType='+encodeURI(encodeURI(uuType)),
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

											 			
											        
											        }  
											        if(index==2){//本年度页面访问
											     	  
											 			/**
											 			 * 中部表格
											 			 */
											        	userPageDetail_datagrid = $(
											 					"#userPageDetail_datagrid").datagrid({
											 						url : './ydUserPage/getDetailPageByuType4thisyear.do?uType='+encodeURI(encodeURI(uuType)),
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
									
										thisyear_userType_datagrid.datagrid('unselectAll');
									  };
								 }//handler
						            },'-',
//						            {
//						            text:'导出统计表文件',
//						            iconCls : 'icon-save',
//						            handler : function(){
//						            	$.messager.show({	
//											msg : '功能开发中....',
//											title : '提示'
//											});
//						            }
//						            },'-',
						            {
						            text:' 取消选中',
						            iconCls : 'icon-cancel',
						            handler : function(){
						            //取消被选择的号
						            	thisyear_userType_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }
    }
});


/******************************************本年****************************************************************/

//本月累计服务访问数统计 0.用户类型
$('#thismoth_center_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiter/getUserTypeThisMonth4chart.do",
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
					$("#thismoth_userType_container").empty();
					
					var data = anychart.data.set( ids);
					var visiteTime = data.mapAs({x: [0], value: [1]});
					var downloadTime = data.mapAs({x: [0], value: [2]});
					var visitePageTime = data.mapAs({x: [0], value: [3]});
					  // chart type
					  var chart = anychart.columnChart();

					  // turn on chart animation
					  chart.animation(true);
					  chart.title().padding([0,0,5,0]);
					  // set title
					  chart.title().text('用户访问最新动态');
					  // set axes titles
					  chart.xAxis().title().text('用户类型');
					  chart.yAxis().title().text('次数累计');
					 
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
					  chart.legend().enabled(true).fontSize(5).padding([0,0,5,0]);

					  // turn on grids
					  var grid = chart.grid();
					  grid.enabled(true).stroke("#ddd");
					  grid.drawLastLine(false);
					  grid.layout("vertical");

					  // tune column paddings
					  chart.barsPadding(0.1);
					  chart.barGroupsPadding(0.9);

					  // draw
					  chart.container('thismoth_userType_container').draw();
		           				                        		
						}else{
							console.log("返回值错误");
						} 
				},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax

    	   thismoth_userType__datagrid = $("#thismoth_userType__datagrid").datagrid
                              ({
                            	url : './ydVisiter/getUserTypeThisMonth.do',
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
						            field :'id',
						            width : 10 ,
						            checkbox : true    
						            },{
									title : '用户类型',
									field : 'typeName',
									width : 50,
									fit : true,
								},{
									title : '访问服务次数',
									field : 'visiteTimes',
									width : 50,
									fit : true,
								},{
									title : '下载数据次数',
									field : 'downloadTimes',
									width : 50,
									fit : true,
									sortable : true,
								},{
									title : '访问页面次数',
									field : 'visitePageTimes',
									width : 50,
									fit : true,
								},
					            ] ],
					            toolbar:[{
						            text:'详细情况',
						            iconCls : 'icon-large-chart',
						            handler : function(){
						            var rows = thismoth_userType__datagrid.datagrid('getSelections');
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
									msg : '请选择一个服务进行修改',
									title : '提示'
									});
									}else if(rows.length == 1){
										uuType = rows[0].typeName;
										$('#audit_DetailDialog').dialog({});
										 $('#userDetail_tt').tabs({
											 border:false,  
											     selected: 0,  
											      onSelect:function(title,index){    
											        if(index==0){
											        	userServiceDetail_datagrid = $("#userServiceDetail_datagrid").datagrid
											                               ({
											                             	url : './ydVisiterService/getDetailService4MonthByuType.do?uType='+encodeURI(encodeURI(uuType)),
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
											 							url : './ydVisiterData/getDetailData4MonthuType.do?uType='+encodeURI(encodeURI(uuType)),
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
										 									width :100,
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

											 			
											        
											        }  else if(index==2){//本年度页面访问
											     	  
											 			/**
											 			 * 中部表格
											 			 */
											        	userPageDetail_datagrid = $(
											 					"#userPageDetail_datagrid").datagrid({
											 						url : './ydUserPage/getDetailPageByuType4thisMonth.do?uType='+encodeURI(encodeURI(uuType)),
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
									
										 thismoth_userType__datagrid.datagrid('unselectAll');
									  };
								 }//handler
						            },'-',
//						            {
//						            text:'导出统计表文件',
//						            iconCls : 'icon-save',
//						            handler : function(){
//						            	$.messager.show({	
//											msg : '功能开发中....',
//											title : '提示'
//											});
//						            }
//						            },'-',
						            {
						            text:' 取消选中',
						            iconCls : 'icon-cancel',
						            handler : function(){
						            //取消被选择的号
						            	thismoth_userType__datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }  
    }
});


/**********************************************本月************************************************************/

//本日累计服务访问数统计 0.服务 1.服务类型  2. 访问用户
$('#today_west_tt').tabs({
border:false,  
  selected: 0,  
   onSelect:function(title,index){    
     if(index==0){
  	 
    	  $.ajax({
              cache: true,
              type: "POST",
              url:"./ydVisiter/getUserTypeToday4chart.do",
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
					$("#today_userType_container").empty();
					
					var data = anychart.data.set( ids);
					var visiteTime = data.mapAs({x: [0], value: [1]});
					var downloadTime = data.mapAs({x: [0], value: [2]});
					var visitePageTime = data.mapAs({x: [0], value: [3]});
					  // chart type
					  var chart = anychart.columnChart();
					  // turn on chart animation
					  chart.animation(true);
					  chart.title().padding([0,0,5,0]);
					  // set title
					  chart.title().text('用户访问最新动态');
					  // set axes titles
					  chart.xAxis().title().text('用户类型');
					  chart.yAxis().title().text('次数累计');
					 
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
					  chart.legend().enabled(true).fontSize(5).padding([0,0,5,0]);

					  // turn on grids
					  var grid = chart.grid();
					  grid.enabled(true).stroke("#ddd");
					  grid.drawLastLine(false);
					  grid.layout("vertical");

					  // tune column paddings
					  chart.barsPadding(0.1);
					  chart.barGroupsPadding(0.9);

					  // draw
					  chart.container('today_userType_container').draw();
		           				                        		
						}else{
							console.log("返回值错误");
						} 
				},
 						error : function(data) {
 							console.log("ajax调取错误");
 						}
            
          });//ajax
   	   

    	  today_userType_datagrid = $("#today_userType_datagrid").datagrid
                             ({
                           	url : './ydVisiter/getUserTypeToday.do',
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
						            field :'id',
						            width : 10 ,
						            checkbox : true    
						            },{
									title : '用户类型',
									field : 'typeName',
									width : 50,
									fit : true,
								},{
									title : '访问服务次数',
									field : 'visiteTimes',
									width : 50,
									fit : true,
								},{
									title : '下载数据次数',
									field : 'downloadTimes',
									width : 50,
									fit : true,
									sortable : true,
								},{
									title : '访问页面次数',
									field : 'visitePageTimes',
									width : 50,
									fit : true,
								},
					            ] ],
					            toolbar:[{
						            text:'详细情况',
						            iconCls : 'icon-large-chart',
						            handler : function(){
						            var rows = today_userType_datagrid.datagrid('getSelections');
									if(rows.length >1) {
									var names = [];
									for(var i =0; i<rows.length; i++){
									names.push(rows[i].TypeName);
									}
									$.messager.show({	
									msg : '只能选择一个服务进行查看或者导出,您已经选了【'+names.join(",")+'】' + rows.length +'个服务',
									title : '提示'
									});
									} else if(rows.length == 0){
									$.messager.show({
									msg : '请选择一个服务进行修改',
									title : '提示'
									});
									}else if(rows.length == 1){
										uuType = rows[0].typeName;
										
										$('#audit_DetailDialog').dialog({});
										 $('#userDetail_tt').tabs({
											 border:false,  
											     selected: 0,  
											      onSelect:function(title,index){    
											        if(index==0){
											        	
											        	userServiceDetail_datagrid = $("#userServiceDetail_datagrid").datagrid
											                               ({
											                             	url : './ydVisiterService/getDetailService4TodayByuType.do?uType='+encodeURI(encodeURI(uuType)),
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
											 							url : './ydVisiterData/getDetailData4TodayuType.do?uType='+encodeURI(encodeURI(uuType)),
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

											 			
											        
											        }  else if(index==2){//本年度页面访问
											     	  
											 			/**
											 			 * 中部表格
											 			 */
											        	userPageDetail_datagrid = $(
											 					"#userPageDetail_datagrid").datagrid({
											 						url : './ydUserPage/getDetailPageByuType4Today.do?uType='+encodeURI(encodeURI(uuType)),
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
									
										 today_userType_datagrid.datagrid('unselectAll');
									  };
								 }//handler
						            },'-',
//						            {
//						            text:'导出统计表文件',
//						            iconCls : 'icon-save',
//						            handler : function(){
//						            	$.messager.show({	
//											msg : '功能开发中....',
//											title : '提示'
//											});
//						            }
//						            },'-',
						            {
						            text:' 取消选中',
						            iconCls : 'icon-cancel',
						            handler : function(){
						            //取消被选择的号
						            	today_userType_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
     
     } 
  }
});

});






