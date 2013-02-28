/**
* (c) 2010 jxf - rpc module. 
*
* The idea of behind this module is fetching data via an RPC-like call:
*	. RESTful web services
*	. XML/SOAP (not yet implemented)
*	. Remote document (HTML; XML; JSON) 
*	. Local documents (HTML; XML; JSON)
* The module will return the data to a callback function should a parser be specified, the module will parse the data and return the parsed data to the callback function
* This allows the client code to minimize parsing should returned data be in a standard format.
* TODO:
*	Improve on how returned data is handled (if necessary).
*/
if(!jx){
  var jx = {}
}
jx.ajax = {
        /**
         * In order to make the ajax requests domain specific we allow users to get an instance while making a global implementation available
         * This is useful for oauth applications that require domain specific headers to be set upon issuing a request
         */
        getInstance:function(){
            var obj = {} ;
            obj.headers = [] ;
            obj.send = function(url,callback,method){
                    var xmlhttp =  new XMLHttpRequest()  ;
                    method = (method==null)?'GET':method ;
                    xmlhttp.onreadystatechange = function(){
                        if(xmlhttp.readyState == 4){
                          callback(xmlhttp) ;
                        }
                    }//-- end of Inline function
                    var key,value ;
                    xmlhttp.open(method,url,true) ;
                    
                    if(obj.headers.length > 0){
                            for(var i=0; i < obj.headers.length; i++){
                                    key = obj.headers[i]['key'] ;
                                    value= obj.headers[i]['value'];
                                    //air.trace(key+'='+value)

                                    if(key != null && value != null){
                                            xmlhttp.setRequestHeader(key,value) ;
                                    }
                            }
                    }
                  xmlhttp.send(null) ;
            }
            return obj;

        },//-- end jx.ajax.getInstance
	send:function(url,callback,method){
	  var xmlhttp =  new XMLHttpRequest()  ;
	  method = (method==null)?'GET':method ;
	  xmlhttp.onreadystatechange = function(){
	    if(xmlhttp.readyState == 4){	
		  callback(xmlhttp) ;
		}
	  }//-- end of Inline function
	  var key,value ;
	  xmlhttp.open(method,url,true) ;
	  xmlhttp.send(null) ;
	
	},//-- end jx.ajax.getInstance() ;
	open:function(id,url){
		var iframe = document.getElementById(id) ;
		iframe.src = url ;
	},//-- end jx.ajax.open(id,url);
	headers:[],
	run:function(url,callback,method){
		var xmlhttp =  new XMLHttpRequest()  ;
    		method = (method==null)?'GET':method ;
    		xmlhttp.onreadystatechange = function(){
      		if(xmlhttp.readyState == 4){	
		      //air.trace(xmlhttp.getAllResponseHeaders()) ;
			callback(xmlhttp) ;
		      }
		}//-- end of Inline function
		var key,value ;
		xmlhttp.open(method,url,true) ;

		if(jx.ajax.headers.length > 0){
			for(var i=0; i < jx.ajax.headers.length; i++){
				key = jx.ajax.headers[i]['key'] ;
				value= jx.ajax.headers[i]['value'];
				
				if(key != null && value != null){
					xmlhttp.setRequestHeader(key,value) ;
				}
			}
		}
		xmlhttp.send(null) ;
    	},//-- end jx.ajax.run
    parser:null
  }//--end jx.ajax

 /**
 * These are a few parsers that can come in handy:
 * urlparser:	This parser is intended to break down a url parameter string in key,value pairs
 */

function urlparser(url){
	var p = url.split('&') ;
	var r = {} ;
	r.meta = [] ;
	r.data = {} ;
	var entry;
	for(var i=0; i < p.length; i++){
		entry = p[i] ;
		key 	= (entry.match('(.*)=')  !=null)? entry.match('(.*)=')[1]:null ;
		value 	= (entry.match('=(.*)$') != null)? entry.match('=(.*)$')[1]:null
		if(key != null){
			r.meta.push(key) ;
			r.data[key] = value  ;
		}
	}

	return r;
}
