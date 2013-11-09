var Coordinates = {
	mousePosition: function(e){
		var x= 0;
		var y = 0;
		var scrollCoord = Coordinates.scrollCoordinates();
		 y = e.clientY +scrollCoord[1];
		 x = e.clientX +scrollCoord[0];
		Report.write('x='+x+' , y='+y);
		return [x,y];
	},
	
	scrollCoordinates: function(){
		var x= 0;
		var y = 0;
		if(typeof(window.pageYOffset)=='number'){
			//netscape
			x = window.pageXOffset;
			y = window.pageYOffset;
		}else if( document.body && (document.body.scrollLeft || document.body.scrollTop ) ){
			//DOM
			x = document.body.scrollLeft;
			y = document.body.scrollTop;
		}else if(document.documentElement && (document.documentElement.scrollLeft || document.documentElement.scrollTop)){
			//IE6 
			x = document.documentElement.scrollLeft;
			y = document.documentElement.scrollTop;
		}
	return [x,y];
	}
};

var Dom = {
        get: function(el) {
        
          if (typeof el === 'string') {
			if(document.getElementById(el.toString())==null){
				alert('NULL gettingbyId '+el.toString());
			}
            return document.getElementById(el.toString());
          } else {
            return el;
          }
          
        },
                
        add: function(el, dest) {
          var el = this.get(el);
          var dest = this.get(dest);
		//alert(el+' appending to '+dest);
          dest.appendChild(el);
		//alert(el+' appended to '+dest);
        },
        
        remove: function(el) {
          var el = this.get(el);
          el.parentNode.removeChild(el);
        },
        
        removeChild: function(el, child) {
          var el = this.get(el);
          var child = this.get(child);
          el.removeChild(child);
        },
	
	 removeAllChildren: function(el) {
          	var el = this.get(el);
	  		if(el.hasChildNodes()){
				el.removeChild(el.firstChild);
				Dom.removeAllChildren(el);	
	   		}
        },

	  hide: function(el){
		el.style.display='none';
	  },

	  unhide: function(el){
		el.style.display='block';
	  },

        /**quirksmode.org/js/findpos.html*/
        getPosition: function(el){
        	var obj = Dom.get(el);
        	var curLeft = curtop = 0;
        	if(obj.offsetParent){
        		curleft = obj.offsetLeft;
        		curtop = obj.offsetTop;
        		while(obj = obj.offsetParent){
        			curleft += obj.offsetLeft;
        			curtop += obj.offsetTop;
        		}
        	}
        	return [curleft,curtop];
        },

        setPermanentPosition: function(el, coord){
        
        	var obj = Dom.get(el);
        	
			if(obj){
				obj.style.left = coord[0];
				obj.style.top = coord[1]; 
				//obj.style.position='static'; 
				obj.style.position='absolute';  
			}  
			   	
        },
        
        getHeight: function(el){
        	var height;
        	if(typeof(el.clientHeight)=='number'){
        		height = el.clientHeight;
        	}else{
        		height = el.offsetHeight;
        	}
        	return height;
        },

        getWidth: function(el){
        	var width;
        	if(typeof(el.clientWidth)=='number'){
        		width = el.clientWidth;
        	}else{
        		width = el.offsetWidth;
        	}
        	return width;
        },        
        
        setHeight: function(el, height){

        	if(typeof(height)=='number'){
        		el.style.height = height+'px';
        	}else{
        		throw new Exception('height is not a number');
        	}
        },

        setWidth: function(el, width){
        
        	if(typeof(width)=='number'){
        		el.style.width = width+'px';
        	}else{
        		throw new Exception('width is not a number');
        	}
        },  

        
        getZIndex: function(el){
        	var zIndex = Dom.getStyleValue(el,'z-index');
     	
        	if(zIndex==null){
        		return 0;
        	}else{
        		return zIndex;
        	}
        },
        
        setZIndex: function(el, zIndex){
        	if(typeof(zIndex)=='number'){
        		Dom.setStyleValue(el, 'z-index', zIndex);
        		//el.style.zIndex= zIndex; 
        	}else{
        		throw new Exception('zIndex is not a number');
        	}
        },
               
        changeId: function(el, newId){
        	var elem = Dom.get(el);
        	var attr = document.createAttribute('id');
        	attr.value = newId;
        	elem.setAttribute('id', newId);
        },
        
        getId: function(el){
        	var elem = Dom.get(el);
        	var attrVal = elem.getAttribute('id');
        	return attrVal;
        },
        
        getStyleValue: function(el, styleProp){
        
        	var elem = Dom.get(el);
        	
        	if(window.getComputedStyle){
        		var val = document.defaultView.getComputedStyle(elem,null).getPropertyValue(styleProp);  
        	}else if(elem.currentStyle){
        		var val = elem.currentStyle[styleProp];
        	}
        	
        	return val;
        },
                
        setStyleValue: function(el, styleProp, value){
        
        	var elem = Dom.get(el);
        	if(window.getComputedStyle){
        		elem.style.setProperty(styleProp, value, "");
        	}else if(elem.currentStyle){
        		switch(styleProp)
				{
				case 'z-index':
  					styleProp = 'zIndex';
  					break;
				default:
  					styleProp = styleProp;
				};
        		
        		eval('elem.style.'+styleProp+'='+value);
			}
        },
        
        strToDom: function(text){
        	var xmlDom = null;
        	if (window.DOMParser)
        	  {
        	  parser=new DOMParser();
        	  xmlDom=parser.parseFromString(text,"text/xml");
        	  }
        	else // Internet Explorer
        	  {
        		xmlDom=new ActiveXObject("Microsoft.XMLDOM");
        		xmlDom.async="false";
        		xmlDom.loadXML(text); 
        	  }
        	return xmlDom;
        },
        
        domToStr: function(xmlNode) {
			   try {
			      // Gecko- and Webkit-based browsers (Firefox, Chrome), Opera.
			      return (new XMLSerializer()).serializeToString(xmlNode);
			  }
			  catch (e) {
			     try {
			        // Internet Explorer.
			        return xmlNode.xml;
			     }
			     catch (e) {  
			        //Other browsers without XML Serializer
			        alert('Xmlserializer not supported');
			     }
			   }
			   return false;
			}
        
        
  };

