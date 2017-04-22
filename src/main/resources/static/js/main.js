$(function(){
  $(".gallery").click(function(e){
	  console.log("HELLO!??!");
	  e.preventDefault();
//	  if(this.hasClass("active")){
//		  $(".gallery").removeClass("active");
//		  $("#largePhoto").hide();   
//	  } else {
		  $(".gallery").addClass("active").not(this).removeClass("active"); 
		  
		  $("#largePhoto").show();       
//	  }
             
  });
});