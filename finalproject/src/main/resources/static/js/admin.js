function outstockChange1() {
	$('#stockoutdiv').css('display', 'none');
	$('#stockmanagediv').css('display', 'block')
};

function outstockChange2() {
	$('#stockoutdiv').css('display', 'block');
	$('#stockmanagediv').css('display', 'none')
};



$(document).ready(function() {
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
	$("#test1").on('click',function(){
		var ctx = document.getElementById("myChart").getContext('2d');
        var myChart = new Chart(ctx, {
           type: 'bar',
           data: {
              labels: name,
              datasets: [{


                 data: stock, //컨트롤러에서 모델로 받아온다.
                 backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)'

                 ],
                 borderColor: [
                    'rgba(255,99,132,1)',
                    'rgba(54, 162, 235, 1)'

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
	
	$(".imagebtn").on('click', function() {
		$('#imagefile').click();
		$('#imagefile').change(function() {
			var imgfilename = $('#imagefile').val();
			$('.imagename').attr('value', imgfilename);
		});
	});
	$('#productlist').DataTable({
		deferRender: true,
		scrollY: 360,
		scrollCollapse: true
	});
	$('#productdetaillist').DataTable({
		aaSorting: [],
		deferRender: true,
		scrollY: 360,
		scrollCollapse: true
	});

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



