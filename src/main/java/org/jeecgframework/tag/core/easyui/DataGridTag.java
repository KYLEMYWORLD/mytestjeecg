package org.jeecgframework.tag.core.easyui;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.online.util.FreemarkerHelper;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.SysThemesUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.tag.core.factory.BootstrapTableComponent;
import org.jeecgframework.tag.core.factory.ComponentFactory;
import org.jeecgframework.tag.vo.easyui.ColumnValue;
import org.jeecgframework.tag.vo.easyui.DataGridColumn;
import org.jeecgframework.tag.vo.easyui.DataGridUrl;
import org.jeecgframework.tag.vo.easyui.OptTypeDirection;
import org.jeecgframework.web.cgform.entity.config.CgFormFieldEntity;
import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
import org.jeecgframework.web.cgform.entity.config.CgSubTableVO;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.cgform.util.PublicUtil;
import org.jeecgframework.web.system.pojo.base.TSOperation;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;


/**
 * 
 * 类描述：DATAGRID标签处理类
 * 
 @author 张代浩
 * @date： 日期：2012-12-7 时间：上午10:17:45
 * @version 1.0
 */
@SuppressWarnings({"serial","rawtypes","unchecked","static-access"})
public class DataGridTag extends TagSupport {
	private static Logger log = Logger.getLogger(DataGridTag.class);
	private final String DATE_FORMATTER = "yyyy-MM-dd";
	private final String DATETIME_FORMATTER = "yyyy-MM-dd hh:mm:ss";
	
	protected String fields = "";// 显示字段
	protected String searchFields = "";// 查询字段  Author:qiulu  Date:20130618 for：添加对区间查询的支持
	protected String name;// 表格标示
	protected String title;// 表格标示
	protected String idField="id";// 主键字段
	protected boolean treegrid = false;// 是否是树形列表
	protected List<DataGridUrl> urlList = new ArrayList<DataGridUrl>();// 列表操作显示
	protected List<DataGridUrl> toolBarList = new ArrayList<DataGridUrl>();// 工具条列表
	protected List<DataGridColumn> columnList = new ArrayList<DataGridColumn>();// 列表操作显示
	protected List<ColumnValue> columnValueList = new ArrayList<ColumnValue>();// 值替换集合
	protected List<ColumnValue> columnStyleList = new ArrayList<ColumnValue>();// 颜色替换集合
	public Map<String, Object> map;// 封装查询条件
	private String actionUrl;// 分页提交路径
	public int allCount;
	public int curPageNo;
	public int pageSize = 10;
	public boolean pagination = true;// 是否显示分页
	private String width;
	private String height;
	private boolean checkbox = false;// 是否显示复选框
	private boolean showPageList = true;// 定义是否显示页面列表
	private boolean openFirstNode = false;//是不是展开第一个节点
	private boolean fit = true;// 是否允许表格自动缩放，以适应父容器
	private boolean fitColumns = true;// 当为true时，自动展开/合同列的大小，以适应的宽度，防止横向滚动.
	private boolean collapsible = false;// 当为true时，开启收起/展开，默认不启动.
	private String sortName;//定义的列进行排序
	private String sortOrder = "desc";//定义列的排序顺序，只能是"递增"或"降序".
	private boolean showRefresh = true;// 定义是否显示刷新按钮
	private boolean showText = true;// 定义是否显示刷新按钮
	private String style = "easyui";// 列表样式easyui,datatables,jqgrid
	private String onLoadSuccess;// 数据加载完成调用方法
	private String onClick;// 单击事件调用方法
	private String onDblClick;// 双击事件调用方法
	private String queryMode = "single";//查询模式
	private String entityName;//对应的实体对象
	private String rowStyler;//rowStyler函数
	private String extendParams;//扩展参数,easyui有的,但是jeecg没有的参数进行扩展
	private boolean autoLoadData=true; // 列表是否自动加载数据
	//private boolean frozenColumn=false; // 是否是冰冻列    默认不是
	private String langArg;

	private boolean nowrap = true;

	private Boolean singleSelect;//是否单选true,false

	protected String cssTheme ;

	private boolean isShowSearch=false;//检索区域是否可收缩

	
	private String treeField;//树形列表展示列

	private String btnCls;//列表上方button样式class属性	

	protected CgFormHeadEntity head;
	protected Map<String, Object> tableData  = new HashMap<String, Object>();
	private String configId = "";
	private boolean isShowSubGrid=false;//是否显示表体数据 值为true 或者false
	
	private String component;//列表组件名称（默认easyui,bootstrap-table）

	private boolean query=true;//是否显示查询条件（默认true,显示为true,不显示为false）
	
	public boolean isQuery() {
		return query;
	}
	public void setQuery(boolean query) {
		this.query = query;
	}

	public void setComponent(String component) {
		this.component = component;
	}
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public boolean getIsShowSubGrid() {
		return isShowSubGrid;
	}
	public void setIsShowSubGrid(boolean isShowSubGrid) {
		this.isShowSubGrid = isShowSubGrid;
	}

	
	public String getBtnCls() {
		return btnCls;
	}
	public void setBtnCls(String btnCls) {

		if(checkBrowerIsNotIE()){
			this.btnCls = btnCls;
		}else{
			//IE浏览器
			this.btnCls = "easyui";
		}

	}
	public String getCssTheme() {
		return cssTheme;
	}
	public void setCssTheme(String cssTheme) {
		this.cssTheme = cssTheme;
	}


	private boolean queryBuilder = false;// 高级查询器
	public boolean isQueryBuilder() {
		return queryBuilder;
	}

	public void setQueryBuilder(boolean queryBulder) {
		this.queryBuilder = queryBulder;
	}

	private boolean superQuery = false; //高级查询器
	public boolean getSuperQuery() {
		return superQuery;
	}
	public void setSuperQuery(boolean superQuery) {
		this.superQuery = superQuery;
	}

	private String complexSuperQuery = ""; //根据表的编码是否存在展示高级查询构造器
	
	public String getComplexSuperQuery() {
		return complexSuperQuery;
	}
	public void setComplexSuperQuery(String complexSuperQuery) {
		this.complexSuperQuery = complexSuperQuery;
	}

	public void setTreeField(String treeField) {
		this.treeField = treeField;
	}

	//json转换中的系统保留字
	protected static Map<String,String> syscode = new HashMap<String,String>();
	static{
		syscode.put("class", "clazz");
	}
	
	@Autowired
	private static SystemService systemService;
	
	public void setOnLoadSuccess(String onLoadSuccess) {
		this.onLoadSuccess = onLoadSuccess;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public void setOnDblClick(String onDblClick) {
		this.onDblClick = onDblClick;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTreegrid(boolean treegrid) {
		this.treegrid = treegrid;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}

	public void setShowPageList(boolean showPageList) {
		this.showPageList = showPageList;
	}

	public void setShowRefresh(boolean showRefresh) {
		this.showRefresh = showRefresh;
	}
	
	public void setSingleSelect(Boolean singleSelect) {
		this.singleSelect = singleSelect;
	}
	
	public boolean getIsShowSearch() {
		return isShowSearch;
	}
	public void setIsShowSearch(boolean isShowSearch) {
		this.isShowSearch = isShowSearch;
	}
	
	public void setNowrap(boolean nowrap) {
		this.nowrap = nowrap;
	}
	
	
	/**
	 * 设置询问操作URL
	 * @param urlfont 
	 * @param urlclass 
	 */

	public void setConfUrl(String url, String title, String message, String exp,String operationCode, String urlStyle, String urlclass, String urlfont ,boolean inGroup) {

		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(OptTypeDirection.Confirm);
		dataGridUrl.setMessage(message);
		dataGridUrl.setExp(exp);

		dataGridUrl.setInGroup(inGroup);

		if(checkBrowerIsNotIE()){
			dataGridUrl.setUrlStyle(urlStyle);

			dataGridUrl.setUrlclass(urlclass);
			dataGridUrl.setUrlfont(urlfont);

		}else if(StringUtil.isEmpty(urlclass) || !"ace_button".equals(urlclass)){
			dataGridUrl.setUrlStyle(urlStyle);
		}

		installOperationCode(dataGridUrl, operationCode,urlList);
	}

	/**
	 * 设置删除操作URL
	 */

	public void setDelUrl(String url, String title, String message, String exp, String funname,String operationCode, String urlStyle,String urlclass,String urlfont,String id,boolean inGroup) {

		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(OptTypeDirection.Del);
		dataGridUrl.setMessage(message);
		dataGridUrl.setExp(exp);
		dataGridUrl.setFunname(funname);
		dataGridUrl.setId(id);
		dataGridUrl.setInGroup(inGroup);

		if(checkBrowerIsNotIE()){
			dataGridUrl.setUrlStyle(urlStyle);

			dataGridUrl.setUrlclass(urlclass);
			dataGridUrl.setUrlfont(urlfont);

		}else if(StringUtil.isEmpty(urlclass) || !"ace_button".equals(urlclass)){
			dataGridUrl.setUrlStyle(urlStyle);
		}

		installOperationCode(dataGridUrl, operationCode,urlList);
	}
	/**
	 * 设置默认操作URL
	 */

	public void setDefUrl(String url, String title, String exp,String operationCode, String urlStyle,String urlclass,String urlfont,boolean inGroup) {

		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(OptTypeDirection.Deff);
		dataGridUrl.setExp(exp);

		dataGridUrl.setInGroup(inGroup);

		if(checkBrowerIsNotIE()){
			dataGridUrl.setUrlStyle(urlStyle);

			dataGridUrl.setUrlclass(urlclass);
			dataGridUrl.setUrlfont(urlfont);

		}else if(StringUtil.isEmpty(urlclass) || !"ace_button".equals(urlclass)){
			dataGridUrl.setUrlStyle(urlStyle);
		}

		installOperationCode(dataGridUrl, operationCode,urlList);
		
	}
	/**
	 * 设置工具条
	 * @param height2 
	 * @param width2 
	 * @param id 
	 */
	public void setToolbar(String url, String title, String icon, String exp,String onclick, String funname,String operationCode, String width2, String height2, String id,boolean inGroup) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setType(OptTypeDirection.ToolBar);

		dataGridUrl.setInGroup(inGroup);

		if(!checkBrowerIsNotIE()){
			//IE浏览器
			if(!icon.startsWith("icon")){
				dataGridUrl.setIcon("icon-add");
			}else{
				dataGridUrl.setIcon(icon);
			}
		}else{
			dataGridUrl.setIcon(icon);
		}

		dataGridUrl.setOnclick(onclick);
		dataGridUrl.setExp(exp);
		dataGridUrl.setFunname(funname);
		dataGridUrl.setWidth(String.valueOf(width2));
		dataGridUrl.setHeight(String.valueOf(height2));

		dataGridUrl.setId(id);

		installOperationCode(dataGridUrl, operationCode,toolBarList);
		
	}

	/**
	 * 设置自定义函数操作URL
	 */
	public void setFunUrl(String title, String exp, String funname,String operationCode, String urlStyle,String urlclass,String urlfont,String id,boolean inGroup) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setType(OptTypeDirection.Fun);
		dataGridUrl.setExp(exp);
		dataGridUrl.setFunname(funname);

		dataGridUrl.setInGroup(inGroup);

		dataGridUrl.setId(id);

		if(checkBrowerIsNotIE()){
			dataGridUrl.setUrlStyle(urlStyle);

			dataGridUrl.setUrlclass(urlclass);
			dataGridUrl.setUrlfont(urlfont);

		}else if(StringUtil.isEmpty(urlclass) || !"ace_button".equals(urlclass)){
			dataGridUrl.setUrlStyle(urlStyle);
		}

		installOperationCode(dataGridUrl, operationCode,urlList);
		
	}

	/**
	 * 设置自定义函数操作URL
	 * @param urlfont 
	 * @param urlclass 
	 */
	public void setOpenUrl(String url, String title, String width, String height, String exp,String operationCode, String openModel, String urlStyle, String urlclass, String urlfont,boolean inGroup) {
		DataGridUrl dataGridUrl = new DataGridUrl();
		dataGridUrl.setTitle(title);
		dataGridUrl.setUrl(url);
		dataGridUrl.setWidth(width);
		dataGridUrl.setHeight(height);
		dataGridUrl.setType(OptTypeDirection.valueOf(openModel));
		dataGridUrl.setExp(exp);

		dataGridUrl.setInGroup(inGroup);

		if(checkBrowerIsNotIE()){
			dataGridUrl.setUrlStyle(urlStyle);

			dataGridUrl.setUrlclass(urlclass);
			dataGridUrl.setUrlfont(urlfont);

		}else if(StringUtil.isEmpty(urlclass) || !"ace_button".equals(urlclass)){
			dataGridUrl.setUrlStyle(urlStyle);
		}

		installOperationCode(dataGridUrl, operationCode,urlList);
		
	}

	/**
	 * 
	 * <b>Summary: </b> setColumn(设置字段)
	 * 
	 * @param title
	 * @param field
	 * @param width
	 * @param showLen 
	 * @param newColumn 
	 */
	public void setColumn(String title, String field, Integer width,Integer showLen,String rowspan, 
			String colspan, String align, boolean sortable, boolean checkbox, 
			String formatter,String formatterjs, boolean hidden, String replace, 
			String treefield, boolean image,String imageSize, 
			boolean query, String url, String funname, 
			String arg,String queryMode, String dictionary,boolean popup,
			boolean frozenColumn,String extend,

			String style,String downloadName,boolean isAuto,String extendParams,String editor,String defaultVal,String showMode, boolean newColumn,String dictCondition,String filterType,boolean optsMenu

			,boolean isAjaxDict) {

		DataGridColumn dataGridColumn = new DataGridColumn();
		dataGridColumn.setAlign(align);
		dataGridColumn.setCheckbox(checkbox);
		dataGridColumn.setColspan(colspan);
		dataGridColumn.setField(field);
		dataGridColumn.setFormatter(formatter);
		dataGridColumn.setFormatterjs(formatterjs);
		dataGridColumn.setHidden(hidden);
		dataGridColumn.setRowspan(rowspan);
		dataGridColumn.setSortable(sortable);
		dataGridColumn.setTitle(title);
		dataGridColumn.setWidth(width);
		//author：xugj--start--date:2016年5月11日 for:TASK #1080 【UI标签改造】t:dgCol 显示内容长度控制 -->
		dataGridColumn.setShowLen(showLen);
		//author：xugj--end---date:2016年5月11日 for:TASK #1080 【UI标签改造】t:dgCol 显示内容长度控制 -->
		dataGridColumn.setTreefield(treefield);
		dataGridColumn.setImage(image);
		dataGridColumn.setImageSize(imageSize);
		dataGridColumn.setReplace(replace);
		dataGridColumn.setQuery(query);
		dataGridColumn.setUrl(url);
		dataGridColumn.setFunname(funname);
		dataGridColumn.setArg(arg);
		dataGridColumn.setQueryMode(queryMode);
		dataGridColumn.setDictionary(dictionary);
		dataGridColumn.setPopup(popup);
		dataGridColumn.setFrozenColumn(frozenColumn);
		dataGridColumn.setExtend(extend);
		dataGridColumn.setStyle(style);
		dataGridColumn.setDownloadName(downloadName);
		dataGridColumn.setAutocomplete(isAuto);
		dataGridColumn.setExtendParams(extendParams);
		dataGridColumn.setEditor(editor);
		dataGridColumn.setNewColumn(newColumn);

		dataGridColumn.setDefaultVal(defaultVal);

		dataGridColumn.setShowMode(showMode);

		dataGridColumn.setDictCondition(dictCondition);

		dataGridColumn.setFilterType(filterType);
		dataGridColumn.setOptsMenu(optsMenu);

		dataGridColumn.setAjaxDict(isAjaxDict);
		columnList.add(dataGridColumn);
		Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute(Globals.OPERATIONCODES);
		if (null!=operationCodes) {
			for (String MyoperationCode : operationCodes) {
				if (oConvertUtils.isEmpty(MyoperationCode))
					break;
				systemService = ApplicationContextUtil.getContext().getBean(
							SystemService.class);
				TSOperation operation = systemService.getEntity(TSOperation.class, MyoperationCode);
				if(operation.getOperationcode().equals(field)){
					columnList.remove(dataGridColumn);
				}
			}
		}
		if (field != "opt") {
			fields += field + ",";
			if ("group".equals(queryMode)) {
				searchFields += field + "," + field + "_begin," + field + "_end,";
			} else {
				searchFields += field + ",";
			}
		}
		if (StringUtil.isNotEmpty(replace)) {
			String[] test = replace.split(",");
			String lang_key = "";
			String text = "";
			String value = "";
			for (String string : test) {
				lang_key = string.substring(0, string.indexOf("_"));
				text += MutiLangUtil.getLang(lang_key) + ",";
				
				value += string.substring(string.indexOf("_") + 1) + ",";
			}
			setColumn(field, text, value);

		}
		if (!StringUtils.isBlank(dictionary)&&(!popup)) {
			if(dictionary.contains(",")){
				String[] dic = dictionary.split(",");
				String text = "";
				String value = "";
				String sql = "select " + dic[1] + " as field," + dic[2]
						+ " as text from " + dic[0];

				if(!StringUtil.isEmpty(dictCondition)){
					sql += " "+dictCondition;
				}

				systemService = ApplicationContextUtil.getContext().getBean(
						SystemService.class);
				List<Map<String, Object>> list = systemService.findForJdbc(sql);
				for (Map<String, Object> map : list){
					text += map.get("text") + ",";
					value += map.get("field") + ",";
				}
				if(list.size()>0)
					setColumn(field, text, value);
			}else{
				String text = "";
				String value = "";
				List<TSType> typeList = ResourceUtil.getCacheTypes(dictionary.toLowerCase());
				if (typeList != null && !typeList.isEmpty()) {
					for (TSType type : typeList) {
						text += MutiLangUtil.doMutiLang(type.getTypename(), "") + ",";
						value += type.getTypecode() + ",";
					}
					setColumn(field, text, value);
				}
			}
		}
		if(StringUtil.isNotEmpty(style)){
			String[] temp = style.split(",");
			String text = "";
			String value = "";
			if(temp.length == 1&&temp[0].indexOf("_")==-1){
				text = temp[0];
			}else{
				for (String string : temp) {
					text += string.substring(0, string.indexOf("_")) + ",";
					value += string.substring(string.indexOf("_") + 1) + ",";
				}
			}
			setStyleColumn(field, text, value);
		}
	}
	
	/**
	 * 设置 颜色替换值
	 * @param field
	 * @param text
	 * @param value
	 */
	private void setStyleColumn(String field, String text, String value) {
		ColumnValue columnValue = new ColumnValue();
		columnValue.setName(field);
		columnValue.setText(text);
		columnValue.setValue(value);
		columnStyleList.add(columnValue);
	}

	/**
	 * 
	 * <b>Summary: </b> setColumn(设置字段替换值)
	 * 
	 * @param name
	 * @param text
	 * @param value
	 */
	public void setColumn(String name, String text, String value) {
		ColumnValue columnValue = new ColumnValue();
		columnValue.setName(name);
		columnValue.setText(text);
		columnValue.setValue(value);
		columnValueList.add(columnValue);
	}

	public int doStartTag() throws JspTagException {
		return EVAL_PAGE;
	}

	
	public int doEndTag() throws JspException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		long start = System.currentTimeMillis();
//        log.info("================================ DataGridTag 开始时间:"+sdf.format(new Date())+"==============================");
		JspWriter out = null;
		try {
			title = MutiLangUtil.doMutiLang(title, langArg);
			
			out = this.pageContext.getOut();

			if("bootstrap-table".equals(component)){
				ComponentFactory componentFactory = new BootstrapTableComponent();
				String content = componentFactory.invoke("/org/jeecgframework/tag/core/factory/ftl/component/bootstrapTable.ftl", getDataGridTag());
				//log.debug("  content ===>" + content);

				StringBuffer bst = new StringBuffer();
				bst.append(content);
				if(superQuery) {
					addSuperQueryBootstrap(bst,btnCls,columnList);
				}
				out.print(bst.toString());

			}else{
				out.print(end().toString());
				//log.debug("  end() ===>" + end().toString());
			}

			out.flush();

//			String indexStyle =null;

//			Cookie[] cookies = ((HttpServletRequest) super.pageContext
//					.getRequest()).getCookies();
//			for (Cookie cookie : cookies) {
//				if (cookie == null || StringUtils.isEmpty(cookie.getName())) {
//					continue;
//				}
//				if (cookie.getName().equalsIgnoreCase("JEECGINDEXSTYLE")) {
//					indexStyle = cookie.getValue();
//				}
//			}
//			SysThemesEnum sysThemesEnum = SysThemesUtil.getSysTheme((HttpServletRequest) super.pageContext.getRequest());
//			if (style.equals("easyui")) {
//				if("ace".equals(sysThemesEnum.getStyle())){
//					out.print(this.aceStyleTable().toString());
//				}else{
//					out.print(end().toString());
//					out.flush();
//				}

//			}else if("jqgrid".equals(style)){
//				out.print(jqGrid().toString());
//				out.flush();

//			}else{
//				out.print(datatables().toString());
//				out.flush();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {

					out.clearBuffer();
//					end().setLength(0);
					// 清空资源
					urlList.clear();
					toolBarList.clear();
					columnValueList.clear();
					columnStyleList.clear();
					columnList.clear();
					fields = "";
					searchFields = "";

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
//		 long end = System.currentTimeMillis();
//         log.info("=============================== DataGridTag 结束时间:"+sdf.format(new Date())+"==============================");
//         log.info("================================ DataGridTag 耗时:"+(end-start)+"ms==============================");
		return EVAL_PAGE;
	}

