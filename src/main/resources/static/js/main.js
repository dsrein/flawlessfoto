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

const $start = $("#start"); 
    $start.click(function() {
                 let postParams = {"url" : window.location.href};
                    $.post("/getCode", postParams, responseJSON => { 
                        const responseObject = JSON.parse(responseJSON);
                        let uri = responseObject.playlistUri;
                        console.log("sent code");
                    });
});

