function doublecheck() {
   var yncheck = $('.confirmyn').val();
   if (yncheck == "n") {
      msg = "email 중복 검사를 하세요";
      $('.description').text(msg);
      $('.modal').modal('show');
   }
}

function zipcodeFind() {
   new daum.Postcode({
      oncomplete: function(data) {
         // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
         // 각 주소의 노출 규칙에 따라 주소를 조합한다.
         // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
         var fullAddr = ''; // 최종 주소 변수
         var extraAddr = ''; // 조합형 주소 변수
         // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
         if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
            fullAddr = data.roadAddress;
         } else { // 사용자가 지번 주소를 선택했을 경우(J)
            fullAddr = data.jibunAddress;
         }
         // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
         if (data.userSelectedType === 'R') {
            //법정동명이 있을 경우 추가한다.
            if (data.bname !== '') {
               extraAddr += data.bname;
            }
            // 건물명이 있을 경우 추가한다.
            if (data.buildingName !== '') {
               extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
            }
            // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
            fullAddr += (extraAddr !== '' ? ' (' + extraAddr + ')' : '');
         }
         // 우편번호와 주소 정보를 해당 필드에 넣는다.
         document.getElementById('zipcode').value = data.zonecode; //5자리 새우편번호 사용
         document.getElementById('addr1').value = fullAddr;
         // 커서를 상세주소 필드로 이동한다.
         document.getElementById('addr2').focus();
      }
   }).open();
}

