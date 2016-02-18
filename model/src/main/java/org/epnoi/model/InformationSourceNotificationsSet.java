package org.epnoi.model;

import org.epnoi.model.domain.resources.Resource;

import java.util.ArrayList;
import java.util.List;



public class InformationSourceNotificationsSet extends Resource {

	private String uri;
	private String timestamp;
	private List<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();

	// ----------------------------------------------------------------------

	public String getUri() {
		return uri;
	}

	// ----------------------------------------------------------------------

	public void setUri(String uri) {
		uri = uri;
	}

	// ----------------------------------------------------------------------

	public String getTimestamp() {
		return timestamp;
	}

	// ----------------------------------------------------------------------

	public List<InformationSourceNotification> getNotifications() {
		return notifications;
	}

	// ----------------------------------------------------------------------

	public void setNotifications(
			List<InformationSourceNotification> notifications) {
		this.notifications = notifications;
	}

	// ----------------------------------------------------------------------

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "InformationSourceNotificationsSet [URI=" + uri + ", timestamp="
				+ timestamp + ", notifications=" + notifications + "]";
	}


	@Override
	public Type getResourceType() {
		return null;
	}
}
