
var thismoth_page__datagrid;
var thismoth_bomule_datagrid;
var thismoth_servicevisitor_datagrid;
var today_page_datagrid;
var today_bomule_datagrid;
var today_pagevisitor_datagrid;
var thisyear_page_datagrid;
var thisyear_bomule_datagrid;
var thisyear_servicevisitor_datagrid;

$(function(){
	
//本年累计服务访问数统计 0.页面 1.栏目 2. 访问用户
$('#thisyear_east_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getAllPageThisYear4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   $("#thisyear_page_container").empty();
  							if ( msg.rows) {
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.pid , item.visitedTotalTimes ];
                                      ids.push(obj);
                                  }
  							var chart = anychart.pieChart( ids);
  							chart.title('页面本年度访问量统计');
  		                    chart.container('thisyear_page_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
      
    	   thisyear_page_datagrid = $("#thisyear_page_datagrid").datagrid
                              ({
                            	url : './ydUserPage/getAllPageThisYear.do',
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
									fit : true,
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
						            var rows = thisyear_page_datagrid.datagrid('getSelections');
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
											url:'./ydUserPage/getDetailByPid4thisyear.do?pid='+encodeURI(encodeURI(rows[0].pid)),
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
										thisyear_page_datagrid.datagrid('unselectAll');
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
						            thisyear_page_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }else if(index==1){//栏目
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getbomuleThisyear4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#thisyear_bmodule_container").empty();
  							if (msg.rows) {
  							
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.name , item.visitedTotalTimes ];
                                      ids.push(obj);
                                  }
  								 var chart2 = anychart.columnChart();
  							 chart2.column(ids);
  							chart2.title('页面本年度访问量统计');
  							chart2.xAxis().title().text('栏目');
  							chart2.yAxis().title().text('访问量');
  		                    chart2.container('thisyear_bmodule_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
    	   thisyear_bmodule_datagrid = $(
					"#thisyear_bmodule_datagrid").datagrid({
							url : './ydUserPage/getbomule4year.do',
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
					            field :'id',
					            width : 10 ,
					            checkbox : true    
					            },{
								title : '栏目',
								field : 'name',
								width : 80,
								fit : true,
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
					            var rows = thisyear_bmodule_datagrid.datagrid('getSelections');
								if(rows.length >1) {
								var names = [];
								for(var i =0; i<rows.length; i++){
								names.push(rows[i].name);
								}
								$.messager.show({	
								msg : '只能选择一条记录进行查看或者导出,您已经选了【'+names.join(",")+'】' + rows.length +'条记录',
								title : '提示'
								});
								} else if(rows.length == 0){
								$.messager.show({
								msg : '请选择一个',
								title : '提示'
								});
								}else if(rows.length == 1){
									$('#audit_DetailDialog').dialog({});
									audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
										url:'./ydUserPage/getDetailBybmodule4thisyear.do?bmName='+encodeURI(encodeURI(rows[0].name)),
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
									thisyear_bmodule_datagrid.datagrid('unselectAll');
									
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
					            thisyear_bomule_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            thisyear_bomule_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});

			
       
       }  else if(index==2){//访问用户
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getAllPageByUserThisyear4chart.do",
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
  	  							chart3.xAxis().title().text('用户类型');
  	  							chart3.yAxis().title().text('访问量');
  	  						chart3.title('页面本年度访问量统计');
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
						url : './ydUserPage/getAllPageByUserThisyear.do',
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
							fit : true,
							
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
							msg : '请选择至少一条记录',
							title : '提示'
							});
							}else if(rows.length == 1){
								$('#audit_DetailDialog').dialog({});
								audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
									url:'./ydUserPage/getDetailByUser4thisyear.do?uid='+encodeURI(encodeURI(rows[0].typeName)),
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
			                                title: '用户标识',
			                                field: 'uid',
			                                width: 100,
			                                sortable: true,
			                                
			                            },{
			                                title: '访问ip',
			                                field: 'uip',
			                                width: 100,
			                                sortable: true,
			                                
			                            }, {
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
								thisyear_servicevisitor_datagrid.datagrid('unselectAll');
						
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

//本日累计服务访问数统计 0.页面 1.栏目 2. 访问用户
$('#today_west_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getAllPageToday4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   $("#today_pageTotal_container").empty();
  							if ( msg.rows) {
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.pid , item.visitedTotalTimes ];
                                      ids.push(obj);
                                  }
  							var chart = anychart.pieChart( ids);
  							chart.title('页面本日访问量统计');
  		                    chart.container('today_pageTotal_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	      
    	   today_page_datagrid = $("#today_page_datagrid").datagrid
                              ({
                            	url:'./ydUserPage/getAllPageToday.do',
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
									width : 20,
									
								},{
									title : '访问次数',
									field : 'visitedTotalTimes',
									width : 18,
									fit : true,
									sortable : true,
									editor : {
										type : 'validatebox',
										options : {
											required : true
											}
									}
								}
					            ] ],
					            toolbar:[{
						            text:'详细情况',
						            iconCls : 'icon-large-chart',
						            handler : function(){
						            var rows = today_page_datagrid.datagrid('getSelections');
									if(rows.length >1) {
									var names = [];
									for(var i =0; i<rows.length; i++){
									names.push(rows[i].sid);
									}
									$.messager.show({	
									msg : '只能选择一条服务,您已经选了【'+names.join(",")+'】' + rows.length +'个服务',
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
											url:'./ydUserPage/getDetailByPid4Today.do?pid='+encodeURI(encodeURI(rows[0].pid)),
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
										today_page_datagrid.datagrid('unselectAll');
										
									  };
								 }
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
						            FMG.editeRow = undefined;
						            //取消选中，更改内容回滚
						            today_page_datagrid.datagrid('rejectChanges');
						            //取消被选择的号
						            today_page_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }else if(index==1){//栏目
    	   
    	 
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getbomuleToday4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#today_bmodule_container").empty();
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
  							chart2.title('页面本日访问量统计');
  		                    chart2.container('today_bmodule_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
    	   today_bmodule_datagrid = $(
					"#today_bmodule_datagrid").datagrid({
							url : './ydUserPage/getbomuletoday.do',
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
								title : '栏目',
								field : 'name',
								width : 80,
								fit : true,
								editor : {
									type : 'validatebox',
									options : {
										required : true
										}
					            }
							},{
								title : '访问次数',
								field : 'visitedTotalTimes',
								width : 18,
								fit : true,
								sortable : true,
								editor : {
									type : 'validatebox',
									options : {
										required : true
										}
								}
							}
				            ] ],
				            toolbar:[{
					            text:'详细情况',
					            iconCls : 'icon-large-chart',
					            handler : function(){
					            var rows = today_bmodule_datagrid.datagrid('getSelections');
								if(rows.length >1) {
								var names = [];
								for(var i =0; i<rows.length; i++){
								names.push(rows[i].name);
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
										url:'./ydUserPage/getDetailBybmoduleToday.do?bmName='+encodeURI(encodeURI(rows[0].name)),
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
									today_bmodule_datagrid.datagrid('unselectAll');	
									
									
									
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
					            today_bmodule_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            today_bmodule_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});
       
       }  else if(index==2){//用户
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getAllPageByUserToday4chart.do",
               async: false,
              dataType: "json",
              contentType : "application/json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#today_pagevisitor_container").empty();
  							if (msg.rows) {
  								
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = {x: item.typeName , value: item.visiteTimes };
                                      ids.push(obj);
                                  }
  								 var chart3 = anychart.columnChart();
  	  							 chart3.column(ids);
  	  							chart3.xAxis().title().text('用户类型');
  	  							chart3.yAxis().title().text('访问量');
  	  						chart3.title('页面本日访问量统计');
  	  		                    chart3.container('today_pagevisitor_container').draw(); 
  				
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
 	     
    	   today_pagevisitor_datagrid = $(
					"#today_pagevisitor_datagrid").datagrid({
						url : './ydUserPage/getDetail4todayByUserType.do',
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
							fit : true,
							
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
				            var rows = today_pagevisitor_datagrid.datagrid('getSelections');
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
									url:'./ydUserPage/getDetail4todayByUserType.do?uid='+encodeURI(encodeURI(rows[0].typeName)),
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
			                                title: '访问页面',
			                                field: 'page',
			                                width: 100,
			                                sortable: true,
			                                
			                            },{
			                                title: '访问时间',
			                                field: 'visitedTime',
			                                
			                                width: 100,
			                                sortable: true,
			                                
			                            },
										
						            ] ]		
								});
								today_pagevisitor_datagrid.datagrid('unselectAll');	
								
								
								
								
								
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
				            today_pagevisitor_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            today_pagevisitor_datagrid.datagrid('unselectAll');
				            }
				            }],

			});
       }
    }
});


