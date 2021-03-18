function order(){

}

$(document).ready(function() {
	$('#Sessionchk1').on('click',function(){
		$.ajax({
		   	type: 'POST',
		   	datatype: 'json',
		   	url: 'sessionCheck',
		   	success: function(data) {
				if(data=="n"){
		      		alert("권한이 없습니다. 로그인을 하십시오.")
				}else{
					document.location.href = "freeBoardList";
				}
			},
		   	error: function(xhr, status, error) {
			alert('ajax error : ' + xhr.status + error);
		   	}
		});
	});
	$('#Sessionchk2').on('click',function(){
		$.ajax({
		   	type: 'POST',
		   	datatype: 'json',
		   	url: 'sessionCheck',
		   	success: function(data) {
				if(data=="n"){
		      		alert("권한이 없습니다. 로그인을 하십시오.")
				}else{
					document.location.href = "Order";
				}
			},
		   	error: function(xhr, status, error) {
			alert('ajax error : ' + xhr.status + error);
		   	}
		});
	});
	$('.btn-cartin-act').click(function(){
		var num = $(this).attr('data-num');
		var ordernum = $('#ordernum').val();
		var procode = $('#procode'+num).val();
		var qty = $('#qty'+num).val();
		$.ajax({
		   type: 'POST',
		   datatype: 'json',
		   data:{procode:procode,qty:qty,ordernum:ordernum},
		   url: 'orderInsert',
		   success: function(data) {
		      alert("담기가 완료되었습니다.");
		   },
		   error: function(xhr, status, error) {
		      alert('ajax error : ' + xhr.status + error);
		   }
		});

	});
	$('#orderconfirm').on('click',function(){
		var ordernum = $('#ordernum').val();
		$.ajax({
	   		type: 'POST',
	   		datatype: 'json',
	   		data:{ordernum:ordernum},
	   		url: 'orderConfirm',
	   		success: function(data) {
	      		alert("주문이 완료되었습니다.");
				document.location.href = "index";
		   	},
		   	error: function(xhr, status, error) {
		   	   alert('ajax error : ' + xhr.status + error);
		   	}
		});
	});
	$('#ordercancle').on('click',function(){
		var ordernum = $('#ordernum').val();
		$.ajax({
	   		type: 'POST',
	   		datatype: 'json',
	   		data:{ordernum:ordernum},
	   		url: 'orderCancle',
	   		success: function(data) {
	      		alert("주문이 취소되었습니다.");
				document.location.href = "index";
		   	},
		   	error: function(xhr, status, error) {
		   	   alert('ajax error : ' + xhr.status + error);
		   	}
		});
	});
	$('#nofirmorderlist').DataTable({
		deferRender: true,
		scrollY: 360,
		scrollCollapse: true
	});
	
	



})