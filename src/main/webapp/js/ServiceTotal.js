

var thismoth_serviceType__datagrid;
var thismoth_service__datagrid;
var thismoth_servicevisitor_datagrid;
var today_serviceType_datagrid;
var today_service_datagrid;
var today_servicevisitor_datagrid;
var thisyear_serviceType_datagrid;
var thisyear_service_datagrid;
var thisyear_servicevisitor_datagrid;
var audit_Detail_datagrid;
var audit_DetailDialog;

$(function(){
	
//本年累计服务访问数统计 0.服务 1.服务类型 2. 访问用户
$('#thisyear_east_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterService/getAllServiceThisYearchart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   $("#thisyear_service_container").empty();
  							if ( msg.rows) {
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.sId , item.visiteTotalTime ];
                                      ids.push(obj);
                                  }
  							var chart = anychart.pieChart( ids);
  							chart.title('本年累计服务访问数统计');
  		                    chart.container('thisyear_service_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
      
    	   thisyear_service_datagrid = $("#thisyear_service_datagrid").datagrid
                              ({
                            	url : './ydVisiterService/getAllServiceThisYear.do',
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
									title : '服务标识',
									field : 'sId',
									width : 80,
									fit : true,
								},{
									title : '访问次数',
									field : 'visiteTotalTime',
									width : 18,
									fit : true,
									
								}
					            ] ],
					            toolbar:[{
						            text:'详细情况',
						            iconCls : 'icon-large-chart',
						            handler : function(){
						            var rows = thisyear_service_datagrid.datagrid('getSelections');
									if(rows.length >1) {
									var names = [];
									for(var i =0; i<rows.length; i++){
									names.push(rows[i].sid);
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
										$('#audit_DetailDialog').dialog({});
										audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
											url:'./ydVisiterService/getDetailServices4yearBySid.do?sId='+rows[0].sId,
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
					                                
					                                
					                            },{
					                                title: '访问ip',
					                                field: 'uip',
					                                width: 100,
					                                
					                                
					                            }, {
					                                title: '访问时间',
					                                field: 'visitedTime',
					                                
					                                width: 100,
					                                
					                                
					                            },
												
								            ] ]		
										});
										thisyear_service_datagrid.datagrid('unselectAll');
									  };
								 }//handler
						            },
//						            '-',{
//						            text:'导出统计表文件',
//						            iconCls : 'icon-save',
//						            handler : function(){
//						            	$.messager.show({	
//											msg : '功能开发中....',
//											title : '提示'
//											});
//						            }
//						            },
						            '-',{
						            text:' 取消选中',
						            iconCls : 'icon-cancel',
						            handler : function(){
						            //取消被选择的号
						            	thisyear_service_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }else if(index==1){//本年度服务类型
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterService/getServiceTypeThisYear4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#thisyear_serviceType_container").empty();
  							if (msg.rows) {
  							
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.name , item.visitedTotalTimes ];
                                      ids.push(obj);
                                  }
  								 var chart2 = anychart.columnChart();
  							 chart2.column(ids);
  							chart2.title('本年累计服务访问数统计');
  							chart2.xAxis().title().text('服务类型');
  							chart2.yAxis().title().text('访问量');
  		                    chart2.container('thisyear_serviceType_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
    	   thisyear_serviceType_datagrid = $("#thisyear_serviceType_datagrid").datagrid({
							url : './ydVisiterService/getServiceTypeThisYear.do',
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
							idField : 'lid',
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
								fit : true
							},{
								title : '访问次数',
								field : 'visitedTotalTimes',
								width : 18,
								fit : true,
								
							}
				            ] ],
				            toolbar:[{
					            text:'详细情况',
					            iconCls : 'icon-large-chart',
					            handler : function(){
					            var rows = thisyear_serviceType_datagrid.datagrid('getSelections');
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
								msg : '请选择一条记录进行修改',
								title : '提示'
								});
								}else if(rows.length == 1){
									$('#audit_DetailDialog').dialog({});
									audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
										url:'./ydVisiterService/getDetailBysType4thisyear.do?tName='+encodeURI(encodeURI(rows[0].name)),
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
				                              
				                                
				                            },{
				                                title: '访问ip',
				                                field: 'uip',
				                                width: 100,
				                               
				                                
				                            },{
				                                title: '访问服务',
				                                field: 'service',
				                                width: 100,
				                                
				                                
				                            }, {
				                                title: '访问时间',
				                                field: 'visitedTime',
				                                width: 100,
				                               
				                                
				                            },
											
							            ] ]		
									});
									thisyear_serviceType_datagrid.datagrid('unselectAll');
									
								  };
							 }
					            },
