/**
* jxf version 0.9
* This file contains the compilation of utilities for miscellaneous/unsorted operations:
*	casting
*	vector extraction from associative array
*	list of keys from an associative array
*/

if(!jx){
	var jx = {} ;
}

jx.utils={} ;

/**
* Extract an array from an array of associative arrays (map)
* @param key	key or column of the associative array
* @param array	an array or associative arrays i.e [{}]
*/
jx.utils.vector=function(key,rec){
	var vector = [] ;
	var value;
	for(var i=0; i < rec.length; i++){
		value = rec[i][key] ;		
		vector.push( value ) ;
	}
	return vector ;

}//-- end jx.utils.vector(key,rec) 

/**
* Extract keys from an associative array
* @param rec	associative array
*/
jx.utils.keys=function(rec){
	var keys = [] ;
	for(var id in rec){
		keys.push(id) ;
	}
	return keys ;
}//-- end jx.utils.keys

/**
* Apply a function to an array (visitor-like design pattern)
* @param fn	casting function on the vector or array of data
* @param list	array of numeric data (hopefully)
* @return 	array containing casted type
*/
jx.utils.cast = function(fn,list){
	for(var i=0; i < list.length; i++){
		list[i] = fn(list[i]) ;
	}
	return list;

}//-- end jx.utils.cast(fn,list)

/**
* Print a dom object to system defined printer (Physical; Network or File)
* @param id	id of a DOM object
*/
jx.utils.print = function(id){}