/**********************************************本日************************************************************/

//本月累计服务访问数统计 0.服务类型 1.服务级别 2. 访问用户
$('#thismoth_center_tt').tabs({
border:false,  
  selected: 0,  
   onSelect:function(title,index){    
     if(index==0){
  	 
  	   $.ajax({
             cache: true,
             type: "POST",
             url:"./ydUserPage/getAllPageThisMonth4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
          	   $("#thismoth_page_container").empty();
							if (msg.rows) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.pid , item.visitedTotalTimes ];
                                    ids.push(obj);
                                }
							var chart = anychart.pieChart( ids);
							chart.title('页面本月访问量统计');
		                    chart.container('thismoth_page_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax      
  	 thismoth_page__datagrid = $("#thismoth_page__datagrid").datagrid
                            ({
                            	url:"./ydUserPage/getAllPageThisMonth.do",
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
									fit : true,
									
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
						            var rows = thismoth_page__datagrid.datagrid('getSelections');
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
									msg : '请选择一个服务进行修改',
									title : '提示'
									});
									}else if(rows.length == 1){
										$('#audit_DetailDialog').dialog({});
										audit_Detail_datagrid = $("#audit_Detail_datagrid").datagrid({
											url:'./ydUserPage/getDetailByPid4thisMonth.do?pid='+ encodeURI(encodeURI(rows[0].pid)),
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
										thismoth_page__datagrid.datagrid('unselectAll');
									  };
								 }
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
						            FMG.editeRow = undefined;
						            //取消选中，更改内容回滚
						            thismoth_page__datagrid.datagrid('rejectChanges');
						            //取消被选择的号
						            thismoth_page__datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
     
     }else if(index==1){
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getbomuleThisMonth4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	  
            	   $("#thismoth_bmodule_container").empty();
  							if (msg.rows) {
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.name , item.visitedTotalTimes ];
                                      ids.push(obj);
                                  }
  								
  								 var chart2 = anychart.columnChart();
  							 chart2.column(ids);
  							chart2.title('页面本月访问量统计');
  							chart2.xAxis().title().text('栏目');
  							chart2.yAxis().title().text('访问量');
  		                    chart2.container('thismoth_bmodule_container').draw(); 					                        		
//  		                    
//  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
    	   thismoth_bmodule_datagrid = $(
					"#thismoth_bmodule_datagrid").datagrid({
							url : './ydUserPage/getbomuleThisMonth.do',
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
								title : '栏目',
								field : 'name',
								width : 80,
								fit : true,
								
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
					            var rows = thismoth_bmodule_datagrid.datagrid('getSelections');
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
										url:'./ydUserPage/getDetailBybmodule4thisMonth.do?bmName='+encodeURI(encodeURI(rows[0].name)),
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
									thismoth_bmodule_datagrid.datagrid('unselectAll');	
									
									
									
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
					            thismoth_bmodule_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            thismoth_bmodule_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});
     
     }  else if(index==2){
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydUserPage/getAllPageByUserThisMonth4chart.do",
               async: false,
              dataType: "json",
              contentType : "application/json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#thismoth_pagevisitor_container").empty();
  							if (msg.rows) {
  								
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = {x: item.typeName , value: item.visiteTimes };
                                      ids.push(obj);
                                  }
  								 var chart3 = anychart.columnChart();
  	  							 chart3.column(ids);
  	  							chart3.xAxis().title().text('用户类型');
  	  							chart3.yAxis().title().text('访问量');
  	  						chart3.title('页面本月访问量统计');
  	  		                    chart3.container('thismoth_pagevisitor_container').draw(); 
  				
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	     
    	   thismoth_pagevisitor_datagrid = $(
					"#thismoth_pagevisitor_datagrid").datagrid({
						url : './ydUserPage/getAllPageByUserThisMonth.do',
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
				            var rows = thismoth_pagevisitor_datagrid.datagrid('getSelections');
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
									url:'./ydUserPage/getDetailByUser4thisMonth.do?uid='+encodeURI(encodeURI(rows[0].typeName)),
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
			                                title: '页面标识',
			                                field: 'page',
			                                width: 100,
			                                sortable: true,
			                                
			                            },{
			                                title: '访问时间',
			                                field: 'visitedTime',
			                                
			                                width: 100,
			                                sortable: true,
			                                
			                            },
										
						            ] ]		
								});
								thismoth_pagevisitor_datagrid.datagrid('unselectAll');	
										
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
				            thismoth_pagevisitor_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            thismoth_pagevisitor_datagrid.datagrid('unselectAll');
				            }
				            }],

			});
    	 
 
     }
  }
});

});