	/**
	 * jqgrid构建datagrid
	 * @return
	 */
	public StringBuffer jqGrid(){
		StringBuffer sb = new StringBuffer();

		appendLine(sb,"<link href=\"plug-in/hplus/css/bootstrap.min.css?v=3.3.6\" rel=\"stylesheet\">");
		appendLine(sb,"<link type=\"text/css\" rel=\"stylesheet\" href=\"plug-in/hplus/css/plugins/jqgrid/ui.jqgrid.css\">");
		appendLine(sb,"<script src=\"plug-in/hplus/js/jquery.min.js\"></script>");
		appendLine(sb,"<link rel=\"stylesheet\" href=\"plug-in/jquery-ui/css/ui-lightness/jquery-ui-1.9.2.custom.min.css\" type=\"text/css\"></link>");
		appendLine(sb,"<script type=\"text/javascript\" src=\"plug-in/lhgDialog/lhgdialog.min.js\"></script>");
		appendLine(sb,"<script src=\"plug-in/hplus/js/bootstrap.min.js\"></script>");
		appendLine(sb,"<script src=\"plug-in/hplus/js/plugins/peity/jquery.peity.min.js\"></script>");
		appendLine(sb,"<script src=\"plug-in/hplus/js/plugins/jqgrid/i18n/grid.locale-cn.js\"></script>");
		appendLine(sb,"<script src=\"plug-in/hplus/js/plugins/jqgrid/jquery.jqGrid.min.js\"></script>");

		appendLine(sb,"<script src=\"plug-in/tools/datagrid_2_jqgrid.js\"></script>");
		appendLine(sb,"<style>");
		appendLine(sb,"#t_"+name+"{border-bottom:1px solid #ddd;}");
		appendLine(sb,"#t_"+name+" .btn{margin-right:10px;}");
		appendLine(sb,".search_div{padding:10px;}");
		appendLine(sb,".tool_bar_div{padding:10px;}");
		appendLine(sb,"</style>");
		appendLine(sb,"<table id=\""+name+"\"></table>");
		appendLine(sb,"<div id=\"gridPager\"></div>");
		appendLine(sb,"<script type=\"text/javascript\">");
		appendLine(sb,"$(document).ready(function() {");
		appendLine(sb," $.jgrid.defaults.styleUI=\"Bootstrap\";");
		appendLine(sb,"$('#"+name+"').jqGrid({");
		appendLine(sb,"url:'" + actionUrl + "&dataStyle=jqgrid&field=" + fields + "',");
		appendLine(sb,"datatype:\"json\",");
		appendLine(sb,"mtype:\"POST\",");
		appendLine(sb,"height:'auto',");
		appendLine(sb,"autowidth:true,");
		appendLine(sb,"shrinkToFit: true,");
		appendLine(sb,"multiselect: true,");
		appendLine(sb,"toolbar:[true,'top'],");
		StringBuffer colNameBuffer = new StringBuffer();
		StringBuffer colModelBuffer = new StringBuffer();
		for (DataGridColumn column : columnList) {
				colNameBuffer.append("'");
				colNameBuffer.append(column.getTitle());
				colNameBuffer.append("',");
				if("opt".equals(column.getField())){
					colModelBuffer.append("{name:'");
					colModelBuffer.append(column.getField());
					colModelBuffer.append("',index:'");
					colModelBuffer.append(column.getField());
					colModelBuffer.append("',width:'");
					colModelBuffer.append(column.getWidth());
					colModelBuffer.append("',align:'");
					colModelBuffer.append(column.getAlign());
					colModelBuffer.append("' ");
					colModelBuffer.append(",hidden:");
					colModelBuffer.append(column.isHidden());
					colModelBuffer.append(",formatter:currencyFormatter");
					colModelBuffer.append("},");
				}else{
					colModelBuffer.append("{name:'");
					colModelBuffer.append(column.getField());
					colModelBuffer.append("',index:'");
					colModelBuffer.append(column.getField());
					colModelBuffer.append("',width:'");
					colModelBuffer.append(column.getWidth());
					colModelBuffer.append("',align:'");
					colModelBuffer.append(column.getAlign());
					colModelBuffer.append("' ");
					if(oConvertUtils.isNotEmpty(column.getFormatter())){
						if("yyyy-MM-dd".equals(column.getFormatter())){
							colModelBuffer.append(",formatter:'date'");
						}else{
							colModelBuffer.append(",formatter:");
							colModelBuffer.append(column.getFormatter());
						}
//						colModelBuffer.append(date);
//						colModelBuffer.append("' ");
					}
					
					if(oConvertUtils.isNotEmpty(column.getReplace())){
						colModelBuffer.append(",formatter:replaceFormatter");
						colModelBuffer.append(",formatoptions:{replace:");
						String[] replaceArray = column.getReplace().split(",");
						StringBuffer replaceBuffer = new StringBuffer();
						replaceBuffer.append("{");
						if(replaceArray.length > 0){
							String text = "";
							String value = "";
							for (String replaceOri : replaceArray) {
								String lang_key = replaceOri.split("_")[0];
								text = MutiLangUtil.getLang(lang_key);
								value =replaceOri.split("_")[1];
								replaceBuffer.append("'");
								replaceBuffer.append(value);
								replaceBuffer.append("':'");
								replaceBuffer.append(text);
								replaceBuffer.append("',");
							}
						}
						replaceBuffer.append("}");
						colModelBuffer.append(replaceBuffer.toString());
						colModelBuffer.append("}");
					}
					if(oConvertUtils.isNotEmpty(column.getFormatterjs())){
						colModelBuffer.append(",formatter:"+column.getFormatterjs());
					}
					colModelBuffer.append(",hidden:");
					colModelBuffer.append(column.isHidden());
					colModelBuffer.append("},");
			}
		}
		String colNames = colNameBuffer.toString();
		colNames = colNames.substring(0,colNames.length()-1);
		String colModels = colModelBuffer.toString();
		colModels = colModels.substring(0,colModels.length()-1);
		appendLine(sb,"colNames:[");
		appendLine(sb,colNames);
		appendLine(sb,"], colModel:[");
		appendLine(sb,colModels);
		appendLine(sb,"],");
		appendLine(sb,"rownumbers:true,");
		appendLine(sb,"viewrecords: true,");
		appendLine(sb,"rowNum:"+pageSize+",");
		appendLine(sb,"rowList:["+pageSize+","+2*pageSize+","+3*pageSize+"],");
//		appendLine(sb,"jsonReader:{");
//		appendLine(sb,"id: \"blackId\",");
//		appendLine(sb,"repeatitems : false},");
		appendLine(sb,"pager:$('#gridPager')");
		appendLine(sb,",caption:'");
		appendLine(sb,title);
		appendLine(sb,"'});");
		
		//自适应表格宽度
//		appendLine(sb,"$(\"#"+name+"\").setGridWidth($(window).width()*0.99);");
		
		//表格顶部，查询、工具栏
		appendLine(sb,"$('#t_"+name+"').append('");
		if(hasQueryColum(columnList)){
			appendLine(sb,"<div id=\""+name+"tb\" class=\"search_div row\">");
			appendLine(sb,"<div name=\"searchColums\" class=\"search-content\"><form name=\""+name+"Form\" id=\""+name+"Form\"></form></div><div class=\"col-sm-1 pull-right\">");
			appendLine(sb,"<button class=\"btn btn-success\" type=\"button\" onclick=\"javascript:"+name+"search();\"><span><i class=\"fa fa-search\"></i>查询</span></button>");
			appendLine(sb,"</div></div>");
		}
		appendLine(sb,"<div class=\"tool_bar_div bg-info\"></div>");
		appendLine(sb,"');");
		//表格顶部查询
		if(hasQueryColum(columnList) && !columnList.isEmpty()){
			for (DataGridColumn column : columnList) {
				if(column.isQuery()){
					appendLine(sb,"$('#t_"+name+" .search-content form').append('");
					appendLine(sb,"<label style=\"margin-right:10px;margin-left:10px;\">");
					appendLine(sb,column.getTitle());
					appendLine(sb,"</label>");
					String dictionary = column.getDictionary();
					
					if(oConvertUtils.isNotEmpty(dictionary)){
						//字典数据信息，存在两种处理方式，一种是表格元素数据，一种是字典表当中的数据
						String showMode = column.getShowMode();
						if(showMode!=null && "radio".equals(showMode)){							
							if(dictionary.indexOf(",")>-1){
								//表格数据信息
								try{
									String[] dictionaryArray = dictionary.split(",");
									if(dictionaryArray.length == 3){
										String field = column.getField().replaceAll("_","\\.");
										appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_radio\"/>");	
										String sql = "select " + dictionaryArray[1]+","+dictionaryArray[2]+" from "+dictionaryArray[0];
										List<Map<String, Object>> dictionaryList = systemService.findForJdbc(sql);
										if(dictionaryList != null && !dictionaryList.isEmpty()){
											for (Map<String, Object> map : dictionaryList) {
												if(map.containsKey(dictionaryArray[1]) && map.containsKey(dictionaryArray[2])){

													appendLine(sb," <input type=\"radio\" value=\"" + map.get(dictionaryArray[1]) + "\" name=\""+field+"_radio\" onclick=\"javascrpt:$('#"+ field+"_radio').val('" + map.get(dictionaryArray[1]) + "');\" />");

													appendLine(sb,(String)map.get(dictionaryArray[2]));
												}
											}
										}
									}
								}catch (Exception e) {
									// TODO: 字典数据异常
								}
							}else{
								//字典表数据
								List<TSType> typeList = ResourceUtil.getCacheTypes(dictionary.toLowerCase());
								if(typeList != null && !typeList.isEmpty()){
									String field = column.getField().replaceAll("_","\\.");
									appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_radio\"/>");
									for (TSType type : typeList) {

										appendLine(sb," <input type=\"radio\" value=\"" + type.getTypecode() + "\" name=\""+field+"_radio\" onclick=\"javascrpt:$('#"+ field+"_radio').val('" + type.getTypecode() + "');\" />");										

										appendLine(sb,MutiLangUtil.getLang(type.getTypename()));
									}
								}
							}
						}else if(showMode!=null && "checkbox".equals(showMode)){
							if(dictionary.indexOf(",")>-1){
								//表格数据信息
								try{
									String[] dictionaryArray = dictionary.split(",");
									if(dictionaryArray.length == 3){
										String field = column.getField().replaceAll("_","\\.");
										appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_checkbox\" value=\"\" />");
										String sql = "select " + dictionaryArray[1]+","+dictionaryArray[2]+" from "+dictionaryArray[0];
										List<Map<String, Object>> dictionaryList = systemService.findForJdbc(sql);
										if(dictionaryList != null && !dictionaryList.isEmpty()){
											for (Map<String, Object> map : dictionaryList) {
												if(map.containsKey(dictionaryArray[1]) && map.containsKey(dictionaryArray[2])){
													String value = map.get(dictionaryArray[1]).toString();
													appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+value+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+value+",',''));}\" value=\"" + value + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" />");
													appendLine(sb,(String)map.get(dictionaryArray[2]));
												}
											}
										}
									}
								}catch (Exception e) {
									// TODO: 字典数据异常
								}
							}else{
								//字典表数据
								List<TSType> typeList = ResourceUtil.getCacheTypes(dictionary.toLowerCase());
								if(typeList != null && !typeList.isEmpty()){
									String field = column.getField().replaceAll("_","\\.");
									appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_checkbox\" value=\"\" />");									
									for (TSType type : typeList) {
										String typeCode = type.getTypecode();
										appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+typeCode+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+typeCode+",',''));}\" value=\"" + typeCode + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" />");
										appendLine(sb,MutiLangUtil.getLang(type.getTypename()));
									}
								}
							}
						}else{
							appendLine(sb,"<select  name=\"");
							appendLine(sb,column.getField());
							appendLine(sb,"\">");
							appendLine(sb,"<option value=\"\"></option>");
							if(dictionary.indexOf(",")>-1){
								//表格数据信息
								try{
									String[] dictionaryArray = dictionary.split(",");
									if(dictionaryArray.length == 3){
										String sql = "select " + dictionaryArray[1]+","+dictionaryArray[2]+" from "+dictionaryArray[0];
										List<Map<String, Object>> dictionaryList = systemService.findForJdbc(sql);
										if(dictionaryList != null && !dictionaryList.isEmpty()){
											for (Map<String, Object> map : dictionaryList) {
												if(map.containsKey(dictionaryArray[1]) && map.containsKey(dictionaryArray[2])){
													appendLine(sb,"<option value=\"");
													appendLine(sb,(String)map.get(dictionaryArray[1]));
													appendLine(sb,"\">");
													appendLine(sb,(String)map.get(dictionaryArray[2]));
													appendLine(sb,"</option>");
												}
											}
										}
									}
								}catch (Exception e) {
									// TODO: 字典数据异常
								}
							}else{
								//字典表数据
								List<TSType> typeList = ResourceUtil.getCacheTypes(dictionary.toLowerCase());
								if(typeList != null && !typeList.isEmpty()){
									for (TSType type : typeList) {
										appendLine(sb,"<option value=\"");
										appendLine(sb,type.getTypecode());
										appendLine(sb,"\">");
										appendLine(sb,MutiLangUtil.getLang(type.getTypename()));
										appendLine(sb,"</option>");
									}
								}
							}
							appendLine(sb,"</select>");
						}
						
					}else if(oConvertUtils.isNotEmpty(column.getReplace())){
						String showMode = column.getShowMode();
						if(showMode!=null && "radio".equals(showMode)){		
							String field = column.getField().replaceAll("_","\\.");
							appendLine(sb,"<input type=\"hidden\"  name=\""+field+"\" id=\""+field+"_radio\"/>");	
							String[] test = column.getReplace().split(",");
							String text = "";
							String value = "";
							for (String string : test) {
								String lang_key = string.split("_")[0];
								text = MutiLangUtil.getLang(lang_key);
								value =string.split("_")[1];

								if(column.getDefaultVal()!=null&&column.getDefaultVal().trim().equals(value)){
									appendLine(sb," <input type=\"radio\" value=\"" + value + "\" name=\""+field+"_radio\" onclick=\"javascrpt:$('#"+ field+"_radio').val('" + value + "');\" checked=\"checked\" />"+text);
									appendLine(sb," <script type=\"text/javascript\">");
									appendLine(sb,"  $('#"+ field+"_radio').val('"+value+"');");
									appendLine(sb," </script>");
								}else{
									appendLine(sb," <input type=\"radio\" value=\"" + value + "\" name=\""+field+"_radio\" onclick=\"javascrpt:$('#"+ field+"_radio').val('" + value + "');\" />"+text);
								}

							}
						}else if(showMode!=null && "checkbox".equals(showMode)){	
							String field = column.getField().replaceAll("_","\\.");
							appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_checkbox\" value=\"\" />");							
							String[] test = column.getReplace().split(",");
							String text = "";
							String value = "";
							for (String string : test) {								
								String lang_key = string.split("_")[0];
								text = MutiLangUtil.getLang(lang_key);
								value =string.split("_")[1];
								if(column.getDefaultVal()!=null&&column.getDefaultVal().trim().equals(value)){									
									appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+value+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+value+",',''));}\" value=\"" + value + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" checked=\"checked\" />"+text);
									appendLine(sb," <script type=\"text/javascript\">");
									appendLine(sb,"  	$(\"#"+ field +"_checkbox\").val($(\"#"+ field +"_checkbox\").val()+,"+value+",);");
									appendLine(sb," </script>");
								}else{
									appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+value+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+value+",',''));}\" value=\"" + value + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" />"+text);
								}
							}
						}else{						
							appendLine(sb,"<select  name=\""+column.getField().replaceAll("_","\\.")+"\" WIDTH=\"100\" style=\"width: 104px\"> ");
							appendLine(sb,StringUtil.replaceAll("<option value =\"\" >{0}</option>", "{0}", MutiLangUtil.getLang("common.please.select")));
							String[] test = column.getReplace().split(",");
							String text = "";
							String value = "";
							for (String string : test) {
								String lang_key = string.split("_")[0];
								text = MutiLangUtil.getLang(lang_key);
								value =string.split("_")[1];
								if(column.getDefaultVal()!=null&&column.getDefaultVal().trim().equals(value)){
									appendLine(sb,"<option value =\""+value+"\" selected=\"selected\">"+text+"</option>");
								}else{
									appendLine(sb,"<option value =\""+value+"\" >"+text+"</option>");
								}
							}
							appendLine(sb,"</select>");
						}
					}else{
						appendLine(sb,"<input  onkeypress=\"EnterPress(event)\" onkeydown=\"EnterPress()\"  type=\"text\" name=\""+column.getField().replaceAll("_","\\.")+"\"  "+extendAttribute(column.getExtend())+" ");
						if(this.DATE_FORMATTER.equals(column.getFormatter())){
							appendLine(sb," style=\"width: 160px\" class=\"Wdate\" onClick=\"WdatePicker()\" ");
						}else if(this.DATETIME_FORMATTER.equals(column.getFormatter())){
							appendLine(sb," style=\"width: 160px\" class=\"Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" ");
						}else{
							appendLine(sb," style=\"width: 120px\" class=\"inuptxt\" ");
						}
						if(oConvertUtils.isNotEmpty(column.getDefaultVal())){
							appendLine(sb," value=\""+column.getDefaultVal()+"\" ");
						}
						appendLine(sb," />");
					}
					appendLine(sb,"');");
				}
			}
		}		
		
		//工具栏的处理方式
		if(toolBarList.size() > 0){
			for (DataGridUrl toolBar : toolBarList) {
				appendLine(sb,"$('#t_"+name+" .tool_bar_div').append('");
				appendLine(sb,"<button class=\"btn btn-success\"");

				if(StringUtil.isNotEmpty(toolBar.getId())){
					appendLine(sb," id=\"");
					appendLine(sb,toolBar.getId());
					appendLine(sb,"\"");
				}

				appendLine(sb," onclick=\"");
				appendLine(sb,toolBar.getFunname());
				appendLine(sb,"(\\'");
				appendLine(sb,toolBar.getTitle());
				appendLine(sb,"\\',\\'");
				appendLine(sb,toolBar.getUrl());
				appendLine(sb,"\\',\\'");
				appendLine(sb,name);
				appendLine(sb,"\\',");
				String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
				String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
				appendLine(sb,width+","+height+")");
				appendLine(sb,"\" >");
				//工具栏图标显示
				String toolBarIcon = toolBar.getIcon();
				if(oConvertUtils.isNotEmpty(toolBarIcon)){
					if(toolBarIcon.equals("icon-add") ){
						appendLine(sb,"<i class=\"fa fa-plus\"></i>");
					}else if(toolBarIcon.equals("icon-edit") ){
						appendLine(sb,"<i class=\"fa fa-edit\"></i>");
					}else if (toolBarIcon.equals("icon-put") ) {
						appendLine(sb,"<i class=\"fa fa-download\"></i>");
					}else if (toolBarIcon.equals("icon-putout")) {
						appendLine(sb,"<i class=\"fa fa-upload\"></i>");
					}else if (toolBarIcon.equals("icon-remove") ) {
						appendLine(sb,"<i class=\"fa fa-trash-o\"></i>");
					}else if (toolBarIcon.equals("icon-search") ) {
						appendLine(sb,"<i class=\"fa fa-search\"></i>");
					}else{
						appendLine(sb,"<i class=\"fa "+toolBarIcon+"\"></i>");
					}
				}
				appendLine(sb,toolBar.getTitle());
				appendLine(sb,"</button>");
				appendLine(sb,"');");
			}
		}
		//添加在底部的按钮
