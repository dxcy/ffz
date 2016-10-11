
var thisyear_dataType_datagrid;
var audit_Detail_datagrid;
var thisyear_dataPrice_datagrid;
var thisyear_provider_datagrid;
var thismoth_data_datagrid;
var thismoth_dataPrice_datagrid;
var thismoth_provider_datagrid;
var today_dataType_datagrid;
var today_dataPrice_datagrid;
var today_provider_datagrid;
$(function(){
	
//本年累计服务下载数统计 0.服务 1.服务类型 2. 下载用户
$('#thisyear_east_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterData/getDataThisYearchart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   $("#thisyear_dataType_container").empty();
  							if ( msg.rows) {
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.typeName , item.downloadTotalTime ];
                                      ids.push(obj);
                                  }
  							var chart = anychart.pieChart( ids);
  						  chart.title('数据本年下载量统计');
  		                    chart.container('thisyear_dataType_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
      
    	   thisyear_dataType_datagrid = $("#thisyear_dataType_datagrid").datagrid
                              ({
                            	url : './ydVisiterData/getDataThisYear.do',
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
						            var rows = thisyear_dataType_datagrid.datagrid('getSelections');
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
											url:'./ydVisiterData/getDetailData4yearBydId.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
					                                title: '下载ip',
					                                field: 'uip',
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
										thisyear_dataType_datagrid.datagrid('unselectAll');
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
						            	thisyear_dataType_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }else if(index==1){//本年度按数据价格
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterData/getDataPriceThisYear4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#thisyear_dataPrice_container").empty();
  							if (msg.rows) {
  							
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.price , item.downloadTotalTime ];
                                      ids.push(obj);
                                  }
  								 var chart2 = anychart.columnChart();
  							 chart2.column(ids);
  							 chart2.title('数据本年下载量统计');
  							chart2.xAxis().title().text('数据价格');
  							chart2.yAxis().title().text('下载量');
  		                    chart2.container('thisyear_dataPrice_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
   
    	   thisyear_dataPrice_datagrid = $("#thisyear_dataPrice_datagrid").datagrid({
							url : './ydVisiterData/getDataPriceThisYear.do',
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
								width : 50,
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
					            var rows = thisyear_dataPrice_datagrid.datagrid('getSelections');
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
										url:'./ydVisiterData/getDetailByPrice4thisyear.do?price='+rows[0].price,
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
				                                title: '下载ip',
				                                field: 'uip',
				                                width: 100,
				                                sortable: true
				                                
				                            }, {
				                                title: '下载时间',
				                                field: 'downloadDate',
				                                width: 100,
				                                sortable: true
				                                
				                            },
											
							            ] ]		
									});
									thisyear_dataPrice_datagrid.datagrid('unselectAll');
									
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
					            thisyear_dataPrice_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            thisyear_dataPrice_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});

			
       
       }  else if(index==2){//本年度供应商
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterData/getProviderThisyear4chart.do",
               async: false,
              dataType: "json",
              contentType : "application/json",
               success : function(msg) {
            	 
            	   $("#thisyear_provider_container").empty();
  							if (msg.rows) {
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = {x: item.Provider , value: item.downloadTotalTime};
                                      ids.push(obj);
                                  }
  								 var chart3 = anychart.columnChart();
  	  							 chart3.column(ids);
  	  						 chart3.title('数据本年下载量统计');
  	  							chart3.xAxis().title().text('供应商');
  	  							chart3.yAxis().title().text('被下载量');
  	  		                    chart3.container('thisyear_provider_container').draw(); 
  				
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
    	   thisyear_provider_datagrid = $(
					"#thisyear_provider_datagrid").datagrid({
						url : './ydVisiterData/getProviderThisyear.do',
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
							title : '数据供应商',
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
				            var rows = thisyear_provider_datagrid.datagrid('getSelections');
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
									url:'./ydVisiterData/getDetailProvider4thisyear.do?uid='+rows[0].provider,
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
			                                title: '下载ip',
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
								thisyear_provider_datagrid.datagrid('unselectAll');
						
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
				            thisyear_provider_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            thisyear_provider_datagrid.datagrid('unselectAll');
				            }
				            }],

			});
       }
    }
});


/******************************************本年****************************************************************/

