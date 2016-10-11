/**
 * 
 */
/**
	 * 数据访问量实时动态
	 */
	FMG.getDataOntime = function() {
		 $.ajax({
             cache: true,
             type: "POST",
             url:"./yddata/getAllData4chart.do",
             async: false,
            dataType: "json",
             success : function(msg) {
							if (msg && msg.success == true) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.dId , item.downloadTotalTime ];
                                    ids.push(obj);
                                }
							$("#container").empty();
							
							 chart = anychart.pie3d(ids);

							                     // set container id for the chart
							                     chart.container('container');

							                     // set chart title text settings
							                     chart.title('数据最新访问动态');
							                     
							                     //set chart radius
							                     chart.radius('43%');

							                     // create empty area in pie chart
							                     chart.innerRadius('30%');

							                     // initiate chart drawing
							                     chart.draw();
      		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
	
		
		$("#audit_datagrid").datagrid({
			url : './yddata/getAllData.do',
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
				title : '数据编号',
				field : 'dId',
				width : 30,
				fit : true,
				
			},{
				title : '数据类型',
				field : 'typeName',
				width : 30,
				sortable : true,
				
			},{
				title : '数据供应商',
				field : 'provider',
				width : 50,
				sortable : true,
				
			},{
				title : '数据访问次数',
				field : 'downloadTotalTime',
				width : 30,
				sortable : true,
				
			}
            ] ],
            toolbar:[{
	            text:'详细情况',
	            iconCls : 'icon-large-chart',
	            handler : function(){
	            var rows = $("#audit_datagrid").datagrid('getSelections');
				if(rows.length >1) {
				var names = [];
				for(var i =0; i<rows.length; i++){
				names.push(rows[i].dId);
				}
				$.messager.show({	
				msg : '只能选择一个数据进行查看或者导出,您已经选了【'+names.join(",")+'】' + rows.length +'个数据',
				title : '提示'
				});
				} else if(rows.length == 0){
				$.messager.show({
				msg : '请选择一个数据进行查看',
				title : '提示'
				});
				}else if(rows.length == 1){
					
				 $('#audit_DetailDialog').dialog({
						});
						$("#audit_Detail_datagrid").datagrid({
						url:'./ydVisiterData/getDetailById.do?did='+rows[0].dId,
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
                                title: '用户',
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
                                field: 'downloadDate',
                                width: 100,
                                sortable: true,
                                
                            },
							
			            ] ]		
					});
						$('#audit_datagrid').datagrid('unselectAll');
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
	            	$("#audit_datagrid").datagrid('unselectAll');
	            }
	            }]	
		});//audit_service_datagrid
	};//数据访问量实时动态