//		appendLine(sb,"$('#"+name+"').navGrid('#gridPager',{edit:false,add:false,del:false,search:false})");
//		if(toolBarList.size() > 0){
//			for (DataGridUrl toolBar : toolBarList) {
//				appendLine(sb,".navButtonAdd('#gridPager',{");
//				appendLine(sb,"caption:'");
//				appendLine(sb,toolBar.getTitle());
//				appendLine(sb,"'");
//				appendLine(sb,",buttonicon:'");
//				appendLine(sb,toolBar.getIcon());
//				appendLine(sb,"'");
//				appendLine(sb,",onClickButton:");
//				appendLine(sb,"function(){");
//				if(oConvertUtils.isNotEmpty(toolBar.getOnclick())){
//					appendLine(sb,toolBar.getOnclick());
//				}else{
//					appendLine(sb,toolBar.getFunname());
//					appendLine(sb,"('");
//					appendLine(sb,toolBar.getTitle());
//					appendLine(sb,"','");
//					appendLine(sb,toolBar.getUrl());
//					appendLine(sb,"','");
//					appendLine(sb,name);
//					appendLine(sb,"',");
//					String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
//					String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
//					appendLine(sb,width+","+height+")");
//				}
//				appendLine(sb,"}");
//				appendLine(sb,",position:'last'");
//				appendLine(sb,"})");
//			}
//		}
		appendLine(sb,"});");
		appendLine(sb,"function currencyFormatter(cellvalue, options, rec){ ");
		appendLine(sb,"var index = options.pos;");
		StringBuffer optSb = new StringBuffer();
		this.getOptUrl(optSb);
		appendLine(sb,optSb.toString());
		appendLine(sb,"}");
		appendLine(sb,"function reloadTable(){");
		appendLine(sb,"try{");
		appendLine(sb,"	$(\'#\'+gridname).trigger(\"reloadGrid\");" );
		appendLine(sb,"}catch(ex){}");
		appendLine(sb,"}");
		
		//数据替换
		appendLine(sb,"function replaceFormatter(cellvalue,options,rec){");
		appendLine(sb,"var formatterOptions = options.colModel.formatoptions;");
		appendLine(sb,"var replace = formatterOptions.replace;");
		appendLine(sb,"return replace[cellvalue];");
		appendLine(sb,"}");
		
		//回车查询
		appendLine(sb,"function EnterPress(e){");
		appendLine(sb,"var e = e || window.event;");
		appendLine(sb,"if(e.keyCode == 13){ ");
		appendLine(sb,name+"search();");
		appendLine(sb,"}}");
		
		//提交查询
		appendLine(sb,"function " + name + "search(){");
		appendLine(sb,"try { if(! $(\"#"+name+"Form\").Validform({tiptype:3}).check()){return false;} } catch (e){}");
		appendLine(sb,"var queryParams = '';");
		appendLine(sb,"$(\'#" + name + "tb\').find('*').each(function(){ if($(this).attr('name') != undefined && $(this).val() != ''){queryParams += \"&\" + $(this).attr('name') + \"=\" + $(this).val();}});");
		appendLine(sb,"console.log(queryParams);");
		appendLine(sb,"var url = '"+actionUrl+"&dataStyle=jqgrid&field="+searchFields+"' + queryParams;");
		appendLine(sb,"console.log(url);");
		appendLine(sb,"$(\'#" + name + "\').jqGrid('setGridParam',{url:url,page:1}).trigger(\"reloadGrid\");" + "}");
		appendLine(sb,"</script>");
		return sb;
	}

	/**
	 * datatables构造方法
	 * 
	 * @return
	 */
	public StringBuffer datatables() {
		StringBuffer sb = new StringBuffer();

		appendLine(sb,"<link href=\"plug-in/hplus/css/plugins/dataTables/dataTables.bootstrap.css\" rel=\"stylesheet\">");
		appendLine(sb,"<script src=\"plug-in/hplus/js/plugins/dataTables/jquery.dataTables.js\"></script>");

		appendLine(sb,"<script type=\"text/javascript\">");
		appendLine(sb,"$(document).ready(function() {");
		appendLine(sb,"var oTable = $(\'#userList\').dataTable({");
		// appendLine(sb,
		// "\"sDom\" : \"<\'row\'<\'span6\'l><\'span6\'f>r>t<\'row\'<\'span6\'i><\'span6\'p>>\",");
		appendLine(sb,"\"bProcessing\" : true,");// 当datatable获取数据时候是否显示正在处理提示信息"
		appendLine(sb,"\"bPaginate\" : true,"); // 是否分页"
		appendLine(sb,"\"sPaginationType\" : \"full_numbers\",");// 分页样式full_numbers,"
		appendLine(sb,"\"bFilter\" : true,");// 是否使用内置的过滤功能"
		appendLine(sb,"\"bSort\" : true, ");// 排序功能"
		appendLine(sb,"\"bAutoWidth\" : true,");// 自动宽度"
		appendLine(sb,"\"bLengthChange\" : true,");// 是否允许用户自定义每页显示条数"
		appendLine(sb,"\"bInfo\" : true,");// 页脚信息"
		appendLine(sb,"\"sAjaxSource\" : \""+ actionUrl + "&field=" + fields+"\",");
		appendLine(sb,"\"bServerSide\" : true,");// 指定从服务器端获取数据
		appendLine(sb,"\"oLanguage\" : {" + "\"sLengthMenu\" : \" _MENU_ 条记录\"," + "\"sZeroRecords\" : \"没有检索到数据\"," + "\"sInfo\" : \"第 _START_ 至 _END_ 条数据 共 _TOTAL_ 条\"," + "\"sInfoEmtpy\" : \"没有数据\"," + "\"sProcessing\" : \"正在加载数据...\"," + "\"sSearch\" : \"搜索\"," + "\"oPaginate\" : {" + "\"sFirst\" : \"首页\"," + "\"sPrevious\" : \"前页\", " + "\"sNext\" : \"后页\"," + "\"sLast\" : \"尾页\"" + "}" + "},"); // 汉化
		// 获取数据的处理函数 \"data\" : {_dt_json : JSON.stringify(aoData)},
		appendLine(sb,"\"fnServerData\" : function(sSource, aoData, fnCallback, oSettings) {");
		// + "\"data\" : {_dt_json : JSON.stringify(aoData)},"
		appendLine(sb,"oSettings.jqXHR = $.ajax({" + "\"dataType\" : \'json\'," + "\"type\" : \"POST\"," + "\"url\" : sSource," + "\"data\" : aoData," + "\"success\" : fnCallback" + "});},");
		appendLine(sb,"\"aoColumns\" : [ ");
		int i = 0;
		for (DataGridColumn column : columnList) {
			i++;
			appendLine(sb,"{");
			appendLine(sb,"\"sTitle\":\"" + column.getTitle() + "\"");
			if (column.getField().equals("opt")) {
				appendLine(sb,",\"mData\":\"" + idField + "\"");
				appendLine(sb,",\"sWidth\":\"20%\"");
				appendLine(sb,",\"bSortable\":false");
				appendLine(sb,",\"bSearchable\":false");
				appendLine(sb,",\"mRender\" : function(data, type, rec) {");
				this.getOptUrl(sb);
				appendLine(sb,"}");
			} else {
				int colwidth = (column.getWidth() == null) ? column.getTitle().length() * 15 : column.getWidth();
				appendLine(sb,",\"sName\":\"" + column.getField() + "\"");
				appendLine(sb,",\"mDataProp\":\"" + column.getField() + "\"");
				appendLine(sb,",\"mData\":\"" + column.getField() + "\"");
				appendLine(sb,",\"sWidth\":\"" + colwidth + "\"");
				appendLine(sb,",\"bSortable\":" + column.isSortable() + "");

				appendLine(sb,",\"bVisible\":" + !column.isHidden() + "");

				appendLine(sb,",\"bSearchable\":" + column.isQuery() + "");
			}
			appendLine(sb,"}");
			if (i < columnList.size())
				appendLine(sb,",");
		}

		appendLine(sb,"]" + "});" + "});" + "</script>");
		appendLine(sb,"<table width=\"100%\"  class=\"" + style + "\" id=\"" + name + "\" toolbar=\"#" + name + "tb\"></table>");
		return sb;

	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String toString(){
		StringBuffer key = new StringBuffer();
		key.append("DataGridTag [fields=").append(fields)
			.append(",searchFields=").append(searchFields)
			.append(",name=").append(name)
			.append(",title=").append(title)
			.append(",idField=").append(idField)
			.append(",treegrid=").append(treegrid)
			.append(",actionUrl=").append(actionUrl)
			.append(",sortName=").append(sortName)
			.append(",queryMode=").append(queryMode)
			.append(",entityName=").append(entityName)
			.append(", cssTheme=").append(cssTheme)
			.append(",treeField=").append(treeField)
			.append(",btnCls=").append(btnCls)
			.append(",queryBuilder=").append(queryBuilder)
			.append(",collapsible=").append(collapsible)
			.append(",superQuery=").append(superQuery)
			.append(",complexSuperQuery=").append(complexSuperQuery);
		key.append(",urlList=[");
		for(DataGridUrl url : urlList){
			key.append(url.toString()+",");
		}
		key.append("]");
		key.append(",toolBarList=[");
		for(DataGridUrl url : toolBarList){
			key.append(url.toString()+",");
		}
		key.append(",columnList=[");
		for(DataGridColumn col : columnList){
			key.append(col.toString()+",");
		}
		key.append("]");
		key.append(",columnValueList=[");
		for(ColumnValue col : columnValueList){
			key.append(col.toString()+",");
		}
		key.append("]");
		key.append(",columnStyleList=[");
		for(ColumnValue col : columnStyleList){
			key.append(col.toString()+",");
		}
		key.append("]");
		key.append(",sysTheme=").append(SysThemesUtil.getSysTheme(ContextHolderUtils.getRequest()).getStyle())
			.append(",brower_type="+ContextHolderUtils.getSession().getAttribute("brower_type"))
			.append("]");
		return key.toString();
	};

	
	/**
	 * easyui构造方法
	 * 
	 * @return
	 */
	public StringBuffer end() {

		StringBuffer sb = null;
		if (style.equals("easyui")) {
			sb = null;
		}else if("jqgrid".equals(style)){
			sb = jqGrid();
		}else{
			sb = datatables();
		}

		String grid = "";
		sb = new StringBuffer();

		if(btnCls!=null && btnCls.indexOf("bootstrap")==0){
			appendLine(sb,"<link rel=\"stylesheet\" href=\"plug-in/bootstrap/css/bootstrap-btn.css\" type=\"text/css\"></link>");
		}

		boolean hasGroup = hasGroup();
		if(hasGroup){
			appendLine(sb,"<link rel=\"stylesheet\" href=\"plug-in/tools/css/optgroup.css\" type=\"text/css\"></link>");
		}

		width = (width == null) ? "auto" : width;
		height = (height == null) ? "auto" : height;

		if(!treegrid && isShowSubGrid){
			appendLine(sb,"<script type=\"text/javascript\" src=\"plug-in/easyui/extends/datagrid-detailview.js\"></script>");
		}

		appendLine(sb,"<script type=\"text/javascript\">");

		if(!treegrid && isShowSubGrid){
			loadSubData(configId);
			appendLine(sb,"function  detailFormatterFun(){");
			appendLine(sb,"var s = '<div class=\"orderInfoHidden\" style=\"padding:2px;\">'+");

			appendLine(sb,"'<div class=\"easyui-tabs\"   style=\"height:230px;width:800px;\">'+");

			String subtableids[] = null;
			if(head.getSubTableStr()!=null && head.getSubTableStr().length()>=0){
				subtableids = head.getSubTableStr().split(",");
				for (String subtable : subtableids) {
					appendLine(sb,"'<div title=\""+((CgSubTableVO)tableData.get(subtable)).getHead().getContent()+"\" style=\"padding:2px;\">'+");
					appendLine(sb,"'<table class=\""+((CgSubTableVO)tableData.get(subtable)).getHead().getTableName()+"tablelines\" ></table>'+");

					appendLine(sb,"'</div>'+");

				}
			}
			appendLine(sb,"'</div>'+");
			appendLine(sb,"'</div>'; return s;}");
			appendLine(sb,"function onExpandRowFun(index,row){");
			appendLine(sb,"var tabs = $(this).datagrid('getRowDetail',index).find('div.easyui-tabs');");
			appendLine(sb,"tabs.tabs();");
			if(subtableids!=null){
			for (String ss : subtableids) {
				CgSubTableVO submap = ((CgSubTableVO)tableData.get(ss));
				String linkid = "";
				String subfield = "";
				String columns = "";
				List<Map<String, Object>> subfieldlist = submap.getFieldList();
				for (Map<String, Object> map : subfieldlist) {
					subfield+=map.get("field_name")+",";

//					if(map.get("main_field")!=null && map.get("main_field").toString().length()>0 && "".equals(linkid)){
//						linkid = (String)map.get("field_name");
//					}

					columns += "{title:'"+map.get("content")+"',field:'"+map.get("field_name")+"',align:'left',width:50},";

				}

				List<CgFormFieldEntity> subFields = submap.getHead().getColumns();
				for (CgFormFieldEntity subField : subFields) {
					if(StringUtils.isNotBlank(subField.getMainField())) {
						linkid=subField.getFieldName();
						break;
					}
				}

				appendLine(sb,"var "+submap.getHead().getTableName()+"durl = 'cgAutoListController.do?datagrid&configId="+submap.getHead().getTableName()+"&"+linkid+"='+row.id+'&field="+subfield+"&page=1&rows=100';");
				appendLine(sb,"var "+submap.getHead().getTableName()+"tablelines = $(this).datagrid('getRowDetail',index).find('table."+submap.getHead().getTableName()+"tablelines');");
				appendLine(sb,""+submap.getHead().getTableName()+"tablelines.datagrid({");
				appendLine(sb,"singleSelect:true,");
				appendLine(sb,"loadMsg:'正在加载',");
				appendLine(sb,"fitColumns:true,");
				appendLine(sb,"height:'180',");
				appendLine(sb,"pageSize : 50,");
				appendLine(sb,"pageList : [ 50, 150, 200, 250, 300 ],");
				appendLine(sb,"border:false,");
				appendLine(sb,"loadMsg:\"\",");
				appendLine(sb,"url: "+submap.getHead().getTableName()+"durl,");
				appendLine(sb,"idField : 'id',");  
				appendLine(sb,"rownumbers : true,");
				appendLine(sb,"pagination : false,"); 
				appendLine(sb,"onLoadSuccess : function(a,b,c) {},");
				appendLine(sb,"columns:[[");
				appendLine(sb,columns);
				appendLine(sb,"{field:'0000',hidden:true}");
				appendLine(sb,"]]");
				appendLine(sb,"});");
			}}
			appendLine(sb,"}");
		}

		//appendLine(sb,"$(function(){  storage=$.localStorage;if(!storage)storage=$.cookieStorage;");
		appendLine(sb,"$(document).ready(function(){  storage=$.localStorage;if(!storage)storage=$.cookieStorage;");
		appendLine(sb,this.getNoAuthOperButton());
		if (treegrid) {
			grid = "treegrid";
			appendLine(sb,"$(\'#" + name + "\').treegrid({");
			appendLine(sb,"idField:'id',");
			if(StringUtils.isNotEmpty(treeField)){
				appendLine(sb,"treeField:'"+treeField+"',");
			}else{
				appendLine(sb,"treeField:'text',");
			}
			appendLine(sb," onBeforeLoad: function(row,param){\n" +
					"                    if (!row) {    \n" +
					"                     delete param.id;  \n" +
					"                    }\n" +
					"                },");
		} else {
			grid = "datagrid";
			appendLine(sb,"$(\'#" + name + "\').datagrid({");
			if (this.isFilter()) {
				appendLine(sb,"onHeaderContextMenu: function(e, field){headerMenu(e, field);},");
			}
			appendLine(sb,"idField: '" + idField + "',");
		}
		if (title != null) {
			appendLine(sb,"title: \'" + title + "\',");
		}
		if(isShowSubGrid){
			appendLine(sb,"view: detailview,");
			appendLine(sb,"detailFormatter:detailFormatterFun,");
			appendLine(sb,"onExpandRow: onExpandRowFun,");
		}
		
		if(autoLoadData)
			appendLine(sb,"url:\'" + actionUrl + "&field=" + fields + "\',");
		else
			appendLine(sb,"url:\'',");
		if(StringUtils.isNotEmpty(rowStyler)){
			appendLine(sb,"rowStyler: function(index,row){ return "+rowStyler+"(index,row);},");
		}
		if(StringUtils.isNotEmpty(extendParams)){
			appendLine(sb,extendParams);
		}
		if (fit) {
			appendLine(sb,"fit:true,");
		} else {
			appendLine(sb,"fit:false,");
		}

		if(!nowrap){
			appendLine(sb,"nowrap:false,");
		}

		appendLine(sb,"rownumbers: true,");
		if(collapsible){
			appendLine(sb,"collapsible: true,");
		}

		if(hasQueryColum(columnList)){
			String queryParams = "";
			queryParams += "queryParams:{";
			for (DataGridColumn col : columnList) {
				if (col.isQuery()&&col.getDefaultVal()!=null&&!col.getDefaultVal().trim().equals("")) {
					//appendLine(sb,"queryParams:{documentTitle:'woniu'},");

					if(!"group".equals(col.getQueryMode())){
						queryParams += col.getField()+":'"+col.getDefaultVal()+"',";
					}

				}
			}
			if(queryParams.indexOf(",")>-1){
				queryParams = queryParams.substring(0, queryParams.length()-1);
			}
			queryParams += "},";
			//System.out.println("queryParams===="+queryParams);
			appendLine(sb,queryParams);
		}

		appendLine(sb,StringUtil.replaceAll("loadMsg: \'{0}\',", "{0}", MutiLangUtil.getLang("common.data.loading")));
		appendLine(sb,"pageSize: " + pageSize + ",");
		appendLine(sb,"pagination:" + pagination + ",");
		appendLine(sb,"pageList:[" + pageSize * 1 + "," + pageSize * 2 + "," + pageSize * 3 + "],");
		if(StringUtils.isNotBlank(sortName)){
			appendLine(sb,"sortName:'" +sortName +"',");
		}
		appendLine(sb,"sortOrder:'" + sortOrder + "',");
		appendLine(sb,"rownumbers:true,");
		if(singleSelect==null){
			appendLine(sb,"singleSelect:" + !checkbox + ",");
		}else{
			appendLine(sb,"singleSelect:" + singleSelect + ",");
		}
		if (fitColumns) {
			appendLine(sb,"fitColumns:true,");
		} else {
			appendLine(sb,"fitColumns:false,");
		}
		appendLine(sb,"striped:true,showFooter:true,");
		appendLine(sb,"frozenColumns:[[");
		this.getField(sb,0);
		appendLine(sb,"]],");
		
		appendLine(sb,"columns:[[");
		this.getField(sb);
		appendLine(sb,"]],");

		appendLine(sb,"onLoadSuccess:function(data){$(\"#"+name+"\")."+grid+"(\"clearChecked\");$(\"#"+name+"\")."+grid+"(\"clearSelections\");");

		//appendLine(sb," $(this).datagrid(\"fixRownumber\");");

		if(openFirstNode&&treegrid){
			appendLine(sb," if(data==null){");
			appendLine(sb," var firstNode = $(\'#" + name + "\').treegrid('getRoots')[0];");
			appendLine(sb," $(\'#" + name + "\').treegrid('expand',firstNode.id)}");
		}

		appendLine(sb,"if(!"+treegrid+"){");
		appendLine(sb,"if(data.total && data.rows.length==0) {");
		appendLine(sb,"var grid = $(\'#"+name+"\');");
		appendLine(sb,"var curr = grid.datagrid(\'getPager\').data(\"pagination\").options.pageNumber;");
		appendLine(sb,"grid.datagrid({pageNumber:(curr-1)});}}");

		appendLine(sb," try{loadAjaxDict(data);}catch(e){}");

		if(hasGroup){
			appendLine(sb,"optsMenuToggle('"+name+"');");
		}

		if (StringUtil.isNotEmpty(onLoadSuccess)) {
			appendLine(sb,onLoadSuccess + "(data);");
		}
		appendLine(sb,"},");
		if (StringUtil.isNotEmpty(onDblClick)) {
			appendLine(sb,"onDblClickRow:function(rowIndex,rowData){" + onDblClick + "(rowIndex,rowData);},");
		}
		if (treegrid) {
			appendLine(sb,"onClickRow:function(rowData){");
		}
		else {
			appendLine(sb,"onClickRow:function(rowIndex,rowData){");
		}
		/**行记录赋值*/
		appendLine(sb,"rowid=rowData.id;");
		appendLine(sb,"gridname=\'"+name+"\';");
		if (StringUtil.isNotEmpty(onClick)) {
			if (treegrid) {
				appendLine(sb,"" + onClick + "(rowData);");
			}else{
				appendLine(sb,"" + onClick + "(rowIndex,rowData);");
			}
		}
		appendLine(sb,"}");
		appendLine(sb,"});");
		this.setPager(sb, grid);

		appendLine(sb,"try{restoreheader();}catch(ex){}");
		appendLine(sb,"});");

		appendLine(sb,"function reloadTable(){");
		appendLine(sb,"try{");
		appendLine(sb,"	$(\'#\'+gridname).datagrid(\'reload\');" );
		appendLine(sb,"	$(\'#\'+gridname).treegrid(\'reload\');" );
		appendLine(sb,"}catch(ex){}");
		appendLine(sb,"}");
		appendLine(sb,"function reload" + name + "(){" + "$(\'#" + name + "\')." + grid + "(\'reload\');" + "}");
		appendLine(sb,"function get" + name + "Selected(field){return getSelected(field);}");
		appendLine(sb,"function getSelected(field){" + "var row = $(\'#\'+gridname)." + grid + "(\'getSelected\');" + "if(row!=null)" + "{" + "value= row[field];" + "}" + "else" + "{" + "value=\'\';" + "}" + "return value;" + "}");
		appendLine(sb,"function get" + name + "Selections(field){" + "var ids = [];" + "var rows = $(\'#" + name + "\')." + grid + "(\'getSelections\');" + "for(var i=0;i<rows.length;i++){" + "ids.push(rows[i][field]);" + "}" + "ids.join(\',\');" + "return ids" + "};");
		appendLine(sb,"function getSelectRows(){");
		appendLine(sb,"	return $(\'#"+name+"\').datagrid('getChecked');");
		appendLine(sb,"}");

		appendLine(sb," function saveHeader(){");
		appendLine(sb," var columnsFields =null;var easyextends=false;try{columnsFields = $('#"+name+"').datagrid('getColumns');easyextends=true;");
		appendLine(sb,"}catch(e){columnsFields =$('#"+name+"').datagrid('getColumnFields');}");
		appendLine(sb,"	var cols = storage.get( '"+name+"hiddenColumns');var init=true;	if(cols){init =false;} " +
				"var hiddencolumns = [];for(var i=0;i< columnsFields.length;i++) {if(easyextends){");
		appendLine(sb,"hiddencolumns.push({field:columnsFields[i].field,hidden:columnsFields[i].hidden});}else{");
		appendLine(sb, " var columsDetail = $('#"+name+"').datagrid(\"getColumnOption\", columnsFields[i]); ");
		appendLine(sb, "if(init){hiddencolumns.push({field:columsDetail.field,hidden:columsDetail.hidden,visible:(columsDetail.hidden==true?false:true)});}else{");
		appendLine(sb,"for(var j=0;j<cols.length;j++){");
		appendLine(sb,"		if(cols[j].field==columsDetail.field){");
		appendLine(sb,"					hiddencolumns.push({field:columsDetail.field,hidden:columsDetail.hidden,visible:cols[j].visible});");
		appendLine(sb,"		}");
		appendLine(sb,"}");
		appendLine(sb,"}} }");
		appendLine(sb,"storage.set( '"+name+"hiddenColumns',JSON.stringify(hiddencolumns));");
		appendLine(sb, "}");

		appendLine(sb," function isShowBut(){");
		appendLine(sb,"	  var isShowSearchId = $(\'#isShowSearchId\').val();");
		appendLine(sb,"	  if(isShowSearchId == \"true\"){");
		appendLine(sb,"		  $(\"#searchColums\").hide();");
		appendLine(sb,"	  	  $(\'#isShowSearchId\').val(\"false\");");
		appendLine(sb,"	  	  $(\'#columsShow\').remove(\"src\");");
		appendLine(sb,"	  	  $(\'#columsShow\').attr(\"src\",\"plug-in/easyui/themes/default/images/accordion_expand.png\");");
		appendLine(sb,"	  } else{");
		appendLine(sb,"		  $(\"#searchColums\").show();");
		appendLine(sb,"	  	  $(\'#isShowSearchId\').val(\"true\");");
		appendLine(sb,"	  	  $(\'#columsShow\').remove(\"src\");");
		appendLine(sb,"	  	  $(\'#columsShow\').attr(\"src\",\"plug-in/easyui/themes/default/images/accordion_collapse.png\");");
		appendLine(sb,"	  }");
		appendLine(sb,"}");

		appendLine(sb, "function restoreheader(){");
		appendLine(sb,"var cols = storage.get( '"+name+"hiddenColumns');if(!cols)return;");
		appendLine(sb, "for(var i=0;i<cols.length;i++){");
		appendLine(sb, "	try{");
		appendLine(sb,"if(cols.visible!=false)$('#"+name+"').datagrid((cols[i].hidden==true?'hideColumn':'showColumn'),cols[i].field);");
		appendLine(sb, "}catch(e){");
		appendLine(sb, "}");
		appendLine(sb, "}");
		appendLine(sb, "}");
		appendLine(sb, "function resetheader(){");
		appendLine(sb,"var cols = storage.get( '"+name+"hiddenColumns');if(!cols)return;");
		appendLine(sb, "for(var i=0;i<cols.length;i++){");
		appendLine(sb, "	try{");
		appendLine(sb,"  $('#"+name+"').datagrid((cols.visible==false?'hideColumn':'showColumn'),cols[i].field);");
		appendLine(sb, "}catch(e){");
		appendLine(sb, "}");
		appendLine(sb, "}");
		appendLine(sb, "}");

		if (columnList.size() > 0) {
			appendLine(sb,"function " + name + "search(){");
			//update by jg_renjie at 2016/1/11 for:TASK #823 增加form实现Form表单验证
			appendLine(sb,"try { if(! $(\"#"+name+"Form\").Validform({tiptype:3}).check()){return false;} } catch (e){}");
			appendLine(sb,"if(true){");
			//update by jg_renjie at 2016/1/11 for:TASK #823 增加form实现Form表单验证
			appendLine(sb,"var queryParams=$(\'#" + name + "\').datagrid('options').queryParams;");
			appendLine(sb,"$(\'#" + name + "tb\').find('*').each(function(){queryParams[$(this).attr('name')]=$(this).val();});");
			appendLine(sb,"$(\'#" + name + "\')." + grid + "({url:'" + actionUrl + "&field=" + searchFields + "',pageNumber:1});" + "}}");
			
			//高级查询执行方法
			appendLine(sb,"function dosearch(params){");
			appendLine(sb,"var jsonparams=$.parseJSON(params);");
			appendLine(sb,"$(\'#" + name + "\')." + grid + "({url:'" + actionUrl + "&field=" + searchFields + "',queryParams:jsonparams});" + "}");

			 //searchbox框执行方法
			  searchboxFun(sb,grid);

			//生成重置按钮功能js

			//回车事件
			appendLine(sb,"function EnterPress(e){");
			appendLine(sb,"var e = e || window.event;");
			appendLine(sb,"if(e.keyCode == 13){ ");
			appendLine(sb,name+"search();");
			appendLine(sb,"}}");


			appendLine(sb,"function searchReset(name){");
			appendLine(sb," $(\"#\"+name+\"tb\").find(\":input\").val(\"\");");
			//update by jg_renjie at 2016/1/11 for:TASK #823 增加form实现Form表单验证,此处避免reset时走验证，代码做了冗余
			//String func = name.trim() + "search();";
			//appendLine(sb,func);
			appendLine(sb,"var queryParams=$(\'#" + name + "\').datagrid('options').queryParams;");
			appendLine(sb,"$(\'#" + name + "tb\').find('*').each(function(){queryParams[$(this).attr('name')]=$(this).val();});");

			appendLine(sb,"$(\'#" + name + "tb\').find(\"input[type='checkbox']\").each(function(){$(this).attr('checked',false);});");
			appendLine(sb,"$(\'#" + name + "tb\').find(\"input[type='radio']\").each(function(){$(this).attr('checked',false);});");

			appendLine(sb,"$(\'#" + name + "\')." + grid + "({url:'" + actionUrl + "&field=" + searchFields + "',pageNumber:1});");
			//update by jg_renjie at 2016/1/11 for:TASK #823 增加form实现Form表单验证,此处避免reset时走验证，代码做了冗余
			appendLine(sb,"}");
		}

		//高级查询避免方法名出现重复
		if(oConvertUtils.isNotEmpty(complexSuperQuery)) {
			appendLine(sb,"function "+name+"SuperQuery(queryCode){if(typeof(windowapi)=='undefined'){$.dialog({content:'url:superQueryMainController.do?dialog&code='+queryCode+'&tableName="+name+"',width:880,height:400,zIndex:getzIndex(),title:'高级查询构造器',cache:false,lock:true})}else{$.dialog({content:'url:superQueryMainController.do?dialog&code='+queryCode+'&tableName="+name+"',width:880,height:400,zIndex:getzIndex(),title:title,cache:false,lock:true,parent:windowapi})}};");
		}
		//过滤操作
		getFilterFields(sb);

		appendLine(sb,"</script>");
		appendLine(sb,"<table width=\"100%\"   id=\"" + name + "\" toolbar=\"#" + name + "tb\"></table>");
		appendLine(sb,"<div id=\"" + name + "tb\" style=\"padding:3px; height: auto\">");

		if(hasQueryColum(columnList)&&isShowSearch==true){
			appendLine(sb,"<input  id=\"columsShow\" type=\"image\" src=\"plug-in/easyui/themes/default/images/accordion_collapse.png\" onclick=\"isShowBut()\">");
		}


		boolean blink = false;

		appendLine(sb,"<input  id=\"_complexSqlbuilder\" name=\"complexSqlbuilder\"   type=\"hidden\" />");

		
		if(hasQueryColum(columnList) && "group".equals(getQueryMode())){

			blink = true;

			String searchColumStyle = toolBarList!=null&&toolBarList.size()!=0?"":"style='border-bottom: 0px'";
			appendLine(sb,"<div name=\"searchColums\" id=\"searchColums\" "+searchColumStyle+">");

			appendLine(sb,"<input  id=\"isShowSearchId\" type=\"hidden\" value=\""+isShowSearch+"\"/>");

			//-----longjb1 增加用于高级查询的参数项 
			appendLine(sb,"<input  id=\"_sqlbuilder\" name=\"sqlbuilder\"   type=\"hidden\" />");

			//update by jg_renjie at 2016/1/11 for:TASK #823 增加form实现Form表单验证

			
			appendLine(sb,"<form onkeydown='if(event.keyCode==13){" + name + "search();return false;}' id='"+name+"Form'>");

			
			//update by jg_renjie at 2016/1/11 for:TASK #823

			appendLine(sb,"<span style=\"max-width: 79%;display: inline-block;\">");

			appendLine(sb,"<span><img style=\"margin-top:-3px;vertical-align:middle;\" src=\"plug-in/easyui/themes/icons/ti.png\"  title=\"提示：模糊查询通配符: *，多个关键字用半角逗号 ',' 隔开！\" alt=\"提示：模糊查询通配符: *，多个关键字用半角逗号 ',' 隔开！\" /></span>");

			
			getSearchFormInfo(sb);
			appendLine(sb,"</span>");

			appendLine(sb,"<span>");
			getSearchButton(sb);
			appendLine(sb,"</span>");

			appendLine(sb,"</form></div>");

		}else if(hasQueryColum(columnList) && "advanced".equals(getQueryMode())){
			blink = true;
			String searchColumStyle = toolBarList!=null&&toolBarList.size()!=0?"":"style='border-bottom: 0px'";

			appendLine(sb,"<div name=\"searchColums\" style=\"display:none;\" id=\"searchColums\" "+searchColumStyle+">");

			appendLine(sb,"<input  id=\"isShowSearchId\" type=\"hidden\" value=\""+isShowSearch+"\"/>");
			//-----longjb1 增加用于高级查询的参数项 
			appendLine(sb,"<input  id=\"_sqlbuilder\" name=\"sqlbuilder\"   type=\"hidden\" />");
			appendLine(sb,"<form onkeydown='if(event.keyCode==13){" + name + "search();return false;}' id='"+name+"Form'>");
			appendLine(sb,"<span style=\"max-width: 79%;display: inline-block;\">");
			getSearchFormInfo(sb);
			appendLine(sb,"</span>");
			appendLine(sb,"<span>");
			getSearchButton(sb);
			appendLine(sb,"</span>");
			appendLine(sb,"</form></div>");
		}

		if(toolBarList==null || toolBarList.size()==0){

			appendLine(sb,"<div style=\"height:0px;\" >");
		}else{//TODO

			appendLine(sb,"<div style=\"border-bottom-width:0;\" class=\"datagrid-toolbar\">");

		}
		appendLine(sb,"<span style=\"float:left;\" >");
		if(toolBarList.size()>0)
		{

			Boolean hasMore = false;
			for (DataGridUrl toolBar : toolBarList) {
				if(toolBar.isInGroup()){
					if(!hasMore){
						hasMore = true;
					}
				}else{
				    //TODO 加载菜单按钮JS
					loadToolbar(toolBar, sb);
				}
			}
			if(hasMore){
				loadToolbarMoreBtn(sb,true,null);
				appendLine(sb,"<div class='toolbar-more-container'><ul class='toolbar-more-list'>");
				for (DataGridUrl toolBar : toolBarList) {
					if(toolBar.isInGroup()){
						appendLine(sb,"		<li>");
						//TODO 加载更多按钮组JS
						loadToolbarMoreBtn(sb,false,toolBar);
						appendLine(sb,"		</li>");
					}
				}
				appendLine(sb,"</ul></div>");
				//appendLine(sb,"<div class='btn-group'><button data-toggle='dropdown' class='btn btn-default dropdown-toggle'>操作<span class='caret'></span> </button><ul class='dropdown-menu'><li><a href='buttons.html#'>置顶</a></li><li><a href='buttons.html#' class='font-bold'>修改</a></li><li><a href='buttons.html#'>禁用</a></li><li class='divider'></li> <li><a href='buttons.html#'>删除</a></li> </ul> </div>");
			}

		}
		appendLine(sb,"</span>");

		 if("single".equals(getQueryMode())&& hasQueryColum(columnList)){//如果表单是单查询
			appendLine(sb,"<span style=\"float:right\">");
			appendLine(sb,"		<input id=\""+name+"searchbox\" class=\"easyui-searchbox\"  data-options=\"searcher:"+name+ StringUtil.replaceAll("searchbox,prompt:\'{0}\',menu:\'#", "{0}", MutiLangUtil.getLang("common.please.input.keyword")) +name+"mm\'\"></input>");
			appendLine(sb,"		<div id=\""+name+"mm\" style=\"width:120px\">");
			for (DataGridColumn col : columnList) {
				if (col.isQuery()) {

					appendLine(sb,"			<div data-options=\"name:\'"+col.getField().replaceAll("_","\\.")+"\',iconCls:\'icon-ok\' \">"+col.getTitle()+"</div>  ");

				}
			}
			appendLine(sb,"		</div>");
			appendLine(sb,"</span>");

		}else if ("advanced".equals(getQueryMode()) && hasQueryColum(columnList)) {// 如果表单是高级查询
			appendLine(sb,"<span style=\"float:right\">");
			if (btnCls != null && !btnCls.equals("easyui")) {// 自定以样式 bootstrap按钮样式
				if (btnCls.indexOf("bootstrap") == 0) {
					String defalutCls = "btn btn-info btn-xs";

					if (btnCls.replace("bootstrap", "").trim().length() > 0) {
						defalutCls = btnCls.replace("bootstrap", "").trim();
					}
					if (superQuery) {

						appendLine(sb,"		<button class=\"" + defalutCls + "\"  type=\"button\" onclick=\"queryBuilder()\">");
						appendLine(sb,"			<i class=\"fa fa-search\"></i>");
						appendLine(sb,"			<span class=\"bigger-110 no-text-shadow\">"+ MutiLangUtil.getLang("common.superquery") + "</span>");
						appendLine(sb,"		</button>");
						appendLine(sb,"</span>");
					}

				} else {// 自定以样式
					if (superQuery) {
						appendLine(sb,"		<a href=\"#\" class=\"" + btnCls + "\" onclick=\"queryBuilder('" + StringUtil
								.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.superquery")));
					}
				}
			} else {// 默认使用easyUI按钮样式
				if (superQuery) {
					appendLine(sb,"		<a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-search\" onclick=\"queryBuilder('"
									+ StringUtil.replaceAll("')\">{0}</a>", "{0}",
											MutiLangUtil.getLang("common.superquery")));
				}
			}
			appendLine(sb,"		</span>");
		}

		appendLine(sb,"		<div style=\"clear:both\"></div>");

		appendLine(sb,"</div>");

		if(blink){
			sb.insert(0, "<link rel=\"stylesheet\" href=\"plug-in/Validform/css/style.css\" type=\"text/css\">" +
						"<link rel=\"stylesheet\" href=\"plug-in/Validform/css/tablefrom.css\" type=\"text/css\">" +
						"<script type=\"text/javascript\" src=\"plug-in/Validform/js/Validform_v5.3.1_min_zh-cn.js\"></script>" +
						"<script type=\"text/javascript\" src=\"plug-in/Validform/js/Validform_Datatype_zh-cn.js\"></script>" +
						"<script type=\"text/javascript\" src=\"plug-in/Validform/js/datatype_zh-cn.js\"></script>");
		}

		
		if(queryBuilder){

			if (btnCls != null && !btnCls.equals("easyui")) {//自定以样式 bootstrap按钮样式
				
				addQueryBuilder(sb,btnCls);
				
			}else{//默认使用easyUI按钮样式 

				addQueryBuilder(sb,"easyui-linkbutton");
			}
		}

		if(superQuery) {
			if(btnCls != null && !btnCls.equals("easyui")) {//自定义bootstrap按钮样式
				addSuperQuery(sb,btnCls,columnList);
			} else {
				addSuperQuery(sb,"easyui-linkbutton",columnList);
			}
		}

		if(oConvertUtils.isNotEmpty(complexSuperQuery)){
			if(btnCls != null && !btnCls.equals("easyui")) {//自定义bootstrap按钮样式
				addAdvancedQuery(sb,btnCls);
			} else {
				addAdvancedQuery(sb,"easyui-linkbutton");
			}
		}

		this.getFilter(sb);

		return sb;
	}

	/**
	 * 仅用于 加载【更多操作】 按钮的方法
	 * @param sb 
	 * @param isShow 是否是【更多操作】按钮，false则为点击【更多操作】后展示的按钮
	 * @param toolBar
	 */
	private void loadToolbarMoreBtn(StringBuffer sb,boolean isShow,DataGridUrl toolBar){
		if(isShow){
			if (btnCls != null && !btnCls.equals("easyui")) {
				if(btnCls.indexOf("bootstrap")==0){
					if (btnCls.replace("bootstrap", "").trim().length() > 0) {
						sb.append("		<button class=\""+btnCls.replace("bootstrap", "").trim()+"\" ");
					}else{
						sb.append("		<button class=\"btn btn-default btn-xs\" ");
					}
					sb.append("onclick='toggleMoreToolbars(this)'");
					appendLine(sb,">");
					appendLine(sb,"		<i class=\"fa fa-caret-down\"></i><span class=\"bigger-110 no-text-shadow\">更多操作</span>");
					appendLine(sb,"	</button>");
				}else{
					appendLine(sb,"		<a href=\"javascript:void(0)\" onclick='toggleMoreToolbars(this)' class=\""+btnCls+" " + toolBar.getIcon()+"\" >更多操作</a>");
				}
			}else if(btnCls == null || btnCls.equals("easyui")){
				appendLine(sb,"		<a href=\"javascript:void(0)\"  onclick='toggleMoreToolbars(this)' class=\"easyui-linkbutton\" plain=\"true\" icon=\"icon-caret-down\">更多操作</a> ");
				
			}
		}else{
			sb.append("<a href='javascript:void(0)' ");
			if(StringUtil.isNotEmpty(toolBar.getId())){
				sb.append(" id=\"");
				sb.append(toolBar.getId());
				sb.append("\" ");
			}
			if(StringUtil.isNotEmpty(toolBar.getOnclick())){
				sb.append("onclick="+toolBar.getOnclick()+"");
			}else{
				sb.append("onclick=\""+toolBar.getFunname()+"(");
				if(!toolBar.getFunname().equals("doSubmit")){
					sb.append("\'"+toolBar.getTitle()+"\',");
				}
				String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
				String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
				sb.append("\'"+toolBar.getUrl()+"\',\'"+name+"\',"+width+","+height+")\"");
			}
			if(btnCls == null || btnCls.equals("easyui")){
				sb.append("><span class=\"easyui-mycon "+toolBar.getIcon()+"\"></span> "+toolBar.getTitle());
			}else{
				sb.append("><i class=\""+toolBar.getIcon()+"\"></i> "+toolBar.getTitle());
			}
			sb.append("</a>");
		}
		
	}

    /**
     * 加载操作栏的js
     * @param toolBar
     * @param sb
     */
	private void loadToolbar(DataGridUrl toolBar,StringBuffer sb){

		if (btnCls != null && !btnCls.equals("easyui")) {//自定以样式 bootstrap按钮样式
			if(btnCls.indexOf("bootstrap")==0){
				if (btnCls.replace("bootstrap", "").trim().length() > 0) {
					sb.append("<button class=\""+btnCls.replace("bootstrap", "").trim()+"\" ");
				}else{
					sb.append("<button class=\"btn btn-default btn-xs\" ");
				}

				if(StringUtil.isNotEmpty(toolBar.getId())){
					sb.append(" id=\"");
					sb.append(toolBar.getId());
					sb.append("\" ");
				}

				
				if(StringUtil.isNotEmpty(toolBar.getOnclick()))
				{
					sb.append("onclick="+toolBar.getOnclick()+"");
				}
				else {
					sb.append("onclick=\""+toolBar.getFunname()+"(");
					if(!toolBar.getFunname().equals("doSubmit"))
					{
						sb.append("\'"+toolBar.getTitle()+"\',");
					}
					String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
					String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
					sb.append("\'"+toolBar.getUrl()+"\',\'"+name+"\',"+width+","+height+")\"");
				}
				appendLine(sb,"><i class=\"" + toolBar.getIcon() + "\"></i>");
				appendLine(sb,"<span class=\"bigger-110 no-text-shadow\">"+toolBar.getTitle()+"</span></button>");
				
			}else{
				sb.append("<a href=\"#\" class=\""+btnCls+" " + toolBar.getIcon()+"\" ");

				if(StringUtil.isNotEmpty(toolBar.getId())){
					sb.append(" id=\"");
					sb.append(toolBar.getId());
					sb.append("\" ");
				}

				if(StringUtil.isNotEmpty(toolBar.getOnclick()))
				{
					sb.append("onclick="+toolBar.getOnclick()+"");
				}
				else {
					sb.append("onclick=\""+toolBar.getFunname()+"(");
					if(!toolBar.getFunname().equals("doSubmit"))
					{
						sb.append("\'"+toolBar.getTitle()+"\',");
					}
					String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
					String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
					sb.append("\'"+toolBar.getUrl()+"\',\'"+name+"\',"+width+","+height+")\"");
				}
                sb.append(">");
                appendLine(sb,toolBar.getTitle()+"</a>");
			}
			
		}else if(btnCls == null || btnCls.equals("easyui")){//easyUI按钮样式
			
			sb.append("<a href=\"#\" class=\"easyui-linkbutton\" plain=\"true\" icon=\""+toolBar.getIcon()+"\" ");

			if(StringUtil.isNotEmpty(toolBar.getId())){
				sb.append(" id=\"");
                sb.append(toolBar.getId());
				sb.append("\" ");
			}

			if(StringUtil.isNotEmpty(toolBar.getOnclick()))
			{
				sb.append("onclick="+toolBar.getOnclick()+"");
			}
			else {
                sb.append("onclick=\""+toolBar.getFunname()+"(");
				if(!toolBar.getFunname().equals("doSubmit"))
				{
                    sb.append("\'"+toolBar.getTitle()+"\',");
				}
				String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
				String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
                sb.append("\'"+toolBar.getUrl()+"\',\'"+name+"\',"+width+","+height+")\"");
			}
            sb.append(">");
			appendLine(sb,toolBar.getTitle()+"</a>");
		}

	}

	private void loadSubData(String id){
		CgFormFieldServiceI cgFormFieldService = (CgFormFieldServiceI)ApplicationContextUtil.getContext().getBean("cgFormFieldService");
		String tableName = id;
    	String tablename = PublicUtil.replaceTableName(tableName);
        Map<String, Object> data = new HashMap<String, Object>();
        Map configData = null;
        String jversion = cgFormFieldService.getCgFormVersionByTableName(id);
        configData = cgFormFieldService.getFtlFormConfig(tableName,jversion);
        
    	data = new HashMap(configData);
    	//如果该表是主表查出关联的附表
    	CgFormHeadEntity head = (CgFormHeadEntity)data.get("head");
    	this.tableData = (Map<String, Object>)data.get("field");
    	this.head = head;
	}

	private void getSearchButton(StringBuffer sb) {
		if("group".equals(getQueryMode()) && hasQueryColum(columnList)){//如果表单是组合查询

			appendLine(sb,"<span style=\"float:right;\">");

			if (btnCls != null && !btnCls.equals("easyui")) {//自定以样式 bootstrap按钮样式
				if(btnCls.indexOf("bootstrap")==0){
					String defalutCls = "btn btn-info btn-xs";
					
					if (btnCls.replace("bootstrap", "").trim().length() > 0) {
						defalutCls = btnCls.replace("bootstrap", "").trim();
					}

					appendLine(sb,"<button class=\""+defalutCls+"\" type=\"button\"  onclick=\"" + name + "search()\">");
					appendLine(sb,"<i class=\"fa fa-search\"></i>");
					appendLine(sb,"<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.query")+"</span>");
					appendLine(sb,"</button>");						

					appendLine(sb,"<button class=\""+defalutCls+"\"  type=\"button\" onclick=\"searchReset('" + name + "')\">");
					appendLine(sb,"<i class=\"fa fa-refresh\"></i>");
					appendLine(sb,"<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.reset")+"</span>");
					appendLine(sb,"</button>");
					
					if(queryBuilder){
						appendLine(sb,"<button class=\""+defalutCls+"\"  type=\"button\" onclick=\"queryBuilder()\">");
						appendLine(sb,"<i class=\"fa fa-search\"></i>");
						appendLine(sb,"<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.querybuilder")+"</span>");
						appendLine(sb,"</button>");
					}

					if(superQuery) {
						appendLine(sb,"<button class=\""+defalutCls+"\"  type=\"button\" onclick=\"queryBuilder()\">");
						appendLine(sb,"<i class=\"fa fa-search\"></i>");
						appendLine(sb,"<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.superquery")+"</span>");
						appendLine(sb,"</button>");
					}

					if(oConvertUtils.isNotEmpty(complexSuperQuery)) {

						appendLine(sb,"<button class=\""+defalutCls+"\"  type=\"button\" onclick=\""+name+"SuperQuery('"+complexSuperQuery+"')\">");

						appendLine(sb,"<i class=\"fa fa-search\"></i>");
						appendLine(sb,"<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.advancedQuery")+"</span>");
						appendLine(sb,"</button>");
					}

				}else{//自定以样式
					appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\""+  name+ StringUtil.replaceAll("search()\">{0}</a>", "{0}", MutiLangUtil.getLang("common.query")));
					appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\"searchReset('"+name+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.reset")) );
					if(queryBuilder){
						appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.querybuilder")) );
					}

					if(superQuery) {
						appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.superquery")) );
					}

					if(oConvertUtils.isNotEmpty(complexSuperQuery)) {

						appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\""+name+"SuperQuery('"+complexSuperQuery+"')\">"+MutiLangUtil.getLang("common.advancedQuery")+"</a>");

					}

				}
			}else{//默认使用easyUI按钮样式 
				appendLine(sb,"<a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-search\" onclick=\""+  name+ StringUtil.replaceAll("search()\">{0}</a>", "{0}", MutiLangUtil.getLang("common.query")));
				appendLine(sb,"<a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-reload\" onclick=\"searchReset('"+name+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.reset")) );
				if(queryBuilder){
					appendLine(sb,"<a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-search\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.querybuilder")) );
				}

				if(superQuery) {
					appendLine(sb,"<a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-search\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.superquery")) );
				}

				if(oConvertUtils.isNotEmpty(complexSuperQuery)) {

					appendLine(sb,"<a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-search\" onclick=\""+name+"SuperQuery('"+complexSuperQuery+"')\">"+MutiLangUtil.getLang("common.advancedQuery")+"</a>");

				}

			}
			appendLine(sb,"</span>");

		}
	}

	/**
	 * 构建查询form当中的信息
	 * @param sb
	 */
	private void getSearchFormInfo(StringBuffer sb) {
		//如果表单是组合查询		
		if("group".equals(getQueryMode())){
			int i = 0;
			for (DataGridColumn col : columnList) {
				if (col.isQuery()) {

					appendLine(sb,"<span style=\"display:-moz-inline-box;display:inline-block;margin-bottom:2px;text-align:justify;\">");

					if(i==0){
						appendLine(sb,"<span style=\"vertical-align:middle;display:-moz-inline-box;display:inline-block;width:74px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space:nowrap; \" title=\""+col.getTitle()+"\">"+"&nbsp;&nbsp;&nbsp;"+col.getTitle()+"："+ (col.getTitle().length()<4?"&nbsp;&nbsp;&nbsp;":"") + "</span>");
					}else{
						appendLine(sb,"<span style=\"vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 90px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space:nowrap; \" title=\""+col.getTitle()+"\">"+col.getTitle()+"：</span>");
					}

					
					if("single".equals(col.getQueryMode())){
						if(!StringUtil.isEmpty(col.getReplace())){
							appendLine(sb,"<select name=\""+col.getField().replaceAll("_","\\.")+"\" WIDTH=\"120\" style=\"width: 120px\"> ");
							appendLine(sb,StringUtil.replaceAll("<option value =\"\" >{0}</option>", "{0}", MutiLangUtil.getLang("common.please.select")));
							String[] test = col.getReplace().split(",");
							String text = "";
							String value = "";
							
							
							
							for (String string : test) {
								String lang_key = string.split("_")[0];
								text = MutiLangUtil.getLang(lang_key);
								value =string.split("_")[1];

								if(col.getDefaultVal()!=null&&col.getDefaultVal().trim().equals(value)){
									appendLine(sb,"<option value =\""+value+"\" selected=\"selected\">"+text+"</option>");
								}else{
									appendLine(sb,"<option value =\""+value+"\" >"+text+"</option>");
								}

								
							}
							appendLine(sb,"</select>");
						}else if(!StringUtil.isEmpty(col.getDictionary())){

							if(col.getDictionary().contains(",")&&col.isPopup()){
								String[] dic = col.getDictionary().split(",");

//								String sql;
//								if(!StringUtil.isEmpty(col.getDictCondition())){
//									sql = "select " + dic[1] + " as field," + dic[2]+ " as text from " + dic[0]+" "+col.getDictCondition();
//								}else{
//									sql = "select " + dic[1] + " as field," + dic[2]+ " as text from " + dic[0];
//								}

								//System.out.println(dic[0]+"--"+dic[1]+"--"+dic[2]);
							//	<input type="text" name="order_code"  style="width: 100px"  class="searchbox-inputtext" value="" onClick="inputClick(this,'account','user_msg');" />

								if(col.getDefaultVal()!=null&&!col.getDefaultVal().trim().equals("")){
									appendLine(sb,"<input type=\"text\" name=\""+col.getField().replaceAll("_","\\.")+"\" style=\"width: 120px\" class=\"searchbox-inputtext\" value=\"\" onClick=\"popupClick(this,'"+dic[2].replaceAll("@", ",")+"','"+dic[1].replaceAll("@", ",")+"','"+dic[0]+"');\" value=\""+col.getDefaultVal()+"\"/> ");
								}else{
									appendLine(sb,"<input type=\"text\" name=\""+col.getField().replaceAll("_","\\.")+"\" style=\"width: 120px\" class=\"searchbox-inputtext\" value=\"\" onClick=\"popupClick(this,'"+dic[2].replaceAll("@", ",")+"','"+dic[1].replaceAll("@", ",")+"','"+dic[0]+"');\" /> ");
								}

							}else if(col.getDictionary().contains(",")&&(!col.isPopup())){
								String[] dic = col.getDictionary().split(",");

								String sql;
								if(!StringUtil.isEmpty(col.getDictCondition())){
									sql = "select " + dic[1] + " as field," + dic[2]+ " as text from " + dic[0]+" "+col.getDictCondition();
								}else{
									sql = "select " + dic[1] + " as field," + dic[2]+ " as text from " + dic[0];
								}

								systemService = ApplicationContextUtil.getContext().getBean(
										SystemService.class);
								List<Map<String, Object>> list = systemService.findForJdbc(sql);
								
								
								String showMode = col.getShowMode();
								if (null != showMode && "radio".equals(showMode)) {
									String field = col.getField().replaceAll("_","\\.");
									appendLine(sb,"<input type=\"hidden\"  name=\""+field+"\" id=\""+field+"_radio\"/>");									
									for (Map<String, Object> map : list){	//									    

										if(col.getDefaultVal()!=null && col.getDefaultVal().trim().equals(map.get("field"))){
											appendLine(sb," <input type=\"radio\" name=\""+field+"_radio\" onclick=\"javascrpt:$('#"+field+"_radio').val('"+map.get("field")+"');\" value=\""+map.get("field")+"\" checked=\"checked\" />");
											appendLine(sb," <script type=\"text/javascript\">");
											appendLine(sb,"  	$('#"+ field+"_radio').val(\""+map.get("field")+"\");");
											appendLine(sb," </script>");
										}else{
											appendLine(sb," <input type=\"radio\" name=\""+field+"_radio\" onclick=\"javascrpt:$('#"+field+"_radio').val('"+map.get("field")+"');\" value=\""+map.get("field")+"\" />");
										}	

										appendLine(sb,(String)map.get("text"));
									}
								}else if (null != showMode && "checkbox".equals(showMode)) {
									String field = col.getField().replaceAll("_","\\.");
									appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_checkbox\" value=\"\" />");
									for (Map<String, Object> map : list){	//									    
										if(col.getDefaultVal()!=null && col.getDefaultVal().trim().equals(map.get("field"))){											
											appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+map.get("field")+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+map.get("field")+",',''));}\" value=\"" + map.get("field") + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" checked=\"checked\" />");
											appendLine(sb," <script type=\"text/javascript\">");
											appendLine(sb,"  	$(\"#"+ field +"_checkbox\").val($(\"#"+ field +"_checkbox\").val()+,"+map.get("field")+",);");
											appendLine(sb," </script>");											
										}else{											
											appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+map.get("field")+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+map.get("field")+",',''));}\" value=\"" + map.get("field") + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" />");
										}
										appendLine(sb,(String)map.get("text"));
									}
								}else{
									appendLine(sb,"<select name=\""+col.getField().replaceAll("_","\\.")+"\" WIDTH=\"120\" style=\"width: 120px\"> ");
									appendLine(sb,StringUtil.replaceAll("<option value =\"\" >{0}</option>", "{0}", MutiLangUtil.getLang("common.please.select")));
									for (Map<String, Object> map : list){

										if(col.getDefaultVal()!=null&&col.getDefaultVal().trim().equals(map.get("field"))){
											appendLine(sb," <option value=\""+map.get("field")+"\" selected=\"selected\">");
										}else{
											appendLine(sb," <option value=\""+map.get("field")+"\">");
										}

	
										appendLine(sb,(String)map.get("text"));
										appendLine(sb," </option>");
									}
									appendLine(sb,"</select>");
								}
								
							}else{
								List<TSType> types = ResourceUtil.getCacheTypes(col.getDictionary().toLowerCase());
								
								String showMode = col.getShowMode();
								if (null != showMode && "radio".equals(showMode)) {
									if (types != null) {
										String field = col.getField().replaceAll("_","\\.");
										appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_radio\"/>");										
										for (TSType type : types) {
											String typeCode = type.getTypecode();

											if(col.getDefaultVal()!=null&&col.getDefaultVal().trim().equals(typeCode)){
												appendLine(sb," <input type=\"radio\" value=\"" + typeCode + "\" name=\""+field+"_radio\" onclick=\"javascrpt:#('#"+ field+"_radio').val('" + typeCode + "');\" checked=\"checked\" />");
												appendLine(sb," <script type=\"text/javascript\">");
												appendLine(sb,"  $('#"+ field+"_radio').val('"+typeCode+"');");
												appendLine(sb," </script>");
											}else{
												appendLine(sb," <input type=\"radio\" value=\"" + typeCode + "\" name=\""+field+"_radio\" onclick=\"javascrpt:$('#"+ field+"_radio').val('" + typeCode + "');\" />");
											}										

											appendLine(sb,MutiLangUtil.getLang(type.getTypename()));
										}
									}
								}else if (null != showMode && "checkbox".equals(showMode)) {
									if (types != null) {
										String field = col.getField().replaceAll("_","\\.");
										appendLine(sb,"<input type=\"hidden\" name=\""+field+"\" id=\""+field+"_checkbox\" value=\"\" />");
										for (TSType type : types) {
											String typeCode = type.getTypecode();
											if(col.getDefaultVal()!=null&&col.getDefaultVal().trim().equals(typeCode)){
												appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+typeCode+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+typeCode+",',''));}\" value=\"" + typeCode + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" checked=\"checked\" />");
												appendLine(sb," <script type=\"text/javascript\">");
												appendLine(sb,"  	$(\"#"+ field +"_checkbox\").val($(\"#"+ field +"_checkbox\").val()+,"+typeCode+",);");
												appendLine(sb," </script>");
											}else{
												appendLine(sb," <input type=\"checkbox\" onclick=\"javascript:if(this.checked)$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val()+',"+typeCode+",');else{$('#"+ field +"_checkbox').val($('#"+ field +"_checkbox').val().replace(',"+typeCode+",',''));}\" value=\"" + typeCode + "\" name=\"" + field +"_checkbox\" class=\"" + field + "_checkbox\" />");
											}										
											appendLine(sb,MutiLangUtil.getLang(type.getTypename()));
										}
									}
								}else{
									appendLine(sb,"<select name=\""+col.getField().replaceAll("_","\\.")+"\" WIDTH=\"120\" style=\"width: 120px\"> ");
									appendLine(sb,StringUtil.replaceAll("<option value =\"\" >{0}</option>", "{0}", MutiLangUtil.getLang("common.please.select")));
									if (types != null) {
										for (TSType type : types) {

											if(col.getDefaultVal()!=null&&col.getDefaultVal().trim().equals(type.getTypecode())){
												appendLine(sb," <option value=\""
														+ type.getTypecode()
														+ "\" selected=\"selected\">");
											}else{
												appendLine(sb," <option value=\""
														+ type.getTypecode()
														+ "\">");
											}

										
											appendLine(sb,MutiLangUtil.getLang(type.getTypename()));
											appendLine(sb," </option>");
										}
									}
									appendLine(sb,"</select>");
								}
							}
						}else if(col.isAutocomplete()){
							appendLine(sb,getAutoSpan(col.getField().replaceAll("_","\\."),extendAttribute(col.getExtend())));
						}else{

							appendLine(sb,"<input onkeypress=\"EnterPress(event)\" onkeydown=\"EnterPress()\"  type=\"text\" name=\""+col.getField().replaceAll("_","\\.")+"\"  "+extendAttribute(col.getExtend())+" ");
							if(this.DATE_FORMATTER.equals(col.getFormatter())){
								appendLine(sb," style=\"width: 120px\" class=\"Wdate\" onClick=\"WdatePicker()\" ");
							}else if(this.DATETIME_FORMATTER.equals(col.getFormatter())){
								appendLine(sb," style=\"width: 160px\" class=\"Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" ");
							}else{
								appendLine(sb," style=\"width: 120px\" class=\"inuptxt\" ");
							}
							if(oConvertUtils.isNotEmpty(col.getDefaultVal())){
								appendLine(sb," value=\""+col.getDefaultVal()+"\" ");
							}
							appendLine(sb," />");

							
							//appendLine(sb,"<input onkeypress=\"EnterPress(event)\" onkeydown=\"EnterPress()\"  type=\"text\" name=\""+col.getField().replaceAll("_","\\.")+"\"  "+extendAttribute(col.getExtend())+"  class=\"inuptxt\" style=\"width: 100px\" value="+col.getDefaultVal()==null?"":"\""+col.getDefaultVal()+"\""+"/>");
							
						}
					}else if("group".equals(col.getQueryMode())){

						if(this.DATE_FORMATTER.equals(col.getFormatter())){
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_begin\"  style=\"width: 100px\" "+extendAttribute(col.getExtend())+" class=\"Wdate\" onClick=\"WdatePicker()\"/>");
							appendLine(sb,"<span style=\"display:-moz-inline-box;display:inline-block;width: 8px;text-align:right;\">~</span>");
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_end\"  style=\"width: 100px\" "+extendAttribute(col.getExtend())+" class=\"Wdate\" onClick=\"WdatePicker()\"/>");
						}else if(this.DATETIME_FORMATTER.equals(col.getFormatter())){
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_begin\"  style=\"width: 140px\" "+extendAttribute(col.getExtend())+" class=\"Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\"/>");
							appendLine(sb,"<span style=\"display:-moz-inline-box;display:inline-block;width: 8px;text-align:right;\">~</span>");
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_end\"  style=\"width: 140px\" "+extendAttribute(col.getExtend())+" class=\"Wdate\" onClick=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\"/>");
						}else{
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_begin\"  style=\"width: 100px\" "+extendAttribute(col.getExtend())+" class=\"inuptxt\"/>");
							appendLine(sb,"<span style=\"display:-moz-inline-box;display:inline-block;width: 8px;text-align:right;\">~</span>");
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_end\"  style=\"width: 100px\" "+extendAttribute(col.getExtend())+" class=\"inuptxt\"/>");
						}

					}
					appendLine(sb,"</span>");
					i++;
				}
			}
		}
	}


	/**
	 * 生成扩展属性
	 * @param field
	 * @return
	 */
	private String extendAttribute(String field) {
		if(StringUtil.isEmpty(field)){
			return "";
		}
		field = dealSyscode(field,1);
		StringBuilder re = new StringBuilder();
		try{
			JSONObject obj = JSONObject.fromObject(field);
			Iterator it = obj.keys();
			while(it.hasNext()){
				String key = String.valueOf(it.next());
				JSONObject nextObj = null;
				try {
					 nextObj =((JSONObject)obj.get(key));
					 Iterator itvalue =nextObj.keys();
						re.append(key+"="+"\"");
						if(nextObj.size()<=1){
							String onlykey = String.valueOf(itvalue.next());
							if("value".equals(onlykey)){
								re.append(nextObj.get(onlykey)+"");
							}else{
								re.append(onlykey+":"+nextObj.get(onlykey)+"");
							}
						}else{
							while(itvalue.hasNext()){
								String multkey = String.valueOf(itvalue.next());
								String multvalue = nextObj.getString(multkey);
								re.append(multkey+":"+multvalue+";");
							}
							re.deleteCharAt(re.length()-1);
						}
						re.append("\" ");
				} catch (Exception e) {
					re.append(key+"="+"\"");
					re.append(obj.get(key)+"\"");
					re.append("\" ");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return dealSyscode(re.toString(), 2);
	}
	
	
	/**
	 * 生成扩展属性
	 * @param field
	 * @return
	 */
	private String extendAttributeOld(String field) {
		StringBuffer sb = new StringBuffer();
		//增加扩展属性
		if (!StringUtils.isBlank(field)) {
			Gson gson = new Gson();
			Map<String, String> mp = gson.fromJson(field, Map.class);
			for(Map.Entry<String, String> entry: mp.entrySet()) { 

				appendLine(sb,entry.getKey()+"=" + gson.toJson(entry.getValue()) + "\"");
				} 
		}
		return sb.toString();
	}
	
	/**
	 * 处理否含有json转换中的保留字
	 * @param field
	 * @param flag 1:转换 2:还原
	 * @return
	 */
	private String dealSyscode(String field,int flag) {
		String change = field;
		Iterator it = syscode.keySet().iterator();
		while(it.hasNext()){
			String key = String.valueOf(it.next());
			String value = String.valueOf(syscode.get(key));
			if(flag==1){
				change = field.replaceAll(key, value);
			}else if(flag==2){
				change = field.replaceAll(value, key);
			}
		}
		return change;
	}
	/**
	 * 判断是否存在查询字段
	 * @return hasQuery true表示有查询字段,false表示没有
	 */
	protected boolean hasQueryColum(List<DataGridColumn> columnList ) {
		boolean hasQuery = false;
		for (DataGridColumn col : columnList) {
			if(col.isQuery()){
				hasQuery =  true;
			}
		}
		return hasQuery;
	}

	/**
	 * 拼装操作地址
	 * 
	 * @param sb
	 */
	protected void getOptUrl(StringBuffer sb) {
		//注：操作列表会带入合计列中去，故加此判断
		appendLine(sb,"     if(!rec.id){return '';}");
		appendLine(sb,"         var href='';");
		List<DataGridUrl> list = urlList;
		StringBuffer operationcodes = new StringBuffer();
		try{
			List<TSOperation> operations  = (List<TSOperation>)super.pageContext.getRequest().getAttribute(Globals.NOAUTO_OPERATIONCODES);
			for(TSOperation o : operations){
				operationcodes.append(","+o.getOperationcode());
			}
		}catch (Exception e){
			log.info(e.getMessage());
		}
		String notAutoBtn = operationcodes.length()==0 ? "":operationcodes.toString();
		for (DataGridUrl dataGridUrl : list) {
			if(dataGridUrl.getId() !=null && notAutoBtn.contains(dataGridUrl.getId())){
				continue;
			}
			if(!dataGridUrl.isInGroup()){
				String url = dataGridUrl.getUrl();
				MessageFormat formatter = new MessageFormat("");
				if (dataGridUrl.getValue() != null) {
					String[] testvalue = dataGridUrl.getValue().split(",");
					List value = new ArrayList<Object>();
					for (String string : testvalue) {
						value.add("\"+rec." + string + " +\"");
					}
					url = formatter.format(url, value.toArray());
				}
				if (url != null && dataGridUrl.getValue() == null) {

					//url = formatUrl(url);
					url = formatUrlPlus(url);

				}
				String exp = dataGridUrl.getExp();// 判断显示表达式
				if (StringUtil.isNotEmpty(exp)) {
					String[] ShowbyFields = exp.split("&&");
					for (String ShowbyField : ShowbyFields) {
						int beginIndex = ShowbyField.indexOf("#");
						int endIndex = ShowbyField.lastIndexOf("#");
						String exptype = ShowbyField.substring(beginIndex + 1, endIndex);// 表达式类型
						String field = ShowbyField.substring(0, beginIndex);// 判断显示依据字段
						String[] values = ShowbyField.substring(endIndex + 1, ShowbyField.length()).split(",");// 传入字段值
						String value = "";
						for (int i = 0; i < values.length; i++) {
							value += "'" + "" + values[i] + "" + "'";
							if (i < values.length - 1) {
								value += ",";
							}
						}
						if ("eq".equals(exptype)) {
							appendLine(sb,"     if($.inArray(rec." + field + ",[" + value + "])>=0){");
						}
						if ("ne".equals(exptype)) {
							appendLine(sb,"     if($.inArray(rec." + field + ",[" + value + "])<0){");
						}
						if ("empty".equals(exptype) && value.equals("'true'")) {
							appendLine(sb,"     if(rec." + field + "==''){");
						}
						if ("empty".equals(exptype) && value.equals("'false'")) {
							appendLine(sb,"     if(rec." + field + "!=''){");
						}
					}
				}

				StringBuffer style = new StringBuffer();
				if (!StringUtil.isEmpty(dataGridUrl.getUrlStyle())) {
					style.append(" style=\'");
					style.append(dataGridUrl.getUrlStyle());
					style.append("\' ");
				}

				StringBuffer urlclass = new StringBuffer();
				if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){
					urlclass.append(" class=\'");
					urlclass.append(dataGridUrl.getUrlclass());
					urlclass.append("\'");
				}
				StringBuffer urlfont = new StringBuffer();
				if(!StringUtil.isEmpty(dataGridUrl.getUrlfont())){
					urlfont.append(" <i class=\' fa ");
					urlfont.append(dataGridUrl.getUrlfont());
					urlfont.append("\'></i>");			
				}

				if (OptTypeDirection.Confirm.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){
						appendLine(sb,"         href+=\"<a href=\'#\'  "+urlclass.toString()+"  onclick=confirm(\'" + url + "\',\'" + dataGridUrl.getMessage() + "\',\'"+name+"\')" + style.toString() + "> "+urlfont.toString()+" \";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\' onclick=confirm(\'" + url + "\',\'" + dataGridUrl.getMessage() + "\',\'"+name+"\')" + style.toString() + "> \";");
					}

				}
				if (OptTypeDirection.Del.equals(dataGridUrl.getType())) {
					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\'  "+urlclass.toString()+"  onclick=delObj(\'" + url + "\',\'"+name+"\')" + style.toString() + "> "+urlfont.toString()+" \";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\'  onclick=delObj(\'" + url + "\',\'"+name+"\')" + style.toString() + ">\";");
					}
					
				}
				if (OptTypeDirection.Fun.equals(dataGridUrl.getType())) {
					String name = TagUtil.getFunction(dataGridUrl.getFunname());
					String parmars = TagUtil.getFunParams(dataGridUrl.getFunname());
					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\'  "+urlclass.toString()+"  onclick=" + name + "(" + parmars + ")" + style.toString() + "> "+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\'   onclick=" + name + "(" + parmars + ")" + style.toString() + ">\";");
					}
					
				}
				if (OptTypeDirection.OpenWin.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\' "+urlclass.toString()+" onclick=openwindow('" + dataGridUrl.getTitle() + "','" + url + "','"+name+"'," + dataGridUrl.getWidth() + "," + dataGridUrl.getHeight() + ")" + style.toString() + ">"+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\' onclick=openwindow('" + dataGridUrl.getTitle() + "','" + url + "','"+name+"'," + dataGridUrl.getWidth() + "," + dataGridUrl.getHeight() + ")" + style.toString() + ">\";");
					}

				}															//update-end--Author:liuht  Date:20130228 for：弹出窗口设置参数不生效
				if (OptTypeDirection.Deff.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){
						appendLine(sb,"         href+=\"<a href=\'" + url + "' "+urlclass.toString()+" title=\'"+dataGridUrl.getTitle()+"\'" + style.toString() + ">"+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'" + url + "' title=\'"+dataGridUrl.getTitle()+"\'" + style.toString() + ">\";");
					}

				}

				if (OptTypeDirection.OpenTab.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\' "+urlclass.toString()+" onclick=addOneTab('" + dataGridUrl.getTitle() + "','" + url  + "') "+ style.toString() +">"+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\' onclick=addOneTab('" + dataGridUrl.getTitle() + "','" + url  + "') "+ style.toString() +">\";");
					}

				}
				if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接后面的"]";
					appendLine(sb,"         href+=\"" + dataGridUrl.getTitle() + "</a>&nbsp;\";");
				}else{
					appendLine(sb,"         href+=\"" + dataGridUrl.getTitle() + "</a>]\";");
				}
				

				if (StringUtil.isNotEmpty(exp)) {
					for (int i = 0; i < exp.split("&&").length; i++) {
						appendLine(sb,"}");
					}

				}
			}
		}

		if(hasGroup()){
			getGroupUrl(sb);
		}

		appendLine(sb,"return href;");
	}

	/**
	 * 分组按钮拼装
	 * 
	 * @param sb
	 */
	protected void getGroupUrl(StringBuffer sb) {
		//注：操作列表会带入合计列中去，故加此判断
		List<DataGridUrl> list = urlList;
		//<i class='fa fa-angle-double-right'></i>更多
		appendLine(sb,"         href+=\"<a href='javascript:void(0)' class='opts-menu-triangle btnmy'> <span class='opts-menu-temp icon-triangle'></span>更多</a>\";");
	/*	appendLine(sb,"href+=\"<div class='opts-menu-triangle icon-triangle' title='更多操作'></div>\";");*/
		appendLine(sb,"         href+=\"<div class='opts-menu-container location-left'>\";");
		appendLine(sb,"         href+=\"<div class='opts-menu-parent'>\";");
		appendLine(sb,"         href+=\"<div class='opts-menu-box'>\";");
		for (DataGridUrl dataGridUrl : list) {
			if(dataGridUrl.isInGroup()){
				String url = dataGridUrl.getUrl();
				MessageFormat formatter = new MessageFormat("");
				if (dataGridUrl.getValue() != null) {
					String[] testvalue = dataGridUrl.getValue().split(",");
					List value = new ArrayList<Object>();
					for (String string : testvalue) {
						value.add("\"+rec." + string + " +\"");
					}
					url = formatter.format(url, value.toArray());
				}
				if (url != null && dataGridUrl.getValue() == null) {

					//url = formatUrl(url);
					url = formatUrlPlus(url);

				}
				String exp = dataGridUrl.getExp();// 判断显示表达式
				if (StringUtil.isNotEmpty(exp)) {
					String[] ShowbyFields = exp.split("&&");
					for (String ShowbyField : ShowbyFields) {
						int beginIndex = ShowbyField.indexOf("#");
						int endIndex = ShowbyField.lastIndexOf("#");
						String exptype = ShowbyField.substring(beginIndex + 1, endIndex);// 表达式类型
						String field = ShowbyField.substring(0, beginIndex);// 判断显示依据字段
						String[] values = ShowbyField.substring(endIndex + 1, ShowbyField.length()).split(",");// 传入字段值
						String value = "";
						for (int i = 0; i < values.length; i++) {
							value += "'" + "" + values[i] + "" + "'";
							if (i < values.length - 1) {
								value += ",";
							}
						}
						if ("eq".equals(exptype)) {
							appendLine(sb,"     if($.inArray(rec." + field + ",[" + value + "])>=0){");
						}
						if ("ne".equals(exptype)) {
							appendLine(sb,"     if($.inArray(rec." + field + ",[" + value + "])<0){");
						}
						if ("empty".equals(exptype) && value.equals("'true'")) {
							appendLine(sb,"     if(rec." + field + "==''){");
						}
						if ("empty".equals(exptype) && value.equals("'false'")) {
							appendLine(sb,"     if(rec." + field + "!=''){");
						}
					}
				}

				StringBuffer style = new StringBuffer();
				if (!StringUtil.isEmpty(dataGridUrl.getUrlStyle())) {
					style.append(" style=\'");
					style.append(dataGridUrl.getUrlStyle());
					style.append("\' ");
				}

				StringBuffer urlclass = new StringBuffer();

				if(StringUtil.isEmpty(dataGridUrl.getUrlclass())){
					dataGridUrl.setUrlclass("btn btn-default ops-more");
				}
				urlclass.append(" class=\'"+dataGridUrl.getUrlclass()+"\'");

				StringBuffer urlfont = new StringBuffer();
				if(!StringUtil.isEmpty(dataGridUrl.getUrlfont())){
					urlfont.append(" <i class=\' fa ");
					urlfont.append(dataGridUrl.getUrlfont());
					urlfont.append("\'></i>");			
				}

				if (OptTypeDirection.Confirm.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){
						appendLine(sb,"         href+=\"<a href=\'#\'  "+urlclass.toString()+"  onclick=confirm(\'" + url + "\',\'" + dataGridUrl.getMessage() + "\',\'"+name+"\')" + style.toString() + "> "+urlfont.toString()+" \";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\' onclick=confirm(\'" + url + "\',\'" + dataGridUrl.getMessage() + "\',\'"+name+"\')" + style.toString() + "> \";");
					}

				}
				if (OptTypeDirection.Del.equals(dataGridUrl.getType())) {
					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\'  "+urlclass.toString()+"  onclick=delObj(\'" + url + "\',\'"+name+"\')" + style.toString() + "> "+urlfont.toString()+" \";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\'  onclick=delObj(\'" + url + "\',\'"+name+"\')" + style.toString() + ">\";");
					}
					
				}
				if (OptTypeDirection.Fun.equals(dataGridUrl.getType())) {
					String name = TagUtil.getFunction(dataGridUrl.getFunname());
					String parmars = TagUtil.getFunParams(dataGridUrl.getFunname());
					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\'  "+urlclass.toString()+"  onclick=" + name + "(" + parmars + ")" + style.toString() + "> "+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\'   onclick=" + name + "(" + parmars + ")" + style.toString() + ">\";");
					}
					
				}
				if (OptTypeDirection.OpenWin.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\' "+urlclass.toString()+" onclick=openwindow('" + dataGridUrl.getTitle() + "','" + url + "','"+name+"'," + dataGridUrl.getWidth() + "," + dataGridUrl.getHeight() + ")" + style.toString() + ">"+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\' onclick=openwindow('" + dataGridUrl.getTitle() + "','" + url + "','"+name+"'," + dataGridUrl.getWidth() + "," + dataGridUrl.getHeight() + ")" + style.toString() + ">\";");
					}

				}															//update-end--Author:liuht  Date:20130228 for：弹出窗口设置参数不生效
				if (OptTypeDirection.Deff.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){
						appendLine(sb,"         href+=\"<a href=\'" + url + "' "+urlclass.toString()+" title=\'"+dataGridUrl.getTitle()+"\'" + style.toString() + ">"+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'" + url + "' title=\'"+dataGridUrl.getTitle()+"\'" + style.toString() + ">\";");
					}

				}

				if (OptTypeDirection.OpenTab.equals(dataGridUrl.getType())) {

					if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接前面的"[";
						appendLine(sb,"         href+=\"<a href=\'#\' "+urlclass.toString()+" onclick=addOneTab('" + dataGridUrl.getTitle() + "','" + url  + "') "+ style.toString() +">"+urlfont.toString()+"\";");
					}else{
						appendLine(sb,"         href+=\"[<a href=\'#\' onclick=addOneTab('" + dataGridUrl.getTitle() + "','" + url  + "') "+ style.toString() +">\";");
					}

				}
				if(!StringUtil.isEmpty(dataGridUrl.getUrlclass())){//倘若urlclass不为空，则去掉链接后面的"]";
					appendLine(sb,"         href+=\"" + dataGridUrl.getTitle() + "</a>&nbsp;\";");
				}else{
					appendLine(sb,"         href+=\"" + dataGridUrl.getTitle() + "</a>]\";");
				}
				if (StringUtil.isNotEmpty(exp)) {
					for (int i = 0; i < exp.split("&&").length; i++) {
						appendLine(sb,"}");
					}

				}
			}
		}
		appendLine(sb,"         href+=\"</div></div><em class='ops_shadeEm' style='display: inline;'></em></div></div>\";");
	}

	
	/**
	 * 列自定义函数
	 * 
	 * @param sb
	 * @param column
	 */
	protected void getFun(StringBuffer sb, DataGridColumn column) {
		String url = column.getUrl();
		url = formatUrlPlus(url);
		appendLine(sb,"     var href=\"<a style=\'color:red\' href=\'#\' onclick=" + column.getFunname() + "('" + column.getTitle() + "','" + url + "')>\";");
		appendLine(sb,"     return href+value+\'</a>\';");

	}

	/**
	 * 格式化URL
	 * 
	 * @return
	 */
	protected String formatUrl(String url) {
		MessageFormat formatter = new MessageFormat("");
		String parurlvalue = "";
		if (url.indexOf("&") >= 0) {
			String beforeurl = url.substring(0, url.indexOf("&"));// 截取请求地址
			String parurl = url.substring(url.indexOf("&") + 1, url.length());// 截取参数
			String[] pras = parurl.split("&");
			List value = new ArrayList<Object>();
			int j = 0;
			for (int i = 0; i < pras.length; i++) {
				if (pras[i].indexOf("{") >= 0 || pras[i].indexOf("#") >= 0) {
					String field = pras[i].substring(pras[i].indexOf("{") + 1, pras[i].lastIndexOf("}"));
					parurlvalue += "&" + pras[i].replace("{" + field + "}", "{" + j + "}");
					value.add("\"+rec." + field + " +\"");
					j++;
				} else {
					parurlvalue += "&" + pras[i];
				}
			}
			url = formatter.format(beforeurl + parurlvalue, value.toArray());
		}
		return url;

	}

	/**
	 * formatUrl增强写法
	 * 支持#{}、{}、##三种传参方式
	 * @param url
	 * @return
	 */
	protected String formatUrlPlus(String url) {
		boolean isTrans = false;
		//tsBlackListController.do?doDel&id=#{id}
		if(url.indexOf("#{") >= 0){
			isTrans = true;
			url = url.replace("#{", "\"+rec.");
			url = url.replace("}", "+\"");
		}
		//tsBlackListController.do?doDel&id={id}
		if(url.indexOf("{") >= 0 && !isTrans){
			url = url.replace("{", "\"+rec.");
			url = url.replace("}", "+\"");
		}
		//tsBlackListController.do?doDel&id=#id#
		if(url.indexOf("#") > 0 && !isTrans){
			Pattern p = Pattern.compile("#", Pattern.CASE_INSENSITIVE);
			StringBuffer sb = new StringBuffer();
			Matcher m = p.matcher(url);
			int i = 0;
			while(m.find()) {
				i++;
				if(i %2 == 0){
					m.appendReplacement(sb, "+\"");
				} else {
					m.appendReplacement(sb, "\"+rec.");
				}
			}
			m.appendTail(sb);
			url = sb.toString();
		}
		return url;
	}

	/**
	 * 拼接字段  普通列
	 * 
	 * @param sb
	 * 
	 */
	 protected void getField(StringBuffer sb){
		 getField(  sb,1);
	 }
	/**
	 * 拼接字段
	 * 
	 * @param sb
	 * @frozen 0 冰冻列    1 普通列
	 */
	protected void getField(StringBuffer sb,int frozen) {
		// 复选框
		if (checkbox&&frozen==0) {
			appendLine(sb,"{field:\'ck\',checkbox:\'true\'},");
		}
		int i = 0;
		for (DataGridColumn column : columnList) {
			i++;
			if((column.isFrozenColumn()&&frozen==0)||(!column.isFrozenColumn()&&frozen==1)){
			String field;
			if (treegrid) {

				field = column.getTreefield();
				if(StringUtils.isEmpty(field)){
					field = column.getField();
				}

			} else {
				field = column.getField();
			}

			appendLine(sb,"{");
			if(StringUtil.isNotEmpty(field)){
				appendLine(sb,"     field:\'" + field + "\',");
			}
			appendLine(sb,"     title:\'" + column.getTitle() + "\'");

			if(column.getWidth() != null){
				appendLine(sb,"     ,width:"+column.getWidth());
			}
			if (column.getAlign()!=null){
				appendLine(sb,"     ,align:\'" + column.getAlign() + "\'");
			}
			if(StringUtil.isNotEmpty(column.getRowspan())){
				appendLine(sb,"     ,rowspan:" + column.getRowspan());
			}
			//添加继承的参数？
			if(StringUtils.isNotEmpty(column.getExtendParams())){

				StringBuffer comboboxStr =new StringBuffer();//声明一个替换扩展参数中的editor:'combobox'的变量
				if(column.getExtendParams().indexOf("editor:'combobox'")>-1){//倘若扩展参数中包含editor:'combobox'
					if(!StringUtil.isEmpty(column.getDictionary())){//根据数据字典生成editor:'combobox'
						if(column.getDictionary().contains(",")){

							if(column.isPopup()){
								//TODO popup值转换处理，此处暂不处理
							}else{
								if(column.getIsAjaxDict()){
									//TODO ajax值转换处理，此处暂不处理

								}else{
									String[] dic = column.getDictionary().split(",");
									String sql = "select " + dic[1] + " as field," + dic[2]+ " as text from " + dic[0];

									if(!StringUtil.isEmpty(column.getDictCondition())){
										sql += " "+column.getDictCondition();
									}

									systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
									List<Map<String, Object>> list = systemService.findForJdbc(sql);

                                    appendLine(comboboxStr,"        editor:{type:'combobox',options:{valueField:'typecode',textField:'typename',data:[");

									for (Map<String, Object> map : list){
                                        appendLine(comboboxStr,"            {'typecode':'"+map.get("field")+"','typename':'"+map.get("text")+"'},");
									}
                                    appendLine(comboboxStr,"        ],required:true}}");
									//再增加formatter参数
									
									comboboxStr.append("    ,formatter:function(value,row){");
									for (Map<String, Object> map : list){
										appendLine(comboboxStr,"        if(value =='"+map.get("field")+"'){");
                                        appendLine(comboboxStr,"            return '"+map.get("text")+"';");
                                        appendLine(comboboxStr,"        }");
									}
                                    appendLine(comboboxStr,"    return value;");

									appendLine(comboboxStr,"}");
								}
							}

						}else{
							List<TSType> types = ResourceUtil.getCacheTypes(column.getDictionary().toLowerCase());
							if (types != null) {
								comboboxStr.append("    editor:{type:'combobox',options:{valueField:'typecode',textField:'typename',data:[");
								for (TSType type : types) {
									comboboxStr.append("        {'typecode':'"+type.getTypecode()+"','typename':'"+MutiLangUtil.getLang(type.getTypename())+"'},");
								}
								comboboxStr.append("    ],required:true}}");
								//再增加formatter参数
								comboboxStr.append("    ,formatter:function(value,row){");
								for (TSType type : types) {
									comboboxStr.append("        if(value =='"+type.getTypecode()+"'){");
									comboboxStr.append("            return '"+MutiLangUtil.getLang(type.getTypename())+"';");
									comboboxStr.append("        }");
								}
								comboboxStr.append("    return row."+field+";");
								comboboxStr.append("}");
							}
						}
				   }

					if(StringUtils.isEmpty(comboboxStr.toString())){
						comboboxStr.append("editor:'text'");
					}

					column.setExtendParams(column.getExtendParams().replaceAll("editor:'combobox'", comboboxStr.toString()));//替换扩展参数
					//System.out.println("column.getExtendParams()=="+column.getExtendParams());
				}

				//appendLine(sb,","+column.getExtendParams().substring(0,column.getExtendParams().length()-1));

				String extendParm = "";
				if(column.getExtendParams().endsWith(",") || column.getExtendParams().endsWith("''")){
					extendParm = column.getExtendParams().substring(0,column.getExtendParams().length()-1);
				}else{
					extendParm = column.getExtendParams();
				}
				extendParm = extendParm.trim();
				if(StringUtils.isNotEmpty(extendParm)){
					appendLine(sb,","+extendParm);
				}

				
			}

			// 隐藏字段
			if (column.isHidden()) {
				appendLine(sb,"		,hidden:true");
			}

			if (!treegrid) {
				// 字段排序

				if ((column.isSortable()) && StringUtil.isNotEmpty(field) && (field.indexOf("_") <= 0 && field != "opt")) {

					appendLine(sb,"     ,sortable:" + column.isSortable() + "");
				}
			}
			if(column.getFormatterjs()!=null){

				if(StringUtils.isNotEmpty(column.getExtendParams())&&column.getExtendParams().indexOf("editor:'combobox'")>-1){//倘若扩展参数中包含editor:'combobox'
						//不再重复增加formatter参数，
				}else{
					appendLine(sb,"     ,formatter:function(value,rec,index){");
					appendLine(sb,"         return "+column.getFormatterjs()+"(value,rec,index);}");
				}

			}else {
				// 显示图片
				if (column.isImage()) {
					if (column.getImageSize() != null) {// 自定义显示图片
	                    String[] tld = column.getImageSize().split(",");
	                    appendLine(sb,"     ,formatter:function(value,rec,index){");
	                    appendLine(sb,"         return '<img width=\"" + tld[0]
	                                    + "\" height=\"" + tld[1]
	                                    + "\" border=\"0\" "
	                                    + " onMouseOver=\"tipImg(this)\" onMouseOut=\"moveTipImg()\" "
//	                                    + " onMouseOver=\"toolTip('<img src='+value+'>')\" onMouseOut=\"toolTip()\" "
	                                    + " src=\"'+value+'\"/>';}");
	                    tld = null;
					}else{
						appendLine(sb,"     ,formatter:function(value,rec,index){");
						appendLine(sb,"         return '<img border=\"0\" src=\"'+value+'\"/>';}");
					}
				} else if(column.getDownloadName() != null){
	            	appendLine(sb,"     ,formatter:function(value,rec,index){");

	            	appendLine(sb,"         var html = '';");

	            	appendLine(sb,"         if(value==null || value.length==0){return html;}");

	            	appendLine(sb,"         if(value.indexOf('.jpg')>-1 || value.indexOf('.png')>-1 || value.indexOf('.jpeg')>-1 || value.indexOf('.gif') > -1){");
	            	appendLine(sb,"             html = '<img onMouseOver=\"tipImg(this)\" onMouseOut=\"moveTipImg()\" src=\"'+value+'\" width=50 height=50/>';");
	            	appendLine(sb,"         }else{");

	                appendLine(sb,"             html = '<a class=\"ace_button fa fa-download\" style=\"padding:3px 5px;\" target=\"_blank\" href=\"systemController/downloadFile.do?filePath='+value+'\">"

	                		+ column.getDownloadName() + "</a>';}");
	                appendLine(sb,"         return html;}");

	            }else if (column.getUrl() != null) { // 自定义链接
					appendLine(sb,"     ,formatter:function(value,rec,index){");
					this.getFun(sb, column);
					appendLine(sb,"}");

				}else if (StringUtil.isNotEmpty(column.getField()) && column.getField().equals("opt")) {// 加入操作
					// TODO 操作栏代码
					appendLine(sb,"     ,formatter:function(value,rec,index){");

					if(column.isOptsMenu()){
						getOptsMenuUrl(sb);
					}else{
						this.getOptUrl(sb);
					}
					appendLine(sb,"     }");

				}else if(column.getFormatter()!=null)
				{
					appendLine(sb,",formatter:function(value,rec,index){");
					appendLine(sb," return new Date().format('"+column.getFormatter()+"',value);}");
				}

				else if(column.getShowLen()!=null){ //设置了显示多少长度的
					appendLine(sb,",formatter:function(value,rec,index){");
					appendLine(sb," if(value==undefined) {return ''} ");
					appendLine(sb," if(value.length<=");
					appendLine(sb,column.getShowLen()+"");
					appendLine(sb,") {return value}");
					appendLine(sb," else{ return '<a title= '+value+'>'+ value.substring(0,");
					appendLine(sb,column.getShowLen()+"");
					appendLine(sb,")+'...';}}");
				}

				else if (columnValueList.size() > 0 && StringUtil.isNotEmpty(column.getField()) && !column.getField().equals("opt")) {// 值替換

					if(column.getDictionary()!=null&&column.getDictionary().contains(",")&&column.getIsAjaxDict()){
						appendLine(sb,",formatter:function(value,rec,index){");
						appendLine(sb,"var rtn = \"<span name=\\\"ajaxDict\\\" dictionary=\\\""+column.getDictionary()
								+"\\\" dictCondition=\\\""+(column.getDictCondition()==null?"":column.getDictCondition())
								+"\\\" popup=\\\""+column.isPopup()
								+"\\\" value=\\\"\"+value+\"\\\"><img src='plug-in/easyui/themes/icons/loading.gif'/></span>\";");
						appendLine(sb,"return rtn;");
						appendLine(sb,"}");
					}else{
						String testString = "";
						for (ColumnValue columnValue : columnValueList) {
							if (columnValue.getName().equals(column.getField())) {
								String[] value = columnValue.getValue().split(",");
								String[] text = columnValue.getText().split(",");
								appendLine(sb,",formatter:function(value,rec,index){");
								appendLine(sb,"if(value==undefined) return '';");
								appendLine(sb,"var valArray = value.split(',');");
								appendLine(sb,"if(valArray.length > 1){");
								appendLine(sb,"var checkboxValue = '';");
								appendLine(sb,"for(var k=0; k<valArray.length; k++){");
								for(int j = 0; j < value.length; j++){
									appendLine(sb,"if(valArray[k] == '" + value[j] + "'){ checkboxValue = checkboxValue + \'" + text[j] + "\' + ',';}");
								}
								appendLine(sb,"}");
								appendLine(sb,"return checkboxValue.substring(0,checkboxValue.length-1);");
								appendLine(sb,"}");
								appendLine(sb,"else{");
								for (int j = 0; j < value.length; j++) {
									testString += "if(value=='" + value[j] + "'){return \'" + text[j] + "\';}";
								}
								appendLine(sb,testString);
								appendLine(sb,"else{return value;}");
								appendLine(sb,"}");
								appendLine(sb,"}");
							}
						}

					}
				}
			}
			// 背景设置

			if (columnStyleList.size() > 0  && StringUtil.isNotEmpty(column.getField()) && !column.getField().equals("opt")) {

				String testString = "";
				for (ColumnValue columnValue : columnStyleList) {
					if (columnValue.getName().equals(column.getField())) {
						String[] value = columnValue.getValue().split(",");
						String[] text = columnValue.getText().split(",");
						appendLine(sb,",styler:function(value,rec,index){");
						if((value.length == 0||StringUtils.isEmpty(value[0]))&&text.length==1){
							if(text[0].indexOf("(")>-1){
								testString = " return \'" + text[0].replace("(", "(value,rec,index") + "\'";
							}else{
								testString = " return \'" + text[0] + "\'";
							}
						}else{
							for (int j = 0; j < value.length; j++) {
								testString += "if(value=='" + value[j] + "'){return \'" + text[j] + "\'}";
							}
						}
						appendLine(sb,testString);
						appendLine(sb,"}");
					}
				}
				
			}

			if(StringUtil.isNotEmpty(column.getColspan())){
				appendLine(sb,",colspan:\"" + column.getColspan() + "\"");
			}
			
			appendLine(sb,"}");
			if(i < columnList.size() && column.isNewColumn()){
				appendLine(sb,"],[");
				continue;
			}//update--begin--author:zhangjiaqiang Date:20170815 for:TASK #2273 【demo】datagrid 多表头demo
			
			// 去除末尾,
			if (i < columnList.size()) {
				appendLine(sb,",");
			}
		}
		}
	}
	/**
	 * 设置分页条信息
	 * 
	 * @param sb
	 */
	protected void setPager(StringBuffer sb, String grid) {
		appendLine(sb,"$(\'#" + name + "\')." + grid + "(\'getPager\').pagination({");
		appendLine(sb,"beforePageText:\'\'," + "afterPageText:\'/{pages}\',");
		if (showText) {
			appendLine(sb,"displayMsg:\'{from}-{to}" + MutiLangUtil.getLang("common.total") + " {total}" + MutiLangUtil.getLang("common.item") + "\',");
		} else {
			appendLine(sb,"displayMsg:\'\',");
		}
		if (showPageList == true) {
			appendLine(sb,"showPageList:true,");
		} else {
			appendLine(sb,"showPageList:false,");
		}
		appendLine(sb,"showRefresh:" + showRefresh + "");
		appendLine(sb,"});");// end getPager
		appendLine(sb,"$(\'#" + name + "\')." + grid + "(\'getPager\').pagination({");
		appendLine(sb,"onBeforeRefresh:function(pageNumber, pageSize){ $(this).pagination(\'loading\');$(this).pagination(\'loaded\'); }");
		appendLine(sb,"});");
	}
	//列表查询框函数
	protected void searchboxFun(StringBuffer sb,String grid)
	{
		appendLine(sb,"function "+name+"searchbox(value,name){");
		appendLine(sb,"var queryParams=$(\'#" + name + "\').datagrid('options').queryParams;");
		appendLine(sb,"queryParams[name]=value;queryParams.searchfield=name;$(\'#" + name + "\')." + grid + "(\'reload\');}");
		appendLine(sb,"$(\'#"+name+"searchbox\').searchbox({");
		appendLine(sb,"searcher:function(value,name){");
		appendLine(sb,""+name+"searchbox(value,name);");
		appendLine(sb,"},");
		appendLine(sb,"menu:\'#"+name+"mm\',");
		appendLine(sb,StringUtil.replaceAll("prompt:\'{0}\'", "{0}", MutiLangUtil.getLang("common.please.input.query.keyword")));
		appendLine(sb,"});");
	}
  
	public String getNoAuthOperButton(){
		StringBuffer sb = new StringBuffer();
		if(ResourceUtil.getSessionUser().getUserName().equals("admin")|| !Globals.BUTTON_AUTHORITY_CHECK){
		}else{
			Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute(Globals.OPERATIONCODES);
			if (null!=operationCodes) {
				for (String MyoperationCode : operationCodes) {
					if (oConvertUtils.isEmpty(MyoperationCode))
						break;
					systemService = ApplicationContextUtil.getContext().getBean(
								SystemService.class);
					TSOperation operation = systemService.getEntity(TSOperation.class, MyoperationCode);
					if (operation.getOperationcode().startsWith(".") || operation.getOperationcode().startsWith("#")){
						if (operation.getOperationType().intValue()==Globals.OPERATION_TYPE_HIDE){
							//out.append("$(\""+name+"\").find(\"#"+operation.getOperationcode().replaceAll(" ", "")+"\").hide();");
							appendLine(sb,"$(\""+operation.getOperationcode().replaceAll(" ", "")+"\").hide();");
						}else {
							//out.append("$(\""+name+"\").find(\"#"+operation.getOperationcode().replaceAll(" ", "")+"\").find(\":input\").attr(\"disabled\",\"disabled\");");
							appendLine(sb,"$(\""+operation.getOperationcode().replaceAll(" ", "")+"\").attr(\"disabled\",\"disabled\");");
							appendLine(sb,"$(\""+operation.getOperationcode().replaceAll(" ", "")+"\").find(\":input\").attr(\"disabled\",\"disabled\");");
						}
					}
				}
			}
			
		}
		//org.jeecgframework.core.util.LogUtil.info("----getNoAuthOperButton-------"+sb.toString());
		return sb.toString(); 
	}
	
	/**
	 * 描述：组装菜单按钮操作权限
	 * dateGridUrl：url
	 * operationCode：操作码
	 * optList： 操作列表
	 * @version 1.0
	 */
	private void installOperationCode(DataGridUrl dataGridUrl,String operationCode,List optList){
		if(ResourceUtil.getSessionUser().getUserName().equals("admin")|| !Globals.BUTTON_AUTHORITY_CHECK){
			optList.add(dataGridUrl);
		}else if(!oConvertUtils.isEmpty(operationCode)){
			Set<String> operationCodes = (Set<String>) super.pageContext.getRequest().getAttribute(Globals.OPERATIONCODES);
			if (null!=operationCodes) {
				List<String> operationCodesStr = new ArrayList<String>();
				for (String MyoperationCode : operationCodes) {
					if (oConvertUtils.isEmpty(MyoperationCode))
						break;
					systemService = ApplicationContextUtil.getContext().getBean(
								SystemService.class);
					TSOperation operation = systemService.getEntity(TSOperation.class, MyoperationCode);
					operationCodesStr.add(operation.getOperationcode());
				}
				if (!operationCodesStr.contains(operationCode)){
					optList.add(dataGridUrl);
				}
			}
		}else {
			optList.add(dataGridUrl);
		}
	}
	
	/**
	 * 获取自动补全的panel
	 * @param filed
	 * @author JueYue
	 * @return
	 */
	private String getAutoSpan(String filed,String extend){
		String id = filed.replaceAll("\\.","_");
		StringBuffer nsb = new StringBuffer();
		appendLine(nsb,"<script type=\"text/javascript\">");
		appendLine(nsb,"$(document).ready(function() {");
		appendLine(nsb,"$(\"#"+getEntityName()+"_"+id+"\").autocomplete(\"commonController.do?getAutoList\",{");
		appendLine(nsb,"max: 5,minChars: 2,width: 200,scrollHeight: 100,matchContains: true,autoFill: false,extraParams:{");
		appendLine(nsb,"featureClass : \"P\",style : \"full\",	maxRows : 10,labelField : \""+filed+"\",valueField : \""+filed+"\",");
		appendLine(nsb,"searchField : \""+filed+"\",entityName : \""+getEntityName()+"\",trem: function(){return $(\"#"+getEntityName()+"_"+id+"\").val();}}");
		appendLine(nsb,",parse:function(data){return jeecgAutoParse.call(this,data);}");
		appendLine(nsb,",formatItem:function(row, i, max){return row['"+filed+"'];} ");
		appendLine(nsb,"}).result(function (event, row, formatted) {");
		appendLine(nsb,"$(\"#"+getEntityName()+"_"+id+"\").val(row['"+filed+"']);}); });");
		appendLine(nsb,"</script>");
		appendLine(nsb,"<input class=\"inuptxt\" type=\"text\" id=\""+getEntityName()+"_"+id+"\" name=\""+filed+"\"  "+extend+ StringUtil.replace(" nullmsg=\"\" errormsg=\"{0}\"/>", "{0}", MutiLangUtil.getLang("input.error")));
		return nsb.toString();
	}
	/**
	 * 获取实体类名称,没有这根据规则设置
	 * @return
	 */
	private String getEntityName() {
		if(StringUtils.isEmpty(entityName)){
			entityName = actionUrl.substring(0,actionUrl.indexOf("Controller"));
			entityName = (entityName.charAt(0)+"").toUpperCase()+entityName.substring(1)+"Entity";
		}
		return entityName;
	}
	
	public boolean isFitColumns() {
		return fitColumns;
	}

	public void setFitColumns(boolean fitColumns) {
		this.fitColumns = fitColumns;
	}

	public boolean isCollapsible() {
		return collapsible;
	}
	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}
	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public String getQueryMode() {
		return queryMode;
	}

	public void setQueryMode(String queryMode) {
		this.queryMode = queryMode;
	}

	public boolean isAutoLoadData() {
		return autoLoadData;
	}

	public void setAutoLoadData(boolean autoLoadData) {
		this.autoLoadData = autoLoadData;
	}

	public void setOpenFirstNode(boolean openFirstNode) {
		this.openFirstNode = openFirstNode;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setRowStyler(String rowStyler) {
		this.rowStyler = rowStyler;
	}

	public void setExtendParams(String extendParams) {

		if(StringUtil.isNotEmpty(extendParams) && !extendParams.endsWith(",")){
			extendParams = extendParams + ",";
		}

		this.extendParams = extendParams;
	}

	public void setLangArg(String langArg) {
		this.langArg = langArg;
	}

	public StringBuffer aceStyleTable() {
		String grid = "";
		StringBuffer sb = new StringBuffer();		

		if(btnCls!=null && btnCls.indexOf("bootstrap")==0){
			appendLine(sb,"<link rel=\"stylesheet\" href=\"plug-in/bootstrap/css/bootstrap-btn.css\" type=\"text/css\"></link>");    
		}

		width = (width == null) ? "auto" : width;
		height = (height == null) ? "auto" : height;
//		appendLine(sb,"<link rel=\"stylesheet\" href=\"plug-in/easyui/themes/metro/main.css\" />");
		appendLine(sb,"<script type=\"text/javascript\">");
		appendLine(sb,"$(function(){  storage=$.localStorage;if(!storage)storage=$.cookieStorage;");
		appendLine(sb,this.getNoAuthOperButton());
		if (treegrid) {
			grid = "treegrid";
			appendLine(sb,"$(\'#" + name + "\').treegrid({");
			appendLine(sb,"idField:'id',");
			appendLine(sb,"treeField:'text',");
		} else {
			grid = "datagrid";
			appendLine(sb,"$(\'#" + name + "\').datagrid({");
			if (this.isFilter()) {
				appendLine(sb,"onHeaderContextMenu: function(e, field){headerMenu(e, field);},");
			}
			appendLine(sb,"idField: '" + idField + "',");
		}
		if (title != null) {
			appendLine(sb,"title: \'" + title + "\',");
		}
		
		if(autoLoadData)
		   appendLine(sb,"url:\'" + actionUrl + "&field=" + fields + "\',");
		else
			appendLine(sb,"url:\'',");
		if(StringUtils.isNotEmpty(rowStyler)){
			appendLine(sb,"rowStyler: function(index,row){ return "+rowStyler+"(index,row);},");
		}
		if(StringUtils.isNotEmpty(extendParams)){
			appendLine(sb,extendParams);
		}
		if (fit) {
			appendLine(sb,"fit:true,");
		} else {
			appendLine(sb,"fit:false,");
		}
		appendLine(sb,StringUtil.replaceAll("loadMsg: \'{0}\',", "{0}", MutiLangUtil.getLang("common.data.loading")));
		appendLine(sb,"striped:true,pageSize: " + pageSize + ",");
		appendLine(sb,"pagination:" + pagination + ",");
		appendLine(sb,"pageList:[" + pageSize * 1 + "," + pageSize * 2 + "," + pageSize * 3 + "],");
		if(StringUtils.isNotBlank(sortName)){
			appendLine(sb,"sortName:'" +sortName +"',");
		}
		appendLine(sb,"sortOrder:'" + sortOrder + "',");
		appendLine(sb,"rownumbers:true,");
		if(singleSelect==null){
			appendLine(sb,"singleSelect:" + !checkbox + ",");
		}else{
			appendLine(sb,"singleSelect:" + singleSelect + ",");
		}
		if (fitColumns) {
			appendLine(sb,"fitColumns:true,");
		} else {
			appendLine(sb,"fitColumns:false,");
		}
		appendLine(sb,"showFooter:true,");
		appendLine(sb,"frozenColumns:[[");
		this.getField(sb,0);
		appendLine(sb,"]],");
		
		appendLine(sb,"columns:[[");
		this.getField(sb);
		appendLine(sb,"]],");
		appendLine(sb,"onLoadSuccess:function(data){$(\"#"+name+"\")."+grid+"(\"clearSelections\");");

		//appendLine(sb," $(this).datagrid(\"fixRownumber\");");

		if(openFirstNode&&treegrid){
			appendLine(sb," if(data==null){");
			appendLine(sb," var firstNode = $(\'#" + name + "\').treegrid('getRoots')[0];");
			appendLine(sb," $(\'#" + name + "\').treegrid('expand',firstNode.id)}");
		}
		if (StringUtil.isNotEmpty(onLoadSuccess)) {
			appendLine(sb,onLoadSuccess + "(data);");
		}
		appendLine(sb,"},");
		if (StringUtil.isNotEmpty(onDblClick)) {
			appendLine(sb,"onDblClickRow:function(rowIndex,rowData){" + onDblClick + "(rowIndex,rowData);},");
		}
		if (treegrid) {
			appendLine(sb,"onClickRow:function(rowData){");
		}
		else {
			appendLine(sb,"onClickRow:function(rowIndex,rowData){");
		}
		/**行记录赋值*/
		appendLine(sb,"rowid=rowData.id;");
		appendLine(sb,"gridname=\'"+name+"\';");
		if (StringUtil.isNotEmpty(onClick)) {
			if (treegrid) {
				appendLine(sb,"" + onClick + "(rowData);");
			}else{
				appendLine(sb,"" + onClick + "(rowIndex,rowData);");
			}
		}
		appendLine(sb,"}");
		appendLine(sb,"});");
		this.setPager(sb, grid);
		appendLine(sb,"try{restoreheader();}catch(ex){}");
		appendLine(sb,"});");
		appendLine(sb,"function reloadTable(){");
		appendLine(sb,"try{");
		appendLine(sb,"	$(\'#\'+gridname).datagrid(\'reload\');" );
		appendLine(sb,"	$(\'#\'+gridname).treegrid(\'reload\');" );
		appendLine(sb,"}catch(ex){}");
		appendLine(sb,"}");
		appendLine(sb,"function reload" + name + "(){" + "$(\'#" + name + "\')." + grid + "(\'reload\');" + "}");
		appendLine(sb,"function get" + name + "Selected(field){return getSelected(field);}");
		appendLine(sb,"function getSelected(field){" + "var row = $(\'#\'+gridname)." + grid + "(\'getSelected\');" + "if(row!=null)" + "{" + "value= row[field];" + "}" + "else" + "{" + "value=\'\';" + "}" + "return value;" + "}");
		appendLine(sb,"function get" + name + "Selections(field){" + "var ids = [];" + "var rows = $(\'#" + name + "\')." + grid + "(\'getSelections\');" + "for(var i=0;i<rows.length;i++){" + "ids.push(rows[i][field]);" + "}" + "ids.join(\',\');" + "return ids" + "};");
		appendLine(sb,"function getSelectRows(){");
		appendLine(sb,"	return $(\'#"+name+"\').datagrid('getChecked');}");

		appendLine(sb," function saveHeader(){");
		appendLine(sb," var columnsFields =null;var easyextends=false;try{columnsFields = $('#"+name+"').datagrid('getColumns');easyextends=true;");
		appendLine(sb,"}catch(e){columnsFields =$('#"+name+"').datagrid('getColumnFields');}");
		appendLine(sb,"	var cols = storage.get( '"+name+"hiddenColumns');var init=true;	if(cols){init =false;} " +
				"var hiddencolumns = [];for(var i=0;i< columnsFields.length;i++) {if(easyextends){");
		appendLine(sb,"hiddencolumns.push({field:columnsFields[i].field,hidden:columnsFields[i].hidden});}else{");
		appendLine(sb, " var columsDetail = $('#"+name+"').datagrid(\"getColumnOption\", columnsFields[i]); ");
		appendLine(sb, "if(init){hiddencolumns.push({field:columsDetail.field,hidden:columsDetail.hidden,visible:(columsDetail.hidden==true?false:true)});}else{");
		appendLine(sb,"for(var j=0;j<cols.length;j++){");
		appendLine(sb,"		if(cols[j].field==columsDetail.field){");
		appendLine(sb,"					hiddencolumns.push({field:columsDetail.field,hidden:columsDetail.hidden,visible:cols[j].visible});");
		appendLine(sb,"		}");
		appendLine(sb,"}");
		appendLine(sb,"}} }");
		appendLine(sb,"storage.set( '"+name+"hiddenColumns',JSON.stringify(hiddencolumns));");
		appendLine(sb, "}");
		appendLine(sb, "function restoreheader(){");
		appendLine(sb,"var cols = storage.get( '"+name+"hiddenColumns');if(!cols)return;");
		appendLine(sb, "for(var i=0;i<cols.length;i++){");
		appendLine(sb, "	try{");
		appendLine(sb,"if(cols.visible!=false)$('#"+name+"').datagrid((cols[i].hidden==true?'hideColumn':'showColumn'),cols[i].field);");
		appendLine(sb, "}catch(e){");
		appendLine(sb, "}");
		appendLine(sb, "}");
		appendLine(sb, "}");
		appendLine(sb, "function resetheader(){");
		appendLine(sb,"var cols = storage.get( '"+name+"hiddenColumns');if(!cols)return;");
		appendLine(sb, "for(var i=0;i<cols.length;i++){");
		appendLine(sb, "	try{");
		appendLine(sb,"  $('#"+name+"').datagrid((cols.visible==false?'hideColumn':'showColumn'),cols[i].field);");
		appendLine(sb, "}catch(e){");
		appendLine(sb, "}");
		appendLine(sb, "}");
		appendLine(sb, "}");

		if (columnList.size() > 0) {
			appendLine(sb,"function " + name + "search(){");
			appendLine(sb,"var queryParams=$(\'#" + name + "\').datagrid('options').queryParams;");
			appendLine(sb,"$(\'#" + name + "tb\').find('*').each(function(){queryParams[$(this).attr('name')]=$(this).val();});");
			appendLine(sb,"$(\'#" + name + "\')." + grid + "({url:'" + actionUrl + "&field=" + searchFields + "',pageNumber:1});" + "}");
			
			//高级查询执行方法
			appendLine(sb,"function dosearch(params){");
			appendLine(sb,"var jsonparams=$.parseJSON(params);");
			appendLine(sb,"$(\'#" + name + "\')." + grid + "({url:'" + actionUrl + "&field=" + searchFields + "',queryParams:jsonparams});" + "}");
			 //searchbox框执行方法
			  searchboxFun(sb,grid);
			//回车事件
			appendLine(sb,"function EnterPress(e){");
			appendLine(sb,"var e = e || window.event;");
			appendLine(sb,"if(e.keyCode == 13){ ");
			appendLine(sb,name+"search();");
			appendLine(sb,"}}");

				
			appendLine(sb,"function searchReset(name){");
			appendLine(sb," $(\"#"+name+"tb\").find(\":input\").val(\"\");");
			String func = name.trim() + "search();";
			appendLine(sb,func);
			appendLine(sb,"}");
		}
		appendLine(sb,"</script>");
		appendLine(sb,"<table width=\"100%\"   id=\"" + name + "\" toolbar=\"#" + name + "tb\"></table>");
		appendLine(sb,"<div id=\"" + name + "tb\" style=\"padding:3px; height: auto\">");
		if(hasQueryColum(columnList)){
			appendLine(sb,"<div name=\"searchColums\">");
			appendLine(sb,"<input  id=\"_sqlbuilder\" name=\"sqlbuilder\"   type=\"hidden\" />");
			//如果表单是组合查询
			if("group".equals(getQueryMode())){
				for (DataGridColumn col : columnList) {
					if (col.isQuery()) {
						appendLine(sb,"<span style=\"display:-moz-inline-box;display:inline-block;\">");
						appendLine(sb,"<span style=\"vertical-align:middle;display:-moz-inline-box;display:inline-block;width: 80px;text-align:right;text-overflow:ellipsis;-o-text-overflow:ellipsis; overflow: hidden;white-space:nowrap; \" title=\""+col.getTitle()+"\">"+col.getTitle()+"：</span>");
						if("single".equals(col.getQueryMode())){
							if(!StringUtil.isEmpty(col.getReplace())){
								appendLine(sb,"<select name=\""+col.getField().replaceAll("_","\\.")+"\" WIDTH=\"100\" style=\"width: 104px\"> ");
								appendLine(sb,StringUtil.replaceAll("<option value =\"\" >{0}</option>", "{0}", MutiLangUtil.getLang("common.please.select")));
								String[] test = col.getReplace().split(",");
								String text = "";
								String value = "";
								
								
								
								for (String string : test) {
									String lang_key = string.split("_")[0];
									text = MutiLangUtil.getLang(lang_key);
									value =string.split("_")[1];
									appendLine(sb,"<option value =\""+value+"\">"+text+"</option>");
								}
								appendLine(sb,"</select>");
							}else if(!StringUtil.isEmpty(col.getDictionary())){
								if(col.getDictionary().contains(",")){
									String[] dic = col.getDictionary().split(",");
									String sql = "select " + dic[1] + " as field," + dic[2]
											+ " as text from " + dic[0];

									if(!StringUtil.isEmpty(col.getDictCondition())){
										sql += " "+col.getDictCondition();
									}

									systemService = ApplicationContextUtil.getContext().getBean(
											SystemService.class);
									List<Map<String, Object>> list = systemService.findForJdbc(sql);
									appendLine(sb,"<select name=\""+col.getField().replaceAll("_","\\.")+"\" WIDTH=\"100\" style=\"width: 104px\"> ");
									appendLine(sb,StringUtil.replaceAll("<option value =\"\" >{0}</option>", "{0}", MutiLangUtil.getLang("common.please.select")));
									for (Map<String, Object> map : list){
										appendLine(sb," <option value=\""+map.get("field")+"\">");
										appendLine(sb,(String)map.get("text"));
										appendLine(sb," </option>");
									}
									appendLine(sb,"</select>");
								}else{
									List<TSType> types = ResourceUtil.getCacheTypes(col.getDictionary().toLowerCase());
									appendLine(sb,"<select name=\""+col.getField().replaceAll("_","\\.")+"\" WIDTH=\"100\" style=\"width: 104px\"> ");
									appendLine(sb,StringUtil.replaceAll("<option value =\"\" >{0}</option>", "{0}", MutiLangUtil.getLang("common.please.select")));
									for (TSType type : types) {
										appendLine(sb," <option value=\""+type.getTypecode()+"\">");
										appendLine(sb,MutiLangUtil.getLang(type.getTypename()));
										appendLine(sb," </option>");
									}
									appendLine(sb,"</select>");
								}
							}else if(col.isAutocomplete()){
								appendLine(sb,getAutoSpan(col.getField().replaceAll("_","\\."),extendAttribute(col.getExtend())));
							}else{
								appendLine(sb,"<input onkeypress=\"EnterPress(event)\" onkeydown=\"EnterPress()\"  type=\"text\" name=\""+col.getField().replaceAll("_","\\.")+"\"  "+extendAttribute(col.getExtend())+" class=\"inuptxt\"/>");
							}
						}else if("group".equals(col.getQueryMode())){
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_begin\"   "+extendAttribute(col.getExtend())+" class=\"inuptxt\"/>");
							appendLine(sb,"<span style=\"display:-moz-inline-box;display:inline-block;width: 8px;text-align:right;\">~</span>");
							appendLine(sb,"<input type=\"text\" name=\""+col.getField()+"_end\"   "+extendAttribute(col.getExtend())+" class=\"inuptxt\"/>");

						}
						appendLine(sb,"</span>");
					}
				}
			}
			appendLine(sb,"</div>");
		}

		if(toolBarList==null || toolBarList.size()==0){

			appendLine(sb,"<div style=\"height:0px;\" >"); 
		}else{//TODO 

			appendLine(sb,"<div style=\"border-bottom-width:0;height:auto;\" class=\"datagrid-toolbar\">");

		}
		appendLine(sb,"<span style=\"float:left;\" >");
		if(toolBarList.size()>0)
		{
			for (DataGridUrl toolBar : toolBarList) {				

				if (btnCls != null && !btnCls.equals("easyui")) {//自定以样式 bootstrap按钮样式
					if(btnCls.indexOf("bootstrap")==0){
						if (btnCls.replace("bootstrap", "").trim().length() > 0) {
							sb.append("<button class=\""+btnCls.replace("bootstrap", "").trim()+"\" ");
						}else{
							sb.append("<button class=\"btn btn-info btn-xs\" ");
						}
						
						if(StringUtil.isNotEmpty(toolBar.getOnclick()))
						{
							sb.append("onclick="+toolBar.getOnclick()+"");
						}
						else {
							sb.append("onclick=\""+toolBar.getFunname()+"(");
							if(!toolBar.getFunname().equals("doSubmit"))
							{
								sb.append("\'"+toolBar.getTitle()+"\',");
							}
							String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
							String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
							sb.append("\'"+toolBar.getUrl()+"\',\'"+name+"\',"+width+","+height+")\"");
						}
						appendLine(sb,">");
						appendLine(sb,"<i class=\"" + toolBar.getIcon() + "\"></i>");
						appendLine(sb,"<span class=\"bigger-110 no-text-shadow\">"+toolBar.getTitle()+"</span></button>");
						
					}else{
						sb.append("<a href=\"#\" class=\""+btnCls+" " + toolBar.getIcon()+"\" ");
						if(StringUtil.isNotEmpty(toolBar.getOnclick()))
						{
							sb.append("onclick="+toolBar.getOnclick()+"");
						}
						else {
							sb.append("onclick=\""+toolBar.getFunname()+"(");
							if(!toolBar.getFunname().equals("doSubmit"))
							{
								sb.append("\'"+toolBar.getTitle()+"\',");
							}
							String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
							String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
							sb.append("\'"+toolBar.getUrl()+"\',\'"+name+"\',"+width+","+height+")\"");
						}
						appendLine(sb,">");
						appendLine(sb,toolBar.getTitle()+"</a>");
					}
					
				}else if(btnCls == null || btnCls.equals("easyui")){//easyUI按钮样式

					sb.append("<a href=\"#\" class=\"button\" plain=\"true\" icon=\""+toolBar.getIcon()+"\" ");
					if(StringUtil.isNotEmpty(toolBar.getOnclick()))
					{
						sb.append("onclick="+toolBar.getOnclick()+"");
					}
					else {
						sb.append("onclick=\""+toolBar.getFunname()+"(");
						if(!toolBar.getFunname().equals("doSubmit"))
						{
							sb.append("\'"+toolBar.getTitle()+"\',");
						}
						String width = toolBar.getWidth().contains("%")?"'"+toolBar.getWidth()+"'":toolBar.getWidth();
						String height = toolBar.getHeight().contains("%")?"'"+toolBar.getHeight()+"'":toolBar.getHeight();
						sb.append("\'"+toolBar.getUrl()+"\',\'"+name+"\',"+width+","+height+")\"");
					}
					appendLine(sb,">");
					appendLine(sb,toolBar.getTitle()+"</a>");
				}				

			}
		}
		appendLine(sb,"</span>");
		if("group".equals(getQueryMode()) && hasQueryColum(columnList)){//如果表单是组合查询	

			appendLine(sb,"<span style=\"float:right\">");
			
			if (btnCls != null && !btnCls.equals("easyui")) {//自定以样式 bootstrap按钮样式
				if(btnCls.indexOf("bootstrap")==0){
					String defalutCls = "btn btn-info btn-xs";
					
					if (btnCls.replace("bootstrap", "").trim().length() > 0) {
						defalutCls = btnCls.replace("bootstrap", "").trim();
					}

					appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"" + name + "search()\">");
					appendLine(sb,"		<i class=\"fa fa-search\"></i>");
					appendLine(sb,"		<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.query")+"</span>");
					appendLine(sb,"</button>");						

					appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"searchReset('" + name + "')\">");
					appendLine(sb,"		<i class=\"fa fa-refresh\"></i>");
					appendLine(sb,"		<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.reset")+"</span>");
					appendLine(sb,"</button>");
					
					if(queryBuilder){
						appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"queryBuilder()\">");
						appendLine(sb,"		<i class=\"fa fa-search\"></i>");
						appendLine(sb,"		<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.querybuilder")+"</span>");
						appendLine(sb,"</button>");
					}

					if(superQuery) {
						appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"queryBuilder()\">");
						appendLine(sb,"		<i class=\"fa fa-search\"></i>");
						appendLine(sb,"		<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.superquery")+"</span>");
						appendLine(sb,"</button>");
					}

					if(oConvertUtils.isNotEmpty(complexSuperQuery)) {

						appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\""+name+"SuperQuery('"+complexSuperQuery+"')\">");

						appendLine(sb,"		<i class=\"fa fa-search\"></i>");
						appendLine(sb,"		<span class=\"bigger-110 no-text-shadow\">"+MutiLangUtil.getLang("common.advancedQuery")+"</span>");
						appendLine(sb,"</button>");
					}

				}else{//自定以样式
					appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\""+  name+ StringUtil.replaceAll("search()\">{0}</a>", "{0}", MutiLangUtil.getLang("common.query")));
					appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\"searchReset('"+name+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.reset")) );
					if(queryBuilder){
						appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.querybuilder")) );
					}

					if(superQuery){
						appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.superquery")) );
					}

					if(oConvertUtils.isNotEmpty(complexSuperQuery)){
 						appendLine(sb,"<a href=\"#\" class=\""+btnCls+"\" onclick=\""+name+"SuperQuery('"+complexSuperQuery+"')\">"+MutiLangUtil.getLang("common.advancedQuery")+"</a>");
					}

				}
			}else{//默认使用easyUI按钮样式 
				appendLine(sb,"<a href=\"#\" class=\"button\" iconCls=\"icon-search\" onclick=\""+  name+ StringUtil.replaceAll("search()\">{0}</a>", "{0}", MutiLangUtil.getLang("common.query")));
				appendLine(sb,"<a href=\"#\" class=\"button\" iconCls=\"icon-reload\" onclick=\"searchReset('"+name+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.reset")) );
				if(queryBuilder){
					appendLine(sb,"<a href=\"#\" class=\"button\" iconCls=\"icon-search\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.querybuilder")) );
				}

				if(superQuery){
					appendLine(sb,"<a href=\"#\" class=\"button\" iconCls=\"icon-search\" onclick=\"queryBuilder('"+ StringUtil.replaceAll("')\">{0}</a>", "{0}", MutiLangUtil.getLang("common.superQuery")) );
				}

				if(oConvertUtils.isNotEmpty(complexSuperQuery)){

					appendLine(sb,"<a href=\"#\" class=\"button\" iconCls=\"icon-search\" onclick=\""+name+"SuperQuery('"+complexSuperQuery+"')\">"+MutiLangUtil.getLang("common.advancedQuery")+"</a>");

				}

			}
			
			appendLine(sb,"</span>");

		}else if("single".equals(getQueryMode())&& hasQueryColum(columnList)){//如果表单是单查询
			appendLine(sb,"<span style=\"float:right\">");
			appendLine(sb,"		<input id=\""+name+"searchbox\" class=\"easyui-searchbox\"  data-options=\"searcher:"+name+ StringUtil.replaceAll("searchbox,prompt:\'{0}\',menu:\'#", "{0}", MutiLangUtil.getLang("common.please.input.keyword")) +name+"mm\'\"></input>");
			appendLine(sb,"		<div id=\""+name+"mm\" style=\"width:120px\">");
			for (DataGridColumn col : columnList) {
				if (col.isQuery()) {
					appendLine(sb,"<div data-options=\"name:\'"+col.getField().replaceAll("_","\\.")+"\',iconCls:\'icon-ok\' "+extendAttribute(col.getExtend())+" \">"+col.getTitle()+"</div>  ");
				}
			}
			appendLine(sb,"</div>");
			appendLine(sb,"</span>");
		}

		appendLine(sb,"<div style=\"clear:both\"></div>");

		appendLine(sb,"</div>");
		if(queryBuilder){

			if (btnCls != null && !btnCls.equals("easyui")) {//自定以样式 bootstrap按钮样式
				
				addQueryBuilder(sb,btnCls);
				
			}else{//默认使用easyUI按钮样式 

				addQueryBuilder(sb,"button");
			}

		}

		if(superQuery) {
			if(btnCls != null && !btnCls.equals("easyui")) {
				addSuperQuery(sb,btnCls,columnList);
			} else {
				addSuperQuery(sb,"button",columnList);
			}
		}

		if(oConvertUtils.isNotEmpty(complexSuperQuery)) {
			if(btnCls != null && !btnCls.equals("easyui")) {
				addAdvancedQuery(sb,btnCls);
			} else {
				addAdvancedQuery(sb,"button");
			}
		}

		return sb;
	}

	private void appendLine(StringBuffer sb,String str) {
		String format = "\r\n"; //调试  格式化
		sb.append(str).append(format);
	}
	/**
	 * TODO 语言做成多语翻译，保留历史查询记录
	 * @param sb
	 */
	private void addQueryBuilder(StringBuffer sb,String buttonSytle) {

		appendLine(sb,"<div style=\"position:relative;overflow:auto;\">");
		appendLine(sb,"		<div id=\""+name+"_qbwin\" class=\"easyui-window\" data-options=\"closed:true,title:'&nbsp;&nbsp;高级查询构造器'\" style=\"width:600px;height:370px;padding:0px\">");
		appendLine(sb,"		<div class=\"easyui-layout\" data-options=\"fit:true\">");
		appendLine(sb,"		<div data-options=\"region:'east',split:true\" style=\"width:130px;overflow: hidden;\"><div data-options=\"fit:true\" class=\"easyui-accordion\" style=\"width:120px;height:300px;\">");
		appendLine(sb,"<div title=\"查询历史\" data-options=\"iconCls:'icon-search'\" style=\"padding:0px;\">");
		appendLine(sb,"	<ul id=\""+ name + "tt\" class=\"easyui-tree\" data-options=\"onClick:function(node){");
		appendLine(sb, "		historyQuery( node.id);  ");
		appendLine(sb, "		},ondbClick: function(node){");
		appendLine(sb, "			$(this).tree('beginEdit',node.target);");
		appendLine(sb, "		},onContextMenu: function(e,node){");
		appendLine(sb, "			e.preventDefault();");
		appendLine(sb, "			$(this).tree('select',node.target);");
		appendLine(sb, "			$('#"+name+"mmTree').menu('show',{");
		appendLine(sb, "			left: e.pageX,");
		appendLine(sb, "			top: e.pageY");
		appendLine(sb, "		});");
		appendLine(sb, "		},  ");
		appendLine(sb, " onAfterEdit:function(node){  ");
		appendLine(sb,"    		if(node.text!=''){ "+name+"his[node.id].name=node.text; saveHistory();}	}");
		appendLine(sb, "		\">");
		appendLine(sb, "	</ul>");
		appendLine(sb, "</div>");
		appendLine(sb, "</div></div>");
		appendLine(sb,"		<div data-options=\"region:'center'\" style=\"width:450px;padding:0px;overflow: hidden;\">");
		appendLine(sb,"			<table id=\""+name+"tg\" class=\"easyui-treegrid\" title=\"查询条件编辑\" style=\"width:450px;height:300px;\"");
		appendLine(sb,"				data-options=\"");
		appendLine(sb,"				iconCls: 'icon-ok',");
		appendLine(sb,"				rownumbers: true,");
		appendLine(sb,"				animate: true,");
		appendLine(sb,"				fitColumns: true,");
		appendLine(sb,"				//url: 'sqlbuilder.json',//可以预加载条件\r\n");
		appendLine(sb,"				method: 'get',");
		appendLine(sb,"				idField: 'id',");
		appendLine(sb,"				fit:true,");
		appendLine(sb,"				autoEditing: true,  ");
		appendLine(sb,"				extEditing: false, ");
		appendLine(sb,"				singleEditing: false ,");
		sb.append("				treeField: 'field',toolbar:toolbar,onContextMenu: onContextMenu");
		appendLine(sb,"\">");
		appendLine(sb,"<thead>");
		appendLine(sb,"	<tr>");
		appendLine(sb,"		<th data-options=\"field:'relation',width:18,formatter:function(value,row){");
		appendLine(sb,"				return value=='and'?'并且':'或者';");
		appendLine(sb,"			},editor:{");
		appendLine(sb,"				type:'combobox',");
		appendLine(sb,"				options:{");
		appendLine(sb,"				valueField:'relationId',");
		appendLine(sb,"						textField:'relationName',");
		appendLine(sb,"						data:  ");
		appendLine(sb,"						[  ");
		appendLine(sb,"						{'relationId':'and','relationName':'并且'},  ");
		appendLine(sb,"						{'relationId':'or','relationName':'或者'}  ");
		appendLine(sb,"						],  ");
		appendLine(sb,"						required:true");
		appendLine(sb,"					}}\">关系</th>");
		appendLine(sb,"		<th data-options=\"field:'field',width:30,formatter:function(value,row){");
		appendLine(sb,"			var data= ");
		StringBuffer fieldArray=new StringBuffer();
		fieldArray.append("	[  ");
		for (int i=0;i<columnList.size();i++){
			DataGridColumn col =columnList.get(i);
			if("opt".equals(col.getField()))continue;//忽略操作虚拟字段
			fieldArray.append("	{'fieldId':'"+getDBFieldName(col.getField())+"','fieldName':'"+col.getTitle()+"'");
			if(col.getEditor()!=null){
				fieldArray.append(",editor:'"+col.getEditor()+"'");
			}
			fieldArray.append("}");
			if(i<columnList.size()-1){
				fieldArray.append(",");
			}
		}
//		appendLine(sb,"				{'fieldId':'office_Phone','fieldName':'办公电话'},");
		fieldArray.append("]");
		appendLine(sb,fieldArray+";");
		appendLine(sb,"		for(var i=0;i<data.length;i++){");
		appendLine(sb,"			if(value == data[i]['fieldId']){");
		appendLine(sb,"				return data[i]['fieldName'];");
		appendLine(sb,"			}");
		appendLine(sb,"		}");
		appendLine(sb,"return value;");
		appendLine(sb,"},editor:{");
		appendLine(sb,"		type:'combobox',");
		appendLine(sb,"		options:{");
		appendLine(sb,"		valueField:'fieldId',");
		appendLine(sb,"		textField:'fieldName',");
		appendLine(sb,"		data:  ");
		appendLine(sb,"			"+fieldArray.toString());
		appendLine(sb," , ");
		appendLine(sb, "	required:true,onSelect : function(record) {");
		appendLine(sb,"			var opts = $('#"+name+"tg').treegrid('getColumnOption','value');");
		appendLine(sb, "		if(record.editor){");
		appendLine(sb, "			opts.editor=record.editor;");
		appendLine(sb, "		}else{");
		appendLine(sb, "			opts.editor='text';");
		appendLine(sb, "		}");
		appendLine(sb, "		var tr = $(this).closest('tr.datagrid-row');");
		appendLine(sb, "		var index = parseInt(tr.attr('node-id'));");
		appendLine(sb, "		$('#"+name+"tg').treegrid('endEdit', index);");
		appendLine(sb, "		$('#"+name+"tg').treegrid('beginEdit', index);");
		appendLine(sb, "	}");
		appendLine(sb,"	}}\">字段</th>");
		appendLine(sb,"<th data-options=\"field:'condition',width:20,align:'right',formatter:function(value,row){");
		appendLine(sb,"			var data=  ");
		appendLine(sb,"			[  ");
		List<TSType> types = ResourceUtil.getCacheTypes("rulecon");
		if (types != null) {
			for (int i=0;i<types.size();i++){
				TSType type = types.get(i);
				appendLine(sb," {'conditionId':'"+type.getTypecode()+"','conditionName':'"
						+MutiLangUtil.getLang(type.getTypename())+"'}");
				if(i<types.size()-1){
					appendLine(sb,",");
				}
			}
		}
		appendLine(sb,"];");
		appendLine(sb,"	for(var i=0;i<data.length;i++){");
		appendLine(sb,"			if(value == data[i]['conditionId']){");
		appendLine(sb,"			return data[i]['conditionName'];");
		appendLine(sb,"			}");
		appendLine(sb,"		}");
		appendLine(sb,"		return value;");
		appendLine(sb,"		},editor:{");
		appendLine(sb,"			type:'combobox',");
		appendLine(sb,"			options:{");
		appendLine(sb,"			valueField:'conditionId',");
		appendLine(sb,"			textField:'conditionName',	");
		appendLine(sb,"			data:  ");
		appendLine(sb,"				[");
		if (types != null) {
			for (int i=0;i<types.size();i++){
				TSType type = types.get(i);
				appendLine(sb," 			{'conditionId':'"+type.getTypecode()+"','conditionName':'"
						+MutiLangUtil.getLang(type.getTypename())+"'}");
				if(i<types.size()-1){
					appendLine(sb,"			,");
				}
			}
		}
		appendLine(sb,"					],  ");
		appendLine(sb,"					required:true");
		appendLine(sb,"				}}\">条件</th>");
		appendLine(sb,"	<th data-options=\"field:'value',width:30,editor:'text'\">值</th>");
		appendLine(sb,"<th data-options=\"field:'opt',width:30,formatter:function(value,row){");
		appendLine(sb,"			return '<a  onclick=\\'removeIt('+row.id+')\\' >删除</a>';}\">操作</th>");
		appendLine(sb,"	</tr>");
		appendLine(sb,"	</thead>");
		appendLine(sb,"	</table>");
		appendLine(sb,"</div>");
		appendLine(sb,"<div data-options=\"region:'south',border:false\" style=\"text-align:right;padding:5px 0 3px;\">");
		if (btnCls != null && !btnCls.equals("easyui")) {
			String defalutCls = "btn btn-default btn-xs";
			if (btnCls.replace("bootstrap", "").trim().length() > 0) {
				defalutCls = btnCls.replace("bootstrap", "").trim();
			}
			appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"javascript:queryBuilderSearch()\">&nbsp;<i class=\"fa fa-check\"></i>确定</button>");
			appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"javascript:$('#"+name+"_qbwin').window('close')\">&nbsp;<i class=\"fa fa-close\"></i>取消</button>");
			
		}else{
			appendLine(sb,"<a class=\""+buttonSytle+"\" data-options=\"iconCls:'icon-ok'\" href=\"javascript:void(0)\" onclick=\"javascript:queryBuilderSearch()\">确定</a>");
			appendLine(sb,"<a class=\""+buttonSytle+"\" data-options=\"iconCls:'icon-cancel'\" href=\"javascript:void(0)\" onclick=\"javascript:$('#"+name+"_qbwin').window('close')\">取消</a>");
		}
		
		appendLine(sb,"		</div>");
		appendLine(sb,"	</div>	");
		appendLine(sb,"</div>		");
		appendLine(sb,"</div>");
		appendLine(sb,"<div id=\"mm\" class=\"easyui-menu\" style=\"width:120px;\">");
		appendLine(sb,"	<div onclick=\"append()\" data-options=\"iconCls:'icon-add'\">添加</div>");
		appendLine(sb,"	<div onclick=\"edit()\" data-options=\"iconCls:'icon-edit'\">编辑</div>");
		appendLine(sb,"	<div onclick=\"save()\" data-options=\"iconCls:'icon-save'\">保存</div>");
		appendLine(sb,"	<div onclick=\"removeIt()\" data-options=\"iconCls:'icon-remove'\">删除</div>");
		appendLine(sb,"	<div class=\"menu-sep\"></div>");
		appendLine(sb,"	<div onclick=\"cancel()\">取消</div>");
		appendLine(sb,"<div onclick=\"expand()\">Expand</div>");
		appendLine(sb,"</div><div id=\""+name+"mmTree\" class=\"easyui-menu\" style=\"width:100px;\">");
		appendLine(sb,"<div onclick=\"editTree()\" data-options=\"iconCls:'icon-edit'\">编辑</div>");
		appendLine(sb,"<div onclick=\"deleteTree()\" data-options=\"iconCls:'icon-remove'\">删除</div></div>");
		//已在baseTag中引入