//本月累计服务下载数统计 0.服务 1.服务类型 2. 下载用户
$('#thismoth_center_tt').tabs({
border:false,  
    selected: 0,  
     onSelect:function(title,index){    
       if(index==0){
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterData/getDataThisMonthchart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   $("#thismoth_data_container").empty();
  							if ( msg.rows) {
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.typeName , item.downloadTotalTime];
                                      ids.push(obj);
                                  }
  							var chart = anychart.pieChart( ids);
  							 chart.title('数据本月下载量统计');
  		                    chart.container('thismoth_data_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax

    	   thismoth_data_datagrid = $("#thismoth_data_datagrid").datagrid
                              ({
                            	url : './ydVisiterData/getDataThisMonth.do',
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
						            var rows = thismoth_data_datagrid.datagrid('getSelections');
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
											url:'./ydVisiterData/getDetailMonthByTypeName.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
					                                title: '下载ip',
					                                field: 'uip',
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
										thismoth_data_datagrid.datagrid('unselectAll');
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
						            	thismoth_data_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
       
       }else if(index==1){//价格
    	   
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterData/getDataPriceThisMonth4chart.do",
               async: false,
              dataType: "json",
               success : function(msg) {
            	   console.log(msg);
            	   $("#thismoth_dataPrice_container").empty();
  							if (msg.rows) {
  							
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = [item.price , item.downloadTotalTime ];
                                      ids.push(obj);
                                  }
  								 var chart2 = anychart.columnChart();
  							 chart2.column(ids);
  							 chart2.title('数据本月下载量统计');
  							chart2.xAxis().title().text('价格');
  							chart2.yAxis().title().text('下载量');
  		                    chart2.container('thismoth_dataPrice_container').draw(); 					                        		
  		                    
  												                        		
  								}else{
  									console.log("返回值错误");
  								} 
  						},
  						error : function(data) {
  							console.log("ajax调取错误");
  						}
             
           });//ajax
    	   
    	   thismoth_dataPrice_datagrid = $("#thismoth_dataPrice_datagrid").datagrid({
							url : './ydVisiterData/getDataPriceThisMonth.do',
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
								title : '数据价格',
								field : 'price',
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
					            var rows = thismoth_dataPrice_datagrid.datagrid('getSelections');
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
										url:'./ydVisiterData/getDetailByPriceThisMonth.do?price='+rows[0].price,
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
				                                title: '下载ip',
				                                field: 'uip',
				                                width: 100,
				                                sortable: true
				                                
				                            }, {
				                                title: '下载时间',
				                                field: 'downloadDate',
				                                width: 100,
				                                sortable: true
				                                
				                            },
											
							            ] ]		
									});
									thismoth_dataPrice_datagrid.datagrid('unselectAll');
									
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
					            thismoth_dataPrice_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            thismoth_dataPrice_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});

       
       }  else if(index==2){
    	  
    	   $.ajax({
               cache: true,
               type: "POST",
               url:"./ydVisiterData/getAllUserThisMonth4chart.do",
               async: false,
              dataType: "json",
              contentType : "application/json",
               success : function(msg) {
            	 
            	   $("#thismoth_provider_container").empty();
  							if (msg.rows) {
  							
  								var ids = [];
  								for (var i = 0; i < msg.rows.length; i++) {
  									var item =  msg.rows[i];
  									var obj = {x: item.Provider , value: item.downloadTotalTime};
                                      ids.push(obj);
                                  }
  								 var chart3 = anychart.columnChart();
  	  							 chart3.column(ids);
  	  						 chart3.title('数据本月下载量统计');
  	  							chart3.xAxis().title().text('供应商');
  	  							chart3.yAxis().title().text('下载量');
  	  		                    chart3.container('thismoth_provider_container').draw(); 
  				
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
    	   thismoth_provider_datagrid = $(
					"#thismoth_provider_datagrid").datagrid({
						url : './ydVisiterData/getProviderThisMont.do',
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
							title : '数据供应商',
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
				            var rows = thismoth_provider_datagrid.datagrid('getSelections');
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
									url:'./ydVisiterData/getDetailProvider4Month.do?uid='+rows[0].provider,
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
			                                title: '下载ip',
			                                field: 'uip',
			                                width: 100,
			                                sortable: true,
			                                
			                            }, {
			                                title: '下载服务',
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
								thismoth_provider_datagrid.datagrid('unselectAll');
						
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
				            thismoth_provider_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            thismoth_provider_datagrid.datagrid('unselectAll');
				            }
				            }],

			});
       }
    }
});


/**********************************************本月************************************************************/

