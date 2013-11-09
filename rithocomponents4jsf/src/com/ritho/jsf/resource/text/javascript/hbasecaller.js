  var HBaseCaller = function(url){
		//prepare scanner
		this.hbaseTableUrl = url;
		
  };
   
  HBaseCaller.prototype.processJSONRow = function processJSONRow(row){

		var colname = JCrypt.enc64('image:url');
		for(var i=0; i<row.Cell.length;i++){
			if(row.Cell[i].column == colname ){
				var url = JCrypt.dec64(row.Cell[i].$) ;
	 	 		var id = JCrypt.dec64(row.key);
	 	 		//alert(id)
	 	 		var loader = new ImageLoader(id,  url, 'resultImg'); 
	 	 		loader.load(false);	//dont call AJAX within another AJAX call
				break;
			}
	 	 }
		  
	}

  
  HBaseCaller.prototype.searchByRowJSON = function(rowname){
   
       var ajax = new PichaAJAX();
	   var hbaseUrl = this.hbaseTableUrl+'/'+rowname+'/image:id,image:url,image:weburl'; 
		
	   function processJSON(){
		   
			if(ajax.getRequest().readyState==4){
				if(ajax.getRequest().status==200){

					var json = eval('(' + ajax.getRequest().responseText +')');
					var Row = json.Row;
					for(var i = 0 ; i < json.Row.length; i++){
						HBaseCaller.prototype.processJSONRow(json.Row[i]);
					}
					
				}else if(ajax.getRequest().status==404){
					alert("Something went wrong")
				}
			}
			
        }
        
	   ajax.execute("GET", hbaseUrl , true, processJSON, "Accept:application/json");
	}
 
  HBaseCaller.prototype.searchByRowJSON = function(rowname, callback){
	   
      var ajax = new PichaAJAX();
	  var hbaseUrl = this.hbaseTableUrl+'/'+rowname+'/image:id,image:url,image:weburl'; 
	  var url;
	  var id;
	   
	   function processJSON(){
		   
			if(ajax.getRequest().readyState==4){
				if(ajax.getRequest().status==200){

					var json = eval('(' + ajax.getRequest().responseText +')');
					var Row = json.Row;
					for(var i = 0 ; i < json.Row.length; i++){
						var row = json.Row[i];
						var colname = JCrypt.enc64('image:url');
						for(var i=0; i<row.Cell.length;i++){
							if(row.Cell[i].column == colname ){
								 url = JCrypt.dec64(row.Cell[i].$) ;
					 	 		 id = JCrypt.dec64(row.key);
								
					 	 		 setTimeout(callback(url,id), 10);
								break;
							}
					 	 }
					}
					
				}else if(ajax.getRequest().status==404){
					alert("Something went wrong")
				}
			}
			
       }
       
	   ajax.execute("GET", hbaseUrl , true, processJSON, "Accept:application/json");

	}

  HBaseCaller.prototype.searchAndLoad = function(rowname){
	  
	  HBaseCaller.prototype.searchByRowJSON(rowname);
	  
  }
  
  //Does not work - under construction
  HBaseCaller.prototype.setDefaultScanner = function(){
	   var scannerXml = "<Scanner batch='1'/>";//JCrypt.enc64("<Scanner batch='1'/>");//
       var ajax = new PichaAJAX();
	   //var hbaseUrl = this.hbaseTableUrl+"?d="+scannerXml; 
	   var hbaseUrl = this.hbaseTableUrl+"/scanner/"+"?d="+scannerXml; 

	   alert("url ="+hbaseUrl);
		
	   function getScannerId(){
		   
			if(ajax.getRequest().readyState==4){
				if(ajax.getRequest().status==200 || ajax.getRequest().status==201){

					//var json = eval('(' + ajax.getRequest().responseText +')');
					alert(ajax.getRequest().responseText);
					
				}else{
					alert("Something went wrong ; status="+ajax.getRequest().status+" ; response = "+ajax.getRequest().responseText)
				}
			}
			
       }       
       ajax.execute("POST", hbaseUrl , true , getScannerId, "Content-type:text/xml");
	}
  
  
  
	