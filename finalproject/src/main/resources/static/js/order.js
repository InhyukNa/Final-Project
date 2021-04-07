var checkUnload = true;
var isRun = false;
function pageOut() {
	if (isRun == true) {
		return;
	};
	isRun = true;
	checkUnload = true;
	$(window).on("beforeunload", function() {
		if (checkUnload) {
			var ordernum = $('#ordernum').val();
			$.ajax({
				type: 'POST',
				datatype: 'json',
				data: { ordernum: ordernum },
				url: 'orderpageout',
				success: function(data) {
					alert('주문중인 정보는 삭제됩니다.');
					isRun = false;
				},
				error: function(xhr, status, error) {
					alert('ajax error : ' + xhr.status + error);
				}
			});
		}
	});
}
function pageOutCancel(){
	checkUnload=false;
}
$(document).ready(function() {
	/** 관리자 주문 들어온 리스트에서 주문 접수처리하는 Script */
	$('.btn-orderin-act').on('click', function() {
		var ordernum = $(this).attr('data-num');
		$.ajax({
			type: 'POST',
			datatype: 'json',
			data: { ordernum: ordernum },
			url: 'NowStockChk',
			success: function(data) {
				if (data == "end") {
					alert("이 주문을 마지막으로 매진되는 재고가 있습니다. 확인바랍니다.");
					document.location.href = "QuickorderConfirm?ordernum=" + ordernum;
				} else if (data == "n") {
					alert("주문 재고 중 부족한 재고가 있습니다. 확인바랍니다.");
				} else {
					lert("주문 접수처리가 완료되었습니다.");
					document.location.href = "QuickorderConfirm?ordernum=" + ordernum;
				}
			},
			error: function(xhr, status, error) {
				alert('ajax error : ' + xhr.status + error);
			}
		});
	});

	/** 관리자 주문 들어온 리스트에서 주문 접수 취소 처리하는 Script */
	$('.btn-orderout-act').on('click', function() {
		var ordernum = $(this).attr('data-num');
		$.ajax({
			type: 'POST',
			datatype: 'json',
			data: { ordernum: ordernum },
			url: 'QuickOrderCancle',
			success: function(data) {
				alert("주문이 취소처리되었습니다.")
				document.location.href = "OrderList";
			},
			error: function(xhr, status, error) {
				alert('ajax error : ' + xhr.status + error);
			}
		});
	});

	/**온라인 주문 들어가기전 세션 체크 */
	$('#Sessionchk2').on('click', function() {
		$.ajax({
			type: 'POST',
			datatype: 'json',
			url: 'sessionCheck',
			success: function(data) {
				if (data == "n") {
					alert("권한이 없습니다. 로그인을 하십시오.")
				} else {
					document.location.href = "Order";
				}
			},
			error: function(xhr, status, error) {
				alert('ajax error : ' + xhr.status + error);
			}
		});
	});

	/**온라인 주문 페이지 재료 담기 Script */
	$('.btn-cartin-act').click(function() {
		var num = $(this).attr('data-num');
		var ordernum = $('#ordernum').val();
		var procode = $('#procode' + num).val();
		var qty = $('#qty' + num).val();
		$.ajax({
			type: 'POST',
			datatype: 'json',
			data: { procode: procode, qty: qty, ordernum: ordernum },
			url: 'orderInsert',
			success: function(data) {
				if(data == "y"){
					alert("담기가 완료되었습니다.");
					checkUnload = false;
					document.location.href = "orderReloading?ordernum=" + ordernum;
				} else if(data == "n"){
					alert("재고가 부족합니다")
				}
			},
			error: function(xhr, status, error) {
				alert('ajax error : ' + xhr.status + error);
			}
		});
	});

	/** 온라인 주문 페이지 주문 접수 */
	$('#orderconfirm').on('click', function() {
		var ordernum = $('#ordernum').val();
		var totprice = $('#totprice').text();
		checkUnload = false;
		if (totprice >= 6000) {
			document.location.href = "orderConfirm?ordernum=" + ordernum;
		}
		else if (totprice == 0) {
			alert('재료를 담아주세요');
		} else {
			alert('6000원 이상 주문 가능합니다')
		}
	});

	$('#ordernameCss2').keyup(function() {
		var usePoint = $('#ordernameCss2').val();
		var totPoint = $('#maxPoint').val();
		var orderPrice = $('#orderPrice').text();
		var totPrice = orderPrice - usePoint;
		if (totPoint - usePoint <= 0) {
			if (orderPrice - totPoint > 0) {
				$('#ordernameCss2').val(totPoint);
				$('#totalPrice').text(orderPrice - totPoint);
				$('#orderPoint').text(totPoint);
				$('#paymentstrong1').text(orderPrice - totPoint);
			} else {
				$('#ordernameCss2').val(orderPrice);
				$('#orderPoint').text(orderPrice);
				$('#totalPrice').text(0);
				$('#paymentstrong1').text(0);
			}
		}
		else {
			$('#orderPoint').text(usePoint);
			$('#totalPrice').text(totPrice);
			$('#paymentstrong1').text(orderPrice - usePoint);
		}

	});

	/** 온라인 주문 페이지 주문 취소 */
	$('#ordercancle').on('click', function() {
		var ordernum = $('#ordernum').val();
		$.ajax({
			type: 'POST',
			datatype: 'json',
			data: { ordernum: ordernum },
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

	/** 관리자 미확인 주문 리스트 DataTables Library */
	$('#nofirmorderlist').DataTable({
		aaSorting: [],
		deferRender: true,
		scrollY: 360,
		scrollCollapse: true
	});
	$('.detailproduct').on('mouseover', function() {
		var num = $(this).attr('data-num')
		$('#proimage' + num).css('display', 'none');
		$('#afterproduct' + num).css('display', 'block');
		$('#afterproduct' + num).css('opacity', '100');
	});

	$('.detailproduct').on('mouseleave', function() {
		var num = $(this).attr('data-num')
		$('#proimage' + num).css('display', 'block');
		$('#afterproduct' + num).css('display', 'none');
		$('#afterproduct' + num).css('opacity', '0');
	});
	$(document).on('click', '#tableid td #ordercarticon', function() {
		var row = $(this).closest('tr');
		var td = row.children();
		var ordernum = td.eq(0).text();
		var proname = td.eq(1).text();
		var proprice = td.eq(3).text();
		var totprice = $('#totprice').text();
		$.ajax({
			type: 'POST',
			datatype: 'json',
			data: { ordernum: ordernum, proname: proname },
			url: 'ordercartdelete',
			success: function(data) {
				row.remove();
				totprice = totprice - proprice;
				$('#totprice').text(totprice);
				alert("주문이 취소되었습니다.");
			},
			error: function(xhr, status, error) {
				alert('ajax error : ' + xhr.status + error);
			}
		});
	})
	$('#detailcheckbox').on('click', function() {
		$('#ordernameCss').val($('#orignname').text());
		$('.orderaddressCss').val($('#orignzipcode').text());
		$('.orderaddressCss2').val($('#orignaddress').text());
		$('.orderaddressCss3').val($('#origndetailaddress').text());
		$('#orderphoneCss').val($('#orignpone1').text());
		$('#orderphoneCss2').val($('#orignpone2').text());
		$('#orderphoneCss3').val($('#orignpone3').text());
		$('#orderemailCss').val($('#orignemail').text());
	});

	$('#checkednew').on('click', function() {
		$('#ordernameCss').val("");
		$('.orderaddressCss').val("");
		$('.orderaddressCss2').val("");
		$('.orderaddressCss3').val("");
		$('#orderphoneCss').val("");
		$('#orderphoneCss2').val("");
		$('#orderphoneCss3').val("");
		$('#orderemailCss').val("");
	});
})