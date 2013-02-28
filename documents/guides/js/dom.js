/**
 * (c) 2008-2010 Steve L. Nyemba <nyemba@gmail.com>
 * http://code.google.com/p/jxframework
 * This package is extracted from the jxframework.js. It was decided to modularize the framework so we can have it coexist with other frameworks and have several extension points should it be extended to iphone; android; adobe air or to any web 2.0 application
 * 
 * jxf.dom:
 *	This namespace implements various HTML element manipulation functions and utilities that revolve around HTML Document objects:
 * 	The namespace contains the following:
 *	jx.dom.set	: sets any value; attributes to a given DOM
 *		.value	id,value
 *		.style	id,	DOM identifier
 			key,	CSS attribute identifier
			value	DOM
 *
 *  The implementation is based on jxframework 0.8. I modularize this implementation to make the code maintainable and lighter
 *
 *  Requirements:
 *      1. This implementation is optimized for adobe air 2.0+
 *      2. Your project must include AIRAliases.js
 * Known Issue:
 *	1. NOT yet backward compatible
 * TODO:
 *	1. Patch for previous versions (not a priority)
 */

if(!jx){

    var jx = {}
}
jx.dom = {
	delegate:function(id,pointer){
		var doc = document.getElementById(id) ;
		if(doc != null && doc[pointer] != null){
			doc[pointer]() ;
		}
	},//-- end jx.dom.delegate()
	enable:function(id){
		var doc = document.getElementById(id) ;
	    	if(doc.disabled != undefined){
			doc.disabled = false
	    	}

	},//-- end jx.dom.enable(id)
	disable:function(id){
	 	var doc = document.getElementById(id) ;
                if(doc.disabled != undefined){
                	doc.disabled = true;
                 }

	},//-- end jx.dom.disable(id)
        set:{
		value:function(id,html){
           
		    var dom = document.getElementById(id) ;
		    var value = html ;
		    if(value == null && dom != null){
		    	//
			// at this point we have a dom with no value
			// we will set the value to empty string so we can process the command
			//
			value = ''
		    }else if (dom == null){
		    	return ;
		    }

		    if(dom.value != undefined){
			    dom.value = value
		    }else if(dom.innerHTML != undefined){
			    dom.innerHTML = value ;
		    }

		},//-- end jx.dom.set.value(id,html)
		style:function(id,key,value){
			var _dom = document.getElementById(id) ;
			if(_dom != null){
				_dom.style[key] = value ;
			}
		},//-- end jx.dom.set.style
		css:function(id,className){
			var _dom = document.getElementById(id) ;
			if(_dom != null){
				_dom.className = className ;
			}
		},
		focus:function(id){
		  document.getElementById(id).focus() ;
		}//-- end jx.dom.set.focus(id)
	},//-- end jx.dom.set
        /**
         * This namespace appends either text to an existing dom object 
         * Or appends a node to an existing dom object provided the id
         */
        append:{
            child:function(id,child){
                document.getElementById(id).appendChild(child) ;
            },//-- end jx.dom.append.child(id,child) ;
            text:function(id,newValue){
                var dom = document.getElementById(id) ;
                var oldValue = (dom.value==null || dom.value==undefined)?dom.innerHTML : dom.value ;
                var value = [oldValue,newValue] ;
                //
                // eating our own dog food this proves the implementation is viable
                //
                jx.dom.set(id,value.join(''));

            }//-- end jx.dom.append.text(id,str) ;

        },//--end jx.dom.append
        get:{
	    DropDown:function(id,field){
	    	    	var _dom = document.getElementById(id) ;
		var value = null ;
		if(field!= null && _dom != null){
			value = [] ;
			var option = 0;
			for(var i=0; i < _dom.options.length; i++){
				option = _dom.options[i] ;
				if(option.selected){
					value.push(option[field])	
				}
			}
		}
		return value ;
	    },//-- end jx.dom.get.DropDown(id,field)
            dropdownvalue:function(id){
		return jx.dom.get.DropDown(id,'value') ;

	    },//-- end jx.dom.get.DropDownValue(id)
            dropdowntext:function(id){
	    	return jx.dom.get.DropDown(id,'text') ;
	    },//-- end jx.dom.getDropDownText(id)
            checkboxvalue:function(id){},//-- end jx.dom.get.CheckboxValue(id)
            value:function(id){
                var dom = document.getElementById(id);
                var value ;
                if(dom.value == undefined){
                    value = dom.innerHTML ;
                }else{
                    value = dom.value ;
                }
                return value ;

            }//-- end jx.dom.get.Value(id)

        },//-- end jx.dom.get
        /**
         * pre: document.getElementById(id) != null
         */
        hide:function(id){
		jx.dom.set.style(id,'display','none') ;
        },//-- end jx.dom.hide(id)
        /**
         * pre : document.getElementById(id) != null
         */
        show:function(id){
		jx.dom.set.style(id,'display','') ;
        }//-- end jx.dom.show(id)

}

/**
 * This section is reserved to insure backward compatibility
 * I use namespace aliasing as a means to implement backward compatibility
 */



/**
*
*  Base64 encode / decode
*  http://www.webtoolkit.info/
*
**/
 
var Base64 = {
 
	// private property
	_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
 
	// public method for encoding
	encode : function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;
 
		input = Base64._utf8_encode(input);
 
		while (i < input.length) {
 
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);
 
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
 
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
 
			output = output +
			this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
			this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
 
		}
 
		return output;
	},
 
	// public method for decoding
	decode : function (input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;
 
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
 
		while (i < input.length) {
 
			enc1 = this._keyStr.indexOf(input.charAt(i++));
			enc2 = this._keyStr.indexOf(input.charAt(i++));
			enc3 = this._keyStr.indexOf(input.charAt(i++));
			enc4 = this._keyStr.indexOf(input.charAt(i++));
 
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
 
			output = output + String.fromCharCode(chr1);
 
			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}
 
		}
 
		output = Base64._utf8_decode(output);
 
		return output;
 
	},
 
	// private method for UTF-8 encoding
	_utf8_encode : function (string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
 
		for (var n = 0; n < string.length; n++) {
 
			var c = string.charCodeAt(n);
 
			if (c < 128) {
				utftext += String.fromCharCode(c);
			}
			else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			}
			else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
 
		}
 
		return utftext;
	},
 
	// private method for UTF-8 decoding
	_utf8_decode : function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
 
		while ( i < utftext.length ) {
 
			c = utftext.charCodeAt(i);
 
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			}
			else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			}
			else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
 
		}
 
		return string;
	}
 
}