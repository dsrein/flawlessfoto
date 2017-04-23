$('#right').click(function(){
	var one = $('.one img')[0].src;
	var two = $('.two img')[0].src;
	var three = $('.three img')[0].src;
	var four = $('.two img')[1].src;
	var five = $('.one img')[1].src;
	

	$('.one img')[0].src = five;
	$('.two img')[0].src = one;
	$('.three img')[0].src = two;
	$('.two img')[1].src = three;
	$('.one img')[1].src = four;
	
	one = $('.one img')[0].alt;
	two = $('.two img')[0].alt;
	three = $('.three img')[0].alt;
	four = $('.two img')[1].alt;
	five = $('.one img')[1].alt;

	$('.one img')[0].alt = five;
	$('.two img')[0].alt = one;
	$('.three img')[0].alt = two;
	$('.two img')[1].alt = three;
	$('.one img')[1].alt = four;

});


$('#left').click(function(){
	var one = $('.one img')[0];
	var two = $('.two img')[0];
	var three = $('.three img')[0];
	var four = $('.two img')[1];
	var five = $('.one img')[1];
	

	$('.one img')[0].src = two.src;
	$('.two img')[0].src = three.src;
	$('.three img')[0].src = four.src;
	$('.two img')[1].src = five.src;
	$('.one img')[1].src = one.src;
	
	one = $('.one img')[0].alt;
	two = $('.two img')[0].alt;
	three = $('.three img')[0].alt;
	four = $('.two img')[1].alt;
	five = $('.one img')[1].alt;

	$('.one img')[0].alt = two;
	$('.two img')[0].alt = three;
	$('.three img')[0].alt = four;
	$('.two img')[1].alt = five;
	$('.one img')[1].alt = one;

	console.log($('.one img')[0].src);
	console.log($('.two img')[0].src);
	console.log($('.three img')[0].src);
	console.log($('.two img')[1].src);	
	console.log($('.one img')[1].src);
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
                        let uri = "https://open.spotify.com/embed?uri=" + responseObject.playlistUri;
                        $("#box").src = uri;
                        console.log("sent code");
                    });
});

