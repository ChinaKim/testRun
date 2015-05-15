package com.zhongli.main.zhonglitenghui.until;

import java.util.HashMap;
import java.util.Map;

public class CreateMap {
	public static Map<String,String>createMap(String []keys,String[]values){
		Map <String,String>map=new HashMap<String,String>();
		for(int i=0;i<keys.length;i++){
			map.put(keys[i], values[i]);
		}
		return map;		
	}
}
