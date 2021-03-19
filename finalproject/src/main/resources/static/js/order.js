function order(){

}

$(document).ready(function() {
   $('.btn-orderout-act').on('click',function(){
      var ordernum = $(this).attr('data-num');
      $.ajax({
            type: 'POST',
            datatype: 'json',
          data:{ordernum:ordernum},
            url: 'QuickOrderCancle',
            success: function(data) {
            alert("주문이 취소처리되었습니다.")
            document.location.href = "noconfirmList";
         },
            error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
            }
      });
   });
   $('.btn-orderin-act').on('click',function(){
      var ordernum = $(this).attr('data-num');
      $.ajax({
            type: 'POST',
            datatype: 'json',
          data:{ordernum:ordernum},
            url: 'NowStockChk',
            success: function(data) {
            if(data=="end"){
                  alert("이 주문을 마지막으로 매진되는 재고가 있습니다. 확인바랍니다.");
               document.location.href = "QuickorderConfirm?ordernum=" + ordernum;               
            }else if(data=="n"){
               alert("주문 재고 중 부족한 재고가 있습니다. 확인바랍니다.");
            }else{
               alert("주문 접수처리가 완료되었습니다.");
               document.location.href = "QuickorderConfirm?ordernum=" + ordernum;
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
            document.location.href = "membermypage";
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
      aaSorting: [],
      deferRender: true,
      scrollY: 360,
      scrollCollapse: true
   });
   
   



})