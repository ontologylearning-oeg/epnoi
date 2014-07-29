package org.epnoi.uia.rest.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.InformationSourceNotification;
import org.epnoi.model.InformationSourceNotificationsSet;
import org.epnoi.model.Resource;
import org.epnoi.uia.informationstore.InformationStore;
import org.epnoi.uia.informationstore.dao.rdf.AnnotationRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.InformationSourceSubscriptionRDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.rdf.UserRDFHelper;
import org.epnoi.uia.rest.services.response.UIA;

@Path("/uia")
public class UIAResource extends UIAService {

	@Context
	ServletContext context;

	Map<String, Class<? extends Resource>> knownDeserializableClasses = new HashMap<>();

	private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
	static {
		resourceTypesTable.put("papers", RDFHelper.PAPER_CLASS);
		resourceTypesTable.put("users", UserRDFHelper.USER_CLASS);
		resourceTypesTable.put("informationsources",
				InformationSourceRDFHelper.INFORMATION_SOURCE_CLASS);
		resourceTypesTable
				.put("informationsourcesubscriptions",
						InformationSourceSubscriptionRDFHelper.INFORMATION_SOURCE_SUBSCRIPTION_CLASS);
		resourceTypesTable.put("researchobjects",
				RDFHelper.RESEARCH_OBJECT_CLASS);
		resourceTypesTable.put("annotations",
				AnnotationRDFHelper.ANNOTATION_CLASS);
	}

	// --------------------------------------------------------------------------------

	@PostConstruct
	public void init() {

		logger = Logger.getLogger(UIAResource.class.getName());
		logger.info("Initializing "+getClass());
		this.core = this.getUIACore();

	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/status")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getUIA() {
		System.out.println("GET: UIA");

		UIA uia = new UIA();
		
		String timeStamp = Long.toString(System.currentTimeMillis());
		uia.setTimestamp(timeStamp);

		for (InformationStore informationStore : this.core
				.getInformationStores()) {
			org.epnoi.uia.rest.services.response.InformationStore informationStoreResponse = new org.epnoi.uia.rest.services.response.InformationStore();

			informationStoreResponse
					.setInformationStoreParameters(informationStore
							.getParameters());
			informationStoreResponse.setStatus(informationStore.test());
			uia.addInformationStores(informationStoreResponse);

		}

		if (uia != null) {
			return Response.ok(uia, MediaType.APPLICATION_JSON).build();
		}
		return Response.status(404).build();
	}

	// --------------------------------------------------------------------------------

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/resources/{RESOURCE_TYPE}")
	public void updateResource(Resource resource,
			@PathParam("RESOURCE_TYPE") String resourceType) {
		System.out.println("POST: UIA " + resource);
		
		this.getUIACore().getInformationAccess().update(resource);

	}
	
	// --------------------------------------------------------------------------------

		@PUT 
		@Consumes({ MediaType.APPLICATION_JSON })
		@Path("/resources/{RESOURCE_TYPE}")
		public void createResource(Resource resource,
				@PathParam("RESOURCE_TYPE") String resourceType) {
			System.out.println("PUT: UIA " + resource);
			
			this.getUIACore().getInformationAccess().put(resource, org.epnoi.model.Context.emptyContext);

		}
	

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/resources/{RESOURCE_TYPE}")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getResource(@QueryParam("uri") String URI,
			@PathParam("RESOURCE_TYPE") String resourceType) {
		System.out.println("GET: UIA uri> " + URI + " reourceType > "
				+ resourceType);

		String resourceClass = UIAResource.resourceTypesTable.get(resourceType);
		if ((URI != null) && (resourceClass != null)) {

			this.core = this.getUIACore();
			System.out.println("Getting the resource "
					+ UIAResource.resourceTypesTable.get(resourceType));
			Resource resource = this.core.getInformationAccess().get(URI,
					resourceClass);

			if (resource != null) {
				return Response.ok(resource, MediaType.APPLICATION_JSON)
						.build();
			}
		}
		return Response.status(404).build();
	}

	// --------------------------------------------------------------------------------

	@DELETE
	@Consumes({ MediaType.APPLICATION_JSON })
	@Path("/resources/{RESOURCE_TYPE}")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response deleteResource(@QueryParam("uri") String URI,
			@PathParam("RESOURCE_TYPE") String resourceType) {
		System.out.println("DELETE: UIA uri> " + URI + " reourceType > "
				+ resourceType);
		String resourceClass = UIAResource.resourceTypesTable.get(resourceType);
		if ((URI != null) && (resourceClass != null)) {
			this.core.getInformationAccess().remove(URI, resourceType);
		}
		return Response.status(404).build();
	}

	// --------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/notifications")
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response getResource(
			@DefaultValue("none") @QueryParam("URI") String URI) {
		System.out.println("GET: UIA");
		
	
		List<InformationSourceNotification> notifications = new ArrayList<InformationSourceNotification>();

		for (InformationSourceNotification notification : core
				.getInformationSourcesHandler().retrieveNotifications(URI)) {

			notifications.add(notification);
		}

		InformationSourceNotificationsSet notificationsSet = new InformationSourceNotificationsSet();

		notificationsSet.setNotifications(notifications);
		notificationsSet.setURI(URI);

		return Response.ok(notificationsSet, MediaType.APPLICATION_JSON)
				.build();
	}


}
