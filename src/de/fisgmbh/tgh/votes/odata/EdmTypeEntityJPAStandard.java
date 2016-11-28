package de.fisgmbh.tgh.votes.odata;

import java.util.Map;

import org.apache.olingo.odata2.api.uri.info.DeleteUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.api.uri.info.PutMergePatchUriInfo;

import de.fisgmbh.tgh.odata.FisEdmTypeEntity;

public abstract class EdmTypeEntityJPAStandard extends FisEdmTypeEntity {

	public EdmTypeEntityJPAStandard(String et_name) {
		super(et_name);
	}

	@Override
	public boolean havePermissionToReadEntity(GetEntityUriInfo uriInfo) {
		return true;
	}

	@Override
	public boolean havePermissionToReadEntitySet(GetEntitySetUriInfo uriInfo) {
		return true;
	}

	@Override
	public boolean havePermissionToCreateEntity(PostUriInfo uriInfo, Map<String, Object> json) {
		if (((CustomODataRequestContext)this.getODataRequestContext()).isManager()){
			return true;
		}
		return false;
	}

	@Override
	public boolean havePermissionToUpdateEntity(PutMergePatchUriInfo uriInfo, Map<String, Object> json) {
		if (((CustomODataRequestContext)this.getODataRequestContext()).isManager()){
			return true;
		}
		return false;
	}

	@Override
	public boolean havePermissionToDeleteEntity(DeleteUriInfo uriInfo) {
		if (((CustomODataRequestContext)this.getODataRequestContext()).isManager()){
			return true;
		}
		return false;
	}

}
