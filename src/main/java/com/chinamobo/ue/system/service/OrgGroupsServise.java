package com.chinamobo.ue.system.service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.commodity.entity.TomCommodity;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.common.servise.TreeServise;
import com.chinamobo.ue.exam.dao.TomTopicMapper;
import com.chinamobo.ue.exam.dao.TomTopicOptionMapper;
import com.chinamobo.ue.exam.entity.TomTopic;
import com.chinamobo.ue.exam.entity.TomTopicOption;
import com.chinamobo.ue.exception.EleException;
import com.chinamobo.ue.system.dao.TomDeptMapper;
import com.chinamobo.ue.system.dao.TomOrgGroupsMapper;
import com.chinamobo.ue.system.dao.TomOrgMapper;
import com.chinamobo.ue.system.entity.TomDept;
import com.chinamobo.ue.system.entity.TomEmpOne;
import com.chinamobo.ue.system.entity.TomOrgGroups;
import com.chinamobo.ue.system.entity.TomRole;
import com.chinamobo.ue.system.entity.TomRoleScopes;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.ums.shiro.ShiroUser;
import com.chinamobo.ue.ums.util.ShiroUtils;
import com.chinamobo.ue.utils.Config;
import com.chinamobo.ue.utils.MapManager;

@Service
public class OrgGroupsServise {
	@Autowired
	private TomOrgGroupsMapper tomOrgGroupsMapper;
	@Autowired
	private TomOrgMapper tomOrgMapper;
	@Autowired
	private TomDeptMapper tomDeptMapper;
	@Autowired
	private OrgServise orgServise;
	@Autowired
	private DeptServise deptServise;
	@Autowired
	private TreeServise treeServise;
	@Autowired
	private RoleScopesServise roleScopesServise;
	
