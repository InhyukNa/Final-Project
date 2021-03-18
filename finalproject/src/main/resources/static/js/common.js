$(document).ready(function(){
	$('.ui.dropdown').dropdown();
	
	$('.ui.dropdown').dropdown({
    onChange: function (lang) {
		$.ajax({
			type:'POST',
	        data : {lang:lang},
			datatype:'json',
			url : 'languageAjax',
			async:false,
			success : function(data){
				console.log(data);
				setTimeout(function(){
		            window.location.reload();
		        },100);
			},
			error : function(xhr, status, error){
			}
		});
    },
    forceSelection: false, 
    selectOnKeydown: false, 
    showOnFocus: false,
    on: "hover" 
  });

});