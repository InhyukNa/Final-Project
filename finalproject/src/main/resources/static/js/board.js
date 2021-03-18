tinymce.init({
   selector: 'textarea',
   plugins: 'a11ychecker advcode casechange formatpainter linkchecker autolink lists checklist media mediaembed pageembed permanentpen powerpaste table advtable tinycomments tinymcespellchecker',
   toolbar: 'a11ycheck addcomment showcomments casechange checklist code formatpainter pageembed permanentpen table',
   toolbar_mode: 'floating',
   tinycomments_mode: 'embedded',
   tinycomments_author: 'Author name',
   width: '100%',
   branding: false
});

$(document).ready(function() {
   $('.attachbtn').on('click', function() {
      $('#f_attachfile').click();
      $('#f_attachfile').change(function() {
         var filename = $('#f_attachfile').val();
         $('#f_attachname').attr('value', filename);
      });
   });

   $('.freeboarddelete').on('click', function() {
      $('.ui.mini.modal.boardmodal').modal('show');

      $('.modalcancel').on('click', function() {
         $('.ui.mini.modal.boardmodal').modal('hide');
         return;
      });

      $('.modaldelete').on('click', function() {
         $('.ui.mini.modal.boardmodal').modal('hide');
         var f_seq = $('#hidden_f_seq').val();
         document.location.href = "freeBoardDelete?f_seq=" + f_seq;
      });
   });

   $('#likebtn').on('click', function() {
      var f_seq = $('#f_seq').val();
      $.ajax({
         type: 'POST',
         data: { f_seq: f_seq },
         datatype: 'json',
         url: 'freeBoardLike',
         success: function(data) {
            if (data.likecheck == 0) {
               $('#heartblack').attr("class", "heart icon black");
               $('#f_like').html(data.selectlike);
            } else {
               $('#heartblack').attr("class", "heart icon");
               $('#f_like').html(data.selectlike);
            }
         },
         error: function(xhr, status, error) {
            alert('ajax error : ' + xhr.status + error);
         }
      });
   });

   $('#likelogin').on('click', function() {
      alert("로그인이 필요한 서비스입니다")
   });

   $('#deletecomment').on('click', function() {
      var comment_num = $('#comment_num').val();
      var f_seq = $('#hidden_f_seq').val();

      $('.ui.mini.modal.commentmodal').modal('show');
      
      $('.commentdelete').on('click', function() {
         $.ajax({
            type: 'POST',
            data: { comment_num: comment_num, f_seq: f_seq },
            datatype: 'json',
            url: 'freeBoardcommentDelete',
            success: function(data) {
               if (data == "y") {
                  $('.description').text("삭제 되었습니다.");
                  $('.ui.mini.modal.commentmodal').modal('show');
                  document.location.href = "freeBoardDetail?f_seq=" + f_seq;

               } else {
                  $('.description').text("삭제 되지 않았습니다.");
                  $('.ui.mini.modal.commentmodal').modal('show');
               }

               $('.ui.mini.modal.commentmodal').modal('hide');
            },
            error: function(xhr, status, error) {
               alert('ajax error' + xhr.status);
            }
         });
      });

      $('.commentcancel').on('click', function() {
         $('.ui.mini.modal.commentmodal').modal('hide');
      });
   });
});