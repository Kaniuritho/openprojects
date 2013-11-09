 //make a call to hbase rest for json response
 var JCrypt = {

		 dec64: function (s){
			if(navigator.appCodeName = "Mozilla" ){
					 return atob(s);
				}else{
					alert("Non-Mozilla browsers not supported. \n See http://www.webtoolkit.info/javascript-base64.html");
					return s;
				}
 	 	},

 	 	enc64: function (s){
			if(navigator.appCodeName = "Mozilla" ){
				  return btoa(s);
				}else{
					alert("Non-Mozilla browsers not supported. \n See http://www.webtoolkit.info/javascript-base64.html");
					return s;
				}
 	 	}
 };