var Event = {
        add: function() {
          if (window.addEventListener) {
            return function(el, type, fn) {
              Dom.get(el).addEventListener(type, fn, false);
            };
          } else if (window.attachEvent) {
            return function(el, type, fn) {
              var f = function() {
                fn.call(Dom.get(el), window.event);
              };
              Dom.get(el).attachEvent('on' + type, f);
            };
          }
      }(),
      
      getTarget: function(e){
      	var targ;
      	if (!e)
			var e = window.event;
		if (e.target)
			targ = e.target;
		else if (e.srcElement)
			targ = e.srcElement;
		return targ;	
      }
   };

   /*****  
   //use this format in you page initialization
   
   Div_Fisheyediv = '';//specify the container's div id
   Div_FisheyePaneldiv = '';//specify the object's div id
   Div_Fisheyediv_normal_height = 60;
   
   window.onload = FishEyePanel.init();
     
   ****/

	var Div_Fisheyediv = 'child';//specify the container's div id
 	var Div_FisheyePaneldiv = 'parent';//specify the object's div id
	var Div_FisheyePaneldiv_max_width = 400;
   	var Div_fisheyediv_normal_height = 60;	
   	var Div_fisheyediv_max_height = 200;
   	var Div_fisheyediv_min_height = 60;
	var Div_fisheyePanel_column_count = 4;
	var Div_fisheyePanel_padding = 60;
	
   	var allElements;
	
