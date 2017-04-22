$(function(){
  $(".gallery").click(function(e){
	  console.log("HELLO!??!");
    e.preventDefault();
             $(".gallery").addClass("active").not(this).removeClass("active"); 
  });
});