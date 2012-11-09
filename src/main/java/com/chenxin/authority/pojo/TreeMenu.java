package com.chenxin.authority.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 菜单类1.1
 * 
 * @author Maty Chen
 * @date 2011-10-25 下午9:53:59
 */
public class TreeMenu implements Serializable {
	private static final long serialVersionUID = -1837699104579478927L;
	private List<BaseModule> list;
	private BaseModule root;

	public TreeMenu(List<BaseModule> list) {
		this.list = list;
		this.root = new BaseModule();
	}

	/**
	 * 组合json
	 * 
	 * @param list
	 * @param node
	 */
	private Tree getNodeJson(List<BaseModule> list, BaseModule node) {
		Tree tree = new Tree();
		tree.setId("_authority_" + node.getId());
		tree.setText(node.getModuleName());
		tree.setIconCls(node.getIconCss());
		tree.setChildren(new ArrayList<Tree>());
		if (list == null) {
			// 防止没有权限菜单时
			return tree;
		}
		if (hasChild(list, node)) {
			List<Tree> lt = new ArrayList<Tree>();
			tree.setUrl("");
			tree.setLeaf(node.getLeaf() == 1 ? true : false);
			tree.setExpanded(node.getExpanded() == 1 ? true : false);
			List<BaseModule> childList = getChildList(list, node);
			Iterator<BaseModule> it = childList.iterator();
			while (it.hasNext()) {
				BaseModule modules = it.next();
				// 递归
				lt.add(getNodeJson(list, modules));
			}
			tree.setChildren(lt);
			// } else if ((node.getParentId() == root.getModuleId()) ||
			// node.getModuleUrl() == null) {
			// // 防止是主菜单,或者主菜单里面的url为空，但是下面没有子菜单的时候
			// tree.setUrl("");
			// tree.setLeaf(node.getLeaf() == 1 ? true : false);
			// tree.setExpanded(node.getExpanded() == 1 ? true : false);
		} else {
			tree.setUrl(node.getModuleUrl());
			tree.setLeaf(node.getLeaf() == 1 ? true : false);
			tree.setExpanded(node.getExpanded() == 1 ? true : false);
		}
		return tree;
	}

	/**
	 * 判断是否有子节点
	 */
	private boolean hasChild(List<BaseModule> list, BaseModule node) {
		return getChildList(list, node).size() > 0 ? true : false;
	}

	/**
	 * 得到子节点列表
	 */
	private List<BaseModule> getChildList(List<BaseModule> list, BaseModule modules) {
		List<BaseModule> li = new ArrayList<BaseModule>();
		Iterator<BaseModule> it = list.iterator();
		while (it.hasNext()) {
			BaseModule temp = it.next();
			if (modules.getId().equals(temp.getParentUrl())) {
				li.add(temp);
			}
		}
		return li;
	}

	public Tree getTreeJson() {
		// 父菜单的id为0
		this.root.setId(0L);
		this.root.setLeaf(0);
		this.root.setExpanded(0);
		return this.getNodeJson(this.list, this.root);
	}
}