function FishEyePanel(){

	var fishEyeImageArray = Array();
	//will be used replace init. Pseudo SOM
	this.initSOM = function(){

		var parent = document.getElementById(Div_FisheyePaneldiv);
		
		//this.addEvents(parent);
		this.addEvents(document);//mouse movement now responding to any point on page
		
		var children = document.getElementById(Div_FisheyePaneldiv).getElementsByTagName('img');

		var panelCoord = Dom.getPosition(parent);	
		var curCol = parseInt(panelCoord[0]) + Div_fisheyePanel_padding;
		var curRow = parseInt(panelCoord[1]) + Div_fisheyePanel_padding;
		
		var up = false;
		var down = true;

		for(var i = 0 ; i < children.length; i++){

			var child = children[i];			
			Dom.changeId(child, Div_FisheyePaneldiv+''+i);
			var fishEyeImage = new FishEyeImage(child);
			fishEyeImage.init();
			//fishEyeImage .addEvents();
			fishEyeImageArray[i] = fishEyeImage ;
			fishEyeImageArray[i].reset();		

			//set permanent position
			var curPos = [ curCol ,  curRow ];
			
			Dom.setPermanentPosition(child.parentNode, curPos );	
			
			//set child position
			if(down){
				
				curRow = curRow + Div_fisheyePanel_padding + Dom.getHeight(child);
				if(curCol <= (parseInt(panelCoord[0]) + Div_fisheyePanel_padding)){
					down=false;
					up=true;
				}else{
					curCol = curCol  - Div_fisheyePanel_padding - Dom.getWidth(child); 
				}
			}else if(up){
				
				curCol = curCol  + Div_fisheyePanel_padding + Dom.getWidth(child);
				if(curRow <= (parseInt(panelCoord[1]) + Div_fisheyePanel_padding)){
					down=true;
					up=false;
				}else{
					curRow = curRow - Div_fisheyePanel_padding - Dom.getHeight(child);
					 
				}
				
			}
			
		}
		
		//resize parent
		Dom.setHeight(parent,curRow);	
		
		//Dom.setWidth( parent,2*Div_FisheyePaneldiv_max_width );
		
		parent.style.width='100%';
		
	}


	this.init = function(){

		var parent = document.getElementById(Div_FisheyePaneldiv);
		
		//this.addEvents(parent);
		this.addEvents(document);//mouse movement now responding to any point on page
		
		var children = document.getElementById(Div_FisheyePaneldiv).getElementsByTagName('img');

		var panelCoord = Dom.getPosition(parent);	
		var curCol = parseInt(panelCoord[0]) + Div_fisheyePanel_padding;
		var curRow = parseInt(panelCoord[1]) + Div_fisheyePanel_padding;
		
		

		for(var i = 0 ; i < children.length; i++){

			var child = children[i];			
			Dom.changeId(child, Div_FisheyePaneldiv+''+i);
			var fishEyeImage = new FishEyeImage(child);
			fishEyeImage.init();
			//fishEyeImage .addEvents();
			fishEyeImageArray[i] = fishEyeImage ;
			fishEyeImageArray[i].reset();		

			//set permanent position
			var curPos = [ curCol ,  curRow ];

			
			Dom.setPermanentPosition(child.parentNode, curPos );	
			
			//set child position
			if(curCol >= Div_FisheyePaneldiv_max_width ){
				curCol = parseInt(panelCoord[0]) + Div_fisheyePanel_padding;
				curRow = curRow + Div_fisheyePanel_padding + Dom.getHeight(child);
			}else{ 
				curCol = curCol  + Div_fisheyePanel_padding + Dom.getWidth(child);
			}			
			
		}
		
		//resize parent
		Dom.setHeight(parent,curRow);	
		
		//Dom.setWidth( parent,2*Div_FisheyePaneldiv_max_width );
		
		parent.style.width='100%';
		
	}
	
	//TODO remove this
	resetChildren= function(){
		//reset every childs size
		for(var i = 0 ; i < fishEyeImageArray.length; i++){	
			fishEyeImageArray[i].reset();
		}
    	return;
	}
	
	this.addEvents = function(el){

            Event.add(el, 'mousemove', function(e) {
            	//recalculate child size
            	for(var i = 0 ; i < fishEyeImageArray.length; i++){	
					fishEyeImageArray[i].resize(e);
					if(fishEyeImageArray[i].getDraggable()){
						fishEyeImageArray[i].drag(e);	
					}
				}
            });
 
            Event.add(el, 'mouseout', function(e) {
            	//is causing some unwanted issues with 
				//setTimeout('resetChildren()', 50);				
            });

            Event.add(el, 'mouseup', function(e){
            	for(var i = 0 ; i < fishEyeImageArray.length; i++){	
            		fishEyeImageArray[i].setDraggable(null);	
            	}
            });
	
	}	

};

