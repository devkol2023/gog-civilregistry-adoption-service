package com.gog.civilregistry.adoption.util;

public class SqlConstants {
	
	public static final String GET_GLOBAL_CONFIG = "SELECT admin_config_id, \"name\", value, text1 from m_global_config where (? = '' or name =?)";
	
	public static final String APPLICATION_VAULT_QUERY = "SELECT * FROM marriage.fn_get_application_vault_adoption(CAST(:pApplicationTypeCode AS VARCHAR), CAST(:pState AS VARCHAR), CAST(:pUserId AS INTEGER), CAST(:pInstituteId AS INTEGER), CAST(:pRoleId AS INTEGER), CAST(:pPageNumber AS INTEGER), CAST(:pPerPageCount AS INTEGER), CAST(:pSearchText AS VARCHAR))";

}
