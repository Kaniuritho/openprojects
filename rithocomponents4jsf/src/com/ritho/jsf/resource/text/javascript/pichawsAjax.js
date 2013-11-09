
var PichaWS = {
	doAjaxSearch: function(url){
		var pichaAjax = new PichaAJAX();
		var request = null;
		
		function showResults(){
			if(request.readyState==4){
				if(request.status==200){
					Report.write("ajax ready "+request.responseXML.documentElement);
				}else{
					Report.write("ajax not ready "+request.status);
					//reload page?
				}
			}
		};
		
		request = pichaAjax.getRequest();
		request.onreadystatechange = showResults;
		request.open("GET",url,true);
		pichaAjax.execute(request);
	}
};

var PichaAJAX = function() {
	
	this.request;
};

PichaAJAX.prototype.getRequest = function(){

	xmlHttp = this.request;
	
	if(xmlHttp==null){
		if(window.XMLHttpRequest){
			xmlHttp = new XMLHttpRequest();
		}
		if(window.ActiveXObject){
			try{
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e){
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} 
		}	
	}
	return xmlHttp;
}

PichaAJAX.prototype.execute = function(request){
	
	
	if(window.XMLHttpRequest){
			request.send(null);
		}else if(window.ActiveXObject){
			request.send(null);
		}else{
		throw new Exception("request type "+request+" is not supported by this browser");
	}
}

PichaAJAX.prototype.execute = function(getpost  , url,  immediate , readyStateFunction){
	PichaAJAX.prototype.execute(getpost  , url,  immediate , readyStateFunction, null);
}



PichaAJAX.prototype.execute = function(getpost  , url,  immediate , readyStateFunction, headerKeyValues){
	
	this.request = this.getRequest();
	this.request.onreadystatechange = readyStateFunction;
	this.request.open(getpost,url,immediate);
	
	
	if(headerKeyValues != null){
		var headerArr = new Array();
		headerArr = headerKeyValues.split(';');
		for(var i = 0 ; i < headerArr.length ; i++ ){
			var keyval = headerArr[i];
			var val = new Array();
			val = keyval.split(':');
			this.request.setRequestHeader(val[0], val[1]);
		}
	}
	this.request.withCredentials = "true";
	
	if(window.XMLHttpRequest){
		this.request.send(null);
		}else if(window.ActiveXObject){
			this.request.send(null);
		}else{
		throw new Exception("request type "+this.request+" is not supported by this browser");
	}
}

var Loader = {
		
		loadimageAjax: function(imgUrl){

			var ajax = new PichaAJAX();

			function showResults(){
				if(ajax.getRequest().readyState==4){
					if(ajax.getRequest().status==200){
						//show actual image
						
					}else{
						//show waiting to load img
						
					}
				}
			};
			
			ajax.execute("GET", imgUrl, true, showResults);

			var img = new Image();
			img.src = imgUrl;
			
		}
		
};
