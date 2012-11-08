package de.uni.due.paluno.casestudy.cosm.model.cosm;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class COSMResponseBody {
	private String feed;
	private String version;
	private String title;
	private String website;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("private")
	private boolean _private;
	private String updated;
	private String auto_feed_url;
	private int id;
	private String creator;
	private String status;
	private String description;
	private String created;
	private COSMDataStreamBody[] datastreams;
	private String[] tags;
	private Map<String, Object> location;

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public Map<String, Object> getLocation() {
		return location;
	}

	public void setLocation(Map<String, Object> location) {
		this.location = location;
	}

	public String getFeed() {
		return feed;
	}

	public void setFeed(String feed) {
		this.feed = feed;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public boolean is_private() {
		return _private;
	}

	public void set_private(boolean _private) {
		this._private = _private;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getAuto_feed_url() {
		return auto_feed_url;
	}

	public void setAuto_feed_url(String auto_feed_url) {
		this.auto_feed_url = auto_feed_url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public COSMDataStreamBody[] getDatastreams() {
		return datastreams;
	}

	public void setDatastreams(COSMDataStreamBody[] datastreams) {
		this.datastreams = datastreams;
	}
}
