/**
 * 视图管理首页
 */

	/**
	 * 服务访问量实时动态
	 */
	FMG.serviceVisitedTop10 = function() {
		 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydService/getTop3Service.do",
             async: false,
            dataType: "json",
             success : function(msg) {
							if (msg && msg.success == true) {
								var ids = [];
								for (var i = 0; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.sId , item.visiteTotalTime ];
                                    ids.push(obj);
                                }
							$("#serviceaVisited_container").empty();
							anychart.licenseKey('YOUR-LICENSE-KEY');
							var chart = anychart.pieChart( ids);
				            chart.title().text('服务访问Top3');
				            chart.container('serviceVisited_container').draw(); 						                        					                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
	
		
	};//服务访问量实时动态
	
	/**
	 * 用户访问Top10
	 */
	FMG.userVisitedTop10 = function() {
		 $.ajax({
             cache: true,
             type: "POST",
             url:"./ydVisiter/getTop3Users.do",
             async: false,
            dataType: "json",
             success : function(msg) {
							if (msg && msg.success == true) {
								var ids = [];
								var champion =  {
									      x: msg.rows[0].uid,             // set x
									      value:  msg.rows[0].visiteTimes,           // set value
									      marker:{                // marker settings
									        type:'star5',         // marker type
									        fill:'gold',          // marker color
									        size: 12,             // marker size
									        enabled: true         // initiate marker draw
									      },
									      hoverMarker: {size: 22} // adjust marker size on mouse over
									    };
								 ids.push(champion);
								for (var i = 1; i < msg.rows.length; i++) {
									var item =  msg.rows[i];
									var obj = [item.uid , item.visiteTimes ];
                                    ids.push(obj);
                                }
							$("#userVisited_container").empty();
							 anychart.licenseKey('YOUR-LICENSE-KEY');
							var chart = anychart.columnChart( ids);
				            chart.title().text('用户访问Top3');
				            chart.xAxis().title().text('用户标识');
				            chart.yAxis().title().text('访问次数');
				            chart.container('userVisited_container').draw(); 						                        					                        		
								}else{
									console.log("返回值错误");
								} 
						},
						error : function(data) {
							console.log("ajax调取错误");
						}
           
         });//ajax
	};
		 /**
			 * 数据访问Top10
			 */
			FMG.dataVisitedTop10 = function() {
				 $.ajax({
		             cache: true,
		             type: "POST",
		             url:"./yddata/getTop3Data.do",
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
									$("#dataVisited_container").empty();
									 anychart.licenseKey('YOUR-LICENSE-KEY');
									var chart = anychart.pieChart( ids);
						            chart.title().text('数据访问Top3');
						            chart.container('dataVisited_container').draw(); 						                        					                        		
										}else{
											console.log("返回值错误");
										} 
								},
								error : function(data) {
									console.log("ajax调取错误");
								}
		           
		         });//ajax
			
		
	};//数据访问量top10
	
	
	 /**
	 * 服务器监控
	 */
	FMG.Monitoring = function() {
        Monitoring_datagrid = $("#Monitoring_datagrid").datagrid({
            url: './monitoring/getServiceMonitoring.do',
            title: '',
            iconCls: 'icon-save',
            cache: false,
            loadMsg: "查找中，请等待 …",
          pagination: true,
          pageSize: 10,
          pageList: [10, 20, 50],
          fit: true,
          fitColumns: true,
          nowrap: false,
          border: false,
          treeField: 'text',
          idField: 'id',
          sortName: '',
          sortOrder: 'desc',
          columns: [[{
                title: '编号',
                field: 'id',
                width: 20,
                sortable: true,
                //添加点击排序   
                checkbox: true
            },
            {
                title: '服务名称',
                field: 'name',
                width: 100,
                sortable: true,
                editor: {
                    type: 'validatebox',
                    options: {
                        required: true,
                        validType: 'chinese'
                    }
                }
            },{
                title: '访问地址',
                field: 'url',
                width: 400,
                sortable: true,
                editor: {
                    type: 'validatebox',
                    options: {
                        required: true,
                         
                    }
                }
            },
            {
                title: '情况',
                field: 'imgSrc',
                align:'center',
                formatter: function(value,row,index){
                	return '<img src="'+row.imgSrc+'" />';
    			}, 
                width: 100,
                sortable: true,
                
            },{
                title: '返回代码',
                field: 'code',
                width: 100,
                sortable: true,
                
            },{
                title: '状态',
                field: 'statut',
                width: 100,
                sortable: true,
                
            }]]

        });

	

};//数据访问量top10