function FishEyeImage (child){
	
	this.imageEl = child;
	var draggable = null;
	var controlsAndButtons = null;
	

	//inject event
	this.addEvents = function(){
		var el = this.imageEl ;

    	Event.add(el.parentNode, 'mouseover', function(e) {
     		if(controlsAndButtons !=null){
				for(var i = 1 ; i < controlsAndButtons.length; i++)
					Dom.unhide(controlsAndButtons[i]);
			}
        });
    	
		if(controlsAndButtons !=null){
			for(var i = 1 ; i < controlsAndButtons.length; i++){
				Event.add(controlsAndButtons[i], 'mouseout', function(e) {
					Dom.hide(controlsAndButtons[i]);
				});
			}
		}
		
		/*
		 * //works ok except for mouse out

         	Event.add(el.parentNode, 'mouseout', function(e) {
         		if(controlsAndButtons !=null){
					for(var i = 1 ; i < controlsAndButtons.length; i++)
						//Dom.hide(controlsAndButtons[i]);
				}			
           	}); 
		 	*/
		
         	Event.add(el.parentNode,'dblclick',function(e){
         		draggable = 'true';
         	});

	};

	this.init = function(){
		var el = this.imageEl;
		controlsAndButtons = el.parentNode.getElementsByTagName('div');
		if(controlsAndButtons.length>1 ){
			for(var i = 1 ; i < controlsAndButtons .length; i++){
					Dom.hide(controlsAndButtons [i]);
			}	
		}
		
		this .addEvents();
	};

	this.drag = function(e){
		
		if(this.getDraggable()=='true'){
			var coord = Coordinates.mousePosition(e);
			Dom.setPermanentPosition(this.imageEl.parentNode,coord);
		}
	};
	
	this.setDraggable = function(val){
		draggable = val;
	};
	
	this.getDraggable = function(){
		return draggable;
	};

	this.resize = function(e){
				var el = this.imageEl ;
				
				if(!e)e = window.event;

				var target = el;

				var height = Dom.getHeight(target);
				var width = Dom.getWidth(target);
				var zIndex = Dom.getZIndex(target);
				var maxheight = Div_fisheyediv_max_height;
				var minheight = Div_fisheyediv_min_height;

				var targPos = Dom.getPosition(target);
				var targPosX = targPos[0];
				var targPosY = targPos[1];
				var centerX = targPos[0] +  Math.floor(width/2);
				var centerY = targPos[1] +  Math.floor(height/2);
				var pointer = Coordinates.mousePosition(e);
				var dist = Math.sqrt(Math.pow(pointer[0]-centerX,2) + Math.pow(pointer[1]-centerY,2));
				
				//bell curve formular
				var pi = 3.14;
				var tao =Div_fisheyediv_max_height/1.5, mu = 0;
				height = Div_fisheyediv_min_height + Math.pow(Div_fisheyediv_max_height,2) * (1/(tao*Math.sqrt(2*pi)))* Math.exp(-1*Math.pow((dist-mu),2)/(2*Math.pow(tao,2)));
				//zIndex = height;
				zIndex = height/Div_fisheyediv_min_height;
			 	
				/**		
				//circle formular		
				var r = maxheight - minheight;
				dist = dist > r ? r : dist;
				var theta = Math.acos(dist/r);
				height = r * Math.sin(theta) + minheight;
				zIndex = r * Math.sin(theta);
				**/
				
				/**
				var change = 4;
				if(height < maxheight && height > minheight){
					//begin changing width depending on distance
					//if dist < 4 wdths away increase and popout
					if(dist < change*height){
						height++;
						zIndex++;
					}else if(dist > change*height){
						height--;
						zIndex--;
					}else{
						height = minheight;
						zIndex = 0;
					}									
				}
				**/


				Dom.setHeight(target, height);//TODO remove
				//target.height = height;//TODO this works
				Dom.setZIndex(target, zIndex);	
				Dom.setZIndex(target.parentNode, zIndex);
				var span = target.parentNode.getElementsByTagName('div')[7];
				//span.innerHTML = 'zIndex '+zIndex;
				span.innerHTML = 'dom getZIndex '+Dom.getZIndex(target);

	}

	this.reset= function(){
		var el = this.imageEl ;
		el.height = Div_fisheyediv_normal_height;
		Dom.setZIndex(el, 1);	
		Dom.setZIndex(el.parentNode.parentNode, 1);
	}
	
};

var Button = {
	//add button
		//inject event
		create: function(name, jsAction){
			var el = document.createElement('input');
			el.setAttribute('type','button');
           	el.value = name;
			//add action on click
			Event.add(el, 'click', function(e) {
				setTimeout(jsAction, 100);
            });
            
			return el ;
		},
	
	//hide button
		removeFrom: function(parent){
			Dom.removeChild(parent,'input')
		}
};

var Report = {
	write: function(txt){
		document.getElementById('report').innerHTML = txt;
	}
};




     