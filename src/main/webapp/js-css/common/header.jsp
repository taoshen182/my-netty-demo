<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld" %>
<!--页面头部-->
<header class="am-topbar admin-header" id="main-header">

    <div class="am-topbar-brand am-hide-lg">
        <img src="/h5APP/amazui/img/logo.png" width="60" height="42"/>
    </div>
    <div class="am-topbar-brand am-show-lg-only ">
        <img src="/h5APP/amazui/img/logo.png" width="50" height="38" style="margin-bottom: 5px;"/>
        <small style="font-size: 1.8rem;"><fmt:message key="system_name"/></small>
    </div>
    <button class="am-topbar-btn am-topbar-toggle pui-btn  pui-btn-shadow pui-btn-secondary am-show-sm-only"
            data-am-collapse="{target: '#topbar-collapse'}" style="margin-top:0.5rem;right: 0.5em;"><span
            class="am-sr-only"><fmt:message key="navigation_switch"/></span> <span class="am-icon-bars"></span></button>

    <div class="am-collapse am-topbar-collapse" id="topbar-collapse">
        <ul
                class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
            <%--<li><a target="main" href="new_index_first.jsp"><span class="am-icon-cogs"></span><fmt:message
                    key="change_theme"/></a></li>--%>
           <%-- <li class="am-dropdown" data-am-dropdown>
                <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;"><span
                        class="am-icon-envelope-o"></span> <fmt:message key="my_job"/> &nbsp;<span
                        class="am-badge am-badge-warning" style="border-radius: 4px;" id="my_AllCnt">0</span> <i
                        class="am-icon-caret-down"></i> </a>
                <ul class="am-dropdown-content">
                    <li>
                        <a href="/workflow.JobActionHandler.listMyToDo?sort=0" target="main"
                           onclick="close_drop()"><span class="am-icon-pencil-square-o"></span> <fmt:message
                                key="approve_job"/>&nbsp;<span class="am-badge am-badge-warning"
                                                               style="border-radius: 4px;" id="todo_Cnt"></span> </a>
                    </li>

                </ul>
            </li>--%>


            <li class="am-dropdown" data-am-dropdown><a class="am-dropdown-toggle" data-am-dropdown-toggle
                                                        href="javascript:;">
                <img src="/basic.MongoFileService.getFile?id=${sessionScope.curuser.pic}&type=annexfile"
                     onerror="this.src='/h5APP/amazui/img/u_head.png'" class="am-circle" width="40" height="40"/>
                Welcome！ ${sessionScope.curuser.username} <span class="am-icon-caret-down"></span>
            </a>
                <ul class="am-dropdown-content">
                    <li>
                        <a href="/workflow.JobActionHandler.listMyToDo?sort=0" target="main"
                           onclick="close_drop()"><span class="am-icon-pencil-square-o"></span> <fmt:message
                                key="approve_job"/>&nbsp;<span class="am-badge am-badge-warning"
                                                               style="border-radius: 4px;" id="todo_Cnt"></span> </a>
                    </li>
                    <%--<li><a href="" onclick="close_drop()"><span class="am-icon-user-md"></span> <fmt:message key="person_set"/></a></li>--%>
                    <%--<li><a href="/programme.ProgrammeAH.list" target="main" onclick="close_drop()">
                        <span class="am-icon-heart-o"></span> <fmt:message key="attention.set"/></a></li>--%>
                    <li><a href="/template/basic/pwdModify.jsp" onclick="close_drop()"><span
                            class="am-icon-user-secret"></span> <fmt:message key="modify_pwd"/></a></li>
                    <li><a href="/basic.LoginAH.exit" onclick="close_drop()"><span class="am-icon-power-off"></span>
                        <fmt:message key="exit"/></a></li>
                </ul>
            </li>
            <!-- <li class="am-hide-sm-only"><a href="javascript:;" id="admin-fullscreen"><span class="am-icon-arrows-alt"></span> <span class="admin-fullText">开启全屏</span></a></li>-->
        </ul>
    </div>
</header>
<!--页面头部-->

<%--
<script type="text/javascript">
function getNormalMyToDoCnt2(){

	$.post("/terminal.TerminalParamAH.getCntAll",function(result){
	

		if(result.respcode=='0000'){
			var allCnt=parseInt(result.todocnt)+parseInt(result.unprocnt);
			$("#my_AllCnt").html(allCnt);
			$("#todo_Cnt").html(result.todocnt);
			$("#temp_Cnt").html(result.unprocnt);
		}
	},"json");
}


function close_drop(){
	$('.am-dropdown').dropdown('close');
}

getNormalMyToDoCnt2();
self.setInterval("getNormalMyToDoCnt2()",300000);//5分钟执行一次
</script>--%>
