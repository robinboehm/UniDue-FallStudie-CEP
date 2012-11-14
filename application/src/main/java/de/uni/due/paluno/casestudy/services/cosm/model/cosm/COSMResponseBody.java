package de.uni.due.paluno.casestudy.services.cosm.model.cosm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class COSMResponseBody {
	private String creator;
	private String status;
	private String created;
	private COSMFeedLocation location;
	private COSMDataStreamBody[] datastreams;
	private String feed;
	private String version;
	private String title;
	private String updated;
	private int id;
	@JsonProperty("private")
	private String _private;

	public String get_private() {
		return _private;
	}

	public void set_private(String _private) {
		this._private = _private;
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

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public COSMFeedLocation getLocation() {
		return location;
	}

	public void setLocation(COSMFeedLocation location) {
		this.location = location;
	}

	public COSMDataStreamBody[] getDatastreams() {
		return datastreams;
	}

	public void setDatastreams(COSMDataStreamBody[] datastreams) {
		this.datastreams = datastreams;
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

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
