package core.sakai.objects;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import GUI.VersionUtil;
import GUI.myDialog;
import control.AnnouncementAdd;
import control.AssignmentAdd;
import control.LocalConstants;
import core.sakai.objects.SakaiAssignment.SakaiAssignmentContent;
import core.sakai.serviceWrapper.AnnouncementServices;
import core.sakai.serviceWrapper.AssignmentServices;

@XmlRootElement(name = "site")
public class SakaiSite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7970570717621854997L;
	private transient AssignmentAdd handler; 
	private transient AnnouncementAdd announcementHandler;
	
	public void addAssignmentAddHandler(AssignmentAdd handler)
	{
		this.handler = handler;
	}
	
	public void addAnnouncementHandler(AnnouncementAdd handler)
	{
		this.announcementHandler = handler;
	}
	
	public HashMap<String, SakaiAssignmentContent> assignments; 
	public HashMap<String, SakaiAssignmentContent> getAllAssignments()
	{
		return assignments;
	}
	
	private HashMap<String, SakaiAnnouncement> announcements = new HashMap<String, SakaiAnnouncement>();
	
	public void setAllAnnouncements(HashMap<String, SakaiAnnouncement> announcements)
	{
		this.announcements = announcements;
	}
	
	public HashMap<String, SakaiAnnouncement> getAllAnnouncements()
	{
		return this.announcements;
	}
	
	public void updataAnnouncement() throws IOException, JAXBException
	{
		SakaiAnnouncement[] rawAnnouncement = 
				AnnouncementServices.getAnnouncementsForSite
				(this.getId(), LocalConstants.sessionID, SakaiConstants.SERVER_URL);
		
		for(SakaiAnnouncement ann : rawAnnouncement)
		{
//			System.out.println(ann.toString());
			if(announcements == null) {
				announcements = new HashMap<String, SakaiAnnouncement>();
			}
//			announcements.keySet();
//			announcements.keySet().contains("");
//			ann.getTitle();
			if(!announcements.keySet().contains(ann.getTitle()))
			{
				System.out.println("New");
				new VersionUtil("新的通知", "站点：" + this.getTitle(), 
						ann.getTitle() + "\n" + ann.getBody());
				if(this.announcementHandler != null)
				{
					announcementHandler.newAnnouncement(ann);
				}
				
				this.announcements.put(ann.getTitle(), ann);
			}
		}
	}
	
	public void updateAssignment() throws IOException, JAXBException, ParserConfigurationException, SAXException
	{
		
		try
		{
			SakaiAssignment[] rawAssignment = 
					AssignmentServices.getAssignmentsForSite
					(this.getId(), LocalConstants.sessionID, SakaiConstants.SERVER_URL);
			for(SakaiAssignment ass:rawAssignment)
			{
				if(!assignments.keySet().contains(ass.getTitle()))
				{					
					SakaiAssignmentContent content= AssignmentServices.getAssignmentContent
					(SakaiConstants.SERVER_URL, this.getId(), ass.getId(), LocalConstants.sessionID);
					
					new VersionUtil("新作业", "站点:" + this.getTitle(),
							this.getTitle() + "\n" + this.getDescription());
					if(handler != null)
					{
						handler.newAssignment(content);
					}
					
					assignments.put(content.getTitle(), content);
				}
			}
		}
		catch(RemoteException e)
		{
			return;
		}
		
		
	}
	
	@XmlRootElement(name = "props")
	public static class Props implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String term_eid;
		private String contact_name;
		boolean sections_student_registration_allowed;
		private String term;
		private boolean sections_student_switching_allowed;
		private boolean sections_externally_maintained;
		private String contact_email;
		public String getTerm_eid() {
			return term_eid;
		}
		public void setTerm_eid(String term_eid) {
			this.term_eid = term_eid;
		}
		@XmlElement(name = "contact-name")
		public String getContact_name() {
			return contact_name;
		}
		public void setContact_name(String contact_name) {
			this.contact_name = contact_name;
		}
		public boolean isSections_student_registration_allowed() {
			return sections_student_registration_allowed;
		}
		public void setSections_student_registration_allowed(
				boolean sections_student_registration_allowed) {
			this.sections_student_registration_allowed = sections_student_registration_allowed;
		}
		public String getTerm() {
			return term;
		}
		public void setTerm(String term) {
			this.term = term;
		}
		public boolean isSections_student_switching_allowed() {
			return sections_student_switching_allowed;
		}
		public void setSections_student_switching_allowed(
				boolean sections_student_switching_allowed) {
			this.sections_student_switching_allowed = sections_student_switching_allowed;
		}
		public boolean isSections_externally_maintained() {
			return sections_externally_maintained;
		}
		public void setSections_externally_maintained(
				boolean sections_externally_maintained) {
			this.sections_externally_maintained = sections_externally_maintained;
		}
		@XmlElement(name = "contact-email")
		public String getContact_email() { return contact_email;
		}
		public void setContact_email(String contact_email) {
			this.contact_email = contact_email;
		}
		@Override
		public String toString() {
			return "Props [getTerm_eid()=" + getTerm_eid()
					+ ", getContact_name()=" + getContact_name()
					+ ", isSections_student_registration_allowed()="
					+ isSections_student_registration_allowed()
					+ ", getTerm()=" + getTerm()
					+ ", isSections_student_switching_allowed()="
					+ isSections_student_switching_allowed()
					+ ", isSections_externally_maintained()="
					+ isSections_externally_maintained()
					+ ", getContact_email()=" + getContact_email() + "]";
		}
	}
	
	@XmlRootElement(name = "siteOwner")
	public static class SiteOwner implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2571070037869338661L;
		private String userDisplayName;
		private String userEntityURL;
		private String userId;
		public String getUserDisplayName() {
			return userDisplayName;
		}
		public void setUserDisplayName(String userDisplayName) {
			this.userDisplayName = userDisplayName;
		}
		public String getUserEntityURL() {
			return userEntityURL;
		}
		public void setUserEntityURL(String userEntityURL) {
			this.userEntityURL = userEntityURL;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		@Override
		public String toString() {
			return "SiteOwner [getUserDisplayName()=" + getUserDisplayName()
					+ ", getUserEntityURL()=" + getUserEntityURL()
					+ ", getUserId()=" + getUserId() + "]";
		}
	}
	private Date createdDate;
	private SakaiTime createdTime;
	private String description;
	private String iconUrl;
	private String iconUrlFull;
	private String id;
	private String infoUrl;
	private String infoUrlFull;
	private String joinerRole;
	private long lastModified;
	private String maintainRole;
	private Date modifiedDate;
	private SakaiTime modifiedTime;
	private String owner;
	private Props props;
	private String providerGroupId;
	private String reference;
	private String shortDescription;
	private String siteGroups;
	private SiteOwner siteOwner;
	private String skin;
	private String softlyDeletedDate;
	private String title;
	private String type;
	private String[] userRoles;
	private boolean activeEdit;
	private boolean customPageOrdered;
	private boolean empty;
	private boolean joinable;
	private boolean pubView;
	private boolean published;
	private boolean softlyDeleted;
	private String entityReference;
	private String entityURL;
	private String entityId;
	private String entityTitle;
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public SakaiTime getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(SakaiTime createdTime) {
		this.createdTime = createdTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getIconUrlFull() {
		return iconUrlFull;
	}
	public void setIconUrlFull(String iconUrlFull) {
		this.iconUrlFull = iconUrlFull;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfoUrl() {
		return infoUrl;
	}
	public void setInfoUrl(String infoUrl) {
		this.infoUrl = infoUrl;
	}
	public String getInfoUrlFull() {
		return infoUrlFull;
	}
	public void setInfoUrlFull(String infoUrlFull) {
		this.infoUrlFull = infoUrlFull;
	}
	public String getJoinerRole() {
		return joinerRole;
	}
	public void setJoinerRole(String joinerRole) {
		this.joinerRole = joinerRole;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public String getMaintainRole() {
		return maintainRole;
	}
	public void setMaintainRole(String maintainRole) {
		this.maintainRole = maintainRole;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public SakaiTime getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(SakaiTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public Props getProps() {
		return props;
	}
	public void setProps(Props props) {
		this.props = props;
	}
	public String getProviderGroupId() {
		return providerGroupId;
	}
	public void setProviderGroupId(String providerGroupId) {
		this.providerGroupId = providerGroupId;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getSiteGroups() {
		return siteGroups;
	}
	public void setSiteGroups(String siteGroups) {
		this.siteGroups = siteGroups;
	}
	public SiteOwner getSiteOwner() {
		return siteOwner;
	}
	public void setSiteOwner(SiteOwner siteOwner) {
		this.siteOwner = siteOwner;
	}
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
	}
	public String getSoftlyDeletedDate() {
		return softlyDeletedDate;
	}
	public void setSoftlyDeletedDate(String softlyDeletedDate) {
		this.softlyDeletedDate = softlyDeletedDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(String[] userRoles) {
		this.userRoles = userRoles;
	}
	public boolean isActiveEdit() {
		return activeEdit;
	}
	public void setActiveEdit(boolean activeEdit) {
		this.activeEdit = activeEdit;
	}
	public boolean isCustomPageOrdered() {
		return customPageOrdered;
	}
	public void setCustomPageOrdered(boolean customPageOrdered) {
		this.customPageOrdered = customPageOrdered;
	}
	public boolean isEmpty() {
		return empty;
	}
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	public boolean isJoinable() {
		return joinable;
	}
	public void setJoinable(boolean joinable) {
		this.joinable = joinable;
	}
	public boolean isPubView() {
		return pubView;
	}
	public void setPubView(boolean pubView) {
		this.pubView = pubView;
	}
	public boolean isPublished() {
		return published;
	}
	public void setPublished(boolean published) {
		this.published = published;
	}
	public boolean isSoftlyDeleted() {
		return softlyDeleted;
	}
	public void setSoftlyDeleted(boolean softlyDeleted) {
		this.softlyDeleted = softlyDeleted;
	}
	public String getEntityReference() {
		return entityReference;
	}
	public void setEntityReference(String entityReference) {
		this.entityReference = entityReference;
	}
	public String getEntityURL() {
		return entityURL;
	}
	public void setEntityURL(String entityURL) {
		this.entityURL = entityURL;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityTitle() {
		return entityTitle;
	}
	public void setEntityTitle(String entityTitle) {
		this.entityTitle = entityTitle;
	}
	@Override
	public String toString() {
		return "SakaiSite [getCreatedDate()=" + getCreatedDate()
				+ ", getCreatedTime()=" + getCreatedTime()
				+ ", getDescription()=" + getDescription() + ", getIconUrl()="
				+ getIconUrl() + ", getIconUrlFull()=" + getIconUrlFull()
				+ ", getId()=" + getId() + ", getInfoUrl()=" + getInfoUrl()
				+ ", getInfoUrlFull()=" + getInfoUrlFull()
				+ ", getJoinerRole()=" + getJoinerRole()
				+ ", getLastModified()=" + getLastModified()
				+ ", getMaintainRole()=" + getMaintainRole()
				+ ", getModifiedDate()=" + getModifiedDate()
				+ ", getModifiedTime()=" + getModifiedTime() + ", getOwner()="
				+ getOwner() + ", getProps()=" + getProps()
				+ ", getProviderGroupId()=" + getProviderGroupId()
				+ ", getReference()=" + getReference()
				+ ", getShortDescription()=" + getShortDescription()
				+ ", getSiteGroups()=" + getSiteGroups() + ", getSiteOwner()="
				+ getSiteOwner() + ", getSkin()=" + getSkin()
				+ ", getSoftlyDeletedDate()=" + getSoftlyDeletedDate()
				+ ", getTitle()=" + getTitle() + ", getType()=" + getType()
				+ ", getUserRoles()=" + Arrays.toString(getUserRoles())
				+ ", isActiveEdit()=" + isActiveEdit()
				+ ", isCustomPageOrdered()=" + isCustomPageOrdered()
				+ ", isEmpty()=" + isEmpty() + ", isJoinable()=" + isJoinable()
				+ ", isPubView()=" + isPubView() + ", isPublished()="
				+ isPublished() + ", isSoftlyDeleted()=" + isSoftlyDeleted()
				+ ", getEntityReference()=" + getEntityReference()
				+ ", getEntityURL()=" + getEntityURL() + ", getEntityId()="
				+ getEntityId() + ", getEntityTitle()=" + getEntityTitle()
				+ "]";
	}
	
	
	
}

