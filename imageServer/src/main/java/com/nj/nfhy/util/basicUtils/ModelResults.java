package com.nj.nfhy.util.basicUtils;

import java.io.Serializable;

public class ModelResults extends ModelBase implements Serializable {
	private Long start;// 记录开始的条数
	private Long end;// 记录结束条数
	private Long resultsCount; // 总记录数
	private Long page;// page当前页码
	private Long rows;// rows是当前页的行数
	private Long rowCount;
	private Integer pageSize;// pageSize每页的记录条数
	private String sql = "";
	private Object permissions;// 操作权限

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Long getRowCount() {
		return rowCount;
	}

	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * 分页初始化
	 * 
	 * @param pagesize
	 * @param page
	 */
	public ModelResults(int pagesize, Long page) {
		this.pageSize = pagesize;
		setPage(page);
	}

	public ModelResults() {

	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public Long getResultsCount() {
		return resultsCount;
	}

	public void setResultsCount(Long resultsCount) {
		if(resultsCount!=null){
			this.resultsCount = resultsCount;
//			Long rc = resultsCount / (long) pageSize;
//			if (resultsCount % pageSize > 0) {
//				rc++;
//			}
//			this.rowCount = rc;
		}
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Long page) {
		if(page!=null){
			if (page > 0) {
				this.page = page;
			} else {
				this.page = 1L;
			}
this.start = (this.page - 1) * this.pageSize;
			this.end = this.page * this.pageSize+1;
		}
	}

	public Long getRows() {
		return rows;
	}

	public void setRows(Long rows) {
		this.rows = rows;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	protected void redyToPrint() {
		this.param = null;
	}

	public Object getPermissions() {
		return permissions;
	}

	public void setPermissions(Object permissions) {
		this.permissions = permissions;
	}

}