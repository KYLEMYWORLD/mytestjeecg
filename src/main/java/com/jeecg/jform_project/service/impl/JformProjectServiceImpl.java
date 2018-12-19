package com.jeecg.jform_project.service.impl;
import com.jeecg.jform_project.service.JformProjectServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.jform_project.entity.JformProjectEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;

import org.jeecgframework.minidao.util.FreemarkerParseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.jeecgframework.core.util.ResourceUtil;

@Service("jformProjectService")
@Transactional
public class JformProjectServiceImpl extends CommonServiceImpl implements JformProjectServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void delete(JformProjectEntity entity) throws Exception{
		super.delete(entity);
		//执行删除操作增强业务
		this.doDelBus(entity);
	}

	public Serializable save(JformProjectEntity entity) throws Exception{
		Serializable t = super.save(entity);
		//执行新增操作增强业务
		this.doAddBus(entity);
		return t;
	}

	public void saveOrUpdate(JformProjectEntity entity) throws Exception{
		super.saveOrUpdate(entity);
		//执行更新操作增强业务
		this.doUpdateBus(entity);
	}
	/**
	 * 自定义按钮-[激活]业务处理
	 * @param t
	 * @return
	 */
	public void doActivateBus(JformProjectEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
		//-----------------sql增强 end------------------------------

		//-----------------java增强 start---------------------------
//		Map<String,Object> data = populationMap(t);
//		executeJavaExtend("class","com.jeecg.jform_project.controller.JformProjectController",data);
		//-----------------java增强 end-----------------------------
	}

	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(JformProjectEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
		//-----------------sql增强 end------------------------------

		//-----------------java增强 start---------------------------
		//-----------------java增强 end-----------------------------
	}
	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(JformProjectEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
		//-----------------sql增强 end------------------------------

		//-----------------java增强 start---------------------------
		//-----------------java增强 end-----------------------------
	}
	/**
	 * 删除操作增强业务
	 * @param t
	 * @return
	 */
	private void doDelBus(JformProjectEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
		//-----------------sql增强 end------------------------------

		//-----------------java增强 start---------------------------
		//-----------------java增强 end-----------------------------
	}
	private Map<String,Object> populationMap(JformProjectEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("project_name", t.getProjectName());
		map.put("project_type", t.getProjectType());
		map.put("start_date", t.getStartDate());
		map.put("finish_date", t.getFinishDate());
		map.put("project_managerid", t.getProjectManagerid());
		map.put("project_manager", t.getProjectManager());
		map.put("planfinish_date", t.getPlanfinishDate());
		map.put("project_info", t.getProjectInfo());
		map.put("project_status", t.getProjectStatus());
		map.put("project_responderid", t.getProjectResponderid());
		map.put("project_responder", t.getProjectResponder());
		map.put("create_name", t.getCreateName());
		map.put("create_by", t.getCreateBy());
		map.put("create_date", t.getCreateDate());
		map.put("update_name", t.getUpdateName());
		map.put("update_by", t.getUpdateBy());
		map.put("update_date", t.getUpdateDate());
		map.put("bpm_status", t.getBpmStatus());
		return map;
	}

	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
	public String replaceVal(String sql,JformProjectEntity t){
		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
		sql  = sql.replace("#{project_name}",String.valueOf(t.getProjectName()));
		sql  = sql.replace("#{project_type}",String.valueOf(t.getProjectType()));
		sql  = sql.replace("#{start_date}",String.valueOf(t.getStartDate()));
		sql  = sql.replace("#{finish_date}",String.valueOf(t.getFinishDate()));
		sql  = sql.replace("#{project_managerid}",String.valueOf(t.getProjectManagerid()));
		sql  = sql.replace("#{project_manager}",String.valueOf(t.getProjectManager()));
		sql  = sql.replace("#{planfinish_date}",String.valueOf(t.getPlanfinishDate()));
		sql  = sql.replace("#{project_info}",String.valueOf(t.getProjectInfo()));
		sql  = sql.replace("#{project_status}",String.valueOf(t.getProjectStatus()));
		sql  = sql.replace("#{project_responderid}",String.valueOf(t.getProjectResponderid()));
		sql  = sql.replace("#{project_responder}",String.valueOf(t.getProjectResponder()));
		sql  = sql.replace("#{create_name}",String.valueOf(t.getCreateName()));
		sql  = sql.replace("#{create_by}",String.valueOf(t.getCreateBy()));
		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
		sql  = sql.replace("#{update_name}",String.valueOf(t.getUpdateName()));
		sql  = sql.replace("#{update_by}",String.valueOf(t.getUpdateBy()));
		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
		sql  = sql.replace("#{bpm_status}",String.valueOf(t.getBpmStatus()));
		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
		return sql;
	}

	/**
	 * 执行JAVA增强
	 */
	private void executeJavaExtend(String cgJavaType,String cgJavaValue,Map<String,Object> data) throws Exception {
		if(StringUtil.isNotEmpty(cgJavaValue)){
			Object obj = null;
			try {
				if("class".equals(cgJavaType)){
					//因新增时已经校验了实例化是否可以成功，所以这块就不需要再做一次判断
					obj = MyClassLoader.getClassByScn(cgJavaValue).newInstance();
				}else if("spring".equals(cgJavaType)){
					obj = ApplicationContextUtil.getContext().getBean(cgJavaValue);
				}
				if(obj instanceof CgformEnhanceJavaInter){
					CgformEnhanceJavaInter javaInter = (CgformEnhanceJavaInter) obj;
					javaInter.execute("jform_project",data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			}
		}
	}

	private void executeSqlEnhance(String sqlEnhance,JformProjectEntity t){
		Map<String,Object> data = populationMap(t);
		sqlEnhance = ResourceUtil.formateSQl(sqlEnhance, data);
		boolean isMiniDao = false;
		try {
			data = ResourceUtil.minidaoReplaceExtendSqlSysVar(data);
			sqlEnhance = FreemarkerParseFactory.parseTemplateContent(sqlEnhance, data);
			isMiniDao = true;
		} catch (Exception e) {
		}
		String [] sqls = sqlEnhance.split(";");
		for(String sql:sqls){
			if(sql == null || sql.toLowerCase().trim().equals("")){
				continue;
			}
			int num = 0;
			if(isMiniDao){
				num = namedParameterJdbcTemplate.update(sql, data);
			}else{
				num = this.executeSql(sql);
			}
		}
	}
}