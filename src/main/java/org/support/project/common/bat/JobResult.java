package org.support.project.common.bat;

public class JobResult {
	
	private int resultCode;
	
	private String stdout;
	

	/**
	 * @param resultCode
	 * @param stdout
	 */
	public JobResult(int resultCode, String stdout) {
		super();
		this.resultCode = resultCode;
		this.stdout = stdout;
	}
	

	public int getResultCode() {
		return resultCode;
	}


	public String getStdout() {
		return stdout;
	}

	
	
}
