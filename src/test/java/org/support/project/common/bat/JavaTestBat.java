package org.support.project.common.bat;

import net.arnx.jsonic.JSON;

public class JavaTestBat {

	public static void main(String[] args) {
		JobResult jobResult = new JobResult(100, "hogehoge");
		System.out.println(JSON.encode(jobResult));
	}

}
