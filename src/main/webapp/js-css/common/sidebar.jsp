<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="m" uri="/WEB-INF/sand-html.tld" %>

<%@ page contentType="text/html;charset=utf-8" %>
<div class="admin-sidebar am-offcanvas" id="admin-offcanvas">
    <div class="am-offcanvas-bar admin-offcanvas-bar">
        <ul class="am-list admin-sidebar-list">
            <li><a href="/basic.HomeAH.viewPlugins" target="main"><span class="am-icon-home"></span><fmt:message
                    key="homepage"/></a></li>

            <%--商品--%>
            <li class="admin-parent">
                <a class="am-cf" data-am-collapse="{target: '#j_G'}">
                    <span class="am-icon-building-o"></span> 商品管理 <span
                        class="am-icon-angle-right am-fr am-margin-right"></span>
                </a>
                <ul class="am-list am-collapse admin-sidebar-sub" id="goods">

                    <li><a target="main" href="/goods.GoodsInfoAH.list">商品管理</a></li>


                    <li><a target="main" href="/tag.TagAH.listGroup">商品分类</a></li>

                    <li><a target="main" href="/extend.ExtendAH.list">附加信息</a></li>

                </ul>
            </li>

            <%--商户--%>
            <li class="admin-parent">
                <a class="am-cf" data-am-collapse="{target: '#j_G'}">
                    <span class="am-icon-building-o"></span> 商户管理 <span
                        class="am-icon-angle-right am-fr am-margin-right"></span>
                </a>
                <ul class="am-list am-collapse admin-sidebar-sub" id="mer">

                    <li><a target="main" href="/merchant.MerAH.show">创建商户</a></li>


                    <li><a target="main" href="/merchant.MerAH.list">商户档案</a></li>

                </ul>
            </li>

            <m:cando functionid="sys_set,workflow,user">
                <%--系统设置--%>
                <li class="admin-parent">
                    <a class="am-cf" data-am-collapse="{target: '#j_G'}">
                        <span class="am-icon-building-o"></span> <fmt:message key="system.set"/> <span
                            class="am-icon-angle-right am-fr am-margin-right"></span>
                    </a>
                    <ul class="am-list am-collapse admin-sidebar-sub" id="sys">

                        <m:cando functionid="user_list">
                            <li><a target="main" href="/basic.UserAH.listUser"><fmt:message key="user.manager"/></a>
                            </li>
                        </m:cando>


                        <m:cando functionid="module_list">
                            <li><a target="main" href="/basic.ModuleAH.listModule"><fmt:message
                                    key="module.manager"/> </a></li>
                        </m:cando>

                        <m:cando functionid="role_list">
                            <li><a target="main" href="/basic.RoleAH.listRole"><fmt:message key="role.manager"/></a>
                            </li>
                        </m:cando>

                        <m:cando functionid="taskmanager">
                            <li><a target="main" href="/task.TaskAH.listTask"><fmt:message key="timer_schedule"/></a>
                            </li>
                        </m:cando>
                        <%--<m:cando functionid="erplog">
                            <li><a target="main" href="/basic.InterfaceLogAH.list"> <fmt:message
                                    key="interface_log"/></a></li>
                        </m:cando>--%>


                    </ul>
                </li>
            </m:cando>

        </ul>
        </ul>
        </li>


    </div>
</div>

<script type="text/javascript">
    var materalColor = localStorage.getItem('material-color');

    var is_in = false;


    $('.admin-sidebar-sub').delegate('li', 'click', function (e) {
        $(".admin-sidebar-sub li").each(function (i) {
            $(this).removeClass("li_active");
            $(this).removeAttr('style');

        });
        e = e || event;
        $(this).addClass('li_active');

        if (materalColor) {
            $('.li_active').css('background', '' + materalColor + '');

        }


        e.stopPropagation();
    })

    //$('.am-cf').on('click',function(){
    //	$('.admin-sidebar-sub').removeClass('am-in');
    //	var obj=$(this).next('.admin-sidebar-sub');
    //		if(obj.hasClass('am-in')){
    //		obj.removeClass('am-in');
    //	}
    //	else{
    //		obj.addClass('am-in');
    //	}
    //
    //	event.stopPropagation();
    //})

    $('.admin-parent').bind('click', function (e) {

        e = e || event;
        $(".admin-parent").each(function (i) {

            $(this).removeClass("li_active");
            $(this).removeAttr('style');

        });
        var obj = $(this).find('.admin-sidebar-sub');
        if (is_in) {
            if (obj.hasClass('am-in')) {
                obj.removeClass('am-in');
                is_in = false;

            }
            else {
                $('.admin-sidebar-sub').removeClass('am-in');
                obj.addClass('am-in');
                $(this).addClass('li_active');
                if (materalColor) {
                    $('.li_active').css('background', '' + materalColor + '');

                }
                is_in = true;
            }
        }
        else {
            $('.admin-sidebar-sub').removeClass('am-in');
            obj.addClass('am-in');
            $(this).addClass('li_active');
            if (materalColor) {
                $('.li_active').css('background', '' + materalColor + '');

            }
            is_in = true;
        }


        e.stopPropagation();
    })


</script>
  


  

  