//					            '-',{
//					            text:'导出统计表文件',
//					            iconCls : 'icon-save',
//					            handler : function(){
//					            	$.messager.show({	
//										msg : '功能开发中....',
//										title : '提示'
//										});
//					            }
//					            },
					            
					            '-',{
					            text:' 取消选中',
					            iconCls : 'icon-cancel',
					            handler : function(){
					            FMG.editeRow = undefined;
					            //取消选中，更改内容回滚
					            thisyear_serviceType_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            thisyear_serviceType_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});

			
       
       }  else if(index==2){//本年度用户
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterService/getAllUserThisyear4chart.do",
               async: false,
              dataType: "json",
              contentType : "application/json",
               success : function(msg) {
            	 
            	   $("#thisyear_servicevisitor_container").empty();
  							if (msg.rows) {
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = {x: item.typeName , value: item.visiteTimes};
                                      ids.push(obj);
                                  }
  								 var chart3 = anychart.columnChart();
  	  							 chart3.column(ids);
  	  							chart3.title('本年累计服务访问数统计');
  	  							chart3.xAxis().title().text('用户类型');
  	  							chart3.yAxis().title().text('访问量');
  	  		                    chart3.container('thisyear_servicevisitor_container').draw(); 
  				
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	     

			/**
			 * 中部表格
			 */
			thisyear_servicevisitor_datagrid = $(
					"#thisyear_servicevisitor_datagrid").datagrid({
						url : './ydVisiterService/getAllUserThisyear.do',
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
							title : '用户类型',
							field : 'typeName',
							width : 80,
							fit : true
						},{
							title : '访问次数',
							field : 'visiteTimes',
							width : 18,
							fit : true,
							
						}
			            ] ],
			            toolbar:[{
				            text:'详细情况',
				            iconCls : 'icon-large-chart',
				            handler : function(){
				            var rows = thisyear_servicevisitor_datagrid.datagrid('getSelections');
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
							msg : '请选择一条件记录',
							title : '提示'
							});
							}else if(rows.length == 1){
								$('#audit_DetailDialog').dialog({});
								audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
									url:'./ydVisiterService/getDetailByUser4thisyear.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
			                                
			                                
			                            },{
			                                title: '访问ip',
			                                field: 'uip',
			                                width: 100,
			                                
			                                
			                            }, {
			                                title: '访问服务',
			                                field: 'service',
			                                width: 100,
			                                
			                                
			                            }, {
			                                title: '访问时间',
			                                field: 'visitedTime',
			                                
			                                width: 100,
			                                
			                                
			                            },
										
						            ] ]		
								});
								thisyear_servicevisitor_datagrid.datagrid('unselectAll');
						
							  };
						 }
				            },
//				            '-',{
//				            text:'导出统计表文件',
//				            iconCls : 'icon-save',
//				            handler : function(){
//				            	$.messager.show({	
//									msg : '功能开发中....',
//									title : '提示'
//									});
//				            }
//				            },
				            '-',{
				            text:' 取消选中',
				            iconCls : 'icon-cancel',
				            handler : function(){
				            FMG.editeRow = undefined;
				            //取消选中，更改内容回滚
				            thisyear_servicevisitor_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            thisyear_servicevisitor_datagrid.datagrid('unselectAll');
				            }
				            }],

			});
       }
    }
});


/******************************************本年****************************************************************/

