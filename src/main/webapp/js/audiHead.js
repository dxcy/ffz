$(document).ready(function(){
	$("#audit_systembox").html(' <div id="audit_systembtn" class="audit_systembtn"><a href="javascript:void(0)" class="audit_abtn"><img src="./img/nav_Audit_li1.png" /></a><div id="audit_menu" class="audit_menu"><dl><dt><a href="javascript:void(0)" onclick="FMG.getServiceDetail();">实时动态</a></dt></dl><dl><dt><a  href="javascript:void(0)"  onclick="FMG.getServiceCurrentTotal();">当前累计</a></dt></dl><dl><dt><a href="javascript:void(0)" onclick="FMG.getServiceHistroyTotal();">历史数据</a></dt></dl></div></div>'
			+'<div id="audit_usebtn" class="audit_usebtn"><a href="javascript:void(0)" class="audit_useabtn"><img src="./img/nav_Audit_li2.png" /></a><div id="audit_usemenu" class="audit_usemenu"><dl><dt><a href="javascript:void(0)" onclick="FMG.getUsersOntimel();">实时动态</a></dt></dl><dl><dt><a href="javascript:void(0)" onclick="FMG.getUserTotal();">当前累计</a></dt></dl><dl><dt><a href="javascript:void(0)"onclick="FMG.getUserHistory();">历史数据</a></dt></dl></div></div>'+
	'<div id="audit_downbtn" class="audit_downbtn"><a href="javascript:void(0)" class="audit_downabtn"><img src="./img/nav_Audit_li3.png" /></a><div id="audit_downmenu" class="audit_downmenu"><dl><dt><a href="javascript:void(0)" onclick="FMG.getDownloadOntime();">实时动态</a></dt></dl><dl><dt><a href="javascript:void(0)" onclick="FMG.getDataTotal();">当前累计</a></dt></dl><dl><dt><a href="javascript:void(0)" onclick="FMG.DataHistory();">历史数据</a></dt></dl></div></div>'+
	'<div id="audit_pagebtn" class="audit_pagebtn"><a href="javascript:void(0)" class="audit_pageabtn"><img src="./img/nav_page.png" /></a><div id="audit_pagemenu" class="audit_pagemenu"><dl><dt><a href="javascript:void(0)" onclick="FMG.getPageOntime();">实时动态</a></dt></dl><dl><dt><a href="javascript:void(0)" onclick="FMG.getPageTotal();">当前累计</a></dt></dl><dl><dt><a href="javascript:void(0)" onclick="FMG.getPageHistory();">历史数据</a></dt></dl></div></div>'+
	'<div id="audit_managebtn" class="audit_managebtn"><a href="javascript:void(0)" class="audit_manageabtn"><img src="./img/nav_Audit_li4.png" /></a><div id="audit_managemenu" class="audit_managemenu"><dl><dt><a href="javascript:void(0)" onclick="FMG.goViewAdmin();">视图管理</a></dt></dl></div></div>');
	

	var audit_systembtn = $("#audit_systembtn");
	var audit_menu = $("#audit_menu");
	audit_systembtn.mouseenter(function(){
		t_delay= setTimeout(function(){
			audit_menu.fadeIn("slow");
		},200);
	});
	audit_systembtn.mouseleave(function(){
		clearTimeout(t_delay);
		audit_menu.fadeOut("slow");
	});
	
	var audit_usebtn = $("#audit_usebtn");
	var audit_usemenu = $("#audit_usemenu");
	audit_usebtn.mouseenter(function(){
		tuse_delay= setTimeout(function(){
			audit_usemenu.fadeIn("slow");
		},200);
	});
	audit_usebtn.mouseleave(function(){
		clearTimeout(tuse_delay);
		audit_usemenu.fadeOut("slow");
	});
	
	var audit_pagebtn = $("#audit_pagebtn");
	var audit_pagemenu = $("#audit_pagemenu");
	audit_pagebtn.mouseenter(function(){
		tuse_delay= setTimeout(function(){
			audit_pagemenu.fadeIn("slow");
		},200);
	});
	audit_pagebtn.mouseleave(function(){
		clearTimeout(tuse_delay);
		audit_pagemenu.fadeOut("slow");
	});
	
	
	var audit_downbtn = $("#audit_downbtn");
	var audit_downmenu = $("#audit_downmenu");
	audit_downbtn.mouseenter(function(){
		tdown_delay= setTimeout(function(){
			audit_downmenu.fadeIn("slow");
		},200);
	});
	audit_downbtn.mouseleave(function(){
		clearTimeout(tdown_delay);
		audit_downmenu.fadeOut("slow");
	});
	
	var audit_managebtn = $("#audit_managebtn");
	var audit_managemenu = $("#audit_managemenu");
	audit_managebtn.mouseenter(function(){
		tmanage_delay= setTimeout(function(){
			audit_managemenu.fadeIn("slow");
		},200);
	});
	audit_managebtn.mouseleave(function(){
		clearTimeout(tmanage_delay);
		audit_managemenu.fadeOut("slow");
	});
});