$(document).ready(function() {
   $('.doublecheck').on('click', function() {
      var email = $('#email').val();
      if (email == "") {
         $('.description').text("E-mail을 입력하세요");
         $('.modal').modal('show');
         return;
      } else {
         var email = email;
         $.ajax({
            type: 'POST',
            data: { email: email },
            datatype: 'json',
            url: 'emailConfirmAjax',
            success: function(data) {
               var msg = "";
               if (data == "y") {
                  msg = "사용중인 email입니다";
                  $('.confirmyn').val('n');
                  $('.description').text(msg);
                  $('.ui.mini.modal').modal('show');
                  $('#email').val('');
                  $('#email').focus();
               } else {
                  $('.confirmyn').val('y');
                  msg = "사용 가능한 email입니다";
                  $('.description').text(msg);
                  $('.modal').modal('show');
               }
            },
            error: function(xhr, status, error) {
               alert('ajax error : ' + xhr.status + error);
            }
         });
      }
   });

   $(document).on('click', '#membertable td #leveleditbtn', function() {
      var row = $(this).closest('tr'); // 현재 선택된 tr을 row로 보겠다
      var td = row.children();
      var email = td.eq(1).children().children().text();
      var level = td.eq(7).children().val();
      $.ajax({
         type: 'POST',
         data: { email: email, level: level },
         datatype: 'json',
         url: 'memberUpdateAjax',
         success: function(data) {
            if (data == 'y') {
               $('#resultmessage').text("수정되었습니다.");
            } else {
               $('#resultmessage').text("수정되지않았습니다.");
            }
            $('#successmsg').css('display', "block")
               .delay(1200).queue(function() {
                  $('#successmsg').css('display', "none").dequeue();
               });
         },
         error: function(xhr, status, error) {
            alert('ajax error : ' + xhr.status + error);
         }
      });
   });

   $(document).on('click', '#membertable td #leveldeletebtn', function() {
      var row = $(this).closest('tr'); // 현재 선택된 tr을 row로 보겠다
      var td = row.children();
      var email = td.eq(1).children().children().text();

      $('.mini.ui.modal.delete').modal('show');

      $('#deleteok').on('click', function() {
         $.ajax({
            type: 'POST',
            data: { email: email },
            datatype: 'json',
            url: 'memberDeleteAjax',
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
               $('.mini.ui.modal.delete').modal('hide');
            },
            error: function(xhr, status, error) {
               alert('ajax error : ' + xhr.status + error);
            }
         });
      });
   });

   $('.idfinder').on('click', function() {
      $('.ui.basic.modal.first').modal('show');
      $('#Idfindclick').on('click', function() {
         var name = $('#name').val();
         var gender = $('#gender').val();
         var birth = $('#birth').val();
         $.ajax({
            type: 'POST',
            data: { name: name, gender: gender, birth: birth },
            datatype: 'json',
            url: 'IdFindUP',
            success: function(data) {
               if (!data) {
                  $('.ui.basic.modal.first').modal('hide');
                  $('.description').text("잘못된 정보입니다.")
                  $('.ui.mini.modal.second').modal('show');
               } else {
                  $('.ui.basic.modal.first').modal('hide');
                  $('.description').text("회원님의 이메일은 " + data + " 입니다")
                  $('.ui.mini.modal.second').modal('show');
               }
            },
            error: function(xhr, status, error) {
               alert('ajax error : ' + xhr.status + error);
            }
         });
      });
   });

   $('.passwordfinder').on('click', function() {
      $('.ui.basic.modal.third').modal('show');
      $('#PWfindclick').on('click', function() {
         var email = $('#PWemail').val();
         var gender = $('#PWgender').val();
         var birth = $('#PWbirth').val();
         $.ajax({
            type: 'POST',
            data: { email: email, gender: gender, birth: birth },
            datatype: 'json',
            url: 'PWFindUP',
            success: function(data) {
               if (data == "n") {
                  $('.ui.basic.modal.third').modal('hide');
                  $('.description.2').text("가입되지 않은 정보입니다.")
                  $('.ui.modal.fourth').modal('show');
                  $('#newPWcreate').on('click', function() {
                     $('#PWemail').val("");
                     $('#PWgender').val("");
                     $('#PWbirth').val("");
                     $('.ui.basic.modal.third').modal('show');
                  });
               } else {
                  $('.ui.basic.modal.third').modal('hide');
                  $('#newPwdInsert1').css('display', 'block')
                  $('#newPwdInsert2').css('display', 'block')
                  $('.ui.modal.fourth').modal('show');
                  var newPwd = $('#newpassword').val();
                  $('#newPWcreate').on('click', function() {
                     if(!newPwd)
                        alert("잠시만 기다려주세요.");
                     else{
                        $('.ui.modal.fourth').modal('hide');
                        $.ajax({
                           type: 'POST',
                           data: { newPwd: newPwd, email: email },
                           datatype: 'json',
                           url: 'PwdUpdate',
                           success: function(data) {
                              $('.description.2').text("성공적으로 변경되었습니다.")
                              $('.ui.modal.fourth').modal('show');
                              $('#newPWcreate').css("display","none")
                              $('#newPWconfrim').css("display","block")
                              $('#newPWconfrim').on('click', function() {
                                 document.location.href = "memberLogin";
                              });
                           },
                           error: function(xhr, status, error) {
                              alert('ajax error : ' + xhr.status + error);
                           }
                        });
                     }
                  });
               }
            },
            error: function(xhr, status, error) {
               alert('ajax error : ' + xhr.status + error);
            }
         });
      });
   });

   $('.Signup').keyup(function() {
      var gender = $('#gender').val();
      if (gender == 1 || gender == 3) {
      }
      else if (gender == 2 || gender == 4) {
      }
      else {
         alert('올바론 숫자를 입력하세요(ex:1,2,3,4)')
      }
   });

   $('#passwordchk').keyup(function() {
      var passwordchk = $('#passwordchk').val();
      var password = $('#password').val();
      if (passwordchk == password) {
         $('#pwdifferent').css("display", "none")
         $('#pwsame').css("display", "block")
      } else if (passwordchk != password) {
         $('#pwsame').css("display", "none")
         $('#pwdifferent').css("display", "block")
      }
   });
   $('#newpasswordchk').keyup(function() {
      var passwordchk = $('#newpasswordchk').val();
      var password = $('#newpassword').val();
      if (passwordchk == password) {
         $('#newPwdifferent').css("display", "none")
         $('#newPwsame').css("display", "block")
      } else if (passwordchk != password) {
         $('#newPwsame').css("display", "none")
         $('#newPwdifferent').css("display", "block")
      }
   });

   $('.Signup1').keyup(function() {
      var gender = $('#PWgender').val();
      if (gender == 1 || gender == 3) {
      }
      else if (gender == 2 || gender == 4) {
      }
      else {
         alert('올바론 숫자를 입력하세요(ex:1,2,3,4)')
      }
   });

   $('#pwshow').on('click', function() {
      $('#password').attr("type", "text");
      $('#passwordchk').attr("type", "text");
      $('#pwshow').css("display", "none");
      $('#pwhide').css("display", "block");
   });

   $('#pwhide').on('click', function() {
      $('#password').attr("type", "password");
      $('#passwordchk').attr("type", "password");
      $('#pwshow').css("display", "block");
      $('#pwhide').css("display", "none");
   });
   $(document).on('click', '#ordertable td #viewdetails', function() {
         var row = $(this).closest('tr'); // 현재 선택된 tr을 row로 보겠다
         var td = row.children();
         var ordernum = td.eq(0).children().children().text();
         $.ajax({
            type: 'POST',
            data: {ordernum:ordernum},
            datatype: 'json',
            url: 'deleteordersAjax',
            success: function(data) {
               if(!data){
                   alert('이미 취소된 주문입니다')
               
               }
               else{
                     alert('주문이 취소되었습니당')
               }
            
            },
            error: function(xhr, status, error) {
               alert('ajax error : ' + xhr.status + error);
            }
      });
   });
});