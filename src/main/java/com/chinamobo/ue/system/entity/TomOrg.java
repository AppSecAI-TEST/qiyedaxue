package com.chinamobo.ue.system.entity;

import java.util.List;

import javax.ws.rs.FormParam;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TomOrg {
	@FormParam("code")
    private String code;
	@FormParam("name")
    private String name;
	@JsonProperty("pk_org")
	@FormParam("pkOrg")
    private String pkOrg;
//	@JsonProperty("pk_fatherorg")
	@FormParam("pkFatherorg")
    private String pkFatherorg;
	@FormParam("coenablestatede")
    private String enablestate;

	private List<TomOrg> Org = Lists.newArrayList();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg == null ? null : pkOrg.trim();
    }

    public String getPkFatherorg() {
        return pkFatherorg;
    }

    public void setPkFatherorg(String pkFatherorg) {
        this.pkFatherorg = pkFatherorg == null ? null : pkFatherorg.trim();
    }

    public String getEnablestate() {
        return enablestate;
    }

    public void setEnablestate(String enablestate) {
        this.enablestate = enablestate == null ? null : enablestate.trim();
    }

	public List<TomOrg> getOrg() {
		return Org;
	}

	public void setOrg(List<TomOrg> org) {
		Org = org;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TomOrg [code=");
		builder.append(code);
		builder.append(", name=");
		builder.append(name);
		builder.append(", pkOrg=");
		builder.append(pkOrg);
		builder.append(", pkFatherorg=");
		builder.append(pkFatherorg);
		builder.append(", enablestate=");
		builder.append(enablestate);
		builder.append(", Org=");
		builder.append(Org);
		builder.append("]");
		return builder.toString();
	}
    
    
}