$(function() {
	// add a click handler to the button
    $("[js-ajax]").click(function(event) {
    	var theThis = $(this)
    	var name=theThis.attr("name")
    	var value=theThis.prop(theThis.attr("js-ajax"))
        console.log("ajax(name:"+name+" value:"+value+")")
        // make an ajax get request to get the message
        jsRoutes.controllers.MessageController.configure(name,value).ajax({
        	success: function(data) {
        		var refresh = theThis.attr("js-refresh")
        		if(refresh){
        			location.reload()
            	}
            },
            complete: function(data,status) {
            	console.log("ajax.complete(status:"+status+" data:"+data+")")
            }
        })
    })
    function endsWith(str, suffix) {
    	return str.indexOf(suffix, str.length - suffix.length) !== -1;
    }
	$(document).bind('keyup', function(e) {
		if(e.which == 39){
			$('.carousel').carousel('next');
		}
		else if(e.which == 37){
			$('.carousel').carousel('prev');
		}
	});
	
	
	
	$('a[target^="_new"]').click(function(e) {
		PopupCenter(this.href,'xtf','500','300'); 
//	     // to open in good size for user
//	    var width = window.innerWidth /0.66 ;
//	    //define the height in 
//	    var height = width * window.innerWidth / window.innerHeight;
//	    //Ratio to the width as the user screen ratio
//	    window.open(this.href, 'newwindow', 'width=' + width + ', height=' + height + ', top=300, left=350');
	    return false;
	});
})

function setCookie(name, value, days) {
	var expires;

	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		expires = "; expires=" + date.toGMTString();
	} else {
		expires = "";
	}
	document.cookie = escape(name) + "=" + escape(value) + expires + "; path=/";
}

function getCookie(name,defaultValue) {
	var nameEQ = escape(name) + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) === ' ') c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) === 0) return unescape(c.substring(nameEQ.length, c.length));
	}
	return defaultValue;
}

function eraseCookie(name) {
	createCookie(name, "", -1);
}

function s4() {
	  return Math.floor((1 + Math.random()) * 0x10000)
				 .toString(16)
				 .substring(1);
};	
function guid() {
	  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
			 s4() + '-' + s4() + s4() + s4();
}
function getOrCreateGuid(){
	if(getCookie("guid",null)==null){
		setCookie("guid",guid(),1);
	}
	return getCookie("guid");
}

function PopupCenter(url, title, w, h) {  
    // Fixes dual-screen position                         Most browsers      Firefox  
    var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : screen.left;  
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : screen.top;  
              
    width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;  
    height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;  
              
    var left = ((width / 2) - (w / 2)) + dualScreenLeft;  
    var top = ((height / 2) - (h / 2)) + dualScreenTop;  
    var newWindow = window.open(url, title, 'scrollbars=yes, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);  
  
    // Puts focus on the newWindow  
    if (window.focus) {  
        newWindow.focus();  
    }  
}