$(function(){
  $(".gallery").click(function(e){
	  console.log("HELLO!??!");
    e.preventDefault();
             $(".gallery").addClass("active").not(this).removeClass("active"); 
  });
});

const $button = $("#button");
 $button.click(function(){
    	 	 $.post("/redirect", responseJSON => {
                 //Parse the JSON response into a JavaScript object.
                    const responseObject = JSON.parse(responseJSON);
                    let URL = responseObject.url;
                    window.location.replace(URL);
    			 });
});