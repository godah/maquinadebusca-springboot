package com.maquinadebusca.app;

public enum RoleEnum {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	private String label;
	
	RoleEnum(String string){
		this.label = string;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