//本月累计服务访问数统计 0.服务 1.服务类型 2. 访问用户
$('#thismoth_center_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterService/getAllServiceThisMonthchart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   $("#thismoth_service_container").empty();
  							if ( msg.rows) {
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.sId , item.visiteTotalTime ];
                                      ids.push(obj);
                                  }
  							var chart = anychart.pieChart( ids);
  							chart.title('本月累计服务访问数统计');
  		                    chart.container('thismoth_service_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
      
    	   thismoth_service__datagrid = $("#thismoth_service__datagrid").datagrid
                              ({
                            	url : './ydVisiterService/getAllServiceThisMonth.do',
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
									title : '服务标识',
									field : 'sId',
									width : 80,
									fit : true,
								},{
									title : '访问次数',
									field : 'visiteTotalTime',
									width : 18,
									fit : true,
									sortable : true,
								}
					            ] ],
					            toolbar:[{
						            text:'详细情况',
						            iconCls : 'icon-large-chart',
						            handler : function(){
						            var rows = thismoth_service__datagrid.datagrid('getSelections');
									if(rows.length >1) {
									var names = [];
									for(var i =0; i<rows.length; i++){
									names.push(rows[i].sid);
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
										$('#audit_DetailDialog').dialog({});
										audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
											url:'./ydVisiterService/getDetailServicesThisMonthBySid.do?sId='+rows[0].sId,
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
					                                
					                                
					                            },{
					                                title: '访问ip',
					                                field: 'uip',
					                                width: 100,
					                                
					                                
					                            }, {
					                                title: '访问时间',
					                                field: 'visitedTime',
					                                
					                                width: 100,
					                                
					                                
					                            },
												
								            ] ]		
										});
										thismoth_service__datagrid.datagrid('unselectAll');
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
						            	thismoth_service__datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }else if(index==1){//服务类型
    	   
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterService/getServiceTypeThisMonth4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#thismoth_serviceType_container").empty();
  							if (msg.rows) {
  							
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.name , item.visitedTotalTimes ];
                                      ids.push(obj);
                                  }
  								 var chart2 = anychart.columnChart();
  							 chart2.column(ids);
  							chart2.title('本月累计服务访问数统计');
  							chart2.xAxis().title().text('服务类型');
  							chart2.yAxis().title().text('访问量');
  		                    chart2.container('thismoth_serviceType_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
    	   thismoth_serviceType__datagrid = $("#thismoth_serviceType__datagrid").datagrid({
							url : './ydVisiterService/getServiceTypeThisMonth.do',
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
							idField : 'lid',
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
								fit : true
							},{
								title : '访问次数',
								field : 'visitedTotalTimes',
								width : 18,
								fit : true,
								
							}
				            ] ],
				            toolbar:[{
					            text:'详细情况',
					            iconCls : 'icon-large-chart',
					            handler : function(){
					            var rows = thismoth_serviceType__datagrid.datagrid('getSelections');
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
								msg : '请选择一条记录进行修改',
								title : '提示'
								});
								}else if(rows.length == 1){
									$('#audit_DetailDialog').dialog({});
									audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
										url:'./ydVisiterService/getDetailBysType4thisMonth.do?tName='+encodeURI(encodeURI(rows[0].name)),
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
				                              
				                                
				                            },{
				                                title: '访问ip',
				                                field: 'uip',
				                                width: 100,
				                               
				                                
				                            },{
				                                title: '访问服务',
				                                field: 'service',
				                                width: 100,
				                                
				                                
				                            }, {
				                                title: '访问时间',
				                                field: 'visitedTime',
				                                width: 100,
				                                
				                                
				                            },
											
							            ] ]		
									});
									thismoth_serviceType__datagrid.datagrid('unselectAll');
									
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
					            thismoth_serviceType__datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            thismoth_serviceType__datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});

       
       }  else if(index==2){
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterService/getAllUserThisMonth4chart.do",
               async: false,
              dataType: "json",
              contentType : "application/json",
               success : function(msg) {
            	 
            	   $("#thismoth_servicevisitor_container").empty();
  							if (msg.rows) {
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = {x: item.typeName , value: item.visiteTimes};
                                      ids.push(obj);
                                  }
  								 var chart3 = anychart.columnChart();
  	  							 chart3.column(ids);
  	  							chart3.title('本月累计服务访问数统计');
  	  							chart3.xAxis().title().text('用户类型');
  	  							chart3.yAxis().title().text('访问量');
  	  		                    chart3.container('thismoth_servicevisitor_container').draw(); 
  				
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	     

			/**
			 * 中部表格
			 */
    	   thismoth_servicevisitor_datagrid = $(
					"#thismoth_servicevisitor_datagrid").datagrid({
						url : './ydVisiterService/getAllUserThisMonth.do',
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
							title : '用户类型',
							field : 'typeName',
							width : 80,
							fit : true
						},{
							title : '访问次数',
							field : 'visiteTimes',
							width : 18,
							fit : true,
						
						}
			            ] ],
			            toolbar:[{
				            text:'详细情况',
				            iconCls : 'icon-large-chart',
				            handler : function(){
				            var rows = thismoth_servicevisitor_datagrid.datagrid('getSelections');
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
							msg : '请选择一条件记录',
							title : '提示'
							});
							}else if(rows.length == 1){
								$('#audit_DetailDialog').dialog({});
								audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
									url:'./ydVisiterService/getDetailByUser4thisMonth.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
			                                
			                                
			                            },{
			                                title: '访问ip',
			                                field: 'uip',
			                                width: 100,
			                                
			                                
			                            }, {
			                                title: '访问服务',
			                                field: 'service',
			                                width: 100,
			                                
			                                
			                            }, {
			                                title: '访问时间',
			                                field: 'visitedTime',
			                                
			                                width: 100,
			                                
			                                
			                            },
										
						            ] ]		
								});
								thismoth_servicevisitor_datagrid.datagrid('unselectAll');
						
							  };
						 }
				            },'-',