//本日累计服务下载数统计 0.服务 1.服务类型  2. 下载用户
$('#today_west_tt').tabs({
border:false,  
  selected: 0,  
   onSelect:function(title,index){    
     if(index==0){
  	 
    	  $.ajax({
              cache: true,
              type: "POST",
              url:"./ydVisiterData/getDataTodaychart.do",
              async: false,
             dataType: "json",
              success : function(msg) {
           	   $("#today_dataType_container").empty();
 							if ( msg.rows) {
 								var ids = [];
 								for (var i = 0; i < msg.rows.length; i++) {
 									var item =  msg.rows[i];
 									var obj = [item.typeName , item.downloadTotalTime ];
                                     ids.push(obj);
                                 }
 							var chart = anychart.pieChart( ids);
 							 chart.title('数据本日下载量统计');
 		                    chart.container('today_dataType_container').draw(); 					                        		
 		                    
 												                        		
 								}else{
 									console.log("返回值错误");
 								} 
 						},
 						error : function(data) {
 							console.log("ajax调取错误");
 						}
            
          });//ajax
   	   
   
    	  today_dataType_datagrid = $("#today_dataType_datagrid").datagrid
                             ({
                           	url : './ydVisiterData/getDataToday.do',
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
						            var rows = today_dataType_datagrid.datagrid('getSelections');
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
											url:'./ydVisiterData/getDetailDataTodayBytypeName.do?typeName='+encodeURI(encodeURI(rows[0].typeName)),
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
					                                title: '下载ip',
					                                field: 'uip',
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
										today_dataType_datagrid.datagrid('unselectAll');
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
						            	today_dataType_datagrid.datagrid('rejectChanges');
						            //取消被选择的号
						            	today_dataType_datagrid.datagrid('unselectAll');
						            }
						            }],
							
							});
     
     }else if(index==1){
    	 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiterData/getDataPriceToday4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
          	   console.log(msg);
          	   $("#today_dataPrice_container").empty();
							if (msg.rows) {							
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.price , item.downloadTotalTime ];
                                    ids.push(obj);
                                }
								 var chart2 = anychart.columnChart();
							 chart2.column(ids);
							 chart2.title('数据本日下载量统计');
							chart2.xAxis().title().text('数据价格');
							chart2.yAxis().title().text('下载量');
		                    chart2.container('today_dataPrice_container').draw(); 					                        		
		                    
												                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
  	 
    	 today_dataPrice_datagrid = $("#today_dataPrice_datagrid").datagrid({
							url : './ydVisiterData/getDataPriceToday.do',
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
								title : '数据价格',
								field : 'price',
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
					            var rows = today_dataPrice_datagrid.datagrid('getSelections');
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
										url:'./ydVisiterData/getDetailByPrice4today.do?price='+rows[0].price,
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
				                                title: '下载ip',
				                                field: 'uip',
				                                width: 100,
				                                sortable: true
				                                
				                            }, {
				                                title: '下载时间',
				                                field: 'downloadDate',
				                                width: 100,
				                                sortable: true
				                                
				                            },
											
							            ] ]		
									});
									today_dataPrice_datagrid.datagrid('unselectAll');
									
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
					            today_dataPrice_datagrid.datagrid('rejectChanges');
					            //取消被选择的号
					            today_dataPrice_datagrid.datagrid('unselectAll');
					            }
					            }],
						
						});
     
     }  else if(index==2){//本日供应商

  	   $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiterData/getProviderToday4chart.do",
             async: false,
            dataType: "json",
            contentType : "application/json",
             success : function(msg) {
          	 
          	   $("#today_provider_container").empty();
							if (msg.rows) {
							
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = {x: item.Provider , value: item.downloadTotalTime};
                                    ids.push(obj);
                                }
								 var chart3 = anychart.columnChart();
	  							 chart3.column(ids);
	  							chart3.title('数据本日下载量统计');
	  							chart3.xAxis().title().text('供应商');
	  							chart3.yAxis().title().text('下载量');
	  		                    chart3.container('today_provider_container').draw(); 
				
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
  	 today_provider_datagrid = $(
					"#today_provider_datagrid").datagrid({
						url : './ydVisiterData/getProviderToday.do',
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
							title : '数据供应商',
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
				            var rows = today_provider_datagrid.datagrid('getSelections');
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
									url:'./ydVisiterData/getDetailProviderToday.do?uid='+rows[0].provider,
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
			                                title: '下载ip',
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
								today_provider_datagrid.datagrid('unselectAll');
						
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
				            today_provider_datagrid.datagrid('rejectChanges');
				            //取消被选择的号
				            today_provider_datagrid.datagrid('unselectAll');
				            }
				            }],

			});
    	 
 
     }
  }
});

});






