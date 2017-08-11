package com.chinamobo.ue.system.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dto.TomActivityPropertyDto;
import com.chinamobo.ue.system.dao.TomEmpMapper;
import com.chinamobo.ue.system.dao.TomMessageDetailsMapper;
import com.chinamobo.ue.system.entity.TomMessageDetails;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.JsonMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service
public class MessageDetailsServise {
	@Autowired
	private TomMessageDetailsMapper tomMessageDetailsMapper;
	@Transactional
	public TomMessageDetails selectById(int id){
		return tomMessageDetailsMapper.selectByPrimaryKey(id);
	}
	@Transactional
	public void update(String jsonStr) throws   Exception {
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObj = jsonObject.fromObject(jsonStr);
		JSONArray jsonArray = jsonObj.getJSONArray("messageDetails");
//		String string = jsonObj.getString("status");
//		TomMessageDetails message = new TomMessageDetails();
//		message.setId(0);
//		message.setStatus(string);
//		tomMessageDetailsMapper.updateByPrimaryKeySelective(message);
		ObjectMapper jsonMapper = new ObjectMapper();
		for(int i =0;i<jsonArray.size();i++){
			TomMessageDetails messageDetails = jsonMapper.readValue(jsonArray.get(i).toString(),
					TomMessageDetails.class);
		tomMessageDetailsMapper.updateByPrimaryKeySelective(messageDetails);
		}
	}
	public  List <TomMessageDetails> selectList() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomMessageDetailsMapper.selectList();
	}
}