//				            {
//				            text:'导出统计表文件',
//				            iconCls : 'icon-save',
//				            handler : function(){
//				            	$.messager.show({	
//									msg : '功能开发中....',
//									title : '提示'
//									});
//				            }
//				            },'-',
				            {
				            text:' 取消选中',
				            iconCls : 'icon-cancel',
				            handler : function(){
				            FMG.editeRow = undefined;
				            //取消选中，更改内容回滚
				            thismoth_servicevisitor_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            thismoth_servicevisitor_datagrid.datagrid('unselectAll');
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
              url:"./ydVisiterService/getAllServiceTodaychart.do",
              async: false,
             dataType: "json",
              success : function(msg) {
           	   $("#today_service_container").empty();
 							if ( msg.rows) {
 								var ids = [];
 								for (var i = 0; i < msg.rows.length; i++) {
 									var item =  msg.rows[i];
 									var obj = [item.sId , item.visiteTotalTime ];
                                     ids.push(obj);
                                 }
 							var chart = anychart.pieChart( ids);
 							chart.title('本日累计服务访问数统计');
 		                    chart.container('today_service_container').draw(); 					                        		
 		                    
 												                        		
 								}else{
 									console.log("返回值错误");
 								} 
 						},
 						error : function(data) {
 							console.log("ajax调取错误");
 						}
            
          });//ajax
   	   
     
    	  today_service_datagrid = $("#today_service_datagrid").datagrid
                             ({
                           	url : './ydVisiterService/getAllServiceToday.do',
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
									title : '服务标识',
									field : 'sId',
									width : 80,
									fit : true,
								},{
									title : '访问次数',
									field : 'visiteTotalTime',
									width : 18,
									fit : true,
									sortable : true,
								}
					            ] ],
					            toolbar:[{
						            text:'详细情况',
						            iconCls : 'icon-large-chart',
						            handler : function(){
						            var rows = today_service_datagrid.datagrid('getSelections');
									if(rows.length >1) {
									var names = [];
									for(var i =0; i<rows.length; i++){
									names.push(rows[i].sid);
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
										$('#audit_DetailDialog').dialog({});
										audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
											url:'./ydVisiterService/getDetailServicesTodayBySid.do?sId='+rows[0].sId,
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
					                                
					                                
					                            },{
					                                title: '访问ip',
					                                field: 'uip',
					                                width: 100,
					                                
					                                
					                            }, {
					                                title: '访问时间',
					                                field: 'visitedTime',
					                                
					                                width: 100,
					                                
					                                
					                            },
												
								            ] ]		
										});
										today_service_datagrid.datagrid('unselectAll');
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
						            	today_service_datagrid.datagrid('rejectChanges');
						            //取消被选择的号
						            	today_service_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
     
     }else if(index==1){
    	 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiterService/getServiceTypeToday4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
          	   console.log(msg);
          	   $("#today_serviceType_container").empty();
							if (msg.rows) {
							
							
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.name , item.visitedTotalTimes ];
                                    ids.push(obj);
                                }
								 var chart2 = anychart.columnChart();
							 chart2.column(ids);
							 chart2.title('本日累计服务访问数统计');
							chart2.xAxis().title().text('服务类型');
							chart2.yAxis().title().text('访问量');
		                    chart2.container('today_serviceType_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
  	   
    	 today_serviceType_datagrid = $("#today_serviceType_datagrid").datagrid({
							url : './ydVisiterService/getServiceTypeToday.do',
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
							idField : 'lid',
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
					            var rows = today_serviceType_datagrid.datagrid('getSelections');
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
								msg : '请选择一条记录进行修改',
								title : '提示'
								});
								}else if(rows.length == 1){
									$('#audit_DetailDialog').dialog({});
									audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
										url:'./ydVisiterService/getDetailBysType4today.do?tName='+encodeURI(encodeURI(rows[0].name)),
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
				                                sortable: true
				                                
				                            },{
				                                title: '访问ip',
				                                field: 'uip',
				                                width: 100,
				                                sortable: true
				                                
				                            },{
				                                title: '访问服务',
				                                field: 'service',
				                                width: 100,
				                                sortable: true
				                                
				                            }, {
				                                title: '访问时间',
				                                field: 'visitedTime',
				                                width: 100,
				                                sortable: true
				                                
				                            },
											
							            ] ]		
									});
									today_serviceType_datagrid.datagrid('unselectAll');
									
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
					            today_serviceType_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            today_serviceType_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});
     
     }  else if(index==2){

  	   $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiterService/getAllUserToday4chart.do",
             async: false,
            dataType: "json",
            contentType : "application/json",
             success : function(msg) {
          	 
          	   $("#today_servicevisitor_container").empty();
							if (msg.rows) {
							
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = {x: item.typeName , value: item.visiteTimes};
                                    ids.push(obj);
                                }
								 var chart3 = anychart.columnChart();
	  							 chart3.column(ids);
	  							chart3.title('本日累计服务访问数统计');
	  							chart3.xAxis().title().text('用户标识');
	  							chart3.yAxis().title().text('访问量');
	  		                    chart3.container('today_servicevisitor_container').draw(); 
				
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
  	     

			/**
			 * 中部表格
			 */
  	 today_servicevisitor_datagrid = $(
					"#today_servicevisitor_datagrid").datagrid({
						url : './ydVisiterService/getAllUserToday.do',
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
				            var rows = today_servicevisitor_datagrid.datagrid('getSelections');
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
							msg : '请选择一条件记录',
							title : '提示'
							});
							}else if(rows.length == 1){
								$('#audit_DetailDialog').dialog({});
								audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
									url:'./ydVisiterService/getDetailByUser4Today.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
			                                
			                                
			                            },{
			                                title: '访问ip',
			                                field: 'uip',
			                                width: 100,
			                                
			                                
			                            }, {
			                                title: '访问服务',
			                                field: 'service',
			                                width: 100,
			                                
			                                
			                            }, {
			                                title: '访问时间',
			                                field: 'visitedTime',
			                                
			                                width: 100,
			                                
			                                
			                            },
										
						            ] ]		
								});
								today_servicevisitor_datagrid.datagrid('unselectAll');
						
							  };
						 }
				            },'-',
//				            {
//				            text:'导出统计表文件',
//				            iconCls : 'icon-save',
//				            handler : function(){
//				            	$.messager.show({	
//									msg : '功能开发中....',
//									title : '提示'
//									});
//				            }
//				            },'-',
				            {
				            text:' 取消选中',
				            iconCls : 'icon-cancel',
				            handler : function(){
				            FMG.editeRow = undefined;
				            //取消选中，更改内容回滚
				            today_servicevisitor_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            today_servicevisitor_datagrid.datagrid('unselectAll');
				            }
				            }],

			});
    	 
 
     }
  }
});

});