	/**
	 * 
	 * 功能描述：[查询所有集团]
	 *
	 * 创建者：cjx 创建时间: 2016年3月1日 下午2:40:10
	 * 
	 * @return
	 */
	public List<Tree> select() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return tomOrgGroupsMapper.select();
	}

	public List selectAllDeOrg() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List list = new ArrayList();
		list.add(tomDeptMapper.select());
		list.add(tomOrgMapper.select());
		return list;
	}

	public List<Tree> tree() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<Tree> tomOrgGroupsList = new ArrayList<Tree>();
		List<Tree> select = select();

		for (Tree tomOrgGroups : select) {
			List<Tree> selectByGrp_Code = orgServise.selectByGrp_Code(tomOrgGroups.getCode()); // 查询集团下所有公司
			List list = new ArrayList();

			for (Tree tomorg : selectByGrp_Code) {
				if (tomorg.getFathercode() == null) {
					Tree t = orgServise.selectByTree(tomorg.getCode()); // 查询公司信息
					Tree tomOr = treeServise.recursiveTreeForOrg(t, selectByGrp_Code); // 查询公司下所有子公司和子公司下部门
					List<Tree> listTree = new ArrayList<Tree>();
					for (TomDept tomDept : ContextInitRedisService.deptList) { // 查询所有部门
						Tree t1 = new Tree();
						if (tomorg.getCode().equals(tomDept.getPkOrg())) {
							t1.setCode(tomDept.getCode());
							t1.setName(tomDept.getName());
							t1.setTopname(tomDept.getTopname());
							t1.setOrgcode(tomDept.getOrgcode());
							if (null != tomDept.getTopcode()) {
								t1.setFathercode(tomDept.getTopcode());
							}
							t1.setStatuss("3");
							listTree.add(t1);
						}
					}

					if (!listTree.isEmpty()) {
						for (Tree tom : listTree) {

							if (tom.getFathercode() == null) {
								Tree t1 = new Tree();
								for (TomDept tomDept : ContextInitRedisService.deptList) {
									if (tomDept.getCode().equals(tom.getCode())) {
										t1.setCode(tomDept.getCode());
										t1.setFathercode(tomDept.getPkOrg());
										t1.setName(tomDept.getName());
										t1.setTopname(tomDept.getTopname());
										t1.setOrgcode(tomDept.getOrgcode());
										t1.setStatuss("3");
									}
								}
								Tree tomDept = treeServise.recursiveTree(t1, listTree);
								tomOr.getChildren().add(tomDept);
							}
						}

					}

					list.add(tomOr);
					tomOrgGroups.setChildren(list);
				}
			}
			tomOrgGroupsList.add(tomOrgGroups);
		}
		return tomOrgGroupsList;

	}

	public List<Tree> tree2() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		List<Tree> tomOrgGroupsList = new ArrayList<Tree>();
		List<Tree> select = select();

		for (Tree tomOrgGroups : select) {
			List<Tree> selectByGrp_Code = orgServise.selectByGrp_Code(tomOrgGroups.getCode());
			List list = new ArrayList();
			for (Tree tomorg : selectByGrp_Code) {
				list.add(tomorg);
				tomOrgGroups.setChildren(list);
			}

			tomOrgGroupsList.add(tomOrgGroups);
		}
		return tomOrgGroupsList;
	}

	// 选择人员树
	public List<Tree> selectEmp() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		ShiroUser user = ShiroUtils.getCurrentUser();
		List<Tree> tomOrgGroupsList = new ArrayList<Tree>();
		// 获得管理员角色
		List<TomRole> tomRoleList = user.getRoles();// 获得员工角色
		if (null != tomRoleList) {
			for (TomRole tomRole : tomRoleList) {
				TomRoleScopes tomRoleScopes = roleScopesServise.selectScopeByRoilId(tomRole.getRoleId());// 查找角色范围
				String scopes = tomRoleScopes.getRoleScope();
				String[] scopess = scopes.split(",");
				if (tomRole.getRoleType().equals("1")) { // 判断角色级别（1.公司、2.部门）
					for (int i = 0; i < scopess.length; i++) {
						Tree tomorg = orgServise.selectByTree(scopess[i]); // 查找公司树对象
						List<Tree> allOrg = tomOrgMapper.selectByAllOrgTree(); // 查找所有公司
						int j = 0;
						for (Tree tomOrgGroup : tomOrgGroupsList) {
							if (tomorg.getName().equals(tomOrgGroup.getName())) {// 判断公司是否已经存在
								j++;
							}
						}
						if (j == 0) {
							Tree tomOr = treeServise.recursiveTreeForOrg(tomorg, allOrg); // 查看公司下所有子公司与部门
							List<Tree> list = new ArrayList<Tree>();
							for (TomDept tomDept : ContextInitRedisService.deptList) {
								Tree t1 = new Tree();
								if (tomorg.getCode().equals(tomDept.getPkOrg())) {
									t1.setCode(tomDept.getCode());
									t1.setName(tomDept.getName());
									if (null != tomDept.getTopcode()) {
										t1.setFathercode(tomDept.getTopcode());
									}
									t1.setStatuss("3");
									list.add(t1);
								}
							}
							if (!list.isEmpty()) {
								List list1 = new ArrayList();
								List list2 = new ArrayList();
								for (Tree tom : list) {

									if (tom.getFathercode() == null) {
										Tree t1 = new Tree();
										for (TomDept tomDept : ContextInitRedisService.deptList) {
											if (tomDept.getCode().equals(tom.getCode())) {
												t1.setCode(tomDept.getCode());
												t1.setFathercode(tomDept.getPkOrg());
												t1.setName(tomDept.getName());
												t1.setStatuss("3");
											}
										}
										// Tree t1 =
										// deptServise.selectByPkDept(tom.getCode());
										Tree tomDept = treeServise.recursiveTree(t1, list);// 部门下所有子部门
										tomOr.getChildren().add(tomDept);
									}
								}

							}
							tomOrgGroupsList.add(tomOr);
						}
					}
				}
			}
			for (TomRole tomRole : tomRoleList) {
				TomRoleScopes tomRoleScopes = roleScopesServise.selectScopeByRoilId(tomRole.getRoleId());
				String scopes = tomRoleScopes.getRoleScope();
				String[] scopess = scopes.split(",");
				if (tomRole.getRoleType().equals("2")) {
					for (int i = 0; i < scopess.length; i++) {
						Tree tomorg = orgServise.selectOrgByDeptCode(scopess[i].toString());
						int j = 0;
						for (Tree tomOrgGroup : tomOrgGroupsList) {
							if (tomorg.getName().equals(tomOrgGroup.getName())) {// 判断公司是否已经存在
								j++;
							}
						}
						if (j == 0) {
							Tree t1 = deptServise.selectByPkDept(scopess[i].toString());
							Tree tomDept = treeServise.recursiveTree(t1, ContextInitRedisService.deptHaveEmp);
							tomOrgGroupsList.add(tomDept);
						}
					}
				}
			}

		}
		return tomOrgGroupsList;
	}

	public List<Tree> selectByScope() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		ShiroUser user = ShiroUtils.getCurrentUser();
		List<Tree> tomOrgGroupsList = new ArrayList<Tree>();
		// 获得管理员角色
		List<TomRole> tomRoleList = user.getRoles();
		if (null != tomRoleList) {
			for (TomRole tomRole : tomRoleList) {
				TomRoleScopes tomRoleScopes = roleScopesServise.selectScopeByRoilId(tomRole.getRoleId());
				String scopes = tomRoleScopes.getRoleScope();
				String[] scopess = scopes.split(",");
				if (tomRole.getRoleType().equals("1")) {
					for (int i = 0; i < scopess.length; i++) {
						Tree tomorg = orgServise.selectByTree(scopess[i]);
						List<Tree> allOrg = tomOrgMapper.selectByAllOrgTree(); // 查找所有公司
						Tree tomOr = treeServise.recursiveTreeForOrg(tomorg, allOrg); // 查看公司下所有子公司与部门
						int j = 0;
						for (Tree tomOrgGroup : tomOrgGroupsList) {
							if (tomorg.getName().equals(tomOrgGroup.getName())) {
								j++;
							}
						}
						if (j == 0) {
							List<Tree> list = new ArrayList<Tree>();
							for (TomDept tomDept : ContextInitRedisService.deptList) {
								Tree t1 = new Tree();
								if (tomorg.getCode().equals(tomDept.getPkOrg())) {
									t1.setCode(tomDept.getCode());
									t1.setName(tomDept.getName());
									if (null != tomDept.getTopcode()) {
										t1.setFathercode(tomDept.getTopcode());
									}
									t1.setStatuss("3");
									list.add(t1);
								}
							}
							if (!list.isEmpty()) {
								List list1 = new ArrayList();
								List list2 = new ArrayList();
								for (Tree tom : list) {

									if (null == tom.getFathercode()) {
										Tree t1 = new Tree();
										for (TomDept tomDept : ContextInitRedisService.deptList) {
											if (tomDept.getCode().equals(tom.getCode())) {
												t1.setCode(tomDept.getCode());
												t1.setFathercode(tomDept.getPkOrg());
												t1.setName(tomDept.getName());
												t1.setStatuss("3");
											}
										}
										Tree tomDept = treeServise.recursiveTree(t1, list);
										tomOr.getChildren().add(tomDept);
									}
								}

							}
							tomOrgGroupsList.add(tomOr);
						}
					}
				}
			}
			for (TomRole tomRole : tomRoleList) {
				TomRoleScopes tomRoleScopes = roleScopesServise.selectScopeByRoilId(tomRole.getRoleId());
				String scopes = tomRoleScopes.getRoleScope();
				String[] scopess = scopes.split(",");
				if (tomRole.getRoleType().equals("2")) {
					for (int i = 0; i < scopess.length; i++) {
						Tree tomorg = orgServise.selectOrgByDeptCode(scopess[i].toString());
						int j = 0;
						for (Tree tomOrgGroup : tomOrgGroupsList) {
							if (tomorg.getName().equals(tomOrgGroup.getName())) {// 判断公司是否已经存在
								j++;
							}
						}
						if (j == 0) {
							Tree t1 = deptServise.selectByPkDept(scopess[i].toString());
							Tree tomDept = treeServise.recursiveTree(t1, ContextInitRedisService.deptHaveEmp);
							tomOrgGroupsList.add(tomDept);
						}

					}
				}
			}

		}
		return tomOrgGroupsList;

	}

	public List<Tree> selectByScope2() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		ShiroUser user = ShiroUtils.getCurrentUser();
		List<Tree> tomOrgGroupsList = new ArrayList<Tree>();
		// 获得管理员角色
		List<TomRole> tomRoleList = user.getRoles();
		if (null != tomRoleList) {
			for (TomRole tomRole : tomRoleList) {
				if ("1".equals(tomRole.getRoleType())) {
					TomRoleScopes tomRoleScopes = roleScopesServise.selectScopeByRoilId(tomRole.getRoleId());
					String scopes = tomRoleScopes.getRoleScope();
					String[] scopess = scopes.split(",");
					for (int i = 0; i < scopess.length; i++) {
						Tree tomorg = orgServise.selectByTree(scopess[i]);
						List<Tree> allOrg = tomOrgMapper.selectByAllOrgTree();
						Tree org = treeServise.recursiveTree(tomorg, allOrg);
						int j = 0;
						for (Tree tomOrgGroup : tomOrgGroupsList) {
							if (tomorg.getName().equals(tomOrgGroup.getName())) {
								j++;
							}
						}
						if (j == 0) {
							tomOrgGroupsList.add(org);
						}
					}
				}
			}
		}
		return tomOrgGroupsList;
	}
	
	//根据code查询集团信息
	public void selectOrgGroups(String code){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		tomOrgGroupsMapper.selectByPrimaryKey(code);
	}
	
	//查询集团全表
	public void selectGroups(){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		tomOrgGroupsMapper.selectPrimary();
	}
}
