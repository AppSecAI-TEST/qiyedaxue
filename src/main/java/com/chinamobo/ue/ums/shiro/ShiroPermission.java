package com.chinamobo.ue.ums.shiro;

import java.io.Serializable;

public class ShiroPermission implements Serializable{
	/** 成员描述：TODO */
	private static final long serialVersionUID = -671524883117558528L;
	private String roleid;
	private String orgdeptcode;
	private String roletype;
	private String authorityid;
	private String authorityname;
	private String authoritytype;
	private String authorityurl;
	private String authoritymethod;
	public String getroleid() {
		return roleid;
	}
	public void setroleid(String roleid) {
		this.roleid = roleid;
	}
	public String getOrgdeptcode() {
		return orgdeptcode;
	}
	public void setOrgdeptcode(String orgdeptcode) {
		this.orgdeptcode = orgdeptcode;
	}
	public String getRoletype() {
		return roletype;
	}
	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}
	public String getAuthorityid() {
		return authorityid;
	}
	public void setAuthorityid(String authorityid) {
		this.authorityid = authorityid;
	}
	public String getAuthorityname() {
		return authorityname;
	}
	public void setAuthorityname(String authorityname) {
		this.authorityname = authorityname;
	}
	public String getAuthoritytype() {
		return authoritytype;
	}
	public void setAuthoritytype(String authoritytype) {
		this.authoritytype = authoritytype;
	}
	public String getAuthorityurl() {
		return authorityurl;
	}
	public void setAuthorityurl(String authorityurl) {
		this.authorityurl = authorityurl;
	}
	public String getAuthoritymethod() {
		return authoritymethod;
	}
	public void setAuthoritymethod(String authoritymethod) {
		this.authoritymethod = authoritymethod;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((authorityid == null) ? 0 : authorityid.hashCode());
		result = prime * result + ((authoritymethod == null) ? 0 : authoritymethod.hashCode());
		result = prime * result + ((authorityname == null) ? 0 : authorityname.hashCode());
		result = prime * result + ((authoritytype == null) ? 0 : authoritytype.hashCode());
		result = prime * result + ((authorityurl == null) ? 0 : authorityurl.hashCode());
		result = prime * result + ((orgdeptcode == null) ? 0 : orgdeptcode.hashCode());
		result = prime * result + ((roletype == null) ? 0 : roletype.hashCode());
		result = prime * result + ((roleid == null) ? 0 : roleid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShiroPermission other = (ShiroPermission) obj;
		if (authorityid == null) {
			if (other.authorityid != null)
				return false;
		} else if (!authorityid.equals(other.authorityid))
			return false;
		if (authoritymethod == null) {
			if (other.authoritymethod != null)
				return false;
		} else if (!authoritymethod.equals(other.authoritymethod))
			return false;
		if (authorityname == null) {
			if (other.authorityname != null)
				return false;
		} else if (!authorityname.equals(other.authorityname))
			return false;
		if (authoritytype == null) {
			if (other.authoritytype != null)
				return false;
		} else if (!authoritytype.equals(other.authoritytype))
			return false;
		if (authorityurl == null) {
			if (other.authorityurl != null)
				return false;
		} else if (!authorityurl.equals(other.authorityurl))
			return false;
		if (orgdeptcode == null) {
			if (other.orgdeptcode != null)
				return false;
		} else if (!orgdeptcode.equals(other.orgdeptcode))
			return false;
		if (roletype == null) {
			if (other.roletype != null)
				return false;
		} else if (!roletype.equals(other.roletype))
			return false;
		if (roleid == null) {
			if (other.roleid != null)
				return false;
		} else if (!roleid.equals(other.roleid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ShiroPermission [roleid=" + roleid + ", orgdeptcode=" + orgdeptcode + ", roletype=" + roletype
				+ ", authorityid=" + authorityid + ", authorityname=" + authorityname + ", authoritytype="
				+ authoritytype + ", authorityurl=" + authorityurl
				+ ", authoritymethod=" + authoritymethod + "]";
	}
	
}
