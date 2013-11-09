				
		var ImageLoader = function (imgUrl, classTxt){
			
			this.imgUrl = imgUrl;
			this.tmpUrl = './rithoresource/loader.gif';
			this.imgId = 'randomNum'+Math.random();
			this.errorImgUrl = './rithoresource/caution.gif';
	
			this.imgTagTxt = '<img class="'+classTxt+'" src="'+this.tmpUrl+'" id="'+this.imgId+'" alt=" "/>';	
			
			this.imgTag = document.createElement('img');
			this.imgTag.innerHTML = this.imgTagTxt;
			
			this.parent = document;
			
			this.parent.appendChild(this.imgTag.firstChild);
			

			this.timer = 0;
			
				
		};
		
		var ImageLoader = function (parentId, imgUrl, classTxt){
			
			this.imgUrl = imgUrl;
			this.tmpUrl = './rithoresource/loader.gif';
			this.imgId = 'randomNum'+Math.random();
			this.errorImgUrl = './rithoresource/caution.gif';
	
			this.imgTagTxt = '<img class="'+classTxt+'" src="'+this.tmpUrl+'" id="'+this.imgId+'" alt=" "/>';	
			
			this.imgTag = document.createElement('img');
			this.imgTag.innerHTML = this.imgTagTxt;
			
			this.parent = document.getElementById(parentId);
			if(this.parent==null){ 
				this.parent = document;
			}

			this.parent.appendChild(this.imgTag.firstChild);
			
			this.timer = 0;			
				
		};
		
        var ImageLoader = function (parentId, imgUrl, classTxt, height, width, alt){
			
			this.imgUrl = imgUrl;
			this.tmpUrl = './rithoresource/loader.gif';
			this.imgId = 'randomNum'+Math.random();
			this.errorImgUrl = './rithoresource/caution.gif';
			
			this.imgTagTxt = '<img class="'+classTxt+'" ' ;
			if(height!=null)this.imgTagTxt = this.imgTagTxt+' height="'+height+'" ';
			if(width!=null)this.imgTagTxt = this.imgTagTxt+' width="'+width+'" ';
			this.imgTagTxt = this.imgTagTxt+' alt="'+alt+'"  src="'+this.tmpUrl+'" id="'+this.imgId+'" />';	

			this.imgTag = document.createElement('img');
			this.imgTag.innerHTML = this.imgTagTxt;
			
			this.parent = document.getElementById(parentId);
			if(this.parent==null){ 
				this.parent = document;
			}
			this.parent.appendChild(this.imgTag.firstChild);
			
			this.timer = 0;
			
		};		

		ImageLoader.prototype.load = function(async){

			if(async){
				
				setInterval(this.ajaxLoad(),5000);
				
			}else{
								
				this.normalLoad();
				
			}
			
		}
		
		ImageLoader.prototype.normalLoad = function(){
			
			img = new Image();
			img.src = this.imgUrl;

			img.onerror = this.showImage(this.errorImgUrl, this.imgId);	
			img.onload = this.showImage(img.src, this.imgId);
			
		}

		ImageLoader.prototype.showImage = function(imgUrl, imgId){
			//alert(imgUrl);
			var imgDom = document.getElementById(this.imgId);
			imgDom.setAttribute("id", imgId);
			imgDom.setAttribute("src", imgUrl);

		}
		
		ImageLoader.prototype.getRESTcallbackmethod = function(){
			var callback = "callbackfunc = function(url, id){"+
			 "  var imgDom = document.getElementById('"+this.imgId+"');"+
			 "  imgDom.setAttribute('id', id);"+
			 "  imgDom.setAttribute('src', url);"+
			 " };";
					
			return callback;
		}

		ImageLoader.prototype.ajaxLoad = function(_immediate){
		
			var ajax = new PichaAJAX();
			var ajx_tmpUrl = this.tmpUrl;
			var ajx_errorImgUrl = this.errorImgUrl;
			var ajx_imgId = this.imgId;
			var ajx_imgUrl = this.imgUrl;
			var immediate = false;
			if(_immediate)immediate=_immediate;
			var t = this.timer;
			
			function getImage(){
				
				if(ajax.getRequest().readyState==4){
					if(ajax.getRequest().status==200){
						imgDom = document.getElementById(ajx_imgId);
						imgDom.setAttribute("src", ajx_imgUrl);
						clearInterval(t);
					}else if(ajax.getRequest().status==404){
						imgDom = document.getElementById(ajx_imgId);
						imgDom.setAttribute("src", ajx_errorImgUrl);
					}
				}
				
			};

			ajax.execute("GET", ajx_imgUrl, immediate, getImage );
			
		}