logged=true;
$( document ).ready(function() {
	if(true === logged){
		$(".logged").removeClass("hide");
		$(".logged").addClass("show");
	} else {
		$(".notlogged").removeClass("hide");
		$(".notlogged").addClass("show");
	}
});