//		appendLine(sb,"<script type=\"text/javascript\" src=\"plug-in/jquery/jquery.cookie.js\" ></script>");
//		appendLine(sb,"<script type=\"text/javascript\" src=\"plug-in/jquery-plugs/storage/jquery.storageapi.min.js\" ></script>"); 
		appendLine(sb,"<script type=\"text/javascript\">");
		
		if (btnCls != null && !btnCls.equals("easyui")) {

			String defalutCls = "btn btn-info btn-xs";
			
			if (btnCls.replace("bootstrap", "").trim().length() > 0) {
				defalutCls = btnCls.replace("bootstrap", "").trim();
			}
			appendLine(sb,"var toolbar = '<div>");
			appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"append()\">&nbsp;<i class=\"fa fa-plus\"></i>&nbsp;</button>");
			appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"edit()\">&nbsp;<i class=\"fa fa-pencil-square-o\"></i></button>");
			appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"removeIt()\">&nbsp;<i class=\"fa fa-trash\"></i></button>");
			appendLine(sb,"<button class=\""+defalutCls+"\" onclick=\"save()\">&nbsp;<i class=\"fa fa-save\"></i></button>");
			appendLine(sb,"</div>';");
			
		}else{		
			appendLine(sb,"var toolbar = [{");
			appendLine(sb,"		text:'',");
			appendLine(sb,"		iconCls:'icon-add',");
			appendLine(sb,"		handler:function(){append();}");
			appendLine(sb,"},{");
			appendLine(sb,"		text:'',");
			appendLine(sb,"		iconCls:'icon-edit',");
			appendLine(sb,"		handler:function(){edit();}");
			appendLine(sb,"},{");
			appendLine(sb,"		text:'',");
			appendLine(sb,"		iconCls:'icon-remove',");
			appendLine(sb,"		handler:function(){removeIt();}");
			appendLine(sb,"},'-',{");
			appendLine(sb,"		text:'',");
			appendLine(sb,"		iconCls:'icon-save',");
			appendLine(sb,"		handler:function(){save();}");
			appendLine(sb,"	}];");
		}
		appendLine(sb,"function onContextMenu(e,row){");
		appendLine(sb,"		e.preventDefault();");
		appendLine(sb,"		$(this).treegrid('select', row.id);");
		appendLine(sb,"		$('#mm').menu('show',{");
		appendLine(sb,"			left: e.pageX,");
		appendLine(sb,"			top: e.pageY");
		appendLine(sb,"		});");
		appendLine(sb,"}");
		appendLine(sb,"	var idIndex = 100;");
		appendLine(sb,"function append(){");
		appendLine(sb,"		idIndex++;");
		appendLine(sb,"		var node = $('#"+name+"tg').treegrid('getSelected');");
		appendLine(sb,"		$('#"+name+"tg').treegrid('append',{");
		appendLine(sb,"		data: [{");
		appendLine(sb,"			id: idIndex,");
		appendLine(sb,"			field: '',");
		appendLine(sb,"		condition:'like',");
		appendLine(sb,"		value: '%a%',");
		appendLine(sb,"		relation: 'and'");
		appendLine(sb,"				}]");
		appendLine(sb,"});$('#"+name+"tg').datagrid('beginEdit',idIndex);");
		appendLine(sb,"}");
		appendLine(sb,"		function removeIt(id){");
		appendLine(sb,"			var node = $('#"+name+"tg').treegrid('getSelected');");
		appendLine(sb,"			if(id){");
		appendLine(sb,"				$('#"+name+"tg').treegrid('remove', id);");
		appendLine(sb,"			}else if(node){	$('#"+name+"tg').treegrid('remove', node.id);");
		appendLine(sb,"			}");
		appendLine(sb,"		}");
		appendLine(sb,"function collapse(){");
		appendLine(sb,"		var node = $('#"+name+"tg').treegrid('getSelected');");
		appendLine(sb,"		if(node){");
		appendLine(sb,"			$('#"+name+"tg').treegrid('collapse', node.id);");
		appendLine(sb,"		}");
		appendLine(sb,"	}");
		appendLine(sb,"function expand(){");
		appendLine(sb,"		var node = $('#"+name+"tg').treegrid('getSelected');");
		appendLine(sb,"		if(node){");
		appendLine(sb,"			$('#"+name+"tg').treegrid('expand', node.id);");
		appendLine(sb,"		}");
		appendLine(sb,"}");
		appendLine(sb,"var editingId;");
		appendLine(sb,"function edit(id){");
		appendLine(sb,"		var row = $('#"+name+"tg').treegrid('getSelected');");
		appendLine(sb,"		if(id){	$('#"+name+"tg').treegrid('beginEdit', id);}else if(row){");
		appendLine(sb,"		$('#"+name+"tg').treegrid('beginEdit', row.id);");
		appendLine(sb,"		}");
		appendLine(sb,"}");
		appendLine(sb,"function save(){");
		appendLine(sb,"		var t = $('#"+name+"tg');");
		appendLine(sb,"		var nodes = t.treegrid('getRoots');");
		appendLine(sb,"		for (var i = 0; i < nodes.length; i++) {");
		appendLine(sb,"			t.treegrid('endEdit',nodes[i].id);}");
		appendLine(sb,"		}");
		appendLine(sb,"function cancel(){");
		appendLine(sb,"		var t = $('#"+name+"tg');");
		appendLine(sb,"		var nodes = t.treegrid('getRoots');for (var i = 0; i < nodes.length; i++) {t.treegrid('cancelEdit',nodes[i].id);}");
		appendLine(sb,"	}");
		appendLine(sb, "var "+name+"his=new Array();");
		appendLine(sb, " function historyQuery(index) {");
		appendLine(sb, "	  	var data  = { rows:JSON.parse("+name+"his[index].json)};  ");
		appendLine(sb, "	    var t = $('#" + name + "tg');");
		appendLine(sb, "		var data = t.treegrid('loadData',data);");
		appendLine(sb, "		$('#_sqlbuilder').val( "+name+"his[index].json);   ");
		appendLine(sb, "		"+name+"search();");
		appendLine(sb, "}");
		appendLine(sb, "function view(){");
		appendLine(sb,"		save();");
		appendLine(sb,"		var t = $('#"+name+"tg');");
		appendLine(sb,"		var data = t.treegrid('getData');");
		appendLine(sb,"		return   JSON.stringify(data) ;");
		appendLine(sb,"}");
		appendLine(sb,"	 function queryBuilder() {");
		appendLine(sb,"		$('#"+name+"_qbwin').window('open');");
		appendLine(sb,"  }");

		appendLine(sb, "function queryBuilderSearch() {");
		appendLine(sb, "	var json =  view();");
		appendLine(sb, "	$('#_sqlbuilder').val(json);  ");
		appendLine(sb, "	var isnew=true;");
		appendLine(sb, "	for(var i=0;i< "+name+"his.length;i++){");
		appendLine(sb, "	if("+name+"his[i]&&"+name+"his[i].json==json){");
		appendLine(sb, "		isnew=false;");
		appendLine(sb, "	}");
		appendLine(sb, "}");
		appendLine(sb, "if(isnew){");
		appendLine(sb, " 	"+name+"his.push({name:'Query'+"+name+"his.length,json:json});saveHistory();");
		appendLine(sb, "	var name= 'Query'+( "+name+"his.length-1);");
		appendLine(sb, "	var name= 'Query'+("+name+"his.length-1);");
		appendLine(sb, "	appendTree("+name+"his.length-1,name);");
		appendLine(sb, "}");
		appendLine(sb, "	" + name + "search();");
		appendLine(sb, " }");
		appendLine(sb, " $(document).ready(function(){ ");
		appendLine(sb, " 	storage=$.localStorage;if(!storage)storage=$.cookieStorage;");
		appendLine(sb, "	var _qhistory = storage.get('" + name+ "_query_history');");
		appendLine(sb, " if(_qhistory){");
		appendLine(sb, " "+name+"his=_qhistory;");
		// appendLine(sb, " 	var data  = { rows:his[0]};");
		appendLine(sb, " 	for(var i=0;i< "+name+"his.length;i++){");
		appendLine(sb, " 		if("+name+"his[i])appendTree(i,"+name+"his[i].name);");
		appendLine(sb, " 	}restoreheader();");
		appendLine(sb, " }});");
		appendLine(sb, "function saveHistory(){");
		appendLine(sb, "	var history=new Array();");
		appendLine(sb, "	for(var i=0;i<" + name + "his.length;i++){");
		appendLine(sb, "		if(" + name + "his[i]){");
		appendLine(sb, "			history.push(" + name + "his[i]);");
		appendLine(sb, "		}");
		appendLine(sb, "	}");
		appendLine(sb, "	storage.set( '"+name+"_query_history',JSON.stringify(history));");
		appendLine(sb, "}");
		appendLine(sb, "function deleteTree(){");
		appendLine(sb, "	var tree = $('#" + name + "tt');var node= tree.tree('getSelected');");
		appendLine(sb, "	" + name + "his[node.id]=null;saveHistory();");
		appendLine(sb, "	tree.tree('remove', node.target);");
		appendLine(sb, "}");
		appendLine(sb, "function editTree(){");
		appendLine(sb, "	var node = $('#" + name + "tt').tree('getSelected');");
		appendLine(sb, "	$('#" + name + "tt').tree('beginEdit',node.target);");
		appendLine(sb, "	saveHistory();");
		appendLine(sb, "}");
		appendLine(sb, "function appendTree(id,name){");
		appendLine(sb, "	$('#"+name+"tt').tree('append',{");
		appendLine(sb, "	data:[{");
		appendLine(sb, "		id : id,");
		appendLine(sb, "		text :name");
		appendLine(sb, "	}]");
		appendLine(sb, "	});");
		appendLine(sb, "}");
		

		appendLine(sb, "</script>");

	}
	/**
	 * hibernate字段名转换为数据库名称，只支持标准命名
	 * 否则转换错误
	 * @param fieldName
	 * @return
	 */
	String getDBFieldName(String fieldName){
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<fieldName.length();i++){
			char c =  fieldName.charAt(i);
			if(c<='Z'&&c>='A'){
				sb.append("_").append((char)((int)c+32));
			}else{
				sb.append(c);
			}
		}
		return sb.toString();
	}


	/**
	 * 高级查询
	 * @param sb
	 */
	private void addSuperQuery(StringBuffer sb,String buttonSytle,List<DataGridColumn> columnList) {
		FreemarkerHelper free = new FreemarkerHelper();
		Map<String, Object> mainConfig = new HashMap<String, Object>();
		mainConfig.put("fields", columnList);
		mainConfig.put("tableName", name);

		mainConfig.put("valueList", columnValueList);

		String superQuery = free.parseTemplate("/org/jeecgframework/tag/ftl/superquery.ftl", mainConfig);
		appendLine(sb,superQuery);
	}
	/**
	 * 高级查询bootstrap版
	 */
	private void addSuperQueryBootstrap(StringBuffer sb,String buttonSytle,List<DataGridColumn> columnList) {
		FreemarkerHelper free = new FreemarkerHelper();
		Map<String, Object> mainConfig = new HashMap<String, Object>();
		mainConfig.put("fields", columnList);
		mainConfig.put("tableName", name);
		mainConfig.put("valueList", columnValueList);
		String superQuery = free.parseTemplate("/org/jeecgframework/tag/ftl/superqueryBootstrap.ftl", mainConfig);
		appendLine(sb,superQuery);
	}

	//是否启用过滤
	protected boolean filter = false;	
	
	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}
	
	public void getFilter(StringBuffer sb){
		if (this.isFilter()) {
			FreemarkerHelper free = new FreemarkerHelper();
			Map<String, Object> mainConfig = new HashMap<String, Object>();
			mainConfig.put("gridId", name);
			String superQuery = free.parseTemplate("/org/jeecgframework/tag/ftl/filter.ftl", mainConfig);
			appendLine(sb,superQuery);
		}
	}

	protected boolean filterBtn = false;//按钮过滤模式是否开启，列表上若有一个按钮可以让其调用xxFilter函数出现过滤行--
	public boolean isFilterBtn() {
		return filterBtn;
	}
	public void setFilterBtn(boolean filterBtn) {
		this.filterBtn = filterBtn;
	}
	/**
	 * 获取过滤字段配置
	 * @return
	 */
	private void getFilterFields(StringBuffer sb){
		if(this.isFilterBtn()){
			StringBuffer ffs = new StringBuffer();
			int index = 0;
			for (DataGridColumn column :columnList) {
				if(column.getField().equals("opt")){
					continue;
				}
				if(index!=0){
					ffs.append(",");
				}
				index++;
				String filterType = column.getFilterType();
				ffs.append("{");
				ffs.append("field:'"+column.getField()+"',");
				ffs.append("type:'"+filterType+"',");
				if("combobox".equals(filterType)){
					ffs.append("options:{");
					ffs.append("panelHeight:'auto',");
					ffs.append("data:[{value:'',text:'All'}");
					for (ColumnValue columnValue : columnValueList) {
						if (columnValue.getName().equals(column.getField())) {
							String[] value = columnValue.getValue().split(",");
							String[] text = columnValue.getText().split(",");
							for (int k = 0; k < value.length; k++) {
								ffs.append(",{value:'"+value[k]+"',text:'"+text[k]+"'}");
							}
							break;
						}
					}
					ffs.append("],");
					ffs.append("onChange:function(value){");//if (value == ''){}$('#"+name+"').datagrid('removeFilterRule', '"+column.getField()+"');
					// else {
					ffs.append("$('#"+name+"').datagrid('addFilterRule', {field: '"+column.getField()+"',op: 'equal',value: value});");//}
					ffs.append("$('#"+name+"').datagrid('doFilter');}}");//option-end
				}else{
					ffs.append("options:{precision:1},");
					if("numberbox".equals(filterType) || "datebox".equals(filterType)|| "datetimebox".equals(filterType)){
						ffs.append("op:['equal','lessorequal','greaterorequal']");
					}else{
						ffs.append("op:['equal','contains']");
					}
				}
				ffs.append("}");
			}
			appendLine(sb,"function "+name+"Filter(){$('#"+name+"').datagrid('enableFilter',["+ffs.toString()+"]);}");
		}
	}
	/**
	 * 拼装操作地址,新的风格
	 * @param sb
	 */
	private void getOptsMenuUrl(StringBuffer sb){
		this.getOptUrl(sb,true,false);
		StringBuffer groupString = new StringBuffer();
		this.getOptUrl(groupString,true,true);
		if(oConvertUtils.isNotEmpty(groupString.toString())){
			appendLine(sb,"     href+='<div style=\"left:40px;top:-1px\" class=\"opts_menu_container\"><div class=\"opts_menu_btn btn-menu\"><i class=\"fa fa-caret-right\" style=\"margin-top:1px;\"></i></div>';");
			appendLine(sb,"     href+='<div style=\"clear: both;\"></div><div style=\"\" class=\"opts-menus-parent pp_menu_box\"><div class=\"opts_menu_box opts-menus-auto\" style=\"left:18px;\">';");
			appendLine(sb,"     "+groupString.toString());
			appendLine(sb,"     href+='</div></div><em class=\"ops_shadeEm\" style=\"display: inline;\"></em></div>';");
		}
		appendLine(sb,"     return href;");
	}
	/**
	 * * 拼装操作地址,新的风格
	 * @param sb
	 * @param noReturn 是否不在该方法中返回href 
	 * @param initGroup 是否加载的是隐藏菜单
	 */
	protected void getOptUrl(StringBuffer sb,boolean noReturn,boolean initGroup) {
		//注：操作列表会带入合计列中去，故加此判断
		List<DataGridUrl> list = urlList;
		if(!initGroup){
			appendLine(sb,"if(!rec.id){return '';}");
			appendLine(sb,"var href='';");
		}
		for (DataGridUrl dataGridUrl : list) {
			if(initGroup){
				//若加载的是组菜单 但该菜单其实不是组菜单则 跳过
				if(!dataGridUrl.isInGroup()){
					continue;
				}
			}else{
				//若加载的不是组菜单 但该菜单其实是组菜单则 跳过
				if(dataGridUrl.isInGroup()){
					continue;
				}
			}
			String url = dataGridUrl.getUrl();
			MessageFormat formatter = new MessageFormat("");
			if (dataGridUrl.getValue() != null) {
				String[] testvalue = dataGridUrl.getValue().split(",");
				List value = new ArrayList<Object>();
				for (String string : testvalue) {
					value.add("\"+rec." + string + " +\"");
				}
				url = formatter.format(url, value.toArray());
			}
			if (url != null && dataGridUrl.getValue() == null) {
				url = formatUrlPlus(url);
			}
			String exp = dataGridUrl.getExp();// 判断显示表达式
			if (StringUtil.isNotEmpty(exp)) {
				String[] ShowbyFields = exp.split("&&");
				for (String ShowbyField : ShowbyFields) {
					int beginIndex = ShowbyField.indexOf("#");
					int endIndex = ShowbyField.lastIndexOf("#");
					String exptype = ShowbyField.substring(beginIndex + 1, endIndex);// 表达式类型
					String field = ShowbyField.substring(0, beginIndex);// 判断显示依据字段
					String[] values = ShowbyField.substring(endIndex + 1, ShowbyField.length()).split(",");// 传入字段值
					String value = "";
					for (int i = 0; i < values.length; i++) {
						value += "'" + "" + values[i] + "" + "'";
						if (i < values.length - 1) {
							value += ",";
						}
					}
					if ("eq".equals(exptype)) {
						appendLine(sb,"if($.inArray(rec." + field + ",[" + value + "])>=0){");
					}
					if ("ne".equals(exptype)) {
						appendLine(sb,"if($.inArray(rec." + field + ",[" + value + "])<0){");
					}
					if ("empty".equals(exptype) && value.equals("'true'")) {
						appendLine(sb,"if(rec." + field + "==''){");
					}
					if ("empty".equals(exptype) && value.equals("'false'")) {
						appendLine(sb,"if(rec." + field + "!=''){");
					}
				}
			}
			StringBuffer style = new StringBuffer();
			if (!StringUtil.isEmpty(dataGridUrl.getUrlStyle())) {
				style.append(" style=\'");
				style.append(dataGridUrl.getUrlStyle());
				style.append("\' ");
			}
			StringBuffer urlclass = new StringBuffer();
			StringBuffer urlfont = new StringBuffer();
			if(initGroup){
				urlclass.append(" class=\'btn btn-menu fa ");
				if(!StringUtil.isEmpty(dataGridUrl.getUrlfont())){
					urlclass.append(dataGridUrl.getUrlfont());
				}else{
					urlclass.append("fa-font");
				}
				urlclass.append(" menu-more\'");
			}else{
				urlclass.append(" class=\'btn-menu\'");
				urlfont.append("<i class=\'fa ");
				if(!StringUtil.isEmpty(dataGridUrl.getUrlfont())){
					urlfont.append(dataGridUrl.getUrlfont());
				}else{
					urlfont.append("fa-font");
				}
				urlfont.append("\'></i>");
			}
			if (OptTypeDirection.Fun.equals(dataGridUrl.getType())) {
				String name = TagUtil.getFunction(dataGridUrl.getFunname());
				String parmars = TagUtil.getFunParams(dataGridUrl.getFunname());
				appendLine(sb,"href+=\"<a href=\'#\' title='"+dataGridUrl.getTitle()+"' "+urlclass.toString()+" onclick=" + name + "(" + parmars + ")" + style.toString() + ">"+urlfont.toString()+"\";");
			}
			if (OptTypeDirection.OpenWin.equals(dataGridUrl.getType())) {
				String funname = dataGridUrl.getFunname();
				if(oConvertUtils.isEmpty(funname)){
					funname = "openwindow";
				}
				String dgFormWidth = dataGridUrl.getWidth();
				if("100%".equals(dgFormWidth)){
					dgFormWidth = "'"+dgFormWidth+"'";
				}else if(oConvertUtils.isEmpty(dgFormWidth)){
					dgFormWidth = "''";
				}
				String dgFormHeight = dataGridUrl.getHeight();
				if("100%".equals(dgFormHeight)){
					dgFormHeight = "'"+dgFormHeight+"'";
				}else if(oConvertUtils.isEmpty(dgFormHeight)){
					dgFormHeight = "''";
				}
				appendLine(sb,"href+=\"<a href=\'####\' title='"+dataGridUrl.getTitle()+"' "+urlclass.toString()+" onclick="+funname+"('" + dataGridUrl.getTitle() + "','" + url + "','"+name+"'," + dgFormWidth + "," + dgFormHeight + ")" + style.toString() + ">"+urlfont.toString()+"\";");
			}
			appendLine(sb,"href+=\"" + "" + "</a>&nbsp;\";");
			if (StringUtil.isNotEmpty(exp)) {
				for (int i = 0; i < exp.split("&&").length; i++) {
					appendLine(sb,"}");
				}
			}
		}
		if(!noReturn){
			appendLine(sb,"return href;");
		}
	}

	/**
	 * 高级查询构造器
	 * @param sb
	 */
	private void addAdvancedQuery(StringBuffer sb,String buttonSytle) {

		/*FreemarkerHelper free = new FreemarkerHelper();
		Map<String, Object> mainConfig = new HashMap<String, Object>();
		mainConfig.put("queryCode", complexSuperQuery);
		mainConfig.put("tableName", name);
		String complexSuperQuery = free.parseTemplate("/org/jeecgframework/tag/ftl/complexSuperQuery.ftl", mainConfig);
		appendLine(sb,complexSuperQuery);*/

	}

	
	/**
	 * 判断当前浏览器不是IE,采用有bootstrap样式按钮
	 */
	private boolean checkBrowerIsNotIE(){
		String browserType = "";
		Object brower_type = ContextHolderUtils.getSession().getAttribute("brower_type");
		if(brower_type==null){
			 	HttpServletRequest req = ContextHolderUtils.getRequest();
		        Cookie[] cookies = req.getCookies();
		        for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if("BROWSER_TYPE".equals(cookie.getName())){
						browserType = cookie.getValue();
					}
				}
		}else{
			browserType = brower_type.toString();
		}
		if(!"Microsoft%20Internet%20Explorer".equals(browserType)){
			return true;
		}
		return false;
	}

	/**
	 * 列表中是否有分组的按钮
	 * @return
	 */
	public boolean hasGroup(){
		boolean flag = false;
		for (DataGridColumn column : columnList) {
			if (StringUtil.isNotEmpty(column.getField()) && column.getField().equals("opt")){
				if(column.isOptsMenu()){
					return false;	
				}
			}
		}
		for (DataGridUrl dataGridUrl : urlList) {
			if(dataGridUrl.isInGroup()){
				flag = true;
				break;
			}
		}
		return flag;
	}

	public String getFields() {
		return fields;
	}
	public void setFields(String fields) {
		this.fields = fields;
	}
	public String getSearchFields() {
		return searchFields;
	}
	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}
	public List<DataGridUrl> getUrlList() {
		return urlList;
	}
	public void setUrlList(List<DataGridUrl> urlList) {
		this.urlList = urlList;
	}
	public List<DataGridUrl> getToolBarList() {
		return toolBarList;
	}
	public void setToolBarList(List<DataGridUrl> toolBarList) {
		this.toolBarList = toolBarList;
	}
	public List<DataGridColumn> getColumnList() {
		return columnList;
	}
	public void setColumnList(List<DataGridColumn> columnList) {
		this.columnList = columnList;
	}
	public List<ColumnValue> getColumnValueList() {
		return columnValueList;
	}
	public void setColumnValueList(List<ColumnValue> columnValueList) {
		this.columnValueList = columnValueList;
	}
	public List<ColumnValue> getColumnStyleList() {
		return columnStyleList;
	}
	public void setColumnStyleList(List<ColumnValue> columnStyleList) {
		this.columnStyleList = columnStyleList;
	}
	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	public int getCurPageNo() {
		return curPageNo;
	}
	public void setCurPageNo(int curPageNo) {
		this.curPageNo = curPageNo;
	}
	public Map<String, Object> getTableData() {
		return tableData;
	}
	public void setTableData(Map<String, Object> tableData) {
		this.tableData = tableData;
	}
	public String getName() {
		return name;
	}
	public String getTitle() {
		return title;
	}
	public String getIdField() {
		return idField;
	}
	public boolean isTreegrid() {
		return treegrid;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public int getPageSize() {
		return pageSize;
	}
	public boolean isPagination() {
		return pagination;
	}
	public String getWidth() {
		return width;
	}
	public String getHeight() {
		return height;
	}
	public boolean isCheckbox() {
		return checkbox;
	}
	public boolean isShowPageList() {
		return showPageList;
	}
	public boolean isOpenFirstNode() {
		return openFirstNode;
	}
	public boolean isFit() {
		return fit;
	}
	public boolean isShowRefresh() {
		return showRefresh;
	}
	public boolean isShowText() {
		return showText;
	}
	public String getStyle() {
		return style;
	}
	public String getOnLoadSuccess() {
		return onLoadSuccess;
	}
	public String getOnClick() {
		return onClick;
	}
	public String getOnDblClick() {
		return onDblClick;
	}
	public String getRowStyler() {
		return rowStyler;
	}
	public String getExtendParams() {
		return extendParams;
	}
	public String getLangArg() {
		return langArg;
	}
	public boolean isNowrap() {
		return nowrap;
	}
	public Boolean getSingleSelect() {
		return singleSelect;
	}
	public String getTreeField() {
		return treeField;
	}
	public String getComponent() {
		return component;
	}
	public void setShowSearch(boolean isShowSearch) {
		this.isShowSearch = isShowSearch;
	}
	public void setShowSubGrid(boolean isShowSubGrid) {
		this.isShowSubGrid = isShowSubGrid;
	}
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	
	public DataGridTag getDataGridTag(){
		return this;
	}

}
