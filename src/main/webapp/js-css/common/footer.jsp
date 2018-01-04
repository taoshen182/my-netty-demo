<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<footer class="admin-content-footer" style="border-top: 1px solid #ddd;">

	<p class="am-padding-left" style="margin-bottom: 0;">© 2016 <fmt:message key="main-foot"/>,PAX.</p>
</footer>



 <a id="hand-offcanvas"  class="am-icon-btn am-secondary am-icon-th-list  admin-menu" style="width: 40px;height: 40px;font-size: 22px;line-height: 40px;" data-am-offcanvas="{target: '#admin-offcanvas'}" data-rel='close'></a>


<script>
  $(function() {
    var id = '#admin-offcanvas';
    var $myOc = $(id);
    $('.admin-menu').on('click', function() {
    	var status=$('#hand-offcanvas').attr('data-rel');
    	var winWidth= window.innerWidth;
    	
  	if(winWidth>638){
  		 
    	
    	if(status=='open')
    	{ 
    			$('.admin-sidebar').removeClass('am-hide');
 		$myOc.offCanvas('open');
    
    	$('#hand-offcanvas').attr('data-rel','close');
    		
    	}
    	else{
      		$myOc.offCanvas('close');
    			$('.admin-sidebar').addClass('am-hide');
    			$('#hand-offcanvas').attr('data-rel','open');
    	}
     
    }
    });

    
  });
</script>