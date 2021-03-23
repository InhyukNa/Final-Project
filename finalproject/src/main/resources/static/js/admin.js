/* 관리자 페이지 5개 이하 재료 확인 div 전환 */
function outstockChange1() {
   $('#stockoutdiv').css('display', 'none');
   $('#stockmanagediv').css('display', 'block')
};

function outstockChange2() {
   $('#stockoutdiv').css('display', 'block');
   $('#stockmanagediv').css('display', 'none')
};
/*END*/


$(document).ready(function() {
   /*Bar Chart START*/
   var name = [];
   var stock = [];
   $.ajax({
      type: 'POST',
      datatype: 'json',
      url: 'productDataSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            name.push(data[i].name);
            stock.push(data[i].stock);
         }
         
      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });
   $("#adminChart1").on('click',function(){
      var ctx = document.getElementById("Chart1").getContext('2d');
        var Chart1 = new Chart(ctx, {
           type: 'bar',
           data: {
              labels: name,
              datasets: [{


                 data: stock, //컨트롤러에서 모델로 받아온다.
                 backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                      'rgba(120, 52, 50, 0.2)',
               'rgba(99, 84, 20, 0.2)',
               'rgba(200, 120, 230, 0.2)',
               'rgba(170, 20, 180, 0.2)',
               'rgba(221, 2, 92, 0.2)',
               'rgba(142, 37, 168, 0.2)',
               'rgba(196, 37, 140, 0.2)',
               'rgba(112, 77, 250, 0.2)',
               'rgba(255, 55, 70, 0.2)'                              
                 ],
                 borderColor: [
                'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                      'rgba(120, 52, 50, 1)',
               'rgba(99, 84, 20, 1)',
               'rgba(200, 120, 230, 1)',
               'rgba(170, 20, 180, 1)',
               'rgba(221, 2, 92, 1)',
               'rgba(142, 37, 168, 1)',
               'rgba(196, 37, 140, 1)',
               'rgba(112, 77, 250, 1)',
               'rgba(255, 55, 70, 1)'   

                 ],
                 borderWidth: 1
              }
              ]
           },
           options: {
              scales: {
                 yAxes: [{
                    ticks: {
                       beginAtZero: true
                    }
                 }]
              },
         legend: {
               display: false,
               
             },
           }
        });
   });
   /*Bar Chart END*/
   
   /*Pie Chart START*/
   var salename = [];
   var salestock = [];
   $.ajax({
      type: 'POST',
      datatype: 'json',
      url: 'SaleProductDataSelect',
      success: function(data) {
         for (var i = 0; i < data.length; i++) {
            salename.push(data[i].salename);
            salestock.push(data[i].salestock);
         }
         
      },
      error: function(xhr, status, error) {
         alert('ajax error : ' + xhr.status + error);
      }
   });
   
   
   $("#adminChart2").on('click',function(){
      var ctx = document.getElementById("Chart2").getContext('2d');
        var Chart2 = new Chart(ctx, {
           type: 'pie',
           data: {
              labels: salename,
              datasets: [{


                 data: salestock, //컨트롤러에서 모델로 받아온다.
                 backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                      'rgba(120, 52, 50, 0.2)',
               'rgba(99, 84, 20, 0.2)',
               'rgba(200, 120, 230, 0.2)',
               'rgba(170, 20, 180, 0.2)',
               'rgba(221, 2, 92, 0.2)',
               'rgba(142, 37, 168, 0.2)',
               'rgba(196, 37, 140, 0.2)',
               'rgba(112, 77, 250, 0.2)',
               'rgba(255, 55, 70, 0.2)'                              
                 ],
                 borderColor: [
                'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                      'rgba(120, 52, 50, 1)',
               'rgba(99, 84, 20, 1)',
               'rgba(200, 120, 230, 1)',
               'rgba(170, 20, 180, 1)',
               'rgba(221, 2, 92, 1)',
               'rgba(142, 37, 168, 1)',
               'rgba(196, 37, 140, 1)',
               'rgba(112, 77, 250, 1)',
               'rgba(255, 55, 70, 1)'   

                 ],
                 borderWidth: 1
              }
              ]
           },
           options: {
              scales: {
                 yAxes: [{
                    ticks: {
                       beginAtZero: true
                    }
                 }]
              },
         legend: {
               display: false,
               
             },
           }
        });
   });
   /*Pie Chart END*/
   
   /* 제품 등록시 image 추가 script */
   $(".imagebtn").on('click', function() {
      $('#imagefile').click();
      $('#imagefile').change(function() {
         var imgfilename = $('#imagefile').val();
         $('.imagename').attr('value', imgfilename);
      });
   });
   /*END*/
   
   /** 제품 목록 DataTables Library */
   $('#productlist').DataTable({
      deferRender: true,
      scrollY: 360,
      scrollCollapse: true
   });
   
   /**제품 상세 거래내역 DataTables Library */
   $('#productdetaillist').DataTable({
      aaSorting: [],
      deferRender: true,
      scrollY: 360,
      scrollCollapse: true
   });
   /**제품 목록에서 제품 삭제 버튼 Script(Ajax 사용) */
   $(document).on('click', '#productlist td #productdeletebtn', function() {
      var row = $(this).closest('tr'); // 현재 선택된 tr을 row로 보겠다
      var td = row.children();
      var code = td.eq(0).text();

      $('.ui.mini.modal.delete').modal('show');

      $('#deleteok').on('click', function() {
         $.ajax({
            type: 'POST',
            data: { code: code },
            datatype: 'json',
            url: 'productDeleteAjax',
            success: function(data) {
               if (data == 'y') {
                  row.remove();
                  $('#resultmessage').text("삭제되었습니다.");
               } else {
                  $('#resultmessage').text("삭제되지않았습니다.");
               }
               $('#successmsg').css('display', "block")
                  .delay(1200).queue(function() {
                     $('#successmsg').css('display', "none").dequeue();
                  });
               $('.ui.mini.modal.delete').modal('hide');
            },
            error: function(xhr, status, error) {
               alert('ajax error : ' + xhr.status + error);
            }
         });
      });
   });
});


