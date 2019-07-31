
package com.sample.slowgrid;

public class GridEntry {

	private String content;

	public GridEntry(final String content) {
		this.setContent(content);
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(final String content) {
		this.content = content;
	}

}
